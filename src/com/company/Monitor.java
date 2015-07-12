package com.company;

import java.util.ArrayList;

/**
 * Created by a on 12.07.15.
 */
public class Monitor {

    ArrayList<Eater> eaters;

    public Monitor(ArrayList<Eater> eaters) {
        this.eaters = eaters;
    }


    public synchronized boolean canIEat(Eater eater) {
        int i = eater.index;
        int leftI = (i == eaters.size() - 1) ? 0 : i + 1;
        int rightI = (i == 0) ? eaters.size() - 1 : i - 1;
        Eater left = eaters.get(leftI);
        Eater right = eaters.get(rightI);

        left.stopEating(eater);
        right.stopEating(eater);

        return !(left.isEating() || right.isEating());
    }
}
