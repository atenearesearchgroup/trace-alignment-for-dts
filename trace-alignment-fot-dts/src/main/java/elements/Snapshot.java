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
    private final Map<Attribute, Object> attributes;

    public Snapshot(Attribute[] attributes, Object[] values, long timestamp) {
        this.timestamp = timestamp;
        this.attributes = new HashMap<>();
        for (int i = 0; i < values.length; i++) {
            this.attributes.put(attributes[i], values[i]);
        }
    }

    public boolean equalsAlignment(Snapshot b, double tolerance) {
        boolean equals = true;
        for (Attribute attribute : attributes.keySet()) {
            if(attribute.getMaxValue() > 0){
                double b_value = (Double) b.getAttributes().get(attribute);
                double this_value = (Double) this.attributes.get(attribute);
                if (Math.abs(b_value / attribute.getMaxValue()
                        - this_value / attribute.getMaxValue()) > tolerance) {
                    equals = false;
                    break;
                }
            } else {
                if(!b.getAttributes().get(attribute).equals(this.attributes.get(attribute))){
                    equals = false;
                    break;
                }
            }
        }
        return equals;
    }

    public Map<Attribute, Object> getAttributes() {
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
