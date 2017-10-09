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
public class GroupPlusAltruisticStrategy implements Strategy {
    
    protected final GroupManager gM;
    private final double threshold;
    private final Collection<Long> acceptedGroupIds = new ArrayList<>();
    private final Collection<Long> declinedGroupIds = new ArrayList<>();
    private int votes = 0;
    private int maxVotesCount;
    private double groupPercentage;
    private double altrPercentage;

    public GroupPlusAltruisticStrategy(GroupManager gM) {
        this.gM = gM;
        this.threshold = 0;        
    }
    
    public GroupPlusAltruisticStrategy(GroupManager gM, double threshold, double groupPercentage) {
        this.gM = gM;
        this.threshold = threshold;   
        this.groupPercentage = groupPercentage;
        this.altrPercentage = 1 - groupPercentage;
    }

    @Override
    public boolean vote(Collection<Offer> offers, Collection<Person> people, long personId) {
        
        Long groupId = gM.getGroupsByPersonId(personId).iterator().next();
        //System.out.println(votes);
        if (acceptedGroupIds.contains(groupId)) {
            votes++;
            return true;
        } else if (declinedGroupIds.contains(groupId)) {
            votes++;
            return false;
        }
        Double socialAvg = Analyzer.getAverageOffer(offers);
        Double groupAvg;
        try {
            groupAvg = Analyzer.getAverageOffer(offers, gM.getMembersByGroupId(groupId));
            boolean isAccepted = (groupAvg * groupPercentage + socialAvg * altrPercentage) > threshold;
            if (isAccepted) {
                acceptedGroupIds.add(groupId);
            } else {
                declinedGroupIds.add(groupId);
            }
            votes++;
            return isAccepted;
        } catch (Exception ex) {
            Logger.getLogger(GroupPlusAltruisticStrategy.class.getName()).log(Level.SEVERE, null, ex);
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
