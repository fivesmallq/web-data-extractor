package im.nll.data.extractor;

import java.util.List;

/**
 * 列表抽取接口
 *
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 15/12/30 上午10:37
 */
public interface ListableExtractor extends Extractor {
    List<String> extractList(String data);
}
