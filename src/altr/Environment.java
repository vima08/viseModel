package altr;

import altr.entity.Group;
import altr.entity.Membership;
import altr.entity.Person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Vitaly
 */
public class Environment {

    private static AtomicInteger uniqueId = new AtomicInteger();

    private long id;
    private String name;
    private Collection<Group> groups;
    private Collection<Membership> memberships;
    private Collection<Person> people;
    private double alpha;

    public Environment(String name, Double alpha) {
        this.id = uniqueId.getAndIncrement();
        this.name = name;
        this.groups = new ArrayList<>();
        this.memberships = new ArrayList<>();
        this.people = new ArrayList<>();
        if (alpha != null) {
            this.alpha = alpha;
        } else {
            this.alpha = 0.5;
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Group> getGroups() {
        return groups;
    }

    public void setGroups(Collection<Group> groups) {
        this.groups = groups;
    }

    public Collection<Membership> getMemberships() {
        return memberships;
    }

    public void setMemberships(Collection<Membership> memberships) {
        this.memberships = memberships;
    }

    public Collection<Person> getPeople() {
        return people;
    }

    public void setPeople(Collection<Person> people) {
        this.people = people;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }
}
