package im.nll.data.extractor.impl;

import com.google.common.collect.Lists;
import im.nll.data.extractor.ListableExtractor;
import im.nll.data.extractor.utils.Logs;
import org.jdom2.*;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 15/12/25 下午9:26
 */
public class XPathExtractor implements ListableExtractor {
    private static final Logger LOGGER = Logs.get();
    private String xpath;

    public XPathExtractor(String xpath) {
        this.xpath = xpath;
    }

    @Override
    public String extract(String data) {
        SAXBuilder sax = new SAXBuilder();
        String result = "";
        try {
            Document doc = sax.build(new StringReader(data));
            XPathFactory xpfac = XPathFactory.instance();
            XPathExpression xp = xpfac.compile(xpath, Filters.fpassthrough());
            Object text = xp.evaluateFirst(doc);
            result = wrap(text);
            return result;
        } catch (JDOMException e) {
            LOGGER.error("extract data error. xpath:{} html:{}", xpath, data, e);
        } catch (IOException e) {
            LOGGER.error("extract data error. xpath:{} html:{}", xpath, data, e);
        }
        return result;
    }

    @Override
    public List<String> extractList(String data) {
        SAXBuilder sax = new SAXBuilder();
        List<String> stringList = Lists.newLinkedList();
        try {
            Document doc = sax.build(new StringReader(data));
            XPathFactory xpfac = XPathFactory.instance();
            XPathExpression xp = xpfac.compile(xpath, Filters.fpassthrough());
            List<Object> texts = xp.evaluate(doc);
            for (Object text : texts) {
                String result = wrap(text);
                stringList.add(result);
            }
            return stringList;
        } catch (JDOMException e) {
            LOGGER.error("extract data error. xpath:{} html:{}", xpath, data, e);
        } catch (IOException e) {
            LOGGER.error("extract data error. xpath:{} html:{}", xpath, data, e);
        }
        return stringList;
    }

    private String wrap(Object text) {
        if (text != null) {
            if (text instanceof Attribute) {
                return ((Attribute) text).getValue();
            } else if (text instanceof Content) {
                XMLOutputter xout = new XMLOutputter(Format.getPrettyFormat());
                if (text instanceof Element) {
                    return xout.outputString((Element) text);
                }
                if (text instanceof Text) {
                    return xout.outputString((Text) text);
                }
            } else {
                LOGGER.error("unsupported document type", text.getClass());
            }
        }
        return "";
    }


}
