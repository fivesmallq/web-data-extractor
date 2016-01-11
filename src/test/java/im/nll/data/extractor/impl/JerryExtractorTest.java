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
 * @date 16/1/11 下午4:03
 */
public class JerryExtractorTest {

    private String html;
    JerryExtractor selectorExtractor;

    @Before
    public void before() {
        try {
            html = Resources.toString(Resources.getResource("list.html"), Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testExtract() throws Exception {
        selectorExtractor = new JerryExtractor("th.title");
        String title = selectorExtractor.extract(html);
        Assert.assertEquals("languages", title);
    }

    @Test
    public void testExtractHtml() throws Exception {
        selectorExtractor = new JerryExtractor("tr:eq(1).html");
        String title = selectorExtractor.extract(html);
        Assert.assertEquals("<th>type</th> \n" +
                "<th>name</th> \n" +
                "<th>website</th>", title);
    }

    @Test
    public void testExtractText() throws Exception {
        selectorExtractor = new JerryExtractor("tr:eq(1).text");
        String title = selectorExtractor.extract(html);
        Assert.assertEquals("type name website", title);
    }

    @Test
    public void testExtractText2() throws Exception {
        selectorExtractor = new JerryExtractor("td:eq(1).text");
        String title = selectorExtractor.extract(html);
        Assert.assertEquals("Java", title);
    }

    @Test
    public void testExtractAttr() throws Exception {
        selectorExtractor = new JerryExtractor("td:eq(1).attr(class)");
        String title = selectorExtractor.extract(html);
        Assert.assertEquals("name", title);
    }

    @Test
    public void testExtractList() throws Exception {
        selectorExtractor = new JerryExtractor("tr:eq(1)");
        List<String> datas = selectorExtractor.extractList(html);
        Assert.assertEquals(1, datas.size());
        Assert.assertEquals("type name website", datas.get(0));
    }

    @Test
    public void testExtractListText() throws Exception {
        selectorExtractor = new JerryExtractor("tr.item.text");
        List<String> datas = selectorExtractor.extractList(html);
        Assert.assertEquals(3, datas.size());
        Assert.assertEquals("static Java https://www.java.com", datas.get(0));
    }

    @Test
    public void testExtractListHtml() throws Exception {
        selectorExtractor = new JerryExtractor("tr.item.html");
        List<String> datas = selectorExtractor.extractList(html);
        Assert.assertEquals(3, datas.size());
        Assert.assertEquals("<tr class=\"item\"> \n" +
                " <td class=\"type\">static</td> \n" +
                " <td class=\"name\">Java</td> \n" +
                " <td class=\"url\">https://www.java.com</td> \n" +
                "</tr>", datas.get(0));
    }

    @Test
    public void testExtractListAttr() throws Exception {
        selectorExtractor = new JerryExtractor("tr.item:eq(1) td.attr(class)");
        List<String> datas = selectorExtractor.extractList(html);
        Assert.assertEquals(3, datas.size());
        Assert.assertEquals("type", datas.get(0));
        Assert.assertEquals("name", datas.get(1));
        Assert.assertEquals("url", datas.get(2));
    }
}