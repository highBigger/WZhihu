package will.wzhihu.common.utils;

/**
 * @author dusiyu
 */
public class ArrayUtils {

    /**
     * Return first index of {@code value} in {@code array}, or {@code -1} if
     * not found.
     */
    public static <T> int indexOf(T[] array, T value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                if (value == null) return i;
            } else {
                if (value != null && array[i].equals(value)) return i;
            }
        }
        return -1;
    }
    public static int indexOf(char[] array, char value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }
}
