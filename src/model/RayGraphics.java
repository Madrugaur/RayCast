package model;

import application.Constants;

import java.awt.*;

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
                if (map.getCell(r, c) == 1) {
                    int x = c * cellWidth;
                    int y = r * cellWidth;
                    g.setColor(Color.WHITE);
                    g.fillRect(x, y, cellWidth, cellWidth);
                    g.setColor(Color.GRAY);
                    g.drawRect(x, y, cellWidth, cellWidth);
                }
            }
        }
    }

    public void drawCamera(Camera camera, Floormap map) {
        int cellWidth = Constants.CELL_WIDTH;
        g.setColor(Color.RED);
        g.fillRect(camera.posX(cellWidth), camera.posY(cellWidth), cellWidth, cellWidth);
        int x1 = camera.posX(cellWidth) + Constants.center(cellWidth);
        int y1 = camera.posY(cellWidth) + Constants.center(cellWidth);
        Point origin = new Point(x1, y1);
        Point endpoint = calculateViewRayEndpoint(camera);
        g.setColor(Color.RED);
        drawLine(origin, endpoint);
        highlightViewedCells(endpoint, origin, map);
    }

    private void highlightViewedCells(Point endpoint, Point origin, Floormap floormap) {
        // y = m(x + x2) + y2
        double slope = (endpoint.y - origin.y * 1f) / (endpoint.x - origin.x);
        int size = Constants.CELL_WIDTH;

        int oR = origin.y / size;
        int oC = origin.x / size;
        int eR = endpoint.y / size;
        int eC = endpoint.x / size;

        System.out.printf("Origin -> r: %d, c: %d\n", oR, oC);
        System.out.printf("Endpnt -> r: %d, c: %d\n", eR, eC);
        System.out.println(slope);
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
