package im.nll.data.extractor;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import im.nll.data.extractor.entity.EntityExtractor;
import im.nll.data.extractor.entity.EntityListExtractor;
import im.nll.data.extractor.impl.*;
import im.nll.data.extractor.utils.Logs;
import im.nll.data.extractor.utils.Reflect;
import im.nll.data.extractor.utils.Validate;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 15/12/25 下午9:26
 */
public class WebDataExtractor {
    private static final Logger LOGGER = Logs.get();
    private String html;
    private List<String> htmlList;

    public WebDataExtractor(String html) {
        this.html = html;
    }

    public static WebDataExtractor of(String html) {
        return new WebDataExtractor(html);
    }

    public WebDataExtractor filter(Extractor extractor) {
        this.html = extractor.extract(html);
        return this;
    }

    public WebDataExtractor json(String... params) {
        this.html = new JSONPathExtractor(params).extract(html);
        return this;
    }

    public WebDataExtractor xpath(String... params) {
        this.html = new XPathExtractor(params).extract(html);
        return this;
    }

    public WebDataExtractor selector(String... params) {
        this.html = new SelectorExtractor(params).extract(html);
        return this;
    }

    public WebDataExtractor regex(String... params) {
        this.html = new RegexExtractor(params).extract(html);
        return this;
    }

    public WebDataExtractor stringRange(String... params) {
        this.html = new StringRangeExtractor(params).extract(html);
        return this;
    }

    /**
     * split html use listable extractor
     *
     * @param listableExtractor
     * @return
     */
    public WebDataExtractor split(ListableExtractor listableExtractor) {
        this.htmlList = listableExtractor.extractList(html);
        return this;
    }

    public String asString() {
        return html;
    }

    public Map<String, String> asMap(Extractors... extractors) {
        Map<String, String> dataMap = Maps.newLinkedHashMap();
        for (Extractors extractorsOne : extractors) {
            String data = extractorsOne.extract(this.html);
            dataMap.put(extractorsOne.getName(), data);
        }
        return dataMap;
    }

    public <T> T asBean(Class<T> clazz, Extractors... extractors) {
        T entity = Reflect.on(clazz).create().get();
        for (Extractors extractorsOne : extractors) {
            String name = extractorsOne.getName();
            String data = extractorsOne.extract(this.html);
            try {
                Reflect.on(entity).set(name, data);
            } catch (Exception e) {
                LOGGER.error("convert to bean error! can't set '{}' with '{}'", name, data, e);
            }
        }
        return entity;
    }

    public <T> List<T> asBeanList(Class<T> clazz, Extractors... extractors) {
        Validate.notNull(htmlList, "must split first!");
        List<T> entityList = Lists.newLinkedList();
        for (String html : htmlList) {
            T entity = Reflect.on(clazz).create().get();
            for (Extractors extractorsOne : extractors) {
                String name = extractorsOne.getName();
                String data = extractorsOne.extract(html);
                try {
                    Reflect.on(entity).set(name, data);
                } catch (Exception e) {
                    LOGGER.error("convertt to bean error! can't set '{}' with '{}'", name, data, e);
                }
            }
            entityList.add(entity);
        }
        return entityList;
    }

    public <T> List<T> asBeanList(EntityListExtractor<T> entityListExtractor) {
        return entityListExtractor.extractList(this.html);
    }

    public <T> List<T> asBeanList(EntityExtractor<T> entityExtractor) {
        Validate.notNull(htmlList, "must split first!");
        List<T> entityList = Lists.newLinkedList();
        for (String html : htmlList) {
            T entity = entityExtractor.extract(html);
            entityList.add(entity);
        }
        return entityList;
    }
}
