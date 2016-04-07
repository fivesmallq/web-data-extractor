package im.nll.data.extractor.rule;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import im.nll.data.extractor.Extractors;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/4/7 下午6:42
 */
public class ExtractRulesTest {
    private String listHtml2;

    @Before
    public void before() {
        try {
            listHtml2 = Resources.toString(Resources.getResource("list2.html"), Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("url", "selector:a.attr(href)");
        fields.put("title", "selector:a");
        fields.put("date", "selector:span.fr");
        ExtractRules rules = ExtractRules
                .newRules("selector:dd.x_ct1.html")
                .fields(fields);

        List<Map<String, String>> datas = Extractors
                .on(listHtml2)
                .split(rules.getSplit().getExtractor())
                .extract(rules.getExtractRules())
                .asMapList();
        Assert.assertEquals(10, datas.size());
        Map<String, String> data = datas.get(2);
        Assert.assertEquals("http://infect.dxy.cn/article/486885", data.get("url"));
        Assert.assertEquals("5 个小测验：教你轻松应对艰难梭菌感染", data.get("title"));
        Assert.assertEquals("2016.03.10", data.get("date"));

    }
}
