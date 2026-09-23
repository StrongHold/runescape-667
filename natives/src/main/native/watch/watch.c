/*
 * Watches the shipped software toolkit call its own routines, and writes down what it hands them.
 *
 * The shipped toolkit reaches most of its rasteriser through stubs, and a stub reads the address
 * it jumps to out of a table. This library is inserted into the virtual machine before anything
 * else is loaded, waits for the toolkit to be opened, and writes its own address into that table
 * for each routine it is asked to watch. Every call then comes here first, is written down, and is
 * passed on to the routine untouched. Nothing about the toolkit on disk is changed.
 *
 * Which routines to watch is read from the file named by SW3D_WATCH_ROUTINES, one to a line:
 *
 *     <symbol> <before|after> <bytes of the first argument> <bytes of the second argument>
 *
 * A routine taking one argument is given a size of nought for the second. Each call is written to
 * the file named by SW3D_TRACE as the number of its line in the list and the bytes each argument
 * points at, in hexadecimal. The harness writes to the same file between scenes, and both open it
 * to append, so what each wrote lands in the order it was written.
 *
 * Reading the list waits until an image is loaded that holds one of the routines, because the
 * harness writes it after this library has been loaded and before it opens the toolkit.
 */

#include <dlfcn.h>
#include <fcntl.h>
#include <mach-o/dyld.h>
#include <mach-o/loader.h>
#include <mach-o/nlist.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

/** How many routines can be watched at once, which is how many stand-ins are written below. */
enum { SLOTS = 16 };

/** The longest name a routine can have. Every one the rasteriser has is well short of this. */
enum { NAME_LONGEST = 512 };

/** The most bytes written down for one argument. */
enum { ARGUMENT_LONGEST = 4096 };

typedef void (*Routine)(void *, void *);

typedef struct {
    char name[NAME_LONGEST];
    int after;
    size_t first;
    size_t second;
    Routine real;
} Slot;

static Slot slots[SLOTS];

static int slotsUsed;

static int listRead;

static int traced = -1;

static void writeAll(const char *text, size_t length) {
    while (length > 0) {
        ssize_t written = write(traced, text, length);
        if (written <= 0) {
            length = 0;
        } else {
            text += written;
            length -= (size_t) written;
        }
    }
}

static size_t hex(char *into, const void *from, size_t length) {
    static const char DIGITS[] = "0123456789abcdef";
    const unsigned char *bytes = from;

    for (size_t at = 0; at < length; at++) {
        into[at * 2] = DIGITS[bytes[at] >> 4];
        into[at * 2 + 1] = DIGITS[bytes[at] & 0xF];
    }
    return length * 2;
}

/**
 * Writes one call down as a single line, in a single write, so that nothing the harness writes
 * to the same file can land inside it.
 */
static void record(int which, const void *first, const void *second) {
    static char line[16 + ARGUMENT_LONGEST * 4 + 4];

    if (traced < 0) {
        return;
    }

    const Slot *slot = &slots[which];
    size_t length = (size_t) snprintf(line, 16, "W %d ", which);
    length += hex(line + length, first, slot->first);
    line[length++] = ' ';
    if (slot->second > 0) {
        length += hex(line + length, second, slot->second);
    } else {
        line[length++] = '-';
    }
    line[length++] = '\n';

    writeAll(line, length);
}

static void called(int which, void *first, void *second) {
    if (!slots[which].after) {
        record(which, first, second);
    }

    slots[which].real(first, second);

    if (slots[which].after) {
        record(which, first, second);
    }
}

/*
 * One stand-in for each slot. A table entry holds only an address, so each routine watched needs
 * a function of its own to say which slot it is. Passing both argument registers on to a routine
 * that takes one is harmless on x86_64, which is the only machine the shipped toolkit runs on.
 */
#define STAND_IN(n) static void standIn##n(void *first, void *second) { called(n, first, second); }
STAND_IN(0) STAND_IN(1) STAND_IN(2) STAND_IN(3) STAND_IN(4) STAND_IN(5) STAND_IN(6) STAND_IN(7)
STAND_IN(8) STAND_IN(9) STAND_IN(10) STAND_IN(11) STAND_IN(12) STAND_IN(13) STAND_IN(14)
STAND_IN(15)

static const Routine STAND_INS[SLOTS] = {
    standIn0, standIn1, standIn2, standIn3, standIn4, standIn5, standIn6, standIn7,
    standIn8, standIn9, standIn10, standIn11, standIn12, standIn13, standIn14, standIn15
};

