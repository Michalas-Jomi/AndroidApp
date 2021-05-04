package me.jomi.androidapp.model;

import java.util.ArrayList;
import java.util.List;

public class Clothes {
    private List<Integer> bought_pants;
    private List<Integer> bought_dress;

    private int pants;
    private int dress;

    public Clothes() {
    }
    public Clothes(int pants, int dress) {
        this.pants = pants;
        this.dress = dress;
    }
    public Clothes(List<Integer> bought_pants, List<Integer> bought_dress, int pants, int dress) {
        this.bought_pants = bought_pants;
        this.bought_dress = bought_dress;
        this.pants = pants;
        this.dress = dress;
    }

    public int getDress() {
        return dress;
    }
    public int getPants() {
        return pants;
    }

    public List<Integer> getBought_pants() {
        return bought_pants;
    }
    public List<Integer> getBought_dress() {
        return bought_dress;
    }
}
