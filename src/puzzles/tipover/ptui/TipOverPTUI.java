package puzzles.tipover.ptui;

import puzzles.common.Observer;
import puzzles.tilt.model.TiltModel;
import puzzles.tilt.ptui.TiltPTUI;
import puzzles.tipover.model.TipOverModel;
import puzzles.tipover.model.TipOverModel;
import puzzles.tipover.solver.TipOver;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class TipOverPTUI implements Observer<TipOverModel, String> {
    private TipOverModel model;
    private Scanner in;
    private boolean gameOn;
    private String filename;

    /**
     * Constructor for PTUI
     * @param filename
     */
    public TipOverPTUI(String filename) {
        try {
            model = new TipOverModel(filename);
            model.addObserver(this);
            in = new Scanner(System.in);
            gameOn = true;
            this.filename = filename;
        } catch (RuntimeException e)
        {
            System.out.println("Please enter a valid file name!");
        }
    }

    /**
     * loads board from file
     * @param filename
     */
    public void loadFromFile(String filename) {
        this.filename = filename;
        model.loadBoardFromFile(filename);
    }


    /**
     * displays the board
     */
    public void displayBoard(){
        StringBuilder result = new StringBuilder();
        result.append("     ");
        for(int col = 0; col < model.getCols(); col++) {
            result.append(" " + col + " ");
        }
        result.append("\n    ");
        for(int col = 0; col < model.getCols(); col++) {
            result.append("---");
        }
        result.append("\n");
        for(int row = 0; row < model.getRows(); row++) {
            result.append(" " + row + " | ");
            for(int col = 0; col < model.getCols(); col++) {
                char temp = model.getVal(row, col);
                if(temp == '0') {
                    result.append(" _ ");
                } else {
                    if (row == model.getTipper().x && col == model.getTipper().y) {
                        result.append("*");
                    } else if (row == model.getEnd().x && col == model.getEnd().y) {
                        result.append("!");
                    } else {
                        result.append(" ");
                    }
                    result.append(temp + " ");
                }
            }
            result.append("\n");
        }
        System.out.println(result);
    }

    /**
     * In charge of looping the game
     */
    private void gameLoop(){
        System.out.println("Loaded: " + filename + "\n");
        displayBoard();
        System.out.println("h(int)              -- hint next move\n" +
                "l(oad) filename     -- load new puzzle file\n" +
                "m(ove) {N|S|E|W}    -- move the tipper in the given direction\n" +
                "q(uit)              -- quit the game\n" +
                "r(eset)             -- reset the current game\n");
        //Uses while to keep game running until quit
        while(gameOn) {
            try {
                String command = in.nextLine().strip();
                String prefix = command.substring(0, 1);
                switch (prefix) {
                    case "q":
                    case "Q": {
                        gameOn = false;
                        break;
                    }
                    case "h":
                    case "H": {
                        model.getHint();
                        displayBoard();
                        break;
                    }
                    case "l":
                    case "L": {
                        String filename = command.substring(1).strip();
                        loadFromFile(filename);
                        break;
                    }
                    case "m":
                    case "M": {
                        String direction = command.substring(1).strip();
                        switch (direction) {
                            case "N": {
                                model.moveNorth();
                                break;
                            }
                            case "S": {
                                model.moveSouth();
                                break;
                            }
                            case "E": {
                                model.moveEast();
                                break;
                            }
                            case "W": {
                                model.moveWest();
                                break;
                            }
                            default: {
                                System.out.println("Please specify a direction to move {N|S|E|W}\n");
                                break;
                            }
                        }
                        displayBoard();
                        break;
                    }
                    case "r":
                    case "R": {
                        model.reset();
                        break;
                    }
                    default: {
                        displayBoard();
                        System.out.println("h(int)              -- hint next move\n" +
                                "l(oad) filename     -- load new puzzle file\n" +
                                "m(ove) {N|S|E|W}    -- move the tipper in the given direction\n" +
                                "q(uit)              -- quit the game\n" +
                                "r(eset)             -- reset the current game");
                        break;
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Please enter a command: ");
            }
        }
    }

    /**
     * Updates based on
     * @param model the model to update
     * @param msg the message sent by the observer
     */
    @Override
    public void update(TipOverModel model, String msg) {
        //Won is used to restrict movement after reaching the goal
        if(msg.equals(TipOverModel.WON))
        {
            System.out.println("> " + msg + "\n");
        }
        else if(model.gameOver() || msg.equals(TipOverModel.WIN)) {
            System.out.println("> You win!");
        }
        else if (msg.equals(TipOverModel.LOADED)){
            System.out.println("> Loaded: " + filename + "\n");
            displayBoard();
            return;
        } else if(msg.equals(TipOverModel.RESET)) {
            System.out.println("> " + msg + "\n");
            displayBoard();
        }
        else if (msg.equals(TipOverModel.LOAD_FAILED)){
            System.out.println("> Error Loading: " + filename + "\n");
            return;
        } else if (msg.startsWith(TipOverModel.HINT_PREFIX)) {
            System.out.println("> " + msg + "\n");
            //don't display board
            return;
        }
        else System.out.println("> " + msg + "\n");
    }
    /**
     * Runs the game loop from main
     */
    public void run(){
        while(true){
            if(!gameOn) {
                break;
            }
            gameLoop();
        }
    }
    /**
     * The main method
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TipOverPTUI filename");
        } else{
            TipOverPTUI ui = new TipOverPTUI(args[0]);
            ui.run();
        }
    }
}
