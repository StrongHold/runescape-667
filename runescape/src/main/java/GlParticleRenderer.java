import com.jagex.ParticleList;
import com.jagex.core.datastruct.Node2;
import com.jagex.math.IntMath;
import jaggl.OpenGL;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalClass;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

/**
 * Draws the particles of a {@link ParticleList} as camera facing quads on the OpenGL toolkit.
 *
 * <p>Particles are depth sorted by bucketing them on their view space depth rather than by
 * comparison, so the bucket count is capped at {@link #BUCKET_COUNT} and the depth range is shifted
 * down to fit. A bucket holds {@link #BUCKET_CAPACITY} particles inline; beyond that it borrows one
 * of {@link #OVERFLOW_BUCKET_COUNT} shared overflow buckets, and particles past that are dropped.
 */
@OriginalClass("client!bj")
public final class GlParticleRenderer {

    private static final int MAX_PARTICLES = 8191;

    private static final int VERTICES_PER_PARTICLE = 4;

    private static final int VERTEX_STRIDE = 24;

    private static final int VERTEX_DATA_CAPACITY = MAX_PARTICLES * VERTICES_PER_PARTICLE * VERTEX_STRIDE;

    private static final int VERTEX_BUFFER_CAPACITY = MAX_PARTICLES * VERTEX_STRIDE;

    private static final int TEX_COORD_OFFSET = 0;

    private static final int POSITION_OFFSET = 8;

    private static final int COLOUR_OFFSET = 20;

    private static final int BUCKET_COUNT = 1600;

    private static final int BUCKET_CAPACITY = 64;

    private static final int OVERFLOW_BUCKET_COUNT = 64;

    private static final int OVERFLOW_BUCKET_CAPACITY = 768;

    @OriginalMember(owner = "client!bj", name = "f", descriptor = "Lclient!ed;")
    public Class94 positionPointer;

    @OriginalMember(owner = "client!bj", name = "h", descriptor = "Lclient!jc;")
    public Interface12 vertexBuffer;

    @OriginalMember(owner = "client!bj", name = "b", descriptor = "Lclient!ed;")
    public Class94 colourPointer;

    @OriginalMember(owner = "client!bj", name = "c", descriptor = "Lclient!ed;")
    public Class94 texCoordPointer;

    @OriginalMember(owner = "client!bj", name = "k", descriptor = "[F")
    public final float[] modelViewMatrix = new float[16];

    @OriginalMember(owner = "client!bj", name = "t", descriptor = "Lclient!jfa;")
    public final Node_Sub21_Sub1 vertices = new Node_Sub21_Sub1(VERTEX_DATA_CAPACITY);

    @OriginalMember(owner = "client!bj", name = "r", descriptor = "I")
    public final int bucketCountBits = IntMath.countBits(BUCKET_COUNT);

    @OriginalMember(owner = "client!bj", name = "s", descriptor = "[[Lclient!up;")
    public final Particle[][] overflowBuckets = new Particle[OVERFLOW_BUCKET_COUNT][OVERFLOW_BUCKET_CAPACITY];

    @OriginalMember(owner = "client!bj", name = "n", descriptor = "[I")
    public final int[] bucketSizes = new int[BUCKET_COUNT];

    @OriginalMember(owner = "client!bj", name = "j", descriptor = "[[Lclient!up;")
    public final Particle[][] buckets = new Particle[BUCKET_COUNT][BUCKET_CAPACITY];

    @OriginalMember(owner = "client!bj", name = "a", descriptor = "I")
    public int overflowBucketCount = 0;

    @OriginalMember(owner = "client!bj", name = "l", descriptor = "[I")
    public final int[] depths = new int[MAX_PARTICLES];

    @OriginalMember(owner = "client!bj", name = "q", descriptor = "[I")
    public final int[] overflowBucketSizes = new int[OVERFLOW_BUCKET_COUNT];

