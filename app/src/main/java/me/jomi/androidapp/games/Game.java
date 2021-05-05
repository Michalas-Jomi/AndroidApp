package me.jomi.androidapp.games;

//TODO Games

import me.jomi.androidapp.api.Api;

public enum Game {
    TIC_TAC_TOE   (1, .45f, .8f),
    FLAPPY_BIRDS  (2, .25f, .75f),
    BUBBLE_SHOOTER(5, .13f, .5f),
    PIANO         (6, .2f, .4f),
    HIDDEN_OBJECTS(6, .37f, .15f),
    SUDOKU        (8, .525f, .3f),
    HERO_RUSH     (10, .8f, .2f);


    public final float x;
    public final float y;
    public final float energy;

    /**
     * Konstruktor
     *
     * @param energy potrzebna energia
     * @param x przesunięcie w bok .5f = środek, 0f = lewo, 1f = prawo
     * @param y przesunięcie w górę .5f = środek, 0f = góra, 1f = dół
     */
    Game(float energy, float x, float y) {
        this.energy = energy;
        this.x = x;
        this.y = y;
    }

    public boolean isUnlocked() {
        float energy = Api.getUser().child("energy").get().getResult().getValue(float.class);
        return energy >= this.energy;
    }

    public void start() {
        System.out.println("Startowanie gry: " + this);

    }
}
