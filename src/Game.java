
// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
    //filepath. The records file consists of 4 lines, where the first 3 lines represents the
    //top 3 records, and the last line represents the current score and time. The two states
    //recorded are the score and time, and they are separated by a single space in the file.
    //rankings of record is evaluated by score * 0.6 + time * 0.4
    private static final String FILEPATH = "files/records.txt";
    
    //clears records file
    public static void clearRecords() {
        try {
            FileWriter fw = new FileWriter(FILEPATH, false);
            fw.close();
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException();
        } catch (IOException e1) {
            System.out.println("Error in writing");
        }
    }
    
    //writes down current score and time
    public static void writeCurrent(int score, int time) {
        int[][] arr = readRecordAndCurrent();
        FileWriter fw;
        try {
            fw = new FileWriter(FILEPATH, false);
            BufferedWriter bw = new BufferedWriter(fw);
            for (int i = 0; i < 3; i++) {
                bw.write(arr[i][0] + " " + arr[i][1] + "\n");
            }
            bw.write(score + " " + time);
            bw.close();
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException();
        } catch (IOException e1) {
            System.out.println("Error in writing");
        }
        
        
    }
    
    //Used when a game ends. Update the top 3 records with the current score and time, if
    //necessary. Change current score and time to 0
    public static void writeRecords() {
        int[][] arr = readRecordAndCurrent();
        int rank = 0;
        double performance = arr[3][0] * 0.6 + arr[3][1] * 0.4;
        
        
        if (performance >= arr[0][0] * 0.6 + arr[0][1] * 0.4) {
            rank = 1;
        } else if (performance >= arr[1][0] * 0.6 + arr[1][1] * 0.4) {
            rank = 2;
        } else if (performance >= arr[2][0] * 0.6 + arr[2][1] * 0.4) {
            rank = 3;
        }
        
        if (rank != 0) {
            FileWriter fw;
            try {
                fw = new FileWriter(FILEPATH, false);
                BufferedWriter bw = new BufferedWriter(fw);
                for (int i = 0; i < rank - 1; i++) {
                    bw.write(arr[i][0] + " " + arr[i][1] + "\n");
                }
                bw.write(arr[3][0] + " " + arr[3][1] + "\n");
                for (int j = 0; j < 3 - rank; j++) {
                    bw.write(arr[rank - 1 + j][0] + " " + arr[rank - 1 + j][1] + "\n");
                }
                bw.write(0 + " " + 0);
                bw.close();
            
            } catch (FileNotFoundException e) {
                throw new IllegalArgumentException();
            } catch (IOException e1) {
                System.out.println("Error in writing");
            }
        }
        
    }
    
    //reads the records and current score and time, and outputs them using a 2D array
    public static int[][] readRecordAndCurrent() {
        int[][] arr = new int[4][2];
        int count = 0;
        try {
            FileReader fr = new FileReader(FILEPATH);
            BufferedReader br = new BufferedReader(fr);
            try {
                String line = br.readLine();
                while (line != null && count < 4) {
                    String[] record = line.split(" ");
                    arr[count][0] = Integer.parseInt(record[0]);
                    arr[count][1] = Integer.parseInt(record[1]);
                    line = br.readLine();
                    count++;
                }
                br.close();
            } catch (IOException e) {
                System.out.println("Error in reading");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error in reading");
        }
        return arr;
    }
    
    //Return a formatted string that states the top 3 records as well as current score and time.
    //To be displayed in game. used html because regular did not successfully skip line.
    public static String getRecords() {
        int[][] arr = readRecordAndCurrent();
        String result = "<html>Records: <br/>----------------------------------------<br/>";
        for (int i = 1; i < 4; i++) {
            result += i + ". Score: " + arr[i - 1][0] + "\tTime: " + arr[i - 1][1] + "<br/>";
        }
        result += "<br/>Current Game:<br/>----------------------------------------<br/>"
                + "   Score: " + arr[3][0] + "\tTime: " + arr[3][1] + "<html>";
        return result;
    }
    
    
    
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for local variables.
        
        
        

        // Top-level frame in which game components live
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("Snake 2.0");
        frame.setLocation(300, 300);
        
        
        String description = ""
                + "This is Snake 2.0, where a snake now has the option to eat magial avocados and\n"
                + " coronaviruses!\n\n"
                + "- Apple: increase length by 3, increase score by 5, change color to blue.\n"
                + "- Avocado: randomly increase length, increase score by 10, randomly change \n"
                + "  color. They will only appear near the wall.\n"
                + "- Coronavirus: randomly decrease length, decrease score by 8, randomly change\n"
                + "  color to a dark grey. They will only appear in the center region of gameboard."
                + "\n\n"
                + "Perfomance is evaluated by calculating a player's score * 0.6 + time * 0.4. The"
                + "\n"
                + "unit for time is seconds.\n\n"
                + "Use direction arrows on the keyboard to direct snake to travel and eat. A player"
                + "\n"
                + "loses when the snake's head hits the wall, or when the score becomes negative \n"
                + "(caused by the coronavirus).\n\n"
                + "Have fun!";
        JOptionPane.showMessageDialog(frame, description);
        
        
        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);
        
        //records panel is placed to the east of the gameboard
        final JPanel records_panel = new JPanel();
        frame.add(records_panel, BorderLayout.EAST);
        final String records_display = getRecords();
        JLabel records = new JLabel(records_display);
        records_panel.add(records);
        
        // Main playing area
        final GameCourt court = new GameCourt(status, records);
        frame.add(court, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we define it as an
        // anonymous inner class that is an instance of ActionListener with its actionPerformed()
        // method overridden. When the button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.reset();
            }
        });
        control_panel.add(reset);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        court.reset();
    }

    /**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
     * runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}