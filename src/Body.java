import java.awt.Color;
import java.awt.Graphics;

public class Body extends Coordinate {
    
    //A block of square that represents a part of the snake's body. Extends the Coordinate
    //class
    private static final int WIDTH = 15;
    private static final int HEIGHT = 15;
    private static final int MAXW = 600;
    private static final int MAXH = 600;
    
    private Color color;
    
    public Body(int x, int y, Color c) {
        super(x, y, WIDTH, HEIGHT, MAXW, MAXH);
        
        color = c;
        
    }
    
    public void setColor(Color c) {
        color = c;
    }
    
    //returns true if the body is out of bounds
    public boolean hitWall() {
        return (getPx() < 0 || getPx() > getMaxX() || getPy() < 0 || getPy() > getMaxY());
    }
    
    //returns true if the current body intersects another coordinate object
    public boolean intersects(Coordinate that) {
        return (getPx() + getWidth() > that.getPx()
            && getPy() + getHeight() > that.getPy()
            && that.getPx() + that.getWidth() > getPx()
            && that.getPy() + that.getHeight() > getPy());
    }


    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(getPx(), getPy(), getWidth(), getHeight());
        
    }
    
}
