package will.wzhihu.common.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import will.wzhihu.WApplication;
import will.wzhihu.common.storage.model.Entry;
import will.wzhihu.common.utils.SqlUtils;
import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * @author dusiyu
 */
public class DatabaseStorage implements Storage{
    private static class DBOpenHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "storage.db";
        private static final int DATABASE_VERSION = 1;

        static {
            cupboard().register(Entry.class);
        }

        public DBOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // this will ensure that all tables are created
            cupboard().withDatabase(db).createTables();
            // add indexes and other database tweaks
            String entryTableName = cupboard().getTable(Entry.class);
            db.execSQL("CREATE UNIQUE INDEX uniq_entry_key ON " + entryTableName + " (key)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // this will upgrade tables, adding columns and new tables.
            // Note that existing columns will not be converted
            cupboard().withDatabase(db).upgradeTables();
            // do migration work
        }
    }

    private SQLiteDatabase db;

    public DatabaseStorage() {
    }

    @Override
    public synchronized <T> T get(String key, Class<T> type) {
        Entry entry = cupboard().withDatabase(db).query(Entry.class).withSelection("key = ?",
                key).get();
        T value = null;
        if (entry != null) {
            if (entry.expiredAt <= System.currentTimeMillis() && entry.expiredAt >= 0) {
                this.remove(key);
            } else {
                value = new Gson().fromJson(entry.data, type);
            }
        }
        return value;
    }

    @Override
    public synchronized void put(String key, Object value) {
        put(key, value, -1);
    }

    @Override
    public synchronized void put(String key, Object value, long ttl) {
        Entry entry = new Entry();
        entry.data = new Gson().toJson(value);
        entry.key = key;
        if (ttl > 0) {
            entry.expiredAt = System.currentTimeMillis() + ttl;
        } else {
            entry.expiredAt = ttl;
        }
        db.beginTransaction();
        try {
            Entry oldValue = cupboard().withDatabase(db).query(Entry.class).withSelection("key = " +
                    "?", key).get();
            if (oldValue != null) {
                entry._id = oldValue._id;
            }
            cupboard().withDatabase(db).put(entry);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private synchronized void initialize() {
        db = new DBOpenHelper(WApplication.getInstance()).getWritableDatabase();
    }

    @Override
    public synchronized void remove(String key) {
        cupboard().withDatabase(db).delete(Entry.class, "key = ?", key);
    }

    @Override
    public synchronized void clearSessionKeys() {
        SqlUtils.executeInTransaction(db, new Runnable() {
            @Override
            public void run() {
                final List<Long> ids = new ArrayList<>();
                Iterator<Entry> iterator =
                    SqlUtils.iterate(cupboard().withDatabase(db), Entry.class, 50);
                while(iterator.hasNext()) {
                    Entry entry = iterator.next();
                    if (entry.expiredAt == Storage.TTL_SESSION) {
                        ids.add(entry._id);
                    }
                }
                SqlUtils.deleteInSelection(cupboard().withDatabase(db), Entry.class, "_id", ids);
            }
        });
    }
}
