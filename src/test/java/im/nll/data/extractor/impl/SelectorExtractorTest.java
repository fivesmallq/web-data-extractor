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
    public void testExtract() throws Exception {
        selectorExtractor = new SelectorExtractor("th.color_tb");
        List<String> title = selectorExtractor.extract(html);
        Assert.assertEquals(title.get(0), "网站或网店信息");
        
        selectorExtractor = new SelectorExtractor("tr:has(td)", "", "1");
        List<String> datas = selectorExtractor.extract(html);
        Assert.assertEquals(datas.size(), 3);
        Assert.assertEquals(datas.get(0), "<td> 网站 </td> \n" +
                "<td>高德导航</td> \n" +
                "<td>www.anav.com</td>");
    }
}