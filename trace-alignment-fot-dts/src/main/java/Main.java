import alignment.NeedlemanWunschTrace;
import alignment.ScoringDistance;
import csv.util.CSVUtil;
import elements.Trace;

import java.util.List;

/**
 * @author Paula Munoz
 */

public class Main {
    private static final String CURRENT_DIR = System.getProperty("user.dir") + "/trace-alignment-fot-dts";
    private static final String INPUT_DIR = "/datafiles/input/";
    private static final String OUTPUT_DIR = "/datafiles/output/";
    private static final String CONTINUOUS = "continuous/";

    public static void main(String[] args) throws Exception {
        NeedlemanWunschTrace nw = new NeedlemanWunschTrace();

        String continuousPath = CURRENT_DIR + INPUT_DIR + CONTINUOUS;

        List<String[]> seqDT = CSVUtil.readAll(continuousPath + "delayDT.csv", ',');
        List<String[]> seqPT = CSVUtil.readAll(continuousPath + "delayPT.csv", ',');

        Trace traceDT = new Trace(seqDT);
        Trace tracePT = new Trace(seqPT);
        nw.loadSequences(tracePT, traceDT);

        double tolerance = 0.01;
        ScoringDistance scoringDistance = new ScoringDistance(2, -3, -1, tolerance);
        nw.setScoringScheme(scoringDistance);

        List<String[]> alignment = nw.getPairwiseAlignment();

        CSVUtil.writeAll(alignment, CURRENT_DIR + OUTPUT_DIR + "delay_result_" + tolerance + ".csv");
    }
}
