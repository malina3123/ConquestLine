package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import java.util.Random;

public class Building {
    private Texture texture;
    private Texture neutralTexture;
    private Texture player1Texture;
    private Texture player2Texture;
    private Vector2 position;
    private Texture player1UnitTexture;
    private Texture player2UnitTexture;
    private int owner;
    private int defense;
    private int health;
    private int maxHealth;
    private BitmapFont font;
    private int unitCost = 200;

    public Building(String neutralTexturePath, String player1TexturePath, String player2TexturePath, String player1UnitTexturePath, String player2UnitTexturePath, float x, float y) {
        neutralTexture = new Texture(neutralTexturePath);
        player1Texture = new Texture(player1TexturePath);
        player2Texture = new Texture(player2TexturePath);
        player1UnitTexture = new Texture(player1UnitTexturePath);
        player2UnitTexture = new Texture(player2UnitTexturePath);
        texture = neutralTexture;
        position = new Vector2(x, y);
        owner = 0; // По умолчанию нейтральное


        Random random = new Random();
        defense = random.nextInt(10) + 10;
        maxHealth = 150;
        health = maxHealth;

        font = new BitmapFont();
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
        font.draw(batch, "HP: " + health + " DEF: " + defense, position.x, position.y + texture.getHeight() + 15);
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean contains(float x, float y) {
        float buildingX = position.x;
        float buildingY = position.y;
        float width = texture.getWidth();
        float height = texture.getHeight();
        return x >= buildingX && x <= buildingX + width && y >= buildingY && y <= buildingY + height;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
        switch (owner) {
            case 1:
                texture = player1Texture;
                break;
            case 2:
                texture = player2Texture;
                break;
            default:
                texture = neutralTexture;
                break;
        }
    }

    public void receiveDamage(int damage, int attackerOwner) {
        health -= damage;
        if (health <= 0) {
            health = 0;
            setOwner(attackerOwner);
            health = maxHealth;
        }
    }

    public int getUnitCost() {
        return unitCost;
    }

    public int getDefense() {
        return defense;
    }

    public int getHealth() {
        return health;
    }

    public void dispose() {
        neutralTexture.dispose();
        player1Texture.dispose();
        player2Texture.dispose();
        player1UnitTexture.dispose();
        player2UnitTexture.dispose();
        font.dispose();
    }
}
