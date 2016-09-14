package will.wzhihu.common.cupboard;

import android.content.ContentValues;
import android.database.Cursor;

import nl.qbusict.cupboard.convert.EntityConverter;
import nl.qbusict.cupboard.convert.FieldConverter;

public class WJoinFieldConverter<T> implements FieldConverter<T> {
    private FieldConverter<T> converter;

    public WJoinFieldConverter(FieldConverter<T> converter) {
        this.converter = converter;
    }

    @Override
    public T fromCursorValue(Cursor cursor, int columnIndex) {
        return converter.fromCursorValue(cursor, columnIndex);
    }

    @Override
    public void toContentValue(T value, String key, ContentValues values) {
        converter.toContentValue(value, key, values);
    }

    @Override
    public EntityConverter.ColumnType getColumnType() {
        return EntityConverter.ColumnType.JOIN;
    }
}
