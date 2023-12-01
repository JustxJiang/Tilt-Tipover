package puzzles.clock;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.Collection;

/**
 * Main class for the clock puzzle.
 *
 * @author JUSTEN JIANG and Alex
 */

public class Clock {
    /**
     * Run an instance of the clock puzzle.
     * Creates a ClockConfig and throws that ClockConfig into the Solver.
     *
     * @param args [0]: the number of hours in the clock;
     *             [1]: the starting hour;
     *             [2]: the finish hour.
     */


    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println(("Usage: java Clock hours start finish"));
        } else {
            // TODO YOUR MAIN CODE HERE
            System.out.println("Hours: " + args[0] + ", Start: " + args[1] + ", End: " + args[2]);
            ClockConfig clock = new ClockConfig(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            Collection<Configuration> result = Solver.solve(clock);
            if (result == null){
                System.out.println("No Solution");
            }
            else{
            int x = 0;
                for (Configuration r : result){
                    System.out.println("Step " + x + ": " + r);
                    x++;
                }
            }
        }
    }


}
