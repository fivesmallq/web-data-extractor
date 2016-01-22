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
            html = Resources.toString(Resources.getResource("list.html"), Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testExtractEscaped() throws Exception {
        String demoHtml = "<td>www.baidu.com ,, www.google.com</a></td>";
        stringRangeExtractor = new StringRangeExtractor("<td>,\\,\\,,true");
        String result = stringRangeExtractor.extract(demoHtml);
        Assert.assertEquals("<td>www.baidu.com ,,", result);

        stringRangeExtractor = new StringRangeExtractor("<td>,\\,\\,,false");
        result = stringRangeExtractor.extract(demoHtml);
        Assert.assertEquals("www.baidu.com ", result);
    }

    @Test
    public void testExtract() throws Exception {
        String demoHtml = "<td>www.baidu.com</td>";
        stringRangeExtractor = new StringRangeExtractor("<td>,</td>");
        String result = stringRangeExtractor.extract(demoHtml);
        Assert.assertEquals(result, "www.baidu.com");

        stringRangeExtractor = new StringRangeExtractor("x,xx");
        result = stringRangeExtractor.extract(demoHtml);
        Assert.assertEquals(result, "");

        stringRangeExtractor = new StringRangeExtractor("<td>,</td>,true");
        result = stringRangeExtractor.extract(demoHtml);
        Assert.assertEquals(result, "<td>www.baidu.com</td>");

        stringRangeExtractor = new StringRangeExtractor("<td>,</td>,false");
        result = stringRangeExtractor.extract(demoHtml);
        Assert.assertEquals(result, "www.baidu.com");

    }

    @Test
    public void testExtractList() throws Exception {
        stringRangeExtractor = new StringRangeExtractor("<tr class=\"item\">,</tr>,true");
        List<String> stringList = stringRangeExtractor.extractList(html);
        Assert.assertNotNull(stringList);
        Assert.assertEquals(stringList.size(), 3);
        Assert.assertEquals("<tr class=\"item\">\n" +
                "        <td class=\"type\">dynamic</td>\n" +
                "        <td class=\"name\">Python</td>\n" +
                "        <td class=\"url\">https://www.python.org</td>\n" +
                "    </tr>", stringList.get(2));
        stringRangeExtractor = new StringRangeExtractor("<tr class=\"item\">,</tr>,false");
        stringList = stringRangeExtractor.extractList(html);
        Assert.assertNotNull(stringList);
        Assert.assertEquals(stringList.size(), 3);
        Assert.assertEquals("\n" +
                "        <td class=\"type\">dynamic</td>\n" +
                "        <td class=\"name\">Python</td>\n" +
                "        <td class=\"url\">https://www.python.org</td>\n" +
                "    ", stringList.get(2));

    }
}