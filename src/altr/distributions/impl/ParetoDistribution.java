package altr.distributions.impl;

import altr.distributions.api.Distribution;

import java.util.Random;

/**
 * @author Vitaly
 */
public class ParetoDistribution implements Distribution {

    private final Random generator = new Random();
    private final double mean;
    private final double deviation;
    private final double k;


    public ParetoDistribution(double mean, double deviation, double k) {
        this.mean = mean;
        this.deviation = deviation;
        this.k = k;
    }

    @Override
    public double getValue() {
        double b = mean + deviation * Math.sqrt((k-1) * (k-2) / 2);
        double a = mean - b;

        double uniform = generator.nextDouble();

        if (uniform <= 0.5)
            return b + a * (2 - Math.pow(2 * uniform, -1/k));
        else //if (uniform > 0.5)
            return b + a * Math.pow((2 - 2 * uniform), -1/k);
    }

    @Override
    public String getName() {
        return String.format("ParetoDistribution mean = %f , deviation = %f, k = %f", mean, deviation, k);
    }

}
