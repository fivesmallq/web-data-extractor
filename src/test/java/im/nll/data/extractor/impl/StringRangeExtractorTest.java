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
 * @date 15/12/30 下午3:42
 */
public class StringRangeExtractorTest {
    private String html;
    StringRangeExtractor stringRangeExtractor;

    @Before
    public void setUp() throws Exception {
        try {
            html = Resources.toString(Resources.getResource("list2.html"), Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testExtract() throws Exception {
        String demoHtml = "<td>www.baidu.com</td>";
        stringRangeExtractor = new StringRangeExtractor("<td>", "</td>");
        String result = stringRangeExtractor.extract(demoHtml);
        Assert.assertEquals(result, "www.baidu.com");

        stringRangeExtractor = new StringRangeExtractor("x", "xx");
        result = stringRangeExtractor.extract(demoHtml);
        Assert.assertEquals(result, "");

        stringRangeExtractor = new StringRangeExtractor("<td>", "</td>", "true");
        result = stringRangeExtractor.extract(demoHtml);
        Assert.assertEquals(result, "<td>www.baidu.com</td>");

        stringRangeExtractor = new StringRangeExtractor("<td>", "</td>", "false");
        result = stringRangeExtractor.extract(demoHtml);
        Assert.assertEquals(result, "www.baidu.com");
    }

    @Test
    public void testExtractList() throws Exception {
        stringRangeExtractor = new StringRangeExtractor("<td style", "</td>", "true");
        List<String> stringList = stringRangeExtractor.extractList(html);
        Assert.assertNotNull(stringList);
        Assert.assertEquals(stringList.size(), 5);
        Assert.assertEquals(stringList.get(3), "<td style=\"text-align:center;\">4</td>");
        stringRangeExtractor = new StringRangeExtractor("<td style", "</td>", "false");
        stringList = stringRangeExtractor.extractList(html);
        Assert.assertNotNull(stringList);
        Assert.assertEquals(stringList.size(), 5);
        Assert.assertEquals(stringList.get(3), "=\"text-align:center;\">4");

    }
}