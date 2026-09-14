import com.jagex.ParticleList;
import com.jagex.core.datastruct.Node2;
import com.jagex.math.IntMath;
import jaclib.memory.Buffer;
import jaclib.memory.Stream;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Draws the particles of a {@link ParticleList} as camera facing quads on the hardware toolkit.
 *
 * <p>Particles are depth sorted by bucketing them on their view space depth rather than by
 * comparison, so the bucket count is capped at {@link #BUCKET_COUNT} and the depth range is shifted
 * down to fit. A bucket holds {@link #BUCKET_CAPACITY} particles inline; beyond that it borrows one
 * of {@link #OVERFLOW_BUCKET_COUNT} shared overflow buckets, and particles past that are dropped.
 */
@OriginalClass("client!rc")
public final class NativeParticleRenderer {

    private static final int MAX_PARTICLES = 8191;

    private static final int VERTICES_PER_PARTICLE = 4;

    private static final int TRIANGLES_PER_PARTICLE = 2;

    private static final int INDICES_PER_PARTICLE = 6;

    private static final int VERTEX_STRIDE = 24;

    private static final int NORMAL_STRIDE = 12;

    private static final int VERTEX_BUFFER_CAPACITY = MAX_PARTICLES * VERTICES_PER_PARTICLE * VERTEX_STRIDE;

    private static final int NORMAL_BUFFER_CAPACITY = MAX_PARTICLES * VERTICES_PER_PARTICLE * NORMAL_STRIDE;

    private static final int INDEX_BUFFER_CAPACITY = MAX_PARTICLES * INDICES_PER_PARTICLE;

    private static final int BUCKET_COUNT = 1600;

    private static final int BUCKET_CAPACITY = 64;

    private static final int OVERFLOW_BUCKET_COUNT = 64;

    private static final int OVERFLOW_BUCKET_CAPACITY = 768;

    @OriginalMember(owner = "client!rc", name = "a", descriptor = "[[Lclient!up;")
    public final Particle[][] buckets = new Particle[BUCKET_COUNT][BUCKET_CAPACITY];

    @OriginalMember(owner = "client!rc", name = "s", descriptor = "[I")
    public final int[] overflowBucketSizes = new int[OVERFLOW_BUCKET_COUNT];

    @OriginalMember(owner = "client!rc", name = "n", descriptor = "[[Lclient!up;")
    public final Particle[][] overflowBuckets = new Particle[OVERFLOW_BUCKET_COUNT][OVERFLOW_BUCKET_CAPACITY];

    @OriginalMember(owner = "client!rc", name = "g", descriptor = "I")
    public int overflowBucketCount = 0;

    @OriginalMember(owner = "client!rc", name = "d", descriptor = "[I")
    public final int[] bucketSizes = new int[BUCKET_COUNT];

    @OriginalMember(owner = "client!rc", name = "c", descriptor = "[I")
    public final int[] depths = new int[MAX_PARTICLES];

    @OriginalMember(owner = "client!rc", name = "i", descriptor = "Lclient!mk;")
    public final Class244 vertexDeclaration;

    @OriginalMember(owner = "client!rc", name = "l", descriptor = "Lclient!mg;")
    public final Interface16 vertexBuffer;

    @OriginalMember(owner = "client!rc", name = "q", descriptor = "Lclient!mg;")
    public final Interface16 normalBuffer;

    @OriginalMember(owner = "client!rc", name = "b", descriptor = "Lclient!ri;")
    public final Interface20 indexBuffer;

    @OriginalMember(owner = "client!rc", name = "<init>", descriptor = "(Lclient!am;)V")
    public NativeParticleRenderer(@OriginalArg(0) NativeToolkit toolkit) {
        this.vertexDeclaration = toolkit.method8148(new Class237[]{new Class237(new Class157[]{Static231.aClass157_1, Static231.aClass157_3, Static231.aClass157_5}), new Class237(Static231.aClass157_2)});
        this.vertexBuffer = toolkit.method8156(true);
        this.normalBuffer = toolkit.method8156(false);
        this.normalBuffer.method3153(NORMAL_STRIDE, NORMAL_BUFFER_CAPACITY);
        this.indexBuffer = toolkit.method8122(false);
        this.indexBuffer.method8543(INDEX_BUFFER_CAPACITY);
        @Pc(96) Buffer indices = this.indexBuffer.method8547();
        if (indices != null) {
            @Pc(103) Stream stream = toolkit.method8100(indices);
            @Pc(112) int vertex;
            @Pc(107) int i;
            if (Stream.b()) {
                for (i = 0; i < MAX_PARTICLES; i++) {
                    vertex = i * VERTICES_PER_PARTICLE;
                    stream.b(vertex);
                    stream.b(vertex + 1);
                    stream.b(vertex + 2);
                    stream.b(vertex + 2);
                    stream.b(vertex + 3);
                    stream.b(vertex);
                }
            } else {
                for (i = 0; i < MAX_PARTICLES; i++) {
                    vertex = i * VERTICES_PER_PARTICLE;
                    stream.c(vertex);
                    stream.c(vertex + 1);
                    stream.c(vertex + 2);
                    stream.c(vertex + 2);
                    stream.c(vertex + 3);
                    stream.c(vertex);
                }
            }
            stream.c();
            this.indexBuffer.method8546();
        }
        @Pc(196) Buffer normals = this.normalBuffer.method3155();
        if (normals != null) {
            @Pc(203) Stream stream = toolkit.method8100(normals);
            if (Stream.b()) {
                for (int particle = 0; particle < MAX_PARTICLES; particle++) {
                    stream.a(0.0F);
                    stream.a(-1.0F);
                    stream.a(0.0F);
                    stream.a(0.0F);
                    stream.a(-1.0F);
                    stream.a(0.0F);
                    stream.a(0.0F);
                    stream.a(-1.0F);
                    stream.a(0.0F);
                    stream.a(0.0F);
                    stream.a(-1.0F);
                    stream.a(0.0F);
                }
            } else {
                for (int particle = 0; particle < MAX_PARTICLES; particle++) {
                    stream.b(0.0F);
                    stream.b(-1.0F);
                    stream.b(0.0F);
                    stream.b(0.0F);
                    stream.b(-1.0F);
                    stream.b(0.0F);
                    stream.b(0.0F);
                    stream.b(-1.0F);
                    stream.b(0.0F);
                    stream.b(0.0F);
                    stream.b(-1.0F);
                    stream.b(0.0F);
                }
            }
            stream.c();
            this.normalBuffer.method3154();
        }
    }

    @OriginalMember(owner = "client!rc", name = "a", descriptor = "(B)V")
    public void release() {
        this.vertexBuffer.method8538();
    }

    @OriginalMember(owner = "client!rc", name = "a", descriptor = "(Lclient!lk;IILclient!am;)V")
    public void render(@OriginalArg(0) ParticleList particles, @OriginalArg(1) int zoom, @OriginalArg(3) NativeToolkit toolkit) {
        if (toolkit.aClass73_Sub1_16 == null) {
            return;
        }
        if (zoom < 0) {
            this.beginRender(toolkit);
        } else {
            this.beginRenderOrtho(zoom, toolkit);
        }
        @Pc(25) float depthX = toolkit.aClass73_Sub1_16.aFloat45;
        @Pc(29) float depthY = toolkit.aClass73_Sub1_16.aFloat36;
        @Pc(33) float depthZ = toolkit.aClass73_Sub1_16.aFloat47;
        @Pc(37) float depthOffset = toolkit.aClass73_Sub1_16.aFloat43;
        try {
            @Pc(39) int index = 0;
            @Pc(41) int minDepth = Integer.MAX_VALUE;
            @Pc(43) int maxDepth = 0;
            @Pc(47) Node2 sentinel = particles.particles.sentinel;
            @Pc(50) Node2 node;
            for (node = sentinel.next2; node != sentinel; node = node.next2) {
                @Pc(54) Particle particle = (Particle) node;
                @Pc(81) int depth = (int) (depthY * (float) (particle.y >> 12) + depthX * (float) (particle.x >> 12) + depthZ * (float) (particle.z >> 12) + depthOffset);
                if (depth < minDepth) {
                    minDepth = depth;
                }
                if (maxDepth < depth) {
                    maxDepth = depth;
                }
                this.depths[index++] = depth;
            }
            @Pc(116) int bucketCount = maxDepth - minDepth;
            final int shift;
            if (bucketCount + 2 > BUCKET_COUNT) {
                shift = IntMath.countBits(bucketCount) + 1 - Static328.anInt5425;
                bucketCount = (bucketCount >> shift) + 2;
            } else {
                shift = 0;
                bucketCount += 2;
            }
            index = 0;
            node = sentinel.next2;
            @Pc(147) int texture = -2;
            @Pc(149) boolean preserveAmbient = true;
            @Pc(151) boolean batchStart = true;
            while (node != sentinel) {
                this.overflowBucketCount = 0;
                for (@Pc(157) int i = 0; i < bucketCount; i++) {
                    this.bucketSizes[i] = 0;
                }
                for (@Pc(169) int i = 0; i < OVERFLOW_BUCKET_COUNT; i++) {
                    this.overflowBucketSizes[i] = 0;
                }
                while (sentinel != node) {
                    @Pc(184) Particle particle = (Particle) node;
                    if (batchStart) {
                        preserveAmbient = particle.preserveAmbient;
                        batchStart = false;
                        texture = particle.texture;
                    }
                    if (index > 0 && (texture != particle.texture || particle.preserveAmbient != preserveAmbient)) {
                        batchStart = true;
                        break;
                    }
                    @Pc(227) int bucket = this.depths[index++] - minDepth >> shift;
                    if (bucket < BUCKET_COUNT) {
                        if (this.bucketSizes[bucket] < BUCKET_CAPACITY) {
                            this.buckets[bucket][this.bucketSizes[bucket]++] = particle;
                        } else {
                            if (this.bucketSizes[bucket] == BUCKET_CAPACITY && this.overflowBucketCount < OVERFLOW_BUCKET_COUNT) {
                                this.bucketSizes[bucket] += this.overflowBucketCount++ + 1;
                            }
                            if (this.bucketSizes[bucket] > BUCKET_CAPACITY) {
                                final int overflow = this.bucketSizes[bucket] - BUCKET_CAPACITY - 1;
                                this.overflowBuckets[overflow][this.overflowBucketSizes[overflow]++] = particle;
                            }
                        }
                    }
                    node = node.next2;
                }
                toolkit.method8108(false, texture < 0 ? -1 : texture, false);
                if (preserveAmbient && Static260.aFloat75 != toolkit.aFloat186) {
                    toolkit.xa(Static260.aFloat75);
                } else if (toolkit.aFloat186 != 1.0F) {
                    toolkit.xa(1.0F);
                }
                this.drawBatch(bucketCount, toolkit);
            }
        } catch (@Pc(370) Exception ex) {
            /* empty */
        }
        this.endRender(toolkit);
    }

    @OriginalMember(owner = "client!rc", name = "a", descriptor = "(ILclient!am;)V")
    public void endRender(@OriginalArg(1) NativeToolkit toolkit) {
        toolkit.method8083(true);
        toolkit.method8117(true);
        if (Static260.aFloat75 != toolkit.aFloat186) {
            toolkit.xa(Static260.aFloat75);
        }
    }

    @OriginalMember(owner = "client!rc", name = "a", descriptor = "(Lclient!am;I)V")
    public void createVertexBuffer(@OriginalArg(0) NativeToolkit toolkit) {
        this.vertexBuffer.method3153(VERTEX_STRIDE, VERTEX_BUFFER_CAPACITY);
    }

    @OriginalMember(owner = "client!rc", name = "a", descriptor = "(IBLclient!am;)V")
    public void beginRenderOrtho(@OriginalArg(0) int zoom, @OriginalArg(2) NativeToolkit toolkit) {
        Static260.aFloat75 = toolkit.aFloat186;
        toolkit.method8046((float) zoom);
        toolkit.method8111();
        toolkit.method8117(false);
        toolkit.method8083(false);
        toolkit.method8096();
    }

    @OriginalMember(owner = "client!rc", name = "a", descriptor = "(BLclient!am;)V")
    public void beginRender(@OriginalArg(1) NativeToolkit toolkit) {
        Static260.aFloat75 = toolkit.aFloat186;
        toolkit.method8044();
        toolkit.method8117(false);
        toolkit.method8083(false);
        toolkit.method8096();
    }

    @OriginalMember(owner = "client!rc", name = "a", descriptor = "(IZLclient!am;)V")
    public void drawBatch(@OriginalArg(0) int bucketCount, @OriginalArg(2) NativeToolkit toolkit) {
        @Pc(5) int quadCount = 0;
        @Pc(9) Matrix_Sub1 matrix = toolkit.method8154();
        @Pc(12) float rightX = matrix.aFloat39;
        @Pc(15) float rightY = matrix.aFloat40;
        @Pc(23) float rightZ = matrix.aFloat38;
        @Pc(26) float upX = matrix.aFloat44;
        @Pc(29) float upY = matrix.aFloat42;
        @Pc(32) float upZ = matrix.aFloat41;
        @Pc(36) float upRightX = upX + rightX;
        @Pc(40) float upRightY = upY + rightY;
        @Pc(44) float upRightZ = upZ + rightZ;
        @Pc(49) float downRightX = rightX - upX;
        @Pc(53) float downRightY = rightY - upY;
        @Pc(57) float downRightZ = rightZ - upZ;
        @Pc(62) float upLeftX = upX - rightX;
        @Pc(67) float upLeftY = upY - rightY;
        @Pc(71) float upLeftZ = upZ - rightZ;
        @Pc(77) Buffer vertices = this.vertexBuffer.method3155();
        if (vertices == null) {
            return;
        }
        @Pc(91) Stream stream = toolkit.method8100(vertices);
        @Pc(97) int bucket;
        @Pc(114) int count;
        @Pc(123) int i;
        @Pc(131) Particle particle;
        @Pc(134) int colour;
        @Pc(139) byte red;
        @Pc(144) byte green;
        @Pc(147) byte blue;
        @Pc(152) byte alpha;
        @Pc(158) float x;
        @Pc(164) float y;
        @Pc(170) float z;
        @Pc(175) int size;
        @Pc(449) float overflowZ;
        @Pc(454) int overflowSize;
        @Pc(395) int overflowBucket;
        @Pc(410) Particle overflowParticle;
        @Pc(413) int overflowColour;
        @Pc(426) byte overflowBlue;
        @Pc(431) byte overflowAlpha;
        @Pc(443) float overflowY;
        int overflowIndex;
        byte overflowRed;
        byte overflowGreen;
        float overflowX;
        if (Stream.b()) {
            for (bucket = bucketCount - 1; bucket >= 0; bucket--) {
                count = this.bucketSizes[bucket] > BUCKET_CAPACITY ? BUCKET_CAPACITY : this.bucketSizes[bucket];
                if (count > 0) {
                    for (i = count - 1; i >= 0; i--) {
                        particle = this.buckets[bucket][i];
                        colour = particle.colour;
                        red = (byte) (colour >> 16);
                        green = (byte) (colour >> 8);
                        blue = (byte) colour;
                        alpha = (byte) (colour >>> 24);
                        x = (float) (particle.x >> 12);
                        y = (float) (particle.y >> 12);
                        z = (float) (particle.z >> 12);
                        size = particle.size >> 12;
                        stream.a((float) -size * upRightX + x);
                        stream.a((float) -size * upRightY + y);
                        stream.a(z + (float) -size * upRightZ);
                        if (toolkit.anInt9178 == 0) {
                            stream.a(red, green, blue, alpha);
                        } else {
                            stream.b(red, green, blue, alpha);
                        }
                        stream.a(0.0F);
                        stream.a(0.0F);
                        stream.a(x + downRightX * (float) size);
                        stream.a(y + downRightY * (float) size);
                        stream.a(z + downRightZ * (float) size);
                        if (toolkit.anInt9178 == 0) {
                            stream.a(red, green, blue, alpha);
                        } else {
                            stream.b(red, green, blue, alpha);
                        }
                        stream.a(1.0F);
                        stream.a(0.0F);
                        stream.a((float) size * upRightX + x);
                        stream.a(y + (float) size * upRightY);
                        stream.a(z + (float) size * upRightZ);
                        if (toolkit.anInt9178 == 0) {
                            stream.a(red, green, blue, alpha);
                        } else {
                            stream.b(red, green, blue, alpha);
                        }
                        stream.a(1.0F);
                        stream.a(1.0F);
                        stream.a((float) size * upLeftX + x);
                        stream.a(upLeftY * (float) size + y);
                        stream.a(z + upLeftZ * (float) size);
                        if (toolkit.anInt9178 == 0) {
                            stream.a(red, green, blue, alpha);
                        } else {
                            stream.b(red, green, blue, alpha);
                        }
                        stream.a(0.0F);
                        stream.a(1.0F);
                        quadCount++;
                    }
                    if (this.bucketSizes[bucket] > BUCKET_CAPACITY) {
                        overflowBucket = this.bucketSizes[bucket] - BUCKET_CAPACITY - 1;
                        for (overflowIndex = this.overflowBucketSizes[overflowBucket] - 1; overflowIndex >= 0; overflowIndex--) {
                            overflowParticle = this.overflowBuckets[overflowBucket][overflowIndex];
                            overflowColour = overflowParticle.colour;
                            overflowRed = (byte) (overflowColour >> 16);
                            overflowGreen = (byte) (overflowColour >> 8);
                            overflowBlue = (byte) overflowColour;
                            overflowAlpha = (byte) (overflowColour >>> 24);
                            overflowX = (float) (overflowParticle.x >> 12);
                            overflowY = (float) (overflowParticle.y >> 12);
                            overflowZ = (float) (overflowParticle.z >> 12);
                            overflowSize = overflowParticle.size >> 12;
                            stream.a(upRightX * (float) -overflowSize + overflowX);
                            stream.a(overflowY + (float) -overflowSize * upRightY);
                            stream.a((float) -overflowSize * upRightZ + overflowZ);
                            if (toolkit.anInt9178 == 0) {
                                stream.a(overflowRed, overflowGreen, overflowBlue, overflowAlpha);
                            } else {
                                stream.b(overflowRed, overflowGreen, overflowBlue, overflowAlpha);
                            }
                            stream.a(0.0F);
                            stream.a(0.0F);
                            stream.a((float) overflowSize * downRightX + overflowX);
                            stream.a(downRightY * (float) overflowSize + overflowY);
                            stream.a(overflowZ + (float) overflowSize * downRightZ);
                            if (toolkit.anInt9178 == 0) {
                                stream.a(overflowRed, overflowGreen, overflowBlue, overflowAlpha);
                            } else {
                                stream.b(overflowRed, overflowGreen, overflowBlue, overflowAlpha);
                            }
                            stream.a(1.0F);
                            stream.a(0.0F);
                            stream.a((float) overflowSize * upRightX + overflowX);
                            stream.a(overflowY + upRightY * (float) overflowSize);
                            stream.a(upRightZ * (float) overflowSize + overflowZ);
                            if (toolkit.anInt9178 == 0) {
                                stream.a(overflowRed, overflowGreen, overflowBlue, overflowAlpha);
                            } else {
                                stream.b(overflowRed, overflowGreen, overflowBlue, overflowAlpha);
                            }
                            stream.a(1.0F);
                            stream.a(1.0F);
                            stream.a(upLeftX * (float) overflowSize + overflowX);
                            stream.a(overflowY + upLeftY * (float) overflowSize);
                            stream.a((float) overflowSize * upLeftZ + overflowZ);
                            if (toolkit.anInt9178 == 0) {
                                stream.a(overflowRed, overflowGreen, overflowBlue, overflowAlpha);
                            } else {
                                stream.b(overflowRed, overflowGreen, overflowBlue, overflowAlpha);
                            }
                            stream.a(0.0F);
                            quadCount++;
                            stream.a(1.0F);
                        }
                    }
                }
            }
        } else {
            for (bucket = bucketCount - 1; bucket >= 0; bucket--) {
                count = this.bucketSizes[bucket] > BUCKET_CAPACITY ? BUCKET_CAPACITY : this.bucketSizes[bucket];
                if (count > 0) {
                    for (i = count - 1; i >= 0; i--) {
                        particle = this.buckets[bucket][i];
                        colour = particle.colour;
                        red = (byte) (colour >> 16);
                        green = (byte) (colour >> 8);
                        blue = (byte) colour;
                        alpha = (byte) (colour >>> 24);
                        x = (float) (particle.x >> 12);
                        y = (float) (particle.y >> 12);
                        z = (float) (particle.z >> 12);
                        size = particle.size >> 12;
                        stream.b(upRightX * (float) -size + x);
                        stream.b(y + (float) -size * upRightY);
                        stream.b(z + upRightZ * (float) -size);
                        if (toolkit.anInt9178 == 0) {
                            stream.a(red, green, blue, alpha);
                        } else {
                            stream.b(red, green, blue, alpha);
                        }
                        stream.b(0.0F);
                        stream.b(0.0F);
                        stream.b(x + downRightX * (float) size);
                        stream.b(downRightY * (float) size + y);
                        stream.b(downRightZ * (float) size + z);
                        if (toolkit.anInt9178 == 0) {
                            stream.a(red, green, blue, alpha);
                        } else {
                            stream.b(red, green, blue, alpha);
                        }
                        stream.b(1.0F);
                        stream.b(0.0F);
                        stream.b(x + upRightX * (float) size);
                        stream.b(upRightY * (float) size + y);
                        stream.b(z + (float) size * upRightZ);
                        if (toolkit.anInt9178 == 0) {
                            stream.a(red, green, blue, alpha);
                        } else {
                            stream.b(red, green, blue, alpha);
                        }
                        stream.b(1.0F);
                        stream.b(1.0F);
                        stream.b(x + upLeftX * (float) size);
                        stream.b((float) size * upLeftY + y);
                        stream.b(z + upLeftZ * (float) size);
                        if (toolkit.anInt9178 == 0) {
                            stream.a(red, green, blue, alpha);
                        } else {
                            stream.b(red, green, blue, alpha);
                        }
                        stream.b(0.0F);
                        stream.b(1.0F);
                        quadCount++;
                    }
                    if (this.bucketSizes[bucket] > BUCKET_CAPACITY) {
                        overflowBucket = this.bucketSizes[bucket] - BUCKET_CAPACITY - 1;
                        for (overflowIndex = this.overflowBucketSizes[overflowBucket] - 1; overflowIndex >= 0; overflowIndex--) {
                            overflowParticle = this.overflowBuckets[overflowBucket][overflowIndex];
                            overflowColour = overflowParticle.colour;
                            overflowRed = (byte) (overflowColour >> 16);
                            overflowGreen = (byte) (overflowColour >> 8);
                            overflowBlue = (byte) overflowColour;
                            overflowAlpha = (byte) (overflowColour >>> 24);
                            overflowX = (float) (overflowParticle.x >> 12);
                            overflowY = (float) (overflowParticle.y >> 12);
                            overflowZ = (float) (overflowParticle.z >> 12);
                            overflowSize = overflowParticle.size >> 12;
                            stream.b(upRightX * (float) -overflowSize + overflowX);
                            stream.b(overflowY + (float) -overflowSize * upRightY);
                            stream.b(upRightZ * (float) -overflowSize + overflowZ);
                            if (toolkit.anInt9178 == 0) {
                                stream.a(overflowRed, overflowGreen, overflowBlue, overflowAlpha);
                            } else {
                                stream.b(overflowRed, overflowGreen, overflowBlue, overflowAlpha);
                            }
                            stream.b(0.0F);
                            stream.b(0.0F);
                            stream.b((float) overflowSize * downRightX + overflowX);
                            stream.b(overflowY + downRightY * (float) overflowSize);
                            stream.b(overflowZ + (float) overflowSize * downRightZ);
                            if (toolkit.anInt9178 == 0) {
                                stream.a(overflowRed, overflowGreen, overflowBlue, overflowAlpha);
                            } else {
                                stream.b(overflowRed, overflowGreen, overflowBlue, overflowAlpha);
                            }
                            stream.b(1.0F);
                            stream.b(0.0F);
                            stream.b(overflowX + (float) overflowSize * upRightX);
                            stream.b((float) overflowSize * upRightY + overflowY);
                            stream.b((float) overflowSize * upRightZ + overflowZ);
                            if (toolkit.anInt9178 == 0) {
                                stream.a(overflowRed, overflowGreen, overflowBlue, overflowAlpha);
                            } else {
                                stream.b(overflowRed, overflowGreen, overflowBlue, overflowAlpha);
                            }
                            stream.b(1.0F);
                            stream.b(1.0F);
                            stream.b(upLeftX * (float) overflowSize + overflowX);
                            stream.b(upLeftY * (float) overflowSize + overflowY);
                            stream.b((float) overflowSize * upLeftZ + overflowZ);
                            if (toolkit.anInt9178 == 0) {
                                stream.a(overflowRed, overflowGreen, overflowBlue, overflowAlpha);
                            } else {
                                stream.b(overflowRed, overflowGreen, overflowBlue, overflowAlpha);
                            }
                            stream.b(0.0F);
                            stream.b(1.0F);
                            quadCount++;
                        }
                    }
                }
            }
        }
        stream.c();
        if (this.vertexBuffer.method3154()) {
            toolkit.method8130(0, this.vertexBuffer);
            toolkit.method8130(1, this.normalBuffer);
            toolkit.method8114(this.vertexDeclaration);
            toolkit.method8052(0, 0, this.indexBuffer, quadCount * VERTICES_PER_PARTICLE, Static104.aClass131_2, quadCount * TRIANGLES_PER_PARTICLE);
        }
    }
}
