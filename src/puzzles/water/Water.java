package puzzles.water;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Main class for the water buckets puzzle.
 *
 * @author JUSTEN JIANG
 */
public class Water {

    /**
     * Run an instance of the water buckets puzzle.
     *
     * @param args [0]: desired amount of water to be collected;
     *             [1..N]: the capacities of the N available buckets.
     */
    static LinkedList<Integer> bucketList = new LinkedList<>();
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(
                    ("Usage: java Water amount bucket1 bucket2 ...")
            );
        } else {
            // TODO YOUR MAIN CODE HERE
            for(int i = 1; i < args.length;i++) {
                bucketList.add(Integer.parseInt(args[i]));
            }

            System.out.println("Amount: " + args[0] + ", Buckets: " + bucketList);
            WaterConfig water = new WaterConfig(Integer.parseInt(args[0]), bucketList);

//            Collection<Configuration> result = Solver.solve(water);
//            if (result == null){
//                System.out.println("No Solution");
//            }leave for now
//            else{
//                int x = 0;
//                for (Configuration r : result){
//                    System.out.println("Step " + x + ": " + r);
//                    x++;
//                }
//            }
        }
    }
}
