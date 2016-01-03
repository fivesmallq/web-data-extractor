package im.nll.data.extractor;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

public class ExtractResult {
    
    private Extractors extractors;
    private String field;
    private List<String> results = Lists.newArrayList();
    
    protected ExtractResult(){
    	
    }
    
    protected ExtractResult(Extractors extractors, String field, List<String> extract) {
    	this.extractors = extractors;
    	this.field = field;
    	this.results = extract;
	}

	public String getField() {
		return field;
	}
    
    public String asString(){
        return results.get(0).toString();
    }
    
    public String first(){
        return results.get(0);
    }
       
    public String get(int i){
        return results.get(i);
    }
    
    public Map<String, String> asMap(){
        return extractors.asMap();
    }
    
    public <T> T asBean(Class<T> clazz){
        return extractors.asBean(clazz);
    }
    
    public <T> List<T> asBeanList(Class<T> clazz){
        return extractors.asBeanList(clazz);
    }
    
    public Extractors and(Extractor extractor){
    	return and(null, extractor);
    }
    
    public Extractors and(String field, Extractor extractor){
    	extractors.extract(extractor);
        return this.extractors;
    }
    
    public Extractors with(Extractor extractor){
    	return with(null, extractor);
    }
    
    public Extractors with(String field, Extractor extractor){
    	extractors.extract(first(), field, extractor);
        return this.extractors;
    }
    
    public Extractors foreach(Extractor extractor){
    	for(String result: results){
    		extractors.extract(result, null, extractor);
    	}
        return this.extractors;
    }
    
    public Extractors foreach(Extractors extractors){
    	return null;// TODO
    }
}