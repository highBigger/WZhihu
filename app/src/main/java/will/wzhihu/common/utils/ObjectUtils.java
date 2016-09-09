package will.wzhihu.common.utils;

/**
 * @author dusiyu
 */
public class ObjectUtils {

    public static boolean equals(Object o1, Object o2) {
        if (o1 != null && o2 != null) {
            return o1.equals(o2);
        }
        return o1 == o2;
    }
}
