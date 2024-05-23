package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import java.util.Iterator;
import java.util.List;

public class InputHandler extends InputAdapter {
    private OrthographicCamera camera;
    private Map map;
    private List<Unit> units;
    private Unit selectedUnit;
    private MyGdxGame game;
    private GameState gameState;
    private Economy economy;


    public InputHandler(OrthographicCamera camera, Map map, List<Unit> units, MyGdxGame game, GameState gameState, Economy economy) {
        this.camera = camera;
        this.map = map;
        this.units = units;
        this.game = game;
        this.gameState = gameState;
        this.economy = economy;
        Gdx.input.setInputProcessor(this);
    }

    public void updateMapAndUnits(Map newMap, List<Unit> newUnits) {
        this.map = newMap;
        this.units = newUnits;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPos3D = new Vector3(screenX, screenY, 0);
        units = map.getUnits();
        List<Building> buildings = map.getBuildings();
        camera.unproject(touchPos3D);
        Vector2 touchPos = new Vector2(touchPos3D.x, touchPos3D.y);

        if (selectedUnit == null) {
            // Проверка нажатия на юнита текущего игрока
            for (Unit unit : units) {
                if (unit.contains(touchPos.x, touchPos.y) && unit.getOwner() == gameState.getCurrentPlayer()) {
                    selectedUnit = unit;
                    game.setHighlightedTiles(map.getMovableTiles(unit));
                    return true;
                }
            }
            // Проверка нажатия на здание для найма юнита
            for (Building building : map.getBuildings()) {
                if (building.contains(touchPos.x, touchPos.y)) {
                    if (!map.isCellOccupied(building.getPosition())) {
                        int currentPlayer = gameState.getCurrentPlayer();
                        if (building.getOwner() == currentPlayer) {
                            if (economy.spendResources(currentPlayer, building.getUnitCost())) {
                                Unit newUnit = building.hireUnit(currentPlayer);
                                if (newUnit != null) {
                                    units.add(newUnit);
                                }
                            } else {
                                System.out.println("Недостаточно ресурсов для найма юнита!");
                            }
                        }
                    }
                    return true;
                }
            }
        } else {
            // Проверка нажатия на вражеского юнита для атаки
            for (Unit unit : units) {
                if (unit.contains(touchPos.x, touchPos.y) && unit.getOwner() != gameState.getCurrentPlayer()) {
                    if (selectedUnit.isWithinAttackRange(unit.getPosition())) {
                        selectedUnit.attack(unit);
                        removeDeadUnits();
                        selectedUnit = null;
                        game.clearHighlightedTiles();
                        gameState.endTurn();
                    } else {
                        System.out.println("Вне радиуса атаки!");
                    }
                    return true;
                }
            }
            // Проверка нажатия на здание для атаки
            for (Building building : map.getBuildings()) {
                if (building.contains(touchPos.x, touchPos.y) && building.getOwner() != gameState.getCurrentPlayer()) {
                    if (selectedUnit.isWithinAttackRange(building.getPosition())) {
                        selectedUnit.attack(building);
                        if (building.getHealth() <= 0) {
                            building.setOwner(selectedUnit.getOwner());
                        }
                        selectedUnit = null;
                        game.clearHighlightedTiles();
                        gameState.endTurn();
                    } else {
                        System.out.println("Вне радиуса атаки!");
                    }
                    return true;
                }
            }
            // Перемещение выбранного юнита и захват здания
            if (selectedUnit.canMoveTo(touchPos.x, touchPos.y, map) && !map.isCellOccupied(touchPos)) {
                // Проверка на наличие союзного юнита в целевой клетке
                for (Unit unit : units) {
                    if (unit.contains(touchPos.x, touchPos.y) && unit.getOwner() == gameState.getCurrentPlayer()) {
                        return false; // Если целевая клетка занята союзным юнитом, просто не реагируем
                    }
                }

                int tileSize = map.getTileSize();
                float targetX = (float) Math.floor(touchPos.x / tileSize) * tileSize;
                float targetY = (float) Math.floor(touchPos.y / tileSize) * tileSize;
                selectedUnit.moveTo(targetX, targetY);

                for (Building building : map.getBuildings()) {
                    if (building.getPosition().dst(selectedUnit.getPosition()) < 16) {
                        building.setOwner(selectedUnit.getOwner());
                    }
                }

                selectedUnit = null;
                game.clearHighlightedTiles();
                gameState.endTurn();
            } else {
                selectedUnit = null;
                game.clearHighlightedTiles();
            }
        }
        return false;
    }





    private void removeDeadUnits() {
        Iterator<Unit> iterator = units.iterator();
        while (iterator.hasNext()) {
            Unit unit = iterator.next();
            if (unit.getHealth() <= 0) {
                iterator.remove();
            }
        }
    }

    public Unit getSelectedUnit() {
        return selectedUnit;
    }

    public void setSelectedUnit(Unit selectedUnit) {
        this.selectedUnit = selectedUnit;
    }
}
