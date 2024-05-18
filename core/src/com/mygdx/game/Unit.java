package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Unit {
    private Texture texture;
    private Vector2 position;
    private int moveRange;
    private int owner; // Владелец юнита (1 или 2)

    public Unit(Texture texture, float x, float y, int moveRange, int owner) {
        this.texture = texture;
        position = new Vector2(x, y);
        this.moveRange = moveRange;
        this.owner = owner;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public void moveTo(float x, float y) {
        int tileSize = 32;
        position.set(x + tileSize / 2 - texture.getWidth() / 2, y + tileSize / 2 - texture.getHeight() / 2);
    }

    public boolean canMoveTo(float x, float y, Map map) {
        int range = moveRange / map.getTileSize();
        int currentX = (int) (position.x / map.getTileSize());
        int currentY = (int) (position.y / map.getTileSize());
        int targetX = (int) (x / map.getTileSize());
        int targetY = (int) (y / map.getTileSize());

        return Math.abs(targetX - currentX) <= range && Math.abs(targetY - currentY) <= range &&
                targetX >= 0 && targetX < map.getWidth() && targetY >= 0 && targetY < map.getHeight();
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getMoveRange() {
        return moveRange;
    }

    public int getOwner() {
        return owner;
    }

    public void dispose() {
        texture.dispose();
    }
}