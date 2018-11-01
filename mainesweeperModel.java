package MAINesweeper;

import javafx.application.Application;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import static java.lang.System.out;

public class mainesweeperModel extends Application {
    boolean gameActive;
    boolean gameOver;
    mainesweeperView view = new mainesweeperView();

    ArrayList bombs = new ArrayList();
    int gridSize;
    HashMap revealedTiles = new HashMap();

    ArrayList flags = new ArrayList();

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

    public void gameOver(String winOrLose, int bombClicked) {
        gameActive = false;
        gameOver = true;
        //view.viewGameOver(winOrLose);
        view.gameOverStart(winOrLose, bombClicked, bombs);
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
        flags.clear();
        bombs.clear();

        Random rndBomb = new Random();
        int index = 0;
        while (index != 40) {
            int value = rndBomb.nextInt(255);
            if (containsValue(bombs, value) == false && value != exception)
                bombs.add(value);
            if (bombs.size() == index)
                index--;
            index++;
        }
        out.println(bombs.toString());

        for (int a = 0; a < view.mineGridGroup.getChildren().size(); a++) {
            int finalA = a;
            if (containsValue(bombs, a)) {
                //view.revealTile(a, -1);
                view.mineGridGroup.getChildren().get(a).setOnMouseClicked(event -> {    //Bombs
                    MouseButton button = event.getButton();
                    if (button == MouseButton.PRIMARY)
                        gameOver("LOSE", finalA);
                    if (button == MouseButton.SECONDARY) {
                        addFlag(finalA);
                        view.playSound("guess");
                    }
                });
            } else {
                view.mineGridGroup.getChildren().get(a).setOnMouseClicked(event -> {
                    MouseButton button = event.getButton();
                    if (button == MouseButton.PRIMARY) {
                        revealTiles(finalA);
                        view.playSound("clickmine");
                    }
                    if (button == MouseButton.SECONDARY) {
                        addFlag(finalA);
                        view.playSound("guess");
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
                    modelGenerateTiles(finalA);
                    revealTiles(finalA);
                    view.playSound("clickfirstmine");
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

        view.revealTile(index, getAdjBombs(wipeTiles, index));
        if (getAdjBombs(wipeTiles, index) == 0) {
            for (int a = 0; a < wipeTiles.size(); a++) {
                if (containsValue(bombs, wipeTiles.get(a)) == false && revealedTiles.containsValue(wipeTiles.get(a)) == false) {
                    revealedTiles.put(revealedTiles.size(), wipeTiles.get(a));

                    if (getAdjBombs(wipeTiles, wipeTiles.get(a)) == 0)
                        revealTiles(wipeTiles.get(a));
                }
            }
        }

    }

    public int getAdjBombs(ArrayList tiles, int index) {
        ArrayList adjTiles = new ArrayList(tiles);
        for (int a = 0; a < adjTiles.size(); a++) {
            if (adjTiles.get(a).equals(index))
                adjTiles.remove(a);
        }

        int adjBombInt = 0;
        for (int a = 0; a < adjTiles.size(); a++) {
            if (containsValue(bombs, (Integer) adjTiles.get(a)))
                adjBombInt++;
        }

        return adjBombInt;
    }

    public void addFlag(int index) {
        if (containsValue(flags, index) == false) {
            flags.add(index);
            view.addFlag(index, flags.size());

            view.mineGridGroup.getChildren().get(index).setOnMouseClicked(event -> {
                MouseButton button = event.getButton();
                if (button == MouseButton.SECONDARY) {
                    removeFlag(index);
                }
            });
            if (sortCheck(flags, bombs) == true) {
                gameOver("WIN", index);
            }
        }
    }

    public void removeFlag(int index) {
        if (containsValue(flags, index) == true) {
            out.println(flags);
            for (int a = 0; a < flags.size(); a++) {
                if (flags.get(a).equals(index)) {
                    flags.remove(a);
                }
            }
            view.removeFlag(index, flags.size());

            view.mineGridGroup.getChildren().get(index).setOnMouseClicked(event -> {
                MouseButton button = event.getButton();
                if (button == MouseButton.SECONDARY) {
                    addFlag(index);
                }
                if (button == MouseButton.PRIMARY) {
                    if (containsValue(bombs, index))
                        gameOver("LOSE", index);
                    else
                        revealTiles(index);
                }
            });
        }
    }

    public boolean sortCheck(ArrayList arrayList1, ArrayList arrayList2) {
        Collections.sort(arrayList1);
        Collections.sort(arrayList2);

        return arrayList1.equals(arrayList2);
    }

    public boolean containsValue(ArrayList arrayList, int value) {
        for (int a = 0; a < arrayList.size(); a++) {
            if (arrayList.get(a).equals(value))
                return true;
        }
        return false;
    }
}