package puzzles.tilt.model;

import puzzles.common.solver.Configuration;
import java.util.Collection;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// TODO: implement your TiltConfig for the common solver
public class TiltConfig implements Configuration{
    private int dimensions;
    private char[][] grid;

    public TiltConfig(String filename) throws IOException{
        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            String[] fields = in.readLine().split("\\s+");
            dimensions = Integer.parseInt(fields[0]);
            grid = new char[dimensions][dimensions];
            for (int j = 0; j < grid.length; j++) {
                fields = in.readLine().split("\\s+");
                for (int k = 0; k < grid[j].length; k++) {
                    grid[j][k] = (fields[k].charAt(0));
                }
            }
        }
    }

    @Override
    public boolean isSolution() {
        return false;
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        return null;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int j=0; j<grid.length; j++){
            for (int k = 0; k < grid.length; k++){
                result.append(grid[j][k]).append(" ");
            }
            if (j != grid.length-1){
                result.append(System.lineSeparator());
            }
        }
        return result.toString();
    }
}