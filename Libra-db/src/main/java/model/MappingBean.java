package model;

import java.util.List;
import java.util.Map;

/**
 * Created by fishiung on 2017-11-24
 */
public class MappingBean {

    private Map<String,Field> properties;

    public Map<String, Field> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Field> properties) {
        this.properties = properties;
    }
}
