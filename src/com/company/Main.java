package com.company;

import com.sun.deploy.util.ArrayUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Main {

    public static void main(String[] args) {

        ArrayList<Integer> eatenPerPeriod = new ArrayList<>();
        ArrayList<Fork> forks = new ArrayList<>();
        ArrayList<Eater> eaters = new ArrayList<>();
        ArrayList<Thread> threads = new ArrayList<>();
        double[] priorities = {1,1,1,1,1,1,2};
        double sumOfPriorities = DoubleStream.of(priorities).sum();

        // нормализуем приоритеты
        for (int i = 0; i < priorities.length; i++)
        {
            priorities[i] = priorities[i] / sumOfPriorities;
        }


        for (int i = 0; i <= 6; i++)
        {
            forks.add(new Fork(i));
            eatenPerPeriod.add(0);
        }
        for (int i = 0; i <= 5; i++)
        {
            eaters.add(new Eater(forks.get(i+1), forks.get(i), i, priorities[i]));
        }
        eaters.add(new Eater(forks.get(6), forks.get(0), 6, priorities[6]));
        for (int i = 0; i <= 6; i++)
        {
            threads.add(new Thread(eaters.get(i)));
            threads.get(i).start();
        }
        try {
            for(int i = 0; i <= 7; i++) {
                TimeUnit.MILLISECONDS.sleep(1000);
                for (Eater eater: eaters)
                {
                    eatenPerPeriod.set(eater.index, eater.eatenPerPeriod);
                }
                double sum = eatenPerPeriod.stream().mapToInt(Integer::intValue).sum();
                for (Eater eater: eaters)
                {
                    eater.setSum(sum);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 6; i >= 0; i--)
        {
            eaters.get(i).continueWorking = false;
            eaters.get(i).print();
        }
    }
}
