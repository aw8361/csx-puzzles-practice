package com.aw8361.puzzle;

import java.util.Collection;

/**
 * A configuration is used in an external breadth-first solver to find a
 * solution given an initial configuration. The solver module is not included
 * in this project.
 * <p>
 * Loosely based off of RIT's original Configuration interface.
 */
public interface Configuration {
    boolean isSolution();
    Collection<Configuration> getNeighbors();
}
