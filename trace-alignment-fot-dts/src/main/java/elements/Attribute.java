package elements;

import java.util.Objects;
/**
 * @author Paula Munoz
 */
public class Attribute {
    private final double maxValue;
    private final String name;

    public Attribute(double maxValue, String name){
        this.name = name;
        this.maxValue = maxValue;
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
