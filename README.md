# web-data-extractor 
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/im.nll.data/extractor/badge.svg)](https://maven-badges.herokuapp.com/maven-central/im.nll.data/extractor/)
[![Build Status](https://travis-ci.org/fivesmallq/web-data-extractor.svg)](https://travis-ci.org/fivesmallq/web-data-extractor)
[![codecov.io](http://codecov.io/github/fivesmallq/web-data-extractor/coverage.svg?branch=master)](http://codecov.io/github/fivesmallq/web-data-extractor?branch=master)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

Extract data from common web format. like HTML,XML,JSON.

##Examples

###extract single data

````java
String number = WebDataExtractor.of(html).filter(new SelectorExtractor("tr:contains(名称) td", "0")).asString();
````

or use ``selector`` method

````java
String name = WebDataExtractor.of(html).selector("tr:contains(名称) td", "0").asString();
````

###extract data to map

````java
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
  ````
  
###extract data to bean

````java
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
````

###extract data to bean list

````java
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
````

see [WebDataExtractorsTest](https://github.com/fivesmallq/web-data-extractor/blob/master/src/test/java/im/nll/data/extractor/WebDataExtractorsTest.java)
