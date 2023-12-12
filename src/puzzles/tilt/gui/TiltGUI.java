package puzzles.tilt.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import javafx.scene.text.Font;
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
    private TiltModel model; //initialized model here
    private String file; //used when resetting and loading files
    private Button[][] buttons; //2d array of buttons
    private Label message = new Label(); //label that will serve as message each time an action happens

    private Image greenDisk = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"green.png")); //Image of green token
    private ImageView green = new ImageView(greenDisk); //ImageView of green token

    private Image blueDisk = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"blue.png")); //Image of blue token
    private ImageView blue = new ImageView(blueDisk); //ImageView of blue token

    private Image holeDisk = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"hole.png")); //Image of hole
    private ImageView hole = new ImageView(holeDisk); //ImageView of hole

    private Image blockDisk = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"block.png")); //Image of blocker
    private ImageView block = new ImageView(blockDisk); //ImageView of blocker


    public void init() {
        String filename = getParameters().getRaw().get(0);
        file = filename;
        model = new TiltModel(filename);
        model.loadBoardFromFile(filename);
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
        message.setFont(Font.font("Elephant"));
        message.setMinSize(50,50);
        message.setText("Loaded: " + file);
        top.getChildren().add(message);
        borderPane.setTop(top);
        top.setAlignment(Pos.CENTER);


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
                file = selectedFile.toString();
                model.loadBoardFromFile(file);
                middle.setCenter(board());
            }
        });
        Button reset = new Button("Reset");
        reset.setOnAction(e-> {
            model.reset();
            middle.setCenter(board());
        });

        Button hint = new Button("Hint");
        hint.setOnAction(e -> {
            try{
                model.getHint();
                middle.setCenter(board());
            }catch(Exception x){
                message.setText("No Hint because there is no solution. Don't Believe Me? Try It Yourself.");
            }

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
        middle.setMaxSize((50*model.getDimensions())+50, (50*model.getDimensions())+50);
        middle.setTop(up);
        middle.setBottom(down);
        middle.setLeft(left);
        middle.setRight(right);
        borderPane.setCenter(middle);


        middle.setCenter(board());
        Scene scene = new Scene(borderPane,1000,1000);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * the main board - the center
     */
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
        center.setMinSize(50*model.getDimensions(),50*model.getDimensions());
        return center;
    }


    @Override
    public void update(TiltModel tiltModel, String msg) {
        // if the message is file loaded, recreate the board with the dimension and
        // set every button in the grid with the info from the model
        if (model.gameOver()) {
            message.setText("You Won. Load New Game, Reset Board, or Play Around.");
            board();

        }else if (msg.equals(model.LOADED)){
            message.setText(file);
            board();
        }else if(msg.equals(model.HINT_PREFIX)){
            message.setText("Next Step!");
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
