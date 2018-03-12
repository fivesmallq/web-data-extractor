package im.nll.data.extractor;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;
import im.nll.data.extractor.entity.*;
import im.nll.data.extractor.rule.ExtractRule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static im.nll.data.extractor.Extractors.*;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 15/12/25 下午9:34
 */
public class ExtractorsTest {
    private String baseHtml;
    private String base4Html;
    private String listHtml;
    private String listHtml2;
    private String jsonString;
    private String base5Xml;


    @Before
    public void before() {
        try {
            baseHtml = Resources.toString(Resources.getResource("demo1.html"), Charsets.UTF_8);
            base4Html = Resources.toString(Resources.getResource("demo4.xml"), Charsets.UTF_8);
            listHtml = Resources.toString(Resources.getResource("list.html"), Charsets.UTF_8);
            listHtml2 = Resources.toString(Resources.getResource("list2.html"), Charsets.UTF_8);
            jsonString = Resources.toString(Resources.getResource("example.json"), Charsets.UTF_8);
            base5Xml = Resources.toString(Resources.getResource("demo5.xml"), Charsets.UTF_8);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGet() throws Exception {
        //use extract method to extract data
        String title = Extractors.on(baseHtml).extract(selector("a.title")).asString();
        //use with method to extend value extractor
        String followers = Extractors.on(baseHtml).extract(selector("div.followers")).with(regex("\\d+")).asString();
        //use filter method to process value
        String description = Extractors.on(baseHtml).extract(selector("div.description")).filter(value -> value.toLowerCase()).asString();
        String year = Extractors.on("<div> Talk is cheap. Show me the code. - Fri, 25 Aug 2000 </div>")
                .extract(selector("div")) // extract with selector
                .filter(value -> value.trim()) // trim result
                .with(regex("20\\d{2}")) // get year with regex
                .filter(value -> "from " + value) // append 'from' string
                .asString();
        Assert.assertEquals("fivesmallq", title);
        Assert.assertEquals("29671", followers);
        Assert.assertEquals("talk is cheap. show me the code.", description);
        Assert.assertEquals("from 2000", year);
    }

    @Test
    public void testAsStringNoName() throws Exception {
        String result = Extractors.on(baseHtml)
                .extract(selector("a.title"))
                .asString();
        Assert.assertEquals("fivesmallq", result);
        result = Extractors.on(baseHtml)
                .extract(selector("a.title")).extract(selector("*"))
                .asString();
        Assert.assertEquals("fivesmallq", result);
    }

    @Test
    public void testAsStringWithName() throws Exception {
        String result = Extractors.on(baseHtml)
                .extract("title", selector("a.title"))
                .asString();
        Assert.assertEquals("{title=fivesmallq}", result);
    }

    @Test
    public void testExtractByRule() throws Exception {
        String result = Extractors.on(baseHtml)
                .extract(ExtractRule.of("title", "selector:a.title"))
                .asString();
        Assert.assertEquals("{title=fivesmallq}", result);
    }

    @Test
    public void testExtractByMoreExtractor() throws Exception {
        String result = Extractors.on(baseHtml)
                .extract("title", selector("a.title"))
                //extract support multiple extractor
                .extract("followers", selector("div.followers"), (regex("\\d+")))
                .extract("description", selector("div.description"))
                .asString();
        Assert.assertEquals("{title=fivesmallq, followers=29671, description=Talk is cheap. Show me the code.}", result);
    }


    @Test
    public void testAsStringMap() throws Exception {
        String result = Extractors.on(baseHtml)
                .extract("title", selector("a.title"))
                .extract("followers", selector("div.followers")).with(regex("\\d+"))
                .extract("description", selector("div.description"))
                .asString();
        Assert.assertEquals("{title=fivesmallq, followers=29671, description=Talk is cheap. Show me the code.}", result);
    }

    @Test
    public void testAsStringMapList() throws Exception {
        String result = Extractors.on(listHtml).split(selector("tr.item.html"))
                .extract("type", selector("td.type"))
                .extract("name", selector("td.name"))
                .extract("url", selector("td.url")).asString();
        Assert.assertEquals("" +
                "[{type=static, name=Java, url=https://www.java.com}, " +
                "{type=dynamic, name=Ruby, url=https://www.ruby-lang.org}, " +
                "{type=dynamic, name=Python, url=https://www.python.org}]", result);
    }

    @Test
    public void testAsJSONStringNoName() throws Exception {
        String result = Extractors.on(baseHtml)
                .extract(selector("a.title"))
                .asJSONString();
        Assert.assertEquals("fivesmallq", result);
        result = Extractors.on(baseHtml)
                .extract(selector("a.title")).extract(selector("*"))
                .asJSONString();
        Assert.assertEquals("fivesmallq", result);
    }

    @Test
    public void testAsJSONStringWithName() throws Exception {
        String result = Extractors.on(baseHtml)
                .extract("title", selector("a.title"))
                .asJSONString();
        Assert.assertEquals("{\"title\":\"fivesmallq\"}", result);
    }

    @Test
    public void testAsJSONStringMap() throws Exception {
        String result = Extractors.on(baseHtml)
                .extract("title", selector("a.title"))
                .extract("followers", selector("div.followers")).with(regex("\\d+"))
                .extract("description", selector("div.description"))
                .asJSONString();
        Assert.assertEquals("{\"title\":\"fivesmallq\",\"followers\":\"29671\",\"description\":\"Talk is cheap. Show me the code.\"}", result);
    }

    @Test
    public void testAsJSONStringMapList() throws Exception {
        String result = Extractors.on(listHtml).split(selector("tr.item.html"))
                .extract("type", selector("td.type"))
                .extract("name", selector("td.name"))
                .extract("url", selector("td.url")).asJSONString();
        Assert.assertEquals("" +
                "[{\"type\":\"static\",\"name\":\"Java\",\"url\":\"https://www.java.com\"}," +
                "{\"type\":\"dynamic\",\"name\":\"Ruby\",\"url\":\"https://www.ruby-lang.org\"}," +
                "{\"type\":\"dynamic\",\"name\":\"Python\",\"url\":\"https://www.python.org\"}]", result);
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
        Assert.assertEquals("{title=fivesmallq, followers=29671, description=Talk is cheap. Show me the code.}", dataMap.toString());
    }

    @Test
    public void testToMapList() throws Exception {
        List<Map<String, String>> languages = Extractors.on(listHtml).split(selector("tr.item.html"))
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
        Assert.assertEquals("[{type=static, name=Java, url=https://www.java.com}," +
                " {type=dynamic, name=Ruby, url=https://www.ruby-lang.org}," +
                " {type=dynamic, name=Python, url=https://www.python.org}]", languages.toString());
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
        List<Language> languages = Extractors.on(listHtml).split(selector("tr.item.html"))
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

    @Test
    public void testExtractByMapToBeanList() throws Exception {
        Map<String, String> config = Maps.newHashMap();
        config.put("type", "selector:td.type");
        config.put("name", "selector:td.name");
        config.put("url", "selector:td.url");
        List<Language> languages = Extractors.on(listHtml).split(selector("tr.item.html"))
                .extract(config)
                .asBeanList(Language.class);
        Assert.assertNotNull(languages);
        Language second = languages.get(1);
        Assert.assertEquals(languages.size(), 3);
        Assert.assertEquals(second.getType(), "dynamic");
        Assert.assertEquals(second.getName(), "Ruby");
        Assert.assertEquals(second.getUrl(), "https://www.ruby-lang.org");
    }

    @Test
    public void testToBeanListString() throws Exception {
        //NOTE only support String type
        List<String> languages = Extractors.on(listHtml2).split(selector("a.h4.attr(href)"))
                .asBeanList(String.class);
        Assert.assertNotNull(languages);
        String second = languages.get(1);
        Assert.assertEquals(languages.size(), 10);
        Assert.assertEquals(second, "http://infect.dxy.cn/article/486796");
    }

    @Test
    public void testAsListString() throws Exception {
        List<String> languages = Extractors.on(listHtml2).split(selector("a.h4.attr(href)")).asStringList();
        Assert.assertNotNull(languages);
        String second = languages.get(1);
        Assert.assertEquals(languages.size(), 10);
        Assert.assertEquals(second, "http://infect.dxy.cn/article/486796");
    }


    @Test
    public void testAsStringList2() throws Exception {
        List<String> languages = Extractors.on(listHtml).split(selector("tr.item.html"))
                .extract("type", selector("td.type"))
                .extract("name", selector("td.name"))
                .extract("url", selector("td.url"))
                .asStringList();
        Assert.assertNotNull(languages);
        String second = languages.get(1);
        Assert.assertEquals(second, "static,Java,https://www.java.com");
    }

    @Test
    public void testAsStringListWithSeparator() throws Exception {
        List<String> languages = Extractors.on(listHtml).split(selector("tr.item.html"))
                .extract("type", selector("td.type"))
                .extract("name", selector("td.name"))
                .extract("url", selector("td.url"))
                .asStringList("||");
        Assert.assertNotNull(languages);
        String second = languages.get(1);
        Assert.assertEquals(second, "static||Java||https://www.java.com");
    }

    @Test
    public void testAsStringList() throws Exception {
        List<String> languages = Extractors.on(listHtml2).split(selector("a.h4.attr(href)")).asStringList();
        Assert.assertNotNull(languages);
        String second = languages.get(1);
        Assert.assertEquals(languages.size(), 10);
        Assert.assertEquals(second, "http://infect.dxy.cn/article/486796");
    }

    @Test
    public void testToBeanListByJerryString() throws Exception {
        List<Language> languages = Extractors.on(listHtml).split("jerry:tr.item.html")
                .extract("type", "jerry:td.type")
                .extract("name", "jerry:td.name")
                .extract("url", "jerry:td.url")
                .asBeanList(Language.class);
        Assert.assertNotNull(languages);
        Language second = languages.get(1);
        Assert.assertEquals(languages.size(), 3);
        Assert.assertEquals(second.getType(), "dynamic");
        Assert.assertEquals(second.getName(), "Ruby");
        Assert.assertEquals(second.getUrl(), "https://www.ruby-lang.org");
    }

    @Test
    public void testToBeanListByXPath() throws Exception {
        List<Language> languages = Extractors.on(listHtml).split(xpath("//tr[@class='item']"))
                .extract("type", xpath("//td[1]/text()"))
                .extract("name", xpath("//td[2]/text()"))
                .extract("url", xpath("//td[3]/text()"))
                .asBeanList(Language.class);
        Assert.assertNotNull(languages);
        Language second = languages.get(1);
        Assert.assertEquals(languages.size(), 3);
        Assert.assertEquals(second.getType(), "dynamic");
        Assert.assertEquals(second.getName(), "Ruby");
        Assert.assertEquals(second.getUrl(), "https://www.ruby-lang.org");
    }

    @Test
    public void testToBeanListByXPathString() throws Exception {
        List<Language> languages = Extractors.on(listHtml).split("xpath://tr[@class='item']")
                .extract("type", "xpath://td[1]/text()")
                .extract("name", "xpath://td[2]/text()")
                .extract("url", "xpath://td[3]/text()")
                .asBeanList(Language.class);
        Assert.assertNotNull(languages);
        Language second = languages.get(1);
        Assert.assertEquals(languages.size(), 3);
        Assert.assertEquals(second.getType(), "dynamic");
        Assert.assertEquals(second.getName(), "Ruby");
        Assert.assertEquals(second.getUrl(), "https://www.ruby-lang.org");
    }

    @Test
    public void testToMapByXPathWithNameSpace() throws Exception {
        Map<String, String> dataMap = Extractors.on(base4Html)
                .extract("href", xpath("//oa:Task/@href").registerNamespace("oa", "http://www.xx.com/xx"))
                .extract("task", xpath("//oa:Task/text()").registerNamespace("oa", "http://www.xx.com/xx"))
                .extract("level", xpath("//oa:OrganisationLevel/text()").registerNamespace("oa", "http://www.xx.com/xx"))
                .asMap();
        Assert.assertEquals("/fivesmallq", dataMap.get("href"));
        Assert.assertEquals("ReceiveKeeper", dataMap.get("task"));
        Assert.assertEquals("50", dataMap.get("level"));
    }


    @Test
    public void testToBeanListFilter() throws Exception {
        List<Language> languages = Extractors.on(listHtml).split(xpath("//tr[@class='item']"))
                .extract("type", xpath("//td[1]/text()")).filter(value -> "type:" + value)
                .extract("name", xpath("//td[2]/text()")).filter(value -> "name:" + value)
                .extract("url", xpath("//td[3]/text()")).filter(value -> "url:" + value)
                .asBeanList(Language.class);
        Assert.assertNotNull(languages);
        Language second = languages.get(1);
        Assert.assertEquals(languages.size(), 3);
        Assert.assertEquals(second.getType(), "type:dynamic");
        Assert.assertEquals(second.getName(), "name:Ruby");
        Assert.assertEquals(second.getUrl(), "url:https://www.ruby-lang.org");
    }

    @Test
    public void testToBeanListFilterBefore() throws Exception {
        List<Language> languages = Extractors.on(listHtml)
                .before(value -> "all-" + value)
                .split(xpath("//tr[@class='item']"))
                .extract("type", xpath("//td[1]/text()")).filter(value -> "type:" + value)
                .extract("name", xpath("//td[2]/text()")).filter(value -> "name:" + value)
                .extract("url", xpath("//td[3]/text()")).filter(value -> "url:" + value)
                .asBeanList(Language.class);
        Assert.assertNotNull(languages);
        Language second = languages.get(1);
        Assert.assertEquals(languages.size(), 3);
        Assert.assertEquals(second.getType(), "type:all-dynamic");
        Assert.assertEquals(second.getName(), "name:all-Ruby");
        Assert.assertEquals(second.getUrl(), "url:all-https://www.ruby-lang.org");
    }

    @Test
    public void testToBeanListFilterBeforeAndAfter() throws Exception {
        List<Language> languages = Extractors.on(listHtml)
                //before and after just process the extract value, then execute the follow filter method.
                .before(value -> "|before|" + value)
                .after(value -> value + "|after|")
                .split(xpath("//tr[@class='item']"))
                .extract("type", xpath("//td[1]/text()")).filter(value -> "filter:" + value)
                .extract("name", xpath("//td[2]/text()")).filter(value -> "filter:" + value)
                .extract("url", xpath("//td[3]/text()")).filter(value -> "filter:" + value)
                .asBeanList(Language.class);
        Assert.assertNotNull(languages);
        Language second = languages.get(1);
        Assert.assertEquals(languages.size(), 3);
        Assert.assertEquals(second.getType(), "filter:|before|dynamic|after|");
        Assert.assertEquals(second.getName(), "filter:|before|Ruby|after|");
        Assert.assertEquals(second.getUrl(), "filter:|before|https://www.ruby-lang.org|after|");
    }

    @Test
    public void testToBeanListByJson() throws Exception {
        List<Book> books = Extractors.on(jsonString).split(json("$..book.*"))
                .extract("category", json("$..category"))
                .extract("author", json("$..author"))
                .extract("title", json("$..title"))
                .extract("price", json("$..price"))
                .asBeanList(Book.class);
        Assert.assertNotNull(books);
        Book second = books.get(1);
        Assert.assertEquals(books.size(), 4);
        Assert.assertEquals(second.getCategory(), "fiction");
        Assert.assertEquals(second.getAuthor(), "Evelyn Waugh");
        Assert.assertEquals(second.getTitle(), "Sword of Honour");
        Assert.assertEquals(second.getPrice(), new Double(12.99));
    }

    @Test
    public void testToBeanListByJsonString() throws Exception {
        List<Book> books = Extractors.on(jsonString).split("json:$..book.*")
                .extract("category", "json:$..category")
                .extract("author", "json:$..author")
                .extract("title", "json:$..title")
                .extract("price", "json:$..price")
                .asBeanList(Book.class);
        Assert.assertNotNull(books);
        Book second = books.get(1);
        Assert.assertEquals(books.size(), 4);
        Assert.assertEquals(second.getCategory(), "fiction");
        Assert.assertEquals(second.getAuthor(), "Evelyn Waugh");
        Assert.assertEquals(second.getTitle(), "Sword of Honour");
        Assert.assertEquals(second.getPrice(), new Double(12.99));
    }

    @Test
    public void testToBeanListByEntityExtractor() throws Exception {
        List<Book> books = Extractors.on(jsonString).split(json("$..book.*")).asBeanList(new EntityExtractor<Book>() {
            @Override
            public Book extract(String data) {
                return Extractors.on(data)
                        .extract("category", json("$..category"))
                        .extract("author", json("$..author"))
                        .extract("title", json("$..title"))
                        .extract("price", json("$..price")).asBean(Book.class);
            }
        });
        Assert.assertNotNull(books);
        Book second = books.get(1);
        Assert.assertEquals(books.size(), 4);
        Assert.assertEquals(second.getCategory(), "fiction");
        Assert.assertEquals(second.getAuthor(), "Evelyn Waugh");
        Assert.assertEquals(second.getTitle(), "Sword of Honour");
        Assert.assertEquals(second.getPrice(), new Double(12.99));
    }

    @Test
    public void testToBeanListByEntityListExtractor() throws Exception {
        List<Book> books = Extractors.on(jsonString).asBeanList(new EntityListExtractor<Book>() {
            @Override
            public List<Book> extractList(String data) {
                return Extractors.on(jsonString).split(json("$..book.*"))
                        .extract("category", json("$..category"))
                        .extract("author", json("$..author"))
                        .extract("title", json("$..title"))
                        .extract("price", json("$..price"))
                        .asBeanList(Book.class);
            }
        });
        Assert.assertNotNull(books);
        Book second = books.get(1);
        Assert.assertEquals(books.size(), 4);
        Assert.assertEquals(second.getCategory(), "fiction");
        Assert.assertEquals(second.getAuthor(), "Evelyn Waugh");
        Assert.assertEquals(second.getTitle(), "Sword of Honour");
        Assert.assertEquals(second.getPrice(), new Double(12.99));
    }


    @Test
    public void testSplit() {
    	//regex is implements ListableExtractor
        List<Language> languages = Extractors.on(listHtml).split(regex("<tr class=\"item\">([\\s\\S]*?.*?)</tr>"))
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

    @Test
    public void testEmbeddable() {
        List<Activity> activities = Extractors.on(base5Xml)
                .split(xpath("//ProcessDefinition/activity").removeNamespace())
                .extract("name", xpath("//activity/@name"))
                .extract("type", xpath("//activity/type/text()"))
                .extract("resourceType", xpath("//activity/resourceType/text()"))
                .extract("config.encoding", xpath("//activity/config/encoding/text()"))
                .extract("config.pollInterval", xpath("//activity/config/pollInterval/text()"))
                //if pollInterval is null set to default '5'
                .filter(value -> value == null ? value : "5")
                .extract("config.compressFile", xpath("//activity/config/compressFile/text()"))
                .extract("inputBindings.fileName", xpath("//activity/inputBindings/WriteActivityInputTextClass/fileName/value-of/@select"))
                .extract("inputBindings.textContent", xpath("//activity/inputBindings/WriteActivityInputTextClass/textContent/value-of/@select"))
                .asBeanList(Activity.class);
        Assert.assertNotNull(activities);
        Assert.assertEquals(1, activities.size());
        Activity activity = activities.get(0);
        Assert.assertEquals("Output1", activity.getName());
        Assert.assertEquals("com.tibco.plugin.file.FileWriteActivity", activity.getType());
        //config
        Config config = activity.getConfig();
        Assert.assertEquals("text", config.getEncoding());
        Assert.assertEquals("None", config.getCompressFile());
        Assert.assertEquals("5", config.getPollInterval());
        //bind
        BindingSpec bindingSpec = activity.getInputBindings();
        Assert.assertEquals("$_globalVariables/ns:GlobalVariables/GlobalVariables/OutputLocation", bindingSpec.getFileName());
        Assert.assertEquals("$File-Poller/pfx:EventSourceOuputTextClass/fileContent/textContent", bindingSpec.getTextContent());
    }
}
