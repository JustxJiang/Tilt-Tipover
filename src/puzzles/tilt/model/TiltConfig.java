package puzzles.tilt.model;

import puzzles.clock.ClockConfig;
import puzzles.common.solver.Configuration;

import java.awt.image.TileObserver;
import java.util.ArrayList;
import java.util.Collection;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;

// TODO: implement your TiltConfig for the common solver
public class TiltConfig implements Configuration{
    private int dimensions;
    private char[][] grid;
    private int[] greenCursor = new int[2];
    private int[] hole = new int[2];
    private LinkedList<int[]> blueCursor = new LinkedList<>();
    private LinkedList<int[]> blocker = new LinkedList<>();

    public TiltConfig(String filename) throws IOException{
        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            String[] fields = in.readLine().split("\\s+");
            this.dimensions = Integer.parseInt(fields[0]);
            this.grid = new char[dimensions][dimensions];
            for (int j = 0; j < grid.length; j++) {
                fields = in.readLine().split("\\s+");
                for (int k = 0; k < grid[j].length; k++) {
                    grid[j][k] = (fields[k].charAt(0));
                }
            }

            for (int i=0; i<grid.length; i++){
                for (int j=0; j < grid[0].length; j++){
                    switch(getVal(i,j)){
                    case 'G':
                        this.greenCursor[0] = i;
                        this.greenCursor[1]=i;
                    case 'B':
                        int[] blue = new int[2];
                        blue[0] = i;
                        blue[1] = j;
                        blueCursor.add(blue);
                    case '*':
                        int[] block = new int[2];
                        block[0] = i;
                        block[1] = j;
                        this.blocker.add(block);
                    case 'O':
                        this.hole[0] = i;
                        this.hole[1] = j;
                    }

                }
            }
        }
    }

    protected TiltConfig(TiltConfig other, String direction){
        this.grid = new char[grid.length][grid.length];
        for (int i =0; i < grid.length; i++) {
            System.arraycopy(other.grid[i], 0, this.grid[i], 0, grid.length);
        }
        switch(direction){
            case "r++":

        }
    }

    @Override
    public boolean isSolution() {
        return false;
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        LinkedList<Configuration> neighbors = new LinkedList<>();

        neighbors.add(new TiltConfig(this, "r++"));
        neighbors.add(new TiltConfig(this, "r--"));
        neighbors.add(new TiltConfig(this, "c++"));
        neighbors.add(new TiltConfig(this, "c--"));

        return neighbors;

    }

    /**
     *
     * @return number of rows/cols
     */
    public int getDimensions() {
        return this.dimensions;
    }


    /**
     * @param row the row
     * @param col the column
     * @return value at (row, col)
     */

    public char getVal(int row, int col) {
        return grid[row][col];
    }

    /**
     * @return current position of green tile
     */

    public int[] getGreenCursor() {
        return this.greenCursor;
    }



    @Override
    public int hashCode() {
        int hash = getGreenCursor()[0] + getGreenCursor()[1];
        for (int[] blue : this.blueCursor){
            hash += blue[0] + blue[1];
        }
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int j=0; j<grid.length; j++){
            for (int k = 0; k < grid.length; k++){
                result.append(getVal(j,k)).append(" ");
            }
            if (j != grid.length-1){
                result.append(System.lineSeparator());
            }
        }
        return result.toString();
    }
}