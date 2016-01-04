package im.nll.data.extractor.impl;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import im.nll.data.extractor.Extractor;

import java.util.List;

/**
 * json extractor<p>impl by <a href=https://github.com/jayway/JsonPath>https://github.com/jayway/JsonPath</a></p>
 *
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 15/12/25 下午9:24
 */
public class JSONPathExtractor extends Extractor {
    static final Configuration conf = Configuration.defaultConfiguration().addOptions(Option.ALWAYS_RETURN_LIST, Option.DEFAULT_PATH_LEAF_TO_NULL, Option.SUPPRESS_EXCEPTIONS);
    private String jsonpath;

    public JSONPathExtractor(String query) {
        this.jsonpath = query;
    }

    @Override
    public List<String> extract(String data) {
        return JsonPath.using(conf).parse(data).read(jsonpath);
    }
}
