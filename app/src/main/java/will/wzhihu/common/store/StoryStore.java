package will.wzhihu.common.store;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import nl.qbusict.cupboard.Cupboard;
import nl.qbusict.cupboard.DatabaseCompartment;
import will.wzhihu.common.utils.SqlUtils;
import will.wzhihu.detail.StoryDetail;
import will.wzhihu.main.model.Story;

/**
 * @author wendeping
 */
public class StoryStore extends BaseStore {
    private static final int VERSION = 1;
    private static final String NAME = "stories.db";

    @Override
    protected String getDatabaseName() {
        return NAME;
    }

    @Override
    protected int getDatabaseVersion() {
        return VERSION;
    }

    @Override
    protected void registerEntities(Cupboard cupboard) {
        cupboard.register(Story.class);
        cupboard.register(StoryDetail.class);
    }

    @Override
    protected void onDatabaseUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDatabaseUpgrade(db, oldVersion, newVersion);
        createIndices(db);
    }

    @Override
    protected void onDatabaseCreate(SQLiteDatabase db) {
        super.onDatabaseCreate(db);
        createIndices(db);
    }

    private void createIndices(SQLiteDatabase db) {
        db.execSQL("CREATE INDEX IF NOT EXISTS story_date ON " +
            getCupboard().getTable(Story.class) +
            " (date)");

        db.execSQL("CREATE INDEX IF NOT EXISTS story_id ON " +
            getCupboard().getTable(StoryDetail.class) +
            " (id)");
    }

    public List<Story> getStoriesByDate(String date) {
        return getDatabaseCompartment().query(Story.class)
            .withSelection("date = ?", date)
            .list();
    }

    public void read(String storyId) {
        DatabaseCompartment db = getDatabaseCompartment();

        ContentValues values = new ContentValues();
        values.put("read", true);
        db.update(Story.class, values, "id = ?", storyId);
    }

    public void putAll(final List<Story> stories) {
        SqlUtils.executeInTransaction(getDatabase(), new Runnable() {
            @Override
            public void run() {
                for (Story story : stories) {
                    getDatabaseCompartment().put(story);
                }
            }
        });
    }

    public long putStoryDetail(StoryDetail detail) {
        return getDatabaseCompartment().put(detail);
    }

    public StoryDetail getStoryDetail(String storyId) {
        return getDatabaseCompartment().query(StoryDetail.class).withSelection("id = ?", storyId).get();
    }
}
