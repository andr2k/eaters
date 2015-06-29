package com.company;

import sun.rmi.runtime.RuntimeUtil;

import java.util.concurrent.TimeUnit;

/**
 * Created by a on 23.06.15.
 */
public class Eater implements Runnable{

    Fork first, second;
    int index;
    long eaten = 0;
    boolean continueWorking = true;
    int cycles;
    public Eater(Fork left, Fork right, int index, int cycles)
    {
        if (left.index > right.index) {
            this.first = left;
            this.second = right;
        }
        else
        {
            this.second = left;
            this.first = right;
        }
        this.index = index;
        this.cycles = cycles;
    }

    public void take(Fork fork) throws Exception {
        while (!fork.taken) {
            synchronized (fork) {
                if (!fork.taken)
                    fork.take();
            }
        }
    }

    public void placeBack(Fork fork) throws Exception {
        while (fork.taken) {
            synchronized (fork) {
                if (fork.taken)
                    fork.placeBack();
            }
        }
    }

    public synchronized void addCycles(int n)
    {
        cycles += n;
    }

    public synchronized void decCycles()
    {
        cycles --;
    }

    public void eat() throws Exception {
        if (!isDone())
        {
            take(first);
            take(second);
            TimeUnit.MILLISECONDS.sleep(17);
            placeBack(second);
            placeBack(first);
            eaten++;
            decCycles();
        }

    }

    public synchronized boolean isDone()
    {
        return cycles == 0;
    }

    public void print()
    {
        System.out.println(this.index + " has eaten " + eaten + " times");
    }

    @Override
    public void run() {
        try {
            while (continueWorking) {
                eat();
            }
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
