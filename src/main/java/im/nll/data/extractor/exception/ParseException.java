package im.nll.data.extractor.exception;

/**
 * parse exception
 *
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 2016-1-19下午11:10:07
 */
public class ParseException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ParseException() {
    }

    public ParseException(String message) {
        super(message);
    }

    public ParseException(Throwable cause) {
        super(cause);
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }

}