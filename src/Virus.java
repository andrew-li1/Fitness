
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

//A subclass of the Food class. Inherits all traits of its parent class. Additionally, a Virus
//changes score by 8, is NOT a "good" fruit, and has it's own implementations of the Food's abstract
//methods.
public class Virus extends Food {
    public static final String IMG_FILE = "files/virus.jpg";
    public static final boolean GOOD = false;
    private static final int WIDTH = 15;
    private static final int HEIGHT = 15;
    private static final int MAXW = 600;
    private static final int MAXH = 600;
    private static final int INC = 8;
    private Snake snake;

    private static BufferedImage img;

    public Virus(Snake snake) {
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
    
    //only relocates in the center 300x300 portion of the 600x600 pixel board
    @Override
    public void randomRelocate() {
        int x = ((int) (Math.random() * 20)) * 15 + 150;
        int y = ((int) (Math.random() * 20)) * 15 + 150;
        while (overlapBody(x, y)) {
            x = ((int) (Math.random() * 20)) * 15 + 150;
            y = ((int) (Math.random() * 20)) * 15 + 150;
        }
        setPx(x);
        setPy(y);
    }
    
    //randomly shrinks the snake's length by 2-6
    @Override
    public boolean randomGrowth() {
        return snake.grow((-1 * ((int) (Math.random() * 5) + 2)));
    }

    //changes the snake's color to a random dark grey
    @Override
    public void newColor() {
        int r = (int) (Math.random() * 3);
        if (r == 0) {
            snake.setColor(new Color(105, 105, 105));
        } else if (r == 1) {
            snake.setColor(new Color(128, 128, 128));
        } else {
            snake.setColor(new Color(169, 169, 169));
        }
        
    }
    
    @Override
    public void draw(Graphics g) {
        g.drawImage(img, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
    }
}
