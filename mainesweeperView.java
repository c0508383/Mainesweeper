package MAINesweeper;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.MotionBlur;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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
    Text greetMAIN = new Text(230,150,"MAIN");
    Text greetSweeper = new Text(430,150,"-esweeper");
    Group startGreetingMineSweeper = new Group(greetSweeper,greetMAIN);

    Group startGreeting = new Group(startGreetingMineSweeper);

    Text time = new Text(100,125,"TIME: ");
    Text score = new Text(100,150,"SCORE: ");

    Text loseText = new Text(100,100,"YA LOSE M7");
    Group lose = new Group(time, score, loseText);

    MediaPlayer soundPlayer;
    Media sound;

    Image[][] mineGrid;

    public static void main(String[]args){
        launch(args);
    }
    public void start(Stage stage) throws Exception {

        greetMAIN.setFont(Font.font("verdana", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 60));
        greetSweeper.setFont(Font.font("verdana", FontWeight.EXTRA_LIGHT, FontPosture.ITALIC, 30));

        loseText.setFont(Font.font("verdana", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 40));
        lose.setVisible(false);

        Pane main = new Pane(startGreeting,lose);
        //main.setScaleX();
        //main.setScaleY();
        StackPane rootPane = new StackPane(main);
        Scene scene = new Scene(rootPane, 800/* * scale*/, 800/* * scale*/);    //450,600
        stage.setTitle("-MAN-sweeper");
        stage.setScene(scene);
        stage.show();

        mineGrid = new Image[16][16];
    }
    public void viewGreetScreen(){
        playSound("win");
    }
    public void viewGreetTranstition(){
        MotionBlur transitionMb = new MotionBlur();
        transitionMb.setAngle(90);

        EventHandler<ActionEvent> greetTransitionEvent = e ->
        {
            transitionMb.setRadius(Math.random()*100);
            startGreeting.setEffect(transitionMb);
            startGreeting.setTranslateY(startGreeting.getTranslateY()+5);
        };
        Timeline greetTransitionAnim = new Timeline(new KeyFrame(Duration.millis(1), greetTransitionEvent));
        greetTransitionAnim.setCycleCount(1000);
        greetTransitionAnim.play();
        greetTransitionAnim.setOnFinished(event -> {
            viewStartGame();
        });
    }
    public void viewGameOver(String winOrLose){
        if(winOrLose=="LOSE")
            lose.setVisible(true);
    }
    public void viewStartGame(){
        lose.setVisible(false);
        startGreeting.setVisible(false);
    }
    public void playSound(String soundName) {
        try {
        } catch (Exception noStreakSound) {
            out.println("Error stopping sound: no sound exists yet");
        }

        String path = "C:\\Users\\khscs003\\Desktop\\CS2 Programs\\src\\MAINesweeper\\snd\\";
        Random rnd = new Random();
        int randomSndDir = 0;

        if(soundName=="win"){
            path+="win\\win0.wav";
        }

        sound = new Media(new File(path).toURI().toString());
        if (soundName != "STREAK") {
            soundPlayer = new MediaPlayer(sound);
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