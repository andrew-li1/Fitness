import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedList;

public class Snake {
    private LinkedList<Body> body;//linkedList of snake body parts
    private Body head;
    private int direction;//1 - right, 2 - left, 3 - up, 4 - down
    private Color color;
    private boolean wall = false;
    private boolean grown = false;
    private boolean movedAfterGrown = false;//If snake did not move it's head after growing when
    //it's body length is 1, the bodyCollision method will believe that the snake's head hit its
    //body. However, this variable will indicate to the method that snake did not experience a
    //body collision if that is the case.
    
    public Snake(int x, int y, Color c) {
        body = new LinkedList<>();
        head = new Body(x, y, c);
        body.add(head);
        color = c;
    }
    
    //used to detect intersection of head and food
    public boolean intersect(Coordinate obj) {
        return head.intersects(obj);
    }
    
    //grows snake by length g. Can be positive or negative
    public boolean grow(int g) {
        if (g >= 0) {
            Body tail = body.peekLast();
            for (int i = 0; i < g; i++) {
                body.addLast(tail);
            }
            grown = true;
            return true;
        } else {
            if (Math.abs(g) >= body.size()) {
                return false;
            } else {
                for (int i = 0; i > g; i--) {
                    body.removeLast();
                }
                return true;
            }
        } 
    }    
    
    //returns body. Encapsulated
    public LinkedList<Body> getBody() {
        LinkedList<Body> bodyList = new LinkedList<>();
        bodyList.addAll(body);
        return bodyList;
    }
    
    //returns color. Encapsulated
    public Color getColor() {
        return new Color(color.getRed(), color.getGreen(), color.getBlue());
    }
    
    public void setColor(Color c) {
        color = c;
        for (Body s : body) {
            s.setColor(c);
        }
    }
    
    //Updates direction. The snake cannot change direction to the opposite of it's current 
    //direction.
    public void changeDirection(int d) {
        if (d < 0 || d > 4) {
            throw new IllegalArgumentException();
        } else {
            if (d == 1 && direction != 2) {
                direction = d;
            } else if (d == 2 && direction != 1) {
                direction = d;
            } else if (d == 3 && direction != 4) {
                direction = d;
            } else if (d == 4 && direction != 3) {
                direction = d;
            }
        }
    }
    
    //returns true if snake has a direction
    public boolean hasDirection() {
        if (direction > 0 && direction < 5) {
            return true;
        }
        return false;
    }
    
    //moves snake by adding a block to the front of the linkedList and removing last element of
    //linkedList
    public void move() {
        if (direction == 0) {
            return;
        } else if (direction == 1) {
            head = new Body(head.getPx() + 15, head.getPy(), color);
        } else if (direction == 2) {
            head = new Body(head.getPx() - 15, head.getPy(), color);
        } else if (direction == 3) {
            head = new Body(head.getPx(), head.getPy() - 15, color);
        } else {
            head = new Body(head.getPx(), head.getPy() + 15, color);
        }
        if (head.hitWall()) {
            wall = true;
        } else {
            body.addFirst(head);
            body.removeLast();
        }
        if (grown) {
            movedAfterGrown = true;
        }
    }
    
    //checks if snake hits wall
    public boolean outOfBound() {
        return wall;
    }
    
    //checks if snake's head hits its body
    public boolean bodyCollision() {
        boolean result = false;
        if (movedAfterGrown) {
            Iterator <Body> iter = body.iterator();
            iter.next();
            while (iter.hasNext()) {
                Body c = iter.next();
                if (c.intersects(head)) {
                    result = true;
                }
            }
        }
        return result;     
    }
    
    public void draw(Graphics g) {
        for (Body c : body) {
            c.draw(g);
        }
    }
    
    
}
