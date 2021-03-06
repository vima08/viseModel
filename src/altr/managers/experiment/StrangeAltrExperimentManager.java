package altr.managers.experiment;

import altr.Environment;
import static altr.Runner.writer;
import altr.distributions.api.Distribution;
import altr.entity.Group;
import altr.entity.Offer;
import altr.entity.Person;
import altr.experiment.Experiment;
import altr.managers.Analyzer;
import altr.managers.PersonManager;
import altr.strategies.SimpleEgoisticStrategy;
import altr.strategies.StrangeAltruisticStrategy;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Vitaly
 */
public class StrangeAltrExperimentManager extends ExperimentManager {    
       
    Collection<Person> altr;
    Collection<Person> others;
    double[] altrAvgMoney;
    double[] othersAvgMoney;
    double[] avgMoney;
    double[] acceptanceCounters;
    int altrSize;
    StrangeAltruisticStrategy altrStrategy;

    public StrangeAltrExperimentManager(Experiment experiment, Environment env, Person altrMan, 
            int altrSize, Person egoist, int egoSize, double threshold) throws CloneNotSupportedException {
        super(experiment, env);
        this.altrSize = altrSize;
        altrAvgMoney = new double[stepNumber]; 
        othersAvgMoney = new double[stepNumber]; 
        avgMoney = new double[stepNumber]; 
        acceptanceCounters = new double[stepNumber]; 
        altrStrategy = new StrangeAltruisticStrategy(threshold);
        altrMan.setStrategy(altrStrategy);
        egoist.setStrategy(new SimpleEgoisticStrategy());
        this.altr = PersonManager.clonePerson(altrMan, altrSize);
        this.others = PersonManager.clonePerson(egoist, egoSize);

        Group g = new Group("test altr", "altr1");
        gM.addGroup(g);
        gM.addPeopleToGroup(altr, g.getId());       
        pM.getPeople().addAll(altr);
        pM.getPeople().addAll(others);
    }
    
    @Override
    protected void results() {      
//        writer.comment("test");
//        writer.value("Average capital").newLine();
//        for(int i = 0; i < stepNumber; i++){
//            writer.value(Double.toString(altrAvgMoney[i]/iterationNumber)).value(Double.toString(othersAvgMoney[i]/iterationNumber)).
//                    value(Double.toString(avgMoney[i]/iterationNumber)).value(Double.toString(acceptanceCounters[i]/iterationNumber)).newLine();
//        }
        int last = stepNumber-1;
        writer.value(Double.toString(altrAvgMoney[last]/iterationNumber)).value(Double.toString(othersAvgMoney[last]/iterationNumber)).
            value(Double.toString(avgMoney[last]/iterationNumber)).value(Double.toString(acceptanceCounters[last]/iterationNumber)).
            newLine();
        System.out.println("Finished!");
    }

    @Override
    protected void analysis() {
        double avg = Analyzer.getAverageMoney(altr);
        double avg1 = Analyzer.getAverageMoney(others);
        double avg2 = Analyzer.getAverageMoney(pM.getPeople());
        altrAvgMoney[step] += avg;
        othersAvgMoney[step] += avg1;
        avgMoney[step] += avg2;  
        acceptanceCounters[step] += acceptanceCounter;
    }

    @Override
    protected void postprocessing() {
        altrStrategy.reset();
        for(Person p: pM.getPeople()){
            p.setActive(p.getMoney() > 0);
        }
    }
    
}
