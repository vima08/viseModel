package altr.managers.experiment;

import altr.Environment;
import altr.entity.Group;
import altr.entity.Person;
import altr.experiment.Experiment;
import altr.managers.Analyzer;
import altr.managers.PersonManager;
import altr.strategies.PerspectiveGroupStrategy;
import altr.strategies.SimpleEgoisticStrategy;
import altr.strategies.SimpleGroupStrategy;

import java.util.Collection;

import static altr.Runner.writer;

/**
 *
 * @author Vitaly
 */
public class SimplePerspectiveGroupExperimentManager extends ExperimentManager {

    Collection<Person> group;
    double[] avgMoney;
    double[] acceptanceCounters;
    int groupSize;
    PerspectiveGroupStrategy groupStrategy;

    public SimplePerspectiveGroupExperimentManager(Experiment experiment, Environment env, Person groupMan, int groupSize) throws CloneNotSupportedException {
        super(experiment, env);
        this.groupSize = groupSize;
        avgMoney = new double[stepNumber]; 
        acceptanceCounters = new double[stepNumber]; 
        groupStrategy = new PerspectiveGroupStrategy(gM, 0.0);
        groupMan.setStrategy(groupStrategy);
        this.group = PersonManager.clonePerson(groupMan, groupSize);

        Group g = new Group("test group", "group1");
        gM.addGroup(g);
        gM.addPeopleToGroup(group, g.getId());
        groupStrategy.init();
        pM.getPeople().addAll(group);
    }
    
    @Override
    protected void results() {
        int last = stepNumber-1;
        writer.value(Double.toString(avgMoney[last]/iterationNumber)).value(Double.toString(acceptanceCounters[last]/iterationNumber)).
            newLine();
        System.out.println("Finished!");
    }

    @Override
    protected void analysis() {
        double avg2 = Analyzer.getAverageMoney(pM.getPeople());
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
