package puzzles.tilt.gui;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import puzzles.common.Observer;
import puzzles.tilt.model.TiltModel;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class TiltGUI extends Application implements Observer<TiltModel, String> {
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";
    private TiltModel model;
    private Button[][] buttons;
    // for demonstration purposes
    private Image greenDisk = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"green.png"));

    public void init() {
        String filename = getParameters().getRaw().get(0);
    }
    public TiltGUI() {
        model = new TiltModel()Model();
        model.addObserver(this);
        buttons = new Button[model.getDimension()][model.getDimension()];
    }

    @Override
    public void start(Stage stage) throws Exception {

        BorderPane borderPane = new BorderPane();
        FlowPane top = new FlowPane();
        borderPane.setTop(top);

        BorderPane middle = new BorderPane();
        middle.setTop(new Button("^"));
        middle.setBottom(new Button("v"));
        middle.setLeft(new Button("<"));
        middle.setRight(new Button(">"));

        GridPane center = new GridPane();
        for (int r = 0; r < model.getDimension(); r++){
            for (int c = 0; c < model.getDimension(); c++){
                buttons[c][r] = new Button();
                buttons[c][r].setMinSize(50,50);
                buttons[c][r].setStyle("-fx-background-color: #ffffff");
                int finalC = c;
                int finalR = r;
                buttons[c][r].setOnAction(e ->{
                    model.toggleTile(finalC, finalR);
                    if(!model.gameOver()){
                        message.setText("Message: Move: " + finalC + ", " + finalR);
                    }
                });
                center.add(buttons[c][r], c, r);
            }
        }

        Button button = new Button();
        button.setGraphic(new ImageView(greenDisk));
        Scene scene = new Scene(button);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void update(TiltModel tiltModel, String message) {
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TiltGUI filename");
            System.exit(0);
        } else {
            Application.launch(args);
        }
    }
}
