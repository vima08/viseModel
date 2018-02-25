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
import altr.strategies.SimpleAltruisticStrategy;
import altr.strategies.SimpleEgoisticStrategy;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Vitaly
 */
public class AltrExperimentManager extends ExperimentManager {    
       
    Collection<Person> group;
    Collection<Person> others;
    double[] groupAvgMoney;
    double[] othersAvgMoney;
    double[] avgMoney;
    double[] acceptanceCounters;
    int altrSize;
    SimpleAltruisticStrategy altrStrategy;
    double acceptance;
  
    public AltrExperimentManager(Experiment experiment, Environment env, Person altr, int altrSize, Person egoist, int egoSize, double acceptance, double leftBound, double rightBound) throws CloneNotSupportedException {
        super(experiment, env);
        this.altrSize = altrSize;
        groupAvgMoney = new double[stepNumber]; 
        othersAvgMoney = new double[stepNumber]; 
        avgMoney = new double[stepNumber]; 
        acceptanceCounters = new double[stepNumber]; 
        altrStrategy = new SimpleAltruisticStrategy(leftBound, rightBound, 0.0);
        this.acceptance = acceptance;
        altr.setStrategy(altrStrategy);
        egoist.setStrategy(new SimpleEgoisticStrategy());
        this.group = PersonManager.clonePerson(altr, altrSize);
        this.others = PersonManager.clonePerson(egoist, egoSize);

        Group g = new Group("test altr group", "group1");
        gM.addGroup(g);
        gM.addPeopleToGroup(group, g.getId());        
        pM.getPeople().addAll(group);
        pM.getPeople().addAll(others);
    }

    public AltrExperimentManager(Experiment experiment, Environment env, Person altr, int altrSize, Person egoist, int egoSize, double acceptance) throws CloneNotSupportedException {
        this(experiment, env, altr, altrSize, egoist, egoSize, acceptance, 0, 100);
    }

    public AltrExperimentManager(Experiment experiment, Environment env, Person altr, int altrSize, Person egoist, int egoSize) throws CloneNotSupportedException {
        this(experiment, env, altr, altrSize, egoist, egoSize, 0.5);
    }
    
    @Override
    protected void accept(Boolean isAccepted, Collection<Offer> offers) {
        if (!isAccepted) return;
        ArrayList<Person> p = (ArrayList)pM.getPeople();
        ArrayList<Offer> o = (ArrayList)offers;
        for(int i =0; i < pM.getPeople().size(); i++) {
            Double money = p.get(i).getMoney();
            p.get(i).setMoney(money + o.get(i).getAmount());
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
        writer.value(Double.toString(groupAvgMoney[last]/iterationNumber)).value(Double.toString(othersAvgMoney[last]/iterationNumber)).
            value(Double.toString(avgMoney[last]/iterationNumber)).value(Double.toString(acceptanceCounters[last]/iterationNumber)).
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
    protected boolean isAccepted(Collection<Offer> offers) {
        double votes = 0;
        long number = pM.getPeople().size();
        for(Person p: pM.getPeople()) {
            if (p.getStrategy().vote(offers, pM.getPeople(),  p.getId())) votes++;
        }
        double percentage = votes / number;
        return (percentage > acceptance);
    }

    @Override
    protected void postprocessing() {
        altrStrategy.reset();
        for(Person p: pM.getPeople()){
            p.setActive(p.getMoney() > 0);
        }
    }
    
}
