package puzzles.clock;

import puzzles.common.solver.Configuration;

import java.util.Collection;
import java.util.LinkedList;


public class ClockConfig implements Configuration {

    private static int numHours;
    private static int startTime;
    private static int endTime;
    private int hour;

    /**
     * creates an instance of ClockConfig!!
     * @param numHours
     * @param start
     * @param end
     */
    public ClockConfig(int numHours, int start, int end){
        this.numHours = numHours;
        this.startTime = start;
        this.endTime = end;
        this.hour = start;
    }

    public ClockConfig(int hour){
        this.hour = hour;
    }

    public int getHour(){
        return this.hour;
    }

    /**
     *
     * @return neighbors of ClockConfig object
     */
    public Collection<Configuration> getNeighbors(){
        LinkedList<Configuration> neighbors = new LinkedList<>();
        int n1, n2;
        if (this.getHour() > 1 && this.getHour()<numHours){
            n1 = this.getHour() - 1;
            n2 = this.getHour() + 1;
        }
        else{
            if (hour == 1){
                n1 = numHours;
                n2 = hour + 1;
            }
            else{
                n1 = hour - 1;
                n2 = 1;
            }
        }
        neighbors.add(new ClockConfig(n1));
        neighbors.add(new ClockConfig(n2));
        return neighbors;

    }

    public boolean isSolution(){
        if (this.getHour() == endTime){
            return true;
        }
        return false;
    }
    @Override
    public boolean equals(Object other){
        if (other instanceof ClockConfig) {
            return this.getHour() == ((ClockConfig)other).getHour();
        }
        return false;
    }
    @Override
    public int hashCode(){
        return this.hour;
    }
    @Override
    public String toString(){
        return String.valueOf(this.hour);
    }


}
