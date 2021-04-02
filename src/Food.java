
//This Food class extends Coordinate. It serves as the parent class of 3 types of food: Apple,
//Avocado, Coronavirus. All Food objects have score, inherits properties of Coordinates, and 
//have methods that check for snakeBody overlap, increases score, as well as gets score.
public abstract class Food extends Coordinate {
    
    private boolean good;//if score is good, the food increases score and vice versa
    private int score;
    private int inc;
    private Snake snake;
    
    
    public Food(int px, int py, int width, int height, int courtWidth, int courtHeight, 
                    boolean g, int inc, Snake snake) {
        super(px, py, width, height, courtWidth, courtHeight);
        this.inc = inc;
        good = g;
        this.snake = snake;
        randomRelocate();
    }
    
    //updates score of the specific food object
    public void changeScore() {
        if (good) {
            score += inc;
        } else {
            score -= inc;
        }
    }
    
    //checks if the inputed position is taken up by the snake's body. Used in randomRelocate to
    //ensure that no Food object relocates onto the snake's body.
    public boolean overlapBody(int x, int y) {
        for (Body b : snake.getBody()) {
            if (x == b.getPx() && y == b.getPy()) {
                return true;
            }
        }
        return false;
    }
    
    
    public int getScore() {
        return score;
    }
    
    //abstract methods that subclasses will override
    public abstract void randomRelocate();
    
    public abstract boolean randomGrowth();
    
    public abstract void newColor();
    

}
