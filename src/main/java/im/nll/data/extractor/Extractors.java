package im.nll.data.extractor;

import com.jayway.jsonpath.JsonPath;
import im.nll.data.extractor.entity.EntityExtractor;
import im.nll.data.extractor.entity.EntityListExtractor;
import im.nll.data.extractor.impl.*;
import im.nll.data.extractor.parser.ExtractorParser;
import im.nll.data.extractor.rule.ExtractRule;
import im.nll.data.extractor.utils.Logs;
import im.nll.data.extractor.utils.Reflect;
import im.nll.data.extractor.utils.StringUtils;
import im.nll.data.extractor.utils.Validate;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 15/12/28 下午4:43
 */
public class Extractors {
    private static final Logger LOGGER = Logs.get();
    private static final String DEFAULT_FIELD = "_default_field_";
    private String html;
    private List<String> htmlList;
    private Map<String, List<Extractor>> extractorsMap = new LinkedHashMap<>();
    private Map<String, List<Filter>> filtersMap = new LinkedHashMap();
    private List<Filter> beforeFilter = new LinkedList<>();
    private List<Filter> afterFilter = new LinkedList<>();
    private String prevField;

    public Extractors(String html) {
        this.html = html;
    }

    /**
     * set the html to extract
     *
     * @param html
     * @return
     */
    public static Extractors on(String html) {
        return new Extractors(html);
    }

    /**
     * extract data by extract rule
     *
     * @param rule
     * @return
     */
    public Extractors extract(ExtractRule rule) {
        extract(rule.getField(), rule.getExtractor());
        return this;
    }


    /**
     * extract data by extractor
     *
     * @param extractor
     * @return
     */
    public Extractors extract(Extractor extractor) {
        extract(DEFAULT_FIELD, extractor);
        return this;
    }

    /**
     * extract data by extractor and set to field
     *
     * @param field
     * @param extractor
     * @return
     */
    public Extractors extract(String field, Extractor... extractor) {
        List<Extractor> extractors = extractorsMap.getOrDefault(field, new LinkedList<>());
        Collections.addAll(extractors, extractor);
        extractorsMap.put(field, extractors);
        this.prevField = field;
        return this;
    }

    /**
     * extract data by extractor string and set to field.
     * <p>
     * current support:
     * <li>xpath : {@link XPathExtractor}</li>
     * <li>jerry : {@link JerryExtractor}</li>
     * <li>regex : {@link RegexExtractor}</li>
     * <li>string : {@link StringRangeExtractor}</li>
     * <li>json:{@link JSONPathExtractor} </li>
     * </p>
     *
     * @param field
     * @param extractorString
     * @return
     */
    public Extractors extract(String field, String extractorString) {
        Extractor extractor = ExtractorParser.parse(extractorString);
        extract(field, extractor);
        return this;
    }

    /**
     * extract data by field and extractor string map.
     * <p>
     * current support:
     * <li>xpath : {@link XPathExtractor}</li>
     * <li>jerry : {@link JerryExtractor}</li>
     * <li>regex : {@link RegexExtractor}</li>
     * <li>string : {@link StringRangeExtractor}</li>
     * <li>json:{@link JSONPathExtractor} </li>
     * </p>
     *
     * @param extractors
     * @return
     */
    public Extractors extract(Map<String, String> extractors) {
        for (Map.Entry<String, String> one : extractors.entrySet()) {
            Extractor extractor = ExtractorParser.parse(one.getValue());
            extract(one.getKey(), extractor);
        }
        return this;
    }

    /**
     * extract data by extract rule list.
     * <p>
     * current support:
     * <li>xpath : {@link XPathExtractor}</li>
     * <li>jerry : {@link JerryExtractor}</li>
     * <li>regex : {@link RegexExtractor}</li>
     * <li>string : {@link StringRangeExtractor}</li>
     * <li>json:{@link JSONPathExtractor} </li>
     * </p>
     *
     * @param extractRules
     * @return
     */
    public Extractors extract(List<ExtractRule> extractRules) {
        for (ExtractRule one : extractRules) {
            Extractor extractor = ExtractorParser.parse(one.getExtractor());
            extract(one.getField(), extractor);
        }
        return this;
    }

