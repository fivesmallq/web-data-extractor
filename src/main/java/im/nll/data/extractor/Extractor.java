package im.nll.data.extractor;

import java.util.List;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 15/12/25 下午6:20
 */
public abstract class Extractor {
	
    int selected = 0;

	public abstract List<String> extract(String data);
}
