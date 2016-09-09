package will.wzhihu.common.utils;

public class Functions {
    public interface Func1<T, R> {
        R call(T t);
    }
    public interface Func2<T1, T2, R> {
        R call(T1 t1, T2 t2);
    }

    @SafeVarargs
    public static <T> Func1<T, Boolean> and(final Func1<T, Boolean> ...conditions) {
        return new Functions.Func1<T, Boolean>() {
            @Override
            public Boolean call(T t) {
                for (Func1<T, Boolean> condition : conditions) {
                    Boolean value = condition.call(t);
                    if (value == null || !value) {
                        return false;
                    }
                }
                return true;
            }
        };
    }


    @SafeVarargs
    public static <T> Func1<T, Boolean> or(final Func1<T, Boolean> ...conditions) {
        return new Func1<T, Boolean>() {
            @Override
            public Boolean call(T t) {
                for (Func1<T, Boolean> condition : conditions) {
                    Boolean value = condition.call(t);
                    if (value != null && value) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public static <T> Functions.Func1<T, Boolean> not(final Functions.Func1<T, Boolean> first) {
        return new Functions.Func1<T, Boolean>() {
            @Override
            public Boolean call(T t) {
                Boolean value = first.call(t);
                return value == null || !value;
            }
        };
    }
}
