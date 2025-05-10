package com.jingineering.multithreading.conindepth.One100MillPrimes;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/*
v1) for 100000 numbers we have: 9592 primes & took PT0.00518S
v1) for 100000000 numbers we have: 5761455 primes & took PT35.823497S

v2) for 100000 numbers we have: 9592 primes & took PT0.022969S
v2) for 100000000 numbers we have: 5761455 primes & took PT6.738088S

v3) for 100000 numbers we have: 9592 primes & took PT0.02879S
v3) for 100000000 numbers we have: 5761455 primes & took PT5.550504S
 */
public class OneHundresMillionPrimesV3FairThreads {
    public static void main(String[] args) throws InterruptedException {
        OneHundresMillionPrimesV3FairThreads obj = new OneHundresMillionPrimesV3FairThreads();
        LocalTime start = LocalTime.now();
        System.out.println("v3) for " + obj.limit + " numbers we have: " + obj.getTotalPrimesUntil()
                + " primes & took " + Duration.between(start, LocalTime.now()));
    }

    private AtomicInteger currNum = new AtomicInteger(1);
    private AtomicInteger totalPrimeCount = new AtomicInteger(0);
    private final int limit = 100_000_000;

    private int getTotalPrimesUntil() throws InterruptedException {
        int totalThreads = 10;
        ExecutorService es = Executors.newFixedThreadPool(totalThreads);
        CompletableFuture<String>[] list = new CompletableFuture[totalThreads];

        for (int i = 0; i < totalThreads; i++) {
            list[i] = CompletableFuture.supplyAsync(() -> {
                doWork();
                return "";
            });
        }

        try {
            CompletableFuture.allOf(list).join();
        } catch (Exception e) {
            e.printStackTrace();
        }

        es.shutdown();
        es.awaitTermination(10000, TimeUnit.SECONDS);
        return totalPrimeCount.get();
    }

    private void doWork() {
        LocalTime start = LocalTime.now();
        while (true) {
            final int c = currNum.incrementAndGet();
            if (c > limit) {
                break;
            }
            if (isPrime(c)) {
                totalPrimeCount.incrementAndGet();
            }
        }
        System.out.println("Thread: " + Thread.currentThread().getName() + " completed in " + Duration.between(start, LocalTime.now()) + " total primes: " + totalPrimeCount.get());
    }

    private boolean isPrime(int n) {
        if (n == 1) {
            return false;
        }
        if (n == 2) {
            return true;
        }
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}