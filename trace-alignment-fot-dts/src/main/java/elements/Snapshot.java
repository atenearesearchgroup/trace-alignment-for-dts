package elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Paula Munoz
 */
public class Snapshot {

    private final long timestamp;
    private final Map<Attribute, Double> attributes;

    public Snapshot(Attribute[] attributes, double[] values, long timestamp) {
        this.timestamp = timestamp;
        this.attributes = new HashMap<>();
        for (int i = 0; i < values.length; i++) {
            this.attributes.put(attributes[i], values[i]);
        }
    }

    public boolean equalsAlignment(Snapshot b, double tolerance) {
        boolean equals = true;
        for (Attribute attribute : attributes.keySet()) {
            if (Math.abs(b.getAttributes().get(attribute) / attribute.getMaxValue()
                    - this.attributes.get(attribute) / attribute.getMaxValue()) > tolerance) {
                equals = false;
                break;
            }
        }
        return equals;
    }

    public Map<Attribute, Double> getAttributes() {
        return attributes;
    }

    public long getTimestamp() {
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
