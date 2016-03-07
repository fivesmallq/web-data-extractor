package im.nll.data.extractor.rule;

/**
 * extract rule model.
 *
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/2/11 下午12:18
 */
public class ExtractRule {
    private String field;
    private String extractor;

    public ExtractRule(String field, String extractor) {
        this.field = field;
        this.extractor = extractor;
    }

    public static ExtractRule newRule(String field, String extractor) {
        return new ExtractRule(field, extractor);
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getExtractor() {
        return extractor;
    }

    public void setExtractor(String extractor) {
        this.extractor = extractor;
    }
}
