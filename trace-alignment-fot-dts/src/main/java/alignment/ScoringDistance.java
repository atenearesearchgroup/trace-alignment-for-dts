package alignment;

import elements.Snapshot;

public class ScoringDistance extends ScoringSchemeTrace {

    /**
     * Creates a new instance of an scoring scheme. The case of characters is significant
     * when subsequently computing their score.
     *
     * @param match
     * @param mismatch
     * @param gap
     */
    public ScoringDistance(int match, int mismatch, int gap, double tolerance) {
        super(match, mismatch, gap, tolerance);
    }

    @Override
    public int scoreSubstitution(Snapshot a, Snapshot b) throws IncompatibleScoringSchemeException {
        if(a.equalsAlignment(b, this.getTolerance())){
            return this.getMatch();
        } else {
            return this.getMismatch();
        }
    }

    @Override
    public int scoreInsertion() throws IncompatibleScoringSchemeException {
        return this.getGap();
    }

    @Override
    public int scoreDeletion() throws IncompatibleScoringSchemeException {
        return this.getGap();
    }

    @Override
    public int maxAbsoluteScore() {
        return Math.max(Math.abs(this.getMismatch()), Math.max(Math.abs(this.getGap()), this.getMatch()));
    }

    @Override
    public boolean isPartialMatchSupported() {
        return true;
    }
}
