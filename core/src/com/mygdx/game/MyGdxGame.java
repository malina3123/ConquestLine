package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.ArrayList;
import java.util.List;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Map map;
	List<Unit> units;
	Unit selectedUnit;
	OrthographicCamera camera;
	Stage stage;
	Skin skin;
	TextButton generateButton;
	InputHandler inputHandler;
	ShapeRenderer shapeRenderer;
	List<Vector2> highlightedTiles;
	GameState gameState;
	private Economy economy;


	@Override
	public void create() {
		batch = new SpriteBatch();
		economy = new Economy();
		map = new Map(20, 15); // карта 20x15 тайлов
		units = new ArrayList<>();
		selectedUnit = null;
		shapeRenderer = new ShapeRenderer();
		highlightedTiles = new ArrayList<>();
		gameState = new GameState(economy, map.getBuildings());

		// Инициализация камеры с увеличенным масштабом
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth() / 1.5f, Gdx.graphics.getHeight() / 1.5f);
		centerCamera();

		// Инициализация интерфейса
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);

		// Загрузка скина для кнопки
		try {
			skin = new Skin(Gdx.files.internal("uiskin.json"), new TextureAtlas(Gdx.files.internal("uiskin.atlas")));
		} catch (Exception e) {
			Gdx.app.error("SkinError", "Error loading skin: " + e.getMessage(), e);
		}

		// Создание кнопки генерации карты
		generateButton = new TextButton("Создать новую карту", skin);
		generateButton.setPosition(Gdx.graphics.getWidth() / 2f - generateButton.getWidth() / 2f, Gdx.graphics.getHeight() - generateButton.getHeight() - 10);
		generateButton.addListener(new ClickListener() {
			@Override
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
				map = new Map(20, 15); // Генерация новой карты
				units.clear(); // Очистка списка юнитов
				centerCamera();
			}
		});

		// Добавление кнопки на сцену
		stage.addActor(generateButton);

		// Инициализация обработчика ввода
		inputHandler = new InputHandler(camera, map, units, this, gameState, economy);
		Gdx.input.setInputProcessor(inputHandler);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		map.render(batch);
		for (Unit unit : units) {
			unit.render(batch);
		}
		batch.end();

		// Отображение обводки выбранных клеток
		renderHighlightedTiles();

		// Отображение интерфейса
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	private void renderHighlightedTiles() {
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(1, 1, 0, 1); // Желтый цвет

		for (Vector2 tile : highlightedTiles) {
			if (!map.isCellOccupied(tile)) {
				shapeRenderer.rect(tile.x, tile.y, map.getTileSize(), map.getTileSize());
			}
		}

		shapeRenderer.end();
	}

	private void centerCamera() {
		float mapWidth = map.getWidth() * map.getTileSize();
		float mapHeight = map.getHeight() * map.getTileSize();
		camera.position.set(mapWidth / 2, mapHeight / 2, 0);
		camera.update();
	}

	public void setHighlightedTiles(List<Vector2> tiles) {
		highlightedTiles = tiles;
	}

	public void clearHighlightedTiles() {
		highlightedTiles.clear();
	}

	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(false, width / 1.5f, height / 1.5f);
		centerCamera();
		stage.getViewport().update(width, height, true);
		generateButton.setPosition(width / 2f - generateButton.getWidth() / 2f, height - generateButton.getHeight() - 10);
	}

	@Override
	public void dispose() {
		batch.dispose();
		map.dispose();
		for (Unit unit : units) {
			unit.dispose();
		}
		stage.dispose();
		skin.dispose();
	}
}
