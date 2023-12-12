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

    public TipOverModel(String filename) {
        currentFile = filename;
        try {
            currentConfig = new TipOverConfig(filename);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public char getVal(int row, int col) {
        return currentConfig.getVal(row, col);
    }
    public int getRows() {
        return currentConfig.getRows();
    }
    public int getCols() {
        return currentConfig.getCols();
    }

    public Point getTipper(){
        return currentConfig.getTipper();
    }
    public Point getEnd(){
        return currentConfig.getEnd();
    }

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
    public boolean gameOver() {
        return currentConfig.isSolution();
    }
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
