package puzzles.tipover.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.tilt.model.TiltConfig;
import puzzles.tipover.solver.TipOver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class TipOverModel {
    /** the collection of observers of this model */
    private final List<Observer<TipOverModel, String>> observers = new LinkedList<>();
    /** the current configuration */
    private TipOverConfig currentConfig;
    private String currentFile;
    public static String LOADED = "loaded";
    public static String LOAD_FAILED = "loadFailed";

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
    public boolean loadBoardFromFile(String filename) {
        try{
            currentConfig = new TipOverConfig(filename);
            alertObservers(LOADED);
            return true;
        }
        catch (IOException e) {
            alertObservers(LOAD_FAILED);
            return false;
        }
    }

    public void moveNorth() {
        TipOverConfig newConfig = new TipOverConfig(currentConfig, "UP");
        if(newConfig.isValid()){
            alertObservers("Move North");
            currentConfig = newConfig;
        }
        else{
            alertObservers("Invalid Move");
        }
    }
    public void moveSouth() {
        TipOverConfig newConfig = new TipOverConfig(currentConfig, "DOWN");
        if(newConfig.isValid()){
            alertObservers("Move South");
            currentConfig = newConfig;
        }
        else{
            alertObservers("Invalid Move");
        }
    }
    public void moveEast() {
        TipOverConfig newConfig = new TipOverConfig(currentConfig, "Right");
        if(newConfig.isValid()){
            alertObservers("Move East");
            currentConfig = newConfig;
        }
        else{
            alertObservers("Invalid Move");
        }
    }
    public void moveWest() {
        TipOverConfig newConfig = new TipOverConfig(currentConfig, "Left");
        if(newConfig.isValid()){
            alertObservers("Move West");
            currentConfig = newConfig;
        }
        else{
            alertObservers("Invalid Move");
        }
    }
    public boolean gameOver() {
        return currentConfig.isSolution();
    }
    public void getHint() {

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
