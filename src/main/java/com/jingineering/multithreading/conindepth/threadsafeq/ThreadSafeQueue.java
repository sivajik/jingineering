package com.jingineering.multithreading.conindepth.threadsafeq;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadSafeQueue {
    public static void main(String[] args) throws InterruptedException {
        ConcurrentQueue q = new ConcurrentQueue();
        ExecutorService es = Executors.newFixedThreadPool(1000);
        Random r = new Random();
        for (int i = 0; i < 1000000; i++) {
            es.execute(() -> {
                q.enqueue(r.nextInt(1, 1000000));
            });
        }
        es.shutdown();
        if (!es.awaitTermination(5, TimeUnit.MINUTES)) {
            System.err.println("Some tasks did not complete within the timeout.");
            es.shutdownNow();
        } else {
            System.out.println("All tasks are completed their job...");
        }
        System.out.println(q.getSize());
        System.out.println("Program exiting....");
    }
}

class ConcurrentQueue {
    // Internally ArrayList uses array and it is NOT threadsafe - which is what we need
    List<Integer> queue = new ArrayList<>();
    Lock myLock = new ReentrantLock();

    public void enqueue(int item) {
        myLock.lock();
        queue.add(item);
        myLock.unlock();
    }

    public int dequeue() {
        myLock.lock();
        if (queue.isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }
        int elem = queue.getFirst();
        queue.removeFirst();
        myLock.unlock();
        return elem;
    }

    public int getSize() {
        myLock.lock();
        int size = queue.size();
        myLock.unlock();
        return size;
    }
}
