package ru.geekbrains.tatun.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.tatun.map.TowerDefenceGame;
import ru.geekbrains.tatun.map.*;
import ru.geekbrains.tatun.util.Assets;

public class Monster {
    private final int hpMax = 3;

    private TextureRegion texture;
    private TextureRegion textureHp;
    private final TowerDefenceGame game;

    private Vector2 position;
    private Vector2 velocity;
    private float speed;
    private Route route;
    private int routeCounter;
    private Vector2 lastPosition;
    private int hp;
    private boolean active;
    private final int gold = 25;

    private void setVelocity(int i) {
        this.velocity = route.getDirections()[i].cpy().scl(speed);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() { return velocity; }

    public boolean isActive() {
        return active;
    }

    public Monster(TowerDefenceGame game, Route route) {
        this.game = game;

        TextureAtlas atlas = Assets.getInstance().getAtlas();

        this.texture = atlas.findRegion("monster");
        this.textureHp = atlas.findRegion("monsterHp");

        this.active = false;
        this.hp = hpMax;

        updateRoute(route);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
        batch.draw(textureHp, position.x - 40, position.y + 60, ((float)hp / hpMax) * 80, 20);
    }

    public boolean takeDamage(int dmg) {
        hp -= dmg;
        if (hp <= 0) {
            active = false;
            return true;
        }
        return false;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);

        Vector2 mapPosition      = Map.convertDisplayToMap(position);
        Vector2 absolutePosition = Map.convertMapToDisplay(mapPosition);

        Map map = game.getMap();
        if (map.isCrossroad((int)mapPosition.x, (int)mapPosition.y) && (absolutePosition.dst(position) < velocity.len() * dt * 2)) {
            if (!lastPosition.equals(mapPosition)) {
                position = absolutePosition;
                routeCounter++;

                lastPosition = mapPosition;

                if (routeCounter > route.getDirections().length - 1) {
                    velocity.set(0, 0);
                    return;
                }

                setVelocity(routeCounter);
            }
        }

        if (map.isHouse((int)mapPosition.x, (int)mapPosition.y) && active) {
            UserInfo userInfo = game.getUserInfo();
            userInfo.decreaseHp(1);
            active = false;
        }
    }

    public void updateRoute(Route route) {
        this.route = route;

        this.position     = Map.convertMapToDisplay(this.route.getStart());
        this.lastPosition = this.route.getStart().cpy();
        this.routeCounter = 0;

        this.speed = MathUtils.random(80.0f, 120.0f);
        setVelocity(this.routeCounter);
    }

    public void activate(Route route) {
        updateRoute(route);
        this.active = true;
    }

}
