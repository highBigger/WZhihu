package will.wzhihu.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dusiyu
 */
public class StringUtils {

    private static final int INDEX_NOT_FOUND = -1;

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((!Character.isWhitespace(str.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    public static String truncate(String string, int maxLength, String ellipsis) {
        int ellipsisLength = ellipsis.length();
        int length = string.length();
        if (length <= maxLength) {
            return string;
        }
        return string.substring(0, maxLength - ellipsisLength) + ellipsis;
    }

    public static boolean isEqual(String str0, String str1) {
        return str0 != null && str1 != null ? str0.equals(str1) : str0 == str1;
    }

    public static String replace(String text, String searchString, String replacement, int max) {
        if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end == INDEX_NOT_FOUND) {
            return text;
        }
        int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = (increase < 0 ? 0 : increase);
        increase *= (max < 0 ? 16 : (max > 64 ? 64 : max));
        StringBuilder buf = new StringBuilder(text.length() + increase);
        while (end != INDEX_NOT_FOUND) {
            buf.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = text.indexOf(searchString, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }

    public static String firstNotNull(String... strings) {
        String result = null;
        for (String string : strings) {
            if (string != null) {
                result = string;
                break;
            }
        }
        return result;
    }

    public static String[] split(String str, char separatorChar) {
        return splitWorker(str, separatorChar, false);
    }

    private static String[] splitWorker(String str, char separatorChar, boolean preserveAllTokens) {
        // Performance tuned for 2.0 (JDK1.4)

        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return new String[0];
        }
        List<String> list = new ArrayList<>();
        int i = 0, start = 0;
        boolean match = false;
        boolean lastMatch = false;
        while (i < len) {
            if (str.charAt(i) == separatorChar) {
                if (match || preserveAllTokens) {
                    list.add(str.substring(start, i));
                    match = false;
                    lastMatch = true;
                }
                start = ++i;
                continue;
            }
            lastMatch = false;
            match = true;
            i++;
        }
        if (match || (preserveAllTokens && lastMatch)) {
            list.add(str.substring(start, i));
        }
        return list.toArray(new String[list.size()]);
    }

    public static String castOrNull(Object obj) {
        if (obj != null && obj instanceof String) {
            return (String)obj;
        }
        return null;
    }

    public static boolean contains(String string, String snippet) {
        if (isEmpty(string) || isEmpty(snippet)) {
            return false;
        }
        return string.contains(snippet);
    }
}
