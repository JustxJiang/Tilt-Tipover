package puzzles.tipover.ptui;

import puzzles.common.Observer;
import puzzles.tilt.model.TiltModel;
import puzzles.tilt.ptui.TiltPTUI;
import puzzles.tipover.model.TipOverModel;
import puzzles.tipover.model.TipOverModel;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class TipOverPTUI implements Observer<TipOverModel, String> {
    private TipOverModel model;
    private Scanner in;
    private boolean gameOn;


    public TipOverPTUI(String filename) {
        model = new TipOverModel(filename);
        model.addObserver(this);
        in = new Scanner(System.in);
        gameOn = true;
    }

    public void loadFromFile(String filename) {
        model.loadBoardFromFile(filename);
    }



    public void displayBoard(){
        System.out.println(model.toString());

//        //prints the column number
//        System.out.print("  ");
//        for(int c =0; c<model.getDimension(); c++){
//            System.out.print(c+" ");
//        }
//        //System.out.print("\033[0;0m"); //turn off underline
//        int currentRow = -1;
//
//        //prints the tiles
//        for(Tile t : model){
//            if (currentRow!=t.getY()){ //newline for new rows.
//                currentRow=t.getY();
//                System.out.printf("%n%d ",currentRow);
//
//            }
//            char symbol = OFFSYMBOL;
//            if (t.isOn()) {
//                symbol= ONSYMBOL;
//            }
//            System.out.print(symbol+" ");
//
//        }
//
//        System.out.printf("\nTotal Moves: %d\n",model.getMoves());
//        //  System.out.println();
    }

    private void gameLoop(){
        String msg;
        displayBoard();
        System.out.println("h(int)              -- hint next move\n" +
                "l(oad) filename     -- load new puzzle file\n" +
                "m(ove) {N|S|E|W}    -- move the tipper in the given direction\n" +
                "q(uit)              -- quit the game\n" +
                "r(eset)             -- reset the current game");

        while(gameOn) {
            msg = "";
            String command = in.nextLine().strip();
            String prefix = command.substring(0, 1);
            switch(prefix) {
                case "q":
                case "Q":
                {
                    gameOn = false;
                    return;
                }
                case "h":
                case "H":
                {
                    model.getHint();
                    displayBoard();
                }
                case "l":
                case "L":
                {
                    String filename = command.substring(1);
                    loadFromFile(filename);
                    displayBoard();
                }
                case "m":
                case "M":
                {
                    String direction = command.substring(1);
                    switch(direction) {
                        case "N": {
                            model.moveNorth();
                        }
                        case "S": {
                            model.moveSouth();
                        }
                        case "E": {
                            model.moveEast();
                        }
                        case "W": {
                            model.moveWest();
                        }
                        default: {
                            System.out.println("Please specify a direction to move {N|S|E|W}");
                        }
                    }
                    displayBoard();
                }
                case "r":
                case "R":
                {
                    model.reset();
                    displayBoard();
                }
                default: {
                    displayBoard();
                    System.out.println("h(int)              -- hint next move\n" +
                            "l(oad) filename     -- load new puzzle file\n" +
                            "m(ove) {N|S|E|W}    -- move the tipper in the given direction\n" +
                            "q(uit)              -- quit the game\n" +
                            "r(eset)             -- reset the current game");
                }
            }


            if (!msg.isEmpty())

                System.out.println("Command: "+command+"\n\033[0;1m***"+msg+"***\033[0;0m");

        }
    }
    

    @Override
    public void update(TipOverModel model, String message) {
        displayBoard();
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
