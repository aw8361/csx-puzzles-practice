package com.aw8361.app;

import com.aw8361.puzzle.AnchorConfig;
import com.aw8361.puzzle.AnchorConfig.Cell;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class DisplayController implements Observer<AnchorModel> {
    private AnchorModel model;      // The model with the current puzzle state
    private AnchorConfig[] puzzles; // The current set of puzzles on display
    private int index;              // The index of the puzzle on display
    private boolean editable;       // Whether the board is currently editable

    @FXML private Label text;
    @FXML private GridPane grid;
    @FXML private Button prev;
    @FXML private Button next;
    @FXML private Button generate;
    @FXML private Button select;

    public DisplayController() {
        this.model = new AnchorModel();
        this.model.addObserver(this);
        this.puzzles = new AnchorConfig[] { model.getOriginalConfig() };
        this.index = 0;
        this.editable = true;
    }

    @FXML
    public void initialize() {
        initializeGrid(this.puzzles[0], editable);

        // Generate the neighboring configurations of the current config
        generate.setOnAction((event) -> {
            this.model.setNeighborConfigs();

            // TODO: Filter out the identical neighbor configurations
            // The puzzles array must now have the new list of neighbors

            this.index = 0;
            this.editable = false;
            initializeGrid(this.puzzles[0], editable);

            this.prev.setDisable(false);
            this.next.setDisable(false);
            this.generate.setDisable(true);
            this.select.setDisable(false);

            this.text.setText("The valid neighbors are:");
        });

        // Rotate through existing list of configurations
        prev.setOnAction((event) -> {
            this.index = (this.index - 1 < 0) ? (this.puzzles.length - 1) : (this.index - 1);
            initializeGrid(this.puzzles[index], this.editable);
        });
        next.setOnAction((event) -> {
            this.index = (this.index + 1 == this.puzzles.length) ? 0 : (this.index + 1);
            initializeGrid(this.puzzles[index], this.editable);
        });

        // Select configuration to proceed
        select.setOnAction((event) -> {
            this.model.setOriginalConfig(this.puzzles[index]);
            this.puzzles = new AnchorConfig[] { this.model.getOriginalConfig() };
            this.index = 0;

            if (this.model.getOriginalConfig().isSolution()) {
                this.text.setText("The solution has been found.");
                this.editable = true;
            } else {
                this.text.setText("Puzzle is not yet complete.");
            }

            initializeGrid(this.puzzles[0], this.editable);

            this.prev.setDisable(true);
            this.next.setDisable(true);
            this.generate.setDisable(false);
            this.select.setDisable(true);
        });
    }

    @Override
    public void update(AnchorModel subject) {
        // When model is updated, update tile grid with new information
        initializeGrid(subject.getOriginalConfig(), this.editable);
    }

    /**
     * Updates the central grid with new puzzle configuration.
     */
    private void initializeGrid(AnchorConfig puzzle, boolean editable) {
        for (int i = 0; i < AnchorConfig.ROWS; i++) {
            for (int j = 0; j < AnchorConfig.COLS; j++) {
                // TODO: Create a button for each space within the grid
                // Button must have unique colors for each possible cell value
                // It must also save its position internally

                if (editable) {
                    if (puzzle.getPosition(new int[] { i, j }) != Cell.ANCHOR) {
                        // TODO: Implement required grid button action
                        // If the button is not an anchor, toggle its value
                    }
                }

                // TODO: Add the finalized button to the grid
            }
        }

        this.grid.setAlignment(Pos.CENTER);
    }

    /**
     * A button which saves its current position within the grid.
     */
    private static class AnchorButton extends Button {
        private int[] position;

        public AnchorButton(int row, int col) {
            super();
            this.position = new int[] { row, col };
            setMinSize(84, 84);
        }

        public int[] getPosition() {
            return Arrays.copyOf(position, 2);
        }
    }
}
