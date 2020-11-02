/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practice;

import java.util.TreeSet;

/**
 *
 * @author Aurelio
 */
public final class Primes {

    public static TreeSet<Long> cachedPrimes = new TreeSet<>();

    protected Primes() {
        //Exists to defeat initialization
    }

    static {
        //Initialize with a few primes.

        //This makes sure that the primes not of the form 6i+/-1 are there

        cachedPrimes.add(2L);
        cachedPrimes.add(3L);
        cachedPrimes.add(5L);
        cachedPrimes.add(7L);
        cachedPrimes.add(11L);
        cachedPrimes.add(13L);
        cachedPrimes.add(17L);
        cachedPrimes.add(19L);
        cachedPrimes.add(23L);
        cachedPrimes.add(29L);
        cachedPrimes.add(31L);
        cachedPrimes.add(37L);
        cachedPrimes.add(41L);
        cachedPrimes.add(43L);
        cachedPrimes.add(47L);
        cachedPrimes.add(53L);
        cachedPrimes.add(59L);
        cachedPrimes.add(61L);
        cachedPrimes.add(67L);
        cachedPrimes.add(71L);
        cachedPrimes.add(73L);
        cachedPrimes.add(79L);
        cachedPrimes.add(83L);
        cachedPrimes.add(89L);
        cachedPrimes.add(97L);
        cachedPrimes.add(101L);
        cachedPrimes.add(103L);
        cachedPrimes.add(107L);
        cachedPrimes.add(109L);
        cachedPrimes.add(113L);
        cachedPrimes.add(127L);
        cachedPrimes.add(131L);
        cachedPrimes.add(137L);
        cachedPrimes.add(139L);
        cachedPrimes.add(149L);
        cachedPrimes.add(151L);
        cachedPrimes.add(157L);
        cachedPrimes.add(163L);
        cachedPrimes.add(167L);
        cachedPrimes.add(173L);
        cachedPrimes.add(179L);
        cachedPrimes.add(181L);
        cachedPrimes.add(191L);
        cachedPrimes.add(193L);
        cachedPrimes.add(197L);
        cachedPrimes.add(199L);
        cachedPrimes.add(211L);
        cachedPrimes.add(223L);
        cachedPrimes.add(227L);
        cachedPrimes.add(229L);
        cachedPrimes.add(233L);
        cachedPrimes.add(239L);
        cachedPrimes.add(241L);
        cachedPrimes.add(251L);
        cachedPrimes.add(257L);
        cachedPrimes.add(263L);
        cachedPrimes.add(269L);
        cachedPrimes.add(271L);
        cachedPrimes.add(277L);
        cachedPrimes.add(281L);
        cachedPrimes.add(283L);
        cachedPrimes.add(293L);
        cachedPrimes.add(307L);
        cachedPrimes.add(311L);
        cachedPrimes.add(313L);
        cachedPrimes.add(317L);
        cachedPrimes.add(331L);
        cachedPrimes.add(337L);
        cachedPrimes.add(347L);
        cachedPrimes.add(349L);
        cachedPrimes.add(353L);
        cachedPrimes.add(359L);
        cachedPrimes.add(367L);
        cachedPrimes.add(373L);
        cachedPrimes.add(379L);
        cachedPrimes.add(383L);
        cachedPrimes.add(389L);
        cachedPrimes.add(397L);
        cachedPrimes.add(401L);
        cachedPrimes.add(409L);
        cachedPrimes.add(419L);
        cachedPrimes.add(421L);
        cachedPrimes.add(431L);
        cachedPrimes.add(433L);
        cachedPrimes.add(439L);
        cachedPrimes.add(443L);
        cachedPrimes.add(449L);
        cachedPrimes.add(457L);
        cachedPrimes.add(461L);
        cachedPrimes.add(463L);
        cachedPrimes.add(467L);
        cachedPrimes.add(479L);
        cachedPrimes.add(487L);
        cachedPrimes.add(491L);
        cachedPrimes.add(499L);
    }
   

    static private boolean divisibleByCachedPrimes(long num) {
        //Check to see if a value is divisble by any of the values on the 
        //currently cached list.

        for (long testVal : cachedPrimes) {
            if (num % testVal == 0) {
                return true;
            }
            if (testVal * testVal > num) {
                return false;
            }
        }
        return false;
    }

    static public boolean isPrime(long num) {
        //Works for numbers greater than 1
        if (num <= 1) {
            return false;
        }

        //Now see if the cache already has it
        if (cachedPrimes.last() >= num) {
            return cachedPrimes.contains(num);
        } else {

            //Add Primes up to and including the sqrt of the number.
            long fact = cachedPrimes.last() / 6 + 1;
            while (cachedPrimes.last() * cachedPrimes.last() <= num) {
                //
                if (!divisibleByCachedPrimes(fact * 6 - 1)) {
                    cachedPrimes.add(fact * 6 - 1);
                }

                if (!divisibleByCachedPrimes(fact * 6 + 1)) {
                    cachedPrimes.add(fact * 6 + 1);
                }
                fact++;
            }

            //Now that the cached values contain all possible factors
            return !divisibleByCachedPrimes(num);
        }

    }

    static public long nextPrime(long start) {
        //Find the first prime number greater than start

        if (cachedPrimes.last() > start) {
            if (cachedPrimes.contains(start)) {
                return cachedPrimes.ceiling(start + 1);
            } else {
                return cachedPrimes.ceiling(start);
            }
        }

        //For all others find the value of "i" to start testing on
        long i = start / 6;
        while ((i * 6 + 1) <= start) {
            i++;
        }

        while (true) {

            if ((i * 6 - 1 > start) && isPrime((i * 6) - 1)) {
                cachedPrimes.add(i * 6 - 1);
                return i * 6 - 1;

            }
            if (isPrime((i * 6) + 1)) {
                cachedPrimes.add(i * 6 + 1);
                return i * 6 + 1;
            }
            i++;
        }

    }
}
