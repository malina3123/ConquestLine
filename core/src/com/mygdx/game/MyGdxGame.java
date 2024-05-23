package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.List;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	SpriteBatch textBatch;
	Map map;
	List<Unit> units;
	Unit selectedUnit;
	OrthographicCamera camera;
	Stage stage;
	Skin skin;
	TextButton generateButton;
	TextButton endTurnButton;
	InputHandler inputHandler;
	ShapeRenderer shapeRenderer;
	List<Vector2> highlightedTiles;
	GameState gameState;
	private Economy economy;
	private BitmapFont font;
	private MusicManager musicManager;

	@Override
	public void create() {
		batch = new SpriteBatch();
		textBatch = new SpriteBatch();
		economy = new Economy();
		map = new Map(20, 15);
		units = map.getUnits();
		selectedUnit = null;
		shapeRenderer = new ShapeRenderer();
		highlightedTiles = new ArrayList<>();
		gameState = new GameState(economy, map.getBuildings());

		font = new BitmapFont();
		font.getData().setScale(1.5f);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth() / 1.5f, Gdx.graphics.getHeight() / 1.5f);
		centerCamera();

		stage = new Stage(new ScreenViewport());

		try {
			skin = new Skin(Gdx.files.internal("uiskin.json"), new TextureAtlas(Gdx.files.internal("uiskin.atlas")));
		} catch (Exception e) {
			Gdx.app.error("SkinError", "Error loading skin: " + e.getMessage(), e);
		}

		generateButton = new TextButton("Создать новую карту", skin);
		generateButton.setPosition(Gdx.graphics.getWidth() / 2f - generateButton.getWidth() / 2f, Gdx.graphics.getHeight() - generateButton.getHeight() - 10);
		generateButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				createNewMap();
			}
		});

		endTurnButton = new TextButton("Закончить ход", skin);
		endTurnButton.setPosition(Gdx.graphics.getWidth() / 2f - endTurnButton.getWidth() / 2f, Gdx.graphics.getHeight() - endTurnButton.getHeight() - 70);
		endTurnButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				endTurn();
			}
		});

		stage.addActor(generateButton);
		stage.addActor(endTurnButton);

		inputHandler = new InputHandler(camera, map, units, this, gameState, economy);
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, inputHandler));

		initializeGame();

		String[] musicFiles = {"music1.mp3", "music2.mp3", "music3.mp3"};
		musicManager = new MusicManager(musicFiles);
	}

	private void initializeGame() {
		gameState.setCurrentPlayer(1); // Начинает игрок 1
	}

	private void createNewMap() {
		map = new Map(20, 15);
		units = map.getUnits(); // Обновление списка юнитов
		gameState.setBuildings(map.getBuildings());
		inputHandler.updateMapAndUnits(map, units); // Обновление карты и юнитов в обработчике ввода

		// Сброс ресурсов игроков
		economy.resetResources();

		centerCamera();
		initializeGame(); // Инициализация начального игрока при создании новой карты
	}

	private void endTurn() {
		gameState.endTurn();
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

		String player1Info = "Player 1, Resources: " + economy.getResources(1);
		String player2Info = "Player 2, Resources: " + economy.getResources(2);
		String currentPlayerInfo = "Now is Player " + gameState.getCurrentPlayer() + " turn!";

		float x = 10;
		float y1 = Gdx.graphics.getHeight() - 10;
		float y2 = Gdx.graphics.getHeight() - 30;
		float y3 = Gdx.graphics.getHeight() - 50;

		textBatch.begin();
		font.draw(textBatch, player1Info, x, y1);
		font.draw(textBatch, player2Info, x, y2);
		font.draw(textBatch, currentPlayerInfo, x, y3);
		textBatch.end();

		renderHighlightedTiles();

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	private void renderHighlightedTiles() {
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(1, 1, 0, 1);

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
		centerCamera(); // Центрирование камеры при изменении размера окна
		stage.getViewport().update(width, height, true);
		generateButton.setPosition(width / 2f - generateButton.getWidth() / 2f, height - generateButton.getHeight() - 10);
		endTurnButton.setPosition(width / 2f - endTurnButton.getWidth() / 2f, height - endTurnButton.getHeight() - 70);
	}

	@Override
	public void dispose() {
		batch.dispose();
		textBatch.dispose();
		map.dispose();
		for (Unit unit : units) {
			unit.dispose();
		}
		shapeRenderer.dispose();
		stage.dispose();
		skin.dispose();
		musicManager.dispose();
	}
}
