package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class ReloadGame extends Game {
    private Stage stage;
    private Skin skin;
    @Override
    public void create() {
        stage = new Stage(new ScreenViewport());
        try {
            skin = new Skin(Gdx.files.internal("uiskin.json"), new TextureAtlas(Gdx.files.internal("uiskin.atlas")));
        } catch (Exception e) {
            Gdx.app.error("SkinError", "Error loading skin: " + e.getMessage(), e);
        }
        Dialog dialog = new Dialog("Пример Dialog", skin){
            protected void result(Object o){
                System.out.println(o);
            }
        };
        dialog.button("Кнопка 1", true);
        dialog.button("Отмена", false);
        dialog.show(stage);
    }


    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
