package MAINesweeper;

import static java.lang.System.*;

import javafx.application.Application;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class mainesweeperCtrl extends Application {
    mainesweeperModel model = new mainesweeperModel();

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        model.start(stage);

        stage.addEventHandler(KeyEvent.KEY_RELEASED, (key) -> {
            model.modelGreetScreen();
            /*
            if (model.gameActive == true) {
                model.modelGameOver("LOSE");
                out.println("game ended");
            }
            else
            {
                model.modelStartGame();
                out.println("game started");
            }
            */
            if(model.gameActive != true){
                model.modelStartGame();
                out.println("game started");
            }
        });
    }
}