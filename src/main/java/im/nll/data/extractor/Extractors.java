package im.nll.data.extractor;

import im.nll.data.extractor.impl.RegexExtractor;
import im.nll.data.extractor.impl.SelectorExtractor;
import im.nll.data.extractor.utils.Logs;
import im.nll.data.extractor.utils.Reflect;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 15/12/28 下午4:43
 */
public class Extractors implements ExtractEnd {
	private static final Logger LOGGER = Logs.get();

	private String html;
	private ExtractResultJoiner last;
	private Map<String, ExtractResultJoiner> joiners = Maps.newLinkedHashMap();
	private String withField;

	private Extractors(String html) {
		this.html = html;
	}

	public Extractors(ExtractResultJoiner last) {
		this.last = last;
	}

	String getInput() {
		return html;
	}

	Extractors setInput(String input) {
		this.html = input;
		return this;
	}

	void setWith(String field) {
		this.withField = field;
	}

	static Extractors lazy(ExtractResultJoiner last) {
		return new Extractors(last);
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
			field = withField == null ? field : withField; // 如果调用过 with() 方法，withField 将不为 null
			
			ExtractResultJoiner joiner = joiners.get(field);
			
			if (joiner == null) {
				joiner = new ExtractResultJoiner(this, field, extractor);
				joiners.put(field, joiner);
			} else {
				joiner.with(extractor);
			}
			return joiner;
		} finally {
			withField = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see im.nll.data.extractor.ExtractEnd#asMap()
	 */
	@Override
	public Map<String, String> asMap() {
		Map<String, String> result = Maps.newLinkedHashMap();
		for (Entry<String, ExtractResultJoiner> entry : joiners.entrySet()) {
			result.put(entry.getKey(), entry.getValue().asString());
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
		T entity = Reflect.on(clazz).create().get();
		for (Entry<String, ExtractResultJoiner> entry : joiners.entrySet()) {
			String name = entry.getKey();
			String data = entry.getValue().asString();
			try {
				Reflect.on(entity).set(name, data);
			} catch (Exception e) {
				LOGGER.error("convert to bean error! can't set '{}' with '{}'",
						name, data, e);
			}
		}
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see im.nll.data.extractor.ExtractEnd#asBeanList()
	 */
	@Override
	public <T> List<T> asBeanList(Class<T> clazz) {
		List<T> result = Lists.newArrayList();
		for(String input: last.outputs()){
			setInput(input);
			result.add(this.asBean(clazz));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see im.nll.data.extractor.ExtractEnd#asString()
	 */
	@Override
	public String asString() {
		return joiners.values().iterator().next().asString();
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
