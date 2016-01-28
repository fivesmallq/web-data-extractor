package im.nll.data.extractor.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import im.nll.data.extractor.ListableExtractor;
import im.nll.data.extractor.annotation.Name;
import im.nll.data.extractor.exception.ExtractException;
import im.nll.data.extractor.utils.Logs;
import im.nll.data.extractor.utils.XmlUtils;
import org.jdom2.*;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.slf4j.Logger;

import java.io.StringReader;
import java.util.List;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 15/12/25 下午9:26
 */
@Name("xpath")
public class XPathExtractor implements ListableExtractor {
    private static final Logger LOGGER = Logs.get();
    private String xpath;
    private boolean removeNamespace = false;
    List<Namespace> namespaces = Lists.newArrayList();

    public XPathExtractor(String xpath) {
        this.xpath = xpath;
    }

    /**
     * keep xml name space
     *
     * @return
     */
    public XPathExtractor removeNamespace() {
        this.removeNamespace = true;
        return this;
    }

    /**
     * keep xml name space
     *
     * @return
     */
    public XPathExtractor registerNamespace(String prefix, String url) {
        this.namespaces.add(Namespace.getNamespace(prefix, url));
        return this;
    }

    @Override
    public String extract(String data) {
        String result = "";
        try {
            Document doc = createDom(data);
            XPathExpression xp = createXpathExpression();
            Object text = xp.evaluateFirst(doc);
            result = wrap(text);
        } catch (Exception e) {
            throw new ExtractException(e);
        }
        return result;
    }

    @Override
    public List<String> extractList(String data) {
        List<String> stringList = Lists.newLinkedList();
        try {
            Document doc = createDom(data);
            XPathExpression xp = createXpathExpression();
            List<Object> texts = xp.evaluate(doc);
            for (Object text : texts) {
                String result = wrap(text);
                stringList.add(result);
            }
        } catch (Exception e) {
            throw new ExtractException(e);
        }
        return stringList;
    }

    private Document createDom(String data) {
        SAXBuilder sax = new SAXBuilder();
        try {
            if (removeNamespace) {
                data = XmlUtils.removeNamespace(data);
            }
            Document doc = sax.build(new StringReader(data));
            return doc;
        } catch (Exception e) {
            throw new ExtractException(e);
        }
    }

    private XPathExpression createXpathExpression() {
        XPathFactory xpfac = XPathFactory.instance();
        XPathExpression xp = null;
        if (namespaces.isEmpty()) {
            xp = xpfac.compile(xpath, Filters.fpassthrough());
        } else {
            xp = xpfac.compile(xpath, Filters.fpassthrough(), Maps.newLinkedHashMap(), namespaces.toArray(new Namespace[namespaces.size()]));
        }
        return xp;
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