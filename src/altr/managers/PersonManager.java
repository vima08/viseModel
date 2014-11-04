/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package altr.managers;

import altr.entity.Person;
import java.util.ArrayList;
import java.util.Collection;
import static altr.Environment.people;

/**
 *
 * @author Vitaly
 */
public class PersonManager {
    //private Collection<Person> people;    

    public static Collection<Person> getPeople() {
        return people;
    }

    public static void setPeople(Collection<Person> _people) {
        people = _people;
    }
        
    public static void reset() {
        for(Person p: people) {
            p.setMoney(p.getInitialMoney());
            p.setActive(true);
        }
    }
    
    public static Collection<Person> clonePerson(Person person, long number) throws CloneNotSupportedException {        
        Collection<Person> people = new ArrayList();
        
        for(int i = 0; i < number; i++){
            people.add((Person) person.clone());
        }        
        
        return people;
    }

    public static void init() {
        people = new ArrayList();
    }  
    
}
