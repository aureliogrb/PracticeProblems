/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package euler;

import practice.Primes;
import practice.PrimeFactors;
import practice.Fraction;
import practice.Utilities;
import static practice.Utilities.*;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.BiPredicate;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author Aurelio
 * Source of problems: https://projecteuler.net/ 
 */
public class Problems051to100 {

    public long problem51() {

        //By replacing the 1st digit of the 2-digit number *3, it turns out that 
        //six of the nine possible values: 13, 23, 43, 53, 73, and 83, are all prime.
        //
        //By replacing the 3rd and 4th digits of 56**3 with the same digit, 
        //this 5-digit number is the first example having seven primes among 
        //the ten generated numbers, yielding the family: 
        //56003, 56113, 56333, 56443, 56663, 56773, and 56993. 
        //Consequently 56003, being the first member of this family,
        //is the smallest prime with this property.
        //
        //Find the smallest prime which, by replacing part of the number
        //(not necessarily adjacent digits) with the same digit, 
        //is part of an eight prime value family.
        //For each prime find it's patterns.
        //Then for each new pattern check the size of the "exploded" list that
        //are prime.
        long primeValue = 2;

        HashMap<String, Integer> visitedPatterns = new HashMap<>();

        ArrayList<String> patterns;

        while (true) {

            patterns = findPatterns(primeValue);

            for (String p : patterns) {
                if (!visitedPatterns.containsKey(p)) {
                    ArrayList<Long> candidates;
                    candidates = explodePatterns(p);

                    int famSize = (int) candidates.stream().filter(Primes::isPrime).count();

                    if (famSize == 8) {
                        //return candidates.stream().filter(Primes::isPrime).min(Comparator.comparing(item -> item)).get();
                        //return candidates.stream().filter(Primes::isPrime).mapToLong(i->i).min().getAsLong();
                        System.out.println(p);
                        return primeValue;
                    } else {
                        visitedPatterns.put(p, famSize);
                    }
                }
            }
            primeValue = Primes.nextPrime(primeValue);
        }
    }

    private ArrayList<String> findPatterns(long n) {
        //Calculate all the Patters for a given value where each digit
        //in turn is replaced by a "*".

        ArrayList<String> patterns = new ArrayList<>();
        String number = Long.toString(n);
        int[] digits = number.chars().distinct().sorted().toArray();
        for (int digit : digits) {
            int instanceCount = countInstances(String.valueOf((char) digit), number);
            if (instanceCount == 1) {
                //Just one pattern replacing that one digit with "*"
                patterns.add(number.replaceFirst(String.valueOf((char) digit), "*"));
            } else {
                //Since there is more than one instance we find all the values.
                //e.g. if there are 3 instances of a digit we have 2^3-1 = 7 options.
                // 12*, 1*3, *23, 1**, *2*, **3, *** 
                //Will use a binary flag to determine which instances to update.
                for (byte i = 1; i < Math.pow(2, instanceCount); i++) {
                    patterns.add(replaceSomeInstances(number, (char) digit, '*', i));
                }
            }

            //System.out.println(String.format("%s has %s in it %d times",number,(char)digit,instanceCount));    
        }
        return patterns;
    }

    public static int countInstances(String find, String inString) {
        //Count the number of instances of a char in a given string.
        return inString.length() - inString.replace(find, "").length();
    }

    private static String replaceSomeInstances(String inString, char find, char replaceWith, byte replaceFlag) {
        //Replace some instances of "find" with "replaceWith" in "inString"
        //Replace Flag determines which instances to replace.
        //e.g. if replace flag is (in binary) 10  we replace the first but not the second instance
        //if replace flag is 101 we replace the first and third instance but not the second one.

        StringBuilder newString = new StringBuilder(inString);

        int foundCount = 0;

        if (replaceFlag > 0) {
            for (int i = 0; i < newString.length(); i++) {
                if (newString.charAt(i) == find) {
                    foundCount++;
                    if ((foundCount & replaceFlag) != 0) {
                        newString.replace(i, i + 1, String.valueOf(replaceWith));
                    }
                }
            }
        }
        return newString.toString();
    }

    private static ArrayList<Long> explodePatterns(String pattern) {
        //For a given pattern.  e.g. 2**4, this function will create a list
        //of all the 10 possible values of replacing all "*" with  so 2004, 2114,
        // 2224, 2334, 2444, 2554, 2664, 2774, 2884, and 2994

        //Note that * is a special char so we have to escape it on the replace instruction.
        ArrayList<Long> retValues = new ArrayList<>(10);
        if (pattern.contains("*")) {
            for (int i = pattern.startsWith("*") ? 1 : 0; i < 10; i++) {
                retValues.add(Long.valueOf(pattern.replaceAll("\\*", Integer.toString(i))));
            }
        }
        return retValues;
    }

    public long problem52() {

        //It can be seen that the number, 125874, and its double, 251748, contain exactly
        //the same digits, but in a different order.
        //
        //Find the smallest positive integer, x, such that 2x, 3x, 4x, 5x, and 6x, 
        //contain the same digits.
        int candidate = 1;
        while (true) {
            String strAux = Integer.toString(candidate);
            if (isPermutationString(strAux, candidate * 2)
                    && isPermutationString(strAux, candidate * 3)
                    && isPermutationString(strAux, candidate * 4)
                    && isPermutationString(strAux, candidate * 5)
                    && isPermutationString(strAux, candidate * 6)) {
                return candidate;
            } else {
                candidate++;
            }

        }

    }

    public long problem53() {
        //There are exactly ten ways of selecting three from five, 12345:
        //
        //123, 124, 125, 134, 135, 145, 234, 235, 245, and 345
        //
        //In combinatorics, we use the notation, 5C3 = 10.
        //
        //In general,
        //nCr = 	
        //n!/r!(n−r)!
        //	,where r ≤ n, n! = n×(n−1)×...×3×2×1, and 0! = 1.
        //
        //It is not until n = 23, that a value exceeds one-million: 23C10 = 1144066.
        //
        //How many, not necessarily distinct, values of  nCr, for 1 ≤ n ≤ 100, are greater than one-million?
        BiPredicate<Integer, Integer> isCombinatorialOver1M;

        isCombinatorialOver1M = (n, r) -> {

            //rather than trying to compute the factorial which is likely to overwhelm 
            // "long" primitives
            //I will first "simplify" numerator and denominator, simplify 
            //and then see if the remainer is over 1M (don't care for exact value)
            if (r >= n) {
                return false;

            } else {
                //For Example of n = 5 and r = 3
                // (5*4*3*2*1) / (3*2*1) * (2*1)

                //So the top value will only be products from n to n - max(r, n-r);
                //and the bottom will be min (r,n-r)!
                PrimeFactors pm = new PrimeFactors(1);

                for (int i = n; i > Math.max(r, n - r); i--) {
                    pm.multiplyBy(i);
                }

                for (int i = Math.min(r, n - r); i > 1; i--) {
                    pm.divideBy(i);
                }

                long val = 1;
                //Now just multiply the values
                for (Map.Entry<Long, Long> entry : pm.factors.entrySet()) {
                    val *= (long) Math.pow(entry.getKey(), entry.getValue());
                    if (val > 1_000_000) {
                        return true;
                    }
                }
                return false;

            }
        };

        int countVals = 0;

        for (int n = 1; n <= 100; n++) {
            for (int r = 1; r <= n; r++) {

                if (isCombinatorialOver1M.test(n, r)) {
                    countVals++;
                }
            }
        }
        return countVals;

    }

    public static double combinatorial(int n, int r) {
        // answer = n!/r!(n-r!)

        //rather than trying to compute the factorial which is likely to overwhelm long values
        //I will first "simplify" numerator and denominator.
        if (r > n) {
            return -1;
        } else if (r == n) {
            return 1;
        } else {
            //For Example of n = 5 and r = 3
            // (5*4*3*2*1) / (3*2*1) * (2*1)

            //So the top value will only be products from n to n - max(r, n-r);
            //and the bottom will be min (r,n-r)!
            PrimeFactors pm = new PrimeFactors(1);

            for (int i = n; i > Math.max(r, n - r); i--) {
                pm.multiplyBy(i);
            }

            for (int i = Math.min(r, n - r); i > 1; i--) {
                pm.divideBy(i);
            }

            return pm.getValue();

        }
    }

    public int problem54() {

        //In the card game poker, a hand consists of five cards and are ranked
        //, from lowest to highest, in the following way:
        //
        //    High Card: Highest value card.
        //    One Pair: Two cards of the same value.
        //    Two Pairs: Two different pairs.
        //    Three of a Kind: Three cards of the same value.
        //    Straight: All cards are consecutive values.
        //    Flush: All cards of the same suit.
        //    Full House: Three of a kind and a pair.
        //    Four of a Kind: Four cards of the same value.
        //    Straight Flush: All cards are consecutive values of same suit.
        //    Royal Flush: Ten, Jack, Queen, King, Ace, in same suit.
        //
        //The cards are valued in the order:
        //2, 3, 4, 5, 6, 7, 8, 9, 10, Jack, Queen, King, Ace.
        //
        //If two players have the same ranked hands then the rank made up of 
        //the highest value wins; for example, a pair of eights beats a pair 
        //of fives (see example 1 below). But if two ranks tie, for example,
        //both players have a pair of queens, then highest cards in each hand 
        //are compared (see example 4 below); if the highest cards tie then the 
        // next highest cards are compared, and so on.
        //
        //Consider the following five hands dealt to two players:
        //Hand	 	Player 1	 	Player 2	 	Winner
        //1	 	5H 5C 6S 7S KD          2C 3S 8S 8D TD          Player 2
        //              Pair of Fives           Pair of Eights
        //	 	
        //2	 	5D 8C 9S JS AC          2C 5C 7D 8S QH          Player 1
        //              Highest card Ace        Highest card Queen
        //	 	
        //3	 	2D 9C AS AH AC          3D 6D 7D TD QD          Player 2
        //              Three Aces              Flush with Diamonds
        //	 		 	
        //4	 	4D 6S 9H QH QC          3D 6D 7H QD QS          Player 1
        //              Pair of Queens          Pair of Queens
        //              Highest card Nine       Highest card Seven
        //	 	
        //5	 	2H 2D 4C 4D 4S          3C 3D 3S 9S 9D          Player 1
        //              Full House              Full House
        //              With Three Fours        with Three Threes
        //
        //The file, poker.txt, contains one-thousand random hands dealt 
        //to two players. Each line of the file contains ten cards (separated by
        //a single space): the first five are Player 1's cards and the last five
        //are Player 2's cards. You can assume that all hands are valid (no 
        //invalid characters or repeated cards), each player's hand is in no 
        //specific order, and in each hand there is a clear winner.
        //
        //How many hands does Player 1 win?
        // H = Heart, D = Diamond, C = Club, J = Jack
        // T = 10, J = Jack, Q = Queen, K = King, A = C
        //PokerHand pAux = new PokerHand("9C 9D 7H TS TD");
        Path path = FileSystems.getDefault().getPath("./src", "p054_poker.txt");
        int CountOfP1Wins = 0;

        try {

            List<String> lines = Files.readAllLines(path, Charset.defaultCharset());

            CountOfP1Wins = (int) lines.stream().filter(line -> {
                PokerHand p1;
                PokerHand p2;
                p1 = new PokerHand(line.substring(0, 14));
                p2 = new PokerHand(line.substring(15));
                //System.out.println(String.format("Player 1 Hand: %s, Player 2 Hand: %s, P1_Wins:%b", line.substring(0, 14), line.substring(15), p1.compareTo(p2) > 0));
                return p1.compareTo(p2) > 0;
            }).count();

        } catch (IOException ex) {
            Logger.getLogger(Problems051to100.class.getName()).log(Level.SEVERE, null, ex);
        }
        return CountOfP1Wins;
    }

    private enum CardSuit implements Comparable<CardSuit> {

        HEART('H'),
        DIAMOND('D'),
        CLUB('C'),
        SPADES('S');

        private final char suit;

        private CardSuit(char suit) {
            this.suit = suit;
        }

        public static CardSuit valueOf(char c) {
            final Optional<CardSuit> h = Arrays.stream(values())
                    .filter(f -> f.suit == c)
                    .findAny();
            if (!h.isPresent()) {
                throw new IllegalArgumentException("Invalid Card Suit: " + c);
            }
            return h.get();
        }
    };

    private class Card
            implements Comparator<Card>, Comparable<Card> {

        private int cardValue;
        private CardSuit cardSuit;

        public Card(String card) {
            //Expect 2 chars e.g. 8C
            if (card.length() != 2) {
                throw new IllegalArgumentException("Invalid Card Length");
            }

            String val = card.toUpperCase().substring(0, 1);
            if (!"23456789TJQKA".contains(val)) {
                throw new IllegalArgumentException("Invalid Card Value: " + val);
            }

            cardSuit = CardSuit.valueOf(card.toUpperCase().charAt(1));

            if ("23456789".contains(val)) {
                cardValue = Integer.valueOf(val);
            } else {
                switch (val) {
                    case "T":
                        cardValue = 10;
                        break;
                    case "J":
                        cardValue = 11;
                        break;
                    case "Q":
                        cardValue = 12;
                        break;
                    case "K":
                        cardValue = 13;
                        break;
                    case "A":
                        cardValue = 14;
                        break;
                }
            }

        }

        public Card(int val, CardSuit suit) {
            cardValue = val;
            cardSuit = suit;
        }

        public int getCardValue() {
            return cardValue;
        }

        public CardSuit getCardSuit() {
            return cardSuit;
        }

        @Override
        public int compare(Card o1, Card o2) {
            if (o1.getCardValue() != o2.getCardValue()) {
                return o1.getCardValue() - o2.getCardValue();
            } else {
                return o1.getCardSuit().compareTo(o2.getCardSuit());
            }
        }

        @Override
        public int compareTo(Card o) {
            return compare(this, o);
        }

        @Override
        public String toString() {
            return "Card{" + cardValue + " " + cardSuit + '}';
        }

    }

    private class PokerHand
            implements Comparator<PokerHand>, Comparable<PokerHand> {

        private ArrayList<Card> hand = new ArrayList<>(5);

        public int rankValue;
        public int pokerHandRankValue;
        public int remainigcardValues;

        public PokerHand(String cards) {
            for (String s : cards.split(" ")) {
                hand.add(new Card(s));
            }

            Collections.sort(hand);

            computeRank();
        }

        private void computeRank() {

            boolean sameSuit = hand.stream().map(Card::getCardSuit).distinct().count() == 1;

            //EXAMPLE OF CODE ITERATION EVOLUTION

            //Somewhere up there we declared
            //List<Card> hand  
            //so "hand" is a collection of "Card" objects.
            //Each playing card has a suit and a value.
            //I want to find the smaller value of all the cards in the hand
            //Initialize my value with the max value of int
            int min = Integer.MAX_VALUE;

            //Basic loop
            for (int i = 0; i < hand.size(); i++) {
                Card card = hand.get(i);
                if (card.getCardValue() < min) {
                    min = card.getCardValue();
                }
            }

            //Iterators were introduced in Java 1.2
            //no need for that extraneous "int i" but 
            //the code looks almost the same.
            //Some would say even a bit more complicated.
            min = Integer.MAX_VALUE;

            for (Iterator<Card> it = hand.iterator(); it.hasNext();) {
                Card card = it.next();
                if (card.getCardValue() < min) {
                    min = card.getCardValue();
                }
            }

            //Java 5 introduced "Enhanced for loop"
            //The bytecode generated by the compiler is the same
            //as the one generated by the code above but this 
            //is much cleaner to read/understand.
            min = Integer.MAX_VALUE;

            for (Card card : hand) {
                if (card.getCardValue() < min) {
                    min = card.getCardValue();
                }
            }

            //We can make the code a bit more compact by getting rid of the 'if'
            //this is probably as small as we can get in JDK 7.
            min = Integer.MAX_VALUE;

            for (Card card : hand) {
                min = Math.min(min, card.getCardValue());
            }


            //This is a good example of "Map Reduce".
            //I have to do two things.  MAP the value of the card into a integer.  So each
            //card is "converted" into a integer.
            //Then I REDUCE all those integers into a single one.  I could sum them, count then, or
            //in this case, find the max or the min.
            //Note that "min()" returns an "Optional<Integer>" also a new thing in JDK 8
            //the collection might be empty and therefore there might be no min.
            OptionalInt optMin = hand.stream().mapToInt(Card::getCardValue).min();

            if (optMin.isPresent()) {
                min = optMin.getAsInt();
            }

            // In this case I know the collection will have some value so 
            //I can just skip the check and go straight to:
            min = hand.stream().mapToInt(Card::getCardValue)
                    .min()
                    .getAsInt();


            //If the collection was large and I wanted to see if I get some value
            //by running this in parallel I would change one thing:
            min = hand.parallelStream().mapToInt(Card::getCardValue)
                    .min()
                    .getAsInt();

            //Let's print out the hand for debugging
            hand.stream().forEach(System.out::println);

            //Check to see if they are all the same suit.
            if (sameSuit && (min == 10)) {
                //Royal Flush: Ten, Jack, Queen, King, Ace, in same suit.
                //Since they are all the same value, if the min is 10 the higher ones have to be J,K,Q,A
                rankValue = 10;
                pokerHandRankValue = 0;
                remainigcardValues = 0;
                return;
            }

            int max = hand.stream().mapToInt(Card::getCardValue).max().getAsInt();

            //Straight Flush: All cards are consecutive values of same suit.
            if (sameSuit && (max - min == 4)) {
                rankValue = 9;
                pokerHandRankValue = max;
                remainigcardValues = 0;
                return;
            }

            //At this point it is a good idea to create a hash map of 
            //card-value, count-of-cards.
            HashMap<Integer, Integer> cardCounts = new HashMap<>();
            hand.stream().forEach(card -> {
                if (!cardCounts.containsKey(card.cardValue)) {
                    cardCounts.put(card.cardValue, 1);
                } else {
                    cardCounts.put(card.cardValue, cardCounts.get(card.cardValue) + 1);
                }
            });

            //Four of a Kind: Four cards of the same value.
            if (cardCounts.containsValue(4)) {
                rankValue = 8;
                pokerHandRankValue = cardCounts.entrySet().stream().filter(i -> i.getValue() == 4).findAny().get().getKey();
                remainigcardValues = cardCounts.entrySet().stream().filter(i -> i.getValue() != 4).findAny().get().getKey();
                return;
            }

            //Full House: Three of a kind and a pair.
            if (cardCounts.containsValue(3) && cardCounts.containsValue(2)) {
                rankValue = 7;
                pokerHandRankValue = cardCounts.entrySet().stream().filter(i -> i.getValue() == 3).findAny().get().getKey();
                remainigcardValues = cardCounts.entrySet().stream().filter(i -> i.getValue() == 2).findAny().get().getKey();
            }

            //Now we might need to start setting the remaining card value to a 
            //composite of more than one card so lets create a function that will do that for us.
            ToIntFunction<List<Card>> computeCardsValue = (c) -> {

                //Lets do the highsest  * 15^4, second * 15^3, third * 15^2, fourth * 15 and fifth * 1. 
                //That way we only compare one number.
                int sum = 0;
                for (int i = c.size() - 1; i >= 0; i--) {
                    sum += c.get(i).getCardValue() * Math.pow(15, i);
                }
                return sum;
            };

            //Flush: All cards of the same suit.
            if (sameSuit) {
                rankValue = 6;
                pokerHandRankValue = max;
                remainigcardValues = computeCardsValue.applyAsInt(hand);
                return;
            }

            //Straight: All cards are consecutive values.
            if ((max - min == 4) && (cardCounts.size() == 5)) {
                rankValue = 5;
                pokerHandRankValue = max;
                remainigcardValues = computeCardsValue.applyAsInt(hand);
                return;
            }

            //Three of a Kind: Three cards of the same value.
            if (cardCounts.containsValue(3)) {
                rankValue = 4;
                pokerHandRankValue = cardCounts.entrySet().stream().filter(i -> i.getValue() == 3).findAny().get().getKey();
                remainigcardValues = computeCardsValue.applyAsInt(hand.stream().filter(i -> i.getCardValue() != pokerHandRankValue).collect(Collectors.toCollection(ArrayList::new)));
                return;
            }
            //Two Pairs: Two different pairs.
            if (cardCounts.entrySet().stream().filter(p -> p.getValue() == 2).count() == 2) {
                rankValue = 3;
                int maxPair = cardCounts.entrySet().stream().filter(i -> i.getValue() == 2).mapToInt(p -> p.getKey()).max().getAsInt();
                int minPair = cardCounts.entrySet().stream().filter(i -> i.getValue() == 2).mapToInt(p -> p.getKey()).min().getAsInt();
                pokerHandRankValue = maxPair * 15 + minPair;
                remainigcardValues = cardCounts.entrySet().stream().filter(p -> p.getValue() == 1).findAny().get().getKey();
                return;
            }

            //One Pair: Two cards of the same value.
            if (cardCounts.containsValue(2)) {
                rankValue = 2;
                pokerHandRankValue = cardCounts.entrySet().stream().filter(i -> i.getValue() == 2).findAny().get().getKey();
                remainigcardValues = computeCardsValue.applyAsInt(hand.stream().filter(i -> i.getCardValue() != pokerHandRankValue).collect(Collectors.toCollection(ArrayList::new)));
                return;
            }

            rankValue = 1;
            pokerHandRankValue = max;
            remainigcardValues = computeCardsValue.applyAsInt(hand);

        }

        @Override
        public int compare(PokerHand o1, PokerHand o2) {

            if (o1.rankValue != o2.rankValue) {
                return o1.rankValue - o2.rankValue;
            } else if (o1.pokerHandRankValue != o2.pokerHandRankValue) {
                return o1.pokerHandRankValue - o2.pokerHandRankValue;
            } else {
                return o1.remainigcardValues - o2.remainigcardValues;
            }

        }

        @Override
        public int compareTo(PokerHand o) {
            return compare(this, o);
        }

    }

