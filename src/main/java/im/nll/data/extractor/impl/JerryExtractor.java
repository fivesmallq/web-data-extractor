package im.nll.data.extractor.impl;

import im.nll.data.extractor.ListableExtractor;
import im.nll.data.extractor.annotation.Name;
import im.nll.data.extractor.utils.StringUtils;
import jodd.jerry.Jerry;
import jodd.lagarto.dom.Node;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.util.LinkedList;
import java.util.List;

import static jodd.jerry.Jerry.jerry;

/**
 * jquery selector impl by jerry. please use {@link SelectorExtractor}
 *
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/1/8 下午4:17
 */
@Name("jerry")
@Deprecated
public class JerryExtractor implements ListableExtractor {
    private final static String TYPE_TEXT = "text";
    private final static String TYPE_HTML = "html";
    /**
     * css selector
     */
    private String query;
    /**
     * 元素序号,0代表第一个(默认第一个元素)
     */
    private int eq = 0;
    /**
     * 输出类型 <li>text 只输出文本.</li> <li>html 输出带有html格式.</li><li>
     * 如果想获取其他属性,直接写属性名,比如'href'则输出元素的href属性值</li>
     */
    private String outType = "text";

    public JerryExtractor(String query) {
        this.query = query;
        String outType = StringUtils.substringAfterLast(query, ".");
        if (outType.equalsIgnoreCase(TYPE_TEXT)) {
            this.query = StringUtils.substringBeforeLast(query, ".text");
            this.outType = TYPE_TEXT;
        }
        if (outType.equalsIgnoreCase(TYPE_HTML)) {
            this.query = StringUtils.substringBeforeLast(query, ".html");
            this.outType = TYPE_HTML;
        }
        if (outType.matches("attr\\(\\S+\\)")) {
            this.query = StringUtils.substringBeforeLast(query, "." + outType);
            this.outType = StringUtils.substringBetween(outType, "(", ")");
        }
    }

    @Override
    public String extract(String data) {
        Jerry doc = jerry(data);
        String result = "";
        switch (outType) {
            case TYPE_TEXT:
                result = parse(doc.$(query).first().text());
                break;
            case TYPE_HTML:
                result = parse(doc.$(query).first().html());
                break;
            default:
                result = parse(doc.$(query).first().attr(outType));
                break;
        }
        return result;
    }

    @Override
    public List<String> extractList(String data) {
        List<String> strings = new LinkedList<>();
        Jerry doc = jerry(data);
        Node[] nodes = doc.$(query).get();
        for (Node node : nodes) {
            switch (outType) {
                case TYPE_TEXT:
                    strings.add(parse(node.getTextContent()));
                    break;
                case TYPE_HTML:
                    strings.add(parse(node.getHtml()));
                    break;
                default:
                    strings.add(parse(node.getAttribute(outType)));
                    break;
            }
        }
        return strings;
    }

    private String parse(String str) {
        Document document = Jsoup.parse(str, "", Parser.xmlParser());
        String result = "";
        switch (outType) {
            case TYPE_TEXT:
                result = document.text();
                break;
            case TYPE_HTML:
                result = document.html();
                break;
            default:
                result = document.text();
                break;
        }
        return result;
    }
}
