package ru.geekbrains.tatun.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import ru.geekbrains.tatun.map.TowerDefenceGame;

public class GameScreen implements Screen {
    private SpriteBatch batch;

    private Stage stage;

    private TowerDefenceGame game;
    private TopPanel topPanel;

    private Vector2 mousePosition;
    private Vector2 selectedPosition;

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {
        game = new TowerDefenceGame();

        Gdx.input.setInputProcessor(null);

        createGUI();
    }

    public void createGUI() {
        stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        topPanel = new TopPanel(game, stage);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        game.render(batch);
        topPanel.render(batch);

        batch.end();

        stage.draw();

    }

    public void update(float dt) {
        game.update(dt);
        topPanel.update(dt);
        stage.act(dt);
    }

    @Override
    public void resize(int width, int height) {
        ScreenManager.getInstance().resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    }
}
