package puzzles.tilt.model;

import puzzles.clock.ClockConfig;
import puzzles.common.solver.Configuration;

import java.awt.image.TileObserver;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// TODO: implement your TiltConfig for the common solver
public class TiltConfig implements Configuration{
    private int dimensions;
    private char[][] grid;
    private static int blueCounter = 0;

    public TiltConfig(String filename) throws IOException{
        blueCounter = 0;
        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            String[] fields = in.readLine().split("\\s+");
            this.dimensions = Integer.parseInt(fields[0]);
            this.grid = new char[dimensions][dimensions];
            for (int j = 0; j < dimensions; j++) {
                fields = in.readLine().split("\\s+");
                for (int k = 0; k < dimensions; k++) {
                    grid[j][k] = (fields[k].charAt(0));
                    if (grid[j][k] == 'B'){
                        blueCounter++;
                    }
                }
            }
        }
    }

    protected TiltConfig(TiltConfig other, String direction){
        this.dimensions = other.grid.length;
        this.grid = new char[getDimensions()][getDimensions()];
        for (int i =0; i < getDimensions(); i++) {
            System.arraycopy(other.grid[i], 0, this.grid[i], 0, grid.length);
        }
        boolean hasMoved = true;
        switch(direction){
            case "up":
                while(hasMoved){
                    hasMoved = false;
                    for (int i =0; i < getDimensions(); i++){
                        for (int j = 0; j < getDimensions(); j++){
                            if ((i-1 >= 0) && ((getVal(i, j) == 'G') || (getVal(i,j) == 'B'))){
                                char temp = getVal(i,j);
                                switch(getVal(i-1, j)){
                                    case '.':
                                        this.grid[i][j] = '.';
                                        this.grid[i-1][j] = temp;
                                        hasMoved = true;
                                        break; //remember to break in cases cuz if not it will still loop through all the cases
                                    case 'O':
                                        this.grid[i][j] = '.';
                                        hasMoved = true;
                                        break;
                                }
                            }
                        }
                    }
                }

                break;

            case "down":
                while(hasMoved){
                    hasMoved = false;

                    for (int i =0; i < getDimensions(); i++){
                        for (int j = 0; j < getDimensions(); j++){
                            if ((i+1 < getDimensions()) && ((getVal(i, j) == 'G') || (getVal(i,j) == 'B'))){
                                char temp = getVal(i,j);
                                switch(getVal(i+1, j)){
                                    case '.':
                                        this.grid[i][j] = '.';
                                        this.grid[i+1][j] = temp;
                                        hasMoved = true;
                                        break; //remember to break in cases cuz if not it will still loop through all the cases
                                    case 'O':
                                        this.grid[i][j] = '.';
                                        hasMoved = true;
                                        break;
                                }
                            }
                        }
                    }
                }

                break;

            case "left":
                while(hasMoved){
                    hasMoved = false;
                    for (int i =0; i < getDimensions(); i++){
                        for (int j = 0; j < getDimensions(); j++){
                            if ((j-1 >= 0) && ((getVal(i, j) == 'G') || (getVal(i,j) == 'B'))){
                                char temp = getVal(i,j);
                                switch(getVal(i, j-1)){
                                    case '.':
                                        this.grid[i][j] = '.';
                                        this.grid[i][j-1] = temp;
                                        hasMoved = true;
                                        break; //remember to break in cases cuz if not it will still loop through all the cases
                                    case 'O':
                                        this.grid[i][j] = '.';
                                        hasMoved = true;
                                        break;
                                }
                            }
                        }
                    }
                }

                break;

            case "right":
                while(hasMoved){
                    hasMoved = false;
                    for (int i =0; i < getDimensions(); i++){
                        for (int j = 0; j < getDimensions(); j++){
                            if ((j+1 < getDimensions()) && ((getVal(i, j) == 'G') || (getVal(i,j) == 'B'))){
                                char temp = getVal(i,j);
                                switch(getVal(i, j+1)){
                                    case '.':
                                        this.grid[i][j] = '.';
                                        this.grid[i][j+1] = temp;
                                        hasMoved = true;
                                        break; //remember to break in cases cuz if not it will still loop through all the cases
                                    case 'O':
                                        this.grid[i][j] = '.';
                                        hasMoved = true;
                                        break;
                                }
                            }
                        }
                    }
                }
        }
    }

    public int getDimensions(){
        return this.dimensions;
    }

    @Override
    public boolean isSolution() {
        for (int i =0; i < getDimensions(); i++){
            for (int j = 0; j < getDimensions(); j++){
                if ((getVal(i,j) == 'G')) {
                    return false;
                }
            }
        }
        return true;
    }
    /**
     * @param row the row
     * @param col the column
     * @return value at (row, col)
     */

    public char getVal(int row, int col) {
        return grid[row][col];
    }



    @Override
    public Collection<Configuration> getNeighbors() {
        LinkedList<Configuration> neighbors = new LinkedList<>();

        neighbors.add(new TiltConfig(this, "up"));
        neighbors.add(new TiltConfig(this, "down"));
        neighbors.add(new TiltConfig(this, "left"));
        neighbors.add(new TiltConfig(this, "right"));

        // removes neighbor if there are not enough blues

        for (int k = 0; k < neighbors.size(); k++) {
            TiltConfig n = (TiltConfig) neighbors.get(k);
            int counter = 0;
            for (int i =0; i < getDimensions(); i++){
                for (int j = 0; j < getDimensions(); j++){
                    if (n.getVal(i,j) == 'B'){
                        counter++;
                    }
                }
            }

            if(blueCounter != counter){
                neighbors.remove(n);
                k--;
            }
        }
        return neighbors;

    }



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        TiltConfig that = (TiltConfig) o;
        return dimensions == that.dimensions && blueCounter == that.blueCounter && Arrays.deepEquals(grid, that.grid);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(dimensions, blueCounter);
        result = 31 * result + Arrays.deepHashCode(grid);
        return result;
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