import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;

class GameTest {
    
    //Note that we cannot test cases when FILEPATH = null or invalid because the writers and
    //readers does not have a parameter for FILEPATH. It is stored as a final variable in Game.
    
    //Also note that while testing the writers, the reader is implicitly tested. I did not write
    //additional tests for the reader because I would be repeating the same tests.
    
    @Test
    public void writeCurrentTest() {
        Game.writeCurrent(10, 20);
        int[][] arr = Game.readRecordAndCurrent();
        assertEquals(arr[3][0], 10);
        assertEquals(arr[3][1], 20);
    }
    
    @Test
    public void clearRecordsTest() {
        Game.writeCurrent(10, 20);
        Game.clearRecords();
        int[][] arr = Game.readRecordAndCurrent();
        assertEquals(arr[0][0], 0);
        assertEquals(arr[0][1], 0);
        assertEquals(arr[1][0], 0);
        assertEquals(arr[1][1], 0);
        assertEquals(arr[2][0], 0);
        assertEquals(arr[2][1], 0);
        assertEquals(arr[3][0], 0);
        assertEquals(arr[3][1], 0);
    }
    
    @Test
    public void writeRecordsTest() {
        Game.clearRecords();
        Game.writeCurrent(100, 200);
        Game.writeRecords();
        int[][] arr = Game.readRecordAndCurrent();
        assertEquals(arr[0][0], 100);
        assertEquals(arr[0][1], 200);
        assertEquals(arr[3][0], 0);
        assertEquals(arr[3][1], 0);
        
        Game.writeCurrent(40, 30);
        Game.writeRecords();
        int[][] arr1 = Game.readRecordAndCurrent();
        assertEquals(arr1[1][0], 40);
        assertEquals(arr1[1][1], 30);
        
        Game.writeCurrent(500, 600);
        Game.writeRecords();
        int[][] arr2 = Game.readRecordAndCurrent();
        assertEquals(arr2[0][0], 500);
        assertEquals(arr2[0][1], 600);
        assertEquals(arr2[1][0], 100);
        assertEquals(arr2[1][1], 200);
        assertEquals(arr2[2][0], 40);
        assertEquals(arr2[2][1], 30);
    }
    
    @Test
    public void collectionsEncapsulationTest() {
        Snake snake = new Snake(0, 0, Color.BLUE);
        LinkedList<Body> body = snake.getBody();
        body.add(new Body(15, 15, Color.BLUE));
        body.add(new Body(30, 30, Color.BLUE));
        assertEquals(snake.getBody().size(), 1);
        assertFalse(body.equals(snake.getBody()));
    }
    
    @Test
    public void snakeColorEncapsulationTest() {
        Snake snake = new Snake(0, 0, Color.BLUE);
        @SuppressWarnings("unused")
        Color color = snake.getColor();
        color = Color.GREEN;
        assertEquals(snake.getColor(), Color.BLUE);
        snake.setColor(Color.BLACK);//uses written method to change color
        assertEquals(snake.getColor(), Color.BLACK);
    }
    
    @Test
    public void snakeMoveTest() {
        Snake snake = new Snake(0, 0, Color.BLUE);
        snake.changeDirection(1);
        snake.move();
        LinkedList<Body> body = snake.getBody();
        Body head = body.peekFirst();
        assertFalse(snake.outOfBound());
        assertFalse(snake.bodyCollision());
        assertEquals(head.getPx(), 15);
        assertEquals(head.getPy(), 0);
    }
    
    @Test
    public void snakeAppleGrowthTest() {
        Snake snake = new Snake(0, 0, Color.BLUE);
        snake.grow(0);
        assertEquals(snake.getBody().size(), 1);
        Apple apple = new Apple(snake);
        apple.randomGrowth();
        assertEquals(snake.getBody().size(), 4);
    }
    
