package altr.strategies;

import altr.entity.Offer;
import altr.entity.Person;
import altr.managers.Analyzer;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Vitaly
 */
public class SimpleAltruisticStrategy implements Strategy {
    
    private final double leftBound;
    private final double rightBound;

    private final double threshold;
    private Boolean result = null;

    public SimpleAltruisticStrategy(double leftBound, double rightBound, double threshold) {
        this.threshold = threshold;
        this.leftBound = leftBound;
        this.rightBound = rightBound;
    }

    public SimpleAltruisticStrategy(double threshold) {
        this.threshold = threshold;
        this.leftBound = 0;
        this.rightBound = 100;
    }

    public SimpleAltruisticStrategy() {
        this.threshold = 0;
        this.leftBound = 0;
        this.rightBound = 100;
    }

    @Override
    public boolean vote(Collection<Offer> offers, Collection<Person> people, long personId) {
        if (result == null) {
            if (leftBound != 0 || rightBound != 100) {
                int peopleCount = people.size();
                int left = (int)Math.round((peopleCount * leftBound)/100);
                int right = (int)Math.floor((peopleCount * rightBound)/100);
                List<Person> sortedPeople = Analyzer.sort(people);
                List<Long> personIds = Analyzer.getQuotaPersonIds(sortedPeople, left, right);
                try {
                    Double socialAvg = Analyzer.getAverageOffer(offers, personIds);
                    result = (socialAvg > threshold);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Double socialAvg = Analyzer.getAverageOffer(offers);
                result = (socialAvg > threshold);
            }
        }
        return result;
    }    

    public void reset() {
        result = null;
    }
}
