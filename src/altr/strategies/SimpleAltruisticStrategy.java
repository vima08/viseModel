/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package altr.strategies;

import altr.entity.Offer;
import altr.entity.Person;
import java.util.Collection;

/**
 *
 * @author Vitaly
 */
public class SimpleAltruisticStrategy implements Strategy {
    
    private final double leftBound;
    private final double rightBound;
    
    public SimpleAltruisticStrategy(double lb, double rb) {
        this.leftBound = lb;
        this.rightBound = rb;
    }

    @Override
    public boolean vote(Collection<Offer> offers, Collection<Person> people, long personId) {
        for(Offer offer: offers) {
            if (offer.getPersonId() == personId) {
                return (offer.getAmmount() > leftBound);
            }
        }
        return false;
    }    
}
