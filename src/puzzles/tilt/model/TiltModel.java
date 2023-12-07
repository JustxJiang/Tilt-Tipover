package puzzles.tilt.model;

import puzzles.common.Observer;

import java.util.LinkedList;
import java.util.List;

public class TiltModel {
    /** the collection of observers of this model */
    private final List<Observer<TiltModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private TiltConfig currentConfig;
    private int moves;

    public TiltModel(){
        moves = 0;
        observers = new LinkedList<>();
        currentConfig = new TiltConfig();
    }
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
