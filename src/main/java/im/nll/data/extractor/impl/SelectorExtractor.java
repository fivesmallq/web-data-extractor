package im.nll.data.extractor.impl;

import im.nll.data.extractor.ListableExtractor;
import im.nll.data.extractor.annotation.Name;
import im.nll.data.extractor.utils.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;

/**
 * * 一个使用Jquery选择器的抽取器
 * <p>
 * 关于jquery选择器语法请参考 <a
 * href="http://www.w3school.com.cn/jquery/jquery_ref_selectors.asp"
 * >http://www.w3school.com.cn/jquery/jquery_ref_selectors.asp</a>
 * <p>
 * 因为是基于jsoup实现，可参考<a
 * href="http://jsoup.org/cookbook/extracting-data/selector-syntax"
 * >http://jsoup.org/cookbook/extracting-data/selector-syntax</a>
 *
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 15/12/25 下午9:25
 */
@Name("selector")
public class SelectorExtractor implements ListableExtractor {
    private final static String TYPE_TEXT = "text";
    private final static String TYPE_HTML = "html";
    /**
     * css selector
     */
    private String query;
    /**
     * the element index
     */
    private int eq = 0;
    /**
     * out type: <ul><li>text - text of element.</li> <li>html - html of element.</li><li>
     * attr - set to "href" means you want to get the href attribute of element</li>
     * </ul>
     */
    private String outType = "text";

    /**
     * data parser. default xmlParser.
     */
    private Parser parser = Parser.xmlParser();

    /**
     * @param query jquery selector
     * @param eq    the element index
     */
    public SelectorExtractor(String query, int eq) {
        this.query = query;
        this.eq = eq;
    }

    /**
     * @param query   jquery selector
     * @param eq      the element index
     * @param outType <ul><li>text - text of element.</li> <li>html - html of element.</li><li>
     *                attr - set to "href" means you want to get the href attribute of element</li>
     *                </ul>
     */
    public SelectorExtractor(String query, int eq, String outType) {
        this.query = query;
        this.eq = eq;
        if (outType.equals("0")) {
            this.outType = TYPE_TEXT;
        } else if (outType.equals("1")) {
            this.outType = TYPE_HTML;
        } else {
            this.outType = outType;
        }
    }

    public SelectorExtractor(String query) {
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

    /**
     * change parser to htmlParser.
     *
     * @return
     */
    public SelectorExtractor htmlParser() {
        this.parser = Parser.htmlParser();
        return this;
    }


    @Override
    public String extract(String data) {
        Document document = Jsoup.parse(data, "", parser);
        String result = "";
        switch (outType) {
            case TYPE_TEXT:
                result = document.select(query).eq(eq).text();
                break;
            case TYPE_HTML:
                result = document.select(query).eq(eq).html();
                break;
            default:
                result = document.select(query).eq(eq).attr(outType);
                break;
        }
        return result;
    }

    @Override
    public List<String> extractList(String content) {
        List<String> strings = new LinkedList<>();
        Document document = Jsoup.parse(content, "", parser);
        Elements elements = document.select(query);
        for (Element element : elements) {
            switch (outType) {
                case TYPE_TEXT:
                    strings.add(element.text());
                    break;
                case TYPE_HTML:
                    strings.add(element.html());
                    break;
                default:
                    strings.add(element.attr(outType));
                    break;
            }
        }
        return strings;
    }
}
