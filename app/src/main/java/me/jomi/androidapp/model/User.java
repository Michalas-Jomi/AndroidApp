package me.jomi.androidapp.model;

import java.util.Date;

public class User {

    private float energy;
    private int money;
    private Location location;
    private Clothes clothes;
    private String lastLogin;


    public User() {
    }
    public User(float energy, int money, Location location, Clothes clothes, String lastLogin) {
        this.energy = energy;
        this.money = money;
        this.location = location;
        this.clothes = clothes;
        this.lastLogin = lastLogin;
    }

    public float getEnergy() {
        return energy;
    }

    public int getMoney() {
        return money;
    }

    public Location getLocation() {
        return location;
    }

    public Clothes getClothes() {
        return clothes;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }
}
