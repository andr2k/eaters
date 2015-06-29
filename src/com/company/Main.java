package com.company;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class Main {
    static ArrayList<Fork> forks = new ArrayList<>();
    static ArrayList<Eater> eaters = new ArrayList<>();
    static ArrayList<Thread> threads = new ArrayList<>();
    static int[] priorities = {10, 10, 10, 10, 10, 10, 100};

//    public static int LCM(int[] arr) {
//        int res = 1;
//        for (int i = 0; i < arr.length; i++)
//            res = LCM(res, arr[i]);
//        return res;
//    }
//
//    public static int LCM(int x, int y) {
//        return x * y / GCD(x, y);
//    }

    public static int GCD(int[] array) {
        int gcd = array[0];
        for (int i = 1; i < array.length; i++) {
            gcd = GCD(gcd < array[i] ? gcd : array[i], gcd < array[i] ? array[i] : gcd);
        }
        return gcd;
    }

    public static int GCD(int m, int n) {
        //НОД(0, n) = n; НОД(m, 0) = m; НОД(m, m) = m;
        if (m == 0)
            return n;
        if (n == 0)
            return m;
        if (m == n)
            return n;

        //НОД(1, n) = 1; НОД(m, 1) = 1;
        if (m == 1 || n == 1)
            return 1;

        //Если m, n чётные, то НОД(m, n) = 2*НОД(m/2, n/2);
        if (m % 2 == 0 && n % 2 == 0)
            return 2 * GCD(m / 2, n / 2);

        //Если m чётное, n нечётное, то НОД(m, n) = НОД(m/2, n);
        if (m % 2 == 0 && n % 2 == 1)
            return GCD(m / 2, n);

        //Если n чётное, m нечётное, то НОД(m, n) = НОД(m, n/2);
        if (m % 2 == 1 && n % 2 == 0)
            return GCD(m, n / 2);

        //Если m, n нечётные и n > m, то НОД(m, n) = НОД((n-m)/2, m);
        if (m % 2 == 1 && n % 2 == 1 && m < n)
            return GCD((n - m) / 2, m);

        //Если m, n нечётные и n < m, то НОД(m, n) = НОД((m-n)/2, n);
        if (m % 2 == 1 && n % 2 == 1 && m > n)
            return GCD((m - n) / 2, n);

        System.out.println("GCD: somethind went wrong");

        return -1;
    }

    public static boolean isAllDone()
    {
        boolean done = true;
        for (Eater eater : eaters) {
            done &= eater.isDone();
        }
        return done;
    }

    public static void main(String[] args) {
        // вычисляем наибольший общий делитель НОД для приоритетов
        int gcd = GCD(priorities);
        // кратно значению НОД уменьшаем приоритеты (нормализация)
        for (int i = 0; i < priorities.length; i++) {
            priorities[i] = priorities[i] / gcd;
        }

        // инициализация значений
        for (int i = 0; i <= 6; i++) {
            forks.add(new Fork(i)); // создаем вилки
        }
        for (int i = 0; i <= 5; i++) {
            eaters.add(new Eater(forks.get(i + 1), forks.get(i), i, priorities[i])); // создаем едоков
        }
        eaters.add(new Eater(forks.get(6), forks.get(0), 6, priorities[6])); // последний едок

        // стартуем потоки едоков
        for (int i = 0; i <= 6; i++) {
            threads.add(new Thread(eaters.get(i)));
            threads.get(i).start();
        }

        // работаем 5 секунд
        long duration = 5000000000L;
        long startTime = System.nanoTime();
        long endTime = startTime + duration;

        while(System.nanoTime() < endTime) {
            if (isAllDone())
                for (Eater eater : eaters)
                    eater.addCycles(priorities[eater.index]);
        }

        // ждем завершения всех процессов
        while (!isAllDone()) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int i = 6; i >= 0; i--) {
            eaters.get(i).continueWorking = false;
            eaters.get(i).print();
        }

    }
}
