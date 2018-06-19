package altr.managers.experiment;

import altr.Environment;
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
import java.util.Collections;
import java.util.Iterator;

import static altr.Runner.writer;

/**
 *
 * @author Vitaly
 */
public class SimpleExperimentWithMortalityManager extends ExperimentManager {

    Collection<Person> group;
    Collection<Person> others;
    double[] groupAvgMoney;
    double[] othersAvgMoney;
    double[] avgMoney;
    double[] acceptanceCounters;

    double groupIncrement;
    long groupIncrementCounter;

    double egoIncrement;
    long egoIncrementCounter;


    SimpleGroupStrategy groupStrategy;

    Person egoPerson;
    int egoCount;

    Person groupPerson;
    int groupCount;

    long liveEgoCount[];
    long liveGroupCount[];

    public SimpleExperimentWithMortalityManager(Experiment experiment, Environment env, Person groupMan, int groupSize, Person egoist, int egoSize) throws CloneNotSupportedException {
        super(experiment, env);

        this.groupCount = groupSize;
        this.egoCount = egoSize;

        groupAvgMoney = new double[stepNumber]; 
        othersAvgMoney = new double[stepNumber]; 
        avgMoney = new double[stepNumber]; 
        acceptanceCounters = new double[stepNumber]; 
        groupStrategy = new SimpleGroupStrategy(gM, 0.0);
        groupMan.setStrategy(groupStrategy);
        egoist.setStrategy(new SimpleEgoisticStrategy());
        this.group = PersonManager.clonePerson(groupMan, groupSize);
        this.others = PersonManager.clonePerson(egoist, egoSize);
        this.groupPerson = groupMan;
        this.egoPerson = egoist;

        Group g = new Group("test group", "group1");
        gM.addGroup(g);
        gM.addPeopleToGroup(group, g.getId());
        groupStrategy.init();
        pM.getPeople().addAll(group);
        pM.getPeople().addAll(others);

        this.groupIncrement = 0;
        this.groupIncrementCounter = 0;

        this.egoIncrement = 0;
        this.egoIncrementCounter = 0;

        this.liveEgoCount = new long[stepNumber];
        this.liveGroupCount = new long[stepNumber];
    }
    
    @Override
    protected void results() {      
//        writer.comment("test");
//        writer.value("Average capital").newLine();
//        for(int i = 0; i < stepNumber; i++){
//            writer.value(Double.toString(groupAvgMoney[i]/iterationNumber)).value(Double.toString(othersAvgMoney[i]/iterationNumber)).
//                    value(Double.toString(avgMoney[i]/iterationNumber)).value(Double.toString(acceptanceCounters[i]/iterationNumber)).newLine();
//        }

//        writer.value(Double.toString(groupAvgMoney[last]/iterationNumber)).value(Double.toString(othersAvgMoney[last]/iterationNumber)).
//            value(Double.toString(avgMoney[last]/iterationNumber)).value(Double.toString(acceptanceCounters[last]/iterationNumber)).
//            newLine();

        int last = stepNumber-1;
        double increment = totalIncrement/iterationNumber;
        double incCout = Double.valueOf(Long.toString(incrementCounter))/iterationNumber;

        double gIncrement = groupIncrement/iterationNumber;
        double gIncCout = Double.valueOf(Long.toString(groupIncrementCounter))/iterationNumber;

        double eIncrement = egoIncrement/iterationNumber;
        double eIncCout = Double.valueOf(Long.toString(egoIncrementCounter))/iterationNumber;

        writer.//value(Double.toString(othersAvgMoney[last]/iterationNumber)).
                value(Double.toString(gIncrement/gIncCout)).
                value(Double.toString(eIncrement/eIncCout)).
                value(Double.toString(acceptanceCounters[last]/iterationNumber)).
                value(Double.toString(increment)).
                value(Double.toString(incCout)).
                value(Double.toString(Double.valueOf(Long.toString(peopleCount[last]))/iterationNumber)).
                value(Double.toString(increment/incCout)).
                value(Double.toString(Double.valueOf(Long.toString(liveGroupCount[last]))/iterationNumber)).
                value(Double.toString(Double.valueOf(Long.toString(liveEgoCount[last]))/iterationNumber)).
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
//        for (Iterator<Person> it = pM.getPeople().iterator(); it.hasNext(); ) {
//            if (it.next().getMoney() < 0) {
//                it.remove();
//            }
//        }
    }

    protected void resetIteration() {
        try {
            this.group = PersonManager.clonePerson(groupPerson, groupCount);
            this.others = PersonManager.clonePerson(egoPerson, egoCount);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        pM.setPeople(new ArrayList<>());
        gM.setGroups(new ArrayList<>());
        step = 0;
        acceptanceCounter = 0;
        Group g = new Group("test group", "group1");
        gM.addGroup(g);
        gM.addPeopleToGroup(group, g.getId());
        groupStrategy.init();
        pM.getPeople().addAll(group);
        pM.getPeople().addAll(others);
    }

    protected void accept(Boolean isAccepted, Collection<Offer> offers) {
        Collection<Person> people = pM.getPeople();
        peopleCount[step] += people.size();
        group.retainAll(people);
        liveGroupCount[step] += group.size();
        liveEgoCount[step] = (peopleCount[step] - liveGroupCount[step]);
        if (!isAccepted) {
            incrementCounter += people.size();
            egoIncrementCounter += (people.size() - group.size());
            groupIncrementCounter += group.size();
        } else {
            for(Offer o: offers) {
                Person p  = PersonManager.getPersonById(o.getPersonId(), people);
                p.setMoney(p.getMoney() + o.getAmount());
                totalIncrement += o.getAmount();
                incrementCounter++;
                if (gM.getGroupsByPersonId(p.getId()).isEmpty()) {
                    egoIncrement += o.getAmount();
                    egoIncrementCounter++;
                } else {
                    groupIncrement += o.getAmount();
                    groupIncrementCounter++;
                }
            }
        }
    }

}
