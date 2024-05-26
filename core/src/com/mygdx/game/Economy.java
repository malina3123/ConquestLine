package com.mygdx.game;

import java.util.List;

public class Economy {
    private int[] playerResources;

    public Economy() {
        playerResources = new int[3];
        playerResources[1] = 1000;
        playerResources[2] = 1000;
    }

    public int getResources(int player) {
        return playerResources[player];
    }

    public void addResources(int player, int amount) {
        playerResources[player] += amount;
    }

    public boolean spendResources(int player, int amount) {
        if (playerResources[player] >= amount) {
            playerResources[player] -= amount;
            return true;
        }
        return false;
    }

    public void setResources(int player, int amount) {
        playerResources[player] = amount;
    }

    public void endTurn(int player, List<Building> buildings) {
        int income = 0;
        for (Building building : buildings) {
            if (building.getOwner() == player) {
                income += 100;
            }
        }
        addResources(player, income);
    }


    public void resetResources() {
        playerResources[1] = 1000;
        playerResources[2] = 1000;
    }
}
