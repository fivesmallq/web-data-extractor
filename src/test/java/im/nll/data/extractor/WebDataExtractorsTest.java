package im.nll.data.extractor;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import im.nll.data.extractor.entity.Basic;
import im.nll.data.extractor.entity.Website;
import im.nll.data.extractor.impl.SelectorExtractor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 15/12/25 下午9:34
 */
public class WebDataExtractorsTest {
    private String html;
    private String listHtml;


    @Before
    public void before() {
        try {
            html = Resources.toString(Resources.getResource("basic.html"), Charsets.UTF_8);
            listHtml = Resources.toString(Resources.getResource("list.html"), Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGet() throws Exception {
        String number = WebDataExtractor.of(html).filter(new SelectorExtractor("tr:contains(注册号) td", "0")).asString();
        String name = WebDataExtractor.of(html).selector("tr:contains(名称) td", "1").asString();
        String type = WebDataExtractor.of(html).selector("tr:contains(类型) td", "0").asString();
        String legalPersion = WebDataExtractor.of(html).selector("tr:contains(法定代表人) td", "1").asString();
        String address = WebDataExtractor.of(html).selector("tr:contains(住所) td", "0").asString();
        String money = WebDataExtractor.of(html).selector("tr:contains(注册资本) td", "0").regex("\\d+.\\d+").asString();

        Assert.assertEquals(number, "110000003277298");
        Assert.assertEquals(name, "高德软件有限公司");
        Assert.assertEquals(type, "有限责任公司(自然人投资或控股)");
        Assert.assertEquals(legalPersion, "陆兆禧");
        Assert.assertEquals(address, "北京市昌平区科技园区昌盛路18号B1座1-5层");
        Assert.assertEquals(money, "24242.4242");
    }

    @Test
    public void testToMap() throws Exception {
        Map<String, String> dataMap = WebDataExtractor.of(html).asMap(
                Extractors.nameOf("number").selector("tr:contains(注册号) td", "0"),
                Extractors.nameOf("name").selector("tr:contains(名称) td", "1"),
                Extractors.nameOf("type").selector("tr:contains(类型) td", "0"),
                Extractors.nameOf("legalPersion").selector("tr:contains(法定代表人) td", "1"),
                Extractors.nameOf("address").selector("tr:contains(住所) td", "0"),
                Extractors.nameOf("money").selector("tr:contains(注册资本) td", "0").regex("\\d+.\\d+"));

        Assert.assertEquals(dataMap.get("number"), "110000003277298");
        Assert.assertEquals(dataMap.get("name"), "高德软件有限公司");
        Assert.assertEquals(dataMap.get("type"), "有限责任公司(自然人投资或控股)");
        Assert.assertEquals(dataMap.get("legalPersion"), "陆兆禧");
        Assert.assertEquals(dataMap.get("address"), "北京市昌平区科技园区昌盛路18号B1座1-5层");
        Assert.assertEquals(dataMap.get("money"), "24242.4242");
    }

    @Test
    public void testToBean() throws Exception {
        Basic basic = WebDataExtractor.of(html).asBean(Basic.class,
                Extractors.nameOf("number").selector("tr:contains(注册号) td", "0"),
                Extractors.nameOf("name").selector("tr:contains(名称) td", "1"),
                Extractors.nameOf("type").selector("tr:contains(类型) td", "0"),
                Extractors.nameOf("legalPersion").selector("tr:contains(法定代表人) td", "1"),
                Extractors.nameOf("address").selector("tr:contains(住所) td", "0"),
                Extractors.nameOf("money").selector("tr:contains(注册资本) td", "0").regex("\\d+.\\d+"));

        Assert.assertEquals(basic.getNumber(), "110000003277298");
        Assert.assertEquals(basic.getName(), "高德软件有限公司");
        Assert.assertEquals(basic.getType(), "有限责任公司(自然人投资或控股)");
        Assert.assertEquals(basic.getLegalPersion(), "陆兆禧");
        Assert.assertEquals(basic.getAddress(), "北京市昌平区科技园区昌盛路18号B1座1-5层");
        Assert.assertEquals(basic.getMoney(), "24242.4242");
    }

    @Test
    public void testToBeanList() throws Exception {
        List<Website> websites = WebDataExtractor.of(listHtml).split(new SelectorExtractor("tr:has(td)")).asBeanList(Website.class,
                Extractors.nameOf("type").selector("td", "0"),
                Extractors.nameOf("name").selector("td", "1"),
                Extractors.nameOf("url").selector("td", "2"));
        Assert.assertNotNull(websites);
        Website first = websites.get(0);
        Assert.assertEquals(websites.size(), 3);
        Assert.assertEquals(first.getType(), "网站");
        Assert.assertEquals(first.getName(), "高德导航");
        Assert.assertEquals(first.getUrl(), "www.anav.com");
    }
}