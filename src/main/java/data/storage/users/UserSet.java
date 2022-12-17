package data.storage.users;

import net.dv8tion.jda.api.entities.User;

import java.io.Serializable;
import java.time.LocalDateTime;

public class UserSet implements Serializable {

    private final static long serialVersionUID = -8600563805485753103L;
    private final String userId;
    private String name;
    private String discriminator;
    private int balance;
    LocalDateTime stealProtection;
    LocalDateTime gainLimiter;


    public UserSet(User user) {
        this.userId = user.getId();
        this.name = user.getName();
        this.discriminator = user.getDiscriminator();
        this.balance = 0;
        this.stealProtection = LocalDateTime.now();
        this.gainLimiter = LocalDateTime.now();
    }

    //Getters
    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public int getBalance() {
        return balance;
    }

    public LocalDateTime getStealProtection() {
        return stealProtection;
    }


    //Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    public void setStealProtection() {
        this.stealProtection = LocalDateTime.now().plusHours(1);
    }

    public void setGainlimiter() {
        this.gainLimiter = LocalDateTime.now().plusMinutes(2);
    }


    //Methods
    public void addBalance(int value, boolean isLimited) {
        if (isLimited && LocalDateTime.now().isBefore(this.gainLimiter)) return;
        if (isLimited) setGainlimiter();
        this.balance = this.balance + value;
    }
}
