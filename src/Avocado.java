
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * A game object displayed using an image.
 * 
 * Note that the image is read from the file when the object is constructed, and that all objects
 * created by this constructor share the same image data (i.e. img is static). This is important for
 * efficiency: your program will go very slowly if you try to create a new BufferedImage every time
 * the draw method is invoked.
 */

//A subclass of the Food class. Inherits all traits of its parent class. Additionally, an Acocado
//changes score by 10, is a "good" fruit, and has it's own implementations of the Food's abstract
//methods.
public class Avocado extends Food {
    public static final String IMG_FILE = "files/avocado.jpeg";
    public static final boolean GOOD = true;
    private static final int WIDTH = 15;
    private static final int HEIGHT = 15;
    private static final int MAXW = 600;
    private static final int MAXH = 600;
    private static final int INC = 10;
    private Snake snake;

    private static BufferedImage img;

    public Avocado(Snake snake) {
        super(0, 0, WIDTH, HEIGHT, MAXW, MAXH, GOOD, INC, snake);
        
        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
        this.snake = snake;
        randomRelocate();
    }
    
    //can only relocate to any location that is 0-1 block away from the wall.
    @Override
    public void randomRelocate() {
        
        int x = ((int) (Math.random() * 40)) * 15;
        int y;
        if (x <= 15 || x >= 570) {
            y = ((int) (Math.random() * 40)) * 15;
        } else {
            int temp = (int) (Math.random() * 4);
            if (temp == 0) {
                y = 0;
            } else if (temp == 1) {
                y = 15;
            } else if (temp == 2) {
                y = 570;
            } else {
                y = 585;
            }
        }
        while (overlapBody(x, y)) {
            x = ((int) (Math.random() * 40)) * 15;
            if (x <= 15 || x >= 570) {
                y = ((int) (Math.random() * 40)) * 15;
            } else {
                int temp = (int) (Math.random() * 4);
                if (temp == 0) {
                    y = 0;
                } else if (temp == 1) {
                    y = 15;
                } else if (temp == 2) {
                    y = 570;
                } else {
                    y = 585;
                }
            }
        }
        setPx(x);
        setPy(y);
    }
    
    //randomly increases snake's length by any int 1-10
    @Override
    public boolean randomGrowth() {
        return snake.grow(((int) (Math.random() * 10) + 1));
    }
    
    //randomly changes snake's color  (any color)
    @Override
    public void newColor() {
        snake.setColor(new Color((int) (Math.random() * 256), (int) (Math.random() * 256), 
                (int) (Math.random() * 256)));
    }
    
    @Override
    public void draw(Graphics g) {
        g.drawImage(img, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
    }
}
