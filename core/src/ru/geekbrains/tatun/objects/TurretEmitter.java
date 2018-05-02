package ru.geekbrains.tatun.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.tatun.map.TowerDefenceGame;
import ru.geekbrains.tatun.map.UserInfo;
import ru.geekbrains.tatun.map.Map;

import java.util.ArrayList;
import java.util.List;

public class TurretEmitter {
    public static final int TOWER_COST = 100;
    public static final int TOWER_UPG = 50;

    private List<Turret> turrets;
    private TowerDefenceGame game;

    public TurretEmitter(TowerDefenceGame game) {
        Map map = game.getMap();

        this.game = game;
        this.turrets = new ArrayList<Turret>();
    }

    public void setTurret(Vector2 mapPosition) {
        UserInfo userInfo = game.getUserInfo();
        if (!userInfo.isMoneyEnough(TOWER_COST))
            return;

        boolean isExist = false;
        Turret newTurret = new Turret(game, mapPosition);
        for (int i = turrets.size() - 1; i >= 0; i--) {
            if (turrets.get(i).equals(newTurret)) {
                isExist = true;
            }
        }

        if (!isExist) {
            turrets.add(new Turret(game, mapPosition));
            userInfo.decreaseMoney(TOWER_COST);
        }
    }

    public void deleteTurret(Vector2 mapPosition) {
        Turret newTurret = new Turret(game, mapPosition);
        for (int i = turrets.size() - 1; i >= 0; i--) {
            if (turrets.get(i).equals(newTurret)) {
                turrets.remove(i);
            }
        }
    }


    public void upgradeTurret(Vector2 mapPosition) {
        UserInfo userInfo = game.getUserInfo();
        if (!userInfo.isMoneyEnough(TOWER_COST))
            return;

        for (int i = turrets.size() - 1; i >= 0; i--) {
            if (mapPosition.equals(turrets.get(i).getPosition())) {
                turrets.get(i).upgrade();
                userInfo.decreaseMoney(TOWER_UPG);
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < turrets.size(); i++)
            turrets.get(i).render(batch);
    }

    public void update(float dt) {
        for (int i = 0; i < turrets.size(); i++)
            turrets.get(i).update(dt);
    }
}