    /**
     * extract data by extractor and set to prev field.
     *
     * @param extractor
     * @return
     */
    public Extractors with(Extractor extractor) {
        Validate.notNull(prevField, "must call extract method first!");
        List<Extractor> extractors = extractorsMap.getOrDefault(prevField, new LinkedList<>());
        extractors.add(extractor);
        extractorsMap.put(prevField, extractors);
        return this;
    }

    /**
     * extract data by extractor string and set to prev field.
     * <p>
     * current support:
     * <li>xpath : {@link XPathExtractor}</li>
     * <li>jerry : {@link JerryExtractor}</li>
     * <li>regex : {@link RegexExtractor}</li>
     * <li>string : {@link StringRangeExtractor}</li>
     * <li>json:{@link JSONPathExtractor} </li>
     * </p>
     *
     * @param extractorString
     * @return
     */
    public Extractors with(String extractorString) {
        Validate.notNull(prevField, "must call extract method first!");
        Extractor extractor = ExtractorParser.parse(extractorString);
        with(extractor);
        return this;
    }

    /**
     * process value use filter
     *
     * @param filter
     * @return
     */
    public Extractors filter(Filter filter) {
        Validate.notNull(prevField, "must call extract method first!");
        List<Filter> filters = filtersMap.getOrDefault(prevField, new LinkedList<>());
        filters.add(filter);
        filtersMap.put(prevField, filters);
        return this;
    }

    /**
     * process all values use filter before field filter
     *
     * @param filter
     * @return
     */
    public Extractors before(Filter filter) {
        beforeFilter.add(filter);
        return this;
    }

    /**
     * process all values use filter after field filter
     *
     * @param filter
     * @return
     */
    public Extractors after(Filter filter) {
        beforeFilter.add(filter);
        return this;
    }


    /**
     * split html use listable extractor
     *
     * @param listableExtractor
     * @return
     */
    public Extractors split(Extractor listableExtractor) {
        Validate.isTrue(listableExtractor instanceof ListableExtractor, "split parameter must implement ListableExtractor." + listableExtractor.getClass().getSimpleName() + " can't be used.");
        this.htmlList = ((ListableExtractor) listableExtractor).extractList(html);
        return this;
    }

    /**
     * split html use listable extractor string
     * <p>
     * current support:
     * <li>xpath : {@link XPathExtractor}</li>
     * <li>jerry : {@link JerryExtractor}</li>
     * <li>regex : {@link RegexExtractor}</li>
     * <li>string : {@link StringRangeExtractor}</li>
     * <li>json:{@link JSONPathExtractor} </li>
     * </p>
     *
     * @param listExtractorString
     * @return
     */
    public Extractors split(String listExtractorString) {
        Extractor listableExtractor = ExtractorParser.parse(listExtractorString);
        split(listableExtractor);
        return this;
    }

    /**
     * extract data as string
     *
     * @return
     */
    public String asString() {
        String result = html;
        List<Extractor> extractors = extractorsMap.get(DEFAULT_FIELD);
        if (extractors == null) {
            if (htmlList != null) {
                result = asMapList().toString();
            } else {
                result = asMap().toString();
            }
        } else {
            for (Extractor extractor : extractors) {
                result = extractor.extract(result);
            }
            result = filterBefore(result);
            result = filter(DEFAULT_FIELD, result);
            result = filterAfter(result);
        }
        return result;
    }

    /**
     * extract data as string
     *
     * @return
     */
    public String asJSONString() {
        String result = html;
        List<Extractor> extractors = extractorsMap.get(DEFAULT_FIELD);
        if (extractors == null) {
            if (htmlList != null) {
                result = JsonPath.parse(asMapList()).jsonString();
            } else {
                result = JsonPath.parse(asMap()).jsonString();
            }
        } else {
            for (Extractor extractor : extractors) {
                result = extractor.extract(result);
            }
            result = filterBefore(result);
            result = filter(DEFAULT_FIELD, result);
            result = filterAfter(result);
        }
        return result;
    }

