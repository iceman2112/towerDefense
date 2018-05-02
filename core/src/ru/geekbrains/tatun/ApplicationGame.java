package ru.geekbrains.tatun;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.geekbrains.tatun.screen.ScreenManager;

public class ApplicationGame extends Game {
	private SpriteBatch batch;

	@Override
	public void create() {
		batch = new SpriteBatch();
		ScreenManager.getInstance().init(this, batch);
		ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
	}

	@Override
	public void render() {
		float dt = Gdx.graphics.getDeltaTime();
		getScreen().render(dt);
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

//	private SpriteBatch batch;
//	private map map;
//	private TextureAtlas atlas;
//
//	private MonsterEmitter monsterEmitter;
//	private TurretEmitter turretEmitter;
//
//	private float timer;
//	private int lastRoute;
//	private Spawner spawner;
//
//	@Override
//	public void create () {
//		batch = new SpriteBatch();
//		atlas = new TextureAtlas(Gdx.files.internal("game.pack"));
//		map = new map(atlas);
//
//		monsterEmitter = new MonsterEmitter(atlas, map, 60);
//		spawner = new Spawner("scenario");
//		turretEmitter = new TurretEmitter(this, atlas, map);
//	}
//
//	public MonsterEmitter getMonsterEmitter() {
//		return monsterEmitter;
//	}
//
//	@Override
//	public void render () {
//		float dt = Gdx.graphics.getDeltaTime();
//		update(dt);
//		Gdx.gl.glClearColor(0, 0, 0, 1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		batch.begin();
//
//		map.render(batch);
//
//		monsterEmitter.render(batch);
//		turretEmitter.render(batch);
//
//		batch.end();
//	}
//
//	public void update(float dt) {
//		timer += dt;
//
//		SpawnerItem[] spawnerItems = spawner.getItems();
//		for (int i = 0; i < spawnerItems.length; i++) {
//			if (!spawnerItems[i].isActive && timer >= spawnerItems[i].time) {
//				monsterEmitter.createMonsters(spawnerItems[i].count, spawnerItems[i].route);
//				spawnerItems[i].isActive = true;
//			}
//		}
//
//		monsterEmitter.update(dt);
//
//		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
//			Vector2 mapPosition = map.convertMouseToMap(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
//			int x = (int) mapPosition.x;
//			int y = (int) mapPosition.y;
//
//			if (map.isGrass(x, y)) {
//				turretEmitter.tryAddTurret(mapPosition);
//			}
//		} else if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
//			Vector2 mapPosition = map.convertMouseToMap(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
//
//			turretEmitter.tryDeleteTurret(mapPosition);
//		}
//
//		turretEmitter.update(dt);
//	}
//
//	@Override
//	public void dispose () {
//		batch.dispose();
//	}
}
