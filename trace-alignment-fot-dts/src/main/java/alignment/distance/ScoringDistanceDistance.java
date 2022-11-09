package alignment.distance;

import alignment.IncompatibleScoringSchemeException;
import alignment.ScoringDistance;
import elements.Snapshot;

public class ScoringDistanceDistance extends ScoringDistance<Double> {
    /**
     * Creates a new instance of a scoring scheme. The case of characters is significant
     * when subsequently computing their score.
     *
     * @param match
     * @param mismatch
     * @param gap
     * @param tolerance
     */
    public ScoringDistanceDistance(double match, double mismatch, double gap, double tolerance) {
        super(match, mismatch, gap, tolerance);
    }

    @Override
    public double scoreSubstitution(Snapshot<Double> a, Snapshot<Double> b) throws IncompatibleScoringSchemeException {
        return a.equalsAlignment(b, getTolerance());
    }

    @Override
    public double scoreInsertion() throws IncompatibleScoringSchemeException {
        return this.getGap();
    }

    @Override
    public double scoreDeletion() throws IncompatibleScoringSchemeException {
        return this.getGap();
    }
}
