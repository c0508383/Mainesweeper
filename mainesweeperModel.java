package MAINesweeper;

import static java.lang.System.*;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Random;

public class mainesweeperModel extends Application {
    boolean gameActive;
    boolean gameOver;
    mainesweeperView view = new mainesweeperView();

    boolean firstClick;
    HashMap bombs = new HashMap();
    int gridSize;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        gridSize = 16;
        view.setGridSize(gridSize);
        view.start(stage);
        modelGreetScreen();
    }

    public void modelGreetScreen() {
        view.viewGreetScreen();
    }

    public void modelGameOver(String winOrLose) {
        gameActive = false;
        gameOver = true;
        view.viewGameOver(winOrLose);
    }

    public void modelTitleTransition() {
        view.viewTitleTransition();

        view.greetTransitionScrollAnim.setOnFinished(event -> {
            view.viewGenerateTiles();
            view.viewStartGame();
            modelFirstClick();
        });
    }

    public void modelStartGame() {
        view.viewGenerateTiles();
        view.viewStartGame();
        modelFirstClick();

        gameOver = false;
        gameActive = true;
    }

    public void modelGenerateTiles(int exception) {
        Random rndBomb = new Random();
        int index = 0;
        while (index != 16) {
            bombs.put(index, rndBomb.nextInt(255));
            for (int a = 0; a < bombs.size(); a++) {
                if (bombs.get(a).equals(exception)) {
                    bombs.remove(a);
                    index--;
                }
            }
            index++;
        }
        out.println(bombs.toString());

        for (int a = 0; a < view.mineGridGroup.getChildren().size(); a++) {
            if (bombs.containsValue(a)) {
                view.mineGridGroup.getChildren().get(a).setOnMouseClicked(event -> {    //Bombs
                    modelGameOver("LOSE");
                });
                out.println("Click event added on bomb#" + view.mineGridGroup.getChildren().get(a).getId());
            }
            else{
                view.mineGridGroup.getChildren().get(a).setOnMouseClicked(event -> {    //Regular Tiles
                    modelTileBehavior();
                });
            }
        }
    }

    public void modelFirstClick() {
        for (int a = 0; a < view.mineGridGroup.getChildren().size(); a++) {
            int finalA = a;
            view.mineGridGroup.getChildren().get(a).setOnMouseClicked(event -> {
                int index = finalA;
                modelGenerateTiles(index);
            });
        }
    }

    public void modelTileBehavior(){
        
    }
}