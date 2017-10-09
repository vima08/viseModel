package altr.distributions.impl;

import altr.distributions.api.Distribution;

/**
 * @author Vitaly
 */
// technically in is not a distribution
public class ConstantDistribution implements Distribution {

    @Override
    public double getValue() {
        return 1;
    }

    @Override
    public String getName() {
        return "ConstantDistribution";
    }

}
