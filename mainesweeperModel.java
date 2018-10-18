package MAINesweeper;

import static java.lang.System.*;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class mainesweeperModel extends Application {
    boolean gameActive;
    boolean gameOver;
    mainesweeperView view = new mainesweeperView();

    HashMap bombs = new HashMap();
    int gridSize;
    HashMap revealedTiles = new HashMap();

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
            firstClick();
        });
    }

    public void modelStartGame() {
        revealedTiles.clear();
        view.viewGenerateTiles();
        view.viewStartGame();
        firstClick();

        gameOver = false;
        gameActive = true;
    }

    public void modelGenerateTiles(int exception) {
        Random rndBomb = new Random();
        int index = 0;
        while (index != 40) {
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
                view.revealTile(a, -1);
                view.mineGridGroup.getChildren().get(a).setOnMouseClicked(event -> {    //Bombs
                    modelGameOver("LOSE");
                });
            } else {
                int finalA = a;
                view.mineGridGroup.getChildren().get(a).setOnMouseClicked(event -> {    //Regular Tiles
                    revealTile(finalA);
                });
            }
        }
    }

    public void firstClick() {
        for (int a = 0; a < view.mineGridGroup.getChildren().size(); a++) {
            int finalA = a;
            view.mineGridGroup.getChildren().get(a).setOnMouseClicked(event -> {
                int index = finalA;
                modelGenerateTiles(index);
            });
        }
    }

    public void revealTile(int index) {
        int surroundingBombs = getAdjBombs(index);

        if (surroundingBombs == 0) {
            view.revealTile(index, 0);
            tileWipe(index);
        } else
            view.revealTile(index, surroundingBombs);
        //else run a method to change the tile's image corresponding to surroundingBombs
    }

    public void tileWipe(int index) {
        out.println(index);

        revealedTiles.put(revealedTiles.size(), index);
        ArrayList<Integer> adjTiles = new ArrayList();

        if (index - 17 >= 0 && index % 16 != 0)
            adjTiles.add(index - 17);//top-left
        if (index - 16 >= 0)
            adjTiles.add(index - 16);//top
        if (index - 15 >= 0 && index % 15 != 0)
            adjTiles.add(index - 15);//top-right

        if (index + 15 <= 255 && index!=0)
            adjTiles.add(index + 15);//bot-left
        if (index + 16 <= 255)
            adjTiles.add(index + 16);//bot
        if (index + 17 <= 255 && index % 15 != 0)
            adjTiles.add(index + 17);//bot-right

        if (index + 1 <= 255 && index % 15 != 0)
            adjTiles.add(index + 1);
        if (index - 1 >= 0 && index % 16 != 0)
            adjTiles.add(index - 1);

        for (int a = 0; a < adjTiles.size(); a++) {
            if (getAdjBombs(adjTiles.get(a)) == 0) {
                view.revealTile(adjTiles.get(a), 0);
                if (revealedTiles.containsValue(adjTiles.get(a)) == false && bombs.containsValue(adjTiles.get(a)) == false)
                    tileWipe(adjTiles.get(a));
            } else if (bombs.containsValue(adjTiles.get(a)) == false)
                view.revealTile(adjTiles.get(a), getAdjBombs(a));
        }
    }

    public int getAdjBombs(int index) {
        int adjBombs = 0;

        if (bombs.containsValue(index - 17) && (index - 17 >= 0) && index % 16 != 0)
            adjBombs++;
        if (bombs.containsValue(index - 16) && (index - 16 >= 0))
            adjBombs++;
        if (bombs.containsValue(index - 15) && (index - 15 >= 0) && index % 15 != 0)
            adjBombs++;

        if (bombs.containsValue(index + 15) && (index + 15 <= 255) && index % 16 != 0)
            adjBombs++;
        if (bombs.containsValue(index + 16) && (index + 16 <= 255))
            adjBombs++;
        if (bombs.containsValue(index + 17) && (index + 17 <= 255) && index % 15 != 0)
            adjBombs++;

        if (bombs.containsValue(index + 1) && index + 1 <= 255 && index % 15 != 0)
            adjBombs++;
        if (bombs.containsValue(index - 1) && index - 1 >= 0 && index % 16 != 0)
            adjBombs++;

        return adjBombs;
    }
}