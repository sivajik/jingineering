package com.jingineering.multithreading.conindepth.threadpools;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPoolImpl {
    public static void main(String[] args) throws InterruptedException {
        int totalCores = Runtime.getRuntime().availableProcessors();
        System.out.println("this machine got " + totalCores + " cores only");
        int workerThreadsCount = 3;
        MyThreadPool myThreadPool = new MyThreadPool(totalCores, workerThreadsCount);// start with 3 workers;
        for (int i = 0; i < 30; i++) {
            final int taskNumber = i;
            myThreadPool.addJob(() -> {
                try {
                    Thread.sleep(1000);
                    System.out.println("Completed job : " + taskNumber + " by thread : " + Thread.currentThread().getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}

class MyThreadPool {
    /*
        Max of 10 tasks can be handles (as my mac has 10 cores only).
        Only 'put' and 'take' are blocked operations.
        'add' 'remove' and 'offer', 'poll' are not blocked so don't use it.
     */
    private final BlockingQueue<Runnable> queue;

    MyThreadPool(int totalCores, int workerThreadsCount) {
        this.queue = new LinkedBlockingQueue<>(totalCores);

        for (int i = 0; i < workerThreadsCount; i++) {
            Thread t = new Thread(() -> {
                while (true) {
                    Runnable task = null;
                    try {
                        task = queue.take(); // 'take' waits until element is available
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    task.run();
                }
            });
            t.start();
        }
    }

    public void addJob(Runnable task) throws InterruptedException {
        System.out.println("Q size: " + queue.size());
        queue.put(task);
    }
}