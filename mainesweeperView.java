package MAINesweeper;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashMap;
import java.util.Random;

import static java.lang.System.out;

public class mainesweeperView extends Application {
    Text winText = new Text(50,50,"You win woohoo");

    MediaPlayer soundPlayer;
    Media sound;
    public static void main(String[]args){
        launch(args);
    }
    public void start(Stage stage) throws Exception {
        winText.setFont(Font.font("verdana", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 20));
        winText.setVisible(false);

        Pane main = new Pane(winText);
        //main.setScaleX();
        //main.setScaleY();
        StackPane rootPane = new StackPane(main);
        Scene scene = new Scene(rootPane, 800/* * scale*/, 800/* * scale*/);    //450,600
        stage.setTitle("-MAN-sweeper");
        stage.setScene(scene);
        stage.show();
    }
    public void viewWin(){
        winText.setVisible(true);
        playSound("win");
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