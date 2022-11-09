package alignment.tolerance;

import alignment.IncompatibleScoringSchemeException;
import alignment.ScoringDistance;
import elements.Snapshot;

public class ScoringDistanceTolerance extends ScoringDistance<Boolean> {

    /**
     * Creates a new instance of a scoring scheme. The case of characters is significant
     * when subsequently computing their score.
     *
     * @param match
     * @param mismatch
     * @param gap
     * @param tolerance
     */
    public ScoringDistanceTolerance(int match, int mismatch, int gap, double tolerance) {
        super(match, mismatch, gap, tolerance);
    }

    public double scoreSubstitution(Snapshot<Boolean> a, Snapshot<Boolean> b) throws IncompatibleScoringSchemeException {
        if(a.equalsAlignment(b, this.getTolerance())){
            return this.getMatch();
        } else {
            return this.getMismatch();
        }
    }

    public double scoreInsertion() throws IncompatibleScoringSchemeException {
        return this.getGap();
    }

    public double scoreDeletion() throws IncompatibleScoringSchemeException {
        return this.getGap();
    }
}
