package model;

import ui.ApplicationFrame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Floormap {
    public static final int WIDTH = 20;
    public static final int HEIGHT = 20;
    private final int[][] map;

    public Floormap(BufferedImage source) {
        map = new int[WIDTH][HEIGHT];
        makeFloormap(source);
    }

    private void makeFloormap(BufferedImage source) {
        assert source.getWidth() == WIDTH;
        assert source.getHeight() == HEIGHT;

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                map[y][x] = (source.getRGB(x, y)  == Color.BLACK.getRGB()) ? 1 : 0;
            }
        }
    }

    public int getCell(int r, int c) {
        return map[r][c];
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Arrays.stream(map).forEach(row -> {
            Arrays.stream(row).forEach(builder::append);
            builder.append("\n");
        });
        return builder.toString();
    }
}
