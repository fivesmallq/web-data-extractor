package im.nll.data.extractor.impl;

import im.nll.data.extractor.Extractor;
import im.nll.data.extractor.utils.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableList;

/**
 * regex extractor
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 15/12/28 下午4:24
 */
public class RegexExtractor extends Extractor {
    private String regex;
    private int group;
    //TODO impl filter
    private String filter;

    public RegexExtractor(String ... params) {
        this.regex = params[0];
        if (params.length > 1 && StringUtils.isNotNullOrEmpty(params[1])) {
            this.group = StringUtils.tryParseInt(params[1], 0);
        }
    }

    @Override
    public List<String> extract(String data) {
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL
                | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(data);
        return ImmutableList.of(matcher.find()?matcher.group(group).trim():"");
    }
}
