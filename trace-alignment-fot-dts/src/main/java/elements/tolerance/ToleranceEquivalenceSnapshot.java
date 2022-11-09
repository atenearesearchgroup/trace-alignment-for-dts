package elements.tolerance;

import elements.Attribute;
import elements.Snapshot;

public class ToleranceEquivalenceSnapshot extends Snapshot<Boolean> {

    public ToleranceEquivalenceSnapshot(Attribute[] attributes, Object[] values, float timestamp) {
        super(attributes, values, timestamp);
    }

    public Boolean equalsAlignment(Snapshot<Boolean> b, double tolerance) {
        boolean equals = true;
        for (Attribute attribute : this.getAttributes().keySet()) {
            if(attribute.getMaxValue() > 0){
                double b_value = (Double) b.getAttributes().get(attribute);
                double this_value = (Double) this.getAttributes().get(attribute);
                if (Math.abs(b_value / attribute.getMaxValue()
                        - this_value / attribute.getMaxValue()) > tolerance) {
                    equals = false;
                    break;
                }
            } else {
                if(!b.getAttributes().get(attribute).equals(this.getAttributes().get(attribute))){
                    equals = false;
                    break;
                }
            }
        }
        return equals;
    }
}
