package MAINesweeper;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.System.out;

public class mainesweeperView extends Application {
    Pane main;
    StackPane rootPane;
    Scene scene;

    MediaPlayer titleScreenPlayer;

    Text disclaimer = new Text(20, 760, "disclaimer: turn your volume\ndown to prevent permanent\near damage (seriously; don't sue me)");
    Text greetMAIN = new Text(100, 250, "MAIN");
    Text greetSweeper = new Text(430, 250, "-esweeper");
    Text pressAKey = new Text(300, 650, "(press any key)");
    FadeTransition fadeInPressAKey;
    FadeTransition fadeOutPressAKey;
    int greenDarkenCounter;
    Group startGreeting = new Group(disclaimer, pressAKey, greetSweeper, greetMAIN);
    Timeline greetTransitionScrollAnim;

    Text time = new Text(100, 125, "TIME: ");
    Text score = new Text(100, 150, "SCORE: ");

    Text loseText = new Text(85, 300, "==G A M E==O V E R==");
    Group lose = new Group(time, score, loseText);

    Text winText = new Text(120, 400, "MAINSWEEPED");
    Group win = new Group(time, score, winText);

    Text flagCounter = new Text(400, 60, ": 40");
    ImageView flagIcon = new ImageView();
    Group flagGroup = new Group(flagCounter, flagIcon);

    Group mineGridGroup;
    int diagonal;
    int gridSize;

