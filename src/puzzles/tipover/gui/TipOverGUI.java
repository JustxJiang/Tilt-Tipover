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

import java.awt.*;
import java.io.File;

public class TipOverGUI extends Application implements Observer<TipOverModel, String> {
    private Label message = new Label();
    private TipOverModel model;

    public void init() {
        String filename = getParameters().getRaw().get(0);
        model = new TipOverModel(filename);
        model.addObserver(this);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load a game board.");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")+"/data/tipover"));
        fileChooser.getExtensionFilters().addAll( new FileChooser.ExtensionFilter("Text Files", "*.txt"));


        BorderPane main = new BorderPane();
        main.setTop(message);
        main.setCenter(updateBoard());

        //Code for toolbar
        GridPane toolbar = new GridPane();
        Button loadGame = new Button("Load Game");
        //Code for loading a board from a file
        loadGame.setOnAction((ActionEvent event) -> {

            File selectedFile = fileChooser.showOpenDialog(stage);
            //Tests for exception in case user closes file window
            try {
                model.loadBoardFromFile("data/tipover/" + selectedFile.getName());
                message.setText("Loaded file: " + selectedFile.getName());
                main.setCenter(updateBoard());
            }
            catch (NullPointerException e)
            {
                main.setCenter(updateBoard());
            }
        });
        Button reset = new Button("Reset");
        reset.setOnAction((ActionEvent event) -> {
            model.reset();
            message.setText("");
            main.setCenter(updateBoard());
        });
        Button hint = new Button("Hint");
        //Code for user hints
        hint.setOnAction((ActionEvent event) -> {
            if(!model.gameOver()) {
                model.getHint();
            }
            main.setCenter(updateBoard());
        });
        GridPane buttons = new GridPane();
        Button up = new Button("^");
        up.setOnAction((ActionEvent event) -> {
            model.moveNorth();
            main.setCenter(updateBoard());
        });
        Button down = new Button("v");
        down.setOnAction((ActionEvent event) -> {
            model.moveSouth();
            main.setCenter(updateBoard());
        });
        Button right = new Button(">");
        right.setOnAction((ActionEvent event) -> {
            model.moveEast();
            main.setCenter(updateBoard());
        });
        Button left = new Button("<");
        left.setOnAction((ActionEvent event) -> {
            model.moveWest();
            main.setCenter(updateBoard());
        });

        buttons.add(up, 1, 0);
        buttons.add(down, 1, 2);
        buttons.add(left, 0, 1);
        buttons.add(right, 2, 1);
        buttons.setAlignment(Pos.CENTER);

        toolbar.add(loadGame, 0, 0);
        toolbar.add(reset, 0, 1);
        toolbar.add(hint, 0, 2);
        toolbar.setAlignment(Pos.CENTER);
        //Code for program BorderPane

        GridPane sidebar = new GridPane();
        sidebar.add(buttons, 0, 0);
        sidebar.add(toolbar, 0, 1);
        sidebar.setAlignment(Pos.CENTER);

        main.setRight(sidebar);
        main.setPadding(new Insets(10));
        main.setMinSize(model.getRows()+100,model.getCols()+100);

        //Code to initialize scene
        Scene scene = new Scene(main);
        stage.setScene(scene);
        stage.setTitle("Tip Over");
        stage.show();
    }

    private GridPane updateBoard()
    {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        for(int row = 0; row < model.getRows(); row++)
        {
            for(int col = 0; col < model.getCols(); col++)
            {
                Label label = new Label(model.getVal(row, col) + "");
                label.setFont(new Font(30));
                grid.add(label, col, row);
                if(model.getTipper().x == row && model.getTipper().y == col)
                {
                    label.setStyle("-fx-background-color: pink;");
                }
                else if(model.getEnd().x == row && model.getEnd().y == col)
                {
                    label.setStyle("-fx-background-color: red;");
                }
            }
        }

        return grid;
    }

    @Override
    public void update(TipOverModel tipOverModel, String msg) {
        if (model.gameOver()) {
            message.setText("You Win");
            updateBoard();

        }else if (msg.equals(model.LOADED)){
            updateBoard();

        }else if(msg.equals(model.HINT_PREFIX)){
            updateBoard();
        }
        else {
            //message.setText(msg);
        }

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
