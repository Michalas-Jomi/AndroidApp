package me.jomi.androidapp.model;

public class User {

    private float energy;
    private int money;
    private Location location;
    private Clothes clothes;


    public User() {
    }
    public User(float energy, int money, Location location, Clothes clothes) {
        this.energy = energy;
        this.money = money;
        this.location = location;
        this.clothes = clothes;
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
}
