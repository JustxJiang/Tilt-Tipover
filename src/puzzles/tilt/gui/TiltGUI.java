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

public class TiltGUI extends Application implements Observer<TiltModel, String> {
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";
    private TiltModel model;
    private Button[][] buttons;
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
    }

    @Override
    public void start(Stage stage) throws Exception {

        //borderPane
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(5));
        borderPane.setMinSize(500,500);
        //initialize middle
        BorderPane middle = new BorderPane();

        //top
        FlowPane top = new FlowPane();
        message.setText("Load a file");
        borderPane.setTop(message);
        top.setAlignment(Pos.TOP_CENTER);


        //right
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
                middle.setCenter(board());
                message.setText(selectedFile.toString());
            }
        });
        Button reset = new Button("Reset");
        reset.setOnAction(e-> {
            model.reset();
            middle.setCenter(board());
        });
        Button hint = new Button("Hint");
        hint.setOnAction(e -> {
            model.getHint();
            middle.setCenter(board());
        });
        rightPanel.getChildren().addAll(load, reset, hint);
        borderPane.setRight(rightPanel);
        rightPanel.setAlignment(Pos.CENTER_RIGHT);



        //center
        Button up = new Button("^");
        up.setMaxWidth(Double.MAX_VALUE);
        up.setOnAction(e -> {
            model.tiltUp();
            middle.setCenter(board());
        });

        Button down = new Button("v");
        down.setMaxWidth(Double.MAX_VALUE);
        down.setOnAction(e -> {
            model.tiltDown();
            middle.setCenter(board());
        });

        Button left = new Button("<");
        left.setMaxHeight(Double.MAX_VALUE);
        left.setOnAction(e -> {
            model.tiltLeft();
            middle.setCenter(board());
        });

        Button right = new Button(">");
        right.setMaxHeight(Double.MAX_VALUE);
        right.setOnAction(e -> {
            model.tiltRight();
            middle.setCenter(board());
        });

        middle.setTop(up);
        middle.setBottom(down);
        middle.setLeft(left);
        middle.setRight(right);
        borderPane.setCenter(middle);


        middle.setCenter(board());
        Scene scene = new Scene(borderPane,800,800);
        stage.setScene(scene);
        stage.show();
    }

    public GridPane board(){
        GridPane center = new GridPane();
        center.setAlignment(Pos.CENTER);
        buttons = new Button[model.getDimensions()][model.getDimensions()];


        for (int y = 0; y < model.getDimensions(); y++){
            for (int x = 0; x < model.getDimensions(); x++){
                buttons[x][y] = new Button();
                buttons[x][y].setStyle("-fx-focus-traversable: false;-fx-background-color: #ffffff");
                //make a new imageView everytime or else it will just move the old imageView
                switch(model.getCell(y,x)){
                    case 'B':
                        blue = new ImageView(blueDisk);
                        blue.setFitWidth(50);
                        blue.setFitHeight(50);
                        buttons[x][y].setGraphic(blue);
                        break;
                    case 'G':
                        green = new ImageView(greenDisk);
                        green.setFitHeight(50);
                        green.setFitWidth(50);
                        buttons[x][y].setGraphic(green);
                        break;
                    case '*':
                        block = new ImageView(blockDisk);
                        block.setFitWidth(50);
                        block.setFitHeight(50);
                        buttons[x][y].setGraphic(block);
                        break;
                    case 'O':
                        hole = new ImageView(holeDisk);
                        hole.setFitWidth(50);
                        hole.setFitHeight(50);
                        buttons[x][y].setGraphic(hole);
                        break;
                    default:
                        buttons[x][y].setStyle("-fx-focus-traversable: false;-fx-background-color: #ffffff");
                }

                buttons[x][y].setMinSize(50, 50);
                buttons[x][y].setMaxSize(50,50);

                center.add(buttons[x][y], x, y);

            }
        }
        center.setGridLinesVisible(true);
        center.setMinSize(400,400);
        return center;
    }


    @Override
    public void update(TiltModel tiltModel, String msg) {
        // if the message is file loaded, recreate the board with the dimension and
        // set every button in the grid with the info from the model
        if (model.gameOver()) {
            message.setText("You Win");
            board();

        }else if (msg.equals(model.LOADED)){
            board();
        }else if(msg.equals(model.HINT_PREFIX)){
            board();
        }
        else {
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