    @Test
    public void snakeAvocadoGrowthTest() {
        Snake snake = new Snake(0, 0, Color.BLUE);
        assertEquals(snake.getBody().size(), 1);
        Avocado avocado = new Avocado(snake);
        avocado.randomGrowth();
        assertTrue(snake.getBody().size() >= 2);//because avocado increases length by at least 1
    }
    
    @Test
    public void snakeVirusGrowthTest() {
        Snake snake = new Snake(0, 0, Color.BLUE);
        Virus virus = new Virus(snake);
        boolean successfulShrink = virus.randomGrowth();
        assertFalse(successfulShrink);//since virus shrink's the length by at least 2 and the
        //snake does not have enough length, it will not shrink
        snake.grow(10);
        boolean successfulShrink2 = virus.randomGrowth();
        assertTrue(successfulShrink2);
        assertTrue(snake.getBody().size() <= 11);
    }
    
    @Test
    public void appleScoreTest() {
        Snake snake = new Snake(0, 0, Color.BLUE);
        Apple apple = new Apple(snake);
        assertEquals(apple.getScore(), 0);
        apple.changeScore();
        assertEquals(apple.getScore(), 5);
    }
    
    @Test
    public void avocadoScoreTest() {
        Snake snake = new Snake(0, 0, Color.BLUE);
        Avocado avocado = new Avocado(snake);
        assertEquals(avocado.getScore(), 0);
        avocado.changeScore();
        assertEquals(avocado.getScore(), 10);
    }
    
    @Test
    public void virusScoreTest() {
        Snake snake = new Snake(0, 0, Color.BLUE);
        Virus virus = new Virus(snake);
        assertEquals(virus.getScore(), 0);
        virus.changeScore();
        assertEquals(virus.getScore(), -8);
    }
    
    @Test
    public void appleColorTest() {
        Snake snake = new Snake(0, 0, Color.BLUE);
        Apple apple = new Apple(snake);
        assertEquals(snake.getColor(), Color.BLUE);
        apple.newColor();
        assertEquals(snake.getColor(), Color.BLUE);
    }
    
    //Note that avocado's color cannot be tested because it's newColor method can change
    //the snake's color to any random color
    
    @Test
    public void virusColorTest() {
        Snake snake = new Snake(0, 0, Color.BLUE);
        Virus virus = new Virus(snake);
        assertEquals(snake.getColor(), Color.BLUE);
        virus.newColor();
        assertFalse(snake.getColor().equals(Color.BLUE));
    }
    
    @Test
    public void snakeIntersectNullTest() {
        Snake snake = new Snake(0, 0, Color.BLUE);
        assertThrows(NullPointerException.class, () -> { 
            snake.intersect(null); });
    }
    
    
    @Test
    public void snakeOutOfBoundsWallTest() {
        Snake snake = new Snake(0, 0, Color.BLUE);
        snake.changeDirection(1);
        snake.move();
        snake.move();
        assertFalse(snake.outOfBound());
        snake.changeDirection(3);
        snake.move();
        assertTrue(snake.outOfBound());
    }
    
    @Test
    public void snakeOutOfBoundsCornerTest() {
        Snake snake = new Snake(0, 0, Color.BLUE);
        snake.changeDirection(4);
        snake.move();
        snake.move();
        assertFalse(snake.outOfBound());
        for (int i = 0; i < 37; i++) {
            snake.move();
        }
        assertFalse(snake.outOfBound());
        snake.move();
        assertTrue(snake.outOfBound());
    }
    
    @Test
    public void snakeBodyCollisionTest() {
        Snake snake = new Snake(0, 0, Color.BLUE);
        snake.changeDirection(4);
        snake.grow(10);
        for (int i = 0; i < 5; i++) {
            snake.move();
        }
        snake.changeDirection(1);
        snake.move();
        snake.changeDirection(3);
        snake.move();
        snake.changeDirection(2);
        snake.move();
        assertTrue(snake.bodyCollision());
    }
    
}
