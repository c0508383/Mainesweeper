package MAINesweeper;

import javafx.animation.Animation;
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
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.out;

public class mainesweeperView extends Application {
    Pane main;
    StackPane rootPane;
    Scene scene;
    Random rnd = new Random();
    boolean canPressKey;

    Media titleMusic = new Media(new File("src/MAINesweeper/snd/titlescreen/music/titlemusic0.wav").toURI().toString());
    MediaPlayer titleScreenPlayer = new MediaPlayer(titleMusic);
    MediaPlayer soundtrack = new MediaPlayer(titleMusic);

    Text disclaimer = new Text(20, 760, "disclaimer: turn your volume\ndown to prevent permanent\near damage (seriously; don't sue me)");
    Text greetMAIN = new Text(100, 250, "MAIN");
    Text greetSweeper = new Text(430, 250, "-esweeper");
    Text pressAKey = new Text(300, 650, "(press any key)");
    FadeTransition fadeInPressAKey;
    FadeTransition fadeOutPressAKey;
    int greenDarkenCounter;
    Group startGreeting = new Group(disclaimer, pressAKey, greetSweeper, greetMAIN);
    Timeline greetTransitionScrollAnim;

    int timeInt;
    Timeline timer = new Timeline();
    Text timeCounterText = new Text(125, 50, "Time: ");
    Text timeCounterTime = new Text(190, 50, "0");
    Group timeDisplay = new Group(timeCounterText, timeCounterTime);

    Text loseText = new Text(85, 300, "==G A M E==O V E R==");
    Group lose = new Group(loseText);

    Text winTime = new Text(280, 400, "TIME: ");
    Text winText = new Text(120, 400, "MAINSWEEPED");
    Group win = new Group(winTime, winText);

    Text flagCounter = new Text(400, 60, ": 40");
    ImageView flagIcon = new ImageView();
    Group flagGroup = new Group(flagCounter, flagIcon);

    Group mineGridGroup = new Group();
    int diagonal;
    int gridSize;


    Image mineImage0 = new Image(new File("src\\MAINesweeper\\img\\mine\\" + 0 + ".png").toURI().toString());
    Image mineImage1 = new Image(new File("src\\MAINesweeper\\img\\mine\\" + 1 + ".png").toURI().toString());
    Image mineImage2 = new Image(new File("src\\MAINesweeper\\img\\mine\\" + 2 + ".png").toURI().toString());
    Image mineImage3 = new Image(new File("src\\MAINesweeper\\img\\mine\\" + 3 + ".png").toURI().toString());
    Image mineImage4 = new Image(new File("src\\MAINesweeper\\img\\mine\\" + 4 + ".png").toURI().toString());
    Image mineImage5 = new Image(new File("src\\MAINesweeper\\img\\mine\\" + 5 + ".png").toURI().toString());
    Image mineImage6 = new Image(new File("src\\MAINesweeper\\img\\mine\\" + 6 + ".png").toURI().toString());
    Image mineImage7 = new Image(new File("src\\MAINesweeper\\img\\mine\\" + 7 + ".png").toURI().toString());
    Image mineImage8 = new Image(new File("src\\MAINesweeper\\img\\mine\\" + 8 + ".png").toURI().toString());
    Image mineImage9 = new Image(new File("src\\MAINesweeper\\img\\mine\\" + 9 + ".png").toURI().toString());

    Image boom0 = new Image(new File("src\\MAINesweeper\\img\\mine\\boom0.png").toURI().toString());
    Image boom1 = new Image(new File("src\\MAINesweeper\\img\\mine\\boom1.png").toURI().toString());
    Image boom2 = new Image(new File("src\\MAINesweeper\\img\\mine\\boom2.png").toURI().toString());
    Image boom3 = new Image(new File("src\\MAINesweeper\\img\\mine\\boom3.png").toURI().toString());

    Image explosion0 = new Image(new File("src\\MAINesweeper\\img\\mine\\explosion\\explosion0.gif").toURI().toString());
    Image explosion1 = new Image(new File("src\\MAINesweeper\\img\\mine\\explosion\\explosion1.gif").toURI().toString());
    Image explosion2 = new Image(new File("src\\MAINesweeper\\img\\mine\\explosion\\explosion2.gif").toURI().toString());
    Image explosion3 = new Image(new File("src\\MAINesweeper\\img\\mine\\explosion\\explosion3.gif").toURI().toString());
    Image explosion4 = new Image(new File("src\\MAINesweeper\\img\\mine\\explosion\\explosion4.gif").toURI().toString());
    Image explosion5 = new Image(new File("src\\MAINesweeper\\img\\mine\\explosion\\explosion5.gif").toURI().toString());
    Image explosion6 = new Image(new File("src\\MAINesweeper\\img\\mine\\explosion\\explosion6.gif").toURI().toString());
    Group explosions = new Group();

    Media loseLaughSound = new Media(new File("src/MAINesweeper/snd/lose/lose0.wav").toURI().toString());
    MediaPlayer loseLaughPlayer = new MediaPlayer(loseLaughSound);

