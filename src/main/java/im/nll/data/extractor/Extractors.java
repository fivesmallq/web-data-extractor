package im.nll.data.extractor;

import im.nll.data.extractor.impl.SelectorExtractor;

import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.google.common.collect.Maps;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 15/12/28 下午4:43
 */
public class Extractors extends ExtractResult{
    
    private String html;
    private Stack<ExtractResult> extractResults = new Stack<ExtractResult>();
    
    public Extractors(String html) {
        this.html = html;
    }

    public static Extractors on(String html) {
        return new Extractors(html);
    }
    
    public ExtractResult extract(Extractor extractor){
        return extract(null, extractor);
    }
    
    public ExtractResult extract(String field, Extractor extractor){
        return extract(html, null, extractor);
    }

	public ExtractResult extract(String input, String field, Extractor extractor) {
        ExtractResult result = new ExtractResult(this, field, extractor.extract(input));
        if(!input.equalsIgnoreCase(html)){
        	extractResults.pop();
        }
        extractResults.push(result);
        return result;
	}
    
	@Override
    public Map<String, String> asMap(){
        // extractResults value - key: result.asString
    	Map<String, String> result = Maps.newLinkedHashMap();
    	for(ExtractResult extract: extractResults){
    		result.put(extract.getField(), extract.asString());
    	}
    	return result;
    }
    
	@Override
    public <T> T asBean(Class<T> clazz){
        return null; // TODO
    }
    
	@Override
    public <T> List<T> asBeanList(Class<T> clazz){
        return null;
    }
	
	public String asString(){
        return extractResults.elementAt(0).asString();
    }
    
    public ExtractResult selector(String query){
    	return selector(null, query);
    }

	public ExtractResult selector(String field, String query) {
		return extract(field, new SelectorExtractor(query));
	}
}
