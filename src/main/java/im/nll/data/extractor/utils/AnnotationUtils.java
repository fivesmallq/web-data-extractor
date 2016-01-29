package im.nll.data.extractor.utils;

import java.lang.reflect.Field;

/**
 * 注解工具类.
 */
public class AnnotationUtils {
    /**
     * 是否有指定的annotation存在.
     *
     * @param field
     * @param annotationClass
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static boolean has(Field field, Class annotationClass) {
        return field.getAnnotation(annotationClass) != null;
    }

    /**
     * 是否有指定的annotation存在.
     *
     * @param classOfBean
     * @param annotationClass
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static boolean has(Class<?> classOfBean, Class annotationClass) {
        return classOfBean.getAnnotation(annotationClass) != null;
    }

}
