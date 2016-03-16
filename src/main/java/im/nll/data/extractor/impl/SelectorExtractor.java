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
     * 元素序号,0代表第一个(默认第一个元素)
     */
    private int eq = 0;
    /**
     * 输出类型 <li>text 只输出文本.</li> <li>html 输出带有html格式.</li><li>
     * 如果想获取其他属性,直接写属性名,比如'href'则输出元素的href属性值</li>
     */
    private String outType = "text";

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

    @Override
    public String extract(String data) {
        Document document = Jsoup.parse(data, "", Parser.xmlParser());
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
        Document document = Jsoup.parse(content, "", Parser.xmlParser());
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
