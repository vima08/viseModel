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
    double alpha; // acceptance
    SimpleGroupStrategy groupStrategy;
    double totalIncrement;
    long incrementCounter;
    Person egoPerson;
    int egoCount;
    long peopleCount[];

    public SimpleEgoWithMortalityExperimentManager(Experiment experiment, Environment env, Person egoist, int egoSize, Double alpha) throws CloneNotSupportedException {
        super(experiment, env);                
        this.alpha = alpha;
        othersAvgMoney = new double[stepNumber];         
        acceptanceCounters = new double[stepNumber]; 
        
        egoist.setStrategy(new SimpleEgoisticStrategy());
        
        this.others = PersonManager.clonePerson(egoist, egoSize);
        egoPerson = egoist;
        egoCount = egoSize;

        this.totalIncrement = 0;
        this.incrementCounter = 0;
        this.peopleCount = new long[stepNumber];
        
        pM.getPeople().addAll(others);
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
        writer.//value(Double.toString(othersAvgMoney[last]/iterationNumber)).
            value(Double.toString(acceptanceCounters[last]/iterationNumber)).
            value(Double.toString(totalIncrement/iterationNumber)).
            value(Long.toString(incrementCounter/iterationNumber)).
            value(Long.toString(peopleCount[last]/iterationNumber)).
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
    protected Collection<Offer> generateOffers(Distribution dist, Collection<Person> people) {
        Collection<Offer> offers = new ArrayList();
        for(Person p: people) {
            Offer offer = new Offer(dist.getValue(), p.getId());
            offers.add(offer);
            //System.out.println(offer);
        }
        return offers;
    }

    @Override
    protected boolean isAccepted(Collection<Offer> offers) {
        double votes = 0;
        long number = pM.getPeople().size();
        for(Person p: pM.getPeople()) {
            if (p.getStrategy().vote(offers, pM.getPeople(),  p.getId())) votes++;
        }
        double percentage = votes / number;
        return (percentage > alpha);
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
