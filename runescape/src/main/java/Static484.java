import org.openrs2.deob.annotation.OriginalMember;

public final class Static484 {

    // $FF: synthetic field
    @OriginalMember(owner = "client!pea", name = "n", descriptor = "Ljava/lang/Class;")
    public static Class locClass;

    static Class getClass(String name) {
        Class instance;
        try {
            instance = Class.forName(name);
        } catch (ClassNotFoundException ex) {
            throw (NoClassDefFoundError) new NoClassDefFoundError().initCause(ex);
        }
        return instance;
    }
}