    Image flag = new Image(new File("src\\MAINesweeper\\img\\mine\\thonk\\thonk0.png").toURI().toString());
    Image mineTile = new Image(new File("src\\MAINesweeper\\img\\mine\\minetile.png").toURI().toString());

    public void setGridSize(int size) {
        gridSize = size;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        Image flagImage = new Image(new File("src\\MAINesweeper\\img\\mine\\bluff.png").toURI().toString());
        flagIcon.setImage(flagImage);
        flagCounter.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 40));
        flagCounter.setText(": 40");
        flagIcon.setScaleX(.15);//.07
        flagIcon.setScaleY(.15);//.07
        flagIcon.setLayoutX(100);//-100
        flagIcon.setLayoutY(-215);//-440

        timeCounterText.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));
        timeCounterTime.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 20));

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
        canPressKey = true;
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
        canPressKey = false;
        titleScreenPlayer.stop();
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
            playSound("soundtrack");
        });
    }

    public void loseStart(int bombClicked, ArrayList bombArray) {
        timer.stop();

        FadeTransition shellShockMine = new FadeTransition(new Duration(3000), mineGridGroup);
        shellShockMine.setFromValue(1);
        shellShockMine.setToValue(0);
        FadeTransition shellShockExplosions = new FadeTransition(new Duration(3000), explosions);
        shellShockExplosions.setFromValue(1);
        shellShockExplosions.setToValue(0);
        FadeTransition shellShockFlag = new FadeTransition(new Duration(3000), flagGroup);
        shellShockFlag.setFromValue(1);
        shellShockFlag.setToValue(0);
        FadeTransition shellShockTimer = new FadeTransition(new Duration(3000), timeDisplay);
        shellShockTimer.setFromValue(1);
        shellShockTimer.setToValue(0);

        if (rnd.nextInt(9) == 1)
            playSound("loselaugh");
        else
            playSound("explosionstart");

        EventHandler<ActionEvent> startExplosionEvent = event -> {
            revealTile(bombClicked, -1 - rnd.nextInt(3));
        };
        Timeline startExplosion = new Timeline(new KeyFrame(Duration.millis(10), startExplosionEvent));
        startExplosion.setCycleCount(100);
        startExplosion.play();

        EventHandler<ActionEvent> chainExplosionEvent = event -> {
            int randomExplosion = rnd.nextInt(6);
            ImageView explosion = new ImageView();
            if (randomExplosion == 0)
                explosion = new ImageView(explosion0);
            if (randomExplosion == 1)
                explosion = new ImageView(explosion1);
            if (randomExplosion == 2)
                explosion = new ImageView(explosion2);
            if (randomExplosion == 3)
                explosion = new ImageView(explosion3);
            if (randomExplosion == 4)
                explosion = new ImageView(explosion4);
            if (randomExplosion == 5)
                explosion = new ImageView(explosion5);
            if (randomExplosion == 6)
                explosion = new ImageView(explosion6);

            explosion.setLayoutX(0 + rnd.nextInt(600));
            explosion.setLayoutY(0 + rnd.nextInt(600));

            if (mineGridGroup.getChildren().size() > 0) {
                mineGridGroup.getChildren().get(rnd.nextInt(255)).setVisible(false);
                mineGridGroup.getChildren().get(rnd.nextInt(255)).setVisible(false);
                mineGridGroup.getChildren().get(rnd.nextInt(255)).setVisible(false);
                mineGridGroup.getChildren().get(rnd.nextInt(255)).setVisible(false);
                mineGridGroup.getChildren().get(rnd.nextInt(255)).setVisible(false);
            }

            explosions.getChildren().add(explosion);
            if (explosions.getChildren().size() == 5)
                explosions.getChildren().remove(0);
        };
        Timeline chainExplosion = new Timeline(new KeyFrame(Duration.millis(50), chainExplosionEvent));
        chainExplosion.setCycleCount(80);

        AtomicInteger flashTime = new AtomicInteger();
        EventHandler<ActionEvent> startExplosionEventSeveral = event -> {
            flashTime.set(flashTime.get() + 1);
            for (int a = 0; a < bombArray.size(); a++) {
                revealTile((Integer) bombArray.get(a), -1 - rnd.nextInt(3));
            }

            if (flashTime.get() == 100) {
                playSound("chainstart");
                playSound("explosion");
                main.getChildren().add(explosions);
                chainExplosion.play();

                shellShockExplosions.play();
                shellShockMine.play();
                shellShockFlag.play();
                shellShockTimer.play();
            }
            if (flashTime.get() > 100 && flashTime.get() % 20 == 0) {
                playSound("explosion");
            }
        };
        Timeline startExplosionSeveral = new Timeline(new KeyFrame(Duration.millis(10), startExplosionEventSeveral));
        startExplosionSeveral.setCycleCount(500);

        startExplosion.setOnFinished(event -> {
            playSound("severalstart");
            startExplosionSeveral.play();
        });
        startExplosionSeveral.setOnFinished(event -> {
            shellShockExplosions.setToValue(1);
            shellShockExplosions.setByValue(1);
            shellShockExplosions.play();

            shellShockTimer.setToValue(1);
            shellShockTimer.setByValue(1);
            shellShockTimer.play();

            shellShockFlag.setToValue(1);
            shellShockFlag.setByValue(1);
            shellShockFlag.play();

            shellShockMine.setToValue(1);
            shellShockMine.setByValue(1);
            shellShockMine.play();

            explosions.getChildren().clear();
            main.getChildren().remove(explosions);

            lose.setVisible(true);
            lose.setOpacity(0);
            gameOver("LOSE");
        });
    }

    public void gameOver(String winOrLose) {
        if (main.getChildren().contains(lose))
            main.getChildren().remove(lose);
        main.getChildren().add(lose);

        FadeTransition shellShockLose = new FadeTransition(new Duration(1000), lose);
        shellShockLose.setFromValue(0);
        shellShockLose.setToValue(1);

        loseText.setFont(Font.font("verdana", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 50));
        if (winOrLose == "LOSE") {
            shellShockLose.play();
            shellShockLose.setOnFinished(event -> {
                canPressKey = true;
            });
        }

        if (main.getChildren().contains(win))
            main.getChildren().remove(win);
        main.getChildren().add(win);

        winText.setFont(Font.font("verdana", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 75));
        winTime.setFont(Font.font("verdana", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 40));
        winTime.setText("Time: " + timeInt);
        if (winOrLose == "WIN")
            win.setVisible(true);

        mineGridGroup.getChildren().clear();
        mineGridGroup.setVisible(false);
        flagGroup.setVisible(false);
        timeDisplay.setVisible(false);
    }

    public void viewStartGame() {
        canPressKey = false;
        if (main.getChildren().contains(mineGridGroup))
            main.getChildren().remove(mineGridGroup);
        main.getChildren().add(mineGridGroup);

        if (main.getChildren().contains(flagGroup))
            main.getChildren().remove(flagGroup);
        main.getChildren().add(flagGroup);
        flagGroup.setVisible(false);

        if (main.getChildren().contains(timeDisplay))
            main.getChildren().remove(timeDisplay);
        main.getChildren().add(timeDisplay);
        timeDisplay.setVisible(false);

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
            timeDisplay.setVisible(true);
        });

        timeInt = 0;
        EventHandler<ActionEvent> timerEvent = e ->
        {
            timeCounterTime.setText(timeInt + "");
            timeInt++;
        };
        timer = new Timeline(new KeyFrame(Duration.millis(1000), timerEvent));
        timer.setCycleCount(Animation.INDEFINITE);
        timer.play();
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
        ImageView mineImageView = null;
        if (number == 0)
            mineImageView = new ImageView(mineImage0);
        if (number == 1)
            mineImageView = new ImageView(mineImage1);
        if (number == 2)
            mineImageView = new ImageView(mineImage2);
        if (number == 3)
            mineImageView = new ImageView(mineImage3);
        if (number == 4)
            mineImageView = new ImageView(mineImage4);
        if (number == 5)
            mineImageView = new ImageView(mineImage5);
        if (number == 6)
            mineImageView = new ImageView(mineImage6);
        if (number == 7)
            mineImageView = new ImageView(mineImage7);
        if (number == 8)
            mineImageView = new ImageView(mineImage8);
        if (number == 9)
            mineImageView = new ImageView(mineImage9);

        if (number == -1)
            mineImageView = new ImageView(boom0);
        if (number == -2)
            mineImageView = new ImageView(boom1);
        if (number == -3)
            mineImageView = new ImageView(boom2);
        if (number == -4)
            mineImageView = new ImageView(boom3);

        if (number == 10)
            mineImageView = new ImageView(flag);
        if (number == 11)
            mineImageView = new ImageView(mineTile);

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

        if (soundName == "explosionstart") {
            path += "explosion/explosionstart.wav";
        }
        if (soundName == "severalstart") {
            path += "explosion/severalstart.wav";
        }
        if (soundName == "chainstart") {
            path += "explosion/chainstart.wav";
        }
        if (soundName == "explosion") {
            path += "explosion/explosion" + rnd.nextInt(1) + ".wav";
        }
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
        if (soundName == "titleboom") {
            path += "titlescreen/boom.wav";
        }

        if (soundName == "loselaugh") {
            loseLaughPlayer.play();
        } else if (soundName == "titlemusic") {
            titleScreenPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            titleScreenPlayer.play();
        } else if (soundName == "soundtrack") {
            soundtrack = new MediaPlayer(new Media(new File("src/MAINesweeper/snd/soundtrack/track" + rnd.nextInt(24) + ".mp3").toURI().toString()));
            soundtrack.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    playSound("soundtrack");
                }
            });

            soundtrack.play();
        } else {
            Media sound = new Media(new File(path).toURI().toString());
            MediaPlayer soundPlayer = new MediaPlayer(sound);
            soundPlayer.setStartTime(Duration.ZERO);
            soundPlayer.play();
        }
    }
}