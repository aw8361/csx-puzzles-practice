package com.aw8361.puzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class AnchorConfig implements Configuration {
    public final static int ROWS = 7;
    public final static int COLS = 7;

    private Cell[][] grid;
    private int[] anchor;
    private int tokens;

    /**
     * The default constructor for an Anchor puzzle configuration.
     */
    public AnchorConfig(Cell[] input) {
        this.grid = new Cell[ROWS][COLS];
        this.anchor = new int[] { 0, 0 };
        this.tokens = 0;

        // Convert the one-dimensional input into a matrix
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                Cell cell = input[(i * ROWS) + j];
                if (cell == Cell.TOKEN) {
                    this.tokens++;
                }

                this.grid[i][j] = cell;
            }
        }

        this.grid[0][0] = Cell.ANCHOR;
    }

    /**
     * A copy constructor of an Anchor puzzle, performing the given move.
     */
    public AnchorConfig(AnchorConfig origin, Direction move) {
        this.grid = new Cell[ROWS][COLS];
        this.anchor = Arrays.copyOf(origin.anchor, COLS);
        this.tokens = origin.tokens;

        // Copy array over to new config
        for (int i = 0; i < ROWS; i++) {
            this.grid[i] = Arrays.copyOf(origin.grid[i], COLS);
        }

        // Move the anchor in the given direction until blocked
        int[] next = { this.anchor[0] + move.increment()[0], this.anchor[1] + move.increment()[1] };
        while (next[0] >= 0 && next[0] < ROWS && next[1] >= 0 && next[1] < COLS && this.grid[next[0]][next[1]] != Cell.BLOCK) {
            grid[this.anchor[0]][this.anchor[1]] = Cell.EMPTY;
            this.anchor = next;

            // If anchor passes over token, remove token
            if (this.grid[anchor[0]][anchor[1]] == Cell.TOKEN) {
                this.tokens--;
            }

            grid[this.anchor[0]][this.anchor[1]] = Cell.ANCHOR;
            next = new int[] { this.anchor[0] + move.increment()[0], this.anchor[1] + move.increment()[1] };
        }
    }

    public Cell getPosition(int[] position) {
        return this.grid[position[0]][position[1]];
    }

    public void setPosition(int[] position) {
        Cell previous = this.grid[position[0]][position[1]];
        Cell current = previous.toggle();
        this.grid[position[0]][position[1]] = current;

        // Update token count when token tiles are added or removed
        if (previous == Cell.TOKEN) {
            this.tokens--;
        }
        if (current == Cell.TOKEN) {
            this.tokens++;
        }
    }

    /**
     * A configuration is a solution when all tokens have been collected.
     */
    @Override
    public boolean isSolution() {
        return this.tokens == 0;
    }

    /**
     * Generates all possible neighbors from the current configuration.
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        List<Configuration> neighbors = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            neighbors.add(new AnchorConfig(this, direction));
        }

        return neighbors;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof AnchorConfig) {
            AnchorConfig config = (AnchorConfig) other;
            return Arrays.deepEquals(this.grid, config.grid);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(this.grid);
    }

    /**
     * A tile within the puzzle grid.
     * <p>
     * Each tile has a {@link #toggle()} method indicating the next subsequent
     * value the tile should display when editing the tile.
     */
    public enum Cell {
        EMPTY { Cell toggle() { return Cell.BLOCK; } },
        BLOCK { Cell toggle() { return Cell.TOKEN; } },
        TOKEN { Cell toggle() { return Cell.EMPTY; } },
        ANCHOR { Cell toggle() { return null; } };
        abstract Cell toggle();
    }

    /**
     * The anchor can only move in cardinal directions.
     * <p>
     * Each direction has an {@link #increment()} method representing the
     * corresponding X and Y movement required to move in that direction within
     * a two dimensional matrix.
     */
    public enum Direction {
        NORTH { int[] increment() { return new int[] { -1, 0 }; } },
        EAST { int[] increment() { return new int[] { 0, 1 }; } },
        SOUTH { int[] increment() { return new int[] { 1, 0 }; } },
        WEST { int[] increment() { return new int[] { 0, -1 }; } };
        abstract int[] increment();
    }
}
