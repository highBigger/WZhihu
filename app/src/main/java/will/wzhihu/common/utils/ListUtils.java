package will.wzhihu.common.utils;

import android.support.v4.util.Pair;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;

/**
 * @author dusiyu
 */
public class ListUtils {

    public static <T> Pair<List<T>, List<T>> partition(List<T> list, Functions.Func1<T, Boolean>
        filter) {
        List<T> positive = new ArrayList<T>();
        List<T> negative = new ArrayList<T>();
        for (T t : list) {
            Boolean keep = filter.call(t);
            if (keep != null && keep) {
                positive.add(t);
            } else {
                negative.add(t);
            }
        }
        return new Pair<>(positive, negative);
    }

    public static <T> List<T> filter(List<T> list, Functions.Func1<T, Boolean> filter) {
        List<T> result = new ArrayList<>();
        if (list.isEmpty()) {
            return result;
        }
        for (T ele : list) {
            Boolean keep = filter.call(ele);
            if (keep != null && keep) {
                result.add(ele);
            }
        }
        return result;
    }

    public static <T> T getFirstMatched(Collection<T> collection, Functions.Func1<T, Boolean> matcher) {
        for (T t : collection) {
            Boolean result = matcher.call(t);
            if (result != null && result) {
                return t;
            }
        }
        return null;
    }

    public static <T> int indexOfFirstMatch(List<T> list, Functions.Func1<T, Boolean> matcher) {
        int i = 0;
        for (T t : list) {
            Boolean result = matcher.call(t);
            if (result != null && result) {
                return i;
            }
            i++;
        }
        return -1;
    }
    private static Comparator<Pair<?, ? extends Comparable>> ASC = new Comparator<Pair<?, ? extends Comparable>>() {

        @Override
        public int compare(Pair<?, ? extends Comparable> lhs, Pair<?, ? extends Comparable> rhs) {
            return lhs.second.compareTo(rhs.second);
        }
    };

    public static <T, Key extends Comparable<Key>> List<T> sortByKey(List<T> list, Functions.Func1<T, Key> keyExtractor) {
        ArrayList<Pair<T, Key>> holders = new ArrayList<>(list.size());
        for (T t : list) {
            holders.add(new Pair<>(t, keyExtractor.call(t)));
        }
        Collections.sort(holders, ASC);
        ArrayList<T> result = new ArrayList<>(list.size());
        for (Pair<T, Key> holder : holders) {
            result.add(holder.first);
        }
        return result;
    }

    public static <T, Key > List<T> sortByKey(List<T> list, Functions.Func1<T, Key> keyExtractor, final Comparator<Key> comparator) {

        ArrayList<Pair<T, Key>> holders = new ArrayList<>(list.size());
        for (T t : list) {
            holders.add(new Pair<>(t, keyExtractor.call(t)));
        }
        Collections.sort(holders, new Comparator<Pair<T, Key>>() {
            @Override
            public int compare(Pair<T, Key> lhs, Pair<T, Key> rhs) {
                return comparator.compare(lhs.second, rhs.second);
            }
        });
        ArrayList<T> result = new ArrayList<T>(list.size());
        for (Pair<T, Key> holder : holders) {
            result.add(holder.first);
        }
        return result;
    }

    public static <T, Key> int indexOfByKey(List<T> list, Functions.Func1<T, Key> keyExtractor, Key key) {
        int index = 0;
        for (T ele : list) {
            if (ObjectUtils.equals(keyExtractor.call(ele), key)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public static <T> List<T> toList(Collection<T> collection) {
        if (collection instanceof List) {
            return (List<T>) collection;
        } else {
            List<T> result = new ArrayList<T>(collection.size());
            for (T t : collection) {
                result.add(t);
            }
            return result;
        }
    }

    public static <T> List<T> fromArray(T[] array) {
        if (array == null) {
            return new ArrayList<>(0);
        } else {
            return new ArrayList<>(Arrays.asList(array));
        }
    }

    public static <T> List<T> addAllToSortedList(List<T> list, Collection<? extends T> items, Comparator<T> comparator) {
        for (T item : items) {
            int index = Collections.binarySearch(list, item, comparator);
            if (index < 0) {
                list.add(-index-1, item);
            } else {
                list.add(index, item);
            }
        }
        return list;
    }

    public static <T extends Comparable<T>> boolean isMax(List<T> list, T item) {
        for (T t : list) {
            int compare = t.compareTo(item);
            if (compare > 0) {
                return false;
            }
        }

        return true;
    }

    public static <T> List<T> fill(T item, int size) {
        return new FilledList<T>(item, size);
    }

    public static List<String> toString(List<?> list) {
        return new ToStringList(list);
    }

    public static <T> String join(List<T> list, String separator) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (T item : list) {
            if (first) {
                first = false;
            } else {
                builder.append(separator);
            }
            builder.append(String.valueOf(item));
        }
        return builder.toString();
    }

    public static <T> List<List<T>> partition(List<T> list, int size) {
        return (list instanceof RandomAccess)
                ? new RandomAccessPartition<T>(list, size)
                : new Partition<T>(list, size);
    }

    private static class RandomAccessPartition<T> extends Partition<T>
            implements RandomAccess {
        RandomAccessPartition(List<T> list, int size) {
            super(list, size);
        }
    }

    private static class FilledList<T> extends AbstractList<T> {
        private final T item;

        private final int size;

        private FilledList(T item, int size) {
            this.item = item;
            this.size = size;
        }

        @Override
        public T get(int location) {
            return item;
        }

        @Override
        public int size() {
            return size;
        }
    }

    private static class ToStringList extends AbstractList<String> {

        final List<?> list;

        private ToStringList(List<?> list) {
            this.list = list;
        }

        @Override
        public String get(int location) {
            return String.valueOf(list.get(location));
        }

        @Override
        public int size() {
            return list.size();
        }
    }

    private static class Partition<T> extends AbstractList<List<T>> {
        final List<T> list;
        final int size;

        Partition(List<T> list, int size) {
            this.list = list;
            this.size = size;
        }

        @Override
        public List<T> get(int index) {
            int start = index * size;
            int end = Math.min(start + size, list.size());
            return list.subList(start, end);
        }

        @Override
        public int size() {
            return (int) Math.ceil((float)list.size() / size);
        }

        @Override
        public boolean isEmpty() {
            return list.isEmpty();
        }
    }

}
