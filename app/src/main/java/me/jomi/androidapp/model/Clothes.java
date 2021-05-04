package me.jomi.androidapp.model;

public class Clothes {
    private int pants;
    private int dress;

    public Clothes() {
    }
    public Clothes(int pants, int dress) {
        this.pants = pants;
        this.dress = dress;
    }

    public int getDress() {
        return dress;
    }
    public int getPants() {
        return pants;
    }
}
