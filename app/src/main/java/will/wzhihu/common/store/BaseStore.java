package will.wzhihu.common.store;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import nl.qbusict.cupboard.Cupboard;
import nl.qbusict.cupboard.CupboardBuilder;
import nl.qbusict.cupboard.DatabaseCompartment;
import nl.qbusict.cupboard.convert.EntityConverter;
import nl.qbusict.cupboard.convert.EntityConverterFactory;
import will.wzhihu.WApplication;
import will.wzhihu.common.cupboard.WEntityConverter;

public abstract class BaseStore {
    private class DBOpenHelper extends SQLiteOpenHelper {
        DBOpenHelper(final Context context, final String databaseName, final int databaseVersion) {
            super(context, databaseName, null, databaseVersion);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // this will ensure that all tables are created
            getCupboard().withDatabase(db).createTables();
            onDatabaseCreate(db);
            // add indexes and other database tweaks
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // this will upgrade tables, adding columns and new tables.
            // Note that existing columns will not be converted
            getCupboard().withDatabase(db).upgradeTables();
            onDatabaseUpgrade(db, oldVersion, newVersion);
            // do migration work
        }
    }

    private SQLiteDatabase db;
    private Cupboard cupboard;

    protected abstract String getDatabaseName();

    protected abstract int getDatabaseVersion();

    protected abstract void registerEntities(Cupboard cupboard);

    protected void onDatabaseCreate(SQLiteDatabase db) {
    }

    protected void onDatabaseUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    protected SQLiteDatabase getDatabase() {
        if (db == null) {
            synchronized (this) {
                if (db == null) {
                    db = new DBOpenHelper(WApplication.getInstance(),
                            getDatabaseName(),
                            getDatabaseVersion()).getWritableDatabase();
                }
            }
        }
        return db;
    }

    private CupboardBuilder getCupboardBuilder() {
        return new CupboardBuilder().useAnnotations().registerEntityConverterFactory(
                new EntityConverterFactory() {
                    @Override
                    public <T> EntityConverter<T> create(Cupboard cupboard, Class<T> type) {
                        return new WEntityConverter<T>(cupboard, type);
                    }
                }
        );
    }

    protected Cupboard getCupboard() {
        if (cupboard == null) {
            synchronized (this) {
                if (cupboard == null) {
                    cupboard = getCupboardBuilder().build();
                    registerEntities(cupboard);
                }
            }
        }
        return cupboard;
    }

    protected DatabaseCompartment getDatabaseCompartment() {
        return getCupboard().withDatabase(getDatabase());
    }

    public void close() {
        if (db != null) {
            db.close();
        }
    }
}
