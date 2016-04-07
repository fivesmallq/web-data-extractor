package im.nll.data.extractor.rule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * extract rule model.
 *
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/2/11 下午12:18
 */
public class ExtractRules implements Serializable {
    private ExtractRule split;
    private List<ExtractRule> extractRules;

    public ExtractRules split(String split) {
        this.split = new ExtractRule("split", split);
        return this;
    }

    public ExtractRules fields(Map<String, String> extractorMap) {
        List<ExtractRule> extractRules = new ArrayList<>();
        for (Map.Entry<String, String> one : extractorMap.entrySet()) {
            extractRules.add(new ExtractRule(one.getKey(), one.getValue()));
        }
        this.extractRules = extractRules;
        return this;
    }

    public static ExtractRules newRules() {
        return new ExtractRules();
    }

    public static ExtractRules newRules(String split) {
        return new ExtractRules().split(split);
    }

    public ExtractRule getSplit() {
        return split;
    }

    public void setSplit(ExtractRule split) {
        this.split = split;
    }

    public List<ExtractRule> getExtractRules() {
        return extractRules;
    }

    public void setExtractRules(List<ExtractRule> extractRules) {
        this.extractRules = extractRules;
    }
}
