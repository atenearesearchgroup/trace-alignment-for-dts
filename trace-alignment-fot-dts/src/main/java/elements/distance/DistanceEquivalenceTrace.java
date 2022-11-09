package elements.distance;

import elements.Attribute;
import elements.Snapshot;
import elements.Trace;

import java.util.List;

public class DistanceEquivalenceTrace extends Trace<Double> {
    public DistanceEquivalenceTrace(List<String[]> fullMatrix) {
        super(fullMatrix);
    }

    @Override
    public Snapshot<Double> createSnapshot(Attribute[] attributes, Object[] values, float timestamp) {
        return new DistanceEquivalenceSnapshot(attributes, values, timestamp);
    }
}
