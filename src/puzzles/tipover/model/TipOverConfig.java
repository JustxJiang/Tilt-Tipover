package puzzles.tipover.model;

import puzzles.common.solver.Configuration;
import puzzles.tilt.model.TiltConfig;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

public class TipOverConfig implements Configuration {
    private int puzzleRows;
    private int puzzleCols;
    private char[][] puzzleGrid;
    private Point start = new Point();
    private Point end = new Point();
    private Point tipper = new Point();
    private String lastMove;
    private String message;
    private boolean isValid;

    /**
     * Constructor for TipOver
     * @param filename The filename to be used
     */
    public TipOverConfig(String filename) throws IOException {
        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            String[] fields = in.readLine().split("\\s+");
            puzzleRows = Integer.parseInt(fields[0]); //Number of rows in the puzzle
            puzzleCols = Integer.parseInt(fields[1]); //Number of columns in the puzzle
            puzzleGrid = new char[puzzleRows][puzzleCols];
            start.x = Integer.parseInt(fields[2]); //Starting row number
            start.y = Integer.parseInt(fields[3]); //Starting column number
            tipper = start;
            end.x = Integer.parseInt(fields[4]); //Ending row number
            end.y = Integer.parseInt(fields[5]); //Ending column number
            fields = in.readLine().split("\\s+");
            for (int row = 0; row < puzzleRows; row++) {
                for (int col = 0; col < puzzleCols; col++) {
                    puzzleGrid[row][col] = fields[col].charAt(0);
                }
                fields = in.readLine().split("\\s+");
            }
        }
    }

    //This might be the worst code I've ever written
    //Code could be drastically shortened by splitting into separate functions, but I am too afraid to break it
    /**
     * Alternate constructor for TipOver
     * @param other Creates a copy of the board and moves in the specified direction
     * @param direction The direction to move the tipper
     */
    public TipOverConfig(TipOverConfig other, String direction) {
        boolean onTower = false;
        this.puzzleRows = other.getRows();
        this.puzzleCols = other.getCols();
        this.puzzleGrid = new char[this.getRows()][this.getCols()];
        this.tipper = new Point(other.tipper.x, other.tipper.y);
        this.start = new Point(other.start.x, other.start.y);
        this.end = new Point(other.end.x, other.end.y);
        this.isValid = true;
        this.message = "";


        for (int i = 0; i < getRows(); i++) {
            System.arraycopy(other.puzzleGrid[i], 0, this.puzzleGrid[i], 0, getCols());
        }
        if (Character.getNumericValue(this.getVal(tipper.x, tipper.y)) > 1) {
            onTower = true;
        }
        //Switch cases could have been split into a separate method to reduce size
        switch (direction) {
            case "up":
                lastMove = "up";
                if (onTower) {
                    boolean valid = true;
                    int towerHeight = Character.getNumericValue(this.getVal(tipper.x, tipper.y));
                    //Tests in bounds
                    if (tipper.x - (towerHeight) < 0) {
                        message = "Out of bounds!";
                        valid = false;
                    } else {
                        //Tests for obstruction
                        for (int test = 1; test <= towerHeight; test++) {
                            if (Character.getNumericValue(this.getVal(tipper.x - test, tipper.y)) > 0) {
                                valid = false;
                                message = "Tower is obstructed!";
                                break;
                            }
                        }
                    }
                    if (valid) {
                        this.puzzleGrid[tipper.x][tipper.y] = '0';
                        this.tipper.x--;
                        for (int set = 0; set < towerHeight; ++set) {
                            this.puzzleGrid[tipper.x - set][tipper.y] = '1';
                        }
                        message = "A tower has been tipped over!";
                    } else if (tipper.x - 1 >= 0) {
                        if (Character.getNumericValue(this.getVal(tipper.x - 1, tipper.y)) > 0) {
                            tipper.x--;
                        } else {
                            message = "Out of bounds!";
                            this.isValid = false;
                        }
                    } else {
                        message = "Out of bounds!";
                        this.isValid = false;
                    }
                    if (this.puzzleGrid[tipper.x][tipper.y] == '0') {
                        message = "No crate or tower there!";
                        this.isValid = false;
                    }
                } else if (tipper.x - 1 >= 0) {
                    if (Character.getNumericValue(this.getVal(tipper.x - 1, tipper.y)) > 0) {
                        tipper.x--;
                    } else {
                        message = "No crate or tower there!";
                        this.isValid = false;
                    }
                } else {
                    message = "Out of bounds!";
                    this.isValid = false;
                }
                break;
                //Lots of duplicate code which could be removed and changed into a singular function
            case "down":
                lastMove = "down";
                if (onTower) {
                    boolean valid = true;
                    int towerHeight = Character.getNumericValue(this.getVal(tipper.x, tipper.y));
                    //Tests in bounds
                    if (tipper.x + (towerHeight) >= this.puzzleRows) {
                        message = "Out of bounds!";
                        valid = false;
                    } else {
                        //Tests for obstruction
                        for (int test = 1; test <= towerHeight; test++) {
                            if (Character.getNumericValue(this.getVal(tipper.x + test, tipper.y)) > 0) {
                                valid = false;
                                message = "Tower is obstructed!";
                                break;
                            }
                        }
                    }
                    if (valid) {
                        this.puzzleGrid[tipper.x][tipper.y] = '0';
                        this.tipper.x++;
                        for (int set = 0; set < towerHeight; ++set) {
                            this.puzzleGrid[tipper.x + set][tipper.y] = '1';
                        }
                        message = "A tower has been tipped over!";
                    } else if (tipper.x + 1 < this.getRows()) {
                        if (Character.getNumericValue(this.getVal(tipper.x + 1, tipper.y)) > 0) {
                            tipper.x++;
                        } else {
                            message = "Out of bounds!";
                            this.isValid = false;
                        }
                    } else {
                        message = "Out of bounds!";
                        this.isValid = false;
                    }
                    if (this.puzzleGrid[tipper.x][tipper.y] == '0') {
                        message = "No crate or tower there!";
                        this.isValid = false;
                    }

                } else if (tipper.x + 1 < this.getRows()) {
                    if (Character.getNumericValue(this.getVal(tipper.x + 1, tipper.y)) > 0) {
                        tipper.x++;
                    } else {
                        message = "No crate or tower there!";
                        this.isValid = false;
                    }
                } else {
                    message = "Out of bounds!";
                    this.isValid = false;
                }
                break;
            case "left":
                lastMove = "left";
                if (onTower) {
                    boolean valid = true;
                    int towerHeight = Character.getNumericValue(this.getVal(tipper.x, tipper.y));
                    //Tests in bounds
                    if (tipper.y - (towerHeight) < 0) {
                        message = "Out of bounds!";
                        valid = false;
                    } else {
                        //Tests for obstruction
                        for (int test = 1; test <= towerHeight; test++) {
                            if (Character.getNumericValue(this.getVal(tipper.x, tipper.y - test)) > 0) {
                                valid = false;
                                message = "Tower is obstructed!";
                                break;
                            }
                        }
                    }
                    if (valid) {
                        this.puzzleGrid[tipper.x][tipper.y] = '0';
                        this.tipper.y--;
                        for (int set = 0; set < towerHeight; ++set) {
                            this.puzzleGrid[tipper.x][tipper.y - set] = '1';
                        }
                        message = "A tower has been tipped over!";
                    } else if (tipper.y - 1 >= 0) {
                        if (Character.getNumericValue(this.getVal(tipper.x, tipper.y - 1)) > 0) {
                            tipper.y--;
                        } else {
                            message = "Out of bounds!";
                            this.isValid = false;
                        }
                    } else {
                        message = "Out of bounds!";
                        this.isValid = false;
                    }
                    if (this.puzzleGrid[tipper.x][tipper.y] == '0') {
                        message = "No crate or tower there!";
                        this.isValid = false;
                    }
                } else if (tipper.y - 1 >= 0) {
                    if (Character.getNumericValue(this.getVal(tipper.x, tipper.y - 1)) > 0) {
                        tipper.y--;
                    } else {
                        message = "No crate or tower there!";
                        this.isValid = false;
                    }
                } else {
                    message = "Out of bounds!";
                    this.isValid = false;
                }
                break;
            case "right":
                lastMove = "right";
                if (onTower) {
                    boolean valid = true;
                    int towerHeight = Character.getNumericValue(this.getVal(tipper.x, tipper.y));
                    //Tests in bounds
                    if (tipper.y + (towerHeight) >= this.puzzleCols) {
                        message = "Out of bounds!";
                        valid = false;
                    } else {
                        //Tests for obstruction
                        for (int test = 1; test <= towerHeight; test++) {
                            if (Character.getNumericValue(this.getVal(tipper.x, tipper.y + test)) > 0) {
                                valid = false;
                                message = "Tower is obstructed!";
                                break;
                            }
                        }
                    }
                    if (valid) {
                        this.puzzleGrid[tipper.x][tipper.y] = '0';
                        this.tipper.y++;
                        for (int set = 0; set < towerHeight; ++set) {
                            this.puzzleGrid[tipper.x][tipper.y + set] = '1';
                        }
                        message = "A tower has been tipped over!";
                    } else if (tipper.y + 1 < this.getCols()) {
                        if (Character.getNumericValue(this.getVal(tipper.x, tipper.y + 1)) > 0) {
                            tipper.y++;
                        } else {
                            message = "Out of bounds!";
                            this.isValid = false;
                        }
                    } else {
                        message = "Out of bounds!";
                        this.isValid = false;
                    }
                    if (this.puzzleGrid[tipper.x][tipper.y] == '0') {
                        message = "No crate or tower there!";
                        this.isValid = false;
                    }
                } else if (tipper.y + 1 < this.getCols()) {
                    if (Character.getNumericValue(this.getVal(tipper.x, tipper.y + 1)) > 0) {
                        tipper.y++;
                    } else {
                        message = "No crate or tower there!";
                        this.isValid = false;
                    }
                } else {
                    message = "Out of bounds!";
                    this.isValid = false;
                }
                break;
                //I wish I had time to fix this
        }
    }
    /**
     * Override equals method
     */
    @Override
    public boolean equals(Object o) {
        TipOverConfig that = (TipOverConfig) o;
        return puzzleRows == that.puzzleRows && puzzleCols == that.puzzleCols && Arrays.deepEquals(puzzleGrid, that.puzzleGrid) && tipper.x == that.tipper.x && tipper.y == that.tipper.y;
    }
    /**
     * Override hashCode method
     */
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(puzzleGrid) + Objects.hashCode(tipper.x) + Objects.hashCode(tipper.y);
    }
    /**
     * Override isSolution method
     */
    @Override
    public boolean isSolution() {
        return (tipper.x == end.x && tipper.y == end.y);
    }
    /**
     * Override getNeighbors method
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        LinkedList<Configuration> neighbors = new LinkedList<>();
        String[] directions = {"up", "down", "left", "right"};
        for (String direction : directions) {
            TipOverConfig config = new TipOverConfig(this, direction);
            if (config.isValid) {
                neighbors.add(config);
            }
        }
        return neighbors;
    }
    /**
     * Gets the number of rows
     */
    public int getRows() {
        return this.puzzleRows;
    }
    /**
     * Gets the number of cols
     */
    public int getCols() {
        return this.puzzleCols;
    }

    /**
     * Gets the specified value
     * @param row Row to get
     * @param col Column to get
     */
    public char getVal(int row, int col) {
        return this.puzzleGrid[row][col];
    }
    /**
     * Checks the validity of the move
     */
    public boolean isValid()
    {
        return isValid;
    }
    /**
     * Gets the coordinates of the tipper
     */
    public Point getTipper() {
        return tipper;
    }
    /**
     * Gets the coordinates of the end
     */
    public Point getEnd() {
        return end;
    }
    /**
     * Gets the previous move
     */
    public String getLastMove(){
        return lastMove;
    }
    /**
     * Gets the message supplied by the new move
     */
    public String getMessage(){
        return message;
    }
    /**
     * Override toString method
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int row = 0; row < getRows(); ++row) {
            for (int col = 0; col < getCols(); ++col) {
                if (row == tipper.x && col == tipper.y) {
                    result.append("*");
                } else if (row == end.x && col == end.y) {
                    result.append("!");
                } else {
                    result.append(" ");
                }
                result.append(getVal(row, col)).append(" ");
            }
            if (row != getRows() - 1) {
                result.append(System.lineSeparator());
            }
        }
        return result.toString();
    }
}
