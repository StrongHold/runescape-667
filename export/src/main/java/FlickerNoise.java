import com.jagex.graphics.EnvironmentLight;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * Writes the noise the client's lights flicker by, beside the textures: the 2048 values of
 * {@code EnvironmentLight.generateNoise} at the client's persistence of 0.4, which a light whose
 * flicker pattern is 3 reads at its phase, out of 4096.
 */
public final class FlickerNoise {

    private static final float PERSISTENCE = 0.4F;

    public static void write(Path textures) throws IOException {
        var light = textures.resolve("light");
        Files.createDirectories(light);
        var noise = EnvironmentLight.generateNoise(PERSISTENCE);
        Files.writeString(light.resolve("flicker.json"), Arrays.toString(noise));
    }

    private FlickerNoise() {
        /* empty */
    }
}
