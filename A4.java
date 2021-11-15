import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.shape.Circle;
import javafx.geometry.*;
import java.util.Random;

import java.awt.event.MouseEvent;
import javafx.scene.paint.Color;

/**
 * Write a description of JavaFX class A4 here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class A4 extends Application
{
    // We keep track of the count, and label displaying the count:
    private int[][] board = new int[7][6];
    
    private static int padding = 25;
    private static int cellSize = 50;
    private Label displayMessage;
    /**
     * The start method is the main entry point for every JavaFX application. 
     * It is called after the init() method has returned and after 
     * the system is ready for the application to begin running.
     *
     * @param  stage the primary stage for this application.
     */
    @Override
    public void start(Stage stage)
    {

        // Create a new grid pane
        GridPane pane = new GridPane();
        pane.setHgap(padding);
        pane.setVgap(padding);
        pane.setPadding(new Insets(padding));
        
        
        Token[][] tokens = new Token[7][6];
        Random rand = new Random();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++)
            {
                board[i][j] = 0;
                Token t = new Token(i, j);
                tokens[i][j] = t;
                pane.add(t, i, j, 1, 1);
                t.setOnMousePressed(event -> {
                    // NOT VALID MOVE
                    if(board[t.row][0] != 0) return;
                    // PLAY PLAYERS MOVE
                    for(int k=board[t.row].length-1; k>=0; --k)
                    {
                        if(board[t.row][k] == 0)
                        {
                            tokens[t.row][k].setFill(Color.YELLOW);
                            board[t.row][k] = 1;
                            break;
                        }
                    }
                    // PLAY COMPUTER'S MOVE
                    int row;
                    boolean valid = false;
                    do
                    {
                        row = rand.nextInt(board.length);
                        for(int k=board[row].length-1; k>=0; --k)
                        {
                            if(board[row][k] == 0)
                            {
                                tokens[row][k].setFill(Color.RED);
                                board[row][k] = 2;
                                valid = true;
                                break;
                            }
                        }
                    } while(!valid);
                });
            }
            // Create Bottom Number
            Label nLabel = new Label((i+1) + "");
            nLabel.setStyle("-fx-text-fill: white;");
            pane.setHalignment(nLabel, HPos.CENTER);
            pane.add(nLabel, i, board[0].length);
        }
        pane.setStyle("-fx-background-color: blue; -fx-margin: 50%;");
        // FOR SOME REASON commenting this line out fixes background?.....
        displayMessage = new Label("Hello guys");
        displayMessage.setStyle("-fx-text-fill: aqua;");
        //displayMessage.setAlignment(Pos.BASELINE_CENTER);
        pane.setHalignment(displayMessage, HPos.CENTER);
        pane.add(displayMessage, 0, board[0].length + 1, board.length, 1);
        
        
        int w = (cellSize+padding) * board.length + padding;
        int h = (int)((cellSize+padding) * (board[0].length + .5))    + padding;
        h += cellSize;
        Scene scene = new Scene(pane, w, h, Color.BLUE);
        stage.setTitle("Connect 4 Game");
        stage.setScene(scene);
        
        stage.show();
    }
    
    public class Token extends Circle
    {
        int row, col;
        public Token (int row, int col)
        {
            super((cellSize)/2);
            this.row = row;
            this.col = col;
            
            setFill(Color.WHITE);
        }
    }
}
