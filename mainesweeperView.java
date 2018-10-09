package MAINesweeper;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.Effect;
import javafx.scene.effect.MotionBlur;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import javafx.scene.image.*;

import java.io.File;
import java.util.HashMap;
import java.util.Random;

import static java.lang.System.out;

public class mainesweeperView extends Application {

    Text greetMAIN = new Text(100, 250, "MAIN");
    Text greetSweeper = new Text(430, 250, "-esweeper");
    Text pressAKey = new Text(300, 650, "(press any key)");
    FadeTransition fadeInPressAKey;
    FadeTransition fadeOutPressAKey;
    int greenDarkenCounter;
    Group startGreetingMineSweeper = new Group(pressAKey, greetSweeper, greetMAIN);

    Group startGreeting = new Group(startGreetingMineSweeper);

    Text time = new Text(100, 125, "TIME: ");
    Text score = new Text(100, 150, "SCORE: ");

    Text loseText = new Text(100, 100, "YA LOSE M7");
    Group lose = new Group(time, score, loseText);

    MediaPlayer soundPlayer;
    Media sound;

    Group mineGridGroup;
    int diagonal;

    int gridSize;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) throws Exception {

        mineGridGroup = new Group();
        mineGridGroup.setVisible(false);

        greetMAIN.setFont(Font.font("verdana", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 100));
        EventHandler<ActionEvent> BAZAMBABOPPIT = e ->
        {
            MotionBlur mb = new MotionBlur();
            greetMAIN.setEffect(mb);
            greetMAIN.setStrokeWidth(2);
            greetMAIN.setStroke(Color.rgb((int) (Math.random() * 100) + 155, 0, 0));
            greetMAIN.setTranslateX((Math.random() * 100) - 50);
            greetMAIN.setTranslateY((Math.random() * 100) - 50);
            greetMAIN.setRotate(Math.random() * 90 - 45);
            mb.setRadius(Math.random() * 100 - 10);
            mb.setAngle(Math.random() * 60 - 30);
        };
        Timeline BAZAMBABOPPED = new Timeline(new KeyFrame(Duration.millis(1), BAZAMBABOPPIT));
        BAZAMBABOPPED.setCycleCount(Timeline.INDEFINITE);
        BAZAMBABOPPED.play();

        greetSweeper.setFont(Font.font("verdana", FontWeight.EXTRA_LIGHT, FontPosture.ITALIC, 60));
        pressAKey.setFont(Font.font("verdana", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 30));
        fadeInPressAKey = new FadeTransition(Duration.millis(500), pressAKey);
        fadeInPressAKey.setFromValue(0.5);
        fadeInPressAKey.setToValue(1);
        fadeInPressAKey.play();
        fadeOutPressAKey = new FadeTransition(Duration.millis(500), pressAKey);
        fadeOutPressAKey.setFromValue(1);
        fadeOutPressAKey.setToValue(0.5);
        fadeOutPressAKey.play();
        fadeInPressAKey.setOnFinished(event -> {
            fadeOutPressAKey.play();
        });
        ;
        fadeOutPressAKey.setOnFinished(event -> {
            fadeInPressAKey.play();
        });
        ;

        loseText.setFont(Font.font("verdana", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 40));
        lose.setVisible(false);

        Pane main = new Pane(startGreeting, lose, mineGridGroup);
        //main.setScaleX();
        //main.setScaleY();
        StackPane rootPane = new StackPane(main);
        Scene scene = new Scene(rootPane, 800/* * scale*/, 800/* * scale*/);    //450,600
        stage.setTitle("-MAN-sweeper");
        stage.setScene(scene);
        stage.show();
    }

    public void viewGreetScreen() {
        //playSound("win");
    }

    public void viewGreetTranstition() {
        pressAKey.setOpacity(1.0);
        fadeOutPressAKey.stop();
        fadeInPressAKey.stop();
        Timeline keyPressedAnim = new Timeline();
        keyPressedAnim.setCycleCount(1000);

        greenDarkenCounter = 0;
        EventHandler<ActionEvent> keyPressedEvent = e ->
        {
            pressAKey.setFill(Color.rgb(0, 255 - greenDarkenCounter / 10, 0));
            greenDarkenCounter++;
        };
        keyPressedAnim.getKeyFrames().add(new KeyFrame(Duration.millis(1), keyPressedEvent));
        keyPressedAnim.play();

        MotionBlur transitionMb = new MotionBlur();
        transitionMb.setAngle(90);
        EventHandler<ActionEvent> greetTransitionScrollEvent = e ->
        {
            transitionMb.setRadius(Math.random() * 100);
            startGreeting.setEffect(transitionMb);
            startGreeting.setTranslateY(startGreeting.getTranslateY() + 5);
        };
        Timeline greetTransitionScrollAnim = new Timeline(new KeyFrame(Duration.millis(1), greetTransitionScrollEvent));
        greetTransitionScrollAnim.setCycleCount(1000);

        keyPressedAnim.setOnFinished(event -> {
            greetTransitionScrollAnim.play();
        });
        greetTransitionScrollAnim.setOnFinished(event -> {
            viewStartGame();
        });
    }

    public void viewGameOver(String winOrLose) {
        if (winOrLose == "LOSE")
            lose.setVisible(true);
        mineGridGroup.setVisible(false);
    }

    public void viewStartGame() {
        int yOffset = 0;
        int xOffset = 0;
        double mineheight = 0;
        for (int a = 0; a < 16; a++) {
            for (int b = 0; b < 16; b++) {
                Rectangle mine = new Rectangle(40, 40);
                mine.setFill(Color.rgb(110, 110, 110));
                mine.setX(80 + xOffset);
                mine.setId(a + "," + b);
                mine.setStroke(Color.rgb(0, 0, 0));
                mine.setStrokeWidth(mine.getWidth() / 5);
                xOffset += mine.getWidth();
                mine.setY(100 + yOffset);
                mineGridGroup.getChildren().add(mine);
                mineheight = mine.getHeight();
            }
            yOffset += mineheight;
            xOffset = 0;
        }

        lose.setVisible(false);
        startGreeting.setVisible(false);
        mineGridGroup.setVisible(true);
        for (int a = 0; a < mineGridGroup.getChildren().size(); a++) {
            mineGridGroup.getChildren().get(a).setVisible(false);
        }
        diagonal = 1;

        EventHandler<ActionEvent> gridGenerateEvent = e ->
        {
            if (diagonal > 16) {
                for (int a = 0; a < 32 - diagonal; a++) {
                    mineGridGroup.getChildren().get(((diagonal - 15) * 16) + (a * 15) - 1).setVisible(true);
                    FadeTransition fadeIn = new FadeTransition(Duration.millis(500), mineGridGroup.getChildren().get(((diagonal - 15) * 16) + (a * 15) - 1));
                    fadeIn.setFromValue(0);
                    fadeIn.setToValue(1);
                    fadeIn.play();
                    if (a % 6 == 0)
                        playSound("generate");
                }
            } else
                for (int a = 0; a < diagonal; a++) {
                    mineGridGroup.getChildren().get((diagonal) + (a * 15) - 1).setVisible(true);
                    FadeTransition fadeIn = new FadeTransition(Duration.millis(500), mineGridGroup.getChildren().get((diagonal) + (a * 15) - 1));
                    fadeIn.setFromValue(0);
                    fadeIn.setToValue(1);
                    fadeIn.play();
                    if (a % 6 == 0)
                        playSound("generate");
                }
            diagonal++;
        };
        Timeline gridGenerateAnim = new Timeline(new KeyFrame(Duration.millis(40), gridGenerateEvent));
        gridGenerateAnim.setCycleCount(32);
        gridGenerateAnim.play();
        gridGenerateAnim.setOnFinished(event -> {
            mineGridGroup.setVisible(true);
        });
    }

    public void setGridSize(int size) {
        gridSize = size;
    }

    public void playSound(String soundName) {
        try {
        } catch (Exception noStreakSound) {
            out.println("Error stopping sound: no sound exists yet");
        }

        String path = "src/MAINesweeper/snd/";
        Random rnd = new Random();
        int randomSndDir = 0;

        if (soundName == "win") {
            path += "win/win0.wav";
        }
        if (soundName == "generate") {
            path += "generate/generate1.wav";
        }

        Media sound = new Media(new File(path).toURI().toString());
        if (soundName != "STREAK") {
            MediaPlayer soundPlayer = new MediaPlayer(sound);
            soundPlayer.play();
        }
    }

    public void stopSound() {
        try {
            soundPlayer.stop();
        } catch (Exception noSoundExists) {
            out.println("Error Stopping Sound: No sound exists yet");
        }
    }
}