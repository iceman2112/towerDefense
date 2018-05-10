package ru.geekbrains.tatun.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.tatun.map.TowerDefenceGame;
import ru.geekbrains.tatun.map.*;
import ru.geekbrains.tatun.util.Assets;

public class Turret {
    private TextureRegion[] texture;

    private final TowerDefenceGame game;

    private int range;
    private int damage;
    private int lever;
    private long lastBullet = 0L;
    private float fireTimer;
    private Monster target;
    private final float fireDelay;

    private Vector2 position;

    private float rotation = 0;

    public Turret(TowerDefenceGame game, Vector2 mapPosition) {
        this.position = mapPosition;
        this.game     = game;

        this.range = 100;
        this.damage = 1;
        this.lever = 1;
        this.fireDelay = 1.0F;

        TextureAtlas atlas = Assets.getInstance().getAtlas();

        this.texture  = new TextureRegion(atlas.findRegion("turrets")).split(80, 80)[0];
    }

    public Vector2 getPosition() { return position; }

    public void render(SpriteBatch batch) {
        Vector2 displayPosition = Map.convertMapToDisplay(position);

        //TextureRegion texture = (lever == 1 ? textureL1 : textureL2);
        batch.draw(texture[lever - 1], displayPosition.x, displayPosition.y, 40, 40, 80, 80, 1, 1, rotation);
    }

    public boolean checkMonsterInRange(Monster monster) {
        return monster.getPosition().dst(Map.convertMapToDisplay(position)) <= this.range;
    }

    public void update(float dt) {
        if (target != null && (!checkMonsterInRange(target) || !target.isActive())) {
            target = null;
        }
        
        Monster[] monsters = game.getMonsterEmitter().getMonsters();
        for (int i = 0; i < monsters.length; i++) {
            if (monsters[i].isActive() && checkMonsterInRange(monsters[i])) {
                target = monsters[i];
                break;
            }
        }

        rotation(dt);
        fire(dt);
    }

    private void rotation(float dt) {
        if (target != null) {
            Vector2 direction = target.getPosition().cpy().sub(Map.convertMapToDisplay(position)).nor();
            direction.y = -direction.y;
            rotation = direction.angle(new Vector2(1, 0));
        }
    }

    public void fire(float dt) {
        fireTimer += dt;
        if (target != null && fireTimer >= fireDelay) {
            fireTimer = 0.0f;

            float bulletSpeed = 400.0f;

            Vector2 displayPosition = Map.convertMapToDisplay(position);
            float fromX = displayPosition.x + 40;
            float fromY = displayPosition.y + 40;
            float time = Vector2.dst(fromX, fromY, target.getPosition().x, target.getPosition().y) / bulletSpeed;
            float toX = target.getPosition().x + 40 + target.getVelocity().x * time;
            float toY = target.getPosition().y + 40 + target.getVelocity().y * time;
            game.getParticleEmitter().setupByTwoPoints(fromX, fromY, toX, toY, time, 1.2f, 1.5f, 1, 1, 0, 1, 1, 0, 0, 1);

            target.takeDamage(damage);
        }
    }

    public void upgrade() {
        if (lever < 2) {
            lever++;
            damage *= 2;
            range *= 2;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Turret turret = (Turret) o;
        return position.equals(turret.position);
    }
}
