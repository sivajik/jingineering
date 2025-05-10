package com.jingineering.multithreading.conindepth.optimisticlocking;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CompareAndSwap {
    private static AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 1000; i++) {
            es.execute(CompareAndSwap::increment);
        }

        es.shutdown();
        if (!es.awaitTermination(5, TimeUnit.MINUTES)) {
            es.shutdownNow();
        }
        System.out.println("End of the program");
    }

    private static void increment() {
        int c = count.get();
        int nextVal = c + 1;
        if (!count.compareAndSet(count.get(), nextVal)) {
            System.out.println("Update is not happening");
        }
    }
}
