package im.nll.data.extractor.parser;

import com.google.common.collect.Maps;
import im.nll.data.extractor.Extractor;
import im.nll.data.extractor.exception.ParseException;
import im.nll.data.extractor.impl.JSONPathExtractor;
import im.nll.data.extractor.impl.JerryExtractor;
import im.nll.data.extractor.impl.RegexExtractor;
import im.nll.data.extractor.impl.XPathExtractor;
import im.nll.data.extractor.utils.Reflect;
import im.nll.data.extractor.utils.StringUtils;

import java.util.Map;

/**
 * parse extractor from string.
 *
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/1/19 上午11:32
 */
public class ExtractorParser {
    private static Map<String, Class<? extends Extractor>> extractorMap = Maps.newHashMap();
    private static final String SPLIT_CHAR = ":";

    static {
        extractorMap.put("json", JSONPathExtractor.class);
        extractorMap.put("jerry", JerryExtractor.class);
        extractorMap.put("xpath", XPathExtractor.class);
        extractorMap.put("regex", RegexExtractor.class);
    }

    public static Extractor parse(String shortString) {
        String type = StringUtils.substringBeforeLast(shortString, SPLIT_CHAR);
        Class extractorClass = extractorMap.get(type);
        if (extractorClass == null) {
            throw new ParseException("parse extractor error! string:" + shortString);
        }
        Extractor extractor = Reflect.on(extractorClass)
                .create(StringUtils.substringAfter(shortString, type + SPLIT_CHAR)).get();
        return extractor;
    }
}
