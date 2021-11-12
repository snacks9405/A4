import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.awt.Color;

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
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setMinSize(300, 300);
        pane.setVgap(10);
        pane.setHgap(10);
        pane.setStyle("-fx-background-color: blue");
        for (int i = 0; i < board.length; i++)

            for (int j = 0; j < board[0].length; j++)
            {
                board[i][j] = 0;
                String buttonName = String.valueOf(j+i);
                pane.add(new Button(buttonName), i, j, 1, 1); //Button() replace with Token()
            }    

        // JavaFX must have a Scene (window content) inside a Stage (window)
        Scene scene = new Scene(pane, 300,100);
        stage.setTitle("Connect 4 Game");
        stage.setScene(scene);

        // Show the Stage (window)
        stage.show();
    }
    
    public class Token extends Circle
    {
        
    }
    
    /**
     * This will be executed when the button is clicked
     * It increments the count by 1
     */
    private void buttonClick(ActionEvent event)
    {
        // Counts number of button clicks and shows the result on a label

    }
}
