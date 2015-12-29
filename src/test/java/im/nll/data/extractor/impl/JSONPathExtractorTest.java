package im.nll.data.extractor.impl;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 15/12/29 上午10:33
 */
public class JSONPathExtractorTest {
    private String json;
    private JSONPathExtractor jsonPathExtractor;

    @Before
    public void before() {
        try {
            json = Resources.toString(Resources.getResource("example.json"), Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testExtract() throws Exception {
        String author0 = new JSONPathExtractor("$.store.book[0].author").extract(json);
        String author1 = new JSONPathExtractor("$.store.book[1].author").extract(json);
        Assert.assertEquals(author0, "Nigel Rees");
        Assert.assertEquals(author1, "Evelyn Waugh");
    }
}