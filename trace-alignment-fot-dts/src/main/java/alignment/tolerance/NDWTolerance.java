package alignment.tolerance;

import alignment.IncompatibleScoringSchemeException;
import alignment.NeedlemanWunschTrace;
import elements.Snapshot;

import java.util.ArrayList;
import java.util.List;

public class NDWTolerance extends NeedlemanWunschTrace<Boolean> {
    /**
     * Builds an optimal global alignment between the loaded sequences. Before it is
     * executed, the dynamic programming matrix must already have been computed by
     * the <CODE>computeMatrix</CODE> method.
     *
     * @return an optimal global alignment between the loaded sequences
     * @throws IncompatibleScoringSchemeException If the scoring scheme
     * is not compatible with the loaded sequences.
     * @see #computeMatrix
     */
    protected List<String[]> buildOptimalAlignment ()
            throws IncompatibleScoringSchemeException
    {
        List<String[]> partialResult = new ArrayList<>();
        List<Snapshot<Boolean>> snapshotsTrace1 = trace1.getSnapshots();
        List<Snapshot<Boolean>> snapshotsTrace2 = trace2.getSnapshots();

        int	r;
        int c;
        double sub;

        // start at the last row, last column
        r = matrix.length - 1;
        c = matrix[r].length - 1;

        while ((r > 0) || (c > 0))
        {
            List<String> row = new ArrayList<>();
            if (c > 0) {
                if (matrix[r][c] == matrix[r][c - 1] + scoreInsertion()) {
                    // insertion was used
                    for (int i = 0; i < snapshotsTrace1.get(0).getValues().size() + 1; i++) {
                        row.add(GAP_CHARACTER);
                    }
                    // row.add(EMPTY_SPACE);
                    row.add(String.valueOf(snapshotsTrace2.get(c-1).getTimestamp()));
                    row.addAll(snapshotsTrace2.get(c-1).getValues());
                    c = c - 1;
                    row.add(INSERTION);

                    // skip to the next iteration
                    partialResult.add(0, row.toArray(new String[row.size()]));
                    continue;
                }
            }

            if ((r > 0) && (c > 0))
            {
                sub = scoreSubstitution(trace1.snapshotAt(r-1), trace2.snapshotAt(c-1));

                if (matrix[r][c] == matrix[r-1][c-1] + sub)
                {
                    // substitution was used
                    row.add(String.valueOf(snapshotsTrace1.get(r-1).getTimestamp()));
                    row.addAll(snapshotsTrace1.get(r-1).getValues());
                    if (trace1.snapshotAt(r-1).equalsAlignment(trace2.snapshotAt(c-1), this.scoring.getTolerance())){
                        // row.add(MATCH_CHARACTER);
                        row.add(String.valueOf(snapshotsTrace2.get(c-1).getTimestamp()));
                        row.addAll(snapshotsTrace2.get(c-1).getValues());
                        row.add(MATCH);
                    } else {
                        // row.add(EMPTY_SPACE);
                        row.add(String.valueOf(snapshotsTrace2.get(c-1).getTimestamp()));
                        row.addAll(snapshotsTrace2.get(c-1).getValues());
                        row.add(MISMATCH);
                    }
                    r = r - 1; c = c - 1;

                    // skip to the next iteration
                    partialResult.add(0, row.toArray(new String[row.size()]));
                    continue;
                }
            }

            // must be a deletion
            row.add(String.valueOf(snapshotsTrace1.get(r-1).getTimestamp()));
            row.addAll(snapshotsTrace1.get(r-1).getValues());
            // row.add(EMPTY_SPACE);
            for(int i = 0; i < snapshotsTrace2.get(0).getValues().size() + 1; i++){
                row.add(GAP_CHARACTER);
            }
            row.add(DELETION);
            r = r - 1;
            partialResult.add(0, row.toArray(new String[row.size()]));
        }

        return partialResult;
    }
}