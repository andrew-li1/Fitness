
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

//A subclass of the Food class. Inherits all traits of its parent class. Additionally, an Apple
//changes score by 5, is a "good" fruit, and has it's own implementations of the Food's abstract
//methods.
public class Apple extends Food {
    public static final String IMG_FILE = "files/apple.jpg";
    public static final boolean GOOD = true;
    private static final int WIDTH = 15;
    private static final int HEIGHT = 15;
    private static final int MAXW = 600;
    private static final int MAXH = 600;
    private static final int INC = 5;
    private Snake snake;
    private static BufferedImage img;
    
    //passes in a snake object so the Apple can update it's length and color.
    public Apple(Snake snake) {
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
    
    
    //randomly relocates to any part of the board
    @Override
    public void randomRelocate() {
        int x = ((int) (Math.random() * 40)) * 15;
        int y = ((int) (Math.random() * 40)) * 15;
        while (overlapBody(x, y)) {
            x = ((int) (Math.random() * 40)) * 15;
            y = ((int) (Math.random() * 40)) * 15;
        }       
        setPx(x);
        setPy(y);
    }
    
    //Always grows the snake by length 3
    @Override
    public boolean randomGrowth() {
        return snake.grow(3);
    }
    
    //Always sets the snake's color to blue when eaten
    @Override
    public void newColor() {
        snake.setColor(Color.BLUE);
    }
    
    @Override
    public void draw(Graphics g) {
        g.drawImage(img, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
    }
}
