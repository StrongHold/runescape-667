import org.openrs2.deob.annotation.OriginalMember;

public final class ShaderQuery {

    /**
     * Scratch buffer for the shader queries in {@link Static34#compileShader}: slot 0 takes the
     * compile status and slot 1 the length of the info log.
     */
    @OriginalMember(owner = "client!kia", name = "m", descriptor = "[I")
    public static final int[] results = new int[2];
}
