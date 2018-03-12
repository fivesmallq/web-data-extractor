package im.nll.data.extractor.impl;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

/**
 * regex extractor test
 * @author 邓华锋 http://dhf.ink
 * @version 0.9.7
 * @date 18/03/12 下午11:33
 */
public class RegexExtractorTest {
	private String html;
	RegexExtractor regexExtractor;

	@Before
	public void setUp() throws Exception {
		try {
			html = Resources.toString(Resources.getResource("list.html"), Charsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testExtractEscaped() throws Exception {
		String demoHtml = "<td>www.baidu.com ,, www.google.com</a></td>";
		regexExtractor = new RegexExtractor("<td>.*\\,\\,,0");
		String result = regexExtractor.extract(demoHtml);
		Assert.assertEquals("<td>www.baidu.com ,,", result);

		regexExtractor = new RegexExtractor("<td>(.*)\\,\\,,1");
		result = regexExtractor.extract(demoHtml);
		Assert.assertEquals("www.baidu.com ", result);
	}

	@Test
	public void testExtract() throws Exception {
		String demoHtml = "<td>www.baidu.com</td>";
		regexExtractor = new RegexExtractor("<td>(.*)</td>");
		String result = regexExtractor.extract(demoHtml);
		Assert.assertEquals(result, "<td>www.baidu.com</td>");

		regexExtractor = new RegexExtractor("<td>(.*)</td>,1");
		result = regexExtractor.extract(demoHtml);
		Assert.assertEquals(result, "www.baidu.com");

		regexExtractor = new RegexExtractor("xxx");
		result = regexExtractor.extract(demoHtml);
		Assert.assertEquals(result, "");

		regexExtractor = new RegexExtractor("<td>(.*)</td>,0");
		result = regexExtractor.extract(demoHtml);
		Assert.assertEquals(result, "<td>www.baidu.com</td>");
	}

	@Test
	public void testExtractList() throws Exception {
		regexExtractor = new RegexExtractor("<tr class=\"item\">([\\s\\S]*?.*?)</tr>");
		List<String> stringList = regexExtractor.extractList(html);
		Assert.assertNotNull(stringList);
		Assert.assertEquals(stringList.size(), 3);
		Assert.assertEquals("<tr class=\"item\">\r\n" + "        <td class=\"type\">dynamic</td>\r\n"
				+ "        <td class=\"name\">Python</td>\r\n"
				+ "        <td class=\"url\">https://www.python.org</td>\r\n" + "    </tr>", stringList.get(2));
		regexExtractor = new RegexExtractor("<tr class=\"item\">([\\s\\S]*?.*?)</tr>,1");
		stringList = regexExtractor.extractList(html);
		Assert.assertNotNull(stringList);
		Assert.assertEquals(stringList.size(), 3);
		Assert.assertEquals(
				"\r\n" + "        <td class=\"type\">dynamic</td>\r\n" + "        <td class=\"name\">Python</td>\r\n"
						+ "        <td class=\"url\">https://www.python.org</td>\r\n" + "    ",
				stringList.get(2));

	}
}
