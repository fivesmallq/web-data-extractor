package im.nll.data.extractor.impl;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 15/12/30 上午10:49
 */
public class SelectorExtractorTest {
    private String html;
    private String html2;
    SelectorExtractor selectorExtractor;

    @Before
    public void before() {
        try {
            html = Resources.toString(Resources.getResource("list.html"), Charsets.UTF_8);
            html2 = Resources.toString(Resources.getResource("list2.html"), Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testExtractEscapedChar() throws Exception {
        String html = "<div id='Meebo.AdElement:Root'>text</div>";
        selectorExtractor = new SelectorExtractor("div[id=Meebo.AdElement:Root]");
        String text = selectorExtractor.extract(html);
        Assert.assertEquals("text", text);
    }

    @Test
    public void testExtractEscaped() throws Exception {
        selectorExtractor = new SelectorExtractor("th:contains(hello,w):nth-child(2).html");
        String title = selectorExtractor.extract(html);
        Assert.assertEquals("hello,world", title);
    }

    @Test
    public void testExtract() throws Exception {
        selectorExtractor = new SelectorExtractor("th.title");
        String title = selectorExtractor.extract(html);
        Assert.assertEquals("languages", title);
    }

    @Test
    public void testExtractAttr() throws Exception {
        selectorExtractor = new SelectorExtractor("a.attr(href)");
        String href = selectorExtractor.extract("<a href=baidu.com >href</a>");
        Assert.assertEquals("baidu.com", href);
    }

    @Test
    public void testExtractHtml() throws Exception {
        selectorExtractor = new SelectorExtractor("tr:nth-child(2).html");
        String title = selectorExtractor.extract(html);
        Assert.assertEquals("<th>type</th> \n" +
                "<th>name</th> \n" +
                "<th>website</th>", title);
    }

    @Test
    public void testExtractHtmlEq() throws Exception {
        selectorExtractor = new SelectorExtractor("tr", 1, "html");
        String title = selectorExtractor.extract(html);
        Assert.assertEquals("<th>type</th> \n" +
                "<th>name</th> \n" +
                "<th>website</th>", title);
    }

    @Test
    public void testExtractTextEq() throws Exception {
        selectorExtractor = new SelectorExtractor("tr", 1, "text");
        String title = selectorExtractor.extract(html);
        Assert.assertEquals("type name website", title);
        selectorExtractor = new SelectorExtractor("tr", 1);
        title = selectorExtractor.extract(html);
        Assert.assertEquals("type name website", title);
    }

    @Test
    public void testExtractText() throws Exception {
        selectorExtractor = new SelectorExtractor("tr:nth-child(2).text");
        String title = selectorExtractor.extract(html);
        Assert.assertEquals("type name website", title);
    }

    @Test
    public void testExtractList() throws Exception {
        selectorExtractor = new SelectorExtractor("tr:has(td)");
        List<String> datas = selectorExtractor.extractList(html);
        Assert.assertEquals(3, datas.size());
        Assert.assertEquals("static Java https://www.java.com", datas.get(0));
    }

    @Test
    public void testExtractList2() throws Exception {
        // .html mehtod only return html in the element
        selectorExtractor = new SelectorExtractor("a.h4.html");
        List<String> datas = selectorExtractor.extractList(html2);
        Assert.assertEquals(10, datas.size());
        Assert.assertEquals("「咳咳咳」春季咳不停 这些药物要记牢", datas.get(0));
    }
}