    /**
     * extract data as string list
     *
     * @return
     */
    public List<String> asStringList(String separator) {
        List<String> stringList = new LinkedList<>();
        if (htmlList != null) {
            for (String input : htmlList) {
                if (extractorsMap == null || extractorsMap.isEmpty()) {
                    stringList.add(input);
                } else {
                    StringBuffer stringBuffer = new StringBuffer();
                    for (Map.Entry<String, List<Extractor>> one : extractorsMap.entrySet()) {
                        String name = one.getKey();
                        List<Extractor> extractors = one.getValue();
                        String result = html;
                        for (Extractor extractor : extractors) {
                            result = extractor.extract(result);
                        }
                        result = filterBefore(result);
                        result = filter(name, result);
                        result = filterAfter(result);
                        stringBuffer.append(result).append(separator);
                    }
                    int length = stringBuffer.length();
                    stringBuffer.delete(length - separator.length(), length);
                    stringList.add(stringBuffer.toString());
                }
            }
        } else {
            String result = asMap().toString();
            stringList.add(result);
        }
        return stringList;
    }

    /**
     * extract data as string list, default separator is ','
     *
     * @return
     */
    public List<String> asStringList() {
        return asStringList(",");
    }

    /**
     * extract data as a map. key set by {@link #extract(String, Extractor...)}}
     *
     * @return
     */
    public Map<String, String> asMap() {
        return extractMap(html);
    }


    /**
     * extract data to a map list. key set by {@link #extract(String, Extractor...)}}
     * must split html by {@link #split(Extractor)} before asMapList.
     *
     * @return
     */
    public List<Map<String, String>> asMapList() {
        Validate.notNull(htmlList, "must split first!");
        List<Map<String, String>> mapList = new LinkedList<>();
        for (String input : htmlList) {
            mapList.add(extractMap(input));
        }
        return mapList;
    }

    /**
     * convert extract data to bean. field name set by {@link #extract(String, Extractor...)}}
     *
     * @return
     */
    public <T> T asBean(Class<T> clazz) {
        return extractBean(html, clazz);
    }

    /**
     * extract data as a bean list. field name set by {@link #extract(String, Extractor...)}}
     * must split html by {@link #split(Extractor)} before call this method.
     *
     * @return
     */
    public <T> List<T> asBeanList(Class<T> clazz) {
        Validate.notNull(htmlList, "must split first!");
        List<T> entityList = new LinkedList<>();
        for (String input : htmlList) {
            entityList.add(extractBean(input, clazz));
        }
        return entityList;
    }

    //------------ custom process --------------//

    /**
     * extract data by custom entity extractor as bean
     *
     * @param entityExtractor
     * @param <T>
     * @return
     */
    public <T> T asBean(EntityExtractor<T> entityExtractor) {
        T entity = entityExtractor.extract(html);
        return entity;
    }

    /**
     * extract data by custom entity extractor as bean list.
     * must split html by {@link #split(Extractor)} before call this method.
     *
     * @param entityExtractor
     * @param <T>
     * @return
     */
    public <T> List<T> asBeanList(EntityExtractor<T> entityExtractor) {
        Validate.notNull(htmlList, "must split first!");
        List<T> entityList = new LinkedList<>();
        for (String input : htmlList) {
            T entity = entityExtractor.extract(input);
            entityList.add(entity);
        }
        return entityList;
    }

    /**
     * extract data by custom entity extractor as bean list
     *
     * @param entityListExtractor
     * @param <T>
     * @return
     */
    public <T> List<T> asBeanList(EntityListExtractor<T> entityListExtractor) {
        return entityListExtractor.extractList(html);
    }
    //------------ custom process --------------//


    //------------ internal --------------//

    private <T> T extractBean(String html, Class<T> clazz) {
        // only support String type
        if (clazz.equals(String.class)) {
            return (T) new String(html);
        }
        T entity = Reflect.on(clazz).create().get();
        Map<String, Object> embeddables = new HashMap<String, Object>();
        for (Map.Entry<String, List<Extractor>> one : extractorsMap.entrySet()) {
            String name = one.getKey();
            List<Extractor> extractors = one.getValue();
            String result = html;
            for (Extractor extractor : extractors) {
                result = extractor.extract(result);
            }
            result = filterBefore(result);
            result = filter(name, result);
            result = filterAfter(result);
            try {
                //process embeddable
                if (name.contains(".")) {
                    String fieldName = StringUtils.substringBefore(name, ".");
                    String embeddableFieldName = StringUtils.substringAfter(name, ".");
                    Object embeddable = embeddables.get(fieldName);
                    Field field = Reflect.on(entity).field0(fieldName);
                    if (embeddable == null) {
                        embeddable = Reflect.on(field.getType()).create().get();
                        embeddables.put(fieldName, embeddable);
                    }
                    Reflect.on(embeddable).set(embeddableFieldName, result);
                    Reflect.on(entity).set(fieldName, embeddable);
                } else {
                    Reflect.on(entity).set(name, result);
                }
            } catch (Exception e) {
                LOGGER.error("convert to bean error! can't set '{}' with '{}'", name, result, e);
            }
        }
        return entity;
    }

