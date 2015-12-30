package im.nll.data.extractor.impl;

import im.nll.data.extractor.ListableExtractor;
import im.nll.data.extractor.utils.StringUtils;

import java.util.List;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 15/12/30 下午3:38
 */
public class StringRangeExtractor implements ListableExtractor {
    private String open;
    private String close;
    private boolean tokenReservedFlag = false;

    public StringRangeExtractor(String... params) {
        this.open = params[0];
        if (params.length > 1 && StringUtils.isNotNullOrEmpty(params[1])) {
            this.close = params[1];
        }
        if (params.length > 2 && StringUtils.isNotNullOrEmpty(params[2])) {
            this.tokenReservedFlag = Boolean.valueOf(params[2]);
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
