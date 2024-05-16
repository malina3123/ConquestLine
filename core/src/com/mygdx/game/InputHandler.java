package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import java.util.List;

public class InputHandler extends InputAdapter {
    private OrthographicCamera camera;
    private Map map;
    private List<Unit> units;
    private Unit selectedUnit;
    private MyGdxGame game;

    public InputHandler(OrthographicCamera camera, Map map, List<Unit> units, MyGdxGame game) {
        this.camera = camera;
        this.map = map;
        this.units = units;
        this.game = game;
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 touchPos3D = new Vector3(screenX, screenY, 0);
        camera.unproject(touchPos3D);
        Vector2 touchPos = new Vector2(touchPos3D.x, touchPos3D.y);

        if (selectedUnit == null) {
            // Проверка нажатия на юнита
            for (Unit unit : units) {
                if (unit.getPosition().dst(touchPos) < 16) {
                    selectedUnit = unit;
                    game.setHighlightedTiles(map.getMovableTiles(unit));
                    return true;
                }
            }
            // Проверка нажатия на здание для найма юнита
            for (Building building : map.getBuildings()) {
                if (building.getPosition().dst(touchPos) < 32) {
                    if (!map.isCellOccupied(building.getPosition())) {
                        units.add(building.hireUnit());
                    }
                    return true;
                }
            }
        } else {
            // Перемещение выбранного юнита
            if (selectedUnit.canMoveTo(touchPos.x, touchPos.y, map) && !map.isCellOccupied(touchPos)) {
                selectedUnit.moveTo(touchPos.x, touchPos.y);
                selectedUnit = null;
                game.clearHighlightedTiles();
            } else {
                selectedUnit = null;
                game.clearHighlightedTiles();
            }
        }
        return false;
    }

    public Unit getSelectedUnit() {
        return selectedUnit;
    }

    public void setSelectedUnit(Unit selectedUnit) {
        this.selectedUnit = selectedUnit;
    }
}
