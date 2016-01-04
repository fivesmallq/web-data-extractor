package im.nll.data.extractor;

import im.nll.data.extractor.impl.RegexExtractor;
import im.nll.data.extractor.impl.SelectorExtractor;

import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.google.common.collect.Maps;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 15/12/28 下午4:43
 */
public class Extractors implements ExtractEnd {

	private String html;
	private Stack<ExtractResultJoiner> extractResults = new Stack<ExtractResultJoiner>();
	private ExtractResultJoiner priority;

	public Extractors(String html) {
		this.html = html;
	}

	ExtractResultJoiner pop() {
		return extractResults.pop();
	}

	void setNextWith(ExtractResultJoiner prev) {
		this.priority = prev;
	}

	public static Extractors on(String html) {
		return new Extractors(html);
	}

	public ExtractResultJoiner extract(Extractor extractor) {
		return extract(null, extractor);
	}

	public ExtractResultJoiner extract(String field, Extractor extractor) {
		return extract(html, field, extractor);
	}

	public ExtractResultJoiner extract(String input, String field,
			Extractor extractor) {
		try {
			ExtractResultJoiner result;
			// 此方法在 with() 后调用时 priority 会被赋值
			if (priority != null) {
				result = new ExtractResultJoiner(this, priority.getFieldName(),
						extractor.extract(priority.asString()));
			} else {
				result = new ExtractResultJoiner(this, field,
						extractor.extract(input));
			}
			extractResults.push(result);
			return result;
		} finally {
			priority = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see im.nll.data.extractor.ExtractEnd#asMap()
	 */
	@Override
	public Map<String, String> asMap() {
		// extractResults value - key: result.asString
		Map<String, String> result = Maps.newLinkedHashMap();
		for (ExtractResultJoiner extract : extractResults) {
			result.put(extract.getFieldName(), extract.asString());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see im.nll.data.extractor.ExtractEnd#asBean()
	 */
	@Override
	public <T> T asBean(Class<T> clazz) {
		return null; // TODO
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see im.nll.data.extractor.ExtractEnd#asBeanList()
	 */
	@Override
	public <T> List<T> asBeanList(Class<T> clazz) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see im.nll.data.extractor.ExtractEnd#asString()
	 */
	@Override
	public String asString() {
		return extractResults.elementAt(0).asString();
	}

	public ExtractResultJoiner selector(String query) {
		return selector(null, query);
	}

	public ExtractResultJoiner selector(String field, String query) {
		return extract(field, new SelectorExtractor(query));
	}

	public ExtractResultJoiner regex(String query) {
		return regex(null, query);
	}

	private ExtractResultJoiner regex(String field, String query) {
		return extract(field, new RegexExtractor(query));
	}
}
