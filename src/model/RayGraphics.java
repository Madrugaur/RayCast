package model;

import application.Constants;

import java.awt.*;
import java.util.Arrays;

/*
Make it so that drawing the camera is super easy

 */
public class RayGraphics {
    private final Graphics g;

    public RayGraphics(Graphics g) {
        this.g = g;
    }

    public void drawFloormap(Floormap map) {
        int cellWidth = Constants.CELL_WIDTH;
        for (int r = 0; r < Floormap.HEIGHT; r++) {
            for (int c = 0; c < Floormap.WIDTH; c++) {
                int cellVal = map.getCell(r, c);
                int x = c * cellWidth;
                int y = r * cellWidth;
                Color color = switch (cellVal) {
                    case 1 -> Color.WHITE;
                    case 2 -> Color.RED;
                    default -> Color.BLACK;
                };
                g.setColor(color);
                g.fillRect(x, y, cellWidth, cellWidth);
                g.setColor(Color.GRAY);
                g.drawRect(x, y, cellWidth, cellWidth);
            }
        }
    }

    public void drawCamera(Camera camera, Floormap map) {
        int cellWidth = Constants.CELL_WIDTH;
        int x1 = camera.posX(cellWidth) + Constants.center(cellWidth);
        int y1 = camera.posY(cellWidth) + Constants.center(cellWidth);
        Point origin = new Point(x1, y1);
        Point endpoint = calculateViewRayEndpoint(camera);

        Point gridEndpoint = new Point (Math.floorDiv(endpoint.x - 10, cellWidth), Math.floorDiv(endpoint.y, cellWidth));

        Floormap copy = highlightViewedCells(gridEndpoint, origin, map);
        drawFloormap(copy);
        _drawCamera(camera, origin, endpoint);
    }

    private void _drawCamera(Camera camera, Point origin, Point endpoint) {
        int size = Constants.CELL_WIDTH;
        g.setColor(Color.RED);
        g.fillRect(camera.posX(size), camera.posY(size), size, size);
        g.setColor(Color.RED);
        drawLine(origin, endpoint);
    }


    private Floormap highlightViewedCells(Point endpoint, Point origin, Floormap floormap) {
        final Floormap copy = floormap.copy();
        final double size = Floormap.WIDTH;
        int[] xarr = new int[] {(int) Math.round(origin.x / size), (int) Math.round(endpoint.x / size)};
        int[] yarr = new int[] {(int) Math.round(origin.y / size), (int) Math.round(endpoint.y / size)};
        Arrays.sort(xarr);
        int dx = xarr[0] - xarr[1];
        int dy = yarr[0] - yarr[1];
        int D = 2 * dy - dx;
        int y = yarr[0];

        for (int x = xarr[0]; x < xarr[1]; x++) {
            System.out.printf("x: %f, y: %f\n", x / size , y / size);
            copy.setCell(2, 20 - (int) Math.round(y / size), 20 - (int) Math.round(x / size));
            if (D > 0) {
                y = y + 1;
                D -= 2 * dx;
            }
            D += 2 * dy;
        }
        System.out.println(copy.toString());
        return copy;
    }

    private Point calculateViewRayEndpoint(double angle, int currX, int currY, int offset) {
        int size = Constants.CELL_WIDTH;
        int width = Floormap.WIDTH * size;
        int height = Floormap.HEIGHT * size;
        double run = size;
        double rise = calcRise(angle + offset) * size;
        double x = currX * size + Constants.center(size);
        double y = currY * size + Constants.center(size);

        if (angle > 180 ) {
            rise = -rise;
            run = -run;
        }

        while(inBound(x, width) && inBound(y, height)) {
            x += run;
            y -= rise;
        }
        int boundedX = bound((int) (x - run), width);
        int boundedY = bound((int) y, height);
        return new Point(boundedX, boundedY);
    }

    private int bound(int val, int bound) {
        if (val < -bound) return -bound;
        return Math.min(val, bound);
    }

    private Point calculateViewRayEndpoint(Camera camera) {
        return calculateViewRayEndpoint(camera.getAngle(), camera.getX(), camera.getY(),
                0);
    }

    private Point calculateViewRayEndpointOffset(Camera camera, int offset) {
        return calculateViewRayEndpoint(camera.getAngle(), camera.getX(), camera.getY(),
                offset);
    }

    private boolean inBound(double val, int bound) {
        return (val > 0 && val < bound);
    }

    private double calcRise(double angle) {
        return Math.tan(Math.toRadians(90 - angle));
    }

    public void drawLine(Point p1, Point p2) {
        g.drawLine(p1.x, p1.y, p2.x, p2.y);
    }
}
