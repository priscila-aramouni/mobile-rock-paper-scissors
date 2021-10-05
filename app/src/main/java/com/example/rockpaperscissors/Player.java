package com.example.rockpaperscissors;

public class Player {
    private String name;
    private String action;
    private int score;

    public Player(String name, String action, int score) {
        this.name = name;
        this.action = action;
        this.score = score;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void wins(){
        this.score++;
    }

    public void loses() { this.score--; }
}
