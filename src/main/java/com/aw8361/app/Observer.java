package com.aw8361.app;

/**
 * A simplified version of the observer pattern used to notify objects about
 * changes in another object's state.
 * <p>
 * Loosely based off of RIT's original Observer interface.
 */
public interface Observer<Subject> {
    void update(Subject subject);
}
