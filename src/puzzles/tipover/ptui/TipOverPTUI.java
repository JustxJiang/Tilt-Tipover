package puzzles.tipover.ptui;

import puzzles.common.Observer;
import puzzles.tipover.model.TipOverModel;
import puzzles.tipover.model.TipOverModel;

import java.util.Scanner;

public class TipOverPTUI implements Observer<TipOverModel, String> {
    private TipOverModel model;
    private Scanner in;
    private boolean gameOn;


    public TipOverPTUI() {
        model = new TipOverModel();
        model.addObserver(this);
        in = new Scanner(System.in);
        gameOn = false;
    }
    

    @Override
    public void update(TipOverModel model, String message) {
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TipOverPTUI filename");
        }
        TipOverPTUI ptui =new TipOverPTUI();
    }
}