    public long problem55() {

        //If we take 47, reverse and add, 47 + 74 = 121, which is palindromic.
        //
        //Not all numbers produce palindromes so quickly. For example,
        //
        //349 + 943 = 1292,
        //1292 + 2921 = 4213
        //4213 + 3124 = 7337
        //
        //That is, 349 took three iterations to arrive at a palindrome.
        //
        //Although no one has proved it yet, it is thought that some numbers, 
        //like 196, never produce a palindrome. A number that never forms a 
        //palindrome through the reverse and add process is called a Lychrel 
        //number. Due to the theoretical nature of these numbers, and for 
        //the purpose of this problem, we shall assume that a number is Lychrel 
        //until proven otherwise. In addition you are given that for every 
        //number below ten-thousand, it will either (i) become a palindrome in 
        //less than fifty iterations, or, (ii) no one, with all the computing power 
        //that exists, has managed so far to map it to a palindrome. In fact, 10677 
        //is the first number to be shown to require over fifty iterations before 
        //producing a palindrome: 4668731596684224866951378664 (53 iterations, 28-digits).
        //
        //Surprisingly, there are palindromic numbers that are themselves Lychrel 
        //numbers; the first example is 4994.
        //
        //How many Lychrel numbers are there below ten-thousand?
        //
        //NOTE: Wording was modified slightly on 24 April 2007 to emphasise the 
        //theoretical nature of Lychrel numbers.   
        //System.out.println(isLychrel(5));
        int countLychel = 0;
        for (int i = 1; i < 10_000; i++) {
            if (isLychrel(i)) {
                //System.out.println(i);
                countLychel++;
            }

        }
        return countLychel;
    }

    private boolean isLychrel(int num) {
        //The numbers can grow big so lets use big int.
        BigInteger number = BigInteger.valueOf(num);
        for (int i = 1; i <= 50; i++) {
            //System.out.println(number.toString());
            BigInteger reverse = setValue(new StringBuilder(number.toString()).reverse().toString());
            number = reverse.add(number);
            if (isPalindrome(number.toString())) {
                return false;
            }

        }
        return true;
    }

    public BigInteger setValue(String val) {

        if (val.length() < 19) {
            return BigInteger.valueOf(Long.valueOf(val));
        } else {
            BigInteger retVal = BigInteger.valueOf(Long.valueOf(val.substring(0, 18)));
            for (int i = 18; i < val.length(); i++) {
                retVal = retVal.multiply(BigInteger.TEN);
                retVal = retVal.add(BigInteger.valueOf(Long.valueOf(val.substring(i, i + 1))));
            }
            return retVal;
        }
    }

    public long problem56() {

        //A googol (10^100) is a massive number: one followed by one-hundred zeros; 100^100 
        //is almost unimaginably large: one followed by two-hundred zeros. Despite their size, 
        //the sum of the digits in each number is only 1.
        //
        //Considering natural numbers of the form, a^b, where a, b < 100, what is the maximum digital sum?
        int maxDigits = 0;
        for (int a = 2; a <= 100; a++) {
            for (int b = 2; b <= 100; b++) {
                BigInteger num = new BigInteger(Integer.toString(a));
                num = num.pow(b);
                maxDigits = Math.max(maxDigits, sumofDigits(num));
            }
        }
        return maxDigits;
    }

    public int problem57() {

        //It is possible to show that the square root of two can be expressed as an 
        //infinite continued fraction.
        //
        // √2 = 1 + 1/(2 + 1/(2 + 1/(2 + ... ))) = 1.414213...
        //
        //By expanding this for the first four iterations, we get:
        //
        //1 + 1/2 = 3/2 = 1.5
        //1 + 1/(2 + 1/2) = 7/5 = 1.4
        //1 + 1/(2 + 1/(2 + 1/2)) = 17/12 = 1.41666...
        //1 + 1/(2 + 1/(2 + 1/(2 + 1/2))) = 41/29 = 1.41379...
        //
        //The next three expansions are 99/70, 239/169, and 577/408, but the eighth 
        //expansion, 1393/985, is the first example where the number of digits in the 
        //numerator exceeds the number of digits in the denominator.
        //
        //In the first one-thousand expansions, how many fractions contain a numerator 
        //with more digits than denominator?
        BigInteger numerator = BigInteger.ONE;
        BigInteger denominator = BigInteger.ONE;

        int countMoreDigits = 0;

        for (int i = 1; i <= 1000; i++) {

            //Add one
            numerator = numerator.add(denominator);

            //flip the order
            BigInteger aux = numerator;
            numerator = denominator;
            denominator = aux;

            //Then add another one
            numerator = numerator.add(denominator);

            //See which one has more digits.
            if (numerator.toString().length() > denominator.toString().length()) {
                countMoreDigits++;
            }

            //System.out.println (String.format("Flip %d, %s/%s",i, numerator.toString(),denominator.toString()));
        }
        return countMoreDigits;

    }

    public int problem58() {

        //Starting with 1 and spiralling anticlockwise in the following way, a square 
        //spiral with side length 7 is formed.
        //
        //37 36 35 34 33 32 31
        //38 17 16 15 14 13 30
        //39 18  5  4  3 12 29
        //40 19  6  1  2 11 28
        //41 20  7  8  9 10 27
        //42 21 22 23 24 25 26
        //43 44 45 46 47 48 49
        //
        //It is interesting to note that the odd squares lie along the bottom right 
        //diagonal, but what is more interesting is that 8 out of the 13 numbers lying 
        //along both diagonals are prime; that is, a ratio of 8/13 ≈ 62%.
        //
        //If one complete new layer is wrapped around the spiral above, a square spiral 
        //with side length 9 will be formed. If this process is continued, what is the 
        //side length of the square spiral for which the ratio of primes along both 
        //diagonals first falls below 10%?
        //Starting condition
        int countOfNumbers = 5; //{1,3,5,7,9}
        int countOfPrimes = 3; //{3,5,7}
        int sideLength = 3;
        int maxVal = 9;

        while ((countOfPrimes * 1.0 / countOfNumbers) > .1) {
            //figure out the values of the diagonal
            sideLength += 2; //Every time we add a layer we add two to the side
            for (int i = 1; i < 5; i++) //Each layer adds four sides
            {
                //New diagonal value
                maxVal += sideLength - 1;
                countOfNumbers++;
                if (Primes.isPrime(maxVal)) {
                    countOfPrimes++;
                }
            }
        }

        return sideLength;
    }

    public int problem59() {

        //Each character on a computer is assigned a unique code and the preferred 
        //standard is ASCII (American Standard Code for Information Interchange). For 
        //example, uppercase A = 65, asterisk (*) = 42, and lowercase k = 107.
        //
        //A modern encryption method is to take a text file, convert the bytes to ASCII, 
        //then XOR each byte with a given value, taken from a secret key. The advantage 
        //with the XOR function is that using the same encryption key on the cipher text, 
        //restores the plain text; for example, 65 XOR 42 = 107, then 107 XOR 42 = 65.
        //
        //For unbreakable encryption, the key is the same length as the plain text 
        //message, and the key is made up of random bytes. The user would keep the 
        //encrypted message and the encryption key in different locations, and without 
        //both "halves", it is impossible to decrypt the message.
        //
        //Unfortunately, this method is impractical for most users, so the modified method
        //is to use a password as a key. If the password is shorter than the message, 
        //which is likely, the key is repeated cyclically throughout the message. The 
        //balance for this method is using a sufficiently long password key for security, 
        //but short enough to be memorable.
        //
        //Your task has been made easy, as the encryption key consists of three lower case
        //characters. Using cipher.txt (right click and 'Save Link/Target As...'), a file 
        //containing the encrypted ASCII codes, and the knowledge that the plain text must 
        //contain common English words, decrypt the message and find the sum of the ASCII 
        //values in the original text.
        Path path = FileSystems.getDefault().getPath("./src", "p059_cipher.txt");
        ArrayList<Character> charCodes = new ArrayList<>();

        try {
            //Split the single line on "," and then remove the quotations.
            List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
            lines.stream().map((line) -> line.split(",")).forEach((values) -> {
                for (String code : values) {
                    charCodes.add((char) Integer.parseInt(code));
                }
            });

        } catch (IOException ex) {
            Logger.getLogger(Problems001to050.class.getName()).log(Level.SEVERE, null, ex);
        }

        for (char a = 'a'; a <= 'z'; a++) {
            for (char b = 'a'; b <= 'z'; b++) {
                for (char c = 'a'; c <= 'z'; c++) {
                    String password = new String(new char[]{a, b, c});

                    //Try to decode:
                    StringBuilder answer = new StringBuilder();
                    for (int i = 0; i < charCodes.size(); i++) {
                        answer.append((char) (charCodes.get(i) ^ password.charAt(i % 3)));
                    }

                    //Now lets see if the result has several 
                    //common english words:https://en.wikipedia.org/wiki/Most_common_words_in_English
                    String[] commonWords = {"the", "be", "to", "of", "and", "in", "that", //removed a from the list
                        "have", "it", "for", "not", "on", "with", "he", //removed I from the list since its too simple
                        "as", "you", "do", "at", "this", "but", "his", "by",
                        "from", "they", "we", "say", "her", "she", "or", "an",
                        "will", "my", "one", "all", "would", "there", "their",
                        "what", "so", "up", "out", "if", "about", "who", "get",
                        "which", "go", "me", "when", "make", "can", "like",
                        "time", "no", "just", "him", "know", "take", "people",
                        "into", "year", "your", "good", "some", "could", "them",
                        "see", "other", "than", "then", "now", "look", "only",
                        "come", "its", "over", "think", "also", "back", "after",
                        "use", "two", "how", "our", "work", "first", "well", "way",
                        "even", "new", "want", "because", "any", "these", "give",
                        "day", "most", "us"};

                    int countCommon = 0;
                    for (String word : commonWords) {
                        if (answer.toString().toLowerCase().contains(word)) {
                            countCommon++;
                        }
                    }

                    if (countCommon > 40) { // Guess some to find the threshold.
                        System.out.println(countCommon);
                        System.out.println(password);
                        System.out.println(answer);

                        // Summ the ascii for the answer.
                        return answer.chars().sum();
                    }
                }
            }
        }

        return -1;
    }

