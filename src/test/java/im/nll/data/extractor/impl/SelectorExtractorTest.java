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
    SelectorExtractor selectorExtractor;

    @Before
    public void before() {
        try {
            html = Resources.toString(Resources.getResource("list.html"), Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public void testExtractHtml() throws Exception {
        selectorExtractor = new SelectorExtractor("tr:nth-child(2).html");
        String title = selectorExtractor.extract(html);
        Assert.assertEquals("<th>type</th> \n" +
                "<th>name</th> \n" +
                "<th>website</th>", title);
    }

    @Test
    public void testExtractList() throws Exception {
        selectorExtractor = new SelectorExtractor("tr:has(td)");
        List<String> datas = selectorExtractor.extractList(html);
        Assert.assertEquals(3, datas.size());
        Assert.assertEquals("static Java https://www.java.com", datas.get(0));
    }
}
