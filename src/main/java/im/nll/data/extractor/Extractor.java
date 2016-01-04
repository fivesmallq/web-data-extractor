package im.nll.data.extractor;

import java.util.List;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 15/12/25 下午6:20
 */
public interface Extractor {
    List<String> extract(String data);
}
