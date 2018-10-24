package MAINesweeper;

import static java.lang.System.*;

import javafx.application.Application;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class mainesweeperCtrl extends Application {
    mainesweeperModel model = new mainesweeperModel();
    boolean justOpened;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        model.start(stage);
        justOpened = true;

        stage.addEventHandler(KeyEvent.KEY_RELEASED, (key) -> {
            if(model.gameActive != true && justOpened == false){
                model.modelStartGame();
            }
            if(model.gameActive != true && justOpened == true && model.view.greenDarkenCounter==0){
                justOpened = false;
                model.modelTitleTransition();
            }
        });
    }
}