package ui;

import application.Constants;
import model.Camera;
import model.Floormap;
import model.RayGraphics;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageGenerator {

    public Image generateTopDown(Floormap map, Camera camera) {
        Image image = new BufferedImage(ApplicationFrame.WIDTH, ApplicationFrame.HEIGHT, BufferedImage.TYPE_INT_RGB);
        RayGraphics graphics = new RayGraphics(image.getGraphics());
        graphics.drawCamera(camera, map);

        return image;
    }

    public Image generateImage() {
        Image image = new BufferedImage(ApplicationFrame.WIDTH, ApplicationFrame.HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        int y = 0;
        graphics.setColor(Color.RED);
        graphics.fillRect(0,0, 20,20);

        return image;
    }

}
