package MAINesweeper;

import static java.lang.System.*;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;
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

    public void revealTile(int index){
        int surroundingBombs = getAdjBombs(index);

        if(surroundingBombs==0){
            view.revealTile(index,0);
            tileWipe(index);
        }
        else
            view.revealTile(index,surroundingBombs);
        //else run a method to change the tile's image corresponding to surroundingBombs
    }

    public void tileWipe(int index){
        ArrayList adjTiles = new ArrayList();
        adjTiles.add(index-17);//top-left
        adjTiles.add(index-16);//top
        adjTiles.add(index-15);//top-right
        adjTiles.add(index+15);//bot-left
        adjTiles.add(index+16);//bot
        adjTiles.add(index+17);//bot-right

        for(int a = 0; a < adjTiles.size(); a++){
            if(getAdjBombs(((int)adjTiles.get(a)))==0) {
                view.revealTile(a,0);
                tileWipe(a);
            }
            else
                view.revealTile(a,getAdjBombs(a));
        }
    }

    public int getAdjBombs(int index){
        int adjBombs = 0;

        if(bombs.containsValue(index-17))
            adjBombs++;
        if(bombs.containsValue(index-16))
            adjBombs++;
        if(bombs.containsValue(index-15))
            adjBombs++;

        if(bombs.containsValue(index+15))
            adjBombs++;
        if(bombs.containsValue(index+16))
            adjBombs++;
        if(bombs.containsValue(index+17))
            adjBombs++;

        return adjBombs;
    }
}