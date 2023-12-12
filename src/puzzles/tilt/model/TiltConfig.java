package puzzles.tilt.model;


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
    private boolean hasMoved;
    public static int blueCounter = 0;

    private enum directions{NONE, UP, DOWN, LEFT, RIGHT};
    private directions lastMove;

    /**
     * Constructor of TiltConfig
     * @param filename
     * @throws IOException
     */
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
            lastMove = directions.NONE;
        }
    }

    /**
     * Second Constructor of TiltConfig
     * @param other
     * @param direction
     */
    protected TiltConfig(TiltConfig other, String direction){
        this.dimensions = other.grid.length;
        this.grid = new char[getDimensions()][getDimensions()];
        for (int i =0; i < getDimensions(); i++) {
            System.arraycopy(other.grid[i], 0, this.grid[i], 0, grid.length);
        }
        switch(direction){
            case "up":
                tiltUp();
                lastMove = directions.UP;
                break;

            case "down":
                tiltDown();
                lastMove = directions.DOWN;
                break;

            case "left":
                tiltLeft();
                lastMove = directions.LEFT;
                break;

            case "right":
                tiltRight();
                lastMove = directions.RIGHT;
                break;
            default:
                lastMove = directions.NONE;
        }
    }

    /**
     *
     * @return the last direction tilted / the last move
     */
    public String getLastMove(){
        return lastMove.toString();
    }

    /**
     * Tilts Board Up
     */
    public void tiltUp(){
        hasMoved = true;
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
    }

    /**
     * Tilts Board Down
     */
    public void tiltDown(){
        hasMoved = true;
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
    }

    /**
     * Tilts Board Right
     */
    public void tiltRight(){
        hasMoved = true;
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

    /**
     * Tilts Board Left
     */
    public void tiltLeft() {
        hasMoved = true;
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
    }

    /**
     *
     * @return dimensions of board
     */
    public int getDimensions(){
        return this.dimensions;
    }

    /**
     *
     * @return true if all Green tokens have gone in the hole and game is over
     */
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


    /**
     *
     * @return possible solutions / tilts the board in every direction
     */
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

    /**
     *
     * @return number of blue tokens
     */

    public static int getBlueCounter() {
        return blueCounter;
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