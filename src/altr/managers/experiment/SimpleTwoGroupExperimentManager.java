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
public class SimpleTwoGroupExperimentManager extends ExperimentManager {
       
    Collection<Person> group;
    Collection<Person> others;
    Collection<Person> fixedGroup;
    double[] groupAvgMoney;
    double[] fixedGroupAvgMoney;
    double[] othersAvgMoney;
    double[] avgMoney;
    double[] acceptanceCounters;
    int groupSize;
    SimpleGroupStrategy groupStrategy;

    public SimpleTwoGroupExperimentManager(Experiment experiment, Environment env, Person groupMan, int groupSize, Person egoist, int egoSize) throws CloneNotSupportedException {
        super(experiment, env);
        this.groupSize = groupSize;
        groupAvgMoney = new double[stepNumber]; 
        othersAvgMoney = new double[stepNumber]; 
        fixedGroupAvgMoney = new double[stepNumber];
        avgMoney = new double[stepNumber]; 
        acceptanceCounters = new double[stepNumber]; 
        groupStrategy = new SimpleGroupStrategy(gM, 0.0);
        groupMan.setStrategy(groupStrategy);
        egoist.setStrategy(new SimpleEgoisticStrategy());
        this.group = PersonManager.clonePerson(groupMan, groupSize);
        this.others = PersonManager.clonePerson(egoist, egoSize);
        this.fixedGroup = PersonManager.clonePerson(groupMan, 50);

        Group g = new Group("test group", "group1");
        gM.addGroup(g);
        gM.addPeopleToGroup(group, g.getId());
        
        Group g2 = new Group("fixed group", "group2");
        gM.addGroup(g2);
        gM.addPeopleToGroup(fixedGroup, g2.getId());
        
        groupStrategy.init();
        pM.getPeople().addAll(group);
        pM.getPeople().addAll(fixedGroup);
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
        writer.value(Double.toString(fixedGroupAvgMoney[last]/iterationNumber)).value(Double.toString(groupAvgMoney[last]/iterationNumber))
                .value(Double.toString(othersAvgMoney[last]/iterationNumber)).
            value(Double.toString(avgMoney[last]/iterationNumber)).value(Double.toString(acceptanceCounters[last]/iterationNumber)).
            newLine();
        System.out.println("Finished!");
    }

    @Override
    protected void analysis() {
        double avg = Analyzer.getAverageMoney(group);
        double avg1 = Analyzer.getAverageMoney(others);
        double avg2 = Analyzer.getAverageMoney(pM.getPeople());
        double avg3 = Analyzer.getAverageMoney(fixedGroup);
        groupAvgMoney[step] += avg;
        othersAvgMoney[step] += avg1;
        avgMoney[step] += avg2;  
        fixedGroupAvgMoney[step] += avg3;
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
