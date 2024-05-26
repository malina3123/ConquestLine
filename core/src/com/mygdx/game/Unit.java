package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import java.util.Random;

public class Unit {
    private int rangeAtack;
    private Texture texture;
    private Vector2 position;
    private int moveRange;
    private int owner;
    private int health;
    private int defense;
    private int attack;
    private BitmapFont font;
    private boolean step;

    public Unit(Texture texture, float x, float y, int moveRange, int owner, int rangeAtack) {
        this.texture = texture;
        position = new Vector2(x, y);
        this.moveRange = moveRange;
        this.owner = owner;
        this.rangeAtack = rangeAtack;

        Random random = new Random();
        health = 100;
        defense = random.nextInt(41) + 10;
        attack =  20;
        step = Boolean.FALSE;
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

    public boolean contains(float x, float y) {
        float unitX = position.x;
        float unitY = position.y;
        float width = texture.getWidth();
        float height = texture.getHeight();
        return x >= unitX && x <= unitX + width && y >= unitY && y <= unitY + height;
    }

    public boolean isOnSamePosition(Unit other) {
        return this.position.epsilonEquals(other.getPosition(), 0.1f) && this.owner == other.getOwner();
    }

    public boolean isWithinAttackRange(Vector2 targetPosition) {
        float distance = position.dst(targetPosition);
        int tileSize = 32;
        int attackRange = rangeAtack * tileSize;

        return distance <= attackRange;
    }

    public boolean isWithinRange(Vector2 targetPosition, int range) {
        float distance = position.dst(targetPosition);
        return distance <= range;
    }

    public void attack(Unit target) {
        if (isWithinAttackRange(target.getPosition())) {
            int damage = attack - (target.getDefense() * attack / 100);
            damage = Math.max(1, damage);
            target.receiveDamage(damage);
        }
    }

    public void attack(Building building) {
        if (isWithinAttackRange(building.getPosition())) {
            int damage = attack - (building.getDefense() * attack / 100);
            damage = Math.max(1, damage);
            building.receiveDamage(damage, owner);
        }
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
        System.out.println("Unit has died");
    }

    public void dispose() {
        texture.dispose();
        font.dispose();
    }

    public boolean isStep() {
        return step;
    }

    public void setStep(boolean step) {
        this.step = step;
    }
}
