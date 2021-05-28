package ui;

import application.Constants;
import model.Camera;
import model.Floormap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ApplicationFrame extends JFrame implements KeyListener {
    public static final Dimension SCREEN_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int WIDTH = (int) (SCREEN_DIMENSION.getWidth() * (2 / 3.0));
    public static final int HEIGHT = (int) (SCREEN_DIMENSION.getHeight() * (2 / 3.0));

    private final JPanel contentPanel;
    private final Camera camera;
    private final ImageGenerator imageGenerator;
    private final Floormap map;

    private final double THETA_STEP = 2.8125;

    private Image source;

    public ApplicationFrame(ImageGenerator imageGenerator, Camera camera, Floormap map) {
        super();
        final ApplicationFrame selfRef = this;
        this.camera = camera;
        this.imageGenerator = imageGenerator;
        this.map = map;

        this.setSize(WIDTH, HEIGHT);
        int x = Constants.center(WIDTH);
        int y = Constants.center(HEIGHT);
        this.setLocation(x,y);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);


        contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(source, 0, 0, null);
            }
        };
        contentPanel.setLocation(0, 0);
        this.add(contentPanel);
        this.addKeyListener(this);
        this.setTitle(String.format("RayCast (%dx%d)", WIDTH, HEIGHT));
        this.setVisible(true);
        this.update();
    }


    public void update() {
        this.source = imageGenerator.generateTopDown(map, camera);
        this.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent ke) {
        if (ke.getKeyCode() == 87) {
            //keyPressed = 'w'
        }else if (ke.getKeyCode() == 65) {
            //keyPressed = 'a'
            camera.rotate(-THETA_STEP);
        }else if (ke.getKeyCode() == 68) {
            //keyPressed = 'd'
            camera.rotate(THETA_STEP);
        }else if (ke.getKeyCode() == 83) {
            //keyPressed = 's'
        }else if (ke.getKeyCode() == 10) {
            //keyPressed = Enter
        }else if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (JOptionPane.showConfirmDialog(this, "Do you really want to quit?", "Exit",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                System.exit(1);
        }
        this.update();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
