package will.wzhihu.common.cupboard;

import android.content.ContentValues;
import android.database.Cursor;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import nl.qbusict.cupboard.convert.EntityConverter;
import nl.qbusict.cupboard.convert.FieldConverter;

public class WJSONFieldConverter<T> implements FieldConverter<T> {
    private Type type;

    public WJSONFieldConverter(Type type) {
        this.type = type;
    }

    @Override
    public T fromCursorValue(Cursor cursor, int columnIndex) {
        return new Gson().fromJson(cursor.getString(columnIndex), type);
    }

    @Override
    public void toContentValue(T value, String key, ContentValues values) {
        values.put(key, new Gson().toJson(value));
    }

    @Override
    public EntityConverter.ColumnType getColumnType() {
        return EntityConverter.ColumnType.TEXT;
    }
}
