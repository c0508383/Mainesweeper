package MAINesweeper;

import static java.lang.System.*;
import javafx.application.Application;
import javafx.stage.Stage;

public class mainesweeperModel extends Application{
    boolean justOpened;
    boolean gameActive;
    boolean gameOver;
    mainesweeperView view = new mainesweeperView();
    public static void main(String[]args){
        launch(args);
    }
    public void start(Stage stage) throws Exception {
        view.start(stage);
    }
    public void modelGreetScreen(){
        view.viewGreetScreen();
        justOpened = true;
    }
    public void modelGameOver(String winOrLose){
        gameActive = false;
        gameOver = true;
        view.viewGameOver(winOrLose);
    }
    public void modelStartGame(){
        if(justOpened==true){
            view.viewGreetTranstition();
            justOpened = false;
        }
        else view.viewStartGame();

        gameOver = false;
        gameActive = true;
    }
}