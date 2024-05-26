package com.mygdx.game;

import java.util.List;

public class GameState {
    private int currentPlayer;
    private Economy economy;
    private List<Building> buildings;

    public GameState(Economy economy, List<Building> buildings) {
        this.economy = economy;
        this.buildings = buildings;
        this.currentPlayer = 1;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }

    public void endTurn() {
        economy.endTurn(currentPlayer, buildings);
        InputHandler.updateSteps();
        currentPlayer = (currentPlayer == 1) ? 2 : 1;

    }
}
