package will.wzhihu.common.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CollectionUtils {


    public static <T> Iterable<List<T>> getBlocks(final List<T> list, final int blockSize) {
        return new Iterable<List<T>>() {
            @Override
            public Iterator<List<T>> iterator() {
                return new Iterator<List<T>>() {
                    private int ptr = 0;
                    private final int count = list.size();

                    @Override
                    public boolean hasNext() {
                        return ptr < count;
                    }

                    @Override
                    public List<T> next() {
                        int next = Math.min(ptr + blockSize, count);
                        List<T> subList = list.subList(ptr, next);
                        ptr = next;
                        return subList;
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    public static <T> List<T> toList(Collection<T> collection) {
        if (collection instanceof List) {
            return (List<T>)collection;
        } else {
            return new ArrayList<>(collection);
        }
    }

    public static <T> Iterable<List<T>> getBlocks(final Collection<T> list, final int blockSize) {
        return getBlocks(new ArrayList<>(list), blockSize);
    }


    public static <T> String[] toStringArray(Collection<T> items) {
        String[] result = new String[items.size()];

        int i = 0;

        for (Object item : items) {
            result[i++] = String.valueOf(item);
        }

        return result;
    }

    public static <T> boolean isEmpty(Collection<T> collection)  {
        return collection == null || collection.isEmpty();
    }

    public static <T> Set<T> difference(Collection<T> x, Collection<T> y) {
        Set<T> result = new HashSet<>(x);
        result.removeAll(y);
        return result;
    }
}

