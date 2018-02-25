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
import altr.strategies.SimpleGroupStrategy;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Vitaly
 */
public class SimpleThresholdExperimentM extends ExperimentManager {    
       
    Collection<Person> group;
    Collection<Person> others;
    double[] groupAvgMoney;
    double[] othersAvgMoney;
    double[] avgMoney;
    double[] acceptanceCounters;
    int groupsSize;
    SimpleGroupStrategy groupStrategy;
    double threshold;

    public SimpleThresholdExperimentM(Experiment experiment, Environment env, Person groupMan, Person egoist, int groupsSize, double threshold) throws CloneNotSupportedException {
        super(experiment, env);
        this.groupsSize = groupsSize;
        groupAvgMoney = new double[stepNumber]; 
        othersAvgMoney = new double[stepNumber]; 
        avgMoney = new double[stepNumber]; 
        acceptanceCounters = new double[stepNumber]; 
        groupStrategy = new SimpleGroupStrategy(gM, threshold);
        groupMan.setStrategy(groupStrategy);
        egoist.setStrategy(new SimpleEgoisticStrategy());
        this.group = PersonManager.clonePerson(groupMan, groupsSize);
        this.others = PersonManager.clonePerson(egoist, groupsSize);
        this.threshold = threshold;

        Group g = new Group("test group", "group1");
        gM.addGroup(g);
        gM.addPeopleToGroup(group, g.getId());
        groupStrategy.init();
        pM.getPeople().addAll(group);
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
        writer.value(Double.toString(groupAvgMoney[last]/iterationNumber)).value(Double.toString(othersAvgMoney[last]/iterationNumber)).
            value(Double.toString(avgMoney[last]/iterationNumber)).value(Double.toString(acceptanceCounters[last]/iterationNumber)).value(Double.toString(threshold)).
            newLine();
        System.out.println("Finished!");
    }

    @Override
    protected void analysis() {
        double avg = Analyzer.getAverageMoney(group);
        double avg1 = Analyzer.getAverageMoney(others);
        double avg2 = Analyzer.getAverageMoney(pM.getPeople());
        groupAvgMoney[step] += avg;
        othersAvgMoney[step] += avg1;
        avgMoney[step] += avg2;  
        acceptanceCounters[step] += acceptanceCounter;
    }

    @Override
    protected void postprocessing() {
        groupStrategy.reset();
        for(Person p: pM.getPeople()){
            p.setActive(p.getMoney() > 0);
        }
    }
    
}
