package com.jingineering.multithreading.abhayani;

import java.time.Duration;
import java.time.LocalTime;
// 5761456
public class OneHundresMillionPrimesV1BruteForce {
    public static void main(String[] args) {
        LocalTime start = LocalTime.now();
        int limit = 100_000_000;
        System.out.println("v1) for " + limit + " numbers we have: " + getTotalPrimesUntil(limit)
                + " primes & took " + Duration.between(start, LocalTime.now()));
    }

    private static int getTotalPrimesUntil(int limit) {
        int totalPrimtCount = 0;
        for (int n = 3; n < limit; n++) {
            if (isPrime(n)) {
                totalPrimtCount++;
            }
        }
        return totalPrimtCount + 1;
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
