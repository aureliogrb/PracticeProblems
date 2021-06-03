package practice;

import euler.Problems201to250;
import euler.Problems650to700;
import programmingPraxis.Problems;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author Aurelio
 */
public class Main {

    public static void main(String[] args) {
        // TODO code application logic here

        //var euler_prob = new Problems001to050();
        //var euler_prob = new Problems201to250();
        //var euler_prob = new Problems051to100();
        //var euler_prob = new Problems650to700();

        Problems praxis_prob1 = new Problems();

        Long startTime, endTime;



        System.gc();

        startTime = new Long(System.nanoTime());

        /* System.out.println(euler_prob.problem684()); */
        System.out.println(praxis_prob1.anagramSolver("crazily" +
                        ""
                , 3
                , ""
                , false));
        endTime = System.nanoTime();



        System.out.printf("Time: %1$,.3f microseconds", (endTime - startTime) / 1000.0);
        System.gc();
        
        /*
        startTime= System.nanoTime();
        System.out.println(euler_prob1.problem48a());
        endTime = System.nanoTime();
        System.out.println(String.format("Time: %0$,.3f microseconds", (endTime-startTime)/1000.0));
        System.gc();
        
        */



//        startTime= System.nanoTime();
//        System.out.println(euler_prob1.problem48a());
//        endTime = System.nanoTime();
//        System.out.println(String.format("Time: %0$,.3f microseconds", (endTime-startTime)/1000.0));
//        System.gc();

    }
    
}
