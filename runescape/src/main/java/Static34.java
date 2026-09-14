import jaggl.OpenGL;
import org.openrs2.deob.annotation.OriginalArg;
import org.openrs2.deob.annotation.OriginalMember;
import org.openrs2.deob.annotation.Pc;

public final class Static34 {

    @OriginalMember(owner = "client!bca", name = "a", descriptor = "(II[BLclient!tca;)Lclient!ns;")
    public static Class265 compileShader(@OriginalArg(0) int shaderType, @OriginalArg(2) byte[] source, @OriginalArg(3) GlxToolkit toolkit) {
        if (source == null || source.length == 0) {
            return null;
        }
        @Pc(22) long shader = OpenGL.glCreateShaderObjectARB(shaderType);
        OpenGL.glShaderSourceRawARB(shader, source);
        OpenGL.glCompileShaderARB(shader);
        OpenGL.glGetObjectParameterivARB(shader, OpenGL.GL_COMPILE_STATUS, Static332.shaderStatus, 0);
        if (Static332.shaderStatus[0] == 0) {
            if (Static332.shaderStatus[0] == 0) {
                System.out.println("Shader compile failed:");
            }
            OpenGL.glGetObjectParameterivARB(shader, OpenGL.GL_INFO_LOG_LENGTH, Static332.shaderStatus, 1);
            if (Static332.shaderStatus[1] > 1) {
                @Pc(69) byte[] log = new byte[Static332.shaderStatus[1]];
                OpenGL.glGetInfoLogARB(shader, Static332.shaderStatus[1], Static332.shaderStatus, 0, log, 0);
                System.out.println(new String(log));
            }
            if (Static332.shaderStatus[0] == 0) {
                OpenGL.glDeleteObjectARB(shader);
                return null;
            }
        }
        return new Class265(toolkit, shader, shaderType);
    }

    /**
     * Walks one scanline of a triangle scan converted by {@link Static264#rasteriseTriangle}, from
     * pixel x0 up to but not including x1, interpolating the depth z by dzdx per pixel. The span is
     * clipped to the viewport width held in {@link Static228#anInt3709}, and index addresses the
     * start of the scanline's row in depthBuffer.
     * <p>
     * {@link Static254#occlusionMode} selects the mode: 1 keeps the nearer of the interpolated depth and
     * the depth already recorded, and always answers true; 2 leaves the buffer alone and answers
     * whether every pixel of the span lies behind the recorded depth, so false means part of the span
     * is visible.
     * <p>
     * The body is unrolled four pixels at a time, with the remaining one to three pixels handled by
     * the trailing loop.
     */
    @OriginalMember(owner = "client!bca", name = "a", descriptor = "(IIIZ[IIII)Z")
    public static boolean rasteriseScanline(@OriginalArg(0) int x0, @OriginalArg(1) int x1, @OriginalArg(2) int z, @OriginalArg(4) int[] depthBuffer, @OriginalArg(5) int dzdx, @OriginalArg(7) int index) {
        if (x0 < 0) {
            x0 = 0;
        }
        if (Static228.anInt3709 < x1) {
            x1 = Static228.anInt3709;
        }
        if (x1 <= x0) {
            return true;
        }
        z += x0 * dzdx;
        @Pc(41) int count = x1 - x0 >> 2;
        index += x0 - 1;
        @Pc(74) int nextZ;
        @Pc(61) int nextIndex;
        if (Static254.occlusionMode == 1) {
            Static432.occludedPixelCount += count;
            while (true) {
                count--;
                if (count < 0) {
                    count = x1 - x0 & 0x3;
                    while (true) {
                        count--;
                        if (count < 0) {
                            return true;
                        }
                        index++;
                        if (depthBuffer[index] > z) {
                            depthBuffer[index] = z;
                        }
                        z += dzdx;
                    }
                }
                nextIndex = index + 1;
                if (z < depthBuffer[nextIndex]) {
                    depthBuffer[nextIndex] = z;
                }
                nextZ = z + dzdx;
                nextIndex++;
                if (nextZ < depthBuffer[nextIndex]) {
                    depthBuffer[nextIndex] = nextZ;
                }
                nextZ += dzdx;
                nextIndex++;
                if (depthBuffer[nextIndex] > nextZ) {
                    depthBuffer[nextIndex] = nextZ;
                }
                nextZ += dzdx;
                index = nextIndex + 1;
                if (nextZ < depthBuffer[index]) {
                    depthBuffer[index] = nextZ;
                }
                z = nextZ + dzdx;
            }
        } else {
            z -= 38400;
            while (true) {
                count--;
                if (count < 0) {
                    count = x1 - x0 & 0x3;
                    while (true) {
                        count--;
                        if (count < 0) {
                            return true;
                        }
                        @Pc(246) int invertedZ = ~z;
                        index++;
                        if (invertedZ > ~depthBuffer[index]) {
                            return false;
                        }
                        z += dzdx;
                    }
                }
                nextIndex = index + 1;
                if (z < depthBuffer[nextIndex]) {
                    return false;
                }
                nextZ = z + dzdx;
                nextIndex++;
                if (depthBuffer[nextIndex] > nextZ) {
                    return false;
                }
                nextZ += dzdx;
                nextIndex++;
                if (depthBuffer[nextIndex] > nextZ) {
                    return false;
                }
                nextZ += dzdx;
                index = nextIndex + 1;
                if (depthBuffer[index] > nextZ) {
                    return false;
                }
                z = nextZ + dzdx;
            }
        }
    }
}
