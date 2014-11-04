/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package altr.managers;

import altr.distributions.Distribution;
import altr.entity.Offer;
import altr.entity.Person;
import altr.experiment.Experiment;
import altr.experiment.Stage;
import java.util.Collection;

/**
 *
 * @author Vitaly
 */
public abstract class ExperimentManager {
    
   // public PersonManager pM = new PersonManager();
   // public GroupManager gM;
    public Experiment experiment;
    int acceptanceCounter;
    int step;
    int stepNumber;
    int iterationNumber;
    
    public ExperimentManager(Experiment experiment) {        
        this.experiment = experiment;
        //this.gM = groupManager;  
        PersonManager.init();
        this.iterationNumber = experiment.getIterationNumber();
        stepNumber = experiment.getStepNumber();
    }
    
    public void carryOut(){
        init();
        for(int j = 0; j < experiment.getIterationNumber(); j++){
            for(Stage stage: experiment.getStages()) {
                for(int i =0; i < stage.getStepNum(); i++) {                    
                    //System.out.println("Step " + step + ": ");
                    //System.out.println(pM.getPeople());
                    Collection<Offer> offers = generateOffers(stage.getDestribution(), PersonManager.getPeople());
                    boolean accepted = isAccepted(offers);                    
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
        PersonManager.reset();
        step = 0;
        acceptanceCounter = 0;
    }
    
    protected void init() {
        step = 0;
        acceptanceCounter = 0;
    }
    
    protected abstract Collection<Offer> generateOffers(Distribution dist, Collection<Person> people);
    protected abstract boolean isAccepted(Collection<Offer> offers);
    protected abstract void accept(Boolean isAccepted, Collection<Offer> offers);
    protected abstract void results();
    protected abstract void analysis();
    protected abstract void postprocessing();
    
}
