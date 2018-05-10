package ru.geekbrains.tatun.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.tatun.map.UserInfo;
import ru.geekbrains.tatun.objects.ParticleEmitter;
import ru.geekbrains.tatun.map.Map;
import ru.geekbrains.tatun.objects.MonsterEmitter;
import ru.geekbrains.tatun.objects.TurretEmitter;
import ru.geekbrains.tatun.util.Assets;

public class TowerDefenceGame {
    private UserInfo userInfo;
    private Map map;
    private TurretEmitter turretEmitter;

    private MonsterEmitter monsterEmitter;
    private ParticleEmitter particleEmitter;

    public ParticleEmitter getParticleEmitter() {
        return particleEmitter;
    }

    public TurretEmitter getTurretEmitter() {
        return turretEmitter;
    }

    public MonsterEmitter getMonsterEmitter() {
        return monsterEmitter;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public TowerDefenceGame() {
        this.userInfo = new UserInfo(350, 32);
        this.map = new Map(this);
        this.monsterEmitter = new MonsterEmitter(this, 40);
        this.turretEmitter = new TurretEmitter(this);
        this.particleEmitter = new ParticleEmitter();
    }

    public Map getMap() { return map; }

    public void render(SpriteBatch batch) {
        map.render(batch);
        monsterEmitter.render(batch);
        turretEmitter.render(batch);
        particleEmitter.render(batch);
    }

    public void update(float dt) {
        monsterEmitter.update(dt);
        turretEmitter.update(dt);
        map.update(dt);
        particleEmitter.update(dt);
        particleEmitter.checkPool();
    }
}
