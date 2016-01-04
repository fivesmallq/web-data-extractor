package im.nll.data.extractor.impl;

import com.google.common.collect.Lists;

import im.nll.data.extractor.Extractor;
import im.nll.data.extractor.utils.StringUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

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
public class SelectorExtractor implements Extractor {
    private final static int TYPE_TEXT = 0;
    private final static int TYPE_HTML = 1;
    private String query;
    /**
     * 元素序号,0代表第一个(默认第一个元素)
     */
    private int eq = 0;
    /**
     * 输出类型 <li>0 只输出文本.</li> <li>1 输出带有html格式.</li><li>
     * 如果想获取其他属性,直接写属性名,比如'href'则输出元素的href属性值</li>
     */
    private int outType = 0;
    private String attr;

    public SelectorExtractor(String... params) {
        this.query = params[0];
        if (params.length > 1 && StringUtils.isNotNullOrEmpty(params[1])) {
            this.eq = StringUtils.tryParseInt(params[1], 0);
        }
        if (params.length > 2 && StringUtils.isNotNullOrEmpty(params[2])) {
            this.outType = StringUtils.tryParseInt(params[2]);
            if (this.outType == -1) {
                attr = params[2];
            }
        }
    }

    @Override
    public List<String> extract(String data) {
    	List<String> strings = Lists.newArrayList();
        Document document = Jsoup.parse(data, "", Parser.xmlParser());
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
                    strings.add(element.attr(attr));
                    break;
            }
        }
        return strings;
    }
}
