package practice;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author Aurelio
 */
public final class Utilities {

    public static String sortString(String s) {
        char[] chars = s.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    public static boolean isPermutationString(int a, int b) {
        //Checks to see if two numbers have the same digits but in different order.
        return sortString(Integer.toString(a)).equals(sortString(Integer.toString(b)));
    }

    public static boolean isPermutationString(String a, int b) {
        //Checks to see if two numbers have the same digits but in different order.
        return sortString(a).equals(sortString(Integer.toString(b)));
    }

    public static boolean isPermutationString(String a, String b) {
        //Checks to see if two strings have the same characters even if on different order.
        return sortString(a).equals(sortString(b));
    }

    public static long factorial(int n) {
        if (n < 0) {
            return 0;
        } else {
            switch (n) {
                case 0:
                    return 1;
                case 1:
                case 2:
                    return n;
                case 3:
                    return 6;
                case 4:
                    return 24;
                case 5:
                    return 120;
                case 6:
                    return 720;
                case 7:
                    return 5040;
                case 8:
                    return 40320;
                case 9:
                    return 362880;
                case 10:
                    return 3628800;
                default:
                    long prod = factorial(10);
                    for (int i = 11; i <= n; i++) {
                        prod *= i;
                    }
                    return prod;

            }
        }

    }

    public static boolean isPalindrome(String value) {
        

        for (int i = 0; i < value.length() / 2; i++) {
            if (value.charAt(i) != value.charAt(value.length() - 1 - i)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isPalindrome(int number) {
        return isPalindrome(Integer.toString(number));
    }

    public static int sumofDigits(BigInteger val) {
        int sum = 0;
        while (val.compareTo(BigInteger.TEN) >= 0) {
            sum += val.mod(BigInteger.TEN).intValue();
            val = val.divide(BigInteger.TEN);
        }
        sum += val.intValue();

        return sum;
    }

    public static int sumofDigits(long num) {
        int sum = 0;

        while (num >= 10) {
            sum += num % 10;
            num /= 10; //integer division
        }

        sum += num;
        return sum;
    }
    
    public static ArrayList<String> permutations(String str) {
        //Initialize the array, we already know the size 
        ArrayList<String> answers = new ArrayList<>((int) factorial(str.length()));

        //compute all values recursively
        permutations("", str, answers);
        return answers;
    }

    private static void permutations(String preStr, String str, ArrayList<String> answers) {
        if (str.length() == 0) {
            answers.add(preStr);
        } else {
            for (int i = 0; i < str.length(); i++) {
                permutations(preStr + str.charAt(i), str.substring(0, i) + str.substring(i + 1), answers);
            }
        }
    }

    public static boolean isInteger(double n) {
        return n == (int) n;
    }
    
    public static boolean isLong(double n) {
        return n == (long) n;
    }

     public static HashMap<Long, Long> lowestCommonMultiple(HashMap<Long, Long> a, HashMap<Long, Long> b) {

        //Start with the value of a
        @SuppressWarnings("unchecked") //We can safely cast as "a" has been chekecd when it was passed as a paremeter
        HashMap<Long, Long> lcm = (HashMap<Long, Long>) a.clone();

        //For each of the factors of b
        b.entrySet().stream().forEach((entry) -> {
            if (lcm.containsKey(entry.getKey())) {
                //Use the largest power between the existing and the new one.
                lcm.replace(entry.getKey(), Math.max(lcm.get(entry.getKey()), entry.getValue()));
            } else {
                lcm.put(entry.getKey(), entry.getValue());
            }
        });
        return lcm;
    }
     
    public static long greatestCommonDivisor(long a, long b) {
        //using the euclidian algorithm: 
        //https://en.wikipedia.org/wiki/Euclidean_algorithm#Procedure
        
        if (a < 1 || b < 1) {
            throw new IllegalArgumentException("The Greatest Common Divisor expects non negative numbers greater than 0");
        }
        if (a < b) {
            long aux = a;
            a = b;
            b = aux;
        }
        while (b != 0) {
            a = a % b;
            long aux = a;
            a = b;
            b = aux;
        }
        return a;
    }
    
    public static long greatestCommonDivisor(long a, long b, long c) {
        return greatestCommonDivisor(a, greatestCommonDivisor(b,c));
    }
    
    public static double triangleArea(long a, long b, long c) {
        //use Heron's formula for the area of a triangle
        double p = (a+b+c)/2.0;
        return Math.sqrt(p*(p-a)*(p-b)*(p-c));
  
    }
    
    public static long truncatedPow(int base, int power) {
        //Computes the power of the base but only caring for the first
        //10 digits.
        long truncVal = base;
        for (int i = 2; i <= power; i++) {
            truncVal = (truncVal * base) % 100_000_000_000L;
        }
        return truncVal;
    }
}

