package me.jomi.androidapp.games;

//TODO Games

import me.jomi.androidapp.api.Api;

public enum Game {
    TIC_TAC_TOE   (1),
    FLAPPY_BIRDS  (2),
    BUBBLE_SHOOTER(5),
    PIANO         (6),
    HIDDEN_OBJECTS(6),
    SUDOKU        (8),
    HERO_RUSH     (10);


    public final float energy;
    Game(float energy) {
        this.energy = energy;
    }

    public boolean isUnlocked() {
        float energy = Api.getUser().child("energy").get().getResult().getValue(float.class);
        return energy >= this.energy;
    }

    public void start() {
        System.out.println("Startowanie gry: " + this);

    }
}
