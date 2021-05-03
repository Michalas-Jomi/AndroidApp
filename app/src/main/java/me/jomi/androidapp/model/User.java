package me.jomi.androidapp.model;

public class User {

    private float energy;
    private Location location;

    public User(float energy, Location location) {
        this.energy = energy;
        this.location = location;

    }

    public float getEnergy() {
        return energy;
    }

    public Location getLocation() {
        return location;
    }
}
