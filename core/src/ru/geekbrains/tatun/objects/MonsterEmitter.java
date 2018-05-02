package ru.geekbrains.tatun.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.geekbrains.tatun.map.*;
import ru.geekbrains.tatun.map.TowerDefenceGame;
import ru.geekbrains.tatun.util.LineReader;
import ru.geekbrains.tatun.map.Wave;

import java.util.List;

public class MonsterEmitter{
    private Monster[] monsters;
    private Wave[] waves;
    private int currentWave;
    private float spawnTimer;
    private final TowerDefenceGame game;

    public Monster[] getMonsters() {
        return monsters;
    }

    public MonsterEmitter(TowerDefenceGame game, int maxSize) {
        this.game = game;
        this.monsters = new Monster[maxSize];
        for (int i = 0; i < monsters.length; i++) {
            this.monsters[i] = new Monster(game, game.getMap().getRoutes().get(0));
        }
        loadScenario("scenario");
    }

    private void loadScenario(String filename) {
        LineReader lineReader = new LineReader(filename);
        List<String> lines = lineReader.read();

        waves = new Wave[lines.size() - 1];
        for (int i = 1; i < lines.size(); i++) {
            waves[i - 1] = new Wave(lines.get(i));
        }
    }

    public void createMonster(Route route) {
        for (int i = 0; i < monsters.length; i++) {
            if (!monsters[i].isActive()) {
                monsters[i].activate(route);
                break;
            }
        }
    }

    public void createMonsters(int count, Route route) {
        for (int j = 0; j <= count; j++) {
            createMonster(route);
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < monsters.length; i++) {
            if (monsters[i].isActive()) {
                monsters[i].render(batch);
            }
        }
    }

    public void update(float dt) {
        if (currentWave < waves.length) {
            spawnTimer += dt;
            Wave wave = waves[currentWave];
            if (spawnTimer >= wave.time) {
                createMonsters(wave.count, game.getMap().getRoutes().get(wave.route));
                currentWave++;
            }
        }

        for (int i = 0; i < monsters.length; i++) {
            if (monsters[i].isActive()) {
                monsters[i].update(dt);
            }
        }
    }
}
