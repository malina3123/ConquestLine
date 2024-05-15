package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Building {
    private Texture texture;
    private Vector2 position;

    public Building(String texturePath, float x, float y) {
        texture = new Texture(texturePath);
        position = new Vector2(x, y);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Unit hireUnit() {
        return new Unit("unit.png", position.x, position.y, 3 * 32);
    }

    public void dispose() {
        texture.dispose();
    }
}
