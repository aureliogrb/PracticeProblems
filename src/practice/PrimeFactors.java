/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Aurelio
 */
public class PrimeFactors {

    public HashMap<Long, Long> factors;

    public PrimeFactors(long num) {
        factors = calculatePrimeFactors(num);
    }


    public static HashMap<Long, Long> calculatePrimeFactors(long num) {

        //The hashmap will store the Factors as keys and the indexes as values.
        //Eg. 8 will be stored as Key = 2, Value = 3 or 2^3.

        HashMap<Long, Long> primeFactors = new HashMap<>();

        for (long i = 2L; i * i <= num; i++) {
            while (num % i == 0) {
                num = num / i;
                if (!primeFactors.containsKey(i)) {
                    primeFactors.put(i, 1L);

                } else {
                    primeFactors.replace(i, primeFactors.get(i) + 1);
                }
            }
        }
        if (num > 1) {
            primeFactors.put(num, 1L);
        }
        return primeFactors;
    }

    public static long valuefromPrimeFactors(HashMap<Long, Long> factors) {

        return factors.entrySet().stream().mapToLong(kp -> (long) Math.pow(kp.getKey(), kp.getValue())).reduce((l1, l2) -> l1 * l2).getAsLong();

//        long calcvalue = 1;
//        for (Map.Entry<Long, Long> entry : factors.entrySet()) {
//            calcvalue = (long) (calcvalue * Math.pow(entry.getKey(), entry.getValue()));
//        }
//        return calcvalue;
    }

    public static int divisorCount(int num) {
        //Returns the number of divisors other than number itself;
        HashMap<Long, Long> factors = calculatePrimeFactors(num);

        if (factors.isEmpty()) {
            return 1;
        } else {
            //return (int) factors.entrySet().stream().mapToLong(kp -> ((long) kp.getValue() + 1)).reduce((i1, i2) -> i1 * i2).getAsLong();
            int prod = 1;
            for (Map.Entry<Long, Long> entry : factors.entrySet()) {
                //Get the values
                prod *= (entry.getValue() + 1);
            }
            return prod;
        }
    }

    public void multiplyBy(long num) {
        //Multiply the current values by another long increasing the factors
        HashMap<Long, Long> moreFactors = calculatePrimeFactors(num);
        multiplyBy(moreFactors);
    }

    public void multiplyBy(HashMap<Long, Long> moreFactors) {
        moreFactors.entrySet().stream().forEach((entry) -> {
            if (factors.containsKey(entry.getKey())) {
                long newVal = entry.getValue() + factors.get(entry.getKey());
                if (newVal != 0) {
                    factors.replace(entry.getKey(), newVal);
                } else {
                    factors.remove(entry.getKey());
                }
            } else {
                factors.put(entry.getKey(), entry.getValue());
            }
        });

    }

    public static void invertFactors(HashMap<Long, Long> toInverse) {
        // the same as doing 1/value;
        toInverse.entrySet().stream().forEach((entry) -> {
            entry.setValue(entry.getValue() * -1);
        });
    }

    public void divideBy(long num) {
        HashMap<Long, Long> moreFactors = calculatePrimeFactors(num);
        invertFactors(moreFactors);
        multiplyBy(moreFactors);
    }

    public long getValue() {
        return valuefromPrimeFactors(factors);
    }

}
