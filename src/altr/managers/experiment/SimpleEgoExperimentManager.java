package altr.managers.experiment;

import altr.Environment;
import static altr.Runner.writer;
import altr.distributions.api.Distribution;
import altr.entity.Offer;
import altr.entity.Person;
import altr.experiment.Experiment;
import altr.managers.Analyzer;
import altr.managers.PersonManager;
import altr.strategies.SimpleEgoisticStrategy;
import altr.strategies.SimpleGroupStrategy;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Vitaly
 */
public class SimpleEgoExperimentManager extends ExperimentManager {    
    
    Collection<Person> others;    
    double[] othersAvgMoney;
    double[] othersGMeanMoney;
    double[] othersMedianMoney;
    double[] acceptanceCounters;
    SimpleGroupStrategy groupStrategy;

    public SimpleEgoExperimentManager(Experiment experiment, Environment env, Person egoist, int egoSize) throws CloneNotSupportedException {
        super(experiment, env);
        othersAvgMoney = new double[stepNumber];
        othersGMeanMoney = new double[stepNumber];
        othersMedianMoney = new double[stepNumber];
        acceptanceCounters = new double[stepNumber]; 
        
        egoist.setStrategy(new SimpleEgoisticStrategy());
        
        this.others = PersonManager.clonePerson(egoist, egoSize);
        
        pM.getPeople().addAll(others);
    }
    
    @Override
    protected void results() {      
//        writer.comment("test");
//        writer.value("Average capital").newLine();
//        for(int i = 0; i < stepNumber; i++){
//            writer.value(Double.toString(groupAvgMoney[i]/iterationNumber)).value(Double.toString(othersAvgMoney[i]/iterationNumber)).
//                    value(Double.toString(avgMoney[i]/iterationNumber)).value(Double.toString(acceptanceCounters[i]/iterationNumber)).newLine();
//        }
        int last = stepNumber-1;
        writer.value(Double.toString(othersAvgMoney[last]/iterationNumber)).
                value(Double.toString(othersGMeanMoney[last]/iterationNumber)).
                value(Double.toString(othersMedianMoney[last]/iterationNumber)).
            value(Double.toString(acceptanceCounters[last]/iterationNumber)).
            newLine();
        System.out.println("Finished!");
    }

    @Override
    protected void analysis() {        
        double avg1 = Analyzer.getAverageMoney(others);
        double gMean = Analyzer.getGeometricMeanMoney(others);
        double median = Analyzer.getMedianMoney(others);
        othersAvgMoney[step] += avg1;
        othersGMeanMoney[step] += gMean;
        othersMedianMoney[step] += median;
        acceptanceCounters[step] += acceptanceCounter;
    }

    @Override
    protected void postprocessing() {        
        for(Person p: pM.getPeople()){
            p.setActive(p.getMoney() > 0);
        }
    }
    
}
