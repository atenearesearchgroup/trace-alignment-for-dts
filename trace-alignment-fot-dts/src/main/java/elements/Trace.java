package elements;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Paula Munoz
 */
public class Trace {
    private final List<Snapshot> snapshots;

    public List<Snapshot> getSnapshots() {
        return snapshots;
    }

    public Trace(List<String[]> fullMatrix){
        this.snapshots = new LinkedList<>();
        String[] headers = fullMatrix.get(0);
        String[] maxValues = fullMatrix.get(1);
        Attribute[] attributes = new Attribute[headers.length];
        for(int i = 0; i < headers.length - 1; i++){
            attributes[i] = new Attribute(Double.parseDouble(maxValues[i+1]), headers[i+1]);
        }

        for(String[] row : fullMatrix.subList(2, fullMatrix.size())){
            double[] values = Arrays.stream(row)
                    .mapToDouble(Double::parseDouble)
                    .toArray();
            snapshots.add(new Snapshot(
                    attributes,
                    Arrays.copyOfRange(values, 1, values.length),
                    (long) values[0]));
        }
    }

    public Snapshot snapshotAt(int i){
        return snapshots.get(i);
    }
}
