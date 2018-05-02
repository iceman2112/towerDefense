package ru.geekbrains.tatun.map;

import com.badlogic.gdx.math.Vector2;

public class Route {
    public static final char LEFT = 'L';
    public static final char RIGHT = 'R';
    public static final char UP = 'U';
    public static final char DOWN = 'D';

    private Vector2 start;

    private Vector2[] directions;

    public Vector2[] getDirections() {
        return directions;
    }

    public Vector2 getStart() {
        return start;
    }

    public void setDirection(int i, char direction) {
        switch (direction) {
            case LEFT:
                directions[i] = new Vector2(-1, 0);
                break;
            case RIGHT:
                directions[i] = new Vector2(1, 0);
                break;
            case UP:
                directions[i] = new Vector2(0, 1);
                break;
            case DOWN:
                directions[i] = new Vector2(0, -1);
                break;
        }
    }

    public Route(String str) {
        String[] data = str.split(",");
        start = new Vector2(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
        directions = new Vector2[data[2].length()];
        for (int i = 0; i < data[2].length(); i++) {
            setDirection(i, data[2].charAt(i));
        }
    }
}