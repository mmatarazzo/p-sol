package com.example.mmataraz.game.model;

import java.util.ArrayList;

/**
 * Created by Michael on 1/3/2017.
 */

public class Event {

    private boolean asteroids, rings;   // maybe scripted later
    private int startTime;
    private ArrayList<Enemy> enemies;
    private ArrayList<String> dialoge;
    private ArrayList<Item> items;

    private boolean completed;

    public Event(int startTime) {
        this.startTime = startTime;
    }

}
