package im.nll.data.extractor.impl;

import im.nll.data.extractor.ListableExtractor;
import im.nll.data.extractor.annotation.Name;
import im.nll.data.extractor.utils.StringUtils;

import java.util.List;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 15/12/30 下午3:38
 */
@Name("stringRange")
public class StringRangeExtractor implements ListableExtractor {
    private String open;
    private String close;
    private boolean tokenReservedFlag = false;

    public StringRangeExtractor(String query) {
        String[] stringList = query.split("(?<!\\\\),");
        this.open = stringList[0].replace("\\,", ",");
        if (stringList.length > 1 && StringUtils.isNotNullOrEmpty(stringList[1])) {
            this.close = stringList[1].replace("\\,", ",");
        }
        if (stringList.length > 2 && StringUtils.isNotNullOrEmpty(stringList[2])) {
            this.tokenReservedFlag = Boolean.valueOf(stringList[2].replace("\\,", ","));
        }
    }

    @Override
    public String extract(String data) {
        List<String> stringList = StringUtils.substringsBetween(data, open, close, tokenReservedFlag);
        if (stringList != null && !stringList.isEmpty()) {
            return stringList.get(0);
        }
        return "";
    }

    @Override
    public List<String> extractList(String data) {
        return StringUtils.substringsBetween(data, open, close, tokenReservedFlag);
    }
}
