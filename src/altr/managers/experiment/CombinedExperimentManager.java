package altr.managers.experiment;

import altr.Environment;
import altr.entity.Group;
import altr.entity.Person;
import altr.experiment.Experiment;
import altr.managers.Analyzer;
import altr.managers.PersonManager;
import altr.strategies.CompinedStrategy;
import altr.strategies.SimpleEgoisticStrategy;
import altr.strategies.SimpleGroupStrategy;

import java.util.*;

/**
 *
 * @author Vitaly
 */
public class CombinedExperimentManager extends ExperimentManager {

    List<Collection<Person>> collections;
    List<double[]> avgMoney;
    double[] acceptanceCounters;
    SimpleGroupStrategy groupStrategy;
    Person egoPerson;
    int egoCount;
    double result;

    public CombinedExperimentManager(Experiment experiment, Environment env, Integer num, // number of groups
                                     Map<Person, Integer> groups) throws CloneNotSupportedException {
        super(experiment, env);
        //TODO check size of Lists
        collections = new ArrayList<Collection<Person>>();

        avgMoney = new ArrayList<>();
        acceptanceCounters = new double[stepNumber];

        Integer i = 0;
        for (Map.Entry<Person, Integer> var : groups.entrySet()) {
            i++;
            Person person = var.getKey();
            Integer groupSize = var.getValue();
            person.setStrategy(new CompinedStrategy(gM, person));
            Group group = new Group(i.toString(), "group");
            Collection<Person> people = PersonManager.clonePerson(person, groupSize);
            gM.addGroup(group);
            gM.addPeopleToGroup(people, group.getId());
            pM.getPeople().addAll(people);

            collections.add(people);
            avgMoney.add(new double[stepNumber]);
        }

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
        for (int i = 0; i < collections.size(); i++) {
            double avg1 = Analyzer.getAverageMoney(collections.get(i));
            avgMoney.get(i)[step] += avg1;
        }
        acceptanceCounters[step] += acceptanceCounter;
    }

    @Override
    protected void postprocessing() {             
//        for (Iterator<Person> it = pM.getPeople().iterator(); it.hasNext(); ) {
//            if (it.next().getMoney() < 0) {
//                it.remove();
//            }
//        }
    }

    protected void resetIteration() {
//        try {
//            this.others = PersonManager.clonePerson(egoPerson, egoCount);
//        } catch (CloneNotSupportedException e) {
//            e.printStackTrace();
//        }
        pM.reset();
        step = 0;
        acceptanceCounter = 0;
//        pM.setPeople(others);
    }
    
}
