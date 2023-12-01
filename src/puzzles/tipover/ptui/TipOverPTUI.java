package puzzles.tipover.ptui;

import puzzles.common.Observer;
import puzzles.tipover.model.TipOverModel;
import puzzles.tipover.model.TipOverModel;

public class TipOverPTUI implements Observer<TipOverModel, String> {
    private TipOverModel model;

    @Override
    public void update(TipOverModel model, String message) {
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TipOverPTUI filename");
        }
    }
}
