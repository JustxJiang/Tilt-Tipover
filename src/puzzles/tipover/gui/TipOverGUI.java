package puzzles.tipover.gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import puzzles.common.Observer;
import puzzles.tipover.model.TipOverModel;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class TipOverGUI extends Application implements Observer<TipOverModel, String> {

    public void init() {
        String filename = getParameters().getRaw().get(0);
    }

    @Override
    public void start(Stage stage) throws Exception {

        stage.show();
    }

    @Override
    public void update(TipOverModel tipOverModel, String message) {
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TipOverGUI filename");
            System.exit(0);
        } else {
            Application.launch(args);
        }
    }
}
