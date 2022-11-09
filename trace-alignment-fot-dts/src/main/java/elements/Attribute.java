package elements;

import java.util.Objects;
/**
 * @author Paula Munoz
 */
public class Attribute {
    private final double maxValue;
    private final double tolerance;
    private final String name;

    public Attribute(double maxValue, String name, double tolerance){
        this.name = name;
        this.maxValue = maxValue;
        this.tolerance = tolerance;
    }

    public double getTolerance() {
        return tolerance;
    }
    public String getName(){
        return name;
    }

    public double getMaxValue() {
        return maxValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attribute attribute = (Attribute) o;
        return name.equals(attribute.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
