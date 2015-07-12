package com.company;

import sun.rmi.runtime.RuntimeUtil;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Created by a on 23.06.15.
 */
public class Eater implements Runnable {

    int index;
    long startedEating = 0;
    long period, duration;
    long ateTimeTotal = 0;
    int ateNumberTotal = 0;
    long timedEating = 0;
    long timedWorking = 0;
    long workedTotal = 0;
    boolean continueWorking = true;
    long dayStarted = 0;
    int priority;

    Monitor monitor;

    public Eater(int period, int duration, int priority, int index) {
        this.period = period * 1000000L;
        this.duration = duration * 1000000L;
        this.index = index;
        this.priority = priority;
    }

    synchronized boolean isHungry() {
        long timePassed = System.nanoTime() - dayStarted;
        long periodsNumber = timePassed / period;
        return ateTimeTotal < periodsNumber * duration; // если он поел меньше ожидаемого, значит голоден
    }

    synchronized boolean isEating() {
        return timedEating > 0 && startedEating > 0;
    }

    synchronized boolean isWorking() {
        return timedWorking > 0;
    }

    boolean startEating() {
        if (monitor.canIEat(this)) {
            startedEating = timedEating = System.nanoTime();
            ateNumberTotal++;
            return true;
        }
        return false;
    }

    boolean hasEatenEnough() {
        return ((System.nanoTime() - startedEating) > duration);
    }

    synchronized void stopEating(Eater eater) {
        if (isEating() && (this.priority < eater.priority)) {
            stopEating();
        }
    }

    synchronized void stopEating() {
        timedEating = 0;
        startedEating = 0;
    }

    void calcStats() {
        long now = System.nanoTime();
        if (isEating()) {
            ateTimeTotal += now - timedEating;
            timedEating = now;
        }
        if (isWorking()){
            workedTotal += now - timedWorking;
            timedWorking = now;
        }
    }

    void startWorking() {
        timedWorking = System.nanoTime();
    }

    void stopWorking() {
        timedWorking = 0;
    }

    @Override
    public void run() {

        dayStarted = System.nanoTime();
        startWorking();

        try {
            while (continueWorking) {
                calcStats();
                //synchronized (this) {
                if (isHungry() && !isEating()) {
                    if (startEating()) {
                        stopWorking();
                    }
                }

                if (isEating() && hasEatenEnough()) {
                    stopEating();
                    startWorking();
                }
                //}

            }
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        String s = String.format("%d\t%d\t%d", ateNumberTotal, ateTimeTotal / 1000000, workedTotal / 1000000);
        return s;
    }
}
