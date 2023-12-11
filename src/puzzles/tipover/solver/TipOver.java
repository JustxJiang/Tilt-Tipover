package puzzles.tipover.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.tilt.model.TiltConfig;
import puzzles.tipover.model.TipOverConfig;

import java.io.IOException;
import java.util.Collection;

public class TipOver {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java TipOver filename");
        }
        else
        {
            System.out.println("Puzzle: " + args[0]);
            TipOverConfig tipover = new TipOverConfig(args[0]);
            System.out.println(tipover);
            Collection<Configuration> result = Solver.solve(tipover);
            if (result == null){
                System.out.println("No Solution");
            }
            else{
                int x = 0;
                for (Configuration r : result){
                    System.out.println("\nStep " + x + ":\n" + r );
                    x++;
                }
            }
        }
    }
}
