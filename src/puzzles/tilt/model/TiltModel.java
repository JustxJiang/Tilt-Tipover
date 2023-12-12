package puzzles.tilt.model;

import puzzles.common.Coordinates;
import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


public class TiltModel {
    /** the collection of observers of this model */
    private final List<Observer<TiltModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private TiltConfig currentConfig;
    private TiltConfig temp;
    private String file;
    public static String HINT_PREFIX = "Next Step!";
    public static String LOAD_FAILED = "Load Failed";
    public static String LOADED = "loaded";

    /**
     * Constructor for Tilt Game Model
     * @param filename
     */
    public TiltModel(String filename) {
        try {
            currentConfig = new TiltConfig(filename);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads Board from file - Serves to help "Load Game"
     * @param filename
     * @return true if board is succesfully loaded from filename
     */
    public boolean loadBoardFromFile(String filename) {
        file = filename;
        try{
            currentConfig = new TiltConfig(filename);
            alertObservers(LOADED);
            return true;
        }
        catch (IOException e) {
            alertObservers(LOAD_FAILED);
            return false;
        }
    }

    /**
     * returns the next correct step
     */
    public void getHint(){
        Collection<Configuration> hint = Solver.solve(currentConfig);
        String direction = ((TiltConfig) hint.toArray()[1]).getLastMove();
        switch(direction){
            case "UP":
                tiltUp();
                break;
            case "DOWN":
                tiltDown();
                break;
            case "LEFT":
                tiltLeft();
                break;
            case "RIGHT":
                tiltRight();
        }
        alertObservers(HINT_PREFIX);
    }

    /**
     *
     * @param row
     * @param col
     * @return value at position(row, col)
     */
    public char getCell(int row, int col) {
        return currentConfig.getVal(row, col);
    }

    /**
     * Tilts the Board Up using TiltConfig
     */
    public void tiltUp(){
        temp = new TiltConfig(currentConfig, "none");
        currentConfig.tiltUp();

        int counter = 0;
        for (int i =0; i < currentConfig.getDimensions(); i++){
            for (int j = 0; j < currentConfig.getDimensions(); j++){
                if (currentConfig.getVal(i,j) == 'B'){
                    counter++;
                }
            }
        }

        if(TiltConfig.getBlueCounter() != counter){
            alertObservers("Illegal Move");
            currentConfig = new TiltConfig(temp, "none");
        }
        else{
            alertObservers("Tilt Up");

        }
    }

    /**
     * Tilts the Board Down using TiltConfig
     */
    public void tiltDown(){
        temp = new TiltConfig(currentConfig, "none");
        currentConfig.tiltDown();

        int counter = 0;
        for (int i =0; i < currentConfig.getDimensions(); i++){
            for (int j = 0; j < currentConfig.getDimensions(); j++){
                if (currentConfig.getVal(i,j) == 'B'){
                    counter++;
                }
            }
        }

        if(TiltConfig.getBlueCounter() != counter) {
            alertObservers("Illegal Move");
            currentConfig = new TiltConfig(temp, "none");
        }
        else{
            alertObservers("Tilt Down");
        }

    }

    /**
     * Tilts Board Left using TiltConfig
     */
    public void tiltLeft(){
        temp = new TiltConfig(currentConfig, "none");
        currentConfig.tiltLeft();

        int counter = 0;
        for (int i =0; i < currentConfig.getDimensions(); i++){
            for (int j = 0; j < currentConfig.getDimensions(); j++){
                if (currentConfig.getVal(i,j) == 'B'){
                    counter++;
                }
            }
        }

        if(TiltConfig.getBlueCounter() != counter){
            alertObservers("Illegal Move");
            currentConfig = new TiltConfig(temp, "none");
        }
        else{
            alertObservers("Tilt Left");
        }

    }

    /**
     * Tilts Board Right using TiltConfig
     */
    public void tiltRight(){
        temp = new TiltConfig(currentConfig, "none");
        currentConfig.tiltRight();

        int counter = 0;
        for (int i =0; i < currentConfig.getDimensions(); i++){
            for (int j = 0; j < currentConfig.getDimensions(); j++){
                if (currentConfig.getVal(i,j) == 'B'){
                    counter++;
                }
            }
        }

        if(TiltConfig.getBlueCounter() != counter){
            alertObservers("Illegal Move");
            currentConfig = new TiltConfig(temp, "none");
        }
        else{
            alertObservers("Tilt Right");
        }
    }

    /**
     * Resets the Board
     */
    public void reset(){
        loadBoardFromFile(file);
        alertObservers("reset");
    }

    /**
     *
     * @return true if the CurrentConfig is the Solution and indicates that the game is over
     */
    public boolean gameOver(){
        if(currentConfig.isSolution()){
            return true;
        }
        return false;
    }

    /**
     *
     * @return the dimensions of the board
     */
    public int getDimensions(){
        return currentConfig.getDimensions();
    }

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<TiltModel, String> observer) {
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
