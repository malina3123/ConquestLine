package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import java.util.Random;

public class Unit {
    private Texture texture;
    private Vector2 position;
    private int moveRange;
    private int owner;
    private int health;
    private int defense;
    private int attack; // Добавим атрибут атаки
    private BitmapFont font;

    public Unit(Texture texture, float x, float y, int moveRange, int owner) {
        this.texture = texture;
        position = new Vector2(x, y);
        this.moveRange = moveRange;
        this.owner = owner;

        Random random = new Random();
        health = 100;
        defense = random.nextInt(41) + 10;
        attack =  20;

        font = new BitmapFont();
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
        font.draw(batch, "HP: " + health + " DEF: " + defense, position.x, position.y + texture.getHeight() + 15);
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

    public void attack(Unit target) {
        int damage = attack - (target.getDefense() * attack / 100);
        damage = Math.max(1, damage); // Урон не может быть меньше 1
        target.receiveDamage(damage);
    }

    public void attack(Building building) {
        int damage = attack - (building.getDefense() * attack / 100);
        damage = Math.max(1, damage); // Урон не может быть меньше 1
        building.receiveDamage(damage, owner); // Передаем владельца атакующего
    }

    public void receiveDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            health = 0;
            die();
        }

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

    public int getHealth() {
        return health;
    }

    public int getDefense() {
        return defense;
    }

    public int getAttack() {
        return attack;
    }


    private void die() {
        // Можно добавить дополнительные действия при смерти юнита, если нужно
        System.out.println("Unit has died");
    }

    public void dispose() {
        texture.dispose();
        font.dispose();
    }

}