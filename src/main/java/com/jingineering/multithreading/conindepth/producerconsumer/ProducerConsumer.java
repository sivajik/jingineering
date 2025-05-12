package com.jingineering.multithreading.conindepth.producerconsumer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumer {
    public static void main(String[] args) {
        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(10);

        Producer p = new Producer(blockingQueue);
        Consumer c = new Consumer(blockingQueue);

        new Thread(p).start();
        new Thread(c).start();
    }
}

class Producer implements Runnable {
    private final BlockingQueue<Integer> blockingQueue;

    Producer(BlockingQueue<Integer> queue) {
        this.blockingQueue = queue;
    }

    public void run() {
        try {
            while (true) {
                int item = (int) (Math.random() * 100);
                blockingQueue.put(item);
                System.out.println("Item Produced: " + item);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            //Thread.currentThread().interrupt();
        }
    }
}

class Consumer implements Runnable {
    private final BlockingQueue<Integer> blockingQueue;

    Consumer(BlockingQueue<Integer> queue) {
        this.blockingQueue = queue;
    }

    public void run() {
        try {
            while (true) {
                Integer item = blockingQueue.take();
                System.out.println("Item Consumer: " + item);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            //Thread.currentThread().interrupt();
        }

    }
}
