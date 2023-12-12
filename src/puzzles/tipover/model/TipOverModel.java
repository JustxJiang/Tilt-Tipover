package puzzles.tipover.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class TipOverModel {
    /** the collection of observers of this model */
    private final List<Observer<TipOverModel, String>> observers = new LinkedList<>();
    /** the current configuration */
    private TipOverConfig currentConfig;
    private String currentFile;
    public static String LOADED = "Loaded!";
    public static String LOAD_FAILED = "Load failed!";
    public static String HINT_PREFIX = "Next step!";
    public static String RESET = "Puzzle reset!";
    public static String WIN = "You win!";
    public static String WON = "This board has already been solved!";
    public static String NO_SOLUTION = "No solution!";

    /**
     * Constructor for the TipOver model
     * @param filename The file to load
     */
    public TipOverModel(String filename) {
        currentFile = filename;
        try {
            currentConfig = new TipOverConfig(filename);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Getter for value
     * @param row Row to get value from
     * @param col Column to get value from
     */
    public char getVal(int row, int col) {
        return currentConfig.getVal(row, col);
    }
    /**
     * Returns amount of rows
     */
    public int getRows() {
        return currentConfig.getRows();
    }
    /**
     * Returns amount of columns
     */
    public int getCols() {
        return currentConfig.getCols();
    }
    /**
     * Returns the coordinates of the tipper
     */
    public Point getTipper(){
        return currentConfig.getTipper();
    }
    /**
     * Returns the coordinates of the end
     */
    public Point getEnd(){
        return currentConfig.getEnd();
    }
    /**
     * Loads a board from file
     * @param filename File to load
     */
    public void loadBoardFromFile(String filename) {
        try{
            currentConfig = new TipOverConfig(filename);
            if(filename.equals(currentFile)) {
                alertObservers(RESET);
            }
            else{
                alertObservers(LOADED);
            }
            currentFile = filename;
        }
        catch (IOException e) {
            alertObservers(LOAD_FAILED);
        }
    }
    /**
     * Moves the tipper North
     */
    public void moveNorth() {
        if(gameOver()) {
            alertObservers(WON);
            return;
        }
        TipOverConfig newConfig = new TipOverConfig(currentConfig, "up");
        if(newConfig.isValid()){
            currentConfig = newConfig;
            alertObservers(currentConfig.getMessage());
        }
        else{
            alertObservers(newConfig.getMessage());
        }
    }
    /**
     * Moves the tipper South
     */
    public void moveSouth() {
        if(gameOver()) {
            alertObservers(WON);
            return;
        }
        TipOverConfig newConfig = new TipOverConfig(currentConfig, "down");
        if (newConfig.isValid()) {
            currentConfig = newConfig;
            alertObservers(currentConfig.getMessage());
        } else {
            alertObservers(newConfig.getMessage());
        }
    }
    /**
     * Moves the tipper East
     */
    public void moveEast() {
        if(gameOver()) {
            alertObservers(WON);
            return;
        }
        TipOverConfig newConfig = new TipOverConfig(currentConfig, "right");
        if (newConfig.isValid()) {
            currentConfig = newConfig;
            alertObservers(currentConfig.getMessage());
        } else {
            alertObservers(newConfig.getMessage());
        }
    }
    /**
     * Moves the tipper West
     */
    public void moveWest() {
        if(gameOver()) {
            alertObservers(WON);
            return;
        }
        TipOverConfig newConfig = new TipOverConfig(currentConfig, "left");
        if (newConfig.isValid()) {
            currentConfig = newConfig;
            alertObservers(currentConfig.getMessage());
        } else {
            alertObservers(newConfig.getMessage());
        }
    }
    /**
     * Checks the status of the game
     */
    public boolean gameOver() {
        return currentConfig.isSolution();
    }
    /**
     * Gets the next hint
     */
    public void getHint() {
        String direction = null;
        if(gameOver()) {
            alertObservers(WON);
            return;
        }
        try {
            Collection<Configuration> hint = Solver.solve(currentConfig);
            direction = ((TipOverConfig) hint.toArray()[1]).getLastMove();
            switch(direction){
                case "up":
                    moveNorth();
                    alertObservers(HINT_PREFIX);
                    break;
                case "down":
                    moveSouth();
                    alertObservers(HINT_PREFIX);
                    break;
                case "left":
                    moveWest();
                    alertObservers(HINT_PREFIX);
                    break;
                case "right":
                    moveEast();
                    alertObservers(HINT_PREFIX);
                    break;
                default:
                {
                    alertObservers(NO_SOLUTION);
                    break;
                }
            }
        } catch (NullPointerException exception){
            alertObservers(NO_SOLUTION);
        }
    }
    /**
     * Resets the board
     */
    public void reset() {
        loadBoardFromFile(currentFile);
    }



    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<TipOverModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }
}