    public void setGridSize(int size) {
        gridSize = size;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        mineGridGroup = new Group();
        mineGridGroup.setVisible(false);

        Image flagImage = new Image(new File("src\\MAINesweeper\\img\\mine\\bluff.png").toURI().toString());
        flagIcon.setImage(flagImage);
        flagCounter.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 40));
        flagCounter.setText(": 40");
        flagIcon.setScaleX(.15);//.07
        flagIcon.setScaleY(.15);//.07
        flagIcon.setLayoutX(100);//-100
        flagIcon.setLayoutY(-215);//-440

        main = new Pane();
        rootPane = new StackPane(main);

        rootPane.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                main.setScaleX(newValue.doubleValue() / 800.0);
                main.setScaleY(newValue.doubleValue() / 800.0);
            }
        });
        rootPane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                main.setScaleX(newValue.doubleValue() / 800.0);
                main.setScaleY(newValue.doubleValue() / 800.0);
            }
        });

        scene = new Scene(rootPane, 800/* * scale*/, 800/* * scale*/);    //450,600
        stage.setTitle("-MAN-sweeper");
        stage.setScene(scene);

        stage.show();
    }

    public void viewGreetScreen() {
        playSound("titlemusic");

        greetMAIN.setX(main.getScaleX() * 100);
        greetSweeper.setX(main.getScaleX() * 430);
        pressAKey.setX(main.getScaleX() * 300);
        greetMAIN.setY(main.getScaleY() * 250);
        greetSweeper.setY(main.getScaleY() * 250);
        pressAKey.setY(main.getScaleY() * 650);

        disclaimer.setFont(Font.font("verdana", FontWeight.EXTRA_LIGHT, FontPosture.ITALIC, 10));
        disclaimer.setFill(Color.rgb(100, 100, 100));

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

        if (main.getChildren().contains(startGreeting))
            main.getChildren().remove(startGreeting);
        main.getChildren().add(startGreeting);
    }

    public void viewTitleTransition() {
        playSound("titleboom");

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
        greetTransitionScrollAnim = new Timeline(new KeyFrame(Duration.millis(1), greetTransitionScrollEvent));
        greetTransitionScrollAnim.setCycleCount(1000);

        keyPressedAnim.setOnFinished(event -> {
            greetTransitionScrollAnim.play();
        });
    }

    public void gameOverStart(String winOrLose, int bombClicked, ArrayList bombArray) {
        ArrayList bombs = new ArrayList(bombArray);

        Random rnd = new Random();
        EventHandler<ActionEvent> hissEvent = event -> {
            revealTile(bombClicked, -1 - rnd.nextInt(3));
        };
        Timeline hiss = new Timeline(new KeyFrame(Duration.millis(10), hissEvent));
        hiss.setCycleCount(1000);
        hiss.play();

        for(int a = 0; a < )

        hiss.setOnFinished(event -> {
            viewGameOver(winOrLose);
        });
    }

    public void viewGameOver(String winOrLose) {
        if (main.getChildren().contains(lose))
            main.getChildren().remove(lose);
        main.getChildren().add(lose);

        loseText.setFont(Font.font("verdana", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 50));
        if (winOrLose == "LOSE")
            lose.setVisible(true);

        if (main.getChildren().contains(win))
            main.getChildren().remove(win);
        main.getChildren().add(win);

        winText.setFont(Font.font("verdana", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 75));
        if (winOrLose == "WIN")
            win.setVisible(true);

        mineGridGroup.getChildren().clear();
        mineGridGroup.setVisible(false);
        flagGroup.setVisible(false);
    }

    public void viewStartGame() {
        if (main.getChildren().contains(mineGridGroup))
            main.getChildren().remove(mineGridGroup);
        main.getChildren().add(mineGridGroup);

        if (main.getChildren().contains(flagGroup))
            main.getChildren().remove(flagGroup);
        main.getChildren().add(flagGroup);
        flagGroup.setVisible(false);

        lose.setVisible(false);
        win.setVisible(false);
        startGreeting.setVisible(false);

        mineGridGroup.setVisible(true);
        for (int a = 0; a < mineGridGroup.getChildren().size(); a++) {
            mineGridGroup.getChildren().get(a).setVisible(false);
        }
        diagonal = 1;

        mineGridGroup.setLayoutX(-130);
        mineGridGroup.setLayoutY(-130);

        EventHandler<ActionEvent> gridGenerateEvent = e ->
        {
            if (diagonal > gridSize) {
                for (int a = 0; a < gridSize * 2 - diagonal; a++) {
                    FadeTransition fadeIn = new FadeTransition(Duration.millis(500), mineGridGroup.getChildren().get(((diagonal - (gridSize - 1)) * gridSize) + (a * (gridSize - 1)) - 1));
                    fadeIn.setFromValue(0);
                    fadeIn.setToValue(1);
                    fadeIn.play();
                    mineGridGroup.getChildren().get(((diagonal - (gridSize - 1)) * gridSize) + (a * (gridSize - 1)) - 1).setVisible(true);
                    if (a % 6 == 0)
                        playSound("generate");
                }
            } else
                for (int a = 0; a < diagonal; a++) {
                    FadeTransition fadeIn = new FadeTransition(Duration.millis(500), mineGridGroup.getChildren().get((diagonal) + (a * (gridSize - 1)) - 1));
                    fadeIn.setFromValue(0);
                    fadeIn.setToValue(1);
                    fadeIn.play();
                    mineGridGroup.getChildren().get((diagonal) + (a * (gridSize - 1)) - 1).setVisible(true);
                    if (a % 6 == 0)
                        playSound("generate");
                }
            diagonal++;
        };

        Timeline gridGenerateAnim = new Timeline(new KeyFrame(Duration.millis(40), gridGenerateEvent));
        gridGenerateAnim.setCycleCount(gridSize * 2);
        gridGenerateAnim.play();
        gridGenerateAnim.setOnFinished(event -> {
            flagGroup.setVisible(true);
        });
    }

    public void viewGenerateTiles() {
        int yOffset = 0;
        int xOffset = 0;
        int idTracker = 0;
        double mineheight = 0;
        Image mineImage = new Image(new File("src\\MAINesweeper\\img\\mine\\minetile.png").toURI().toString());
        for (int a = 0; a < gridSize; a++) {
            for (int b = 0; b < gridSize; b++) {
                ImageView mineImageView = new ImageView(mineImage);
                mineImageView.setId(idTracker + "");

                mineImageView.setScaleX(0.075);
                mineImageView.setScaleY(0.075);
                mineImageView.setLayoutX(xOffset);
                mineImageView.setLayoutY(yOffset);
                xOffset += mineImage.getWidth() * mineImageView.getScaleX();

                mineGridGroup.getChildren().add(mineImageView);
                mineheight = mineImage.getHeight() * mineImageView.getScaleY();
                idTracker++;
            }
            yOffset += mineheight;
            xOffset = 0;
        }
    }

    public void revealTile(int index, int number) {
        Random rnd = new Random();
        Image mineImage = new Image(new File("src\\MAINesweeper\\img\\mine\\" + number + ".png").toURI().toString());

        if (number == -1)
            mineImage = new Image(new File("src\\MAINesweeper\\img\\mine\\boom0.png").toURI().toString());
        if (number == -2)
            mineImage = new Image(new File("src\\MAINesweeper\\img\\mine\\boom1.png").toURI().toString());
        if (number == -3)
            mineImage = new Image(new File("src\\MAINesweeper\\img\\mine\\boom2.png").toURI().toString());
        if (number == -4)
            mineImage = new Image(new File("src\\MAINesweeper\\img\\mine\\boom3.png").toURI().toString());

        if (number == 10)
            mineImage = new Image(new File("src\\MAINesweeper\\img\\mine\\thonk\\thonk0.png").toURI().toString());
        if (number == 11)
            mineImage = new Image(new File("src\\MAINesweeper\\img\\mine\\minetile.png").toURI().toString());

        ImageView mineImageView = new ImageView(mineImage);

        mineImageView.setLayoutX(mineGridGroup.getChildren().get(index).getLayoutX());
        mineImageView.setLayoutY(mineGridGroup.getChildren().get(index).getLayoutY());
        mineImageView.setScaleX(mineGridGroup.getChildren().get(index).getScaleX());
        mineImageView.setScaleY(mineGridGroup.getChildren().get(index).getScaleY());

        mineGridGroup.getChildren().set(index, mineImageView);
        mineGridGroup.getChildren().get(index).setId(mineGridGroup.getChildren().get(index).getId());
    }

    public void addFlag(int index, int flags) {
        flagIcon.setImage(new Image(new File("src\\MAINesweeper\\img\\mine\\thonk\\thonkIcon.png").toURI().toString()));
        flagIcon.setScaleX(.07);//.07
        flagIcon.setScaleY(.07);//.07
        flagIcon.setLayoutX(-100);//-100
        flagIcon.setLayoutY(-440);//-440

        revealTile(index, 10);
        flagCounter.setText(": " + (40 - flags));
    }

    public void removeFlag(int index, int flags) {
        revealTile(index, 11);
        flagCounter.setText(": " + (40 - flags));
    }

    public void playSound(String soundName) {
        try {
        } catch (Exception noStreakSound) {
            out.println("Error stopping sound: no sound exists yet");
        }

        String path = "src/MAINesweeper/snd/";
        int randomSndDir = 0;

        if (soundName == "clickmine") {
            path += "misc/clickmine.wav";
        }
        if (soundName == "clickfirstmine") {
            path += "misc/clickfirstmine.wav";
        }
        if (soundName == "guess") {
            path += "misc/guess.wav";
        }
        if (soundName == "win") {
            path += "win/win0.wav";
        }
        if (soundName == "generate") {
            path += "generate/generate1.wav";
        }
        if (soundName == "titlemusic") {
            path += "titlescreen/music/titlemusic0.wav";
        }
        if (soundName == "titleboom") {
            path += "titlescreen/boom.wav";
        }

        Media sound = new Media(new File(path).toURI().toString());
        MediaPlayer soundPlayer = new MediaPlayer(sound);
        soundPlayer.setStartTime(Duration.ZERO);

        if (soundName == "titlemusic") {
            soundPlayer.setStopTime(sound.getDuration());
            soundPlayer.setAutoPlay(true);
            soundPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        }
        soundPlayer.play();
    }
}