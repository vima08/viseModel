package altr.entity;

/**
 * @author Vitaly
 */
public class Offer {
    private double amount;
    private long personId;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public Offer(double ammount, long personId) {
        this.amount = ammount;
        this.personId = personId;
    }

    @Override
    public String toString() {
        return "Offer{" + "amount=" + amount + ", personId=" + personId + '}';
    }

}
