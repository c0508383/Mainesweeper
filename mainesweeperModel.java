package MAINesweeper;

import static java.lang.System.*;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.*;

public class mainesweeperModel extends Application {
    boolean gameActive;
    boolean gameOver;
    mainesweeperView view = new mainesweeperView();

    HashMap bombs = new HashMap();
    int gridSize;
    HashMap revealedTiles = new HashMap();

    HashMap flags = new HashMap();

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
        view.viewGenerateTiles();
        view.viewStartGame();
        firstClick();

        gameOver = false;
        gameActive = true;
    }

    public void modelGenerateTiles(int exception) {
        revealedTiles.clear();

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
                int finalA = a;
                view.mineGridGroup.getChildren().get(a).setOnMouseClicked(event -> {    //Bombs
                    MouseButton button = event.getButton();
                    if (button == MouseButton.PRIMARY)
                        modelGameOver("LOSE");
                    else if (button == MouseButton.SECONDARY) {
                        view.revealTile(finalA, 10);
                        if (addFlag(finalA) == true)
                            modelGameOver("WIN");
                    }
                });
            } else {
                int finalA = a;
                view.mineGridGroup.getChildren().get(a).setOnMouseClicked(event -> {
                    MouseButton button = event.getButton();
                    if (button == MouseButton.PRIMARY) {
                        revealTiles(finalA);
                    } else if (button == MouseButton.SECONDARY) {
                        view.revealTile(finalA, 10);
                        if (addFlag(finalA))
                            modelGameOver("WIN");
                    }
                });
            }
        }
    }

    public void firstClick() {
        for (int a = 0; a < view.mineGridGroup.getChildren().size(); a++) {
            int finalA = a;
            view.mineGridGroup.getChildren().get(a).setOnMouseClicked(event -> {
                MouseButton button = event.getButton();
                if (button == MouseButton.PRIMARY) {
                    int index = finalA;
                    modelGenerateTiles(index);
                    revealTiles(index);
                }
            });
        }
    }

    public void revealTiles(int index) {
        ArrayList<Integer> wipeTiles = new ArrayList();

        if (index % 16 != 0 && (index - 17) >= 0)
            wipeTiles.add(wipeTiles.size(), index - 17);
        if ((index - 16) >= 0 && (index - 16) >= 0)
            wipeTiles.add(wipeTiles.size(), index - 16);
        if ((index + 1) % 16 != 0 && (index - 15) >= 0)
            wipeTiles.add(wipeTiles.size(), index - 15);

        if (index % 16 != 0 && (index - 1) >= 0)
            wipeTiles.add(wipeTiles.size(), index - 1);
        wipeTiles.add(wipeTiles.size(), index);
        if ((index + 1) % 16 != 0 && (index + 1) <= 255)
            wipeTiles.add(wipeTiles.size(), index + 1);

        if (index + 15 <= 255 && index % 16 != 0 && (index + 15) <= 255)
            wipeTiles.add(wipeTiles.size(), index + 15);
        if (index + 16 <= 255 && (index + 16) <= 255)
            wipeTiles.add(wipeTiles.size(), index + 16);
        if (index + 17 <= 255 && (index + 1) % 16 != 0 && (index + 17) <= 255)
            wipeTiles.add(wipeTiles.size(), index + 17);

        if (getAdjBombs(wipeTiles, index) == 0) {
            for (int a = 0; a < wipeTiles.size(); a++) {
                if (bombs.containsValue(wipeTiles.get(a)) == false && revealedTiles.containsValue(wipeTiles.get(a)) == false) {
                    revealedTiles.put(revealedTiles.size(), wipeTiles.get(a));
                    view.revealTile(index, 0);

                    if (getAdjBombs(wipeTiles, wipeTiles.get(a)) == 0)
                        revealTiles(wipeTiles.get(a));
                }
            }
        } else
            view.revealTile(index, getAdjBombs(wipeTiles, index));
    }

    public int getAdjBombs(ArrayList tiles, int index) {
        ArrayList adjTiles = new ArrayList(tiles);
        for (int a = 0; a < adjTiles.size(); a++) {
            if (adjTiles.get(a).equals(index))
                adjTiles.remove(a);
        }

        int adjBombInt = 0;
        for (int a = 0; a < adjTiles.size(); a++) {
            if (bombs.containsValue(adjTiles.get(a)))
                adjBombInt++;
        }

        return adjBombInt;
    }

    public boolean addFlag(int index) {
        flags.put(flags.size(), index);

        for (int a = 0; a < flags.size(); a++) {
            if (bombs.containsValue(flags.get(a)) == false)
                return false;
        }
        return true;
    }
}