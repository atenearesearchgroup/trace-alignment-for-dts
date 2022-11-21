import alignment.IncompatibleScoringSchemeException;
import alignment.InvalidSequenceException;
import alignment.distance.NDWDistance;
import alignment.distance.ScoringDistanceDistance;
import alignment.tolerance.NDWTolerance;
import alignment.tolerance.ScoringDistanceTolerance;
import csv.util.CSVUtil;
import elements.distance.DistanceEquivalenceTrace;
import elements.tolerance.ToleranceEquivalenceTrace;

import java.io.IOException;
import java.util.List;

/**
 * @author Paula Munoz
 */

public class SingleFile {
    private static final String CURRENT_DIR = System.getProperty("user.dir") + "/trace-alignment-fot-dts";
    private static final String INPUT_DIR = "/src/main/resources/input/";
    private static final String OUTPUT_DIR = "/src/main/resources/output/";

    public static void main(String[] args) throws Exception {
        String inputPath = CURRENT_DIR + INPUT_DIR;
        String inputNxj = inputPath + "lift\\";

        String pythonScript = CURRENT_DIR + "\\src\\main\\python\\" + "graphic_generator.py";

        String DTPath = inputNxj + "\\04.5-simulation\\";
        String PTPath = inputNxj + "\\03-derived_values\\";

        String DTFile = "Bajada_4_0_4.csv";
        String PTFile = "Bajada_4_0_4_02.csv";
        double tolerance = 0.5;

        List<String[]> seqDT = CSVUtil.readAll(DTPath + DTFile, ',');
        List<String[]> seqPT = CSVUtil.readAll(PTPath+ PTFile, ',');

        // Alignment based on tolerance equivalence
        //Object[] alignmentResults = getToleranceAlignment(seqDT, seqPT, tolerance);
        Object[] alignmentResults = getDistanceAlignment(seqDT, seqPT, tolerance);
        List<String[]> alignment = (List<String[]>) alignmentResults[0];
        double score = (double) alignmentResults[1];

        System.out.println("Score " + tolerance + "=> " + score);

        String filename = CURRENT_DIR + OUTPUT_DIR + "lift\\" + DTFile.substring(0, DTFile.length()-4)
                + PTFile.substring(0, PTFile.length()-4) + "-" + tolerance +
                ".csv";
        CSVUtil.writeAll(alignment, filename);

        String paramOfInterest = "accel(m/s2)";
        Runtime.getRuntime().exec("python \"" + pythonScript + "\" \"" + filename + "\" " + paramOfInterest);
    }

    public static Object[] getToleranceAlignment(List<String[]> seqDT, List<String[]> seqPT, double tolerance) throws Exception {
        NDWTolerance nw = new NDWTolerance();
        ToleranceEquivalenceTrace traceDT = new ToleranceEquivalenceTrace(seqDT);
        ToleranceEquivalenceTrace tracePT = new ToleranceEquivalenceTrace(seqPT);
        nw.loadSequences(traceDT, tracePT);

        ScoringDistanceTolerance scoringDistance = new ScoringDistanceTolerance(1, -2, 0, tolerance);
        nw.setScoringScheme(scoringDistance);
        return new Object[] {nw.getPairwiseAlignment(), nw.getScore()};
    }

    public static Object[] getDistanceAlignment(List<String[]> seqDT, List<String[]> seqPT, double tolerance) throws Exception {
        NDWDistance nw = new NDWDistance();
        DistanceEquivalenceTrace traceDT = new DistanceEquivalenceTrace(seqDT);
        DistanceEquivalenceTrace tracePT = new DistanceEquivalenceTrace(seqPT);
        nw.loadSequences(traceDT, tracePT);

        ScoringDistanceDistance scoringDistance = new ScoringDistanceDistance(1, 0, -0.2, tolerance);
        nw.setScoringScheme(scoringDistance);
        return new Object[] {nw.getPairwiseAlignment(), nw.getScore()};
    }
}
