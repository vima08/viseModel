package altr.managers.experiment;

import altr.Environment;
import altr.distributions.api.Distribution;
import altr.entity.Group;
import altr.entity.Offer;
import altr.entity.Person;
import altr.experiment.Experiment;
import altr.managers.Analyzer;
import altr.managers.PersonManager;
import altr.strategies.SimpleAltruisticStrategy;
import altr.strategies.SimpleEgoisticStrategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static altr.Runner.writer;

/**
 *
 * @author Vitaly
 */
public class AltrWithMortalityExperimentManager extends ExperimentManager {

    Collection<Person> group;
    Collection<Person> others;
    double[] groupAvgMoney;
    double[] othersAvgMoney;
    double[] avgMoney;
    double[] acceptanceCounters;
    int altrSize;
    int egoCount;
    SimpleAltruisticStrategy altrStrategy;
    double totalIncrement;
    long incrementCounter;
    long peopleCount[];
    Person egoPerson;
    Person altrPerson;

    public AltrWithMortalityExperimentManager(Experiment experiment, Environment env, Person altr, int altrSize, Person egoist, int egoSize, double leftBound, double rightBound) throws CloneNotSupportedException {
        super(experiment, env);
        this.altrSize = altrSize;
        this.egoCount = egoSize;
        groupAvgMoney = new double[stepNumber];
        othersAvgMoney = new double[stepNumber];
        avgMoney = new double[stepNumber];
        acceptanceCounters = new double[stepNumber];
        altrStrategy = new SimpleAltruisticStrategy(leftBound, rightBound, 0.0);
        altr.setStrategy(altrStrategy);
        egoist.setStrategy(new SimpleEgoisticStrategy());
        this.group = PersonManager.clonePerson(altr, altrSize);
        this.others = PersonManager.clonePerson(egoist, egoSize);
        this.totalIncrement = 0;
        this.incrementCounter = 0;
        this.peopleCount = new long[stepNumber];
        egoPerson = egoist;
        altrPerson = altr;

        Group g = new Group("test altr group", "group1");
        gM.addGroup(g);
        gM.addPeopleToGroup(group, g.getId());
        pM.getPeople().addAll(group);
        pM.getPeople().addAll(others);
    }

    public AltrWithMortalityExperimentManager(Experiment experiment, Environment env, Person altr, int altrSize, Person egoist, int egoSize) throws CloneNotSupportedException {
        this(experiment, env, altr, altrSize, egoist, egoSize, 0, 100);
    }
    
    @Override
    protected void accept(Boolean isAccepted, Collection<Offer> offers) {
        Collection<Person> people = pM.getPeople();
        peopleCount[step] += people.size();
        if (!isAccepted) {
            incrementCounter += people.size();
        } else {
            for(Offer o: offers) {
                Person p  = PersonManager.getPersonById(o.getPersonId(), people);
                p.setMoney(p.getMoney() + o.getAmount());
                totalIncrement += o.getAmount();
                incrementCounter++;
            }
        }
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
        writer.//value(Double.toString(groupAvgMoney[last]/iterationNumber)).
//            value(Double.toString(othersAvgMoney[last]/iterationNumber)).
//            value(Double.toString(avgMoney[last]/iterationNumber)).
            value(Double.toString(acceptanceCounters[last]/iterationNumber)).
            value(Double.toString(totalIncrement/iterationNumber)).
            value(Long.toString(incrementCounter/iterationNumber)).
            value(Long.toString(peopleCount[last]/iterationNumber)).
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
        altrStrategy.reset();
        for (Iterator<Person> it = pM.getPeople().iterator(); it.hasNext(); ) {
            if (it.next().getMoney() < 0) {
                it.remove();
            }
        }
    }

    protected void resetIteration() {
        try {
            this.others = PersonManager.clonePerson(egoPerson, egoCount);
            this.group = PersonManager.clonePerson(altrPerson, altrSize);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        pM.reset();
        step = 0;
        acceptanceCounter = 0;
        pM.setPeople(others);
        pM.getPeople().addAll(group);
    }
    
}
