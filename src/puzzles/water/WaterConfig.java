package puzzles.water;


import puzzles.common.solver.Configuration;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class WaterConfig implements Configuration {

    private int desiredAmount;
    private LinkedList<Integer> buckets = new LinkedList<>();

    /**
     * creates instance of WaterConfig
     * @param amount
     * @param buckets
     */
    public WaterConfig(int amount, List<Integer> buckets){
        this.desiredAmount = amount;
        this.buckets.addAll(buckets);
    }

    /**
     *
     * @return neighbors of ClockConfig objectx
     */
    public Collection<Configuration> getNeighbors(){
        LinkedList<Configuration> neighbors = new LinkedList<>();
        LinkedList<Integer> temp = Water.bucketList;
        for (int i = 0; i < this.buckets.size(); i++){

            //A way to restore the temporary list
            if (temp.size() != this.buckets.size()){
                temp = this.buckets;
            }
            Integer current = temp.remove(i);

            //fill
            if(current != this.buckets.get(i)){
                temp.add(i, this.buckets.get(i));
                neighbors.add(new WaterConfig(desiredAmount, temp));
                temp = this.buckets;
                current = temp.remove(i);
            }

            //empty
            if (current != 0){
                temp.add(i, this.buckets.get(i));
                neighbors.add(new WaterConfig(desiredAmount, temp));
                temp = this.buckets;
                current = temp.remove(i);
            }
        }
        return neighbors;
    }

    public boolean isSolution(){
        if (this.buckets.contains(desiredAmount)){
            return true;
        }
        return false;
    }


    @Override
    public boolean equals(Object other){
        boolean result = true;
        if (other instanceof WaterConfig) {
            LinkedList<Integer> compare = ((WaterConfig) other).buckets;
            for (int i = 0; i < compare.size();i++){
                if (!(this.buckets.get(i) == compare.get(i))){
                    result = false;
                }
            }
            return result;
        }
        return false;
    }

    @Override
    public int hashCode(){
        int counter = 0;
        for (int x:this.buckets){
            counter+=x;
        }
        return counter;
    }
    @Override
    public String toString(){
        return String.valueOf(this.buckets);
    }


}
