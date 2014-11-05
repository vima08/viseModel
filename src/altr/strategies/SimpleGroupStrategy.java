/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package altr.strategies;

import altr.entity.Offer;
import altr.entity.Person;
import altr.managers.Analyzer;
import altr.managers.GroupManager;
import static altr.managers.GroupManager.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vitaly
 */
public class SimpleGroupStrategy implements Strategy {
    
    protected final GroupManager gM;
    private final double threshold;
//    private static Collection<Long> acceptedGroupIds = new ArrayList<>();
//    private static Collection<Long> declinedGroupIds = new ArrayList<>();
//    private int maxVotesCount;
//    private static int votes = 0;
    
    public SimpleGroupStrategy(GroupManager gM) {
        this.gM = gM;
        this.threshold = 0;        
    }
    
    public SimpleGroupStrategy(GroupManager gM, double threshold) {
        this.gM = gM;
        this.threshold = threshold;        
    }

    @Override
    public boolean vote(Collection<Offer> offers, Collection<Person> people, long personId) {
        Long groupId = gM.getGroupsByPersonId(personId).iterator().next();
        
//        if (votes == maxVotesCount) reset();
//        if (this.acceptedGroupIds.contains(groupId)) {
//            votes++;
//            return true;
//        } else if (this.declinedGroupIds.contains(groupId)) {
//            votes++;
//            return false;
//        }
        Double groupAvg;
        try {
            groupAvg = Analyzer.getAverageOffer(offers, gM.getMembersByGroupId(groupId));
            boolean isAccepted = groupAvg > threshold;
//            if (isAccepted) {
//                this.acceptedGroupIds.add(groupId);
//            } else {       
//                this.declinedGroupIds.add(groupId);
//            }        
//            votes++;
            return isAccepted;
        } catch (Exception ex) {
            Logger.getLogger(SimpleGroupStrategy.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }        
    }
    
//    private static void reset() {
//        votes = 0;
//        acceptedGroupIds = new ArrayList<>();
//        declinedGroupIds = new ArrayList<>();
//    }
//    
//    public void init(){
//        reset();
//        this.maxVotesCount = gM.getMemberships().size();
//        System.out.println(maxVotesCount);
//    }
    
}
