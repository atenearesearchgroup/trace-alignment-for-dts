package elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Paula Munoz
 */
public abstract class Snapshot<T> {

    private final float timestamp;
    private final Map<Attribute, Object> attributes;

    public Snapshot(Attribute[] attributes, Object[] values, float timestamp) {
        this.timestamp = timestamp;
        this.attributes = new HashMap<>();
        for (int i = 0; i < values.length; i++) {
            this.attributes.put(attributes[i], values[i]);
        }
    }

    public abstract T equalsAlignment(Snapshot<T> b, double tolerance);

    public Map<Attribute, Object> getAttributes() {
        return attributes;
    }

    public float getTimestamp() {
        return timestamp;
    }

    public List<String> getValues(){
        List<String> result = new ArrayList<>();
        for(Attribute att : attributes.keySet()){
            result.add(attributes.get(att).toString());
        }
        return result;
    }
}
