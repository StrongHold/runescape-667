import java.util.List;
import java.util.Optional;

/**
 * One group of the sprites archive: every frame the client reads from it, in order, and the name
 * the client asks for it by, where the client asks by name.
 */
public record SpriteArchive(int id, Optional<String> name, List<SpriteFrame> frames) {
}
