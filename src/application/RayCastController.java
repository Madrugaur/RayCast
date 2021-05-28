package application;

import model.Camera;
import model.Floormap;
import ui.ApplicationFrame;
import ui.ImageGenerator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RayCastController {
    public RayCastController(String imagePath) throws IOException {
        BufferedImage image = ImageIO.read(new File(imagePath));
        Floormap floormap = new Floormap(image);
        ImageGenerator imageGenerator = new ImageGenerator();
        Camera camera = new Camera();
        imageGenerator.generateTopDown(floormap, camera);
        new ApplicationFrame(imageGenerator, camera, floormap);
    }
}
