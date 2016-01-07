# web-data-extractor 
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/im.nll.data/extractor/badge.svg)](https://maven-badges.herokuapp.com/maven-central/im.nll.data/extractor/)
[![Build Status](https://travis-ci.org/fivesmallq/web-data-extractor.svg)](https://travis-ci.org/fivesmallq/web-data-extractor)
[![codecov.io](http://codecov.io/github/fivesmallq/web-data-extractor/coverage.svg?branch=master)](http://codecov.io/github/fivesmallq/web-data-extractor?branch=master)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

Extract data from common web format, like HTML,XML,JSON.

##Examples

###extract single data

````java
    String followers = Extractors.on(baseHtml).extract(new SelectorExtractor("div.followers")).with(new RegexExtractor("\\d+")).asString();
````

or use static method

````java
    String followers = Extractors.on(baseHtml).extract(selector("div.followers")).with(regex("\\d+")).asString();
````

###extract data to map

````java
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
  ````
  
###extract data to map list

````java

    @Test
    public void testToMapList() throws Exception {
        //split param must implements ListableExtractor
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
    }
  ````
  
  
###extract data to bean

````java
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
````

###extract data to bean list

````java
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
````

see [Example](https://github.com/fivesmallq/web-data-extractor/blob/master/src/test/java/im/nll/data/extractor/ExtractorsTest.java)
