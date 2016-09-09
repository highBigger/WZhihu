package will.wzhihu.common.utils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertyUtils {
    private static final int DEFAULT_CAPACITY = 128;
    private static final Pattern PROPERTY_PATTERN = Pattern.compile("get([A-Z][\\w]*)");

    private static final Map<Class<?>, Set<String>> cache =
            Collections.synchronizedMap(new WeakHashMap<Class<?>, Set<String>>(DEFAULT_CAPACITY));

    private static final Set<String> objectProperties = getPropertyNamesWithoutCache(Object.class);

    public static Set<String> getPropertyNames(Class<?> klass) {
        Set<String> properties;

        if (!cache.containsKey(klass)) {
            properties = getPropertyNamesWithoutCache(klass);
            properties.removeAll(objectProperties);
            cache.put(klass, properties);
        } else {
            properties = cache.get(klass);
        }
        return properties;
    }

    private static Set<String> getPropertyNamesWithoutCache(Class<?> klass) {
        Set<String> properties = new HashSet<String>();

        Method[] methods = klass.getMethods();

        for (Method method : methods) {
            String name = method.getName();

            Matcher matcher = PROPERTY_PATTERN.matcher(name);

            if (matcher.matches()) {
                String propertyName = matcher.group(1);
                properties.add(Character.toLowerCase(propertyName.charAt(0)) + propertyName.substring(1));
            }
        }

        return properties;
    }
}
