/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package euler;

import static practice.Utilities.*;
import java.util.stream.IntStream;

/**
 *
 * @author Aurelio
 * Source of problems: https://projecteuler.net/ 
 */
public class Problems550to600 {
    public long problem551() {

        //Let a0, a1, a2, ... be an integer sequence defined by:
        //
        //    a0 = 1;
        //    for n ≥ 1, an is the sum of the digits of all preceding terms.
        //
        //The sequence starts with 1, 1, 2, 4, 8, 16, 23, 28, 38, 49, ...
        //You are given a10^6 = 3,1054,319.
        //
        //Find a10^15.
        

        long nextVal= 1;
        for (long i=2; i<=1_000_000;i++) {
            for (long j=2; j<=1_000_000;j++) {
            nextVal+=sumofDigits(nextVal);
            //System.out.println(String.format("%d->%d", i,nextVal));
        }}
        return nextVal;
    }
    
}
