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
import java.util.Iterator;

/**
 *
 * @author Vitaly
 */
public class SimpleEgoWithMortalityExperimentManager extends ExperimentManager {    
    
    Collection<Person> others;    
    double[] othersAvgMoney;    
    double[] acceptanceCounters;
    SimpleGroupStrategy groupStrategy;
    Person egoPerson;
    int egoCount;

    public SimpleEgoWithMortalityExperimentManager(Experiment experiment, Environment env, Person egoist, int egoSize) throws CloneNotSupportedException {
        super(experiment, env);
        othersAvgMoney = new double[stepNumber];         
        acceptanceCounters = new double[stepNumber]; 
        
        egoist.setStrategy(new SimpleEgoisticStrategy());
        
        this.others = PersonManager.clonePerson(egoist, egoSize);
        egoPerson = egoist;
        egoCount = egoSize;
        
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
        double increment = totalIncrement/iterationNumber;
        double incCout = Double.valueOf(Long.toString(incrementCounter))/iterationNumber;
        writer.//value(Double.toString(othersAvgMoney[last]/iterationNumber)).
            value(Double.toString(acceptanceCounters[last]/iterationNumber)).
            value(Double.toString(increment)).
            value(Double.toString(incCout)).
            value(Double.toString(Double.valueOf(Long.toString(peopleCount[last]))/iterationNumber)).
            value(Double.toString(increment/incCout)).
            newLine();
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
