package me.jomi.androidapp.model;

import java.util.List;

public class Clothes {
    private List<Integer> bought_cap;
    private List<Integer> bought_shirt;
    private List<Integer> bought_dress;
    private List<Integer> bought_pants;


    private int cap;
    private int shirt;
    private int dress;
    private int pants;

    public Clothes() {
    }
    public Clothes(int pants, int dress) {
        this.pants = pants;
        this.dress = dress;
    }
    public Clothes(List<Integer> bought_cap, List<Integer> bought_shirt, List<Integer> bought_dress, List<Integer> bought_pants, int cap, int shirt, int dress, int pants) {
        this.bought_cap = bought_cap;
        this.bought_shirt = bought_shirt;
        this.bought_pants = bought_pants;
        this.bought_dress = bought_dress;
        this.pants = pants;
        this.dress = dress;
        this.cap = cap;
        this.shirt = shirt;
    }

    public int getCap() {
        return cap;
    }
    public int getShirt() {
        return shirt;
    }
    public int getDress() {
        return dress;
    }
    public int getPants() {
        return pants;
    }

    public List<Integer> getBought_cap() {
        return bought_cap;
    }
    public List<Integer> getBought_shirt() {
        return bought_shirt;
    }
    public List<Integer> getBought_dress() {
        return bought_dress;
    }
    public List<Integer> getBought_pants() {
        return bought_pants;
    }
}
