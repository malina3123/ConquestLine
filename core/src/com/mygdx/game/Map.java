package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Map {
    private Tile[][] tiles;
    private List<Building> buildings;
    private int width;
    private int height;
    private int tileSize = 32;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        tiles = new Tile[width][height];
        buildings = new ArrayList<>();
        generateMap();
    }

    private void generateMap() {
        Random random = new Random();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = new Tile("grass.png", x * tileSize, y * tileSize);
            }
        }

        // Генерация зданий
        for (int i = 0; i < 5; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            buildings.add(new Building("building.png", x * tileSize, y * tileSize));
        }
    }

    public void render(SpriteBatch batch) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y].render(batch);
            }
        }

        for (Building building : buildings) {
            building.render(batch);
        }
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTileSize() {
        return tileSize;
    }

    public void dispose() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y].dispose();
            }
        }

        for (Building building : buildings) {
            building.dispose();
        }
    }
}