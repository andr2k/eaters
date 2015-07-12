package com.company;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class Main {
    static ArrayList<Eater> eaters = new ArrayList<>();
    static ArrayList<Thread> threads = new ArrayList<>();
    static int[] periods = {300, 300, 300, 300, 300, 300, 300}; // период приема пищи, мс
    static int[] durations = {30, 30, 30, 30, 30, 30, 250}; // время в мс, сколько времени длится один прием пищи
    static int[] priorities = {1, 1, 1, 1, 1, 1, 2}; // приоритет приема пищи
    static int total = 50; // время работы программы в сек

    public static void main(String[] args) {

        for (int i = 0; i <= 6; i++) {
            eaters.add(new Eater(periods[i], durations[i], priorities[i], i)); // создаем едоков
        }

        Monitor monitor = new Monitor(eaters);

        for (Eater eater: eaters) {
            eater.monitor = monitor;
        }

        // стартуем потоки едоков
        for (int i = 0; i <= 6; i++) {
            threads.add(new Thread(eaters.get(i)));
            threads.get(i).start();
        }

        long duration = (long)total * 1000000000L;
        long startTime = System.nanoTime();
        long endTime = startTime + duration;
        // работаем до итечения времени
        while(System.nanoTime() < endTime) {
            // кажду секунду выводим статистику
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(Eater eater: eaters) {
                System.out.format("%d: %s", eater.index, eater);
                System.out.println();
            }
        }

        for (int i = 6; i >= 0; i--) {
            eaters.get(i).continueWorking = false;
            threads.get(i).interrupt();
        }

    }
}
