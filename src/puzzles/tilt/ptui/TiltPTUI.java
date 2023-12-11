package puzzles.tilt.ptui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import puzzles.common.Observer;
import puzzles.tilt.model.TiltModel;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class TiltPTUI implements Observer<TiltModel, String> {
    private TiltModel model;
    private Scanner in;
    private boolean gameOn;
    private String command;
    private String file;
    public TiltPTUI(String filename){
        model = new TiltModel(filename);
        model.loadBoardFromFile(filename);
        file = filename;
        model.addObserver(this);
        in = new Scanner(System.in);
        gameOn = true;
    }


    public void run(){
        while (gameOn){
            gameLoop();
        }
    }

    public void gameLoop() {
        System.out.println("Loaded: " + file);
        displayBoard();
        System.out.println();
        while(gameOn) {
            System.out.println("    h(int)              -- hint next move\n" +
                    "    l(oad) filename     -- load new puzzle file\n" +
                    "    t(ilt) {N|S|E|W}    -- tilt the board in the given direction\n" +
                    "    q(uit)              -- quit the game\n" +
                    "    r(eset)             -- reset the current game");

            try{
                command = in.nextLine().strip();
                String prefix = command.substring(0,1);
                switch(prefix){
                    case "h":
                    case "H":
                        model.getHint();
                        displayBoard();
                        System.out.println();
                        break;
                    case "L":
                    case "l":
                        try{
                            model.loadBoardFromFile(command.substring(1).strip());
                        }catch (Exception e){
                            System.out.println("Invalid Filename or No File inputted.\n"+
                                    "   h(int)              -- hint next move\n" +
                                    "   l(oad) filename     -- load new puzzle file\n" +
                                    "   t(ilt) {N|S|E|W}    -- tilt the board in the given direction\n" +
                                    "   q(uit)              -- quit the game\n" +
                                    "   r(eset)             -- reset the current game");
                        }
                        break;
                    case "t":
                    case "T":
                        String direction = command.substring(1).strip();
                        switch(direction){
                            case "N":
                            case "n":
                                model.tiltUp();
                                break;
                            case "S":
                            case "s":
                                model.tiltDown();
                                break;
                            case "W":
                            case "w":
                                model.tiltLeft();
                                break;
                            case "E":
                            case "e":
                                model.tiltRight();
                                break;
                            default:
                                System.out.println("Direction must be: t or T, and then N S E or W <ON ONE LINE> \n"+
                                        "   h(int)              -- hint next move\n" +
                                        "   l(oad) filename     -- load new puzzle file\n" +
                                        "   t(ilt) {N|S|E|W}    -- tilt the board in the given direction\n" +
                                        "   q(uit)              -- quit the game\n" +
                                        "   r(eset)             -- reset the current game");
                        }
                        displayBoard();
                        System.out.println();
                        break;
                    case "q":
                    case "Q":
                        System.out.println("Quitting to main menu.");
                        gameOn = false;
                        break;
                    case "r":
                    case "R":
                        model.reset();
                        displayBoard();
                        System.out.println();
                        break;
                    default:
                        System.out.println("Enter one of the commands below:\n" +
                                "   h(int)              -- hint next move\n" +
                                "   l(oad) filename     -- load new puzzle file\n" +
                                "   t(ilt) {N|S|E|W}    -- tilt the board in the given direction\n" +
                                "   q(uit)              -- quit the game\n" +
                                "   r(eset)             -- reset the current game");
                }
            }catch(IndexOutOfBoundsException e){
                System.out.println("No Input Detected!");
                displayBoard();
            }

        }


    }

    public void displayBoard(){

        //prints the tiles
        for (int r = 0; r < model.getDimensions(); r++){
            for (int c = 0; c < model.getDimensions(); c++){
                System.out.print(model.getCell(r,c) +  " ");
            }
            System.out.println();
        }
    }

    @Override
    public void update(TiltModel model, String message) {
        if (message.equals(TiltModel.LOADED)){ // game is loaded successfully
            System.out.println("Game Loaded");
            displayBoard();
        }else if (message.equals(TiltModel.LOAD_FAILED)){ //Game failed to load
            System.out.println("Error Loading Game");
        } else if (message.startsWith(TiltModel.HINT_PREFIX)) { //Model is reporting a  hint
            System.out.println(message);
        }else{
            System.out.println(message);
        }

        if (model.gameOver()) { //checks if game is over.
            System.out.println("You win. Good for you.");
            gameOn = false;
        }
    }
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java TiltPTUI filename");
        }
        else{
            TiltPTUI ui = new TiltPTUI(args[0]);
            ui.run();
        }
    }
}
