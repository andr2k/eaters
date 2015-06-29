package com.company;

/**
 * Created by a on 23.06.15.
 */
public class Fork {
    boolean taken = false;
    int index;
    public Fork (int index)
    {
        this.index = index;
    }

    public synchronized void placeBack() throws Exception {
        if (!taken)
            throw new Exception("The fork " + this.index + " is already on place");
        taken = false;
    }

    public synchronized void take() throws Exception {
        if (taken)
            throw new Exception("The fork " + this.index + " is already taken");
        taken = true;
    }
}
