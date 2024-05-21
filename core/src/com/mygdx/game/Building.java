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
    private int owner; // 0 - нейтральное, 1 - игрок 1, 2 - игрок 2
    private int defense; // Защита здания
    private int health; // Здоровье здания
    private int maxHealth; // Максимальное здоровье здания
    private BitmapFont font;

    public Building(String neutralTexturePath, String player1TexturePath, String player2TexturePath, String player1UnitTexturePath, String player2UnitTexturePath, float x, float y) {
        neutralTexture = new Texture(neutralTexturePath);
        player1Texture = new Texture(player1TexturePath);
        player2Texture = new Texture(player2TexturePath);
        player1UnitTexture = new Texture(player1UnitTexturePath);
        player2UnitTexture = new Texture(player2UnitTexturePath);
        texture = neutralTexture;
        position = new Vector2(x, y);
        owner = 0; // По умолчанию нейтральное

        // Инициализация случайного значения защиты и установка здоровья на 200
        Random random = new Random();
        defense = random.nextInt(81) + 10; // Диапазон от 10 до 90
        maxHealth = 200; // Максимальное здоровье по умолчанию
        health = maxHealth; // Здоровье по умолчанию

        font = new BitmapFont(); // Инициализация шрифта для отображения текста
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
        // Отображение значения здоровья и защиты над зданием
        font.draw(batch, "HP: " + health + " DEF: " + defense, position.x, position.y + texture.getHeight() + 15);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Unit hireUnit(int owner) {
        if (this.owner == owner) {
            Texture unitTexture = (owner == 1) ? player1UnitTexture : player2UnitTexture;
            return new Unit(unitTexture, position.x, position.y, 3 * 32, owner);
        }
        return null;
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
        health = maxHealth; // Обновляем здоровье до максимального при смене владельца
    }

    public void receiveDamage(int damage, int attackerOwner) {
        health -= damage;
        if (health <= 0) {
            health = 0;
            setOwner(attackerOwner); // Здание меняет владельца на атакующего игрока
        }
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
        font.dispose(); // Освобождение ресурса шрифта
    }
}
