package MAINesweeper;

import static java.lang.System.*;
import javafx.application.Application;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class mainesweeperCtrl extends Application {
    mainesweeperModel model = new mainesweeperModel();
    public static void main(String[]args){
        launch(args);
    }
    public void start(Stage stage) throws Exception {
        model.start(stage);

        stage.addEventHandler(KeyEvent.KEY_RELEASED, (key) -> {
            if(key.getCode() == KeyCode.W){
                out.println("you win in the view");
                model.modelWin();
            }
        });
    }
}