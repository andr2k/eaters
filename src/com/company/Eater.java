package com.company;

import sun.rmi.runtime.RuntimeUtil;

import java.util.concurrent.TimeUnit;

/**
 * Created by a on 23.06.15.
 */
public class Eater implements Runnable{

    Fork first, second;
    int index;
    int eatenPerPeriod = 0;
    long eaten = 0;
    int ms = 100;
    boolean continueWorking = true;
    int k = 1;
    double priority;
    public Eater(Fork left, Fork right, int index, double priority)
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
        this.priority = priority;
    }

    public void setSum(double sum)
    {
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

    public void eat() throws Exception {
        take(first);
        take(second);

        long startTime = System.nanoTime();
        long finishTime = (long) (startTime + ms * priority * 1000);
        long startEaten = eaten;
/*        while (System.nanoTime() < finishTime)
        {
            eaten++;
        }*/
        eatenPerPeriod = (int)(eaten - startEaten);
        eaten++;
        placeBack(second);
        placeBack(first);
        //TimeUnit.NANOSECONDS.sleep((long) (1000000000 - (System.nanoTime() - startTime)));
        //System.out.println(this.index + " has eaten " + eaten + " times");
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
