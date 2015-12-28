package com.nihiler.spider;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.nihiler.spider.impl.SelectorExtractor;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 15/12/25 下午9:34
 */
public class WebDataExtractorsTest {

    @Test
    public void testGet() throws Exception {
        String html = Resources.toString(Resources.getResource("basic.html"), Charsets.UTF_8);

        String number = WebDataExtractor.of(html).extract(new SelectorExtractor("tr:contains(注册号) td", "0")).get();
        String name = WebDataExtractor.of(html).selector("tr:contains(名称) td", "1").get();
        String type = WebDataExtractor.of(html).selector("tr:contains(类型) td", "0").get();
        String legalPersion = WebDataExtractor.of(html).selector("tr:contains(法定代表人) td", "1").get();
        String address = WebDataExtractor.of(html).selector("tr:contains(住所) td", "0").get();
        String money = WebDataExtractor.of(html).selector("tr:contains(注册资本) td", "0").regex("\\d+.\\d+").get();

        Assert.assertEquals(number, "110000003277298");
        Assert.assertEquals(name, "高德软件有限公司");
        Assert.assertEquals(type, "有限责任公司(自然人投资或控股)");
        Assert.assertEquals(legalPersion, "陆兆禧");
        Assert.assertEquals(address, "北京市昌平区科技园区昌盛路18号B1座1-5层");
        Assert.assertEquals(money, "24242.4242");
    }

    @Test
    public void testToMap() throws Exception {
        String html = Resources.toString(Resources.getResource("basic.html"), Charsets.UTF_8);

        Map<String, String> dataMap = WebDataExtractor.of(html).toMap(
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
        String html = Resources.toString(Resources.getResource("basic.html"), Charsets.UTF_8);
        Basic basic = WebDataExtractor.of(html).toBean(Basic.class,
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
}