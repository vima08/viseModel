package altr.managers;

import altr.entity.Person;
import java.util.ArrayList;
import java.util.Collection;
import altr.Environment;

/**
 *
 * @author Vitaly
 */
public class PersonManager {
    private final Environment env;

    public PersonManager(Environment env) {
        this.env = env;
    }

    public Collection<Person> getPeople() {
        return env.getPeople();
    }

    public void setPeople(Collection<Person> people) {
        env.setPeople(people);
    }
        
    public void reset() {
        for(Person p: getPeople()) {
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
    
    public static Person getPersonById(long id, Collection<Person> people) {
        for(Person person: people) {
            if (id == person.getId()) {
                return person;
            }
        }
        return null;
    }    
}
