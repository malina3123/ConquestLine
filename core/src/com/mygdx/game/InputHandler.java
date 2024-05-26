package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import javax.swing.text.StyledEditorKit;
import java.util.Iterator;
import java.util.List;

public class InputHandler extends InputAdapter {
    private final OrthographicCamera camera;
    private Map map;
    private static List<Unit> units;
    private Unit selectedUnit;
    private final MyGdxGame game;
    private final GameState gameState;
    private final Economy economy;

    public InputHandler(OrthographicCamera camera, Map map, List<Unit> units, MyGdxGame game, GameState gameState, Economy economy) {
        this.camera = camera;
        this.map = map;
        InputHandler.units = units;
        this.game = game;
        this.gameState = gameState;
        this.economy = economy;
        Gdx.input.setInputProcessor(this);
    }

    public void updateMapAndUnits(Map newMap, List<Unit> newUnits) {
        this.map = newMap;
        units = newUnits;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPos3D = new Vector3(screenX, screenY, 0);
        units = map.getUnits();
        List<Building> buildings = map.getBuildings();
        camera.unproject(touchPos3D);
        Vector2 touchPos = new Vector2(touchPos3D.x, touchPos3D.y);
        System.out.println("______________________________________");
        for (Unit unit:units){
            if (unit.getOwner() == gameState.getCurrentPlayer()){
                System.out.println(unit.isStep());
            }
        }
        if (selectedUnit == null) {
            for (Unit unit : units) {
                if (unit.contains(touchPos.x, touchPos.y) && unit.getOwner() == gameState.getCurrentPlayer() && !unit.isStep()) {
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
                            game.setShopping(Boolean.TRUE);
                            game.setPosition(building.getPosition());
                        }
                    }
                    return true;
                }
            }
        } else {
            for (Unit unit : units) {
                if (unit.contains(touchPos.x, touchPos.y) && unit.getOwner() != gameState.getCurrentPlayer()) {
                    if (selectedUnit.isWithinAttackRange(unit.getPosition())) {
                        selectedUnit.attack(unit);
                        removeDeadUnits();
                        selectedUnit.setStep(Boolean.TRUE);
                        selectedUnit = null;
                        game.clearHighlightedTiles();
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
                        if (BuldingIs()){
                            VictoryNotification victoryNotification = new VictoryNotification(gameState.getCurrentPlayer());
                            victoryNotification.run();
                            game.Destroy();
                        }
                        selectedUnit.setStep(Boolean.TRUE);
                        selectedUnit = null;
                        game.clearHighlightedTiles();
                    } else {
                        System.out.println("Вне радиуса атаки!");
                    }
                    return true;
                }
            }
            if (selectedUnit.canMoveTo(touchPos.x, touchPos.y, map) && !map.isCellOccupied(touchPos)) {
                for (Unit unit : units) {
                    if (unit.contains(touchPos.x, touchPos.y) && unit.getOwner() == gameState.getCurrentPlayer()) {
                        return false;
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
                selectedUnit.setStep(Boolean.TRUE);
                selectedUnit = null;
                game.clearHighlightedTiles();
            } else {
                selectedUnit = null;
                game.clearHighlightedTiles();
            }
        }

        if (allStep()){
            for (Unit unit:units){
                if (unit.getOwner() == gameState.getCurrentPlayer()){
                    unit.setStep(Boolean.FALSE);
                }
            }
            gameState.endTurn();
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

    private Boolean IsFinished(){
        return Boolean.FALSE;
    }

    public Unit getSelectedUnit() {
        return selectedUnit;
    }

    public void setSelectedUnit(Unit selectedUnit) {
        this.selectedUnit = selectedUnit;
    }

    public void addUnit(Unit unit){
        units.add(unit);
    }
    public GameState getGameState(){
        return this.gameState;
    }
    public Boolean allStep(){
        Boolean step = Boolean.FALSE;
        for (Unit unit: units){
            if (unit.getOwner() == gameState.getCurrentPlayer()){
                if (unit.isStep()){
                    step = Boolean.TRUE;
                }else {
                    return Boolean.FALSE;
                }
            }
        }
        return step;
    }

    public Boolean BuldingIs(){
        Boolean buildingB = Boolean.TRUE;
        for (Building building: map.getBuildings()){
            if (building.getOwner() == gameState.getCurrentPlayer()){
                buildingB = Boolean.TRUE;
            }else if (building.getOwner() != 0){
                return Boolean.FALSE;
            }
        }
        return buildingB;
    }

    static public void updateSteps(){
        for (Unit unit: units){
            unit.setStep(Boolean.FALSE);
        }
    }
}
