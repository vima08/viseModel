package altr.managers.experiment;

import altr.Environment;
import altr.entity.Person;
import altr.experiment.Experiment;
import altr.managers.Analyzer;
import altr.managers.PersonManager;
import altr.strategies.SimpleEgoisticStrategy;
import altr.strategies.SimpleGroupStrategy;

import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author Vitaly
 */
public class SimpleEgo extends ExperimentManager {

    Collection<Person> others;
    double[] othersAvgMoney;
    double[] acceptanceCounters;
    SimpleGroupStrategy groupStrategy;
    Person egoPerson;
    int egoCount;
    double result;

    public SimpleEgo(Experiment experiment, Environment env, Person egoist, int egoSize) throws CloneNotSupportedException {
        super(experiment, env);
        othersAvgMoney = new double[stepNumber];         
        acceptanceCounters = new double[stepNumber]; 
        
        egoist.setStrategy(new SimpleEgoisticStrategy());
        
        this.others = PersonManager.clonePerson(egoist, egoSize);
        egoPerson = egoist;
        egoCount = egoSize;
        
        pM.getPeople().addAll(others);
    }

    public double getResult() {
        return result;
    }

    @Override
    protected void results() {
        int last = stepNumber-1;
        double inc = Double.valueOf(Long.toString(incrementCounter))/iterationNumber;
        double total = totalIncrement/iterationNumber;
        result =  total / inc;
        System.out.println("Finished!");
    }

    @Override
    protected void analysis() {        
        double avg1 = Analyzer.getAverageMoney(others);
        othersAvgMoney[step] += avg1;
        acceptanceCounters[step] += acceptanceCounter;
    }

    @Override
    protected void postprocessing() {             
        for (Iterator<Person> it = pM.getPeople().iterator(); it.hasNext(); ) {
            if (it.next().getMoney() < 0) {
                it.remove();
            }
        }        
    }

    protected void resetIteration() {
        try {
            this.others = PersonManager.clonePerson(egoPerson, egoCount);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        pM.reset();
        step = 0;
        acceptanceCounter = 0;
        pM.setPeople(others);
    }
    
}
