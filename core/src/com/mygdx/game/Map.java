package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.HashSet;
import java.util.Set;

public class Map {
    private Tile[][] tiles;
    private List<Building> buildings;
    private List<Unit> units;
    private int width;
    private int height;
    private int tileSize = 32;
    private String grassTexturePath = "grass.png";
    private String forestTexturePath = "forest.png";

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        tiles = new Tile[width][height];
        buildings = new ArrayList<>();
        units = new ArrayList<>();
        generateMap();
    }

    private boolean isPositionValid(Vector2 position, Set<Vector2> occupiedPositions) {
        int[][] directions = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},         {0, 1},
                {1, -1}, {1, 0}, {1, 1}
        };

        if (occupiedPositions.contains(position)) {
            return false;
        }

        for (int[] dir : directions) {
            Vector2 neighborPosition = new Vector2(position.x + dir[0] * tileSize, position.y + dir[1] * tileSize);
            if (occupiedPositions.contains(neighborPosition)) {
                return false;
            }
        }

        return true;
    }

    private void generateMap() {
        Random random = new Random();


        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                String texturePath = random.nextFloat() < 0.4f ? forestTexturePath : grassTexturePath; // 20% вероятность леса
                tiles[x][y] = new Tile(texturePath, x * tileSize, y * tileSize);
            }
        }


        buildings.add(new Building("neutral_building.png", "player1_building.png", "player2_building.png", "player1_unit.png", "player2_unit.png", 0 * tileSize, 0 * tileSize));
        buildings.get(0).setOwner(1);
        buildings.add(new Building("neutral_building.png", "player1_building.png", "player2_building.png", "player1_unit.png", "player2_unit.png", (width - 1) * tileSize, (height - 1) * tileSize));
        buildings.get(1).setOwner(2);


        int neutralBuildingCount = 8;
        Set<Vector2> occupiedPositions = new HashSet<>();
        occupiedPositions.add(new Vector2(0, 0));
        occupiedPositions.add(new Vector2((width - 1) * tileSize, (height - 1) * tileSize));

        for (int i = 0; i < neutralBuildingCount; i++) {
            boolean positionFound = false;
            while (!positionFound) {
                int x = random.nextInt(width);
                int y = random.nextInt(height);
                Vector2 candidatePosition = new Vector2(x * tileSize, y * tileSize);

                if (isPositionValid(candidatePosition, occupiedPositions)) {
                    buildings.add(new Building("neutral_building.png", "player1_building.png", "player2_building.png", "player1_unit.png", "player2_unit.png", x * tileSize, y * tileSize));
                    occupiedPositions.add(candidatePosition);
                    positionFound = true;
                }
            }
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
        int tileSize = getTileSize();
        float centerX = (float) Math.floor(position.x / tileSize) * tileSize + tileSize / 2;
        float centerY = (float) Math.floor(position.y / tileSize) * tileSize + tileSize / 2;

        for (Unit unit : units) {
            Vector2 unitPos = unit.getPosition();
            if (Math.floor(unitPos.x / tileSize) * tileSize + tileSize / 2 == centerX &&
                    Math.floor(unitPos.y / tileSize) * tileSize + tileSize / 2 == centerY) {
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

    public void clearMap() {
        dispose();
        tiles = new Tile[width][height];
        buildings.clear();
        units.clear();
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