static void readList(void) {
    const char *where = getenv("SW3D_WATCH_ROUTINES");
    FILE *list = where == NULL ? NULL : fopen(where, "r");

    if (list != NULL) {
        char when[16];
        while (slotsUsed < SLOTS
               && fscanf(list, "%511s %15s %zu %zu", slots[slotsUsed].name, when,
                         &slots[slotsUsed].first, &slots[slotsUsed].second) == 4) {
            Slot *slot = &slots[slotsUsed];
            slot->after = strcmp(when, "after") == 0;
            slot->first = slot->first > ARGUMENT_LONGEST ? ARGUMENT_LONGEST : slot->first;
            slot->second = slot->second > ARGUMENT_LONGEST ? ARGUMENT_LONGEST : slot->second;
            slotsUsed++;
        }
        fclose(list);
        listRead = 1;
    }

    const char *trace = getenv("SW3D_TRACE");
    if (listRead && trace != NULL && traced < 0) {
        traced = open(trace, O_WRONLY | O_CREAT | O_APPEND, 0644);
    }
}

/** Which slot watches a symbol, or -1 when none does. */
static int slotFor(const char *name) {
    int found = -1;
    for (int which = 0; which < slotsUsed && found < 0; which++) {
        if (strcmp(slots[which].name, name) == 0) {
            found = which;
        }
    }
    return found;
}

/**
 * Points every table entry in one section that names a watched routine at that routine's stand-in.
 *
 * The address already in the entry may be the binder that goes and finds the routine the first
 * time it is called rather than the routine itself, so the routine is asked for by name instead.
 */
static void rebindSection(const struct section_64 *table, intptr_t slide, const uint32_t *indirect,
                          const struct nlist_64 *symbols, const char *strings) {
    void **entries = (void **) ((uintptr_t) slide + table->addr);
    uint32_t count = (uint32_t) (table->size / sizeof(void *));

    for (uint32_t at = 0; at < count; at++) {
        uint32_t symbol = indirect[table->reserved1 + at];
        int local = symbol == INDIRECT_SYMBOL_ABS || symbol == INDIRECT_SYMBOL_LOCAL;
        const char *name = local ? NULL : strings + symbols[symbol].n_un.n_strx;
        int which = name == NULL ? -1 : slotFor(name);

        if (which >= 0) {
            slots[which].real = (Routine) dlsym(RTLD_DEFAULT, name + 1);
            entries[at] = (void *) STAND_INS[which];
        }
    }
}

static void imageAdded(const struct mach_header *plain, intptr_t slide) {
    if (!listRead) {
        readList();
    }

    const struct mach_header_64 *header = (const struct mach_header_64 *) plain;
    const struct symtab_command *names = NULL;
    const struct dysymtab_command *dynamic = NULL;
    uintptr_t linkedit = 0;

    const struct load_command *command = (const struct load_command *) (header + 1);
    for (uint32_t i = 0; i < header->ncmds; i++) {
        if (command->cmd == LC_SYMTAB) {
            names = (const struct symtab_command *) command;
        } else if (command->cmd == LC_DYSYMTAB) {
            dynamic = (const struct dysymtab_command *) command;
        } else if (command->cmd == LC_SEGMENT_64) {
            const struct segment_command_64 *segment = (const struct segment_command_64 *) command;
            if (strcmp(segment->segname, SEG_LINKEDIT) == 0) {
                linkedit = (uintptr_t) slide + segment->vmaddr - segment->fileoff;
            }
        }
        command = (const struct load_command *) ((uintptr_t) command + command->cmdsize);
    }

    if (slotsUsed > 0 && names != NULL && dynamic != NULL && linkedit != 0) {
        const struct nlist_64 *symbols = (const struct nlist_64 *) (linkedit + names->symoff);
        const char *strings = (const char *) (linkedit + names->stroff);
        const uint32_t *indirect = (const uint32_t *) (linkedit + dynamic->indirectsymoff);

        command = (const struct load_command *) (header + 1);
        for (uint32_t i = 0; i < header->ncmds; i++) {
            if (command->cmd == LC_SEGMENT_64) {
                const struct segment_command_64 *segment =
                    (const struct segment_command_64 *) command;
                const struct section_64 *part = (const struct section_64 *) (segment + 1);

                for (uint32_t s = 0; s < segment->nsects; s++, part++) {
                    uint32_t kind = part->flags & SECTION_TYPE;
                    if (kind == S_LAZY_SYMBOL_POINTERS || kind == S_NON_LAZY_SYMBOL_POINTERS) {
                        rebindSection(part, slide, indirect, symbols, strings);
                    }
                }
            }
            command = (const struct load_command *) ((uintptr_t) command + command->cmdsize);
        }
    }
}

__attribute__((constructor)) static void watchEverythingLoaded(void) {
    _dyld_register_func_for_add_image(imageAdded);
}
