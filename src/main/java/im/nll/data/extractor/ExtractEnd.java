package im.nll.data.extractor;

import java.util.List;
import java.util.Map;

public interface ExtractEnd {

	public abstract String asString();

	public abstract Map<String, String> asMap();

	public abstract <T> T asBean(Class<T> clazz);

	public abstract <T> List<T> asBeanList(Class<T> clazz);

}