package elements.tolerance;

import elements.Attribute;
import elements.Snapshot;
import elements.Trace;

import java.util.List;

public class ToleranceEquivalenceTrace extends Trace<Boolean> {
    public ToleranceEquivalenceTrace(List<String[]> fullMatrix) {
        super(fullMatrix);
    }

    @Override
    public Snapshot<Boolean> createSnapshot(Attribute[] attributes, Object[] values, float timestamp) {
        return new ToleranceEquivalenceSnapshot(attributes, values, timestamp);
    }
}
