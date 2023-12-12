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

    public TiltPTUI(String filename){
        model = new TiltModel(filename);
        model.addObserver(this);
        in = new Scanner(System.in);
        gameOn = false;
    }

    public boolean loadFromFile(){
        boolean ready = false;

        while(!ready){
            System.out.println("Enter a valid file name or type Q to go back.");
            String command =  in.next();
            if (command.equals("q") || command.equals("Q")) {
                System.out.println("going back...");
                return false;
            }
            else {
                ready = model.loadBoardFromFile(command);
            }

        }
        return true;
    }


    public boolean gameStart(){
        boolean ready = false;
        while(!ready){
            System.out.println("(L)oad Game. (Q)uit");
            String command =  in.next(); // Using next allows you to string together load commands like l boards/1.lob.
            switch (command){
                case "L":
                case "l":
                    ready = loadFromFile();
                    break;
                case "Q":
                case "q":
                    System.out.println("Exiting");
                    in = new Scanner(System.in);//get rid of any remaining commands from the start menu
                    ready = true;
                    return false;

                default:
                    System.out.println("Enter L or Q.");
            }
            gameOn = true;
        }
        in = new Scanner(System.in);//get rid of any remaining commands from the start menu
        return true;
    }

    public void run() throws IOException {
        while (true){
            if(!(gameStart())){
                break;
            }
            gameLoop();
        }
    }

    public void gameLoop() throws IOException {
        String msg;

        while(gameOn) {
            msg = "";
            System.out.println("Enter up, down, left, right to tilt board, (R)eset, (H)int, or (Q)uit to main menu");
            String command = in.nextLine().strip();
            if (command.equals("q") || command.equals("Q")) {
                System.out.println("Quitting to main menu.");
                gameOn = false;

                return;

            }else if (command.equals("h") || command.equals("H")) {
                model.getHint();
                displayBoard();

            }else if(command.equals("r") || command.equals("R")){
                model.reset();
            }else {
                try {
                    Scanner s = new Scanner(command);
                    String direction = s.next();
                    switch(direction){
                        case "up":
                            model.tiltUp();
                            break;
                        case "down":
                            model.tiltDown();
                            break;
                        case "left":
                            model.tiltLeft();
                            break;
                        case "right":
                            model.tiltRight();
                    }
                    displayBoard();

                } catch (InputMismatchException e) {

                    msg = "Direction  must be string and lowercase";
                } catch (NoSuchElementException e) {

                    msg = "Must enter direction on one line";
                }
            }

            if (!msg.isEmpty())
                System.out.println("Command: "+command+"\n\033[0;1m***"+msg+"***\033[0;0m");
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
        if (message.equals(TiltModel.LOADED)){
            System.out.println("Game Loaded");
            displayBoard();
        }else if (message.equals(TiltModel.LOAD_FAILED)){
            System.out.println("Error Loading Game");
        } else if (message.startsWith(TiltModel.HINT_PREFIX)) {
            System.out.println(message);
        }else{
            System.out.println(message);
        }

        if (model.gameOver()) { //checks if game is over.
            displayBoard();

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
