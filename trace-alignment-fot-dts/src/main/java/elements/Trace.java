package elements;

import java.util.*;

/**
 * @author Paula Munoz
 */
public class Trace {
    private final List<Snapshot> snapshots;

    public Trace(List<String[]> fullMatrix) {
        this.snapshots = new LinkedList<>();
        String[] headers = fullMatrix.get(0);
        // Find the timestamp headers only to consider the columns to the right of it
        int startingColumn = Arrays.asList(headers).indexOf("timestamp") + 1;

        Attribute[] attributes = new Attribute[headers.length - startingColumn];
        for (int i = startingColumn; i < headers.length; i++) {
            double max = getMax(fullMatrix, i);
            attributes[i-startingColumn] = new Attribute(max, headers[i]);
        }

        for (String[] row : fullMatrix.subList(1, fullMatrix.size())) {
            Object[] values = new Object[row.length - startingColumn];
            for (int i = startingColumn; i < row.length; i++) {
                if(isDouble(row[i])){
                    values[i-startingColumn] = Double.parseDouble(row[i]);
                } else if(row[i].equals("FALSE") || row[i].equals("TRUE")){
                    values[i-startingColumn] = Boolean.parseBoolean(row[i]);
                } else {
                    values[i-startingColumn] = row[i];
                }
            }
            snapshots.add(new Snapshot(attributes, values,
                    Long.parseLong(row[startingColumn-1])));
        }
    }

    private static boolean isDouble(String string) {
        try {
            Double.parseDouble(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public List<Snapshot> getSnapshots() {
        return snapshots;
    }

    private double getMax(List<String[]> fullMatrix, int i) {
        double max = 0;
        List<Double> list = new ArrayList<>();
        for (int j = 1; j < fullMatrix.size(); j++) {
            try {
                list.add(Double.valueOf(fullMatrix.get(j)[i]));
            } catch (NumberFormatException e) {
                max = -1;
                break;
            }
        }
        if (max >= 0) {
            max = Collections.max(list);
        }
        return max;
    }

    public Snapshot snapshotAt(int i) {
        return snapshots.get(i);
    }
}
