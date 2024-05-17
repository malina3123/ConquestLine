package com.mygdx.game;

public class GameState {
    private int currentPlayer;

    public GameState() {
        currentPlayer = 1;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void endTurn() {
        currentPlayer = 3 - currentPlayer; // Переключаемся между 1 и 2
    }
}
