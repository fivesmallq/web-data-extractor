package im.nll.data.extractor.impl;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import im.nll.data.extractor.exception.ExtractException;
import org.jdom2.Namespace;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/1/6 上午12:32
 */
public class XPathExtractorTest {
    XPathExtractor xPathExtractor;
    private String baseHtml;
    private String base2Html;
    private String base3Html;
    private String base4Html;

    @Before
    public void setUp() throws Exception {
        baseHtml = Resources.toString(Resources.getResource("demo1.html"), Charsets.UTF_8);
        base2Html = Resources.toString(Resources.getResource("demo2.html"), Charsets.UTF_8);
        base3Html = Resources.toString(Resources.getResource("demo3.html"), Charsets.UTF_8);
        base4Html = Resources.toString(Resources.getResource("demo4.xml"), Charsets.UTF_8);
    }


    @Test
    public void testOwnText() {
        xPathExtractor = new XPathExtractor("//div[@class='contents']/div[1]/div[1]/text()").fixhtml();
        String s = xPathExtractor.extract(base3Html);
        Assert.assertEquals("&nbsp; 2013&nbsp;", s);
    }

    @Test
    public void testRemoveNamespaceError() {
        //attribute
        xPathExtractor = new XPathExtractor("//div/a[1]/@href").removeNamespace();
        String s = xPathExtractor.extract(baseHtml);
        Assert.assertEquals("/fivesmallq", s);
        //element
        xPathExtractor = new XPathExtractor("//div/a[1]").removeNamespace();
        s = xPathExtractor.extract(baseHtml);
        Assert.assertEquals("<a href=\"/fivesmallq\" class=\"title\">fivesmallq</a>", s);
        //text
        xPathExtractor = new XPathExtractor("//div/a[1]/text()").removeNamespace();
        s = xPathExtractor.extract(baseHtml);
        Assert.assertEquals("fivesmallq", s);
    }

    @Test
    public void testRigisterNameSpace() {
        //attribute
        xPathExtractor = new XPathExtractor("//oa:Task/@href").registerNamespace("oa", "http://www.xx.com/xx");
        String s = xPathExtractor.extract(base4Html);
        Assert.assertEquals("/fivesmallq", s);
        //element
        // register namespace use jdom2 namespace
        Namespace namespace = Namespace.getNamespace("oa", "http://www.xx.com/xx");
        xPathExtractor = new XPathExtractor("//oa:Task").registerNamespace(namespace);
        s = xPathExtractor.extract(base4Html);
        Assert.assertEquals("<oa:Task xmlns:oa=\"http://www.xx.com/xx\" href=\"/fivesmallq\">ReceiveKeeper</oa:Task>", s);
        //text
        xPathExtractor = new XPathExtractor("//oa:Task/text()").registerNamespace("oa", "http://www.xx.com/xx");
        s = xPathExtractor.extract(base4Html);
        Assert.assertEquals("ReceiveKeeper", s);
    }

    @Test(expected = ExtractException.class)
    public void testExtractParseError() throws Exception {
        //attribute
        xPathExtractor = new XPathExtractor("//div/a[1]/@href");
        // parse not standard xml error without fixhtml.
        String s = xPathExtractor.extract(base2Html);
        Assert.assertEquals("/fivesmallq", s);
        //element
        xPathExtractor = new XPathExtractor("//div/a[1]");
        s = xPathExtractor.extract(baseHtml);
        Assert.assertEquals("<a href=\"/fivesmallq\" class=\"title\">fivesmallq</a>", s);
        //text
        xPathExtractor = new XPathExtractor("//div/a[1]/text()");
        s = xPathExtractor.extract(baseHtml);
        Assert.assertEquals("fivesmallq", s);
    }

    @Test
    public void testFixhtml() throws Exception {
        //attribute
        xPathExtractor = new XPathExtractor("//div/a[1]/@href").fixhtml();
        String s = xPathExtractor.extract(base2Html);
        Assert.assertEquals("http://chat.stackoverflow.com", s);
        //element
        xPathExtractor = new XPathExtractor("//div/a[1]").fixhtml();
        s = xPathExtractor.extract(baseHtml);
        //XXX fixhtml option has changed the order of attributes.
        Assert.assertEquals("<a class=\"title\" href=\"/fivesmallq\">fivesmallq</a>", s);
        //text
        xPathExtractor = new XPathExtractor("//div/a[1]/text()").fixhtml();
        s = xPathExtractor.extract(baseHtml);
        Assert.assertEquals("fivesmallq", s);
    }

    @Test
    public void testExtract() throws Exception {
        //attribute
        xPathExtractor = new XPathExtractor("//div/a[1]/@href");
        String s = xPathExtractor.extract(baseHtml);
        Assert.assertEquals("/fivesmallq", s);
        //element
        xPathExtractor = new XPathExtractor("//div/a[1]");
        s = xPathExtractor.extract(baseHtml);
        Assert.assertEquals("<a href=\"/fivesmallq\" class=\"title\">fivesmallq</a>", s);
        //text
        xPathExtractor = new XPathExtractor("//div/a[1]/text()");
        s = xPathExtractor.extract(baseHtml);
        Assert.assertEquals("fivesmallq", s);
    }

    @Test
    public void testExtractList() throws Exception {
        //attribute
        xPathExtractor = new XPathExtractor("//div/a/@href");
        List<String> s = xPathExtractor.extractList(baseHtml);
        Assert.assertNotNull(s);
        Assert.assertEquals(2, s.size());
        String second = s.get(1);
        Assert.assertEquals("/fivesmallq/followers", second);

        //element
        xPathExtractor = new XPathExtractor("//div/a");
        s = xPathExtractor.extractList(baseHtml);
        Assert.assertNotNull(s);
        Assert.assertEquals(2, s.size());
        second = s.get(1);
        Assert.assertEquals("<a href=\"/fivesmallq/followers\">29671 Followers</a>", second);

        //text
        xPathExtractor = new XPathExtractor("//div/a/text()");
        s = xPathExtractor.extractList(baseHtml);
        Assert.assertNotNull(s);
        Assert.assertEquals(2, s.size());
        second = s.get(1);
        Assert.assertEquals("29671 Followers", second);
    }
}