    {

//    public int problem60_BAD() {
//
//        //The primes 3, 7, 109, and 673, are quite remarkable. By taking any two primes
//        //and concatenating them in any order the result will always be prime. For 
//        //example, taking 7 and 109, both 7109 and 1097 are prime. The sum of these four 
//        //primes, 792, represents the lowest sum for a set of four primes with this property.
//        //
//        //Find the lowest sum for a set of five primes for which any two primes 
//        //concatenate to produce another prime.
//        //Assuming that the largest value in the series is under 10,000 (if wrong we will bump up)
//        boolean found = false;
//        int target = 5;
//
//        int maxPrime = 10_000;
//
//        long start = 2;
//
//        ArrayList<Long> primeList = null;
//        while (!found & start < maxPrime) {
//            start = Primes.nextPrime(start);
//            primeList = new ArrayList<>();
//            primeList.add(start);
//            //See if we can find truncables;
//            primeList = AddNextConcatenablePrimate(primeList, maxPrime, target);
//            found = primeList.size() == target;
//        }
//
//        if (found) {
//            primeList.forEach(System.out::println);
//        }
//        return -1;
//    }
//
//    private ArrayList<Long> AddNextConcatenablePrimate(ArrayList<Long> currentConcatenablePrimes, long maxPrime, int desiredCount) {
//        //Assume that "currentConcatenablePrimaes" includes a ordered list of primes of at least one.
//        //they are all "concatenable.  Find the "next" value < maxPrime that is concatenable.
//
//        if (currentConcatenablePrimes.size() == desiredCount) {
//            return currentConcatenablePrimes;
//        } else {
//            long currentPrime = Primes.nextPrime(currentConcatenablePrimes.get(currentConcatenablePrimes.size() - 1));
//            if ((currentConcatenablePrimes.size() == 1) && (currentConcatenablePrimes.get(0) == 13)) {
//                System.out.println("now in 5197");
//            }
//            while (currentPrime < maxPrime) {
//                //See if the currentPrime is "concatenable" with the prior ones.
//                long testVariable = currentPrime;  //This is beause lambda was effectively final.
//
//                if (currentConcatenablePrimes.stream().filter(p -> areConcatenablePrimates(p, testVariable)).count()
//                        == currentConcatenablePrimes.size()) {
//                    ArrayList<Long> copy = new ArrayList<>();
//                    currentConcatenablePrimes.forEach(copy::add);
//                    copy.add(currentPrime);
//                    return AddNextConcatenablePrimate(copy, maxPrime, desiredCount);
//                }
//                //Try with the next prime 
//                currentPrime = Primes.nextPrime(currentPrime);
//            }
//
//        }
//
//        //if I get to here I couldn't find an answer so I can just return the current list.
//        System.out.println(String.format("Max size %d, min prime %d", currentConcatenablePrimes.size(), currentConcatenablePrimes.get(0)));
//        return currentConcatenablePrimes;
//    }
//
//    public long problem60_BAD2() {
//
//        //The primes 3, 7, 109, and 673, are quite remarkable. By taking any two primes
//        //and concatenating them in any order the result will always be prime. For 
//        //example, taking 7 and 109, both 7109 and 1097 are prime. The sum of these four 
//        //primes, 792, represents the lowest sum for a set of four primes with this property.
//        //
//        //Find the lowest sum for a set of five primes for which any two primes 
//        //concatenate to produce another prime.
//        long thisPrime = 11;
//        boolean found = false;
//        OptionalInt retVal = OptionalInt.of(0);
//
//        HashMap<String, PrimeSequence> sets = new HashMap<>();
//
//        while (!found) {
//            thisPrime = Primes.nextPrime(thisPrime);
//            //Find the contatenable primes that make up this prime
//            PrimeSequence newVals = splitPrimeContatenations(thisPrime);
//
//            newVals.primes.forEach(p -> {
//                if (!sets.containsKey(Long.toString(p))) {
//                    //Test each new value against each current set 
//                    //and if the value is contatenatble create a new set add 
//                    //them to the possible sets.
//                    HashMap<String, PrimeSequence> newSet = new HashMap<>();
//                    newSet.put(p.toString(), new PrimeSequence(p));
//
//                    sets.forEach((k, v) -> {
//                        if (v.primes.stream().filter(j -> areConcatenablePrimates(j, p)).count() == v.primes.size()) {
//                            PrimeSequence ps = v.clone();
//                            ps.add(p);
//                            newSet.put(ps.toString(), ps);
//                        }
//                    });
//
//                    //Add all the new sets to the "running" total
//                    newSet.forEach((k, v) -> {
//                        if (!sets.containsKey(k)) {
//                            sets.put(k, v);
//                        }
//                    });
//
//                };
//            });
//            retVal = sets.values().stream().mapToInt(p -> p.primes.size()).max();
//            if (retVal.isPresent()) {
//                found = (retVal.getAsInt() >= 5);
//                System.out.println(String.format("For value %s the max # is %d", thisPrime, retVal.getAsInt()));
//            }
//
//        }
//
//        return sets.values().stream().filter(p -> p.primes.size() >= 5).findFirst().get().primes.stream().mapToLong(r -> r).sum();
//
//    }
//
//    private PrimeSequence splitPrimeContatenations(long prime) {
//        PrimeSequence returnVal = new PrimeSequence();
//        if (prime < 10) {
//            return returnVal;
//        }
//        String primeStr = Long.toString(prime);
//        for (int i = 1; i <= primeStr.length() - 1; i++) {
//            if (primeStr.charAt(i) != '0') {
//                long a = Long.valueOf(primeStr.substring(0, i));
//                long b = Long.valueOf(primeStr.substring(i));
//                if (Primes.isPrime(a) && Primes.isPrime(b)) {
//                    returnVal.add(a, b);
//                    //System.out.println(String.format("String 1 %s; String 2: %s", primeStr.substring(0, i), primeStr.substring(i)));
//                }
//            }
//        }
//        return returnVal;
//    }
//
//    private class PrimeSequence {
//
//        public TreeSet<Long> primes;
//
//        public PrimeSequence(TreeSet<Long> values) {
//            primes = new TreeSet<>();
//            values.forEach(primes::add);
//        }
//
//        public PrimeSequence() {
//            primes = new TreeSet<>();
//        }
//
//        public PrimeSequence(long... values) {
//            primes = new TreeSet<>();
//            this.add(values);
//            for (long p : values) {
//                primes.add(p);
//            }
//        }
//
//        public void add(long... values) {
//            for (long p : values) {
//                primes.add(p);
//            }
//        }
//
//        @Override
//        public PrimeSequence clone() {
//            return new PrimeSequence(primes);
//        }
//
//        @Override
//        public int hashCode() {
//            int hash = 7;
//            hash = 97 * hash + Objects.hashCode(this.primes);
//            return hash;
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            if (obj == null) {
//                return false;
//            }
//            if (getClass() != obj.getClass()) {
//                return false;
//            }
//            final PrimeSequence other = (PrimeSequence) obj;
//            if (!Objects.equals(this.primes, other.primes)) {
//                return false;
//            }
//            return true;
//        }
//
//        @Override
//        public String toString() {
//            StringBuilder value = new StringBuilder();
//            primes.forEach(p -> value.append(String.format("%d,", p)));
//            return value.toString().substring(0, value.length() - 1);
//        }
//
//    }
    }

    public long problem60() {

        //The primes 3, 7, 109, and 673, are quite remarkable. By taking any two primes
        //and concatenating them in any order the result will always be prime. For 
        //example, taking 7 and 109, both 7109 and 1097 are prime. The sum of these four 
        //primes, 792, represents the lowest sum for a set of four primes with this property.
        //
        //Find the lowest sum for a set of five primes for which any two primes 
        //concatenate to produce another prime.
        long MaxPrime = 10_000;

        for (long a = 3; a < MaxPrime; a = Primes.nextPrime(a)) {

            for (long b = Primes.nextPrime(a); b < MaxPrime; b = Primes.nextPrime(b)) {

                if (areConcatenablePrimates(a, b)) {

                    for (long c = Primes.nextPrime(b); c < MaxPrime; c = Primes.nextPrime(c)) {

                        if (areConcatenablePrimates(a, c)
                                && areConcatenablePrimates(b, c)) {

                            for (long d = Primes.nextPrime(c); d < MaxPrime; d = Primes.nextPrime(d)) {

                                if (areConcatenablePrimates(a, d)
                                        && areConcatenablePrimates(b, d)
                                        && areConcatenablePrimates(c, d)) {

                                    for (long e = Primes.nextPrime(d); e < MaxPrime; e = Primes.nextPrime(e)) {
                                        if (areConcatenablePrimates(a, e)
                                                && areConcatenablePrimates(b, e)
                                                && areConcatenablePrimates(c, e)
                                                && areConcatenablePrimates(d, e)) {
                                            return a + b + c + d + e;
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return -1;
    }

    public long problem60b() {
        //The size of the array determines the target size of concatenable primes
        long values[] = new long[5];

        //The 10_000 is my guess as to what the largest value should be to get a
        //valid solution.  If it doesn't work I will get zero and will have to 
        //increment the max value.
        return searchForNext(values, 10_000, 0, 3);
    }

    private long searchForNext(long[] values, long maxPrime, int position, long start) {
        for (long a = start; a <= maxPrime; a = Primes.nextPrime(a)) {
            boolean isConcatenable = true;
            for (int b = 0; b <= position - 1; b++) {
                isConcatenable = isConcatenable && areConcatenablePrimates(a, values[b]);
            }
            if (isConcatenable) {
                values[position] = a;
                if (position < values.length - 1) {
                    long ret = searchForNext(values, maxPrime, position + 1, Primes.nextPrime(a));
                    if (ret > 0) {
                        return ret;
                    }
                } else {
                    //Found the answer!
                    long sum = 0;
                    for (long val : values) {
                        System.out.println(val);
                        sum += val;
                    }
                    return sum;
                }
            }
        }
        return 0;
    }

    private boolean areConcatenablePrimates(long p1, long p2) {

        return (Primes.isPrime(Long.valueOf(String.valueOf(p1) + String.valueOf(p2)))
                && Primes.isPrime(Long.valueOf(String.valueOf(p2) + String.valueOf(p1))));
    }

    public int problem61() {
        //Triangle, square, pentagonal, hexagonal, heptagonal, and octagonal numbers are 
        //all figurate (polygonal) numbers and are generated by the following formulae:
        //Triangle 	  	P3,n=n(n+1)/2 	  	1, 3, 6, 10, 15, ...
        //Square 	  	P4,n=n^2 	  	1, 4, 9, 16, 25, ...
        //Pentagonal 	  	P5,n=n(3n−1)/2 	  	1, 5, 12, 22, 35, ...
        //Hexagonal 	  	P6,n=n(2n−1) 	  	1, 6, 15, 28, 45, ...
        //Heptagonal 	  	P7,n=n(5n−3)/2 	  	1, 7, 18, 34, 55, ...
        //Octagonal 	  	P8,n=n(3n−2) 	  	1, 8, 21, 40, 65, ...
        //
        //The ordered set of three 4-digit numbers: 8128, 2882, 8281, has three 
        //interesting properties.
        //
        //    The set is cyclic, in that the last two digits of each number is the first 
        //    two digits of the next number (including the last number with the first).
        //    Each polygonal type: triangle (P3,127=8128), square (P4,91=8281), and 
        //    pentagonal (P5,44=2882), is represented by a different number in the set.
        //    This is the only set of 4-digit numbers with this property.
        //
        //Find the sum of the only ordered set of six cyclic 4-digit numbers for which 
        //each polygonal type: triangle, square, pentagonal, hexagonal, heptagonal, and 
        //octagonal, is represented by a different number in the set.

        TreeSet<Integer>[] possibleValues = new TreeSet[6];

        //Fill the possible values.
        for (int i = 0; i <= 5; i++) {
            possibleValues[i] = new TreeSet<>();
        }

        //Find the first triangular value over 9999.
        int n = (int) Math.floor((Math.sqrt(1000 * 8 + 1) - 1) / 2);
        n++;
        int a = triangular(n);

        //Put all the four digits triangulars into the collection
        while (a < 10_000) {
            if (a % 100 > 10) {
                //Exclude the values that have zero as the tenths digit.
                possibleValues[0].add(a);
            }
            n++;
            a = triangular(n);
        }
        //Squares
        n = (int) Math.floor(Math.sqrt(1000));
        n++;
        a = square(n);
        while (a < 10_000) {
            if (a % 100 > 10) //Exclude the values that have zero as the tenths digit.
            {
                possibleValues[1].add(a);
            }
            n++;
            a = square(n);
        }
        //Pentagons
        n = (int) Math.floor((Math.sqrt(1000 * 24 + 1) + 1) / 6);
        n++;
        a = pentagonal(n);
        while (a < 10_000) {
            if (a % 100 > 10) //Exclude the values that have zero as the tenths digit.
            {
                possibleValues[2].add(a);
            }
            n++;
            a = pentagonal(n);
        }
        //Hexagonal
        n = (int) Math.floor((Math.sqrt(1000 * 8 + 1) + 1) / 4);
        n++;
        a = hexagonal(n);
        while (a < 10_000) {
            if (a % 100 > 10) //Exclude the values that have zero as the tenths digit.
            {
                possibleValues[3].add(a);
            }
            n++;
            a = hexagonal(n);
        }
        //Heptagonal
        n = (int) Math.floor((Math.sqrt(1000 * 40 + 9) + 3) / 10);
        n++;
        a = heptagonal(n);
        while (a < 10_000) {
            if (a % 100 > 10) //Exclude the values that have zero as the tenths digit.
            {
                possibleValues[4].add(a);
            }
            n++;
            a = heptagonal(n);
        }
        //Octagonal
        n = (int) Math.floor((Math.sqrt(1000 * 3 + 1) + 1) / 3);
        n++;
        a = octagonal(n);
        while (a < 10_000) {
            if (a % 100 > 10) //Exclude the values that have zero as the tenths digit.
            {
                possibleValues[5].add(a);
            }
            n++;
            a = octagonal(n);
        }

//        int i = 0;
//        for (TreeSet<Integer> ts : possibleValues) {
//            System.out.println(i++);
//            ts.forEach(System.out::println);
//        }
        //Use an ArrayList of LinkedHashMaps to store the possible sets.
        //The keys in the maps will refer to the "Polygonal" value
        //The values will be the sets
        ArrayList<SetofPolygonals> sets = new ArrayList<>();

        //First copy the last polygon.
        for (Integer l : possibleValues[2]) {
            SetofPolygonals set = new SetofPolygonals();
            set.put(2, l);
            sets.add(set);
        }

        final int targetSize = 6;
        for (int j = 0; j < targetSize - 1; j++) {
            //Find the remaining poligons.
            sets = addCyclicValue(possibleValues, sets, targetSize);
        }

        Optional<SetofPolygonals> ans;
        //System.out.println(sets.stream().filter(s -> s.isClosedLoop()).count());
        ans = sets.stream().filter(s -> s.isClosedLoop()).findFirst();
        if (ans.isPresent()) {
            //ans.get().choosenValues.forEach(System.out::println);
            return ans.get().choosenValues.stream().mapToInt(l -> l).sum();
        } else {
            return -1;
        }

    }

    private ArrayList<SetofPolygonals> addCyclicValue(TreeSet<Integer>[] possibleValues, ArrayList<SetofPolygonals> sets, int maxPoly) {
        //Add a new value to the each set in "sets"
        //Store the new sets in a new list.
        ArrayList<SetofPolygonals> newSets = new ArrayList<>();
        for (SetofPolygonals set : sets) {
            for (int i = 0; i < maxPoly; i++) {
                if (!set.choosenPolygons.contains(i)) {
                    SortedSet<Integer> matches = possibleValues[i].subSet(set.getNextValueMin(), set.getNextValueMax() + 1);
                    if (matches.size() > 0) {
                        for (Integer a : matches) {
                            SetofPolygonals newSet = set.clone();
                            newSet.put(i, a);
                            newSets.add(newSet);
                        }
                    }
                }
            }
        }
        return newSets;
    }

    private class SetofPolygonals {

        public ArrayList<Integer> choosenPolygons = new ArrayList<>();
        public ArrayList<Integer> choosenValues = new ArrayList<>();

        public boolean put(int poligonal, int value) {
            if (this.choosenPolygons.contains(poligonal)) {
                return false;
            } else {
                this.choosenPolygons.add(poligonal);
                this.choosenValues.add(value);
                return true;
            }
        }

        public SetofPolygonals clone() {
            SetofPolygonals theClone = new SetofPolygonals();
            
            theClone.choosenPolygons = (ArrayList<Integer>) this.choosenPolygons.clone();
            theClone.choosenValues = (ArrayList<Integer>) this.choosenValues.clone();
            return theClone;
        }

        public int getLastValue() {
            if (this.choosenValues.size() > 0) {
                return this.choosenValues.get(this.choosenValues.size() - 1);
            } else {
                return 0;
            }
        }

        public int getNextValueMin() {
            if (this.choosenValues.size() > 0) {
                return this.getLastValue() % 100 * 100;
            } else {
                return 0;
            }

        }

        public int getNextValueMax() {
            if (this.choosenValues.size() > 0) {
                return this.getNextValueMin() + 99;
            } else {
                return 0;
            }
        }

        public boolean isClosedLoop() {
            if (this.choosenValues.size() > 0) {
                return this.choosenValues.get(0) >= this.getNextValueMin() && this.choosenValues.get(0) <= this.getNextValueMax();
            } else {
                return false;
            }
        }
    }

    public int triangular(int n) {
        return (int) n * (n + 1) / 2;
    }

    public int square(int n) {
        return n * n;
    }

    public int pentagonal(int n) {
        return (int) n * (3 * n - 1) / 2;
    }

    public long pentagonal(long n) {
        return n * (3 * n - 1) / 2;
    }

    public int hexagonal(int n) {
        return (int) n * (2 * n - 1);
    }

    public int heptagonal(int n) {
        return (int) n * (5 * n - 3) / 2;
    }

    public int octagonal(int n) {
        return (int) n * (3 * n - 2);
    }

    public int nextTriangular(int num) {
        //Return ths next triangular number.
        //P3,n=n(n+1)/2 
        //First figure out "n"
        int n = (int) Math.floor((Math.sqrt(num * 8 + 1) - 1) / 2);
        n++;
        return triangular(n);
    }

    public int nextSquare(int num) {
        //P4,n=n^2 
        //First figure out "n"
        int n = (int) Math.floor(Math.sqrt(num));
        n++;
        return square(n);
    }

    public int nextPentagonal(int num) {
        //P5,n=n(3n−1)/2 	 
        //First figure out "n"
        int n = (int) Math.floor((Math.sqrt(num * 24 + 1) + 1) / 6);
        n++;
        return pentagonal(n);
    }

    public int nextHexagonal(int num) {
        //P6,n=n(2n−1)  
        //First figure out "n"
        int n = (int) Math.floor((Math.sqrt(num * 8 + 1) + 1) / 4);
        n++;
        return hexagonal(n);
    }

    public int nextHeptagonal(int num) {
        //P7,n=n(5n−3)/2
        //First figure out "n"
        int n = (int) Math.floor((Math.sqrt(num * 40 + 9) + 3) / 10);
        n++;
        return heptagonal(n);
    }

    public int nextOctagonal(int num) {
        //P8,n=n(3n−2) 
        //First figure out "n"
        int n = (int) Math.floor((Math.sqrt(num * 3 + 1) + 1) / 3);
        n++;
        return octagonal(n);
    }

    public int problem62() {

        //The cube, 41063625 (345^3), can be permuted to produce two other cubes: 56623104 (384^3) 
        //and 66430125 (405^3). In fact, 41063625 is the smallest cube which has exactly three 
        //permutations of its digits which are also cube.
        //
        //Find the smallest cube for which exactly five permutations of its digits are cube.
        int c = 1;
        HashMap<String, ArrayList<Integer>> counter = new HashMap<>();
        String key;
        while (true) {
            key = Utilities.sortString(longCube(c));
            Integer countVal;
            if (counter.containsKey(key)) {
                counter.get(key).add(c);
                if (counter.get(key).size() == 5) {
                    //Since the answer is longer than int I will print it
                    System.out.println(longCube(counter.get(key).get(0)));
                    //Answer is this value cubed.
                    return counter.get(key).get(0);
                }
            } else {
                counter.put(key, new ArrayList<>());
                counter.get(key).add(c);
            }
            c++;
        }
    }

    private String longCube(int c) {
        BigInteger val = BigInteger.valueOf(c);
        val = val.pow(3);
        return val.toString();
    }

    public int problem63() {

        //The 5-digit number, 16807=7^5, is also a fifth power. Similarly, the 9-digit 
        //number, 134217728=8^9, is a ninth power.
        //
        //How many n-digit positive integers exist which are also an nth power?
        //Since I don't know hoe many powers to try I will try with all powers
        //until I can't find a number that works
        boolean found = true;
        int count = 0;
        int pow = 0;
        while (found) {
            pow++;
            found = false;
            //Since 10^1 has 2 digits, 10^2 four, etc. We need to test only from 1 to 9
            for (int base = 1; base < 10; base++) {
                BigInteger num = BigInteger.valueOf(base);
                if (num.pow(pow).toString().length() == pow) {
                    count++;
                    found = true;
                }
            }
        }

        return count;
    }

    public int problem64() {

        //SEE http://en.wikipedia.org/wiki/Methods_of_computing_square_roots#Continued_fraction_expansion
        //All square roots are periodic when written as continued fractions and can be 
        //written in the form:
        //√N = a0 + 	1
        //  	    a1 + 1
        //  	  	a2 +1
        //  	  	  a3 + ...
        //
        // (...)
        //
        //It can be seen that the sequence is repeating. For conciseness, we use the 
        //notation √23 = [4;(1,3,1,8)], to indicate that the block (1,3,1,8) repeats 
        //indefinitely.
        //
        //The first ten continued fraction representations of (irrational) square roots are:
        //
        //√2=[1;(2)], period=1
        //√3=[1;(1,2)], period=2
        //√5=[2;(4)], period=1
        //√6=[2;(2,4)], period=2
        //√7=[2;(1,1,1,4)], period=4
        //√8=[2;(1,4)], period=2
        //√10=[3;(6)], period=1
        //√11=[3;(3,6)], period=2
        //√12= [3;(2,6)], period=2
        //√13=[3;(1,1,1,1,6)], period=5
        //
        //Exactly four continued fractions, for N ≤ 13, have an odd period.
        //
        //How many continued fractions for N ≤ 10000 have an odd period?
        int count = 0;
        for (int i = 2; i <= 10_000; i++) {
            int period = countFractionExpansion(i);
            if ((period != 0) && (period % 2 == 1)) {
                count++;
            }
            //System.out.println(String.format("%d: Period = %d", i,countFractionExpansion(i)));
        }

        return count;

    }

    private int countFractionExpansion(int rootTarget) {
        if (Utilities.isInteger(Math.sqrt(rootTarget))) {
            return 0;
        } else {
            int m = 0;
            int d = 1;
            int a = (int) Math.floor(Math.sqrt(rootTarget));
            ArrayList<String> codes = new ArrayList<>();
            String strCode = String.format("%d,%d,%d", m, d, a);
            while (!codes.contains(strCode)) {
                codes.add(strCode);
                m = d * a - m;
                d = (rootTarget - m * m) / d;
                a = ((int) Math.floor(Math.sqrt(rootTarget)) + m) / d;
                strCode = String.format("%d,%d,%d", m, d, a);
            }
            return codes.size() - codes.indexOf(strCode);
        }
    }

    public int problem65() {
        //The square root of 2 can be written as an infinite continued fraction.
        //The infinite continued fraction can be written, √2 = [1;(2)], (2) 
        //indicates that 2 repeats ad infinitum. In a similar way, √23 = [4;(1,3,1,8)].

        //What is most surprising is that the important mathematical constant,
        //e = [2; 1,2,1, 1,4,1, 1,6,1 , ... , 1,2k,1, ...].
        //The first ten terms in the sequence of convergents for e are:
        //2, 3, 8/3, 11/4, 19/7, 87/32, 106/39, 193/71, 1264/465, 1457/536, ...
        //The sum of digits in the numerator of the 10th convergent is 1+4+5+7=17.
        //Find the sum of digits in the numerator of the 100th convergent of the
        //continued fraction for e.
        final int TERM = 100;
        BigInteger numerator = BigInteger.ONE;
        BigInteger denominator = BigInteger.valueOf(expansionFactorFore(TERM));

        for (int i = TERM - 1; i >= 1; i--) {
            //add "next Factor" 
            numerator = numerator.add(denominator.multiply(BigInteger.valueOf(expansionFactorFore(i))));
            // invert the values;
            BigInteger aux = numerator;
            numerator = denominator;
            denominator = aux;
        }

        //flip a last time.
        BigInteger aux = numerator;
        numerator = denominator;
        denominator = aux;

        //System.out.println(String.format("%d/%d",numerator, denominator));
        return Utilities.sumofDigits(numerator);

    }

    private int expansionFactorFore(int n) {
        if (n == 1) {
            return 2;
        }
        if ((n % 3) == 0) {
            return (2 * n / 3);
        } else {
            return 1;
        }
    }

    public int problem66() {

        //Consider quadratic Diophantine equations of the form:
        //
        //x^2 – Dy^2 = 1
        //
        //For example, when D=13, the minimal solution in x is 649^2 – 13×180^2 = 1.
        //
        //It can be assumed that there are no solutions in positive integers when D is square.
        //
        //By finding minimal solutions in x for D = {2, 3, 5, 6, 7}, we obtain the following:
        //
        //3^2 – 2×2^2 = 1
        //2^2 – 3×1^2 = 1
        //9^2 – 5×4^2 = 1
        //5^2 – 6×2^2 = 1
        //8^2 – 7×3^2 = 1
        //
        //Hence, by considering minimal solutions in x for D ≤ 7, the largest x is obtained when D=5.
        //
        //Find the value of D ≤ 1000 in minimal solutions of x for which the largest value of x is obtained.
        //Find the value of X for D = 2;
        BigInteger largestX = BigInteger.ZERO;
        int largestD = 0;

        for (int d = 2; d <= 1000; d++) {
            if (!Utilities.isInteger(Math.sqrt(d))) {
                boolean found = false;
                int f = 0;
                BigInteger numerator = BigInteger.ONE;
                BigInteger denominator = BigInteger.ONE;
                while (!found) {
                    f++;
                    numerator = BigInteger.ONE;
                    denominator = BigInteger.valueOf(expansionFactorFromString(f, computeFractionExpansion(d)));

                    for (int i = f - 1; i >= 1; i--) {
                        //add "next Factor" 
                        numerator = numerator.add(denominator.multiply(BigInteger.valueOf(expansionFactorFromString(i, computeFractionExpansion(d)))));

                        // invert the values;
                        BigInteger aux = numerator;
                        numerator = denominator;
                        denominator = aux;
                    }
                    //flip a last time.
                    BigInteger aux = numerator;
                    numerator = denominator;
                    denominator = aux;
                    //System.out.println(String.format("For d=%d Trying x=%d, y=%d", d, numerator.longValue(), denominator.longValue()));
                    //Try 
                    found = numerator.pow(2).subtract(denominator.pow(2).multiply(BigInteger.valueOf(d))).equals(BigInteger.ONE);
                }

                //System.out.println(String.format("d=%d , x=%d, y=%d", d, numerator, denominator));
                if (numerator.compareTo(largestX) > 0) {
                    largestX = numerator;
                    largestD = d;
                }
            }

        }

        return largestD;
    }

    private String computeFractionExpansion(int rootTarget) {

        if (Utilities.isInteger(Math.sqrt(rootTarget))) {
            return String.format("[%d]", (int) Math.sqrt(rootTarget));
        } else {
            int m = 0;
            int d = 1;
            int a = (int) Math.floor(Math.sqrt(rootTarget));
            StringBuilder retVal = new StringBuilder();
            retVal.append(String.format("[%d;(", a));

            ArrayList<String> codes = new ArrayList<>();
            String strCode = String.format("%d,%d,%d", m, d, a);
            while (!codes.contains(strCode)) {
                codes.add(strCode);
                m = d * a - m;
                d = (rootTarget - m * m) / d;
                a = ((int) Math.floor(Math.sqrt(rootTarget)) + m) / d;
                strCode = String.format("%d,%d,%d", m, d, a);
                retVal.append(String.format("%d,", a));
            }
            return retVal.toString().substring(0, retVal.length() - 2 - Integer.toString(a).length()) + ")]";
        }
    }

    private int expansionFactorFromString(int n, String fractionExpansion) {
        //fractionExpansion represents how to expand the fraction.
        //E.g. √6=[2;(2,4)]

        //The value between [ and ; represents the first value
        if (n == 1) {
            if (fractionExpansion.contains(";")) {
                return Integer.valueOf(fractionExpansion.substring(fractionExpansion.indexOf('[') + 1, fractionExpansion.indexOf(';')));
            } else {
                return Integer.valueOf(fractionExpansion.substring(fractionExpansion.indexOf('[') + 1, fractionExpansion.indexOf(']')));
            }
        } else {
            if (!fractionExpansion.contains("(") || !fractionExpansion.contains(")")) {
                throw new IllegalArgumentException("Expect a value in parenthesis for n > 1");
            }
            String[] values = fractionExpansion.substring(fractionExpansion.indexOf('(') + 1, fractionExpansion.indexOf(')')).split(",");
            return Integer.valueOf(values[((n - 2) % values.length)]);
        }
    }

    public int problem67() {

        //By starting at the top of the triangle below and moving to adjacent numbers on 
        //the row below, the maximum total from top to bottom is 23.
        //
        //   3
        //  7 4
        // 2 4 6
        //8 5 9 3
        //
        //That is, 3 + 7 + 4 + 9 = 23.
        //
        //Find the maximum total from top to bottom in triangle.txt (right click and 
        //'Save Link/Target As...'), a 15K text file containing a triangle with one-hundred rows.
        //
        //NOTE: This is a much more difficult version of Problem 18. It is not possible
        //to try every route to solve this problem, as there are 299 altogether! If you 
        //could check one trillion (1012) routes every second it would take over twenty 
        //billion years to check them all. There is an efficient algorithm to solve it. 
        Path path = FileSystems.getDefault().getPath("./src", "p067_triangle.txt");
        int numbers[][] = new int[100][];

        try {

            List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
            int i = 0;
            for (String line : lines) {
                Problems001to050.initRow(numbers, i++, line);
            }

        } catch (IOException ex) {
            Logger.getLogger(Problems051to100.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Problems001to050.maxTotalRoute(numbers);

    }

    public long problem68() {
        //Consider the following "magic" 3-gon ring, filled with the numbers 1
        //to 6, and each line adding to nine.

        //4
        //  3
        // 1  2  6
        //5
        //Working clockwise, and starting from the group of three with the 
        //numerically lowest external node (4,3,2 in this example), each solution 
        //can be described uniquely. For example, the above solution can be 
        //described by the set: 4,3,2; 6,2,1; 5,1,3.
        //
        //It is possible to complete the ring with four different totals: 9, 10,
        //11, and 12. There are eight solutions in total.
        //Total	Solution Set
        //9	4,2,3; 5,3,1; 6,1,2
        //9	4,3,2; 6,2,1; 5,1,3
        //10	2,3,5; 4,5,1; 6,1,3
        //10	2,5,3; 6,3,1; 4,1,5
        //11	1,4,6; 3,6,2; 5,2,4
        //11	1,6,4; 5,4,2; 3,2,6
        //12	1,5,6; 2,6,4; 3,4,5
        //12	1,6,5; 3,5,4; 2,4,6
        //By concatenating each group it is possible to form 9-digit strings; 
        //the maximum string for a 3-gon ring is 432621513.
        //
        //Using the numbers 1 to 10, and depending on arrangements, it is 
        //possible to form 16- and 17-digit strings. What is the maximum 
        //16-digit string for a "magic" 5-gon ring?  
        //          0  
        //            6    1
        //          5     7
        //       4  9   8     2
        //            3
        //We know that since it is sorted with the "external lowest numerical value"
        //The "largest" solution must start with "6".  So we can ignore all solutions where 
        // 6 is not the first entry.  The outer values have to be 10,9,8,7 and 6
        //or the highest value won't be six.
        ArrayList<String> outerOptions = Utilities.permutations("789A");

        //The innerValues are 1 through 5. 
        ArrayList<String> innerOptions = Utilities.permutations("12345");

        //The outer values sum 10+9+8+7+6 = 40
        //The inner values sum 1+2+3+4+5=15 and are counted twice so 30 
        //Total "sum" is 70 so each value is 14.
        //This lambda will extract the values from a string.
        ToIntBiFunction<String, Integer> readVal = (strValue, position) -> {
            return Integer.valueOf(strValue.substring(position, position + 1), 11);
        };

        long longestVal = Long.MIN_VALUE;
        for (String outer : outerOptions) {
            for (String inner : innerOptions) {
                //The values are 067 - 178 - 289 - 395 - 456
                //outer options are 0 (fixed at 6) 1234
                //inner options are 56789

                //Checks the sums.
                if (((6 + readVal.applyAsInt(inner, 1) + readVal.applyAsInt(inner, 2)) == 14) //067
                        && (((readVal.applyAsInt(outer, 0) + readVal.applyAsInt(inner, 2) + readVal.applyAsInt(inner, 3)) == 14)) // 178
                        && (((readVal.applyAsInt(outer, 1) + readVal.applyAsInt(inner, 3) + readVal.applyAsInt(inner, 4)) == 14)) // 289
                        && (((readVal.applyAsInt(outer, 2) + readVal.applyAsInt(inner, 4) + readVal.applyAsInt(inner, 0)) == 14)) // 395
                        && (((readVal.applyAsInt(outer, 3) + readVal.applyAsInt(inner, 0) + readVal.applyAsInt(inner, 1)) == 14)) // 456
                        ) {
                    //Valid solution
                    long aux = Long.valueOf(
                            ("6" + inner.charAt(1) + inner.charAt(2)
                            + outer.charAt(0) + inner.charAt(2) + inner.charAt(3)
                            + outer.charAt(1) + inner.charAt(3) + inner.charAt(4)
                            + outer.charAt(2) + inner.charAt(4) + inner.charAt(0)
                            + outer.charAt(3) + inner.charAt(0) + inner.charAt(1)).replaceAll("A", "10"));
                    if (aux > longestVal) {
                        longestVal = aux;
                    }

                }

            }
        }

        //Filter out the ones on which the "5-gon" ring values match
        return longestVal;
    }

    public int problem69() {

        //Euler's Totient function, φ(n) [sometimes called the phi function], 
        //is used to determine the number of numbers less than n which are 
        //relatively prime to n. For example, as 1, 2, 4, 5, 7, and 8, are all 
        //less than nine and relatively prime to nine, φ(9)=6.
        //n 	Relatively Prime 	φ(n) 	n/φ(n)
        //2 	1                       1 	2
        //3 	1,2                     2 	1.5
        //4 	1,3                     2 	2
        //5 	1,2,3,4                 4 	1.25
        //6 	1,5                     2 	3
        //7 	1,2,3,4,5,6             6 	1.1666...
        //8 	1,3,5,7                 4 	2
        //9 	1,2,4,5,7,8             6 	1.5
        //10 	1,3,7,9                 4 	2.5
        //
        //It can be seen that n=6 produces a maximum n/φ(n) for n ≤ 10.
        //
        //Find the value of n ≤ 1,000,000 for which n/φ(n) is a maximum.
        int maxN = 0;
        double maxR = 0;

        for (int n = 2; n <= 1_000_000; n++) {
            double ratio = ((double) n) / φ(n);
            if (ratio > maxR) {

                maxR = ratio;
                maxN = n;

            }
            //System.out.println(String.format("For n=%d the φ=%d", i, φ(i)));
        }

        return maxN;
    }

    private int φ(int n) {
        //Per https://en.wikipedia.org/wiki/Euler%27s_totient_function
        // φ(n) can be computed by multiplying n * (1 - 1/a) * (1 - 1/b) * (1 - 1/c)....
        // Where a,b,c, etc... are the distinct prime factors of n
        // e.g. for 10 the prime factors are 2 and 5 so 10 * (1-1/2) * (1-1/5) = 10 * 1/2 * 4/5 = 4
        // for 20, it has the same prime factors 2^2 and 5 so 20 * (1-1/2) * (1-1/5) = 20 * 1/2 * 4/5 = 8

        if (n == 1) {
            return 1;
        } else {
            PrimeFactors pf = new PrimeFactors(n);
            return (int) Math.round(pf.factors.keySet().stream().mapToDouble(k -> {
                return (1.0 - (1.0 / k));
            }).reduce((a, b) -> a * b).getAsDouble() * (double) n);
        }
    }

    public int problem70() {

        //Euler's Totient function, φ(n) [sometimes called the phi function], is 
        //used to determine the number of positive numbers less than or equal to 
        //n which are relatively prime to n. For example, as 1, 2, 4, 5, 7, and 
        //8, are all less than nine and relatively prime to nine, φ(9)=6.
        //The number 1 is considered to be relatively prime to every positive 
        //number, so φ(1)=1.
        //
        //Interestingly, φ(87109)=79180, and it can be seen that 87109 is a 
        //permutation of 79180.
        //Find the value of n, 1 < n < 10^7, for which φ(n) is a permutation of n 
        //and the ratio n/φ(n) produces a minimum.
        double ratio = Double.MAX_VALUE;
        int result = 0;

        for (int n = 2; n <= Math.pow(10, 7); n++) {
            int phi = φ(n);
            if (isPermutationString(n, phi)) {
                double currRatio = 1.0 * n / phi;
                //System.out.println(String.format("For n=%d and φ=%d, ratio = %e", n, phi,currRatio));
                if (currRatio < ratio) {
                    ratio = currRatio;
                    result = n;
                    //System.out.println("New Min");
                }
            }
            //if (n%1000 ==0)  System.out.println(String.format("For n=%d and φ=%d", n, phi));
        }
        return result;
    }

    public long problem71() {

        //Consider the fraction, n/d, where n and d are positive integers. If 
        //n<d and HCF(n,d)=1, it is called a reduced proper fraction.
        //
        //If we list the set of reduced proper fractions for d ≤ 8 in ascending 
        //order of size, we get:
        //
        //1/8, 1/7, 1/6, 1/5, 1/4, 2/7, 1/3, 3/8, 2/5, 3/7, 1/2, 4/7, 3/5, 5/8, 
        //2/3, 5/7, 3/4, 4/5, 5/6, 6/7, 7/8
        //
        //It can be seen that 2/5 is the fraction immediately to the left of 3/7.
        //
        //By listing the set of reduced proper fractions for d ≤ 1,000,000 in 
        //ascending order of size, find the numerator of the fraction 
        //immediately to the left of 3/7.
        //for each possible denominator computer the proper fraction less than 3/7
        int closestNumerator = 0;
        int closestDenominator = 1;

        for (int denominator = 2; denominator <= 1_000_000; denominator++) {
            if (denominator != 7) {

                //Find the proper fraction lower than 3/7.
                //First the highest possible numerator that would make the fraction
                //still be below 3/7.
                int numerator = (int) Math.floor(denominator * 3 / 7);

                //Decrease num until we get a proper fraction for the given numerator.
                while ((numerator > 0) && (greatestCommonDivisor(denominator, numerator) != 1)) {
                    numerator--;
                }
                // if closestNumerator/closestDenominator < numerator/denominator
                if (closestNumerator * denominator < numerator * closestDenominator) {
                    closestNumerator = numerator;
                    closestDenominator = denominator;
                    //System.out.println(String.format("%d/%d", numerator,denominator));
                }
            }
        }

        //Compare those to see which one is largest.
        return closestNumerator;

    }

    public long problem72_bad() {

        //Consider the fraction, n/d, where n and d are positive integers. If 
        //n<d and HCF(n,d)=1, it is called a reduced proper fraction.
        //
        //If we list the set of reduced proper fractions for d ≤ 8 in ascending
        //order of size, we get:
        //
        //1/8, 1/7, 1/6, 1/5, 1/4, 2/7, 1/3, 3/8, 2/5, 3/7, 1/2, 4/7, 3/5, 5/8, 2/3, 5/7, 3/4, 4/5, 5/6, 6/7, 7/8
        //
        //It can be seen that there are 21 elements in this set.
        //
        //How many elements would be contained in the set of reduced proper 
        //fractions for d ≤ 1,000,000?
        long count = 0;
        for (int i = 1; i <= 1_000_000; i++) {
            count += countProperFractions(i);
        }
        return count;
    }

    public int countProperFractions(int denominator) {
        int counter = 0;
        for (int i = 1; i <= denominator; i++) {
            if (greatestCommonDivisor(i, denominator) == 1) {
                counter++;
            }
        }
        return counter;
    }

    public long problem72_bad2() {

        //Consider the fraction, n/d, where n and d are positive integers. If 
        //n<d and HCF(n,d)=1, it is called a reduced proper fraction.
        //
        //If we list the set of reduced proper fractions for d ≤ 8 in ascending
        //order of size, we get:
        //
        //1/8, 1/7, 1/6, 1/5, 1/4, 2/7, 1/3, 3/8, 2/5, 3/7, 1/2, 4/7, 3/5, 5/8, 2/3, 5/7, 3/4, 4/5, 5/6, 6/7, 7/8
        //
        //It can be seen that there are 21 elements in this set.
        //
        //How many elements would be contained in the set of reduced proper 
        //fractions for d ≤ 1,000,000?
        int d = 1_000_000;
        int count = 1;
        Fraction a = new Fraction(0, 1);
        Fraction b = new Fraction(1, d);
        Fraction c;

//        System.out.println (a);
//        System.out.println (b);
        while (true) {
            c = getNextFareySequence(a, b, d);
            if (!c.isProperFraction()) {
                break;
            } else {
                //System.out.println (c);
                a = b;
                b = c;
                count++;
            }
        }
        return count;
    }

    public long problem72() {

        //Consider the fraction, n/d, where n and d are positive integers. If 
        //n<d and HCF(n,d)=1, it is called a reduced proper fraction.
        //
        //If we list the set of reduced proper fractions for d ≤ 8 in ascending
        //order of size, we get:
        //
        //1/8, 1/7, 1/6, 1/5, 1/4, 2/7, 1/3, 3/8, 2/5, 3/7, 1/2, 4/7, 3/5, 5/8, 2/3, 5/7, 3/4, 4/5, 5/6, 6/7, 7/8
        //
        //It can be seen that there are 21 elements in this set.
        //
        //How many elements would be contained in the set of reduced proper 
        //fractions for d ≤ 1,000,000?
        //From https://en.wikipedia.org/wiki/Farey_sequence#Sequence_length_and_index_of_a_fraction
        //We alreasdy have, from problem 69 the formula for φ(n).
        long retVal = 1;

        for (int m = 1; m <= 1_000_000; m++) {
            retVal += φ(m);
        }

        //This returns all values including 0 and 1 so take those off.: 
        return retVal - 2;
    }

    public int problem73() {

        //Consider the fraction, n/d, where n and d are positive integers. 
        //If n<d and HCF(n,d)=1, it is called a reduced proper fraction.
        //
        //If we list the set of reduced proper fractions for d ≤ 8 in ascending 
        //order of size, we get:
        //
        //1/8, 1/7, 1/6, 1/5, 1/4, 2/7, 1/3, 3/8, 2/5, 3/7, 1/2, 4/7, 3/5, 5/8, 2/3, 5/7, 3/4, 4/5, 5/6, 6/7, 7/8
        //
        //It can be seen that there are 3 fractions between 1/3 and 1/2.
        //
        //How many fractions lie between 1/3 and 1/2 in the sorted set of reduced proper fractions for d ≤ 12,000?
        int d = 12_000;
        int count = 0;
        Fraction a = new Fraction(0, 1);
        Fraction b = new Fraction(1, d);

        Fraction lowerLimit = new Fraction(1, 3);
        Fraction upperlLimit = new Fraction(1, 2);

        Fraction c;
//      System.out.println (a);
//      System.out.println (b);

        while (true) {
            c = getNextFareySequence(a, b, d);
            if (c.equals(upperlLimit)) {
                break;
            } else {
                //System.out.println (c);
                if (c.compareTo(lowerLimit) > 0) {
                    count++;
                }
                a = b;
                b = c;
            }
        }
        return count;

    }

    public Fraction getNextFareySequence(Fraction f1, Fraction f2, int n) {
        //See http://en.wikipedia.org/wiki/Farey_sequence#Farey_neighbours for explanation of how this works

        //  if f1 = a/b and f2 = c/d 
        // The next Farey sequence with denominators <= n will be p/q
        // a/b < c/d < p/q  if they are neigbors then c/d = a+p/b+q so
        
        long numerator = (long) Math.floorDiv(n + f1.getDenominator(), f2.getDenominator()) * f2.getNumerator() - f1.getNumerator();
        long denominator = (long) Math.floorDiv(n + f1.getDenominator(), f2.getDenominator()) * f2.getDenominator() - f1.getDenominator();

        Fraction ret = new Fraction(numerator, denominator);
        ret.reduce();
        return ret;

    }

    ;

    public int problem74() {

        //The number 145 is well known for the property that the sum of the 
        //factorial of its digits is equal to 145:
        //
        //1! + 4! + 5! = 1 + 24 + 120 = 145
        //
        //Perhaps less well known is 169, in that it produces the longest 
        //chain of numbers that link back to 169; it turns out that there 
        //are only three such loops that exist:
        //
        //169 → 363601 → 1454 → 169
        //871 → 45361 → 871
        //872 → 45362 → 872
        //
        //It is not difficult to prove that EVERY starting number will 
        //eventually get stuck in a loop. For example,
        //
        //69 → 363600 → 1454 → 169 → 363601 (→ 1454)
        //78 → 45360 → 871 → 45361 (→ 871)
        //540 → 145 (→ 145)
        //
        //Starting with 69 produces a chain of five non-repeating terms, but 
        //the longest non-repeating chain with a starting number below one 
        //million is sixty terms.
        //
        //How many chains, with a starting number below one million, contain
        //exactly sixty non-repeating terms?
        int count = 0;
        for (int i = 1; i < 1_000_000; i++) {
            LinkedHashSet<Long> values = new LinkedHashSet<>();
            long num = i;
            while (!values.contains(num)) {
                values.add(num);
                num = sumOfFactorialsOfDigits(num);
            }
            if (values.size() == 60) {   //System.out.println(values.toArray()[0]);
                count++;
            }
        }
        return count;
    }

    private long sumOfFactorialsOfDigits(long n) {
        //returns the sum of the sumOfFactorialsOfDigits so e.g.
        //for 145 ->  1! + 4! + 5! = 1 + 24 + 120 = 145
        long sum = 0;
        while (n > 0) {
            sum += factorial((int) (n % 10));
            n /= 10;
        }
        return sum;
    }

    public long problem75() {

        //It turns out that 12 cm is the smallest length of wire that can be 
        //bent to form an integer sided right angle triangle in exactly one way,
        //but there are many more examples.
        //
        //12 cm: (3,4,5)
        //24 cm: (6,8,10)
        //30 cm: (5,12,13)
        //36 cm: (9,12,15)
        //40 cm: (8,15,17)
        //48 cm: (12,16,20)
        //
        //In contrast, some lengths of wire, like 20 cm, cannot be bent to form 
        //an integer sided right angle triangle, and other lengths allow more 
        //than one solution to be found; for example, using 120 cm it is possible 
        //to form exactly three different integer sided right angle triangles.
        //
        //120 cm: (30,40,50), (20,48,52), (24,45,51)
        //
        //Given that L is the length of the wire, for how many values of 
        //L ≤ 1,500,000 can exactly one integer sided right angle triangle be 
        //formed?
        //Using https://en.wikipedia.org/wiki/Pythagorean_triple
        //Variables m, n and m > n
        //a = m^2 - n^2 , b = 2mn  c = m^2 + n^2
        //Sum of sides = 2m^2 + 2mn= 2m(m+n)
        //This map will have, for each lenght, all the possible trianlges
        HashMap<Long, ArrayList<String>> triangles = new HashMap<>();

        int MAXSIZE = 1_500_000;
        int m = 2;
        int n = 1;

        while (2 * m * (m + 1) <= MAXSIZE) {
            n = 1;
            while ((n < m) && (2 * m * (m + n) <= MAXSIZE)) {
                int a = m * m - n * n;
                int b = 2 * m * n;
                if (a > b) {
                    //Sort the triangle so a < b to avoid dups
                    int aux = a;
                    a = b;
                    b = aux;
                }

                int c = m * m + n * n;
                //Scale the triangle to all values below the maxsize

                for (int k = 1; (a + b + c) * k <= MAXSIZE; k++) {
                    Long key = (long) (a + b + c) * k;
                    String thisTriangle = a * k + "," + b * k + "," + c * k;

                    ArrayList<String> sameSize;

                    //Find triangles of that size
                    if (triangles.containsKey(key)) {
                        sameSize = triangles.get(key);
                        if (!sameSize.contains(thisTriangle)) {
                            sameSize.add(thisTriangle);
                        }
                    } else {
                        sameSize = new ArrayList<>();
                        sameSize.add(thisTriangle);
                    }
                    triangles.put(key, sameSize);
                }
                n++;
            }
            m++;
        }

//        triangles.entrySet().stream().sorted((e1, e2) -> {
//            return Long.compare(e1.getKey(), e2.getKey());
//        }).filter(e1 -> e1.getValue().size() == 1).forEach(es -> {
//            System.out.println(es.getKey() + ":");
//            es.getValue().forEach(System.out::println);
//        });
        long ans = triangles.entrySet().stream().filter(e1 -> e1.getValue().size() == 1).count();
        return ans;

    }

    public int problem76() {

        //It is possible to write five as a sum in exactly six different ways:
        //
        //4 + 1
        //3 + 2
        //3 + 1 + 1
        //2 + 2 + 1
        //2 + 1 + 1 + 1
        //1 + 1 + 1 + 1 + 1
        //
        //How many different ways can one hundred be written as a sum of at 
        //least two positive integers?
        HashMap<String, Integer> cache = new HashMap<>();
        int count = countWays(100, 100, cache);
        //The answer will include one answer of "all in a single coin" which is not a sum.
        //So return count -1;
        return count - 1;
    }

    private int countWays(int maxNumber, int target, HashMap<String, Integer> cache) {
        String Key = maxNumber + "," + target;
        if (cache.containsKey(Key)) {
            return cache.get(Key);
        } else if ((maxNumber == 1) || (target == 0)) {
            cache.put(Key, 1);
            return 1;
        } else {
            int sumWays = 0;
            for (int i = 0; i * maxNumber <= target; i++) {
                sumWays += countWays(maxNumber - 1, target - (i * maxNumber), cache);
            }
            cache.put(Key, sumWays);
            return sumWays;
        }
    }

    public int problem76a() {
        //Using http://en.wikipedia.org/wiki/Partition_%28number_theory%29#Partition_function
        //By convention p(0) = 1, p(n) = 0 for n negative.
        int[] map = new int[101];
        fillPartitionMap(map);
        return map[100];

    }

    private void fillPartitionMap(int[] map) {
        //Using http://en.wikipedia.org/wiki/Partition_%28number_theory%29#Partition_function
        //By convention p(0) = 1, p(n) = 0 for n negative.
        map[0] = 1;
        for (int i = 1; i < map.length; i++) {
            int s = 1;
            int neg = 1;
            int nextP = pentagonal(s);
            while (nextP <= i) {
                map[i] += neg * map[i - nextP];
                //Try the -s value;
                nextP = pentagonal(-s);
                if (nextP <= i) {
                    map[i] += neg * map[i - nextP];

                    //Alternate sign
                    neg *= -1;
                    //Try the next pentagonal
                    s++;
                    nextP = pentagonal(s);
                }

            }
        }
    }

    public int problem77() {

        //It is possible to write ten as the sum of primes in exactly five 
        //different ways:
        //
        //7 + 3
        //5 + 5
        //5 + 3 + 2
        //3 + 3 + 2 + 2
        //2 + 2 + 2 + 2 + 2
        //
        //What is the first value which can be written as the sum of primes in
        //over five thousand different ways?
        //Lets try with the first hundred primes
        //as "possible values to try".
        int[] primeNumbers = new int[100];
        {
            int i = 0;
            int curP = 2;
            while (i < primeNumbers.length) {
                primeNumbers[i] = curP;
                curP = (int) Primes.nextPrime(curP);
                i++;
            }
        }
        HashMap<String, Integer> cache = new HashMap<>();
        int value = 10; //We know ten is too small so we can start with 11.            
        int count = 0;
        while (count < 5_000) {
            value++;
            count = countWays(primeNumbers.length - 1, value, cache, primeNumbers);
        }

        return value;

    }

    private int countWays(int currentFactorIndex, int remainder, HashMap<String, Integer> cache, int[] validFactors) {

        //First check the cache to see if we already solved this one;
        String key = validFactors[currentFactorIndex] + ":" + remainder;
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        // if not in the cache go ahead and solve it and add it to the cache
        if ((remainder == 0)) {
            //If we have nothing left we already solved with the bigger partitions and nothing smaller is needed.
            //System.out.println();
            cache.put(key, 1);
            return 1;
        } else if (currentFactorIndex == 0) {
            //If this is the smaller factor
            //Check to see if the reminder can be solved with the min value;
            if ((remainder % validFactors[0]) == 0) {
                //System.out.println(remainder/validFactors[0] + " of " + validFactors[0]);
                cache.put(key, 1);
                return 1;
            } else {
                //This means we reduced it to a number that can't be expressed with the smaller
                //denomination so there is no valid solution here.
                cache.put(key, 0);
                //System.out.println();
                return 0;
            }
        } else {
            int sumWays = 0;
            //You can do 0, 1, 2, etc of "this value" and "the rest" in "smaller values"
            for (int i = 0; i * validFactors[currentFactorIndex] <= remainder; i++) {
                //System.out.print(i + " of " + validFactors[currentFactorIndex] + ", ");
                sumWays += countWays(currentFactorIndex - 1, remainder - i * validFactors[currentFactorIndex], cache, validFactors);
            }
            cache.put(key, sumWays);
            return sumWays;
        }
    }

    public long problem78() {
        //Let p(n) represent the number of different ways in which n coins can be separated 
        //into piles. For example, five coins can be separated into piles in exactly seven 
        //different ways, so p(5)=7.
        //
        //OOOOO
        //OOOO   O
        //OOO   OO
        //OOO   O   O
        //OO   OO   O
        //OO   O   O   O
        //O   O   O   O   O
        //
        //Find the least value of n for which p(n) is divisible by one million.

        //Using http://en.wikipedia.org/wiki/Partition_%28number_theory%29#Partition_function
        //By convention p(0) = 1, p(n) = 0 for n negative.
        //Simliar to priblem 76 but we only need to save the values up to 10 million
        //to make sure that the values are divisble over 1 million.
        ArrayList<BigInteger> p = new ArrayList<>();

        p.add(BigInteger.ONE); //p(0)=1
        while (true) {
            int s = 1;
            boolean neg = false;
            int nextP = pentagonal(s);
            int i = p.size();
            BigInteger nextVal = BigInteger.ZERO;

            while (nextP <= i) {
                if (neg) {
                    nextVal = nextVal.add(p.get(i - nextP));
                } else {
                    nextVal = nextVal.subtract(p.get(i - nextP));
                }

                //Try the -s value;
                nextP = pentagonal(-s);
                if (nextP <= i) {

                    if (neg) {
                        nextVal = nextVal.add(p.get(i - nextP));
                    } else {
                        nextVal = nextVal.subtract(p.get(i - nextP));
                    }

                    //Alternate sign
                    neg = !neg;

                    //Try the next pentagonal
                    s++;
                    nextP = pentagonal(s);
                }
            }

            p.add(nextVal);
            System.out.println(String.format("For %d there are %d partions", p.size(), nextVal));
            if (nextVal.mod(BigInteger.valueOf(1_000_000)) == BigInteger.ZERO) {
                break;
            }
        }
        //Not yet solved.
        return (p.size() - 1);
    }

    public int problem79() {

        //A common security method used for online banking is to ask the user for three 
        //random characters from a attempt. For example, if the attempt was 531278, they
        //may ask for the 2nd, 3rd, and 5th characters; the expected reply would be: 317.
        //
        //The text file, keylog.txt, contains fifty successful login attempts.
        //
        //Given that the three characters are always asked for in order, analyse the file 
        //so as to determine the shortest possible secret attempt of unknown length.
        ArrayList<String> attempt = new ArrayList<>();

        attempt.add("319");
        attempt.add("680");
        attempt.add("180");
        attempt.add("690");
        attempt.add("129");
        attempt.add("620");
        attempt.add("762");
        attempt.add("689");
        attempt.add("762");
        attempt.add("318");
        attempt.add("368");
        attempt.add("710");
        attempt.add("720");
        attempt.add("710");
        attempt.add("629");
        attempt.add("168");
        attempt.add("160");
        attempt.add("689");
        attempt.add("716");
        attempt.add("731");
        attempt.add("736");
        attempt.add("729");
        attempt.add("316");
        attempt.add("729");
        attempt.add("729");
        attempt.add("710");
        attempt.add("769");
        attempt.add("290");
        attempt.add("719");
        attempt.add("680");
        attempt.add("318");
        attempt.add("389");
        attempt.add("162");
        attempt.add("289");
        attempt.add("162");
        attempt.add("718");
        attempt.add("729");
        attempt.add("319");
        attempt.add("790");
        attempt.add("680");
        attempt.add("890");
        attempt.add("362");
        attempt.add("319");
        attempt.add("760");
        attempt.add("316");
        attempt.add("729");
        attempt.add("380");
        attempt.add("319");
        attempt.add("728");
        attempt.add("716");

        //Sort and delete dups: 
        Collections.sort(attempt);
        for (int i = attempt.size() - 1; i > 0; i--) {
            if (attempt.get(i) == attempt.get(i - 1)) {
                attempt.remove(i);
            }
        }

        //Then show the cleaned up collection
        attempt.forEach(System.out::println);
        //Paper Solution:
        //**1*2*9
        //**16***0
        //**162
        //**16*8
        //**1**80
        //****289
        //****2*90
        //*316
        //*31**8
        //*31***9
        //*3*62
        //*3*6*8
        //*3***8*0
        //*3***89
        //***62**0
        //***62*9
        //***6*8*0
        //***6*89
        //***6**90
        //7*1****0
        //7*16
        //7*1**8
        //7*1***9
        //7***2**0
        //7***28
        //7***2*9
        //731
        //73*6
        //7**6***0
        //7**62
        //7**6**9
        //7*****90
        //*****890
        //Sort and remove dups
        //First pass

        int solved = 0; //Pointer to the solved problem.  Assume the first answer is the correct one

        for (int next = 0; next < attempt.size(); next++) {
            //attempt.get(next).
        };

        return 0;

    }

    public int problem80() {
        return -1;
    }

    public int problem81() {

        //In the 5 by 5 matrix below, the minimal path sum from the top left to the bottom right, by only moving to the right and down, is indicated in bold red and is equal to 2427.
        //Find the minimal path sum, in matrix.txt (right click and "Save Link/Target As..."), a 31K text file containing a 80 by 80 matrix, from the top left to the bottom right by only moving right and down.
        int numbers[][] = {
            {4445, 2697, 5115, 718, 2209, 2212, 654, 4348, 3079, 6821, 7668, 3276, 8874, 4190, 3785, 2752, 9473, 7817, 9137, 496, 7338, 3434, 7152, 4355, 4552, 7917, 7827, 2460, 2350, 691, 3514, 5880, 3145, 7633, 7199, 3783, 5066, 7487, 3285, 1084, 8985, 760, 872, 8609, 8051, 1134, 9536, 5750, 9716, 9371, 7619, 5617, 275, 9721, 2997, 2698, 1887, 8825, 6372, 3014, 2113, 7122, 7050, 6775, 5948, 2758, 1219, 3539, 348, 7989, 2735, 9862, 1263, 8089, 6401, 9462, 3168, 2758, 3748, 5870},
            {1096, 20, 1318, 7586, 5167, 2642, 1443, 5741, 7621, 7030, 5526, 4244, 2348, 4641, 9827, 2448, 6918, 5883, 3737, 300, 7116, 6531, 567, 5997, 3971, 6623, 820, 6148, 3287, 1874, 7981, 8424, 7672, 7575, 6797, 6717, 1078, 5008, 4051, 8795, 5820, 346, 1851, 6463, 2117, 6058, 3407, 8211, 117, 4822, 1317, 4377, 4434, 5925, 8341, 4800, 1175, 4173, 690, 8978, 7470, 1295, 3799, 8724, 3509, 9849, 618, 3320, 7068, 9633, 2384, 7175, 544, 6583, 1908, 9983, 481, 4187, 9353, 9377},
            {9607, 7385, 521, 6084, 1364, 8983, 7623, 1585, 6935, 8551, 2574, 8267, 4781, 3834, 2764, 2084, 2669, 4656, 9343, 7709, 2203, 9328, 8004, 6192, 5856, 3555, 2260, 5118, 6504, 1839, 9227, 1259, 9451, 1388, 7909, 5733, 6968, 8519, 9973, 1663, 5315, 7571, 3035, 4325, 4283, 2304, 6438, 3815, 9213, 9806, 9536, 196, 5542, 6907, 2475, 1159, 5820, 9075, 9470, 2179, 9248, 1828, 4592, 9167, 3713, 4640, 47, 3637, 309, 7344, 6955, 346, 378, 9044, 8635, 7466, 5036, 9515, 6385, 9230},
            {7206, 3114, 7760, 1094, 6150, 5182, 7358, 7387, 4497, 955, 101, 1478, 7777, 6966, 7010, 8417, 6453, 4955, 3496, 107, 449, 8271, 131, 2948, 6185, 784, 5937, 8001, 6104, 8282, 4165, 3642, 710, 2390, 575, 715, 3089, 6964, 4217, 192, 5949, 7006, 715, 3328, 1152, 66, 8044, 4319, 1735, 146, 4818, 5456, 6451, 4113, 1063, 4781, 6799, 602, 1504, 6245, 6550, 1417, 1343, 2363, 3785, 5448, 4545, 9371, 5420, 5068, 4613, 4882, 4241, 5043, 7873, 8042, 8434, 3939, 9256, 2187},
            {3620, 8024, 577, 9997, 7377, 7682, 1314, 1158, 6282, 6310, 1896, 2509, 5436, 1732, 9480, 706, 496, 101, 6232, 7375, 2207, 2306, 110, 6772, 3433, 2878, 8140, 5933, 8688, 1399, 2210, 7332, 6172, 6403, 7333, 4044, 2291, 1790, 2446, 7390, 8698, 5723, 3678, 7104, 1825, 2040, 140, 3982, 4905, 4160, 2200, 5041, 2512, 1488, 2268, 1175, 7588, 8321, 8078, 7312, 977, 5257, 8465, 5068, 3453, 3096, 1651, 7906, 253, 9250, 6021, 8791, 8109, 6651, 3412, 345, 4778, 5152, 4883, 7505},
            {1074, 5438, 9008, 2679, 5397, 5429, 2652, 3403, 770, 9188, 4248, 2493, 4361, 8327, 9587, 707, 9525, 5913, 93, 1899, 328, 2876, 3604, 673, 8576, 6908, 7659, 2544, 3359, 3883, 5273, 6587, 3065, 1749, 3223, 604, 9925, 6941, 2823, 8767, 7039, 3290, 3214, 1787, 7904, 3421, 7137, 9560, 8451, 2669, 9219, 6332, 1576, 5477, 6755, 8348, 4164, 4307, 2984, 4012, 6629, 1044, 2874, 6541, 4942, 903, 1404, 9125, 5160, 8836, 4345, 2581, 460, 8438, 1538, 5507, 668, 3352, 2678, 6942},
            {4295, 1176, 5596, 1521, 3061, 9868, 7037, 7129, 8933, 6659, 5947, 5063, 3653, 9447, 9245, 2679, 767, 714, 116, 8558, 163, 3927, 8779, 158, 5093, 2447, 5782, 3967, 1716, 931, 7772, 8164, 1117, 9244, 5783, 7776, 3846, 8862, 6014, 2330, 6947, 1777, 3112, 6008, 3491, 1906, 5952, 314, 4602, 8994, 5919, 9214, 3995, 5026, 7688, 6809, 5003, 3128, 2509, 7477, 110, 8971, 3982, 8539, 2980, 4689, 6343, 5411, 2992, 5270, 5247, 9260, 2269, 7474, 1042, 7162, 5206, 1232, 4556, 4757},
            {510, 3556, 5377, 1406, 5721, 4946, 2635, 7847, 4251, 8293, 8281, 6351, 4912, 287, 2870, 3380, 3948, 5322, 3840, 4738, 9563, 1906, 6298, 3234, 8959, 1562, 6297, 8835, 7861, 239, 6618, 1322, 2553, 2213, 5053, 5446, 4402, 6500, 5182, 8585, 6900, 5756, 9661, 903, 5186, 7687, 5998, 7997, 8081, 8955, 4835, 6069, 2621, 1581, 732, 9564, 1082, 1853, 5442, 1342, 520, 1737, 3703, 5321, 4793, 2776, 1508, 1647, 9101, 2499, 6891, 4336, 7012, 3329, 3212, 1442, 9993, 3988, 4930, 7706},
            {9444, 3401, 5891, 9716, 1228, 7107, 109, 3563, 2700, 6161, 5039, 4992, 2242, 8541, 7372, 2067, 1294, 3058, 1306, 320, 8881, 5756, 9326, 411, 8650, 8824, 5495, 8282, 8397, 2000, 1228, 7817, 2099, 6473, 3571, 5994, 4447, 1299, 5991, 543, 7874, 2297, 1651, 101, 2093, 3463, 9189, 6872, 6118, 872, 1008, 1779, 2805, 9084, 4048, 2123, 5877, 55, 3075, 1737, 9459, 4535, 6453, 3644, 108, 5982, 4437, 5213, 1340, 6967, 9943, 5815, 669, 8074, 1838, 6979, 9132, 9315, 715, 5048},
            {3327, 4030, 7177, 6336, 9933, 5296, 2621, 4785, 2755, 4832, 2512, 2118, 2244, 4407, 2170, 499, 7532, 9742, 5051, 7687, 970, 6924, 3527, 4694, 5145, 1306, 2165, 5940, 2425, 8910, 3513, 1909, 6983, 346, 6377, 4304, 9330, 7203, 6605, 3709, 3346, 970, 369, 9737, 5811, 4427, 9939, 3693, 8436, 5566, 1977, 3728, 2399, 3985, 8303, 2492, 5366, 9802, 9193, 7296, 1033, 5060, 9144, 2766, 1151, 7629, 5169, 5995, 58, 7619, 7565, 4208, 1713, 6279, 3209, 4908, 9224, 7409, 1325, 8540},
            {6882, 1265, 1775, 3648, 4690, 959, 5837, 4520, 5394, 1378, 9485, 1360, 4018, 578, 9174, 2932, 9890, 3696, 116, 1723, 1178, 9355, 7063, 1594, 1918, 8574, 7594, 7942, 1547, 6166, 7888, 354, 6932, 4651, 1010, 7759, 6905, 661, 7689, 6092, 9292, 3845, 9605, 8443, 443, 8275, 5163, 7720, 7265, 6356, 7779, 1798, 1754, 5225, 6661, 1180, 8024, 5666, 88, 9153, 1840, 3508, 1193, 4445, 2648, 3538, 6243, 6375, 8107, 5902, 5423, 2520, 1122, 5015, 6113, 8859, 9370, 966, 8673, 2442},
            {7338, 3423, 4723, 6533, 848, 8041, 7921, 8277, 4094, 5368, 7252, 8852, 9166, 2250, 2801, 6125, 8093, 5738, 4038, 9808, 7359, 9494, 601, 9116, 4946, 2702, 5573, 2921, 9862, 1462, 1269, 2410, 4171, 2709, 7508, 6241, 7522, 615, 2407, 8200, 4189, 5492, 5649, 7353, 2590, 5203, 4274, 710, 7329, 9063, 956, 8371, 3722, 4253, 4785, 1194, 4828, 4717, 4548, 940, 983, 2575, 4511, 2938, 1827, 2027, 2700, 1236, 841, 5760, 1680, 6260, 2373, 3851, 1841, 4968, 1172, 5179, 7175, 3509},
            {4420, 1327, 3560, 2376, 6260, 2988, 9537, 4064, 4829, 8872, 9598, 3228, 1792, 7118, 9962, 9336, 4368, 9189, 6857, 1829, 9863, 6287, 7303, 7769, 2707, 8257, 2391, 2009, 3975, 4993, 3068, 9835, 3427, 341, 8412, 2134, 4034, 8511, 6421, 3041, 9012, 2983, 7289, 100, 1355, 7904, 9186, 6920, 5856, 2008, 6545, 8331, 3655, 5011, 839, 8041, 9255, 6524, 3862, 8788, 62, 7455, 3513, 5003, 8413, 3918, 2076, 7960, 6108, 3638, 6999, 3436, 1441, 4858, 4181, 1866, 8731, 7745, 3744, 1000},
            {356, 8296, 8325, 1058, 1277, 4743, 3850, 2388, 6079, 6462, 2815, 5620, 8495, 5378, 75, 4324, 3441, 9870, 1113, 165, 1544, 1179, 2834, 562, 6176, 2313, 6836, 8839, 2986, 9454, 5199, 6888, 1927, 5866, 8760, 320, 1792, 8296, 7898, 6121, 7241, 5886, 5814, 2815, 8336, 1576, 4314, 3109, 2572, 6011, 2086, 9061, 9403, 3947, 5487, 9731, 7281, 3159, 1819, 1334, 3181, 5844, 5114, 9898, 4634, 2531, 4412, 6430, 4262, 8482, 4546, 4555, 6804, 2607, 9421, 686, 8649, 8860, 7794, 6672},
            {9870, 152, 1558, 4963, 8750, 4754, 6521, 6256, 8818, 5208, 5691, 9659, 8377, 9725, 5050, 5343, 2539, 6101, 1844, 9700, 7750, 8114, 5357, 3001, 8830, 4438, 199, 9545, 8496, 43, 2078, 327, 9397, 106, 6090, 8181, 8646, 6414, 7499, 5450, 4850, 6273, 5014, 4131, 7639, 3913, 6571, 8534, 9703, 4391, 7618, 445, 1320, 5, 1894, 6771, 7383, 9191, 4708, 9706, 6939, 7937, 8726, 9382, 5216, 3685, 2247, 9029, 8154, 1738, 9984, 2626, 9438, 4167, 6351, 5060, 29, 1218, 1239, 4785},
            {192, 5213, 8297, 8974, 4032, 6966, 5717, 1179, 6523, 4679, 9513, 1481, 3041, 5355, 9303, 9154, 1389, 8702, 6589, 7818, 6336, 3539, 5538, 3094, 6646, 6702, 6266, 2759, 4608, 4452, 617, 9406, 8064, 6379, 444, 5602, 4950, 1810, 8391, 1536, 316, 8714, 1178, 5182, 5863, 5110, 5372, 4954, 1978, 2971, 5680, 4863, 2255, 4630, 5723, 2168, 538, 1692, 1319, 7540, 440, 6430, 6266, 7712, 7385, 5702, 620, 641, 3136, 7350, 1478, 3155, 2820, 9109, 6261, 1122, 4470, 14, 8493, 2095},
            {1046, 4301, 6082, 474, 4974, 7822, 2102, 5161, 5172, 6946, 8074, 9716, 6586, 9962, 9749, 5015, 2217, 995, 5388, 4402, 7652, 6399, 6539, 1349, 8101, 3677, 1328, 9612, 7922, 2879, 231, 5887, 2655, 508, 4357, 4964, 3554, 5930, 6236, 7384, 4614, 280, 3093, 9600, 2110, 7863, 2631, 6626, 6620, 68, 1311, 7198, 7561, 1768, 5139, 1431, 221, 230, 2940, 968, 5283, 6517, 2146, 1646, 869, 9402, 7068, 8645, 7058, 1765, 9690, 4152, 2926, 9504, 2939, 7504, 6074, 2944, 6470, 7859},
            {4659, 736, 4951, 9344, 1927, 6271, 8837, 8711, 3241, 6579, 7660, 5499, 5616, 3743, 5801, 4682, 9748, 8796, 779, 1833, 4549, 8138, 4026, 775, 4170, 2432, 4174, 3741, 7540, 8017, 2833, 4027, 396, 811, 2871, 1150, 9809, 2719, 9199, 8504, 1224, 540, 2051, 3519, 7982, 7367, 2761, 308, 3358, 6505, 2050, 4836, 5090, 7864, 805, 2566, 2409, 6876, 3361, 8622, 5572, 5895, 3280, 441, 7893, 8105, 1634, 2929, 274, 3926, 7786, 6123, 8233, 9921, 2674, 5340, 1445, 203, 4585, 3837},
            {5759, 338, 7444, 7968, 7742, 3755, 1591, 4839, 1705, 650, 7061, 2461, 9230, 9391, 9373, 2413, 1213, 431, 7801, 4994, 2380, 2703, 6161, 6878, 8331, 2538, 6093, 1275, 5065, 5062, 2839, 582, 1014, 8109, 3525, 1544, 1569, 8622, 7944, 2905, 6120, 1564, 1839, 5570, 7579, 1318, 2677, 5257, 4418, 5601, 7935, 7656, 5192, 1864, 5886, 6083, 5580, 6202, 8869, 1636, 7907, 4759, 9082, 5854, 3185, 7631, 6854, 5872, 5632, 5280, 1431, 2077, 9717, 7431, 4256, 8261, 9680, 4487, 4752, 4286},
            {1571, 1428, 8599, 1230, 7772, 4221, 8523, 9049, 4042, 8726, 7567, 6736, 9033, 2104, 4879, 4967, 6334, 6716, 3994, 1269, 8995, 6539, 3610, 7667, 6560, 6065, 874, 848, 4597, 1711, 7161, 4811, 6734, 5723, 6356, 6026, 9183, 2586, 5636, 1092, 7779, 7923, 8747, 6887, 7505, 9909, 1792, 3233, 4526, 3176, 1508, 8043, 720, 5212, 6046, 4988, 709, 5277, 8256, 3642, 1391, 5803, 1468, 2145, 3970, 6301, 7767, 2359, 8487, 9771, 8785, 7520, 856, 1605, 8972, 2402, 2386, 991, 1383, 5963},
            {1822, 4824, 5957, 6511, 9868, 4113, 301, 9353, 6228, 2881, 2966, 6956, 9124, 9574, 9233, 1601, 7340, 973, 9396, 540, 4747, 8590, 9535, 3650, 7333, 7583, 4806, 3593, 2738, 8157, 5215, 8472, 2284, 9473, 3906, 6982, 5505, 6053, 7936, 6074, 7179, 6688, 1564, 1103, 6860, 5839, 2022, 8490, 910, 7551, 7805, 881, 7024, 1855, 9448, 4790, 1274, 3672, 2810, 774, 7623, 4223, 4850, 6071, 9975, 4935, 1915, 9771, 6690, 3846, 517, 463, 7624, 4511, 614, 6394, 3661, 7409, 1395, 8127},
            {8738, 3850, 9555, 3695, 4383, 2378, 87, 6256, 6740, 7682, 9546, 4255, 6105, 2000, 1851, 4073, 8957, 9022, 6547, 5189, 2487, 303, 9602, 7833, 1628, 4163, 6678, 3144, 8589, 7096, 8913, 5823, 4890, 7679, 1212, 9294, 5884, 2972, 3012, 3359, 7794, 7428, 1579, 4350, 7246, 4301, 7779, 7790, 3294, 9547, 4367, 3549, 1958, 8237, 6758, 3497, 3250, 3456, 6318, 1663, 708, 7714, 6143, 6890, 3428, 6853, 9334, 7992, 591, 6449, 9786, 1412, 8500, 722, 5468, 1371, 108, 3939, 4199, 2535},
            {7047, 4323, 1934, 5163, 4166, 461, 3544, 2767, 6554, 203, 6098, 2265, 9078, 2075, 4644, 6641, 8412, 9183, 487, 101, 7566, 5622, 1975, 5726, 2920, 5374, 7779, 5631, 3753, 3725, 2672, 3621, 4280, 1162, 5812, 345, 8173, 9785, 1525, 955, 5603, 2215, 2580, 5261, 2765, 2990, 5979, 389, 3907, 2484, 1232, 5933, 5871, 3304, 1138, 1616, 5114, 9199, 5072, 7442, 7245, 6472, 4760, 6359, 9053, 7876, 2564, 9404, 3043, 9026, 2261, 3374, 4460, 7306, 2326, 966, 828, 3274, 1712, 3446},
            {3975, 4565, 8131, 5800, 4570, 2306, 8838, 4392, 9147, 11, 3911, 7118, 9645, 4994, 2028, 6062, 5431, 2279, 8752, 2658, 7836, 994, 7316, 5336, 7185, 3289, 1898, 9689, 2331, 5737, 3403, 1124, 2679, 3241, 7748, 16, 2724, 5441, 6640, 9368, 9081, 5618, 858, 4969, 17, 2103, 6035, 8043, 7475, 2181, 939, 415, 1617, 8500, 8253, 2155, 7843, 7974, 7859, 1746, 6336, 3193, 2617, 8736, 4079, 6324, 6645, 8891, 9396, 5522, 6103, 1857, 8979, 3835, 2475, 1310, 7422, 610, 8345, 7615},
            {9248, 5397, 5686, 2988, 3446, 4359, 6634, 9141, 497, 9176, 6773, 7448, 1907, 8454, 916, 1596, 2241, 1626, 1384, 2741, 3649, 5362, 8791, 7170, 2903, 2475, 5325, 6451, 924, 3328, 522, 90, 4813, 9737, 9557, 691, 2388, 1383, 4021, 1609, 9206, 4707, 5200, 7107, 8104, 4333, 9860, 5013, 1224, 6959, 8527, 1877, 4545, 7772, 6268, 621, 4915, 9349, 5970, 706, 9583, 3071, 4127, 780, 8231, 3017, 9114, 3836, 7503, 2383, 1977, 4870, 8035, 2379, 9704, 1037, 3992, 3642, 1016, 4303},
            {5093, 138, 4639, 6609, 1146, 5565, 95, 7521, 9077, 2272, 974, 4388, 2465, 2650, 722, 4998, 3567, 3047, 921, 2736, 7855, 173, 2065, 4238, 1048, 5, 6847, 9548, 8632, 9194, 5942, 4777, 7910, 8971, 6279, 7253, 2516, 1555, 1833, 3184, 9453, 9053, 6897, 7808, 8629, 4877, 1871, 8055, 4881, 7639, 1537, 7701, 2508, 7564, 5845, 5023, 2304, 5396, 3193, 2955, 1088, 3801, 6203, 1748, 3737, 1276, 13, 4120, 7715, 8552, 3047, 2921, 106, 7508, 304, 1280, 7140, 2567, 9135, 5266},
            {6237, 4607, 7527, 9047, 522, 7371, 4883, 2540, 5867, 6366, 5301, 1570, 421, 276, 3361, 527, 6637, 4861, 2401, 7522, 5808, 9371, 5298, 2045, 5096, 5447, 7755, 5115, 7060, 8529, 4078, 1943, 1697, 1764, 5453, 7085, 960, 2405, 739, 2100, 5800, 728, 9737, 5704, 5693, 1431, 8979, 6428, 673, 7540, 6, 7773, 5857, 6823, 150, 5869, 8486, 684, 5816, 9626, 7451, 5579, 8260, 3397, 5322, 6920, 1879, 2127, 2884, 5478, 4977, 9016, 6165, 6292, 3062, 5671, 5968, 78, 4619, 4763},
            {9905, 7127, 9390, 5185, 6923, 3721, 9164, 9705, 4341, 1031, 1046, 5127, 7376, 6528, 3248, 4941, 1178, 7889, 3364, 4486, 5358, 9402, 9158, 8600, 1025, 874, 1839, 1783, 309, 9030, 1843, 845, 8398, 1433, 7118, 70, 8071, 2877, 3904, 8866, 6722, 4299, 10, 1929, 5897, 4188, 600, 1889, 3325, 2485, 6473, 4474, 7444, 6992, 4846, 6166, 4441, 2283, 2629, 4352, 7775, 1101, 2214, 9985, 215, 8270, 9750, 2740, 8361, 7103, 5930, 8664, 9690, 8302, 9267, 344, 2077, 1372, 1880, 9550},
            {5825, 8517, 7769, 2405, 8204, 1060, 3603, 7025, 478, 8334, 1997, 3692, 7433, 9101, 7294, 7498, 9415, 5452, 3850, 3508, 6857, 9213, 6807, 4412, 7310, 854, 5384, 686, 4978, 892, 8651, 3241, 2743, 3801, 3813, 8588, 6701, 4416, 6990, 6490, 3197, 6838, 6503, 114, 8343, 5844, 8646, 8694, 65, 791, 5979, 2687, 2621, 2019, 8097, 1423, 3644, 9764, 4921, 3266, 3662, 5561, 2476, 8271, 8138, 6147, 1168, 3340, 1998, 9874, 6572, 9873, 6659, 5609, 2711, 3931, 9567, 4143, 7833, 8887},
            {6223, 2099, 2700, 589, 4716, 8333, 1362, 5007, 2753, 2848, 4441, 8397, 7192, 8191, 4916, 9955, 6076, 3370, 6396, 6971, 3156, 248, 3911, 2488, 4930, 2458, 7183, 5455, 170, 6809, 6417, 3390, 1956, 7188, 577, 7526, 2203, 968, 8164, 479, 8699, 7915, 507, 6393, 4632, 1597, 7534, 3604, 618, 3280, 6061, 9793, 9238, 8347, 568, 9645, 2070, 5198, 6482, 5000, 9212, 6655, 5961, 7513, 1323, 3872, 6170, 3812, 4146, 2736, 67, 3151, 5548, 2781, 9679, 7564, 5043, 8587, 1893, 4531},
            {5826, 3690, 6724, 2121, 9308, 6986, 8106, 6659, 2142, 1642, 7170, 2877, 5757, 6494, 8026, 6571, 8387, 9961, 6043, 9758, 9607, 6450, 8631, 8334, 7359, 5256, 8523, 2225, 7487, 1977, 9555, 8048, 5763, 2414, 4948, 4265, 2427, 8978, 8088, 8841, 9208, 9601, 5810, 9398, 8866, 9138, 4176, 5875, 7212, 3272, 6759, 5678, 7649, 4922, 5422, 1343, 8197, 3154, 3600, 687, 1028, 4579, 2084, 9467, 4492, 7262, 7296, 6538, 7657, 7134, 2077, 1505, 7332, 6890, 8964, 4879, 7603, 7400, 5973, 739},
            {1861, 1613, 4879, 1884, 7334, 966, 2000, 7489, 2123, 4287, 1472, 3263, 4726, 9203, 1040, 4103, 6075, 6049, 330, 9253, 4062, 4268, 1635, 9960, 577, 1320, 3195, 9628, 1030, 4092, 4979, 6474, 6393, 2799, 6967, 8687, 7724, 7392, 9927, 2085, 3200, 6466, 8702, 265, 7646, 8665, 7986, 7266, 4574, 6587, 612, 2724, 704, 3191, 8323, 9523, 3002, 704, 5064, 3960, 8209, 2027, 2758, 8393, 4875, 4641, 9584, 6401, 7883, 7014, 768, 443, 5490, 7506, 1852, 2005, 8850, 5776, 4487, 4269},
            {4052, 6687, 4705, 7260, 6645, 6715, 3706, 5504, 8672, 2853, 1136, 8187, 8203, 4016, 871, 1809, 1366, 4952, 9294, 5339, 6872, 2645, 6083, 7874, 3056, 5218, 7485, 8796, 7401, 3348, 2103, 426, 8572, 4163, 9171, 3176, 948, 7654, 9344, 3217, 1650, 5580, 7971, 2622, 76, 2874, 880, 2034, 9929, 1546, 2659, 5811, 3754, 7096, 7436, 9694, 9960, 7415, 2164, 953, 2360, 4194, 2397, 1047, 2196, 6827, 575, 784, 2675, 8821, 6802, 7972, 5996, 6699, 2134, 7577, 2887, 1412, 4349, 4380},
            {4629, 2234, 6240, 8132, 7592, 3181, 6389, 1214, 266, 1910, 2451, 8784, 2790, 1127, 6932, 1447, 8986, 2492, 5476, 397, 889, 3027, 7641, 5083, 5776, 4022, 185, 3364, 5701, 2442, 2840, 4160, 9525, 4828, 6602, 2614, 7447, 3711, 4505, 7745, 8034, 6514, 4907, 2605, 7753, 6958, 7270, 6936, 3006, 8968, 439, 2326, 4652, 3085, 3425, 9863, 5049, 5361, 8688, 297, 7580, 8777, 7916, 6687, 8683, 7141, 306, 9569, 2384, 1500, 3346, 4601, 7329, 9040, 6097, 2727, 6314, 4501, 4974, 2829},
            {8316, 4072, 2025, 6884, 3027, 1808, 5714, 7624, 7880, 8528, 4205, 8686, 7587, 3230, 1139, 7273, 6163, 6986, 3914, 9309, 1464, 9359, 4474, 7095, 2212, 7302, 2583, 9462, 7532, 6567, 1606, 4436, 8981, 5612, 6796, 4385, 5076, 2007, 6072, 3678, 8331, 1338, 3299, 8845, 4783, 8613, 4071, 1232, 6028, 2176, 3990, 2148, 3748, 103, 9453, 538, 6745, 9110, 926, 3125, 473, 5970, 8728, 7072, 9062, 1404, 1317, 5139, 9862, 6496, 6062, 3338, 464, 1600, 2532, 1088, 8232, 7739, 8274, 3873},
            {2341, 523, 7096, 8397, 8301, 6541, 9844, 244, 4993, 2280, 7689, 4025, 4196, 5522, 7904, 6048, 2623, 9258, 2149, 9461, 6448, 8087, 7245, 1917, 8340, 7127, 8466, 5725, 6996, 3421, 5313, 512, 9164, 9837, 9794, 8369, 4185, 1488, 7210, 1524, 1016, 4620, 9435, 2478, 7765, 8035, 697, 6677, 3724, 6988, 5853, 7662, 3895, 9593, 1185, 4727, 6025, 5734, 7665, 3070, 138, 8469, 6748, 6459, 561, 7935, 8646, 2378, 462, 7755, 3115, 9690, 8877, 3946, 2728, 8793, 244, 6323, 8666, 4271},
            {6430, 2406, 8994, 56, 1267, 3826, 9443, 7079, 7579, 5232, 6691, 3435, 6718, 5698, 4144, 7028, 592, 2627, 217, 734, 6194, 8156, 9118, 58, 2640, 8069, 4127, 3285, 694, 3197, 3377, 4143, 4802, 3324, 8134, 6953, 7625, 3598, 3584, 4289, 7065, 3434, 2106, 7132, 5802, 7920, 9060, 7531, 3321, 1725, 1067, 3751, 444, 5503, 6785, 7937, 6365, 4803, 198, 6266, 8177, 1470, 6390, 1606, 2904, 7555, 9834, 8667, 2033, 1723, 5167, 1666, 8546, 8152, 473, 4475, 6451, 7947, 3062, 3281},
            {2810, 3042, 7759, 1741, 2275, 2609, 7676, 8640, 4117, 1958, 7500, 8048, 1757, 3954, 9270, 1971, 4796, 2912, 660, 5511, 3553, 1012, 5757, 4525, 6084, 7198, 8352, 5775, 7726, 8591, 7710, 9589, 3122, 4392, 6856, 5016, 749, 2285, 3356, 7482, 9956, 7348, 2599, 8944, 495, 3462, 3578, 551, 4543, 7207, 7169, 7796, 1247, 4278, 6916, 8176, 3742, 8385, 2310, 1345, 8692, 2667, 4568, 1770, 8319, 3585, 4920, 3890, 4928, 7343, 5385, 9772, 7947, 8786, 2056, 9266, 3454, 2807, 877, 2660},
            {6206, 8252, 5928, 5837, 4177, 4333, 207, 7934, 5581, 9526, 8906, 1498, 8411, 2984, 5198, 5134, 2464, 8435, 8514, 8674, 3876, 599, 5327, 826, 2152, 4084, 2433, 9327, 9697, 4800, 2728, 3608, 3849, 3861, 3498, 9943, 1407, 3991, 7191, 9110, 5666, 8434, 4704, 6545, 5944, 2357, 1163, 4995, 9619, 6754, 4200, 9682, 6654, 4862, 4744, 5953, 6632, 1054, 293, 9439, 8286, 2255, 696, 8709, 1533, 1844, 6441, 430, 1999, 6063, 9431, 7018, 8057, 2920, 6266, 6799, 356, 3597, 4024, 6665},
            {3847, 6356, 8541, 7225, 2325, 2946, 5199, 469, 5450, 7508, 2197, 9915, 8284, 7983, 6341, 3276, 3321, 16, 1321, 7608, 5015, 3362, 8491, 6968, 6818, 797, 156, 2575, 706, 9516, 5344, 5457, 9210, 5051, 8099, 1617, 9951, 7663, 8253, 9683, 2670, 1261, 4710, 1068, 8753, 4799, 1228, 2621, 3275, 6188, 4699, 1791, 9518, 8701, 5932, 4275, 6011, 9877, 2933, 4182, 6059, 2930, 6687, 6682, 9771, 654, 9437, 3169, 8596, 1827, 5471, 8909, 2352, 123, 4394, 3208, 8756, 5513, 6917, 2056},
            {5458, 8173, 3138, 3290, 4570, 4892, 3317, 4251, 9699, 7973, 1163, 1935, 5477, 6648, 9614, 5655, 9592, 975, 9118, 2194, 7322, 8248, 8413, 3462, 8560, 1907, 7810, 6650, 7355, 2939, 4973, 6894, 3933, 3784, 3200, 2419, 9234, 4747, 2208, 2207, 1945, 2899, 1407, 6145, 8023, 3484, 5688, 7686, 2737, 3828, 3704, 9004, 5190, 9740, 8643, 8650, 5358, 4426, 1522, 1707, 3613, 9887, 6956, 2447, 2762, 833, 1449, 9489, 2573, 1080, 4167, 3456, 6809, 2466, 227, 7125, 2759, 6250, 6472, 8089},
            {3266, 7025, 9756, 3914, 1265, 9116, 7723, 9788, 6805, 5493, 2092, 8688, 6592, 9173, 4431, 4028, 6007, 7131, 4446, 4815, 3648, 6701, 759, 3312, 8355, 4485, 4187, 5188, 8746, 7759, 3528, 2177, 5243, 8379, 3838, 7233, 4607, 9187, 7216, 2190, 6967, 2920, 6082, 7910, 5354, 3609, 8958, 6949, 7731, 494, 8753, 8707, 1523, 4426, 3543, 7085, 647, 6771, 9847, 646, 5049, 824, 8417, 5260, 2730, 5702, 2513, 9275, 4279, 2767, 8684, 1165, 9903, 4518, 55, 9682, 8963, 6005, 2102, 6523},
            {1998, 8731, 936, 1479, 5259, 7064, 4085, 91, 7745, 7136, 3773, 3810, 730, 8255, 2705, 2653, 9790, 6807, 2342, 355, 9344, 2668, 3690, 2028, 9679, 8102, 574, 4318, 6481, 9175, 5423, 8062, 2867, 9657, 7553, 3442, 3920, 7430, 3945, 7639, 3714, 3392, 2525, 4995, 4850, 2867, 7951, 9667, 486, 9506, 9888, 781, 8866, 1702, 3795, 90, 356, 1483, 4200, 2131, 6969, 5931, 486, 6880, 4404, 1084, 5169, 4910, 6567, 8335, 4686, 5043, 2614, 3352, 2667, 4513, 6472, 7471, 5720, 1616},
            {8878, 1613, 1716, 868, 1906, 2681, 564, 665, 5995, 2474, 7496, 3432, 9491, 9087, 8850, 8287, 669, 823, 347, 6194, 2264, 2592, 7871, 7616, 8508, 4827, 760, 2676, 4660, 4881, 7572, 3811, 9032, 939, 4384, 929, 7525, 8419, 5556, 9063, 662, 8887, 7026, 8534, 3111, 1454, 2082, 7598, 5726, 6687, 9647, 7608, 73, 3014, 5063, 670, 5461, 5631, 3367, 9796, 8475, 7908, 5073, 1565, 5008, 5295, 4457, 1274, 4788, 1728, 338, 600, 8415, 8535, 9351, 7750, 6887, 5845, 1741, 125},
            {3637, 6489, 9634, 9464, 9055, 2413, 7824, 9517, 7532, 3577, 7050, 6186, 6980, 9365, 9782, 191, 870, 2497, 8498, 2218, 2757, 5420, 6468, 586, 3320, 9230, 1034, 1393, 9886, 5072, 9391, 1178, 8464, 8042, 6869, 2075, 8275, 3601, 7715, 9470, 8786, 6475, 8373, 2159, 9237, 2066, 3264, 5000, 679, 355, 3069, 4073, 494, 2308, 5512, 4334, 9438, 8786, 8637, 9774, 1169, 1949, 6594, 6072, 4270, 9158, 7916, 5752, 6794, 9391, 6301, 5842, 3285, 2141, 3898, 8027, 4310, 8821, 7079, 1307},
            {8497, 6681, 4732, 7151, 7060, 5204, 9030, 7157, 833, 5014, 8723, 3207, 9796, 9286, 4913, 119, 5118, 7650, 9335, 809, 3675, 2597, 5144, 3945, 5090, 8384, 187, 4102, 1260, 2445, 2792, 4422, 8389, 9290, 50, 1765, 1521, 6921, 8586, 4368, 1565, 5727, 7855, 2003, 4834, 9897, 5911, 8630, 5070, 1330, 7692, 7557, 7980, 6028, 5805, 9090, 8265, 3019, 3802, 698, 9149, 5748, 1965, 9658, 4417, 5994, 5584, 8226, 2937, 272, 5743, 1278, 5698, 8736, 2595, 6475, 5342, 6596, 1149, 6920},
            {8188, 8009, 9546, 6310, 8772, 2500, 9846, 6592, 6872, 3857, 1307, 8125, 7042, 1544, 6159, 2330, 643, 4604, 7899, 6848, 371, 8067, 2062, 3200, 7295, 1857, 9505, 6936, 384, 2193, 2190, 301, 8535, 5503, 1462, 7380, 5114, 4824, 8833, 1763, 4974, 8711, 9262, 6698, 3999, 2645, 6937, 7747, 1128, 2933, 3556, 7943, 2885, 3122, 9105, 5447, 418, 2899, 5148, 3699, 9021, 9501, 597, 4084, 175, 1621, 1, 1079, 6067, 5812, 4326, 9914, 6633, 5394, 4233, 6728, 9084, 1864, 5863, 1225},
            {9935, 8793, 9117, 1825, 9542, 8246, 8437, 3331, 9128, 9675, 6086, 7075, 319, 1334, 7932, 3583, 7167, 4178, 1726, 7720, 695, 8277, 7887, 6359, 5912, 1719, 2780, 8529, 1359, 2013, 4498, 8072, 1129, 9998, 1147, 8804, 9405, 6255, 1619, 2165, 7491, 1, 8882, 7378, 3337, 503, 5758, 4109, 3577, 985, 3200, 7615, 8058, 5032, 1080, 6410, 6873, 5496, 1466, 2412, 9885, 5904, 4406, 3605, 8770, 4361, 6205, 9193, 1537, 9959, 214, 7260, 9566, 1685, 100, 4920, 7138, 9819, 5637, 976},
            {3466, 9854, 985, 1078, 7222, 8888, 5466, 5379, 3578, 4540, 6853, 8690, 3728, 6351, 7147, 3134, 6921, 9692, 857, 3307, 4998, 2172, 5783, 3931, 9417, 2541, 6299, 13, 787, 2099, 9131, 9494, 896, 8600, 1643, 8419, 7248, 2660, 2609, 8579, 91, 6663, 5506, 7675, 1947, 6165, 4286, 1972, 9645, 3805, 1663, 1456, 8853, 5705, 9889, 7489, 1107, 383, 4044, 2969, 3343, 152, 7805, 4980, 9929, 5033, 1737, 9953, 7197, 9158, 4071, 1324, 473, 9676, 3984, 9680, 3606, 8160, 7384, 5432},
            {1005, 4512, 5186, 3953, 2164, 3372, 4097, 3247, 8697, 3022, 9896, 4101, 3871, 6791, 3219, 2742, 4630, 6967, 7829, 5991, 6134, 1197, 1414, 8923, 8787, 1394, 8852, 5019, 7768, 5147, 8004, 8825, 5062, 9625, 7988, 1110, 3992, 7984, 9966, 6516, 6251, 8270, 421, 3723, 1432, 4830, 6935, 8095, 9059, 2214, 6483, 6846, 3120, 1587, 6201, 6691, 9096, 9627, 6671, 4002, 3495, 9939, 7708, 7465, 5879, 6959, 6634, 3241, 3401, 2355, 9061, 2611, 7830, 3941, 2177, 2146, 5089, 7079, 519, 6351},
            {7280, 8586, 4261, 2831, 7217, 3141, 9994, 9940, 5462, 2189, 4005, 6942, 9848, 5350, 8060, 6665, 7519, 4324, 7684, 657, 9453, 9296, 2944, 6843, 7499, 7847, 1728, 9681, 3906, 6353, 5529, 2822, 3355, 3897, 7724, 4257, 7489, 8672, 4356, 3983, 1948, 6892, 7415, 4153, 5893, 4190, 621, 1736, 4045, 9532, 7701, 3671, 1211, 1622, 3176, 4524, 9317, 7800, 5638, 6644, 6943, 5463, 3531, 2821, 1347, 5958, 3436, 1438, 2999, 994, 850, 4131, 2616, 1549, 3465, 5946, 690, 9273, 6954, 7991},
            {9517, 399, 3249, 2596, 7736, 2142, 1322, 968, 7350, 1614, 468, 3346, 3265, 7222, 6086, 1661, 5317, 2582, 7959, 4685, 2807, 2917, 1037, 5698, 1529, 3972, 8716, 2634, 3301, 3412, 8621, 743, 8001, 4734, 888, 7744, 8092, 3671, 8941, 1487, 5658, 7099, 2781, 99, 1932, 4443, 4756, 4652, 9328, 1581, 7855, 4312, 5976, 7255, 6480, 3996, 2748, 1973, 9731, 4530, 2790, 9417, 7186, 5303, 3557, 351, 7182, 9428, 1342, 9020, 7599, 1392, 8304, 2070, 9138, 7215, 2008, 9937, 1106, 7110},
            {7444, 769, 9688, 632, 1571, 6820, 8743, 4338, 337, 3366, 3073, 1946, 8219, 104, 4210, 6986, 249, 5061, 8693, 7960, 6546, 1004, 8857, 5997, 9352, 4338, 6105, 5008, 2556, 6518, 6694, 4345, 3727, 7956, 20, 3954, 8652, 4424, 9387, 2035, 8358, 5962, 5304, 5194, 8650, 8282, 1256, 1103, 2138, 6679, 1985, 3653, 2770, 2433, 4278, 615, 2863, 1715, 242, 3790, 2636, 6998, 3088, 1671, 2239, 957, 5411, 4595, 6282, 2881, 9974, 2401, 875, 7574, 2987, 4587, 3147, 6766, 9885, 2965},
            {3287, 3016, 3619, 6818, 9073, 6120, 5423, 557, 2900, 2015, 8111, 3873, 1314, 4189, 1846, 4399, 7041, 7583, 2427, 2864, 3525, 5002, 2069, 748, 1948, 6015, 2684, 438, 770, 8367, 1663, 7887, 7759, 1885, 157, 7770, 4520, 4878, 3857, 1137, 3525, 3050, 6276, 5569, 7649, 904, 4533, 7843, 2199, 5648, 7628, 9075, 9441, 3600, 7231, 2388, 5640, 9096, 958, 3058, 584, 5899, 8150, 1181, 9616, 1098, 8162, 6819, 8171, 1519, 1140, 7665, 8801, 2632, 1299, 9192, 707, 9955, 2710, 7314},
            {1772, 2963, 7578, 3541, 3095, 1488, 7026, 2634, 6015, 4633, 4370, 2762, 1650, 2174, 909, 8158, 2922, 8467, 4198, 4280, 9092, 8856, 8835, 5457, 2790, 8574, 9742, 5054, 9547, 4156, 7940, 8126, 9824, 7340, 8840, 6574, 3547, 1477, 3014, 6798, 7134, 435, 9484, 9859, 3031, 4, 1502, 4133, 1738, 1807, 4825, 463, 6343, 9701, 8506, 9822, 9555, 8688, 8168, 3467, 3234, 6318, 1787, 5591, 419, 6593, 7974, 8486, 9861, 6381, 6758, 194, 3061, 4315, 2863, 4665, 3789, 2201, 1492, 4416},
            {126, 8927, 6608, 5682, 8986, 6867, 1715, 6076, 3159, 788, 3140, 4744, 830, 9253, 5812, 5021, 7616, 8534, 1546, 9590, 1101, 9012, 9821, 8132, 7857, 4086, 1069, 7491, 2988, 1579, 2442, 4321, 2149, 7642, 6108, 250, 6086, 3167, 24, 9528, 7663, 2685, 1220, 9196, 1397, 5776, 1577, 1730, 5481, 977, 6115, 199, 6326, 2183, 3767, 5928, 5586, 7561, 663, 8649, 9688, 949, 5913, 9160, 1870, 5764, 9887, 4477, 6703, 1413, 4995, 5494, 7131, 2192, 8969, 7138, 3997, 8697, 646, 1028},
            {8074, 1731, 8245, 624, 4601, 8706, 155, 8891, 309, 2552, 8208, 8452, 2954, 3124, 3469, 4246, 3352, 1105, 4509, 8677, 9901, 4416, 8191, 9283, 5625, 7120, 2952, 8881, 7693, 830, 4580, 8228, 9459, 8611, 4499, 1179, 4988, 1394, 550, 2336, 6089, 6872, 269, 7213, 1848, 917, 6672, 4890, 656, 1478, 6536, 3165, 4743, 4990, 1176, 6211, 7207, 5284, 9730, 4738, 1549, 4986, 4942, 8645, 3698, 9429, 1439, 2175, 6549, 3058, 6513, 1574, 6988, 8333, 3406, 5245, 5431, 7140, 7085, 6407},
            {7845, 4694, 2530, 8249, 290, 5948, 5509, 1588, 5940, 4495, 5866, 5021, 4626, 3979, 3296, 7589, 4854, 1998, 5627, 3926, 8346, 6512, 9608, 1918, 7070, 4747, 4182, 2858, 2766, 4606, 6269, 4107, 8982, 8568, 9053, 4244, 5604, 102, 2756, 727, 5887, 2566, 7922, 44, 5986, 621, 1202, 374, 6988, 4130, 3627, 6744, 9443, 4568, 1398, 8679, 397, 3928, 9159, 367, 2917, 6127, 5788, 3304, 8129, 911, 2669, 1463, 9749, 264, 4478, 8940, 1109, 7309, 2462, 117, 4692, 7724, 225, 2312},
            {4164, 3637, 2000, 941, 8903, 39, 3443, 7172, 1031, 3687, 4901, 8082, 4945, 4515, 7204, 9310, 9349, 9535, 9940, 218, 1788, 9245, 2237, 1541, 5670, 6538, 6047, 5553, 9807, 8101, 1925, 8714, 445, 8332, 7309, 6830, 5786, 5736, 7306, 2710, 3034, 1838, 7969, 6318, 7912, 2584, 2080, 7437, 6705, 2254, 7428, 820, 782, 9861, 7596, 3842, 3631, 8063, 5240, 6666, 394, 4565, 7865, 4895, 9890, 6028, 6117, 4724, 9156, 4473, 4552, 602, 470, 6191, 4927, 5387, 884, 3146, 1978, 3000},
            {4258, 6880, 1696, 3582, 5793, 4923, 2119, 1155, 9056, 9698, 6603, 3768, 5514, 9927, 9609, 6166, 6566, 4536, 4985, 4934, 8076, 9062, 6741, 6163, 7399, 4562, 2337, 5600, 2919, 9012, 8459, 1308, 6072, 1225, 9306, 8818, 5886, 7243, 7365, 8792, 6007, 9256, 6699, 7171, 4230, 7002, 8720, 7839, 4533, 1671, 478, 7774, 1607, 2317, 5437, 4705, 7886, 4760, 6760, 7271, 3081, 2997, 3088, 7675, 6208, 3101, 6821, 6840, 122, 9633, 4900, 2067, 8546, 4549, 2091, 7188, 5605, 8599, 6758, 5229},
            {7854, 5243, 9155, 3556, 8812, 7047, 2202, 1541, 5993, 4600, 4760, 713, 434, 7911, 7426, 7414, 8729, 322, 803, 7960, 7563, 4908, 6285, 6291, 736, 3389, 9339, 4132, 8701, 7534, 5287, 3646, 592, 3065, 7582, 2592, 8755, 6068, 8597, 1982, 5782, 1894, 2900, 6236, 4039, 6569, 3037, 5837, 7698, 700, 7815, 2491, 7272, 5878, 3083, 6778, 6639, 3589, 5010, 8313, 2581, 6617, 5869, 8402, 6808, 2951, 2321, 5195, 497, 2190, 6187, 1342, 1316, 4453, 7740, 4154, 2959, 1781, 1482, 8256},
            {7178, 2046, 4419, 744, 8312, 5356, 6855, 8839, 319, 2962, 5662, 47, 6307, 8662, 68, 4813, 567, 2712, 9931, 1678, 3101, 8227, 6533, 4933, 6656, 92, 5846, 4780, 6256, 6361, 4323, 9985, 1231, 2175, 7178, 3034, 9744, 6155, 9165, 7787, 5836, 9318, 7860, 9644, 8941, 6480, 9443, 8188, 5928, 161, 6979, 2352, 5628, 6991, 1198, 8067, 5867, 6620, 3778, 8426, 2994, 3122, 3124, 6335, 3918, 8897, 2655, 9670, 634, 1088, 1576, 8935, 7255, 474, 8166, 7417, 9547, 2886, 5560, 3842},
            {6957, 3111, 26, 7530, 7143, 1295, 1744, 6057, 3009, 1854, 8098, 5405, 2234, 4874, 9447, 2620, 9303, 27, 7410, 969, 40, 2966, 5648, 7596, 8637, 4238, 3143, 3679, 7187, 690, 9980, 7085, 7714, 9373, 5632, 7526, 6707, 3951, 9734, 4216, 2146, 3602, 5371, 6029, 3039, 4433, 4855, 4151, 1449, 3376, 8009, 7240, 7027, 4602, 2947, 9081, 4045, 8424, 9352, 8742, 923, 2705, 4266, 3232, 2264, 6761, 363, 2651, 3383, 7770, 6730, 7856, 7340, 9679, 2158, 610, 4471, 4608, 910, 6241},
            {4417, 6756, 1013, 8797, 658, 8809, 5032, 8703, 7541, 846, 3357, 2920, 9817, 1745, 9980, 7593, 4667, 3087, 779, 3218, 6233, 5568, 4296, 2289, 2654, 7898, 5021, 9461, 5593, 8214, 9173, 4203, 2271, 7980, 2983, 5952, 9992, 8399, 3468, 1776, 3188, 9314, 1720, 6523, 2933, 621, 8685, 5483, 8986, 6163, 3444, 9539, 4320, 155, 3992, 2828, 2150, 6071, 524, 2895, 5468, 8063, 1210, 3348, 9071, 4862, 483, 9017, 4097, 6186, 9815, 3610, 5048, 1644, 1003, 9865, 9332, 2145, 1944, 2213},
            {9284, 3803, 4920, 1927, 6706, 4344, 7383, 4786, 9890, 2010, 5228, 1224, 3158, 6967, 8580, 8990, 8883, 5213, 76, 8306, 2031, 4980, 5639, 9519, 7184, 5645, 7769, 3259, 8077, 9130, 1317, 3096, 9624, 3818, 1770, 695, 2454, 947, 6029, 3474, 9938, 3527, 5696, 4760, 7724, 7738, 2848, 6442, 5767, 6845, 8323, 4131, 2859, 7595, 2500, 4815, 3660, 9130, 8580, 7016, 8231, 4391, 8369, 3444, 4069, 4021, 556, 6154, 627, 2778, 1496, 4206, 6356, 8434, 8491, 3816, 8231, 3190, 5575, 1015},
            {3787, 7572, 1788, 6803, 5641, 6844, 1961, 4811, 8535, 9914, 9999, 1450, 8857, 738, 4662, 8569, 6679, 2225, 7839, 8618, 286, 2648, 5342, 2294, 3205, 4546, 176, 8705, 3741, 6134, 8324, 8021, 7004, 5205, 7032, 6637, 9442, 5539, 5584, 4819, 5874, 5807, 8589, 6871, 9016, 983, 1758, 3786, 1519, 6241, 185, 8398, 495, 3370, 9133, 3051, 4549, 9674, 7311, 9738, 3316, 9383, 2658, 2776, 9481, 7558, 619, 3943, 3324, 6491, 4933, 153, 9738, 4623, 912, 3595, 7771, 7939, 1219, 4405},
            {2650, 3883, 4154, 5809, 315, 7756, 4430, 1788, 4451, 1631, 6461, 7230, 6017, 5751, 138, 588, 5282, 2442, 9110, 9035, 6349, 2515, 1570, 6122, 4192, 4174, 3530, 1933, 4186, 4420, 4609, 5739, 4135, 2963, 6308, 1161, 8809, 8619, 2796, 3819, 6971, 8228, 4188, 1492, 909, 8048, 2328, 6772, 8467, 7671, 9068, 2226, 7579, 6422, 7056, 8042, 3296, 2272, 3006, 2196, 7320, 3238, 3490, 3102, 37, 1293, 3212, 4767, 5041, 8773, 5794, 4456, 6174, 7279, 7054, 2835, 7053, 9088, 790, 6640},
            {3101, 1057, 7057, 3826, 6077, 1025, 2955, 1224, 1114, 6729, 5902, 4698, 6239, 7203, 9423, 1804, 4417, 6686, 1426, 6941, 8071, 1029, 4985, 9010, 6122, 6597, 1622, 1574, 3513, 1684, 7086, 5505, 3244, 411, 9638, 4150, 907, 9135, 829, 981, 1707, 5359, 8781, 9751, 5, 9131, 3973, 7159, 1340, 6955, 7514, 7993, 6964, 8198, 1933, 2797, 877, 3993, 4453, 8020, 9349, 8646, 2779, 8679, 2961, 3547, 3374, 3510, 1129, 3568, 2241, 2625, 9138, 5974, 8206, 7669, 7678, 1833, 8700, 4480},
            {4865, 9912, 8038, 8238, 782, 3095, 8199, 1127, 4501, 7280, 2112, 2487, 3626, 2790, 9432, 1475, 6312, 8277, 4827, 2218, 5806, 7132, 8752, 1468, 7471, 6386, 739, 8762, 8323, 8120, 5169, 9078, 9058, 3370, 9560, 7987, 8585, 8531, 5347, 9312, 1058, 4271, 1159, 5286, 5404, 6925, 8606, 9204, 7361, 2415, 560, 586, 4002, 2644, 1927, 2824, 768, 4409, 2942, 3345, 1002, 808, 4941, 6267, 7979, 5140, 8643, 7553, 9438, 7320, 4938, 2666, 4609, 2778, 8158, 6730, 3748, 3867, 1866, 7181},
            {171, 3771, 7134, 8927, 4778, 2913, 3326, 2004, 3089, 7853, 1378, 1729, 4777, 2706, 9578, 1360, 5693, 3036, 1851, 7248, 2403, 2273, 8536, 6501, 9216, 613, 9671, 7131, 7719, 6425, 773, 717, 8803, 160, 1114, 7554, 7197, 753, 4513, 4322, 8499, 4533, 2609, 4226, 8710, 6627, 644, 9666, 6260, 4870, 5744, 7385, 6542, 6203, 7703, 6130, 8944, 5589, 2262, 6803, 6381, 7414, 6888, 5123, 7320, 9392, 9061, 6780, 322, 8975, 7050, 5089, 1061, 2260, 3199, 1150, 1865, 5386, 9699, 6501},
            {3744, 8454, 6885, 8277, 919, 1923, 4001, 6864, 7854, 5519, 2491, 6057, 8794, 9645, 1776, 5714, 9786, 9281, 7538, 6916, 3215, 395, 2501, 9618, 4835, 8846, 9708, 2813, 3303, 1794, 8309, 7176, 2206, 1602, 1838, 236, 4593, 2245, 8993, 4017, 10, 8215, 6921, 5206, 4023, 5932, 6997, 7801, 262, 7640, 3107, 8275, 4938, 7822, 2425, 3223, 3886, 2105, 8700, 9526, 2088, 8662, 8034, 7004, 5710, 2124, 7164, 3574, 6630, 9980, 4242, 2901, 9471, 1491, 2117, 4562, 1130, 9086, 4117, 6698},
            {2810, 2280, 2331, 1170, 4554, 4071, 8387, 1215, 2274, 9848, 6738, 1604, 7281, 8805, 439, 1298, 8318, 7834, 9426, 8603, 6092, 7944, 1309, 8828, 303, 3157, 4638, 4439, 9175, 1921, 4695, 7716, 1494, 1015, 1772, 5913, 1127, 1952, 1950, 8905, 4064, 9890, 385, 9357, 7945, 5035, 7082, 5369, 4093, 6546, 5187, 5637, 2041, 8946, 1758, 7111, 6566, 1027, 1049, 5148, 7224, 7248, 296, 6169, 375, 1656, 7993, 2816, 3717, 4279, 4675, 1609, 3317, 42, 6201, 3100, 3144, 163, 9530, 4531},
            {7096, 6070, 1009, 4988, 3538, 5801, 7149, 3063, 2324, 2912, 7911, 7002, 4338, 7880, 2481, 7368, 3516, 2016, 7556, 2193, 1388, 3865, 8125, 4637, 4096, 8114, 750, 3144, 1938, 7002, 9343, 4095, 1392, 4220, 3455, 6969, 9647, 1321, 9048, 1996, 1640, 6626, 1788, 314, 9578, 6630, 2813, 6626, 4981, 9908, 7024, 4355, 3201, 3521, 3864, 3303, 464, 1923, 595, 9801, 3391, 8366, 8084, 9374, 1041, 8807, 9085, 1892, 9431, 8317, 9016, 9221, 8574, 9981, 9240, 5395, 2009, 6310, 2854, 9255},
            {8830, 3145, 2960, 9615, 8220, 6061, 3452, 2918, 6481, 9278, 2297, 3385, 6565, 7066, 7316, 5682, 107, 7646, 4466, 68, 1952, 9603, 8615, 54, 7191, 791, 6833, 2560, 693, 9733, 4168, 570, 9127, 9537, 1925, 8287, 5508, 4297, 8452, 8795, 6213, 7994, 2420, 4208, 524, 5915, 8602, 8330, 2651, 8547, 6156, 1812, 6271, 7991, 9407, 9804, 1553, 6866, 1128, 2119, 4691, 9711, 8315, 5879, 9935, 6900, 482, 682, 4126, 1041, 428, 6247, 3720, 5882, 7526, 2582, 4327, 7725, 3503, 2631},
            {2738, 9323, 721, 7434, 1453, 6294, 2957, 3786, 5722, 6019, 8685, 4386, 3066, 9057, 6860, 499, 5315, 3045, 5194, 7111, 3137, 9104, 941, 586, 3066, 755, 4177, 8819, 7040, 5309, 3583, 3897, 4428, 7788, 4721, 7249, 6559, 7324, 825, 7311, 3760, 6064, 6070, 9672, 4882, 584, 1365, 9739, 9331, 5783, 2624, 7889, 1604, 1303, 1555, 7125, 8312, 425, 8936, 3233, 7724, 1480, 403, 7440, 1784, 1754, 4721, 1569, 652, 3893, 4574, 5692, 9730, 4813, 9844, 8291, 9199, 7101, 3391, 8914},
            {6044, 2928, 9332, 3328, 8588, 447, 3830, 1176, 3523, 2705, 8365, 6136, 5442, 9049, 5526, 8575, 8869, 9031, 7280, 706, 2794, 8814, 5767, 4241, 7696, 78, 6570, 556, 5083, 1426, 4502, 3336, 9518, 2292, 1885, 3740, 3153, 9348, 9331, 8051, 2759, 5407, 9028, 7840, 9255, 831, 515, 2612, 9747, 7435, 8964, 4971, 2048, 4900, 5967, 8271, 1719, 9670, 2810, 6777, 1594, 6367, 6259, 8316, 3815, 1689, 6840, 9437, 4361, 822, 9619, 3065, 83, 6344, 7486, 8657, 8228, 9635, 6932, 4864},
            {8478, 4777, 6334, 4678, 7476, 4963, 6735, 3096, 5860, 1405, 5127, 7269, 7793, 4738, 227, 9168, 2996, 8928, 765, 733, 1276, 7677, 6258, 1528, 9558, 3329, 302, 8901, 1422, 8277, 6340, 645, 9125, 8869, 5952, 141, 8141, 1816, 9635, 4025, 4184, 3093, 83, 2344, 2747, 9352, 7966, 1206, 1126, 1826, 218, 7939, 2957, 2729, 810, 8752, 5247, 4174, 4038, 8884, 7899, 9567, 301, 5265, 5752, 7524, 4381, 1669, 3106, 8270, 6228, 6373, 754, 2547, 4240, 2313, 5514, 3022, 1040, 9738},
            {2265, 8192, 1763, 1369, 8469, 8789, 4836, 52, 1212, 6690, 5257, 8918, 6723, 6319, 378, 4039, 2421, 8555, 8184, 9577, 1432, 7139, 8078, 5452, 9628, 7579, 4161, 7490, 5159, 8559, 1011, 81, 478, 5840, 1964, 1334, 6875, 8670, 9900, 739, 1514, 8692, 522, 9316, 6955, 1345, 8132, 2277, 3193, 9773, 3923, 4177, 2183, 1236, 6747, 6575, 4874, 6003, 6409, 8187, 745, 8776, 9440, 7543, 9825, 2582, 7381, 8147, 7236, 5185, 7564, 6125, 218, 7991, 6394, 391, 7659, 7456, 5128, 5294},
            {2132, 8992, 8160, 5782, 4420, 3371, 3798, 5054, 552, 5631, 7546, 4716, 1332, 6486, 7892, 7441, 4370, 6231, 4579, 2121, 8615, 1145, 9391, 1524, 1385, 2400, 9437, 2454, 7896, 7467, 2928, 8400, 3299, 4025, 7458, 4703, 7206, 6358, 792, 6200, 725, 4275, 4136, 7390, 5984, 4502, 7929, 5085, 8176, 4600, 119, 3568, 76, 9363, 6943, 2248, 9077, 9731, 6213, 5817, 6729, 4190, 3092, 6910, 759, 2682, 8380, 1254, 9604, 3011, 9291, 5329, 9453, 9746, 2739, 6522, 3765, 5634, 1113, 5789},
            {5304, 5499, 564, 2801, 679, 2653, 1783, 3608, 7359, 7797, 3284, 796, 3222, 437, 7185, 6135, 8571, 2778, 7488, 5746, 678, 6140, 861, 7750, 803, 9859, 9918, 2425, 3734, 2698, 9005, 4864, 9818, 6743, 2475, 132, 9486, 3825, 5472, 919, 292, 4411, 7213, 7699, 6435, 9019, 6769, 1388, 802, 2124, 1345, 8493, 9487, 8558, 7061, 8777, 8833, 2427, 2238, 5409, 4957, 8503, 3171, 7622, 5779, 6145, 2417, 5873, 5563, 5693, 9574, 9491, 1937, 7384, 4563, 6842, 5432, 2751, 3406, 7981},};

        //First compute the "edges" on which there is only one path
        for (int i = 78; i >= 0; i--) {
            numbers[79][i] += numbers[79][i + 1];
            numbers[i][79] += numbers[i + 1][79];
        }

        //Now just "walk" backwards choosing the lower value
        for (int r = 78; r >= 0; r--) {
            for (int c = 78; c >= 0; c--) {
                numbers[r][c] += Math.min(numbers[r + 1][c], numbers[r][c + 1]);
            }
        }
        return numbers[0][0];
    }

    public long problem92() {

        //A number chain is created by continuously adding the square of the digits
        //in a number to form a new number until it has been seen before.
        //
        //For example,
        //
        //44 → 32 → 13 → 10 → 1 → 1
        //85 → 89 → 145 → 42 → 20 → 4 → 16 → 37 → 58 → 89
        //
        //Therefore any chain that arrives at 1 or 89 will become stuck in an 
        //endless loop. What is most amazing is that EVERY starting number will 
        //eventually arrive at 1 or 89.
        //
        //How many starting numbers below ten million will arrive at 89?
        HashSet<Integer> ones = new HashSet<>();
        ones.add(1);
        HashSet<Integer> eightnines = new HashSet<>();
        eightnines.add(89);

        for (int i = 2; i< 10_000_000 ; i++) {
            int val = i;
           
            HashSet<Integer> question = new HashSet<>();
            while (!ones.contains(val) && !eightnines.contains(val)) {
                question.add(val);
                int aux = 0;
                while (val >= 10) {
                    int digit = val % 10;
                    val /= 10;
                    aux += digit * digit;
                }
                val = val * val + aux;
            }
            if (ones.contains(val)) {
                ones.addAll(question);
            } else {
                eightnines.addAll(question);
            }
        }

        return eightnines.size();

    }
    
    public String problem97(){
        
        //The first known prime found to exceed one million digits was discovered in
        //1999, and is a Mersenne prime of the form 26972593−1; it contains exactly
        //2,098,960 digits. Subsequently other Mersenne primes, of the form 2^p−1,
        //have been found which contain more digits.
        //
        //However, in 2004 there was found a massive non-Mersenne prime which
        //contains 2,357,207 digits: 28433×2^7830457+1.
        //
        //Find the last ten digits of this prime number.
        
        String retVal = Long.toString(Utilities.truncatedPow(2, 7830457) * 28433 +1);
        
        return retVal;
                
    }

}
