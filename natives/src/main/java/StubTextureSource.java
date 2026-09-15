import com.jagex.graphics.TextureMetrics;
import com.jagex.graphics.TextureSource;

public final class StubTextureSource implements TextureSource {
    public int textureCount() { return 0; }
    public boolean textureAvailable(int id) { return false; }
    public int[] argbOutput(float f, int id, int width, int height) { return null; }
    public float[] floatArgbOutput(int a, int b, float c, int d) { return null; }
    public TextureMetrics getMetrics(int id) { return null; }
    public int[] rgbOutput(int a, boolean b, int c, int d, float e) { return null; }
}
