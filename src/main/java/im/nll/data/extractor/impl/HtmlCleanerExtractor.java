package im.nll.data.extractor.impl;

import im.nll.data.extractor.ListableExtractor;
import im.nll.data.extractor.annotation.Name;
import im.nll.data.extractor.exception.ExtractException;
import im.nll.data.extractor.utils.Logs;
import im.nll.data.extractor.utils.TypeUtils;
import org.htmlcleaner.*;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 15/12/25 下午9:26
 */
@Name("htmlcleaner")
public class HtmlCleanerExtractor implements ListableExtractor {
    private static final Logger logger = Logs.get();
    private String xpath;

    public HtmlCleanerExtractor(String xpath) {
        this.xpath = xpath;
    }

    @Override
    public String extract(String content) {
        String result = "";
        try {
            HtmlCleaner htmlCleaner = getHtmlCleaner();
            TagNode node = htmlCleaner.clean(content);
            Object[] objects = node.evaluateXPath(xpath);
            if (objects != null && objects.length > 0) {
                result = wrap(objects[0], htmlCleaner);
            } else {
                logger.warn("not found content,xpath:{}", xpath);
                logger.debug("content:{}", content);
            }
        } catch (Exception e) {
            throw new ExtractException(e);
        }
        return result;
    }

    @Override
    public List<String> extractList(String content) {
        List<String> list = new ArrayList<>();
        try {
            HtmlCleaner htmlCleaner = getHtmlCleaner();
            TagNode node = htmlCleaner.clean(content);
            Object[] objects = node.evaluateXPath(xpath);
            if (objects != null && objects.length > 0) {
                for (int i = 0; i < objects.length; i++) {
                    list.add(wrap(objects[i], htmlCleaner));
                }
                return list;
            } else {
                logger.warn("not found content,xpath:{}", xpath);
                logger.debug("content:{}", content);
            }
        } catch (Exception e) {
            throw new ExtractException(e);
        }
        return list;
    }

    private HtmlCleaner getHtmlCleaner() {
        HtmlCleaner htmlCleaner = new HtmlCleaner();
        htmlCleaner.getProperties().setUseCdataForScriptAndStyle(false);
        htmlCleaner.getProperties().setPruneTags("script,style");
        htmlCleaner.getProperties().setTreatUnknownTagsAsContent(true);
        htmlCleaner.getProperties().setOmitUnknownTags(true);
        return htmlCleaner;
    }

    private String wrap(Object text, HtmlCleaner htmlCleaner) {
        if (text != null) {
            if (text instanceof TagNode) {
                final CleanerProperties cleanerProperties = htmlCleaner.getProperties();
                cleanerProperties.setOmitXmlDeclaration(true);
                final HtmlSerializer htmlSerializer = new PrettyHtmlSerializer(cleanerProperties);
                final String html = htmlSerializer.getAsString((TagNode) text);
                return html;
            } else {
                return TypeUtils.castToString(text);
            }
        }
        return "";
    }
}
