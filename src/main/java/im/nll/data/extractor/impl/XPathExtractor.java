package im.nll.data.extractor.impl;

import im.nll.data.extractor.ListableExtractor;
import im.nll.data.extractor.annotation.Name;
import im.nll.data.extractor.exception.ExtractException;
import im.nll.data.extractor.utils.Logs;
import im.nll.data.extractor.utils.XmlUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.jdom2.*;
import org.jdom2.filter.Filters;
import org.jdom2.input.DOMBuilder;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.slf4j.Logger;

import javax.xml.parsers.ParserConfigurationException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
    private boolean fixhtml = false;
    List<Namespace> namespaces = new ArrayList<>();

    public XPathExtractor(String xpath) {
        this.xpath = xpath;
    }

    /**
     * remove xml namespace
     *
     * @return
     */
    public XPathExtractor removeNamespace() {
        this.removeNamespace = true;
        return this;
    }

    /**
     * register xml namespace
     *
     * @param namespace
     * @return
     */
    public XPathExtractor registerNamespace(Namespace namespace) {
        this.namespaces.add(namespace);
        return this;
    }

    /**
     * register xml namespace
     *
     * @param prefix namespace prefix
     * @param url    namespace url
     * @return
     */
    public XPathExtractor registerNamespace(String prefix, String url) {
        this.namespaces.add(Namespace.getNamespace(prefix, url));
        return this;
    }

    /**
     * fix html to standard xml<p><font color=red>WARN: this option will change attribute order. </font></p>
     *
     * @return
     */
    public XPathExtractor fixhtml() {
        this.fixhtml = true;
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
        List<String> stringList = new LinkedList<>();
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
        // clean html use htmlcleaner
        if (fixhtml) {
            HtmlCleaner cleaner = new HtmlCleaner();
            CleanerProperties props = cleaner.getProperties();
            props.setUseCdataForScriptAndStyle(false);
            props.setRecognizeUnicodeChars(true);
            props.setUseEmptyElementTags(true);
            props.setAdvancedXmlEscape(true);
            props.setTranslateSpecialEntities(false);
            props.setBooleanAttributeValues("empty");
            props.setAllowHtmlInsideAttributes(true);
            props.setPruneTags("script,style");

            try {
                if (removeNamespace) {
                    data = XmlUtils.removeNamespace(data);
                }
                TagNode tagNode = cleaner.clean(data);
                org.w3c.dom.Document doc = null;
                try {
                    doc = new DomSerializer(props, false).createDOM(tagNode);
                } catch (ParserConfigurationException e) {
                    LOGGER.error("conver dom error!", e);
                }
                DOMBuilder in = new DOMBuilder();
                Document jdomDoc = in.build(doc);
                return jdomDoc;
            } catch (Exception e) {
                throw new ExtractException(e);
            }
        } else {
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
    }

    private XPathExpression createXpathExpression() {
        XPathFactory xpfac = XPathFactory.instance();
        XPathExpression xp = null;
        if (namespaces.isEmpty()) {
            xp = xpfac.compile(xpath, Filters.fpassthrough());
        } else {
            xp = xpfac.compile(xpath, Filters.fpassthrough(), new LinkedHashMap<String, Object>(), namespaces.toArray(new Namespace[namespaces.size()]));
        }
        return xp;
    }

    private String wrap(Object text) {
        if (text != null) {
            if (text instanceof Attribute) {
                return StringEscapeUtils.unescapeHtml4(((Attribute) text).getValue());
            } else if (text instanceof Content) {
                XMLOutputter xout = new XMLOutputter(Format.getPrettyFormat());
                if (text instanceof Element) {
                    return StringEscapeUtils.unescapeHtml4(xout.outputString((Element) text));
                }
                if (text instanceof Text) {
                    return StringEscapeUtils.unescapeHtml4(xout.outputString((Text) text));
                }
            } else {
                LOGGER.error("unsupported document type", text.getClass());
            }
        }
        return "";
    }
}
