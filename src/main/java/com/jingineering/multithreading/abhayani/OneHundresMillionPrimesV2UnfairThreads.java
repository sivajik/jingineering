package com.jingineering.multithreading.abhayani;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/*
v1) for 100000 numbers we have: 9592 primes & took PT0.005003S
v1) for 100000000 numbers we have: 5761455 primes & took PT36.209807S

v2) for 100000 numbers we have: 9592 primes & took PT0.022969S
v2) for 100000000 numbers we have: 5761455 primes & took PT6.738088S
 */
public class OneHundresMillionPrimesV2UnfairThreads {
    public static void main(String[] args) throws InterruptedException {
        LocalTime start = LocalTime.now();
        int limit = 100_000_000;
        System.out.println("v2) for " + limit + " numbers we have: " + getTotalPrimesUntil(limit)
                + " primes & took " + Duration.between(start, LocalTime.now()));
    }

    private static int getTotalPrimesUntil(int limit) throws InterruptedException {
        AtomicInteger totalPrimeCount = new AtomicInteger(0);

        int totalThreads = 10;
        int batchSize = limit / totalThreads;

        ExecutorService es = Executors.newFixedThreadPool(totalThreads);
        CompletableFuture<String>[] list = new CompletableFuture[totalThreads];


        for (int i = 0; i < totalThreads; i++) {
            int batchStart = i * batchSize + 1;
            int batchEnd = batchStart + batchSize - 1;

            list[i] = CompletableFuture.supplyAsync(() -> {
                LocalTime start = LocalTime.now();
                for (int currNum = batchStart; currNum <= batchEnd; currNum++) {
                    if (isPrime(currNum)) {
                        totalPrimeCount.incrementAndGet();
                    }
                }
                System.out.println("Thread: [" + Thread.currentThread().getName() + "] for range: ["
                        + batchStart + " -> " + batchEnd + "] found: " + totalPrimeCount.get()
                        + " took: " + Duration.between(start, LocalTime.now()));
                return "";
            }, es);
        }

        try {
            CompletableFuture.allOf(list).join();/*.thenAccept(i -> {
                for (var x : list) {
                    var result = x.join();
                    System.out.println(result);
                }
            });*/
            System.out.println("All Tasks/threads are completed their execution...");
        } catch (Exception e) {
            e.printStackTrace();
        }

        es.shutdown();
        es.awaitTermination(100, TimeUnit.SECONDS);
        return totalPrimeCount.get();
    }

    private static boolean isPrime(int n) {
        if (n == 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}