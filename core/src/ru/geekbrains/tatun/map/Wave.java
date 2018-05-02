package ru.geekbrains.tatun.map;

public class Wave {
    public final int time;
    public final int count;
    public final int route;

    public Wave(String data) {
        String[] arr = data.split(" ");
        this.time = Integer.parseInt(arr[0]);
        this.route = Integer.parseInt(arr[1]);
        this.count = Integer.parseInt(arr[2]);
    }
}