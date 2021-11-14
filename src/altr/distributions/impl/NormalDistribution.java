package altr.distributions.impl;

import altr.distributions.api.Distribution;

import java.util.Random;

/**
 * @author Vitaly
 */
public class NormalDistribution implements Distribution {

    private final Random generator = new Random();
    private final double mean;
    private final double deviation;


    public NormalDistribution(double mean, double deviation) {
        this.mean = mean;
        this.deviation = deviation;
    }

    @Override
    public double getValue() {
        return this.mean + (this.deviation * generator.nextGaussian());
    }

    @Override
    public String getName() {
        return String.format("NormalDistribution " +
                "mean = %f , deviation = %f", mean, deviation);
    }

}
