package com.processsynchronization.simulate;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

public class DiningPhilosophers {
    static int NUMBER_CHOPSTICKS;
    static Semaphore[] chopstick;
    static int[] hold;

    static {
        NUMBER_CHOPSTICKS = 5;
        chopstick = new Semaphore[5];
        for (int i = 0; i < 5; ++i) {
            chopstick[i] = new Semaphore(1);
        }
        hold = new int[5];
        for (int i = 0; i < 5; ++i) {
            hold[i] = -1;
        }
    }

    static Runnable createPhilosopherJob(int id) {
        return () -> {
            while (true) {
                try {
                    chopstick[id].acquire();
                    hold[id] = id;
                    chopstick[(id + 1) % 5].acquire();
                    hold[(id + 1) % 5] = id;

                    Thread.sleep(5); // eating

                    hold[id] = -1;
                    hold[(id + 1) % 5] = -1;
                    chopstick[id].release();
                    chopstick[(id + 1) % 5].release();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    static void main() {
        Thread[] philosopher = new Thread[5];
        for (int i = 0; i < 5; ++i) {
            philosopher[i] = new Thread(createPhilosopherJob(i));
        }
        for (int i = 0; i < 5; ++i) {
            philosopher[i].start();
        }

        long startTime = System.nanoTime();
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                for (int i = 0; i < 5; ++i) {
                    if (hold[i] == -1) {
                        System.out.println("The chopstick " + i + " is not held by any philosopher");
                    } else {
                        System.out.printf("The chopstick %d is held by philosopher %d%n", i, hold[i]);
                    }
                }
                String line = "-".repeat(5);
                System.out.println(line);
                for (int i = 0; i < 5; ++i) {
                    if (hold[i] == i && hold[(i + 1) % 5] == i) {
                        System.out.println("Philosopher " + i + " is eating");
                    } else {
                        System.out.println("Philosopher " + i + " is thinking");
                    }
                }
                System.out.println(line);
                System.out.printf("Time elapsed (s): %.2f\n", 1.0 * (System.nanoTime() - startTime) / 1_000_000_000);
                System.out.println(line);
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 5000);
    }
}
