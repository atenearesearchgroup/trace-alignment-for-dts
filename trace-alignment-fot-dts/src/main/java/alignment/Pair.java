package alignment;
;
public class Pair {
    public double value;
    public Action action; // 0 substitution, 1 insertion, 2 deletion

    public Pair(double value, Action action){
        this.value = value;
        this.action = action;
    }

    public String toString(){
        return "[" + value + ", " + action.toString() + "]";
    }
}
