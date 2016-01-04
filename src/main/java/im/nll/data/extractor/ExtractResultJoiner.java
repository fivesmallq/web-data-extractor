package im.nll.data.extractor;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

public class ExtractResultJoiner implements ExtractEnd {
    
    private Extractors superior;
    private List<Extractor> extractors;
	private String field;
	private Extractors inferior;
    
    ExtractResultJoiner(Extractors extractors, String field, Extractor extractor) {
    	this.field = field;
    	this.superior = extractors;
    	this.extractors = Lists.newArrayList(extractor);
	}
    
	public ExtractResultJoiner get(int i){
		extractors.get(extractors.size()-1).selected = i;
        return this;
    }

	List<String> outputs() {
		String data = superior.getInput();
		int i=0;
    	for(; i< extractors.size()-1; i++){
    		Extractor extractor = extractors.get(i);
    		data = extractor.extract(data).get(extractor.selected);
    	}
		Extractor extractor = extractors.get(i);
		return extractor.extract(data);
	}
    
    /* (non-Javadoc)
	 * @see im.nll.data.extractor.ExtractEnd#asString()
	 */
    @Override
	public String asString(){
    	String data = superior.getInput();
    	for(int i=0; i< extractors.size(); i++){
    		Extractor extractor = extractors.get(i);
    		data = extractor.extract(data).get(extractor.selected);
    	}
    	return data;
    }

	public List<String> asList() {
		return outputs();
	}
    
    /* (non-Javadoc)
	 * @see im.nll.data.extractor.ExtractEnd#asMap()
	 */
    @Override
	public Map<String, String> asMap(){
        return superior.asMap();
    }
    
    /* (non-Javadoc)
	 * @see im.nll.data.extractor.ExtractEnd#asBean(java.lang.Class)
	 */
    @Override
	public <T> T asBean(Class<T> clazz){
        return superior.asBean(clazz);
    }
    
    /* (non-Javadoc)
	 * @see im.nll.data.extractor.ExtractEnd#asBeanList(java.lang.Class)
	 */
    @Override
	public <T> List<T> asBeanList(Class<T> clazz){
    	if(this.inferior != null){
    		List<String> datas = outputs();
    		List<T> result = Lists.newArrayList();
    		for(int i=0; i<datas.size(); i++){
    			T bean = inferior.setInput(datas.get(i)).asBean(clazz);
    			result.add(bean);
    		}
    		return result;
    	}else{
            return superior.asBeanList(clazz);
    	}
    }

	public Extractors and() {
		return and(null, null);
	}
    
    public Extractors and(Extractor extractor){
    	return and(null, extractor);
    }
    
    public Extractors and(String field, Extractor extractor){
    	if(extractor != null){
        	superior.extract(extractor);
    	}
        return this.superior;
    }

	public Extractors with() {
		this.superior.setWith(field);
    	return with(null);
	}
    
    public Extractors with(Extractor extractor){
    	if(extractor != null){
        	this.extractors.add(extractor);
    	}
    	return this.superior;
    }
    
    public ExtractResultJoiner foreach(){
    	inferior = Extractors.lazy(this);
    	return this;
    }

	public Extractors begin() {
		return inferior;
	}

	public ExtractResultJoiner end() {
		return this;
	}
}