package model;

import application.Constants;

import java.awt.*;
import java.util.Arrays;

/*
Make it so that drawing the camera is super easy

 */
public class RayGraphics {
    private final Graphics g;
    private static final double EFFECTIVE_INFINITY = 3.2662478706390739E17;
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
        DoublePoint origin = new DoublePoint(x1, y1);
        DoublePoint endpoint = calculateViewRayEndpoint(camera);

        Floormap copy = highlightViewedCells(endpoint, origin, map);
        drawFloormap(copy);
        _drawCamera(camera, origin, endpoint);
    }

    private void _drawCamera(Camera camera, DoublePoint origin, DoublePoint endpoint) {
        int size = Constants.CELL_WIDTH;
        g.setColor(Color.RED);
        g.fillRect(camera.posX(size), camera.posY(size), size, size);
        g.setColor(Color.RED);
        drawLine(origin, endpoint);
    }


    private Floormap highlightViewedCells(DoublePoint endpoint, DoublePoint origin, Floormap floormap) {
        final Floormap copy = floormap.copy();
        final double size = Floormap.WIDTH;
        double[] xarr = new double[] {origin.getX(), endpoint.getIntX()};
        double[] yarr = new double[] {origin.getY(), endpoint.getIntY()};
        Arrays.sort(xarr);
        Arrays.sort(yarr);
        double dx = xarr[0] - xarr[1];
        double dy = yarr[0] - yarr[1];
        if (dx == 0) {
            int step = 1;
            if (endpoint.y > origin.y) step = -step;
            for (int y = (int) (origin.y / size); y >= 0 && y < Floormap.HEIGHT; y -= step) {
                copy.setCell(2, y, (int) (origin.x / size));
            }
        } else {
            double D = 2 * dy - dx;
            double y = yarr[0];


            for (double x = xarr[0]; x < xarr[1]; x++) {
                int ny = (int) Math.round(y / size);
                int nx = (int) Math.round(x / size);
                copy.setCell(2, ny, nx);
                if (D > 0) {
                    y = y + 1;
                    D -= 2 * dx;
                }
                D += 2 * dy;
            }
        }

        return copy;
    }

    private DoublePoint calculateViewRayEndpoint(double angle, int currX, int currY, int offset) {
        int size = Constants.CELL_WIDTH;
        int width = Floormap.WIDTH * size;
        int height = Floormap.HEIGHT * size;
        double run = 1;
        double rise = calcRise(angle + offset) * size;
        double x = currX * size + Constants.center(size);
        double y = currY * size + Constants.center(size);

        if (angle > 180 ) {
            rise = -rise;
            run = -run;
        }
        double boundedX = 0;
        double boundedY = 0;

        if (Math.abs(rise) >= EFFECTIVE_INFINITY) {
            boundedX = (int) x;
            if (rise < 0) {
                boundedY = height;
            }
        } else if (Math.abs(rise) == Math.abs(run)) {
            boundedY = rise > 0 ? 0 : 400;
            boundedX = run > 0 ? 400 : 0;
        } else if (rise == 0){
            boundedX = run > 0 ? 400 : 0;
            boundedY = (int) y;
        } else {
            run /= rise;
            rise = 1;
            while(inBound(x, width) && inBound(y, height)) {
                x += run;
                y -= rise;
            }
            boundedX = bound(x, width);
            boundedY = bound(y, height);
        }

        return new DoublePoint(boundedX, boundedY);
    }

    private double bound(double val, int bound) {
        if (val < 0) return -bound;
        if (val > bound) return bound;
        return val;
    }

    private DoublePoint calculateViewRayEndpoint(Camera camera) {
        return calculateViewRayEndpoint(camera.getAngle(), camera.getX(), camera.getY(),
                0);
    }

    private boolean inBound(double val, int bound) {
        return (val > 0 && val < bound);
    }

    private double calcRise(double angle) {
        return Math.tan(Math.toRadians(90 - angle));
    }

    public void drawLine(DoublePoint p1, DoublePoint p2) {
        g.drawLine(p1.getIntX(), p1.getIntY(), p2.getIntX(), p2.getIntY());
    }

    private static class DoublePoint {
        private double x, y;
        public DoublePoint(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public int getIntX() {
            return (int) x;
        }
        public int getIntY() {
            return (int) y;
        }
    }
}
