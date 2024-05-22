package com.mygdx.game;

import java.util.List;

public class GameState {
    private int currentPlayer;
    private Economy economy;
    private List<Building> buildings;


    public GameState(Economy economy, List<Building> buildings) {
        this.economy = economy;
        this.buildings = buildings;
        currentPlayer = 1;
    }


    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void endTurn() {
        economy.endTurn(currentPlayer, buildings);
        currentPlayer = 3 - currentPlayer; // Переключаемся между 1 и 2
    }
}