    private Map<String, String> extractMap(String html) {
        Map<String, String> map = new LinkedHashMap<>();
        for (Map.Entry<String, List<Extractor>> one : extractorsMap.entrySet()) {
            String name = one.getKey();
            List<Extractor> extractors = one.getValue();
            String result = html;
            for (Extractor extractor : extractors) {
                result = extractor.extract(result);
            }
            result = filterBefore(result);
            result = filter(name, result);
            result = filterAfter(result);
            try {
                map.put(name, result);
            } catch (Exception e) {
                LOGGER.error("convert to map error! can't set '{}' with '{}'", name, result, e);
            }
        }
        return map;
    }

    private String filter(String name, String result) {
        List<Filter> filters = filtersMap.getOrDefault(name, new LinkedList<>());
        for (Filter filter : filters) {
            result = filter.process(result);
        }
        return result;
    }

    private String filterBefore(String result) {
        for (Filter filter : beforeFilter) {
            result = filter.process(result);
        }
        return result;
    }

    private String filterAfter(String result) {
        for (Filter filter : afterFilter) {
            result = filter.process(result);
        }
        return result;
    }

    //------------ internal --------------//


    /**
     * return a selector extractor for xml.
     *
     * @param query
     * @return
     */
    public static SelectorExtractor selector(String query) {
        return new SelectorExtractor(query);
    }

    /**
     * return a selector extractor for xml.
     *
     * @param query
     * @param eq
     * @return
     */
    public static SelectorExtractor selector(String query, int eq) {
        return new SelectorExtractor(query, eq);
    }

    /**
     * return a selector extractor for xml.
     *
     * @param query
     * @param eq      element index
     * @param outType
     * @return
     */
    public static SelectorExtractor selector(String query, int eq, String outType) {
        return new SelectorExtractor(query, eq, outType);
    }

    /**
     * return a selector extractor for html.
     *
     * @param query
     * @return
     */
    public static SelectorExtractor selectorh(String query) {
        return new SelectorExtractor(query).htmlParser();
    }

    /**
     * return a selector extractor for html.
     *
     * @param query
     * @param eq    element index
     * @return
     */
    public static SelectorExtractor selectorh(String query, int eq) {
        return new SelectorExtractor(query, eq).htmlParser();
    }

    /**
     * return a selector extractor for html.
     *
     * @param query
     * @param eq      element index
     * @param outType
     * @return
     */
    public static SelectorExtractor selectorh(String query, int eq, String outType) {
        return new SelectorExtractor(query, eq, outType).htmlParser();
    }


    /**
     * return a jerry extractor. please use {@link Extractors#selector(String)}
     *
     * @param query
     * @return
     */
    @Deprecated
    public static JerryExtractor jerry(String query) {
        return new JerryExtractor(query);
    }

    /**
     * return a json extractor.
     *
     * @param query
     * @return
     */
    public static JSONPathExtractor json(String query) {
        return new JSONPathExtractor(query);
    }

    /**
     * return a xpath extractor.
     *
     * @param query
     * @return
     */
    public static XPathExtractor xpath(String query) {
        return new XPathExtractor(query);
    }

    /**
     * return a regex extractor.
     *
     * @param query
     * @return
     */
    public static RegexExtractor regex(String query) {
        return new RegexExtractor(query);
    }

    /**
     * return a string range extractor.
     *
     * @param query
     * @return
     */
    public static StringRangeExtractor stringRange(String query) {
        return new StringRangeExtractor(query);
    }
}
