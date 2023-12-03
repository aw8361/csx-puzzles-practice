package com.aw8361.app;

import com.aw8361.puzzle.AnchorConfig;
import com.aw8361.puzzle.AnchorConfig.Cell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnchorModel {
    private List<Observer<AnchorModel>> observers;

    private AnchorConfig original;    // The current board state
    private AnchorConfig[] neighbors; // The possible neighbor configurations

    /**
     * The default puzzle state is a blank puzzle with all empty tiles.
     */
    public AnchorModel() {
        this.observers = new ArrayList<>();

        Cell[] input = new Cell[AnchorConfig.ROWS * AnchorConfig.COLS];
        Arrays.fill(input, Cell.EMPTY);
        this.original = new AnchorConfig(input);
    }

    public AnchorConfig getOriginalConfig() {
        return this.original;
    }

    /**
     * Sets the current configuration to the given configuration.
     * <p>
     * The list of neighbor configurations is not updated automatically when
     * this function is called: use {@link #setNeighborConfigs()} when needed.
     */
    public void setOriginalConfig(AnchorConfig config) {
        this.original = config;
    }

    public AnchorConfig[] getNeighborConfigs() {
        return this.neighbors;
    }

    /**
     * Sets the list of neighbors to the neighbors of the current configuration.
     */
    public void setNeighborConfigs() {
        this.neighbors = this.original.getNeighbors().toArray(new AnchorConfig[0]);
    }

    public void addObserver(Observer<AnchorModel> observer) {
        this.observers.add(observer);
    }

    public void alertObservers() {
        for (Observer<AnchorModel> observer : observers) {
            observer.update(this);
        }
    }
}
