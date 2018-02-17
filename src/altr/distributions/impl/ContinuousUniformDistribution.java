package altr.distributions.impl;

import altr.distributions.api.Distribution;

import java.util.Random;

/**
 * @author Vitaly
 */
public class ContinuousUniformDistribution implements Distribution {

    private final Random generator = new Random();
    private final double a;
    private final double b;
    private final double mean;
    private final double deviation;


    public ContinuousUniformDistribution(double a, double b) {
        this.a = a;
        this.b = b;
        this.mean = (a + b) / 2;
        this.deviation = (b - a) / Math.sqrt(12);
    }

    public ContinuousUniformDistribution(double mean, double deviation, boolean x) {
        this.mean = mean;
        this.deviation = deviation;
        this.a = mean - deviation * Math.sqrt(12) / 2;
        this.b = mean + deviation * Math.sqrt(12) / 2;
    }

    @Override
    public double getValue() {
        double l = this.b - this.a;
        return this.a + (l * generator.nextDouble());
    }

    @Override
    public String getName() {
        return "ContinuousUniformDistribution";
    }

}
