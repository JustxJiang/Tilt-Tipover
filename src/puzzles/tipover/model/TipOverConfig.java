package puzzles.tipover.model;

// TODO: implement your TipOverConfig for the common solver

import puzzles.common.solver.Configuration;
import puzzles.tilt.model.TiltConfig;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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


    public TipOverConfig(String filename)
    {
        try (BufferedReader in = new BufferedReader(new FileReader(filename)))
        {
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
            for(int row = 0; row < puzzleRows; row++)
            {
                for(int col = 0; col < puzzleCols; col++)
                {
                    puzzleGrid[row][col] = fields[col].charAt(0);
                }
                fields = in.readLine().split("\\s+");
            }
        }
        catch (IOException e)
        {
            System.out.println("Error: File not found!");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipOverConfig that = (TipOverConfig) o;
        return puzzleRows == that.puzzleRows && puzzleCols == that.puzzleCols && Arrays.equals(puzzleGrid, that.puzzleGrid) && Objects.equals(start, that.start) && Objects.equals(end, that.end) && Objects.equals(tipper, that.tipper);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(puzzleRows, puzzleCols, start, end, tipper);
        result = 31 * result + Arrays.hashCode(puzzleGrid);
        return result;
    }

    public TipOverConfig(TipOverConfig other, String direction)
    {
        boolean onTower = false;
        this.puzzleRows = other.getRows();
        this.puzzleCols = other.getCols();
        this.puzzleGrid = new char[this.getRows()][this.getCols()];


        for(int i = 0; i < getRows(); i++)
        {
            System.arraycopy(other.puzzleGrid[i], 0, this.puzzleGrid[i], 0, getCols());
        }
        if((int)this.getVal(tipper.x,tipper.y) > 1) {
            onTower = true;
        }
        switch(direction) {
            case "up":
                if(onTower) {
                    boolean valid = true;
                    int towerHeight = (int)this.getVal(tipper.x,tipper.y);
                    //Tests in bounds
                    if(tipper.x - (towerHeight + 1) < 0) {
                        valid = false;
                    }
                    else {
                        //Tests for obstruction
                        for (int test = 0; test <= towerHeight; test++) {
                            if ((int) this.getVal(tipper.x - test, tipper.y) > 0) {
                                valid = false;
                                break;
                            }
                        }
                    }
                    if(valid) {
                        this.puzzleGrid[tipper.x][tipper.y] = '0';
                        this.tipper.x--;
                        for(int set = 0; set <= towerHeight; ++set) {
                            this.puzzleGrid[tipper.x-set][tipper.y] = '1';
                        }
                    }
                }
                else {
                        this.tipper.x--;
                }
                break;
            case "down":
                if(onTower) {
                    boolean valid = true;
                    int towerHeight = (int)this.getVal(tipper.x,tipper.y);
                    //Tests in bounds
                    if(tipper.x + (towerHeight + 1) > this.puzzleCols) {
                        valid = false;
                    }
                    else {
                        //Tests for obstruction
                        for (int test = 0; test <= towerHeight; test++) {
                            if ((int) this.getVal(tipper.x + test, tipper.y) > 0) {
                                valid = false;
                                break;
                            }
                        }
                    }
                    if(valid) {
                        this.puzzleGrid[tipper.x][tipper.y] = '0';
                        this.tipper.x++;
                        for(int set = 0; set <= towerHeight; ++set) {
                            this.puzzleGrid[tipper.x+set][tipper.y] = '1';
                        }
                    }
                }
                else {
                    this.tipper.x++;
                }
                break;
            case "left":
                if(onTower) {
                    boolean valid = true;
                    int towerHeight = (int)this.getVal(tipper.x,tipper.y);
                    //Tests in bounds
                    if(tipper.y - (towerHeight + 1) < 0) {
                        valid = false;
                    }
                    else {
                        //Tests for obstruction
                        for (int test = 0; test <= towerHeight; test++) {
                            if ((int) this.getVal(tipper.x, tipper.y - test) > 0) {
                                valid = false;
                                break;
                            }
                        }
                    }
                    if(valid) {
                        this.puzzleGrid[tipper.x][tipper.y] = '0';
                        this.tipper.y--;
                        for(int set = 0; set <= towerHeight; ++set) {
                            this.puzzleGrid[tipper.x][tipper.y-set] = '1';
                        }
                    }
                }
                else {
                    this.tipper.y--;
                }
                break;
            case "right":
                if(onTower) {
                    boolean valid = true;
                    int towerHeight = (int)this.getVal(tipper.x,tipper.y);
                    //Tests in bounds
                    if(tipper.y + (towerHeight + 1) > this.puzzleRows) {
                        valid = false;
                    }
                    else {
                        //Tests for obstruction
                        for (int test = 0; test <= towerHeight; test++) {
                            if ((int) this.getVal(tipper.x, tipper.y + test) > 0) {
                                valid = false;
                                break;
                            }
                        }
                    }
                    if(valid) {
                        this.puzzleGrid[tipper.x][tipper.y] = '0';
                        this.tipper.y++;
                        for(int set = 0; set <= towerHeight; ++set) {
                            this.puzzleGrid[tipper.x][tipper.y+set] = '1';
                        }
                    }
                }
                else {
                    this.tipper.y++;
                }
                break;
        }
    }

    @Override
    public boolean isSolution() {
        return (tipper.x == end.x && tipper.y == end.y);
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        LinkedList<Configuration> neighbors = new LinkedList<>();

        neighbors.add(new TipOverConfig(this, "up"));
        neighbors.add(new TipOverConfig(this, "down"));
        neighbors.add(new TipOverConfig(this, "left"));
        neighbors.add(new TipOverConfig(this, "right"));


        for (int k = 0; k < neighbors.size(); k++) {
            TipOverConfig current = (TipOverConfig) neighbors.get(k);
            if(current.getVal(tipper.x, tipper.y) == 0)
            {
                neighbors.remove(k);
                k--;
            }
        }
        return neighbors;
    }

    public int getRows() {
        return this.puzzleRows;
    }

    public int getCols() {
        return this.puzzleCols;
    }


    public char getVal(int row, int col) {
        return this.puzzleGrid[row][col];
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for (int row=0; row<getRows(); ++row) {
            for (int col=0; col<getCols(); ++col) {
                if(row == tipper.x && col == tipper.y)
                {
                    result.append("*");
                }
                else if(row == end.x && col == end.y)
                {
                    result.append("!");
                }
                else {
                    result.append(" ");
                }
                result.append(getVal(row, col)).append(" ");
            }
            if (row != getRows()-1) {
                result.append(System.lineSeparator());
            }
        }
        return result.toString();
    }
}
