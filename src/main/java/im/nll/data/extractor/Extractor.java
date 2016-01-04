package im.nll.data.extractor;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 15/12/25 下午6:20
 */
@FunctionalInterface
public interface Extractor {
    String extract(String data);
}
