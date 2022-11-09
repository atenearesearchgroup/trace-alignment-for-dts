package alignment;

import elements.Snapshot;

public abstract class ScoringDistance<T> extends ScoringSchemeTrace<T> {

    /**
     * Creates a new instance of an scoring scheme. The case of characters is significant
     * when subsequently computing their score.
     *
     * @param match
     * @param mismatch
     * @param gap
     */
    public ScoringDistance(double match, double mismatch, double gap, double tolerance) {
        super(match, mismatch, gap, tolerance);
    }

    @Override
    public abstract double scoreSubstitution(Snapshot<T> a, Snapshot<T> b) throws IncompatibleScoringSchemeException;

    @Override
    public abstract double scoreInsertion() throws IncompatibleScoringSchemeException;

    @Override
    public abstract double scoreDeletion() throws IncompatibleScoringSchemeException;

    @Override
    public double maxAbsoluteScore() {
        return Math.max(Math.abs(this.getMismatch()), Math.max(Math.abs(this.getGap()), this.getMatch()));
    }

    @Override
    public boolean isPartialMatchSupported() {
        return true;
    }
}
