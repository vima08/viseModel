package altr.managers.experiment;

import altr.Environment;
import altr.distributions.api.Distribution;
import altr.entity.Offer;
import altr.entity.Person;
import altr.experiment.Experiment;
import altr.experiment.Stage;
import altr.managers.GroupManager;
import altr.managers.PersonManager;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Vitaly
 */
public abstract class ExperimentManager {
    
    public PersonManager pM;
    public GroupManager gM;
    public Experiment experiment;
    public Environment env;
    int acceptanceCounter;
    int step;
    int stepNumber;
    int iterationNumber;
    double totalIncrement;
    long incrementCounter;
    long peopleCount[];
    
    public ExperimentManager(Experiment experiment, Environment env) {
        this.experiment = experiment;
        this.env = env;
        this.pM = new PersonManager(this.env);
        this.gM = new GroupManager(this.env);
        this.iterationNumber = experiment.getIterationNumber();
        stepNumber = experiment.getStepNumber();
        this.totalIncrement = 0;
        this.incrementCounter = 0;
        this.peopleCount = new long[stepNumber];
    }
    
    public void carryOut(){
        init();
        for(int j = 0; j < experiment.getIterationNumber(); j++){
            for(Stage stage: experiment.getStages()) {
                for(int i =0; i < stage.getStepNum(); i++) {                    
                    //System.out.println("Step " + step + ": ");
                    //System.out.println(pM.getPeople());
                    Collection<Offer> offers = generateOffers(stage.getDestribution(), pM.getPeople());
                    boolean accepted = isAccepted(offers, env.getAlpha());
                    //mulAccept(accepted, offers);
                    accept(accepted, offers);
                    if (accepted) acceptanceCounter++;
                    analysis();
                    postprocessing();
                    step++;
                }
            }
            resetIteration();
        }
        results();
    };   
    
    protected void resetIteration() {
        pM.reset();
        step = 0;
        acceptanceCounter = 0;
    }
    
    protected void init() {
        step = 0;
        acceptanceCounter = 0;
    }

    protected Collection<Offer> generateOffers(Distribution dist, Collection<Person> people) {
        Collection<Offer> offers = new ArrayList();
        for(Person p: people) {
            Offer offer = new Offer(dist.getValue(), p.getId());
            offers.add(offer);
            //System.out.println(offer);
        }
        return offers;
    }

    protected boolean isAccepted(Collection<Offer> offers, double alpha) {
        double votes = 0;
        long number = pM.getPeople().size();
        for(Person p: pM.getPeople()) {
            if (p.getStrategy().vote(offers, pM.getPeople(),  p.getId())) votes++;
        }
        double percentage = votes / number;
        return (percentage > alpha);
    }

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

    protected void mulAccept(Boolean isAccepted, Collection<Offer> offers) {
        Collection<Person> people = pM.getPeople();
        peopleCount[step] += people.size();
        if (!isAccepted) {
            incrementCounter += people.size();
        } else {
            for(Offer o: offers) {
                Person p  = PersonManager.getPersonById(o.getPersonId(), people);
                p.setMoney(p.getMoney() * Math.pow(Math.E, o.getAmount()));
                totalIncrement += o.getAmount();
                incrementCounter++;
            }
        }
    }

    protected abstract void results();
    protected abstract void analysis();
    protected abstract void postprocessing();
    
}
