package will.wzhihu.common.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import nl.qbusict.cupboard.DatabaseCompartment;
import will.wzhihu.common.log.Log;

public class SqlUtils {

    public final static int MAX_SQL_PARAMETER_COUNT = 20;

    public static final Functions.Func1<Cursor, String> GET_STRING = new Functions.Func1<Cursor, String>() {
        @Override
        public String call(Cursor cursor) {
            return cursor.getString(0);
        }
    };

    public static void executeInTransaction(SQLiteDatabase db, Runnable runnable) {
        db.beginTransaction();
        try {
            runnable.run();
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.e("SqlUtils", "executeInTransaction", ex);
            throw ex;
        } finally {
            db.endTransaction();
        }
    }

    public static String generateInSelection(String column, int count) {
        StringBuilder builder = new StringBuilder();
        builder.append(column);
        builder.append(" IN (");
        for (int i = 0; i < count; i++) {
            builder.append("?");
            if (i < count -1) {
                builder.append(",");
            }
        }
        builder.append(" )");
        return builder.toString();
    }


    public static <T, V> void deleteInSelection(DatabaseCompartment compartment, Class<T> klass, String column, List<V> items) {
        for (List<V> block : CollectionUtils.getBlocks(items, MAX_SQL_PARAMETER_COUNT)) {
            compartment.delete(klass, generateInSelection(column, block.size()), CollectionUtils.toStringArray(block));
        }
    }

    public static <T, V> List<T> listInSelection(DatabaseCompartment.QueryBuilder<T> query, String column, Collection<V> items) {
        List<T> result = new ArrayList<T>();

        for (List<V> block : CollectionUtils.getBlocks(items, MAX_SQL_PARAMETER_COUNT)) {
            result.addAll(query.withSelection(generateInSelection(column, block.size()),
                CollectionUtils
                    .toStringArray(block)).list());
        }

        return result;
    }

    public static <T, V> int updateInSelection(DatabaseCompartment compartment, Class<T> klass, String column, List<V> items, ContentValues values) {
        int processed = 0;
        for (List<V> block : CollectionUtils.getBlocks(items, MAX_SQL_PARAMETER_COUNT)) {
            processed += compartment.update(klass, values, generateInSelection(column, block.size()), CollectionUtils.toStringArray(block));
        }
        return processed;
    }

    public static List<String> list(Cursor cursor) {
        return list(cursor, GET_STRING);
    }


    public static <T> List<T> list(Cursor cursor, Functions.Func1<Cursor, T> mapper) {
        ArrayList<T> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            list.add(mapper.call(cursor));
        }
        return list;
    }

    public static <T> T reduce(Cursor cursor, Functions.Func2<T, Cursor, T> reducer, T initValue) {
        T value = initValue;
        while(cursor.moveToNext()) {
            value = reducer.call(value, cursor);
        }
        return value;
    }

    public static <T> T first(Cursor cursor, Functions.Func1<Cursor, T> mapper) {
        T t = null;
        if(cursor.moveToNext()) {
            t = mapper.call(cursor);
        }
        return t;
    }

    public static <T> Iterator<T> iterate(DatabaseCompartment db, Class<T> klass, int step) {
        return new IdIterator<>(db, klass, step);
    }

    public static class IdIterator<T> implements Iterator<T> {

        private long id = 0;

        private final int step;

        private final DatabaseCompartment db;

        private int offset = 0;

        private List<T> items;

        private Class<T> clazz;

        private Field idField;

        public IdIterator(DatabaseCompartment db, Class<T> clazz, int step) {
            this.db = db;
            this.clazz = clazz;
            this.step = step;
            try {
                this.idField = this.clazz.getDeclaredField(BaseColumns._ID);
            } catch (NoSuchFieldException e) {
            }
        }

        private boolean fetchMore() {
            items = db.query(clazz).withSelection(BaseColumns._ID + " > ?", String.valueOf(id)).orderBy(BaseColumns._ID + " ASC").limit(step).list();
            offset = 0;
            if (items.size() > 0) {
                T last = items.get(items.size() - 1);
                try {
                    id = getId(last);
                } catch (IllegalAccessException e) {
                    return false;
                }
            }
            return !items.isEmpty();
        }

        private Long getId(T value) throws IllegalAccessException {
            return (Long)idField.get(value);
        }

        @Override
        public boolean hasNext() {
            return (items != null && offset < items.size()) || fetchMore();
        }

        @Override
        public T next() {
            return items.get(offset++);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
