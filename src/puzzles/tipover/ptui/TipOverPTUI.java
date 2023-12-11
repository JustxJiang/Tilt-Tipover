package puzzles.tipover.ptui;

import puzzles.common.Observer;
import puzzles.tilt.model.TiltModel;
import puzzles.tilt.ptui.TiltPTUI;
import puzzles.tipover.model.TipOverModel;
import puzzles.tipover.model.TipOverModel;
import puzzles.tipover.solver.TipOver;

import java.sql.SQLOutput;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class TipOverPTUI implements Observer<TipOverModel, String> {
    private TipOverModel model;
    private Scanner in;
    private boolean gameOn;
    private String filename;


    public TipOverPTUI(String filename) {
        model = new TipOverModel(filename);
        model.addObserver(this);
        in = new Scanner(System.in);
        gameOn = true;
    }

    public void loadFromFile(String filename) {
        model.loadBoardFromFile("data/tipover/" + filename);
        this.filename = filename;
    }



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

    private void gameLoop(){
        displayBoard();
        System.out.println("h(int)              -- hint next move\n" +
                "l(oad) filename     -- load new puzzle file\n" +
                "m(ove) {N|S|E|W}    -- move the tipper in the given direction\n" +
                "q(uit)              -- quit the game\n" +
                "r(eset)             -- reset the current game\n");

        while(gameOn) {
            String command = in.nextLine().strip();
            String prefix = command.substring(0, 1);
            switch(prefix) {
                case "q":
                case "Q":
                {
                    gameOn = false;
                    break;
                }
                case "h":
                case "H":
                {
                    model.getHint();
                    displayBoard();
                    break;
                }
                case "l":
                case "L":
                {
                    String filename = command.substring(1).strip();
                    loadFromFile(filename);
                    break;
                }
                case "m":
                case "M":
                {
                    String direction = command.substring(1).strip();
                    switch(direction) {
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
                            System.out.println("Please specify a direction to move {N|S|E|W}");
                            break;
                        }
                    }
                    displayBoard();
                    break;
                }
                case "r":
                case "R":
                {
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

        }

    }
    

    @Override
    public void update(TipOverModel model, String msg) {
        if (msg.equals(TipOverModel.LOADED)){
            System.out.println("Loaded: data/tipover/\"" + filename);
            displayBoard();
            return;
        }else if (msg.equals(TipOverModel.LOAD_FAILED)){
            System.out.println("Error Loading Game");
            return;
        }else if(model.gameOver()) {

        }

        else if (msg.startsWith(TipOverModel.HINT_PREFIX)) {
            System.out.println(msg);
            //don't display board
            return;
        }
    }
    public void run(){
        while(true){
            if(!gameOn) {
                break;
            }
            gameLoop();
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TipOverPTUI filename");
        } else{
            TipOverPTUI ui = new TipOverPTUI(args[0]);
            ui.run();
        }
    }
}
