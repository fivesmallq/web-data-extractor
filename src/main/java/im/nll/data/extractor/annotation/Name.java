package im.nll.data.extractor.annotation;

import java.lang.annotation.*;

/**
 * define extractor short name
 *
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/1/19 下午2:15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface Name {
    String[] value() default {};
}
