package com.processsynchronization.simulate;

import java.util.concurrent.Semaphore;

public class ReadersWriters {
    static Semaphore rw_mutex = new Semaphore(1);
    static Semaphore mutex = new Semaphore(1);
    static int read_count = 0;

    static Runnable createWriterJob(int id) {
        return () -> {
            while (true) {
                try {
                    rw_mutex.acquire();
                    System.out.printf("The writer %d starts to write something\n", id);
//                    System.out.println("The writer stops writing");
                    rw_mutex.release();
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    static Runnable createReaderJob(int id) {
        return () -> {
            while (true) {
                try {
                    mutex.acquire();
                    ++read_count;
                    if (read_count == 1) {
                        rw_mutex.acquire();
                    }
                    mutex.release();

                    System.out.printf("A reader %d starts to read something\n", id);
                    System.out.println("The number of current readers: " + read_count);

                    mutex.acquire();
                    --read_count;
                    if (read_count == 0) {
                        rw_mutex.release();
                    }
                    mutex.release();
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    static void main() {
        Thread[] writer = new Thread[3];
        Thread[] reader = new Thread[5];

        for (int i = 0; i < 3; ++i) {
            writer[i] = new Thread(createWriterJob(i));
        }
        for (int i = 0; i < 5; ++i) {
            reader[i] = new Thread(createReaderJob(i));
        }
        for (int i = 0; i < 3; ++i) {
            writer[i].start();
        }
        for (int i = 0; i < 5; ++i) {
            reader[i].start();
        }
    }
}