    @OriginalMember(owner = "client!bj", name = "a", descriptor = "(ZLclient!qha;)V")
    public void beginRender(@OriginalArg(1) GlToolkit toolkit) {
        Static481.aFloat124 = toolkit.aFloat149;
        toolkit.method7027();
        OpenGL.glDisable(OpenGL.GL_LIGHT0);
        OpenGL.glDisable(OpenGL.GL_LIGHT1);
        toolkit.method6972(false);
        OpenGL.glNormal3f(0.0F, -1.0F, 0.0F);
    }

    @OriginalMember(owner = "client!bj", name = "a", descriptor = "(BLclient!qha;I)V")
    public void drawBatch(@OriginalArg(1) GlToolkit toolkit, @OriginalArg(2) int bucketCount) {
        OpenGL.glGetFloatv(OpenGL.GL_MODELVIEW_MATRIX, this.modelViewMatrix, 0);
        @Pc(15) float rightX = this.modelViewMatrix[0];
        @Pc(20) float rightY = this.modelViewMatrix[4];
        @Pc(35) float rightZ = this.modelViewMatrix[8];
        @Pc(40) float upX = this.modelViewMatrix[1];
        @Pc(45) float upY = this.modelViewMatrix[5];
        @Pc(50) float upZ = this.modelViewMatrix[9];
        @Pc(54) float upRightX = upX + rightX;
        @Pc(58) float upRightY = upY + rightY;
        @Pc(62) float upRightZ = upZ + rightZ;
        @Pc(66) float downRightX = rightX - upX;
        @Pc(71) float downRightY = rightY - upY;
        @Pc(75) float downRightZ = rightZ - upZ;
        @Pc(80) float upLeftX = upX - rightX;
        @Pc(84) float upLeftY = upY - rightY;
        @Pc(88) float upLeftZ = upZ - rightZ;
        this.vertices.pos = 0;
        @Pc(99) int bucket;
        @Pc(117) int count;
        @Pc(123) int i;
        @Pc(134) Particle particle;
        @Pc(137) int colour;
        @Pc(142) byte red;
        @Pc(147) byte green;
        @Pc(150) byte blue;
        @Pc(155) byte alpha;
        @Pc(161) float x;
        @Pc(167) float y;
        @Pc(173) float z;
        @Pc(178) int size;
        @Pc(503) float overflowZ;
        @Pc(508) int overflowSize;
        @Pc(448) int overflowBucket;
        @Pc(464) Particle overflowParticle;
        @Pc(467) int overflowColour;
        @Pc(480) byte overflowBlue;
        @Pc(485) byte overflowAlpha;
        @Pc(497) float overflowY;
        int overflowIndex;
        byte overflowRed;
        byte overflowGreen;
        float overflowX;
        if (toolkit.aBoolean618) {
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
                        this.vertices.method4336(0.0F);
                        this.vertices.method4336(0.0F);
                        this.vertices.method4336(x + (float) -size * upRightX);
                        this.vertices.method4336((float) -size * upRightY + y);
                        this.vertices.method4336((float) -size * upRightZ + z);
                        this.vertices.p1(red);
                        this.vertices.p1(green);
                        this.vertices.p1(blue);
                        this.vertices.p1(alpha);
                        this.vertices.method4336(1.0F);
                        this.vertices.method4336(0.0F);
                        this.vertices.method4336(x + (float) size * downRightX);
                        this.vertices.method4336((float) size * downRightY + y);
                        this.vertices.method4336(z + (float) size * downRightZ);
                        this.vertices.p1(red);
                        this.vertices.p1(green);
                        this.vertices.p1(blue);
                        this.vertices.p1(alpha);
                        this.vertices.method4336(1.0F);
                        this.vertices.method4336(1.0F);
                        this.vertices.method4336(upRightX * (float) size + x);
                        this.vertices.method4336(y + (float) size * upRightY);
                        this.vertices.method4336(z + upRightZ * (float) size);
                        this.vertices.p1(red);
                        this.vertices.p1(green);
                        this.vertices.p1(blue);
                        this.vertices.p1(alpha);
                        this.vertices.method4336(0.0F);
                        this.vertices.method4336(1.0F);
                        this.vertices.method4336(upLeftX * (float) size + x);
                        this.vertices.method4336(y + (float) size * upLeftY);
                        this.vertices.method4336(z + upLeftZ * (float) size);
                        this.vertices.p1(red);
                        this.vertices.p1(green);
                        this.vertices.p1(blue);
                        this.vertices.p1(alpha);
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
                            this.vertices.method4336(0.0F);
                            this.vertices.method4336(0.0F);
                            this.vertices.method4336((float) -overflowSize * upRightX + overflowX);
                            this.vertices.method4336(upRightY * (float) -overflowSize + overflowY);
                            this.vertices.method4336((float) -overflowSize * upRightZ + overflowZ);
                            this.vertices.p1(overflowRed);
                            this.vertices.p1(overflowGreen);
                            this.vertices.p1(overflowBlue);
                            this.vertices.p1(overflowAlpha);
                            this.vertices.method4336(1.0F);
                            this.vertices.method4336(0.0F);
                            this.vertices.method4336((float) overflowSize * downRightX + overflowX);
                            this.vertices.method4336(overflowY + downRightY * (float) overflowSize);
                            this.vertices.method4336((float) overflowSize * downRightZ + overflowZ);
                            this.vertices.p1(overflowRed);
                            this.vertices.p1(overflowGreen);
                            this.vertices.p1(overflowBlue);
                            this.vertices.p1(overflowAlpha);
                            this.vertices.method4336(1.0F);
                            this.vertices.method4336(1.0F);
                            this.vertices.method4336(overflowX + upRightX * (float) overflowSize);
                            this.vertices.method4336(overflowY + upRightY * (float) overflowSize);
                            this.vertices.method4336(upRightZ * (float) overflowSize + overflowZ);
                            this.vertices.p1(overflowRed);
                            this.vertices.p1(overflowGreen);
                            this.vertices.p1(overflowBlue);
                            this.vertices.p1(overflowAlpha);
                            this.vertices.method4336(0.0F);
                            this.vertices.method4336(1.0F);
                            this.vertices.method4336(overflowX + upLeftX * (float) overflowSize);
                            this.vertices.method4336(overflowY + (float) overflowSize * upLeftY);
                            this.vertices.method4336(upLeftZ * (float) overflowSize + overflowZ);
                            this.vertices.p1(overflowRed);
                            this.vertices.p1(overflowGreen);
                            this.vertices.p1(overflowBlue);
                            this.vertices.p1(overflowAlpha);
                        }
                    }
                }
            }
        } else {
            for (bucket = bucketCount - 1; bucket >= 0; bucket--) {
                count = this.bucketSizes[bucket] <= BUCKET_CAPACITY ? this.bucketSizes[bucket] : BUCKET_CAPACITY;
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
                        this.vertices.method4337(0.0F);
                        this.vertices.method4337(0.0F);
                        this.vertices.method4337(upRightX * (float) -size + x);
                        this.vertices.method4337((float) -size * upRightY + y);
                        this.vertices.method4337(upRightZ * (float) -size + z);
                        this.vertices.p1(red);
                        this.vertices.p1(green);
                        this.vertices.p1(blue);
                        this.vertices.p1(alpha);
                        this.vertices.method4337(1.0F);
                        this.vertices.method4337(0.0F);
                        this.vertices.method4337(downRightX * (float) size + x);
                        this.vertices.method4337(y + (float) size * downRightY);
                        this.vertices.method4337(z + downRightZ * (float) size);
                        this.vertices.p1(red);
                        this.vertices.p1(green);
                        this.vertices.p1(blue);
                        this.vertices.p1(alpha);
                        this.vertices.method4337(1.0F);
                        this.vertices.method4337(1.0F);
                        this.vertices.method4337(upRightX * (float) size + x);
                        this.vertices.method4337((float) size * upRightY + y);
                        this.vertices.method4337(z + (float) size * upRightZ);
                        this.vertices.p1(red);
                        this.vertices.p1(green);
                        this.vertices.p1(blue);
                        this.vertices.p1(alpha);
                        this.vertices.method4337(0.0F);
                        this.vertices.method4337(1.0F);
                        this.vertices.method4337((float) size * upLeftX + x);
                        this.vertices.method4337(y + (float) size * upLeftY);
                        this.vertices.method4337((float) size * upLeftZ + z);
                        this.vertices.p1(red);
                        this.vertices.p1(green);
                        this.vertices.p1(blue);
                        this.vertices.p1(alpha);
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
                            this.vertices.method4337(0.0F);
                            this.vertices.method4337(0.0F);
                            this.vertices.method4337(overflowX + upRightX * (float) -overflowSize);
                            this.vertices.method4337(upRightY * (float) -overflowSize + overflowY);
                            this.vertices.method4337((float) -overflowSize * upRightZ + overflowZ);
                            this.vertices.p1(overflowRed);
                            this.vertices.p1(overflowGreen);
                            this.vertices.p1(overflowBlue);
                            this.vertices.p1(overflowAlpha);
                            this.vertices.method4337(1.0F);
                            this.vertices.method4337(0.0F);
                            this.vertices.method4337(downRightX * (float) overflowSize + overflowX);
                            this.vertices.method4337((float) overflowSize * downRightY + overflowY);
                            this.vertices.method4337(downRightZ * (float) overflowSize + overflowZ);
                            this.vertices.p1(overflowRed);
                            this.vertices.p1(overflowGreen);
                            this.vertices.p1(overflowBlue);
                            this.vertices.p1(overflowAlpha);
                            this.vertices.method4337(1.0F);
                            this.vertices.method4337(1.0F);
                            this.vertices.method4337(overflowX + upRightX * (float) overflowSize);
                            this.vertices.method4337((float) overflowSize * upRightY + overflowY);
                            this.vertices.method4337((float) overflowSize * upRightZ + overflowZ);
                            this.vertices.p1(overflowRed);
                            this.vertices.p1(overflowGreen);
                            this.vertices.p1(overflowBlue);
                            this.vertices.p1(overflowAlpha);
                            this.vertices.method4337(0.0F);
                            this.vertices.method4337(1.0F);
                            this.vertices.method4337(overflowX + upLeftX * (float) overflowSize);
                            this.vertices.method4337(upLeftY * (float) overflowSize + overflowY);
                            this.vertices.method4337(overflowZ + (float) overflowSize * upLeftZ);
                            this.vertices.p1(overflowRed);
                            this.vertices.p1(overflowGreen);
                            this.vertices.p1(overflowBlue);
                            this.vertices.p1(overflowAlpha);
                        }
                    }
                }
            }
        }
        if (this.vertices.pos != 0) {
            this.vertexBuffer.method5002(this.vertices.data, this.vertices.pos, VERTEX_STRIDE);
            toolkit.method7039(this.colourPointer, null, this.positionPointer, this.texCoordPointer);
            toolkit.method6998(this.vertices.pos / VERTEX_STRIDE);
        }
    }

    @OriginalMember(owner = "client!bj", name = "a", descriptor = "(ILclient!qha;)V")
    public void endRender(@OriginalArg(1) GlToolkit toolkit) {
        toolkit.method6972(true);
        OpenGL.glEnable(OpenGL.GL_LIGHT0);
        OpenGL.glEnable(OpenGL.GL_LIGHT1);
        if (toolkit.aFloat149 != Static481.aFloat124) {
            toolkit.xa(Static481.aFloat124);
        }
    }

    @OriginalMember(owner = "client!bj", name = "a", descriptor = "(ILclient!qha;I)V")
    public void beginRenderOrtho(@OriginalArg(0) int zoom, @OriginalArg(1) GlToolkit toolkit) {
        Static481.aFloat124 = toolkit.aFloat149;
        toolkit.method6964((float) zoom);
        toolkit.method6978();
        OpenGL.glDisable(OpenGL.GL_LIGHT0);
        OpenGL.glDisable(OpenGL.GL_LIGHT1);
        toolkit.method6972(false);
        OpenGL.glNormal3f(0.0F, -1.0F, 0.0F);
    }

    @OriginalMember(owner = "client!bj", name = "a", descriptor = "(Lclient!qha;Lclient!lk;IZ)V")
    public void render(@OriginalArg(0) GlToolkit toolkit, @OriginalArg(1) ParticleList particles, @OriginalArg(2) int zoom) {
        if (toolkit.aClass73_Sub3_3 == null) {
            return;
        }
        if (zoom >= 0) {
            this.beginRenderOrtho(zoom, toolkit);
        } else {
            this.beginRender(toolkit);
        }
        @Pc(34) float depthX = toolkit.aClass73_Sub3_3.aFloat155;
        @Pc(38) float depthY = toolkit.aClass73_Sub3_3.aFloat151;
        @Pc(42) float depthZ = toolkit.aClass73_Sub3_3.aFloat154;
        @Pc(46) float depthOffset = toolkit.aClass73_Sub3_3.aFloat159;
        try {
            @Pc(48) int index = 0;
            @Pc(50) int minDepth = Integer.MAX_VALUE;
            @Pc(52) int maxDepth = 0;
            @Pc(56) Node2 sentinel = particles.particles.sentinel;
            @Pc(59) Node2 node;
            for (node = sentinel.next2; node != sentinel; node = node.next2) {
                @Pc(64) Particle particle = (Particle) node;
                @Pc(91) int depth = (int) (depthOffset + (depthZ * (float) (particle.z >> 12) + ((float) (particle.x >> 12) * depthX + (float) (particle.y >> 12) * depthY)));
                if (depth > maxDepth) {
                    maxDepth = depth;
                }
                this.depths[index++] = depth;
                if (minDepth > depth) {
                    minDepth = depth;
                }
            }
            @Pc(118) int bucketCount = maxDepth - minDepth;
            final int shift;
            if (bucketCount + 2 <= BUCKET_COUNT) {
                shift = 0;
                bucketCount += 2;
            } else {
                shift = IntMath.countBits(bucketCount) + 1 - this.bucketCountBits;
                bucketCount = (bucketCount >> shift) + 2;
            }
            index = 0;
            node = sentinel.next2;
            @Pc(152) int texture = -2;
            @Pc(154) boolean preserveAmbient = true;
            @Pc(156) boolean batchStart = true;
            while (node != sentinel) {
                this.overflowBucketCount = 0;
                for (@Pc(165) int i = 0; i < bucketCount; i++) {
                    this.bucketSizes[i] = 0;
                }
                for (@Pc(184) int i = 0; i < OVERFLOW_BUCKET_COUNT; i++) {
                    this.overflowBucketSizes[i] = 0;
                }
                while (node != sentinel) {
                    @Pc(210) Particle particle = (Particle) node;
                    if (batchStart) {
                        preserveAmbient = particle.preserveAmbient;
                        texture = particle.texture;
                        batchStart = false;
                    }
                    if (index > 0 && (particle.texture != texture || particle.preserveAmbient != preserveAmbient)) {
                        batchStart = true;
                        break;
                    }
                    @Pc(257) int bucket = this.depths[index++] - minDepth >> shift;
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
                if (texture >= 0) {
                    toolkit.method7046(texture);
                } else {
                    toolkit.method7046(-1);
                }
                if (preserveAmbient && Static481.aFloat124 != toolkit.aFloat149) {
                    toolkit.xa(Static481.aFloat124);
                } else if (toolkit.aFloat149 != 1.0F) {
                    toolkit.xa(1.0F);
                }
                this.drawBatch(toolkit, bucketCount);
            }
        } catch (@Pc(421) Exception ex) {
            /* empty */
        }
        this.endRender(toolkit);
    }

    @OriginalMember(owner = "client!bj", name = "b", descriptor = "(ILclient!qha;)V")
    public void createVertexBuffer(@OriginalArg(1) GlToolkit toolkit) {
        this.vertexBuffer = toolkit.method7024(true, VERTEX_BUFFER_CAPACITY, null, VERTEX_STRIDE);
        this.texCoordPointer = new Class94(this.vertexBuffer, OpenGL.GL_FLOAT, 2, TEX_COORD_OFFSET);
        this.positionPointer = new Class94(this.vertexBuffer, OpenGL.GL_FLOAT, 3, POSITION_OFFSET);
        this.colourPointer = new Class94(this.vertexBuffer, OpenGL.GL_UNSIGNED_BYTE, 4, COLOUR_OFFSET);
    }
}
