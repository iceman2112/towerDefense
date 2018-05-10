package ru.geekbrains.tatun.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.tatun.util.Assets;
import ru.geekbrains.tatun.util.LineReader;

import java.util.ArrayList;
import java.util.List;

public class Map {
    public static final int GRASS_CODE = 0;
    public static final int ROAD_CODE = 1;
    public static final int CROOSS_CODE = 2;
    public static final int HOUSE_CODE = 3;

    public static Vector2 convertDisplayToMap(Vector2 position) {
        return new Vector2((int)position.x / 80, (int)((640 - position.y) / 80));
    }

    public static Vector2 convertMapToDisplay(Vector2 position) {
        return new Vector2(position.x * 80, 640 - position.y * 80);
    }

    public static Vector2 convertMouseToMap(Vector2 position) {
        return new Vector2((int)position.x / 80, (int)((position.y) / 80));
    }

    private int width;
    private int height;

    private Vector2 housePosition;

    private ArrayList<Vector2>[][] crossroads;
    private List<Route> routes;

    private TextureRegion textureGrass;
    private TextureRegion textureRoad;
    private TextureRegion textureHouse;

    private TowerDefenceGame game;

    private byte[][] data;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public Map(TowerDefenceGame game) {
        TextureAtlas atlas = Assets.getInstance().getAtlas();
        textureGrass = atlas.findRegion("grass");
        textureRoad = atlas.findRegion("road");
        textureHouse = atlas.findRegion("house");

        this.game = game;

        loadMap("map");
    }

    public void addNewCrossRoad(int x, int y, Vector2 direction) {
        if (crossroads[x][y] == null)
            crossroads[x][y] = new ArrayList<Vector2>();
        crossroads[x][y].add(direction);
    }

    private void loadMap(String mapName) {
        LineReader lineReader = new LineReader("map");
        List<String> lines = lineReader.read();

        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).equals("routes")) {
                height = i;
                break;
            }
        }

        width = lines.get(0).split(",").length;
        data = new byte[width][height];
        for (int i = 0; i < height; i++) {
            String[] arr = lines.get(i).split(",");
            for (int j = 0; j < width; j++) {
                data[j][i] = Byte.parseByte(arr[j]);
            }
        }

        routes = new ArrayList<Route>();
        for (int i = height + 1; i < lines.size(); i++) {
            routes.add(new Route(lines.get(i)));
        }
    }

    public boolean isCrossroad(int x, int y) {
        return (data[x][y] == CROOSS_CODE) || (data[x][y] == HOUSE_CODE);
    }

    public boolean isHouse(int x, int y) {
        return (data[x][y] == HOUSE_CODE);
    }

    public boolean isGrass(int x, int y) {
        return data[x][y] == GRASS_CODE;
    }

    private void drawPoint(SpriteBatch batch, int i, int j) {
        Vector2 displayPosition = convertMapToDisplay(new Vector2(i, j));
        switch (data[i][j]) {
            case GRASS_CODE:
                batch.draw(textureGrass, displayPosition.x, displayPosition.y);
                break;
            case ROAD_CODE:
                batch.draw(textureRoad, displayPosition.x, displayPosition.y);
                break;
            case CROOSS_CODE:
                batch.draw(textureRoad, displayPosition.x, displayPosition.y);
                break;
            case HOUSE_CODE:
                batch.draw(textureRoad, displayPosition.x, displayPosition.y);
                batch.draw(textureHouse, displayPosition.x, displayPosition.y);
                housePosition = new Vector2(i, j);
                break;
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                drawPoint(batch, i, j);
            }
        }
    }

    public void update(float dt) {
        if (housePosition != null) {
            Vector2 fromPosition = Map.convertMapToDisplay(new Vector2(housePosition.x, housePosition.y - 1));
            Vector2 toPosition = Map.convertMapToDisplay(new Vector2(housePosition.x, housePosition.y - 2));

            game.getParticleEmitter().setupByTwoPoints(fromPosition.x + 20, fromPosition.y, toPosition.x + 20 + MathUtils.random(-10, 10), toPosition.y, 2, 1.2f, 1.5f, 0.5F, 0.5F, 0.5F, 0.5F, 1, 1, 1, 1);
        }
    }
}
