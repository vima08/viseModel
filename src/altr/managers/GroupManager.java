/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package altr.managers;

import altr.entity.Group;
import altr.entity.Membership;
import altr.entity.Person;
import java.util.ArrayList;
import java.util.Collection;
import static altr.Environment.*;

/**
 *
 * @author Vitaly
 */
public class GroupManager {
    //private Collection<Group> groups; 
    //private Collection<Membership> memberships;

    public static Collection<Group> getGroups() {
        return groups;
    }

    public static void setGroups(Collection<Group> _groups) {
        groups = _groups;
    }

    public Collection<Membership> getMemberships() {
        return memberships;
    }

    public static void setMemberships(Collection<Membership> _memberships) {
        memberships = _memberships;
    }   
    
    public static void addPeopleToGroup(Collection<Person> people, long groupId) {
        for(Person p: people) {
            memberships.add(new Membership(p.getId(), groupId));
        }        
    }   
    
    public static void addGroup(Group g) {
        groups.add(g);
    }
     
    public static Collection<Long> getMembersByGroupId(long groupId){
        if (groups == null || memberships == null) return null;
        
        Collection<Long> members = new ArrayList();
        for(Membership m: memberships) {
            if (m.getGroupId() == groupId) members.add(m.getPersonId());
        }
        
        return members;
    }
    
    public static Collection<Long> getGroupsByPersonId(long personId){
        if (groups == null || memberships == null) return null;
        
        Collection<Long> personGroups = new ArrayList();
        for(Membership m: memberships) {
            if (m.getPersonId() == personId) personGroups.add(m.getGroupId());
        }
        
        return personGroups;
    }   
    
    public static void clear() {
        groups = new ArrayList<>();
        memberships = new ArrayList<>();
    }

    public static void init() {
        groups = new ArrayList<>();
        memberships = new ArrayList<>();
    }    
    
}
