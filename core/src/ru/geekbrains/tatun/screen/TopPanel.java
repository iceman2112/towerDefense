package ru.geekbrains.tatun.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import ru.geekbrains.tatun.map.TowerDefenceGame;
import ru.geekbrains.tatun.map.UserInfo;
import ru.geekbrains.tatun.map.Map;
import ru.geekbrains.tatun.util.Assets;

public class TopPanel {
    public TowerDefenceGame game;
    private Label moneyLabel;
    private Label hpLabel;


    private TextureRegion cursorTexture;

    private Vector2 selectedPosition;
    private Vector2 mousePosition;

    private String action = "";

    private void addUserLabel(Group menuGroup) {
        BitmapFont font36 = Assets.getInstance().getAssetManager().get("zorque32.ttf", BitmapFont.class);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font36;

        hpLabel = new Label("", labelStyle);
        moneyLabel = new Label("", labelStyle);

        hpLabel.setPosition(160, 30);
        moneyLabel.setPosition(400, 30);

        Image panelImage = new Image(Assets.getInstance().getAtlas().findRegion("upperPanel"));

        menuGroup.addActor(panelImage);
        menuGroup.addActor(hpLabel);
        menuGroup.addActor(moneyLabel);

    }

    private void setAction(String action) {
        if (action == "") {
            this.action = action;
        }
        else if (action == this.action) {
            this.action = "";
        } else {
            this.action = action;
        }
    }
    private void addActionButtons(Group menuGroup) {
        BitmapFont font20 = Assets.getInstance().getAssetManager().get("zorque20.ttf", BitmapFont.class);

        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());
        skin.add("font24", font20);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("smallButton");
        textButtonStyle.font = font20;

        skin.add("simpleSkin", textButtonStyle);

        Button btnSetTurret = new TextButton("Set", skin, "simpleSkin");
        Button btnUpgradeTurret = new TextButton("Upg", skin, "simpleSkin");
        Button btnDestroyTurret = new TextButton("Dst", skin, "simpleSkin");
        Button btnExit = new TextButton("Exit", skin, "simpleSkin");
        btnSetTurret.setPosition(640, 10);
        btnUpgradeTurret.setPosition(740, 10);
        btnDestroyTurret.setPosition(840, 10);
        btnExit.setPosition(940, 10);

        menuGroup.addActor(btnSetTurret);
        menuGroup.addActor(btnUpgradeTurret);
        menuGroup.addActor(btnDestroyTurret);
        menuGroup.addActor(btnExit);

        btnSetTurret.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setAction("ADD");
            }
        });

        btnExit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
            }
        });

        btnDestroyTurret.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setAction("DELETE");
            }
        });

        btnUpgradeTurret.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setAction("UPGRADE");
            }
        });

    }

    public TopPanel(final TowerDefenceGame game, final Stage stage) {
        this.game = game;

        TextureAtlas atlas = Assets.getInstance().getAtlas();
        cursorTexture = atlas.findRegion("cursor");

        mousePosition = new Vector2(0, 0);
        selectedPosition = new Vector2(0, 0);

        Group menuGroup = new Group();
        menuGroup.setPosition(0, 720 - 60);

        addUserLabel(menuGroup);

        final InputProcessor myProc = new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                selectedPosition = Map.convertDisplayToMap(mousePosition);
                if ("ADD".equals(action)) {
                    game.getTurretEmitter().setTurret(selectedPosition);
                } else if ("DELETE".equals(action)) {
                    game.getTurretEmitter().deleteTurret(selectedPosition);
                } else if ("UPGRADE".equals(action)) {
                    game.getTurretEmitter().upgradeTurret(selectedPosition);

                }
                return true;
            }
        };

        InputMultiplexer im = new InputMultiplexer(stage, myProc);
        Gdx.input.setInputProcessor(im);

        addActionButtons(menuGroup);

        stage.addActor(menuGroup);
    }

    public void render(SpriteBatch batch) {
        if (action != "") {
            batch.setColor(1, 1, 0, 0.5f);
            batch.draw(cursorTexture, mousePosition.x, mousePosition.y);
            batch.setColor(1, 1, 1, 1);
        }
    }

    public void update(float dt) {
        mousePosition.set(Gdx.input.getX(), Gdx.input.getY());
        ScreenManager.getInstance().getViewport().unproject(mousePosition);
        mousePosition = Map.convertMapToDisplay(Map.convertDisplayToMap(mousePosition));

        UserInfo userInfo = game.getUserInfo();

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.setLength(0);
        stringBuffer.append(userInfo.getHp()).append(" / ").append(userInfo.getHpMax());
        hpLabel.setText(stringBuffer);

        stringBuffer.setLength(0);
        stringBuffer.append(userInfo.getMoney());
        moneyLabel.setText(stringBuffer);
    }
}
