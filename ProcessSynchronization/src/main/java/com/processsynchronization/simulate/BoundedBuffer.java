package com.processsynchronization.simulate;

import java.util.concurrent.Semaphore;

public class BoundedBuffer {
    final static int BUFFER_SIZE = 10;

    final static Semaphore mutex = new Semaphore(1);
    final static Semaphore full = new Semaphore(0);
    final static Semaphore empty = new Semaphore(BUFFER_SIZE);
    final static int[] buffer = new int[BUFFER_SIZE];
    static int counter = 0;

    static void main() {
        Runnable customerJob = () -> {
            while (true) {
                try {
                    full.acquire();
                    mutex.acquire();
                    --counter;
                    System.out.println("User use an item from the buffer!");
                    System.out.println("Counter: " + counter);
                    mutex.release();
                    empty.release();

//                    Thread.sleep(2000); // sleep 2s to avoid much thing printed on the screen
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Runnable producerJob = () -> {
            while (true) {
                try {
                    empty.acquire();
                    mutex.acquire();
                    ++counter;
                    System.out.println("Producer add an item to the buffer!");
                    System.out.println("Counter: " + counter);
                    mutex.release();
                    full.release();

//                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Thread customer= new Thread(customerJob);
        Thread producer = new Thread(producerJob);

        customer.start();
        producer.start();
    }
}