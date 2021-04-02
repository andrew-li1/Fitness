

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact with one another. Take
 * time to understand how the timer interacts with the different methods and how it repaints the GUI
 * on every tick().
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

    // the state of the game logic
    private Snake snake;
    private Apple apple;
    private Avocado avocado;
    private Virus virus;
    int count;//used to track number of ticks to calculate time
    int score;
    
    private boolean playing = false; // whether the game is running 
    private JLabel status; // Current status text, i.e. "Running..."
    private JLabel records;

    // Game constants
    public static final int COURT_WIDTH = 600;
    public static final int COURT_HEIGHT = 600;
    //public static final int SQUARE_VELOCITY = 4;

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 60;

    public GameCourt(JLabel status, JLabel records) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // The timer is an object which triggers an action periodically with the given INTERVAL. We
        // register an ActionListener with this timer, whose actionPerformed() method is called each
        // time the timer triggers. We define a helper method called tick() that actually does
        // everything that should be done in a single timestep.
        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start(); // MAKE SURE TO START THE TIMER!

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // This key listener allows the snake to change change directions when a directional arrow
        //is pressed. (The tick method below actually moves the square.)
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    //if (snake.getDirection() != 1) {
                        snake.changeDirection(2);
                    //}
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    //if (snake.getDirection() != 2) {
                        snake.changeDirection(1);
                    //}
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    //if (snake.getDirection() != 3) {
                        snake.changeDirection(4);
                    //}
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    //if (snake.getDirection() != 4) {
                        snake.changeDirection(3);
                    //}
                }
            }
        });

        this.status = status;
        this.records = records;
        
        Game.clearRecords();
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        snake = new Snake(0, 0, Color.BLUE);
        avocado = new Avocado(snake);
        apple = new Apple(snake);
        virus = new Virus(snake);
        
        Food f = new Apple(snake);
        
        count = 0;
        playing = true;
        status.setText("Running...");
        //update records
        Game.writeRecords();
        records.setText(Game.getRecords());

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }

    /**
     * This method is called every time the timer defined in the constructor triggers.
     */
    void tick() {
        if (playing) {
            count++;
            // advance the snake in their current direction.
            snake.move();
            
            //check for intersection with fruits or viruses
            if (snake.intersect(apple)) {
                apple.randomGrowth();
                apple.randomRelocate();
                apple.changeScore();
                apple.newColor();
                //snake.setColor(apple.newColor());
            } else if (snake.intersect(avocado)) {
                avocado.randomGrowth();
                avocado.randomRelocate();
                avocado.changeScore();
                avocado.newColor();
            } else if (snake.intersect(virus)) {
                virus.changeScore();
                virus.randomRelocate();
                if (virus.randomGrowth()) {
                    virus.newColor();
                } else {
                    playing = false;
                    snake.setColor(Color.BLACK);
                    status.setText("You lose!");
                }
            }
            
            //updates current score
            score = apple.getScore() + avocado.getScore() + virus.getScore();
            Game.writeCurrent(score, ((int)(count * 60 / 1000.0)));
            records.setText(Game.getRecords());
                    
            // check for the game end conditions
            if (snake.hasDirection()) {
                if (snake.outOfBound()) {
                    playing = false;
                    snake.setColor(Color.BLACK);
                    status.setText("You lose!");
                } else if (snake.bodyCollision()) {
                    playing = false;
                    snake.setColor(Color.BLACK);
                    status.setText("You lose!");
                } else if (score < 0) {
                    playing = false;
                    snake.setColor(Color.BLACK);
                    status.setText("You lose!");
                }
            }
            
            
            // update the display
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        snake.draw(g);
        apple.draw(g);
        avocado.draw(g);
        virus.draw(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}