package MAINesweeper;

import static java.lang.System.*;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class mainesweeperModel extends Application {
    boolean justOpened;
    boolean gameActive;
    boolean gameOver;
    mainesweeperView view = new mainesweeperView();

    HashMap bombs = new HashMap();
    int gridSize;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        view.setGridSize(16);
        view.start(stage);
        view.mineGridGroup.setOnMouseClicked(event -> {
            modelGameOver("LOSE");
        });
    }

    public void modelGreetScreen() {
        view.viewGreetScreen();
        justOpened = true;
    }

    public void modelGameOver(String winOrLose) {
        gameActive = false;
        gameOver = true;
        view.viewGameOver(winOrLose);
    }

    public void modelStartGame() {
        if (justOpened == true) {
            view.viewGreetTranstition();
            justOpened = false;
        } else view.viewStartGame();

        gameOver = false;
        gameActive = true;

        Random rndBomb = new Random();
        int index = 0;
        while(bombs.size()!=16){
            bombs.put(index,rndBomb.nextInt(255));
            index++;
        }
        out.println(bombs.toString());
    }
}