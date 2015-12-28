package im.nll.spider.extractor.impl;

import im.nll.spider.extractor.Extractor;
import im.nll.spider.extractor.utils.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * regex extractor
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 15/12/28 下午4:24
 */
public class RegexExtractor implements Extractor {
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
    public String extract(String data) {
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL
                | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(data);
        if (matcher.find())
            return matcher.group(group).trim();
        else {
            return "";
        }
    }
}
