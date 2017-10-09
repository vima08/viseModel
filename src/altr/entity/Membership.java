package altr.entity;

/**
 *
 * @author Vitaly
 */
public class Membership {
    private long personId;
    private long groupId;

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }     

    public Membership(long personId, long groupId) {
        this.personId = personId;
        this.groupId = groupId;
    }
     
}
