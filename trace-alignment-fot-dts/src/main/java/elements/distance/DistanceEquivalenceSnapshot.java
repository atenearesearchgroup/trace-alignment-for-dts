package elements.distance;

import elements.Attribute;
import elements.Snapshot;

public class DistanceEquivalenceSnapshot extends Snapshot<Double> {
    public DistanceEquivalenceSnapshot(Attribute[] attributes, Object[] values, float timestamp) {
        super(attributes, values, timestamp);
    }

    @Override
    public Double equalsAlignment(Snapshot<Double> b, double tolerance) {
        double sum = 0.0;
        for (Attribute attribute : this.getAttributes().keySet()) {
            if(attribute.getMaxValue() > 0){
                double difference = Math.abs((Double) b.getAttributes().get(attribute)
                        - (Double) this.getAttributes().get(attribute));
                if(difference > attribute.getTolerance()){
                    sum += 0;
                } else {
                    sum += (1-difference/attribute.getTolerance());
                };
            } else {
                if(!b.getAttributes().get(attribute).equals(this.getAttributes().get(attribute))){
                    sum += 0;
                } else {
                    sum += 1;
                }
            }
        }
        return sum/this.getAttributes().size();
    }
}
