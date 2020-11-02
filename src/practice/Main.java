package practice;
import programmingPraxis.Problems;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Aurelio
 */
public class Main {
    
    public static void main(String[] args) {
        // TODO code application logic here

        //Problems001to050 euler_prob0 = new Problems001to050();
        //Problems201to250 euler_prob1 = new Problems201to250();
        //Problems051to100 euler_prob2 = new Problems051to100();
        
        Problems praxis_prob1 = new Problems();
        
        Long startTime, endTime;

        System.gc();
        
        startTime= System.nanoTime();
        //System.out.println(euler_prob0.problem01());
        praxis_prob1.anagramSolver("levrea" ,3);
        endTime = System.nanoTime();

        System.out.println(String.format("Time: %0$,.3f microseconds", (endTime-startTime)/1000.0));
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
