package puzzles.tilt.solver;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.tilt.model.TiltConfig;

import java.io.IOException;
import java.util.Collection;

public class Tilt {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Tilt filename");
        }
        else{
            System.out.println("Puzzle: " + args[0]);
            try{
                Configuration init;
                init = new TiltConfig(args[0]);
                System.out.println(init);
                Collection<Configuration> result = Solver.solve(init);
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
            }catch(IOException ioe){
                System.err.println(ioe.getMessage());
            }
        }

    }
}
