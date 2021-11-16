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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
/**
 * an interactive game of C4
 *
 * @author michael hulbert
 *         alex rodriguez
 * @version 11.26.21
 */
public class A4 extends Application
{
    // We keep track of the count, and label displaying the count:
    private int[][] board = new int[7][6];
    private static int padding = 25;
    private static int cellSize = 50;
    private Label displayMessage;
    private boolean gameover = false;

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
        GridPane pane = new GridPane();
        pane.setHgap(padding);
        pane.setVgap(padding);
        pane.setPadding(new Insets(padding));

        Token[][] tokens = new Token[7][6];
        Random rand = new Random();
        for (int i = 0; i < board.length; i++)
        {
            for (int j = 0; j < board[0].length; j++)
            {
                //    (i, j)   ->   (x, y)
                board[i][j] = 0;
                Token token = new Token(i, j);
                tokens[i][j] = token;
                pane.add(token, i, j);
                
                token.setOnMousePressed(event -> {
                        if (!gameover)
                        {
                            int col = token.col;
                            int row;
                            // PLAY PLAYERS MOVE
                            if((row = token.playToken(tokens, 1, Color.YELLOW)) == -1)
                            {
                                displayMessage.setText("The column is full. Try again!");
                                return;
                            }
                            // Test victory / tie (player)
                            int playerVictory = tokens[col][row].isVictory();
                            if(token.updateLabel(playerVictory, "", "You won!!", "You tied!!", ""))
                                return;

                            // PLAY COMPUTER'S MOVE
                            do
                            {
                                col = rand.nextInt(board.length);
                            } while((row = tokens[col][0].playToken(tokens, 2, Color.RED)) == -1);

                            // Test victory / tie (computer)
                            int compVictory = tokens[col][row].isVictory();
                            if(tokens[col][row].updateLabel(compVictory,
                                "The computer played in column " + (col+1),
                                " and won!",
                                " and tied!",
                                ". Your turn!"))
                                return;
                        }
                    });
            }
            // Create Bottom Number
            Label nLabel = new Label((i+1) + "");
            nLabel.setStyle("-fx-text-fill: white;");
            pane.setHalignment(nLabel, HPos.CENTER);
            pane.add(nLabel, i, board[0].length);
        }
        pane.setStyle("-fx-background-color: blue; -fx-margin: 50%;");

        displayMessage = new Label("It's your turn to play...");        //initial display message & style & alignment
        displayMessage.setFont(Font.font("Comic Sans MS", 18));
        displayMessage.setStyle("-fx-text-fill: aqua;");
        pane.setHalignment(displayMessage, HPos.CENTER);
        pane.add(displayMessage, 0, board[0].length + 1, board.length, 1);

        int w = (cellSize+padding) * board.length + padding;
        int h = (int)((cellSize + padding) * (board[0].length + .5)) + padding;
        h += cellSize;
        Scene scene = new Scene(pane, w, h);
        stage.setTitle("Connect 4 Game");
        stage.setScene(scene);

        stage.show();
    }// start method

    private class Token extends Circle
    {
        int row, col;
        
        /**
         * Constructor for objects of class Token
         * 
         * @param col       col index of token
         * @param row       row index of token
         */
        public Token (int col, int row)
        {
            super((cellSize) / 2);
            this.row = row;
            this.col = col;

            setFill(Color.WHITE);
        }// constructor

        /**
         * Updates label based on the state and parameters
         * 
         * @param victoryState      the state of victory
         * @param prefix            first part of message, always displayed.
         * @param victoryText       tail of the message, displayed on victory
         * @param tieText           tail of the message, displayed on tie
         * @param defaultText       tail of the message, displayed on no-victory
         * 
         * @return true if the game is over (based on victoryState)
         * 
         */
        public boolean updateLabel(int victoryState, String prefix, String victoryText, String tieText, String defaultText)
        {
            String message = prefix;
            switch(victoryState)
            {
                case 1: // Victory
                    message += victoryText;
                    break;
                case 3: // tieText
                    message += tieText;
                    break;
                default: // non-victory
                    message += defaultText;
                    break;
            }
            displayMessage.setText(message);

            gameover = victoryState != 0;
            return gameover;
        }// updateLabel method

        /**
         * Plays a red or yellow token
         * 
         * @param tokens    an array filled with all token objects (for color manipulation)
         * @param id        the id of the token user
         * @param color     the color that will be set
         * 
         * @return the column the token was played (-1 if invalid move)
         */
        public int playToken(Token[][] tokens, int id, Color color)
        {

            for(int k = board[col].length-1; k>=0; --k)
            {
                if(board[col][k] == 0)
                {
                    tokens[col][k].setFill(color);
                    board[col][k] = id;
                    return k;
                }
            }
            return -1;
        }// playToken method

        /**
         * Tests if a this cell contains a victory condition
         * 
         * @return 0 if no victory, 1 if victory, 2 if tie
         */
        public int isVictory()
        {
            int id = board[col][row];
            if(id == 0) return 0;

            boolean victory = isVictoryInDirection(id, 1, 0)   // horizontal
                || isVictoryInDirection(id, 0,  1)   // vertical
                || isVictoryInDirection(id, 1,  1)   // diagonal, up
                || isVictoryInDirection(id, 1, -1);  // diagonal, down
            if(victory) return 1;

            boolean tie = true;
            for(int i = 0; i < board.length; i++)
                if(board[i][0] == 0) tie = false;
            return tie ? 2 : 0;
        }// isVictory method

        /**
         * Helper function for testing victory state
         * 
         * @param id    the id to test against (1 for player, 2 for computer)
         * @param dx    delta x: direction to test victory state
         * @param dy    delta y: direction to test victory state
         * 
         * @return if there is a victory in the given direction
         */
        private boolean isVictoryInDirection(int id, int dx, int dy)
        {
            for(int i = 0; i < 4; i++)
            {
                boolean victory = true;
                for(int j = 0; j < 4; j++)
                {
                    int x = col + (j - i) * dx;
                    int y = row + (j - i) * dy;

                    if(!isPositionValid(x, y) || board[x][y] != id)
                    {
                        victory = false;
                        break;
                    }
                }
                if(victory) return true;
            }

            return false;
        }// isVictoryInDirection method

        /**
         * Tests if a position is within the bounds of the board
         * 
         * @return if the position is valid
         */
        private boolean isPositionValid(int x, int y)
        {
            return x >= 0 && x < board.length && y >= 0 && y < board[x].length;
        }// isPositionValid method
    }// Token Class ( Inner )
}// A4 Class
