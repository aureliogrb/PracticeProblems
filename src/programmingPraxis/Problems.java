/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programmingPraxis;


//import euler.Problems001to050;
//import euler.Problems051to100;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * @author Aurelio source https://programmingpraxis.com/
 */
public class Problems {

    public long squarePyramidalNumbers() {
        //Source https://programmingpraxis.com/2017/03/14/square-pyramidal-numbers/
        //Cannonballs are traditionally stacked in a pyramid with a square base. A stack 
        //with a square base of 15 cannonballs has 15 × 15 = 225 on the bottom level, 14 × 14 = 196 
        //on the level above that, and so on, a total of 1240 cannonballs.
        //Your task is to write a program to compute the number of cannonballs in a stack; 
        //use it to compute the number of cannonballs in a stack with a base of 1,000,000 cannonballs on a side

        return LongStream.rangeClosed(1, 1_000_000).map(y -> y * y).sum();

    }

    public boolean wordSets_a() {
        //Source https://programmingpraxis.com/2017/03/17/word-sets/

        //Given a list of words and a set of characters, determine which words 
        //in the list can be formed from the given characters. For instance, 
        //given the characters a, c and t, the words act and cat can be formed, 
        //but not the word stop.
        //Version a with dups: Assume that given act you can spell tact

        Set<Character> chars = Set.of('c', 'a', 't');

        Predicate<String> contains = x -> !x.chars().mapToObj(i -> (char) i).anyMatch(y -> !chars.contains(y));

        System.out.println("test" + contains.test("test"));
        System.out.println("act" + contains.test("act"));
        System.out.println("tact" + contains.test("tact"));
        System.out.println("tacts" + contains.test("tacts"));

        return true;

    }

    public String doubleSpaces(String in) {
        //https://programmingpraxis.com/2017/04/04/double-space/

        //Write a program that returns an input string with each space character in the string doubled. 
        //For instance, the string “hello hello” (with one space between the two words) is transformed 
        //to “hello  hello” (with two spaces between the two words).
        //
        //That may not come out right in all browsers; here is the transformation again, 
        //with space characters replaced by plus signs for visibility, using a constant-space font:
        //
        //    "hello+hello" => "hello++hello"
        //
        //Depending on your language, that might be an easy task, or a hard one. 
        //You may have to deal with memory allocation, or non-mutable strings, or 
        //appends that blow up to quadratic time.
        //
        //Your task is to write a program that doubles the space characters in its input. 
        //When you are finished, you are welcome to read or run a suggested solution, 
        //or to post your own solution or discuss the exercise in the comments below.
        return in.replace(" ", "  ");
    }

    public long anagramSolver(String anagram, int minSize, boolean moreWords) {

        return anagramSolver(anagram, minSize, "", moreWords);
    }

    public long anagramSolver(String anagram, int minSize, String mustInclude, boolean moreWords) {
        //For a given set of characters return the list of words in english, of minsize chars or more
        //that can be written with those letters.
        //
        //   e.g. for arey, 3 return:
        //are    aye    ayr    ear    era
        //ray    rya    rye    yea
        //yare    year


        char[] letters = anagram.toCharArray();
        //The algorithm expects the letter array to be sorted
        //More efficient to sort now outside rather than for every word
        Arrays.sort(letters);

        //Load the dictionary
        ArrayList<String> answers = new ArrayList<>();

        if (mustInclude.length() > 0)
            System.out.println("Must include: " + mustInclude);

        String wordlist;
        if (!moreWords)
            wordlist = "wordlist.txt"; //combined and cleaned from www-personal.umich.edu and corncob_lowercase
        else
            wordlist = "words_alpha.txt"; //from https://github.com/dwyl/english-words/

        //list available at:
        // http://www-personal.umich.edu/~jlawler/wordlist.html
        // "corncob_lowercase.txt"; //from http://www.mieliestronk.com/wordlist.html
        // "words_alpha.txt"; //from https://github.com/dwyl/english-words/
        Path path = FileSystems.getDefault().getPath("./src", wordlist);
        try {
            List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
            answers = lines.stream()
                    .filter(x -> x.length() >= minSize
                            && x.length() <= letters.length
                            && lettersMatch(x, letters)
                            && x.contains(mustInclude))
                    .collect(Collectors.toCollection(ArrayList::new));


        } catch (IOException ex) {
            Logger.getLogger(Problems.class.getName()).log(Level.SEVERE, null, ex);

        }

        
        Collections.sort(answers, Comparator.comparing(String::length)
                .thenComparing(String::toString));
        if (!answers.isEmpty()) {

            int i = 0;
            int len = answers.get(0).length();


            for (String answer : answers) {
                if (len != answer.length()) {
                    len = answer.length();
                    if (i != 0) {
                        System.out.println();
                        i = 0;
                    }
                }
                System.out.print(answer + "    ");
                i++;
                if (i % 5 == 0) {
                    i = 0;
                    System.out.println();
                }
            }
            System.out.println();
        }

        return answers.size();
    }

