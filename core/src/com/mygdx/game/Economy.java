package com.mygdx.game;

import java.util.List;

public class Economy {
    private int[] playerResources;

    public Economy() {
        playerResources = new int[3]; // Индексы 1 и 2 используются для игроков
        playerResources[1] = 1000; // Начальные ресурсы игрока 1
        playerResources[2] = 1000; // Начальные ресурсы игрока 2
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
                income += 100; // Каждый контролируемый игроком 1 или 2 building добавляет 100 ресурсов за ход
            }
        }
        addResources(player, income);
    }

    // Метод для сброса ресурсов игроков к начальным значениям
    public void resetResources() {
        playerResources[1] = 1000; // Начальные ресурсы игрока 1
        playerResources[2] = 1000; // Начальные ресурсы игрока 2
    }
}
