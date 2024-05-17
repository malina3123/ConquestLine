package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Map {
    private Tile[][] tiles;
    private List<Building> buildings;
    private List<Unit> units;
    private int width;
    private int height;
    private int tileSize = 32;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        tiles = new Tile[width][height];
        buildings = new ArrayList<>();
        units = new ArrayList<>();
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
        buildings.add(new Building("neutral_building.png", "player1_building.png", "player2_building.png", "player1_unit.png", "player2_unit.png", 0 * tileSize, 0 * tileSize));
        buildings.get(0).setOwner(1);
        buildings.add(new Building("neutral_building.png", "player1_building.png", "player2_building.png", "player1_unit.png", "player2_unit.png", (width - 1) * tileSize, (height - 1) * tileSize));
        buildings.get(1).setOwner(2);

        for (int i = 2; i < 5; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            buildings.add(new Building("neutral_building.png", "player1_building.png", "player2_building.png", "player1_unit.png", "player2_unit.png", x * tileSize, y * tileSize));
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

    public List<Unit> getUnits() {
        return units;
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

    public boolean isCellOccupied(Vector2 position) {
        for (Unit unit : units) {
            if (unit.getPosition().equals(position)) {
                return true;
            }
        }
        return false;
    }

    public List<Vector2> getMovableTiles(Unit unit) {
        List<Vector2> tiles = new ArrayList<>();
        Vector2 position = unit.getPosition();
        int range = unit.getMoveRange() / tileSize;
        for (int x = (int) (position.x / tileSize) - range; x <= (int) (position.x / tileSize) + range; x++) {
            for (int y = (int) (position.y / tileSize) - range; y <= (int) (position.y / tileSize) + range; y++) {
                Vector2 tile = new Vector2(x * tileSize, y * tileSize);
                if (x >= 0 && x < width && y >= 0 && y < height && !isCellOccupied(tile)) {
                    tiles.add(tile);
                }
            }
        }
        return tiles;
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
