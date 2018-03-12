package im.nll.data.extractor.parser;

import im.nll.data.extractor.Extractor;
import im.nll.data.extractor.annotation.Name;
import im.nll.data.extractor.exception.ParseException;
import im.nll.data.extractor.utils.AnnotationClassScanner;
import im.nll.data.extractor.utils.Logs;
import im.nll.data.extractor.utils.Reflect;
import im.nll.data.extractor.utils.StringUtils;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * parse extractor from string.
 *
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/1/19 上午11:32
 */
public class ExtractorParser {
    private static Logger LOGGER = Logs.get();
    private static Map<String, Class<? extends Extractor>> extractorMap = new HashMap<>();
    private static final String SPLIT_CHAR = ":";

    static {
        String packageName = Extractor.class.getPackage().getName() + ".impl";
        LOGGER.debug("scan package:{}", packageName);
        Set<Class<?>> classes = AnnotationClassScanner.scan(Name.class, packageName);
        for (Class<?> clazz : classes) {
            // 不是接口的才一起玩.
            if (!clazz.isInterface()) {
                Name cityAnnotation = clazz.getAnnotation(Name.class);
                String[] cities = cityAnnotation.value();
                if (cities != null) {
                    for (String city : cities) {
                        LOGGER.debug("added '{}' extractor implement.", city);
                        extractorMap.put(city, (Class<? extends Extractor>) clazz);
                    }
                }
            }
        }
        if (classes.isEmpty()) {
            LOGGER.error("no extractor implement find");
        }
    }

    public static Extractor parse(String shortString) {
        String type = StringUtils.substringBefore(shortString, SPLIT_CHAR);
        Class extractorClass = extractorMap.get(type);
        if (extractorClass == null) {
            throw new ParseException("parse extractor error! unsupport extractor:'" + type + "'. string:'" + shortString + "'");
        }
        Extractor extractor = Reflect.on(extractorClass)
                .create(StringUtils.substringAfter(shortString, type + SPLIT_CHAR)).get();
        return extractor;
    }
}
