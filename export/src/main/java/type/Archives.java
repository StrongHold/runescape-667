package type;

import com.jagex.js5.js5;

/**
 * The cache's archives as the client's own {@code js5}, which the kinds hand to the client's type
 * lists that their types decode with.
 */
public interface Archives {

    js5 js5(int archive);
}
