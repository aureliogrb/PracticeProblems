package euler;

import java.util.function.IntToLongFunction;
import java.util.function.LongUnaryOperator;

public class Problems650to700 {
    public long problem684() {
//        Define s(n) to be the smallest number that has a digit sum of n. For example s(10) =19
//
//        Let S(k) be the sum from 1 to k of s(n).  You are given S(20) = 1074
//
//        Let f(i) be the Fibonnacci sequence defined by f0=0; f1=1 and fi=fi-2+fi-1 for all i >=2
//
//        Find sum from 1 tp 90 of S(f(i))
//
//        Give the anwser modulo 1 000 000 007

        IntToLongFunction s = n -> {
            //the answer will be (n modulo 9) followed by as many 9 as needed:
            long ans = Math.floorMod(n, 9);
            int sum = (int) ans;
            while (sum < n) {
                ans = ans * 10 + 9;
                sum += 9;
            }
            return ans;
        };

        IntToLongFunction S = n -> {
            long ans = 0;
            for (int i = 1; i <= n; i++) ans += s.applyAsLong(i);
            return ans;
        };

        int a = 0;
        int b = 1;
        int c;
        long ans = 0;

        for (int i=2; i<=90;i++) {
            //Compute the next value for fiobannaci
            c = a+b;
            ans += S.applyAsLong(c);
            ans = Math.floorMod(ans, 1_000_000_0007L);
            //get ready for next fiobonnaci
            a = b;
            b = c;
        }


        return ans;
    }
}




