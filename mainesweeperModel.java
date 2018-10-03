package MAINesweeper;

import static java.lang.System.*;
import javafx.application.Application;
import javafx.stage.Stage;

public class mainesweeperModel extends Application{
    boolean win;
    mainesweeperView view = new mainesweeperView();
    public static void main(String[]args){
        launch(args);
    }
    public void start(Stage stage) throws Exception {
        view.start(stage);
    }
    public void modelWin(){
        win = true;
        out.println("you won in the model too what");
        view.viewWin();
    }
}