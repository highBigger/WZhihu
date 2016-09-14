package will.wzhihu.common.cupboard;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.FIELD})
public @interface WColumn {
    class DefaultFieldConverter {}
    String name() default "";
    Class fieldConverter() default DefaultFieldConverter.class;
    boolean readonly() default false;
    boolean ignored() default false;
}
