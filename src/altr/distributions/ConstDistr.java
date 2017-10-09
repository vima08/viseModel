package altr.distributions;

/**
 *
 * @author Vitaly
 */
public class ConstDistr implements Distribution {

    @Override
    public double getValue() {
        return 1;
    }

    @Override
    public String getName() {
        return "ConstDistr";
    }
    
}
