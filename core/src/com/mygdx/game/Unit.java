package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Unit {
    private Texture texture;
    private Vector2 position;
    private int moveRange;

    public Unit(String texturePath, float x, float y, int moveRange) {
        texture = new Texture(texturePath);
        position = new Vector2(x, y);
        this.moveRange = moveRange;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public void moveTo(float x, float y) {
        position.set(x, y);
    }

    public boolean canMoveTo(float x, float y) {
        return position.dst(x, y) <= moveRange;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void dispose() {
        texture.dispose();
    }
}
