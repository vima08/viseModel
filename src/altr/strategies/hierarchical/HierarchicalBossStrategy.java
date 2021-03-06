package altr.strategies.hierarchical;

import altr.entity.Offer;
import altr.entity.Person;
import altr.strategies.Strategy;
import java.util.Collection;

/**
 *
 * @author Vitaly
 */
public class HierarchicalBossStrategy implements Strategy {
    
    private final double alfa;
    
    public HierarchicalBossStrategy() {
        this.alfa = 0;
    }

    public HierarchicalBossStrategy(double alfa) {
        this.alfa = alfa;
    }

    @Override
    public boolean vote(Collection<Offer> offers, Collection<Person> people, long personId) {        
        double s = 0;
        double t = 0;
        for(Offer offer: offers) {
            if (offer.getPersonId() == personId) {
                s = offer.getAmount();
            } else {
                t += offer.getAmount();
            }
        }
        return (alfa * s + (1 - alfa) * t) > 0;
    }    
}
