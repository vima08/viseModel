package altr.strategies;

import altr.entity.Offer;
import altr.entity.Person;
import altr.managers.Analyzer;
import altr.managers.GroupManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vitaly
 */
public class CompinedStrategy implements Strategy {

    protected final GroupManager gM;
    private final double threshold;
    private final Collection<Long> acceptedGroupIds = new ArrayList<>();
    private final Collection<Long> declinedGroupIds = new ArrayList<>();
    private int votes = 0;
    private int maxVotesCount;
    private double groupPercentage;
    private double altrPercentage;
    private double egoPercentage;

    public CompinedStrategy(GroupManager gM, Person person) {
        this.gM = gM;
        this.threshold = 0;
        this.egoPercentage = person.getEgo();
        this.altrPercentage = person.getAltr();
        this.groupPercentage = person.getGroup();
    }

    public CompinedStrategy(GroupManager gM, double threshold, Person person) {
        this.gM = gM;
        this.threshold = threshold;   
        this.egoPercentage = person.getEgo();
        this.altrPercentage = person.getAltr();
        this.groupPercentage = person.getGroup();
    }

    @Override
    public boolean vote(Collection<Offer> offers, Collection<Person> people, long personId) {
        
        Long groupId = gM.getGroupsByPersonId(personId).iterator().next();
        //System.out.println(votes);
        Double socialAvg = Analyzer.getAverageOffer(offers);
        Double groupAvg;
        Double pesonalOffer = null;
        try {
            pesonalOffer = Analyzer.getOfferByPersonId(offers, personId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //TODO save average of Group and of Society
        try {
            groupAvg = Analyzer.getAverageOffer(offers, gM.getMembersByGroupId(groupId));
            boolean isAccepted = (groupAvg * groupPercentage + socialAvg * altrPercentage + egoPercentage * pesonalOffer) > threshold;
            votes++;
            return isAccepted;
        } catch (Exception ex) {
            System.out.println("Error");
            Logger.getLogger(CompinedStrategy.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public void reset() {
        votes = 0;
        acceptedGroupIds.clear();
        declinedGroupIds.clear();
    }
    
    public void init(){        
        maxVotesCount = gM.getMemberships().size();        
    }
}
