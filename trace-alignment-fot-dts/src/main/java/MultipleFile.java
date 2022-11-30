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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.DoubleStream;

/**
 * @author Paula Munoz
 */

public class MultipleFile {
    private static final String CURRENT_DIR = System.getProperty("user.dir") + "\\trace-alignment-fot-dts";
    private static final String INPUT_DIR = "\\src\\main\\resources\\input\\";
    private static final String OUTPUT_DIR = "\\src\\main\\resources\\output\\";

    public static void main(String[] args) throws Exception {
        String inputPath = CURRENT_DIR + INPUT_DIR;
        String inputNxj = inputPath + "lift\\";

        String pythonScript = CURRENT_DIR + "\\src\\main\\python\\" + "process_alignment.py";

        String dTPath = inputNxj + "\\04.5-simulation\\";
        String pTPath = inputNxj + "\\03-derived_values\\";
        double tolerance = 0.5;

        String dTFile = "Bajada_4_0_4.csv";
        String output_file_path = CURRENT_DIR + OUTPUT_DIR + "lift\\";

        for(String pTFile : CSVUtil.filterFileNamesInDirectory(pTPath, ".csv", "Bajada_4_0_4_01")){
            // Empty the distances file
            String filename =  dTFile.substring(0, dTFile.length()-4)
                    + pTFile.substring(0, pTFile.length()-4);

            List<String[]> headers = new ArrayList<>();
            headers.add(new String[]{"gap", "%matched", "frechet", "mean", "std"});
            CSVUtil.writeAll(headers, output_file_path + filename + ".csv");

            for(double gap : DoubleStream.iterate(-0.4, n -> n+0.1).limit(1).toArray()){
                List<String[]> seqDT = CSVUtil.readAll(dTPath + dTFile, ',');
                List<String[]> seqPT = CSVUtil.readAll(pTPath+ pTFile, ',');

                String paramOfInterest = "accel(m/s2)";

                Object[] alignmentResults = getDistanceAlignment(seqDT, seqPT, tolerance, gap);
                List<String[]> alignment = (List<String[]>) alignmentResults[0];
                double score = (double) alignmentResults[1];

                System.out.println("Score " + tolerance + "=> " + score);

                String gapInformation =  "-" + String.format("%.1f",gap) + ".csv";
                CSVUtil.writeAll(alignment, output_file_path + filename + gapInformation);

                // Generate graphics and distance analysis
                Process p = Runtime.getRuntime().exec("python \"" + pythonScript + "\" "
                        + filename + gapInformation + " " + paramOfInterest + " \"" + output_file_path + "\"");
                p.waitFor();
            }
        }
    }

    public static Object[] getDistanceAlignment(List<String[]> seqDT, List<String[]> seqPT, double tolerance,
                                                double gap) throws Exception {
        NDWDistance nw = new NDWDistance();
        DistanceEquivalenceTrace traceDT = new DistanceEquivalenceTrace(seqDT);
        DistanceEquivalenceTrace tracePT = new DistanceEquivalenceTrace(seqPT);
        nw.loadSequences(traceDT, tracePT);

        ScoringDistanceDistance scoringDistance = new ScoringDistanceDistance(1, 0, gap, tolerance);
        nw.setScoringScheme(scoringDistance);
        return new Object[] {nw.getPairwiseAlignment(), nw.getScore()};
    }
}
