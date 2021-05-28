import application.RayCastController;
import ui.ApplicationFrame;

import java.io.IOException;

public class Launch {
    public static void main (String[] args) throws IOException {
        if (args.length != 0) {
            System.out.println(args[0]);
            new RayCastController(args[0]);
        }
    }
}
