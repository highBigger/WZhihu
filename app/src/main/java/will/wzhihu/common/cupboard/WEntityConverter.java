package will.wzhihu.common.cupboard;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Collection;

import nl.qbusict.cupboard.Cupboard;
import nl.qbusict.cupboard.annotation.Ignore;
import nl.qbusict.cupboard.convert.FieldConverter;
import nl.qbusict.cupboard.convert.ReflectiveEntityConverter;

public class WEntityConverter<T> extends ReflectiveEntityConverter<T> {
    public WEntityConverter(Cupboard cupboard, Class<T> entityClass) {
        super(cupboard, entityClass);
    }

    public WEntityConverter(Cupboard cupboard, Class<T> entityClass,
                            Collection<String> ignoredFieldsNames) {
        super(cupboard, entityClass, ignoredFieldsNames);
    }

    public WEntityConverter(Cupboard cupboard, Class<T> entityClass,
                            Collection<String> ignoredFieldNames,
                            Collection<Column> additionalColumns) {
        super(cupboard, entityClass, ignoredFieldNames, additionalColumns);
    }

    @Override
    protected String getColumn(Field field) {
        WColumn column = field.getAnnotation(WColumn.class);
        String name = null;

        if (column != null) {
            name = column.name();
        }

        return name != null && !name.equals("") ? name : field.getName();
    }

    @Override
    protected boolean isIgnored(Field field) {
        int modifiers = field.getModifiers();

        boolean ignored = Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers) || Modifier
                .isTransient(modifiers);
        Ignore ignore = field.getAnnotation(Ignore.class);
        if (ignore != null) {
            ignored = true;
        }
        if (!ignored) {
            WColumn column = field.getAnnotation(WColumn.class);
            ignored = column != null && column.ignored();
        }

        return ignored;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected FieldConverter<?> getFieldConverter(Field field) {
        WColumn column = field.getAnnotation(WColumn.class);
        Type type = field.getGenericType();

        if (column != null) {
            Class fieldConverter = column.fieldConverter();
            if (fieldConverter != null && fieldConverter != WColumn.DefaultFieldConverter.class) {
                try {
                    return (FieldConverter<?>) fieldConverter.getConstructor(Type.class)
                            .newInstance(type);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }

        FieldConverter<?> delegate = mCupboard.getFieldConverter(field.getGenericType());

        if (column != null && column.readonly()) {
            return new WJoinFieldConverter(delegate);
        } else {
            return delegate;
        }
    }
}
