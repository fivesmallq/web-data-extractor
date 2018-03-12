package im.nll.data.extractor.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import im.nll.data.extractor.ListableExtractor;
import im.nll.data.extractor.annotation.Name;
import im.nll.data.extractor.utils.StringUtils;

/**
 * regex extractor
 * 
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @rewriter 邓华锋 http://dhf.ink
 * @version 0.9.7
 * @date 18/03/12 下午11:01
 * 
 */
@Name("regex")
public class RegexExtractor implements ListableExtractor {
	private String regex;
	private int group = 0;

	public RegexExtractor(String regex) {
		String[] stringList = regex.split("(?<!\\\\),");
		this.regex = stringList[0].replace("\\,", ",");
		if (stringList.length > 1 && StringUtils.isNotNullOrEmpty(stringList[1])) {
			this.group = Integer.valueOf(stringList[1].replace("\\,", ","));
		}
	}

	@Override
	public String extract(String data) {
		Pattern pattern = Pattern.compile(regex, Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(data);
		if (matcher.find())
			return matcher.group(group);
		else {
			return "";
		}
	}

	@Override
	public List<String> extractList(String data) {
		List<String> stringList = new LinkedList<>();
		Pattern pattern = Pattern.compile(regex, Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		Matcher m = pattern.matcher(data);
		while (m.find()) {
			if (group > 0) {
				if (m.groupCount() >= 1) {
					stringList.add(m.group(group));
				}
			} else {
				stringList.add(m.group());
			}
		}
		return stringList;
	}

}
