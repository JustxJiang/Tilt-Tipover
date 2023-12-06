package puzzles.tipover.solver;

import puzzles.tipover.model.TipOverConfig;

public class TipOver {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TipOver filename");
        }
        else
        {
            TipOverConfig tipover = new TipOverConfig(args[0]);
            System.out.println(tipover);
        }
    }
}
