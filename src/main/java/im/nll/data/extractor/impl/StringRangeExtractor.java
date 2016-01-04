package im.nll.data.extractor.impl;

import im.nll.data.extractor.Extractor;
import im.nll.data.extractor.utils.StringUtils;

import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 15/12/30 下午3:38
 */
public class StringRangeExtractor extends Extractor {
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
	public List<String> extract(String data) {
		List<String> result = StringUtils.substringsBetween(data, open, close,
				tokenReservedFlag);
		return result == null || result.size() == 0 ? ImmutableList.of("") : result;
	}
}
