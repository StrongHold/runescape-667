import org.openrs2.deob.annotation.OriginalMember;

public final class Static332 {

    /**
     * Scratch buffer for the shader queries in {@link Static34#compileShader}: slot 0 takes the
     * compile status and slot 1 the length of the info log.
     */
    @OriginalMember(owner = "client!kia", name = "m", descriptor = "[I")
    public static final int[] shaderStatus = new int[2];
}
