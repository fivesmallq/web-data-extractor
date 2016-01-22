package im.nll.data.extractor.impl;

import com.google.common.base.Splitter;
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

    public StringRangeExtractor(String query) {
        List<String> stringList = Splitter.fixedLength(1).limit(3).trimResults().on(",")
                .splitToList(query);
        this.open = stringList.get(0);
        if (stringList.size() > 1 && StringUtils.isNotNullOrEmpty(stringList.get(1))) {
            this.close = stringList.get(1);
        }
        if (stringList.size() > 2 && StringUtils.isNotNullOrEmpty(stringList.get(2))) {
            this.tokenReservedFlag = Boolean.valueOf(stringList.get(2));
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
