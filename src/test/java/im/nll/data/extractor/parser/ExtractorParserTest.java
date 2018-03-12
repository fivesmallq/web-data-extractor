package im.nll.data.extractor.parser;

import im.nll.data.extractor.Extractor;
import im.nll.data.extractor.exception.ParseException;
import im.nll.data.extractor.impl.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/1/19 下午1:37
 */
public class ExtractorParserTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testParse() throws Exception {
        //json
        String string = "json:$..books";
        Extractor extractor = ExtractorParser.parse(string);
        Assert.assertNotNull(extractor);
        Assert.assertEquals(JSONPathExtractor.class, extractor.getClass());

        //jerry
        string = "jerry:div.item.html";
        extractor = ExtractorParser.parse(string);
        Assert.assertNotNull(extractor);
        Assert.assertEquals(JerryExtractor.class, extractor.getClass());

        //regex
        string = "regex:\\d+";
        extractor = ExtractorParser.parse(string);
        Assert.assertNotNull(extractor);
        Assert.assertEquals(RegexExtractor.class, extractor.getClass());

        //xpath
        string = "xpath://td";
        extractor = ExtractorParser.parse(string);
        Assert.assertNotNull(extractor);
        Assert.assertEquals(XPathExtractor.class, extractor.getClass());

        //htmlcleaner
        string = "htmlcleaner://td";
        extractor = ExtractorParser.parse(string);
        Assert.assertNotNull(extractor);
        Assert.assertEquals(HtmlCleanerExtractor.class, extractor.getClass());

        //stringRange
        string = "stringRange:open,close";
        extractor = ExtractorParser.parse(string);
        Assert.assertNotNull(extractor);
        Assert.assertEquals(StringRangeExtractor.class, extractor.getClass());
    }

    @Test(expected = ParseException.class)
    public void testParseError() throws Exception {
        String string = "json2:$..books";
        ExtractorParser.parse(string);
    }

    @Test
    public void testStringRangeParse() throws Exception {
        String string = "stringRange:<li style=\"background: rgba(0, 0, 0, 0) none repeat scroll 0% 0%;\">,</li>";
        ExtractorParser.parse(string);
    }
}
