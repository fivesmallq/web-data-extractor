package im.nll.data.extractor.impl;

import com.google.common.collect.Lists;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import im.nll.data.extractor.ListableExtractor;

import java.util.List;

/**
 * json extractor<p>impl by <a href=https://github.com/jayway/JsonPath>https://github.com/jayway/JsonPath</a></p>
 *
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 15/12/25 下午9:24
 */
public class JSONPathExtractor implements ListableExtractor {
    static final Configuration conf = Configuration.defaultConfiguration().addOptions(Option.ALWAYS_RETURN_LIST, Option.DEFAULT_PATH_LEAF_TO_NULL, Option.SUPPRESS_EXCEPTIONS);
    private String jsonpath;

    public JSONPathExtractor(String jsonpath) {
        this.jsonpath = jsonpath;
    }

    @Override
    public String extract(String data) {
        List<String> list = JsonPath.using(conf).parse(data).read(jsonpath);
        if (list.get(0) == null) {
            return "";
        } else {
            return list.get(0);
        }
    }

    @Override
    public List<String> extractList(String data) {
        List<Object> list = JsonPath.using(conf).parse(data).read(jsonpath);
        List<String> stringList = Lists.newLinkedList();
        for (Object one : list) {
            stringList.add(String.valueOf(one));
        }
        return stringList;
    }
}
