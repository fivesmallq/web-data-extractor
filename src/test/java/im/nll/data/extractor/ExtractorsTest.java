package im.nll.data.extractor;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import im.nll.data.extractor.entity.Base;
import im.nll.data.extractor.entity.Language;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static im.nll.data.extractor.Extractors.regex;
import static im.nll.data.extractor.Extractors.selector;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 15/12/25 下午9:34
 */
public class ExtractorsTest {
    private String baseHtml;
    private String listHtml;


    @Before
    public void before() {
        try {
            baseHtml = Resources.toString(Resources.getResource("base.html"), Charsets.UTF_8);
            listHtml = Resources.toString(Resources.getResource("list.html"), Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGet() throws Exception {
        String title = Extractors.on(baseHtml).extract(selector("a.title")).asString();
        String followers = Extractors.on(baseHtml).extract(selector("div.followers")).with(regex("\\d+")).asString();
        String description = Extractors.on(baseHtml).extract(selector("div.description")).asString();
        Assert.assertEquals("fivesmallq", title);
        Assert.assertEquals("29671", followers);
        Assert.assertEquals("Talk is cheap. Show me the code.", description);
    }

    @Test
    public void testToMap() throws Exception {
        Map<String, String> dataMap = Extractors.on(baseHtml)
                .extract("title", selector("a.title"))
                .extract("followers", selector("div.followers")).with(regex("\\d+"))
                .extract("description", selector("div.description"))
                .asMap();
        Assert.assertEquals("fivesmallq", dataMap.get("title"));
        Assert.assertEquals("29671", dataMap.get("followers"));
        Assert.assertEquals("Talk is cheap. Show me the code.", dataMap.get("description"));
    }

    @Test
    public void testToMapList() throws Exception {
        List<Map<String, String>> languages = Extractors.on(listHtml).split(selector("tr.item,0,html"))
                .extract("type", selector("td.type"))
                .extract("name", selector("td.name"))
                .extract("url", selector("td.url"))
                .asMapList();
        Assert.assertNotNull(languages);
        Map<String, String> second = languages.get(1);
        Assert.assertEquals(languages.size(), 3);
        Assert.assertEquals(second.get("type"), "dynamic");
        Assert.assertEquals(second.get("name"), "Ruby");
        Assert.assertEquals(second.get("url"), "https://www.ruby-lang.org");
    }

    @Test
    public void testToBean() throws Exception {
        Base base = Extractors.on(baseHtml)
                .extract("title", selector("a.title"))
                .extract("followers", selector("div.followers")).with(regex("\\d+"))
                .extract("description", selector("div.description"))
                .asBean(Base.class);
        Assert.assertEquals("fivesmallq", base.getTitle());
        Assert.assertEquals("29671", base.getFollowers());
        Assert.assertEquals("Talk is cheap. Show me the code.", base.getDescription());
    }

    @Test
    public void testToBeanList() throws Exception {
        List<Language> languages = Extractors.on(listHtml).split(selector("tr.item,0,html"))
                .extract("type", selector("td.type"))
                .extract("name", selector("td.name"))
                .extract("url", selector("td.url"))
                .asBeanList(Language.class);
        Assert.assertNotNull(languages);
        Language second = languages.get(1);
        Assert.assertEquals(languages.size(), 3);
        Assert.assertEquals(second.getType(), "dynamic");
        Assert.assertEquals(second.getName(), "Ruby");
        Assert.assertEquals(second.getUrl(), "https://www.ruby-lang.org");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSplit() {
        //regex is not implements ListableExtractor
        List<Language> languages = Extractors.on(listHtml).split(regex("tr.item.html"))
                .extract("type", selector("td.type"))
                .extract("name", selector("td.name"))
                .extract("url", selector("td.url"))
                .asBeanList(Language.class);
        Assert.assertNotNull(languages);
        Language second = languages.get(1);
        Assert.assertEquals(languages.size(), 3);
        Assert.assertEquals(second.getType(), "dynamic");
        Assert.assertEquals(second.getName(), "Ruby");
        Assert.assertEquals(second.getUrl(), "https://www.ruby-lang.org");
    }

}