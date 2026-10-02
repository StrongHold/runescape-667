import java.util.Arrays;

/**
 * A list of floats that grows as it is added to, without boxing each one.
 */
public final class FloatList {

    private static final int FIRST_CAPACITY = 64;

    private float[] values = new float[FIRST_CAPACITY];
    private int size;

    public void add(float value) {
        if (size == values.length) {
            values = Arrays.copyOf(values, size * 2);
        }
        values[size++] = value;
    }

    public float[] toArray() {
        return Arrays.copyOf(values, size);
    }
}
