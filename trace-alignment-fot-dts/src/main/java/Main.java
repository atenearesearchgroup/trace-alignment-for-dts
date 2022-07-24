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
    private static final String INPUT_DIR = "/src/main/resources/input/";
    private static final String OUTPUT_DIR = "/src/main/resources/output/";

    public static void main(String[] args) throws Exception {
        NeedlemanWunschTrace nw = new NeedlemanWunschTrace();

        String inputPath = CURRENT_DIR + INPUT_DIR;
        String inputNxj = inputPath + "nxj/";

        String DTFile = "LegoCarSyntheticTraces-1lapcar2.csv";
        String PTFile = "LegoCarSyntheticTraces-1lapcar2-accel-PT.csv";
        double tolerance = 0.5;

        List<String[]> seqDT = CSVUtil.readAll(inputNxj + DTFile, ',');
        List<String[]> seqPT = CSVUtil.readAll(inputNxj + PTFile, ',');

        Trace traceDT = new Trace(seqDT);
        Trace tracePT = new Trace(seqPT);
        nw.loadSequences(traceDT, tracePT);

        ScoringDistance scoringDistance = new ScoringDistance(1, -1, 0, tolerance);
        nw.setScoringScheme(scoringDistance);

        List<String[]> alignment = nw.getPairwiseAlignment();
        System.out.println("Score " + tolerance + "=> " + nw.getScore());

        CSVUtil.writeAll(alignment,
                CURRENT_DIR + OUTPUT_DIR + "nxj/" + DTFile.substring(0, DTFile.length()-4)
                        + PTFile.substring(PTFile.indexOf("-"), PTFile.length()-4) + "-" + tolerance +
                        ".csv");
    }
}
