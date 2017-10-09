package altr.strategies;

import altr.entity.Offer;
import altr.entity.Person;
import java.util.Collection;

/**
 *
 * @author Vitaly
 */
public interface Strategy {
    
    public boolean vote(Collection<Offer> offers, Collection<Person> people, long personId);
}