    public boolean lettersMatch(String word, char[] letterChars) {

        //Compares a word with an array of letters
        //The algorithm assumes the array of letters is already sorted.  Duplicate letters are ok.

        //Returns true if all the letters that comprise the word are included in the array
        //If a word uses the same letter more than once the array has to contain
        //that letter at least the same number of times to be a match.


        char[] wordChars = word.toCharArray();

        if (wordChars.length > letterChars.length)
            return false;
        else {
            Arrays.sort(wordChars);

            //Arrays.sort(letterChars); presume they come in sorted.
            int l = 0;
            for (int w = 0; w < wordChars.length; w++) {
                while (letterChars[l] != wordChars[w]) {
                    l++;
                    if (l == letterChars.length) {
                        //We passed the last letter in the array. Comparison is zero-based.
                        //so last l would be letterChars.length-1
                        return false;
                    }
                }
                if ((letterChars.length - l) < (wordChars.length - w))
                    //If there are more letters to match in the word
                    //than letters left in the array its not a match
                    return false;
                l++;
            }
            return true;
        }
    }

    public int BinaryConcatenation(int n) {
        //https://programmingpraxis.com/2020/07/14/13013/
        //
        //We have an interview question today:
        //
        //The concatenation of the first four integers, written in
        //binary, is 11011100; that is, 1 followed by 10 followed by 11
        //followed by 100. That concatenated number resolves to 220. A
        //similar process can convert the concatenation of the first n binary
        //numbers to a normal decimal number.
        //
        //Your task is to compute the nth binary concatenation in the manner
        //described above; report the result modulo 10^9+7, because the result
        //grows so quickly. When you are finished, you are welcome to read or run
        //a suggested solution, or to post your own solution or discuss the
        //exercise in the comments below.

        //Since we will module 10^9+7 we can compute only the last
        // 30 bits
        return 0;
    }

    public static String NumberSimplication(int input) {
        //https://codegolf.stackexchange.com/questions/189786/simplification-of-numbers

        //	How, you can’t remember the 6 or 7-digit phone number that appeared
        //on the TV screen for a second?! Using the special technique described
        //below, you will turn into a walking phonebook!
        //
        //	Obviously, the number 402 is easier to remember than the number
        //110010010, and the number 337377 is easier to remember than the number
        //957472. This means that the memorized number, on the one hand, should
        //contain as few digits as possible, and on the other hand, it is
        //desirable that the number contains as many repeating numbers as
        //possible.
        //
        //	As a criterion for the difficulty of remembering, we take the sum
        //of the number of digits in number and the number of different digits in
        //number. A memorized number can be written in another number system,
        //perhaps then it will be easier to remember. For example, the number
        //65535 in the hexadecimal notation looks like FFFF. Task
        //
        //	You need to write a program for selecting the base of the number
        //system to minimize the complexity criterion. The base of the number
        //system must be selected in the range from 2 to 36, then the numbers 0-9
        //and the English letters A-Z can be used to represent the number. Input
        //
        //	The input contains a decimal integer from 1 to 999999999. Output
        //
        //	The output must contain the base of the number system (from 2 to
        //36), minimizing the criterion of memorization complexity, and the
        //number in the selected number system, separated by one space. If
        //several bases give the same value for the criterion, then choose the
        //smallest (base)among them.

        int ret_base=0;
        String ret_value="";
        long ret_complexity = Long.MAX_VALUE;

        String cur_value="";
        long cur_complexity;

        //Once the base is larger than the input the answer won't change
        for (int i = 2;(i<=36) && (i<=input); i++)
        {
            cur_value = RebaseNumber(input,i);
            cur_complexity = cur_value.chars().distinct().count();
            cur_complexity+=cur_value.length();
            if (cur_complexity < ret_complexity) {
                ret_complexity =cur_complexity;
                ret_base = i;
                ret_value = cur_value;
            }
        }
        return Integer.toString(ret_base) + " " + ret_value;
    }


    public static String RebaseNumber(int number, int base) {
        //Writes a decimal number in any base between 2 and 36
        //Use the following as "digits"
        char[] digits = {'0','1','2','3','4','5','6','7','8','9'
                        ,'a','b','c','d','e','f','g','h','i','j'
                        ,'k','l','m','n','o','p','q','r','s','t'
                        ,'u','v','w','x','y','z'};

            if ((base<2) || (base > 36))
            throw new IllegalArgumentException("The base should be a value between 2 and 36.");

        //Will use a string builder to concatenate the result into a valid string;
        StringBuilder result = new StringBuilder();
        while (number >= base) {
            result.append(digits[(number % base)]);
            number /= base;
        }
        //Append the last one
        result.append(digits[(number)]);
        // The result is reversed so flip it for return
        return result.reverse().toString();
    }
}
