package altr;

public class Variable {
    private Object val;
    private boolean isVariable = false;
    private Double multiplier;

    public Variable(boolean isVariable, Object val) {
        this.isVariable = isVariable;
        if (isVariable) {
            this.multiplier = (Double) val;
            this.val = null;
        } else {
            this.multiplier = null;
            this.val = val;
        }
    }

    public Object getValue(int variable) {
        return isVariable ? multiplier * variable : val;
    }

    public Object getValue() {
        if (isVariable) {
            System.out.print("variable is empty");
            throw new RuntimeException("variable is empty");
        }
        return val;
    }

    public boolean isVariable() {
        return isVariable;
    }
}
