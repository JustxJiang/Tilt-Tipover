package puzzles.tilt.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.tilt.model.TiltModel;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.File;
import java.util.List;

public class TiltGUI extends Application implements Observer<TiltModel, String> {
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";
    private TiltModel model;
    private Button[][] buttons;
    // for demonstration purposes
    private Label message = new Label();
    private Image greenDisk = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"green.png"));
    private ImageView green = new ImageView(greenDisk);

    private Image blueDisk = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"blue.png"));
    private ImageView blue = new ImageView(blueDisk);

    private Image holeDisk = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"hole.png"));
    private ImageView hole = new ImageView(holeDisk);

    private Image blockDisk = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"block.png"));
    private ImageView block = new ImageView(blockDisk);



    public void init() {
        String filename = getParameters().getRaw().get(0);
        model = new TiltModel(filename);
        model.addObserver(this);
        buttons = new Button[model.getDimensions()][model.getDimensions()];
    }

    @Override
    public void start(Stage stage) throws Exception {

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(5));
        borderPane.setMinSize(model.getDimensions()+100,model.getDimensions()+100);
        message.setText("Load a file");
        borderPane.setTop(message);
        message.setAlignment(Pos.TOP_CENTER);

        VBox rightPanel = new VBox();
        rightPanel.setPadding(new Insets(50,0,50,0));
        rightPanel.setSpacing(10);
        Button load = new Button("Load Game");
        load.setOnAction(e->{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")+"/data/tilt"));
            fileChooser.getExtensionFilters().addAll( new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            //open up a window for the user to interact with.
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile.toString() != null) {
                model.loadBoardFromFile(selectedFile.toString());
            }
        });
        Button reset = new Button("Reset");
        reset.setOnAction(e-> model.reset());
        Button hint = new Button("Hint");
        hint.setOnAction(e -> model.getHint());
        rightPanel.getChildren().addAll(load, reset, hint);
        borderPane.setRight(rightPanel);
        rightPanel.setAlignment(Pos.CENTER_RIGHT);


        BorderPane middle = new BorderPane();
        Button up = new Button("^");
        up.setOnAction(e -> model.tiltUp());
        Button down = new Button("v");
        down.setOnAction(e -> model.tiltDown());
        Button left = new Button("<");
        left.setOnAction(e -> model.tiltLeft());
        Button right = new Button(">");
        right.setOnAction(e -> model.tiltRight());
        middle.setTop(up);
        middle.setBottom(down);
        middle.setLeft(left);
        middle.setRight(right);
        borderPane.setCenter(middle);


        GridPane center = new GridPane();
        center.setAlignment(Pos.CENTER);

        green.fitHeightProperty().bind(center.heightProperty().subtract(100).divide(model.getDimensions()));
        green.fitWidthProperty().bind(center.widthProperty().subtract(100).divide(model.getDimensions()));
        blue.fitHeightProperty().bind(center.heightProperty().subtract(100).divide(model.getDimensions()));
        blue.fitWidthProperty().bind(center.widthProperty().subtract(100).divide(model.getDimensions()));
        hole.fitHeightProperty().bind(center.heightProperty().subtract(100).divide(model.getDimensions()));
        hole.fitWidthProperty().bind(center.widthProperty().subtract(100).divide(model.getDimensions()));
        block.fitHeightProperty().bind(center.heightProperty().subtract(100).divide(model.getDimensions()));
        block.fitWidthProperty().bind(center.widthProperty().subtract(100).divide(model.getDimensions()));


        for (int y = 0; y < model.getDimensions(); y++){
            for (int x = 0; x < model.getDimensions(); x++){
                buttons[x][y] = new Button();
                if (model.getCell(y, x) == 'B') {
                    buttons[x][y].setGraphic(blue);
                }
                if(model.getCell(y, x) == 'G') {
                    buttons[x][y].setGraphic(green);
                }
                if (model.getCell(y,x) == '*'){
                    buttons[x][y].setGraphic(block);
                }
                if (model.getCell(y,x) == 'O'){
                    buttons[x][y].setGraphic(hole);
                }
                center.add(buttons[x][y], x, y);
                buttons[x][y].setStyle("-fx-focus-traversable: false;-fx-background-color: #ffffff");

            }
        }
        center.setGridLinesVisible(true);
        center.setMinWidth(model.getDimensions());
        middle.setCenter(center);
        Scene scene = new Scene(borderPane,500,500);
        stage.setScene(scene);
        stage.show();
    }


    public void updateBoard(){

        for (int y = 0; y < model.getDimensions(); y++){
            for (int x = 0; x < model.getDimensions(); x++){
                if (model.getCell(y, x) == 'B') {
                    buttons[x][y].setGraphic(blue);
                }
                else if (model.getCell(y, x) == 'G') {
                    buttons[x][y].setGraphic(green);
                }
                else if (model.getCell(y,x) == '*'){
                    buttons[x][y].setGraphic(block);
                }
                else if (model.getCell(y,x) == 'O'){
                    buttons[x][y].setGraphic(hole);
                }
                else {
                    buttons[x][y].setStyle("-fx-background-color: #ffffff");
                }
            }
        }
    }

    @Override
    public void update(TiltModel tiltModel, String msg) {
        updateBoard();
        if (model.gameOver()) {
            message.setText("You Win");
            updateBoard();
        } else {
            message.setText(msg);
        }

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
