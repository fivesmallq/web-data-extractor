package im.nll.data.extractor;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

public class ExtractResultJoiner implements ExtractEnd {
    
    private Extractors extractors;
    private List<String> results = Lists.newArrayList();
    private String field;
	private int selected;
	
	private ExtractResultJoiner prev;
    
    ExtractResultJoiner(Extractors extractors, String field, List<String> extract) {
    	this.extractors = extractors;
    	this.field = field;
    	this.results = extract;
    	this.selected = 0;
	}

	String getFieldName() {
		return field;
	}
    
	public ExtractResultJoiner get(int i){
		this.selected = i;
        return this;
    }
    
    /* (non-Javadoc)
	 * @see im.nll.data.extractor.ExtractEnd#asString()
	 */
    @Override
	public String asString(){
        return results.get(selected).toString();
    }
    
    /* (non-Javadoc)
	 * @see im.nll.data.extractor.ExtractEnd#asMap()
	 */
    @Override
	public Map<String, String> asMap(){
        return extractors.asMap();
    }
    
    /* (non-Javadoc)
	 * @see im.nll.data.extractor.ExtractEnd#asBean(java.lang.Class)
	 */
    @Override
	public <T> T asBean(Class<T> clazz){
        return extractors.asBean(clazz);
    }
    
    /* (non-Javadoc)
	 * @see im.nll.data.extractor.ExtractEnd#asBeanList(java.lang.Class)
	 */
    @Override
	public <T> List<T> asBeanList(Class<T> clazz){
        return extractors.asBeanList(clazz);
    }

	public Extractors and() {
		return and(null, null);
	}
    
    public Extractors and(Extractor extractor){
    	return and(null, extractor);
    }
    
    public Extractors and(String field, Extractor extractor){
    	if(extractor != null){
        	extractors.extract(extractor);
    	}
        return this.extractors;
    }

	public Extractors with() {
    	return with(null, null);
	}
    
    public Extractors with(Extractor extractor){
    	return with(null, extractor);
    }
    
    public Extractors with(String field, Extractor extractor){
    	prev = extractors.pop();
    	if(extractor != null){
        	extractors.extract(asString(), field, extractor);
    	}else{
    		extractors.setNextWith(prev);
    	}
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