package im.nll.data.extractor.impl;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/1/6 上午12:32
 */
public class HtmlCleanerExtractorTest {
    HtmlCleanerExtractor xPathExtractor;
    private String baseHtml;

    @Before
    public void setUp() throws Exception {
        baseHtml = Resources.toString(Resources.getResource("demo1.html"), Charsets.UTF_8);
    }

    @Test
    public void testExtract() throws Exception {
        //attribute
        xPathExtractor = new HtmlCleanerExtractor("//div/a/@href");
        String s = xPathExtractor.extract(baseHtml);
        Assert.assertEquals("/fivesmallq", s);
        //element
        xPathExtractor = new HtmlCleanerExtractor("//div/a[1]");
        s = xPathExtractor.extract(baseHtml);
        Assert.assertEquals("<a href=\"/fivesmallq\" class=\"title\">fivesmallq</a>", s);
        //text
        xPathExtractor = new HtmlCleanerExtractor("//div/a[1]/text()");
        s = xPathExtractor.extract(baseHtml);
        Assert.assertEquals("fivesmallq", s);
    }

    @Test
    public void testExtractList() throws Exception {
        //attribute
        xPathExtractor = new HtmlCleanerExtractor("//div/a/@href");
        List<String> s = xPathExtractor.extractList(baseHtml);
        Assert.assertNotNull(s);
        Assert.assertEquals(2, s.size());
        String second = s.get(1);
        Assert.assertEquals("/fivesmallq/followers", second);

        //element
        xPathExtractor = new HtmlCleanerExtractor("//div/a");
        s = xPathExtractor.extractList(baseHtml);
        Assert.assertNotNull(s);
        Assert.assertEquals(2, s.size());
        second = s.get(1);
        Assert.assertEquals("<a href=\"/fivesmallq/followers\">29671 Followers</a>", second);

        //text
        xPathExtractor = new HtmlCleanerExtractor("//div/a/text()");
        s = xPathExtractor.extractList(baseHtml);
        Assert.assertNotNull(s);
        Assert.assertEquals(2, s.size());
        second = s.get(1);
        Assert.assertEquals("29671 Followers", second);
    }
}