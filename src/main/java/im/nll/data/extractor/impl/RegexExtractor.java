package im.nll.data.extractor.impl;

import im.nll.data.extractor.Extractor;

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
    private int group = 0;

    public RegexExtractor(String regex) {
        this.regex = regex;
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
