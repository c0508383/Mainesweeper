package MAINesweeper;

import javafx.application.Application;
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
            if(model.gameActive != true && justOpened == false && model.view.canPressKey==true){
                model.modelStartGame();
            }
            if(model.gameActive != true && justOpened == true && model.view.canPressKey==true){
                justOpened = false;
                model.modelTitleTransition();
            }
        });
    }
}