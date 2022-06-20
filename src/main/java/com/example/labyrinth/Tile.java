package com.example.labyrinth;

public class Tile {
    int xCord;
    int yCord;
    int tileStatus;

    public Tile(int xCord, int yCord, int tileStatus) {
        this.xCord = xCord;
        this.yCord = yCord;
        this.tileStatus = tileStatus;
    }

    public int getxCord() {
        return xCord;
    }

    public void setxCord(int xCord) {
        this.xCord = xCord;
    }

    public int getyCord() {
        return yCord;
    }

    public void setyCord(int yCord) {
        this.yCord = yCord;
    }

    public int getTileStatus() {
        return tileStatus;
    }

    public void setTileStatus(int tileStatus) {
        this.tileStatus = tileStatus;
    }
}
