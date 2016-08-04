package im.nll.data.extractor.entity;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/8/3 下午10:35
 */
public class Activity {
    private String name;
    private String type;
    private String description;
    private String resourceType;
    private Config config;
    private BindingSpec inputBindings;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public BindingSpec getInputBindings() {
        return inputBindings;
    }

    public void setInputBindings(BindingSpec inputBindings) {
        this.inputBindings = inputBindings;
    }
}
