package puzzles.tipover.gui;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.tipover.model.TipOverModel;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import puzzles.tipover.solver.TipOver;
import java.awt.*;
import java.io.File;
import java.lang.reflect.InvocationTargetException;

public class TipOverGUI extends Application implements Observer<TipOverModel, String> {
    private final Label message = new Label();
    private TipOverModel model;
    private String filename;
    /**
     * Main method
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TipOverGUI filename");
            System.exit(0);
        } else {
            try {
                Application.launch(args);
            } catch (Exception e) {
                System.out.println("Please enter a valid file name!");
            }
        }
    }
    /**
     * Initializes the GUI
     */
    public void init() {
        String filename = getParameters().getRaw().get(0);

        try {
            model = new TipOverModel(filename);
            message.setText("Loaded: " + filename.substring(13));
            model.addObserver(this);
        } catch (RuntimeException e) {
            System.out.println("Please enter a valid file name!");
        }
    }
    /**
     * Override the start method
     */
    @Override
    public void start(Stage stage) throws Exception {
        //Creates a file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load a game board.");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir") + "/data/tipover"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        BorderPane main = new BorderPane();
        main.setTop(message);
        main.setCenter(updateBoard());

        //Code for toolbar
        GridPane toolbar = new GridPane();
        //Code for loading a board from a file
        Button loadGame = new Button("Load Game");
        loadGame.setPadding(new Insets(5));
        loadGame.setOnAction((ActionEvent event) -> {
            File selectedFile = fileChooser.showOpenDialog(stage);
            //Tests for exception in case user closes file window
            try {
                model.loadBoardFromFile("data/tipover/" + selectedFile.getName());
                message.setText("Loaded: " + selectedFile.getName());
                main.setCenter(updateBoard());
            } catch (NullPointerException e) {
                main.setCenter(updateBoard());
            }
            stage.sizeToScene();
        });
        //Code for reset button
        Button reset = new Button("Reset");
        reset.setPadding(new Insets(5));
        reset.setOnAction((ActionEvent event) -> {
            model.reset();
            main.setTop(message);
            main.setCenter(updateBoard());
        });
        //Code for user hints
        Button hint = new Button("Hint");
        hint.setPadding(new Insets(5));
        hint.setOnAction((ActionEvent event) -> {
            if (model.gameOver()) {
                message.setText(TipOverModel.WON);
                main.setTop(message);
            } else {
                model.getHint();
                main.setCenter(updateBoard());
                main.setTop(message);
            }
        });
        //Code for directional buttons
        GridPane buttons = new GridPane();
        Button up = new Button("^");
        up.setPrefSize(30, 30);
        up.setOnAction((ActionEvent event) -> {
            model.moveNorth();
            main.setCenter(updateBoard());
        });
        Button down = new Button("v");
        down.setPrefSize(30, 30);
        down.setOnAction((ActionEvent event) -> {
            model.moveSouth();
            main.setCenter(updateBoard());
        });
        Button right = new Button(">");
        right.setPrefSize(30, 30);
        right.setOnAction((ActionEvent event) -> {
            model.moveEast();
            main.setCenter(updateBoard());
        });
        Button left = new Button("<");
        left.setPrefSize(30, 30);
        left.setOnAction((ActionEvent event) -> {
            model.moveWest();
            main.setCenter(updateBoard());
        });
        //Code to add directional buttons to grid
        buttons.add(up, 1, 0);
        buttons.add(down, 1, 2);
        buttons.add(left, 0, 1);
        buttons.add(right, 2, 1);
        buttons.setAlignment(Pos.CENTER);
        //Code to add toolbar buttons to toolbar
        toolbar.add(loadGame, 0, 0);
        toolbar.add(reset, 0, 1);
        toolbar.add(hint, 0, 2);
        toolbar.setAlignment(Pos.CENTER);
        toolbar.setPadding(new Insets(10));
        //Code to add directional buttons and toolbar to sidebar
        GridPane sidebar = new GridPane();
        sidebar.add(buttons, 0, 0);
        sidebar.add(toolbar, 0, 1);
        sidebar.setAlignment(Pos.CENTER);
        sidebar.setPadding(new Insets(10));
        //Add all buttons to main
        main.setRight(sidebar);
        main.setPadding(new Insets(10));
        //Code to initialize scene
        Scene scene = new Scene(main);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setTitle("Tip Over");
        stage.show();
    }
    /**
     * Creates an updated board based on the values
     */
    private GridPane updateBoard() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10));
        //Creates a new GridPane with updated values
        for (int row = 0; row < model.getRows(); row++) {
            for (int col = 0; col < model.getCols(); col++) {
                Label label = new Label(model.getVal(row, col) + "");
                label.setFont(new Font(30));
                grid.add(label, col, row);
                if (model.getTipper().x == row && model.getTipper().y == col) {
                    label.setStyle("-fx-background-color: pink;");
                } else if (model.getEnd().x == row && model.getEnd().y == col) {
                    label.setStyle("-fx-background-color: red;");
                }
            }
        }

        return grid;
    }
    /**
     * Override the update method
     */
    @Override
    public void update(TipOverModel tipOverModel, String msg) {
        //Won is used to restrict movement after reaching the goal
        if (msg.equals(TipOverModel.WON)) {
            message.setText(TipOverModel.WON);
        } else if (model.gameOver() || msg.equals(TipOverModel.WIN)) {
            message.setText(TipOverModel.WIN);
        } else {
            message.setText(msg);
        }

    }
}
