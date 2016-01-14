package im.nll.data.extractor.exception;

/**
 * extract exception
 *
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 2016-1-14下午11:10:07
 */
public class ExtractException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ExtractException() {
    }

    public ExtractException(String message) {
        super(message);
    }

    public ExtractException(Throwable cause) {
        super(cause);
    }

    public ExtractException(String message, Throwable cause) {
        super(message, cause);
    }

}