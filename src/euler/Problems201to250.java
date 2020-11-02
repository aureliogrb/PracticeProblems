package euler;

public class Problems201to250 {
    public long problem206 () {
//        Find the unique positive integer whose square has the form 1_2_3_4_5_6_7_8_9_0,
//        where each “_” is a single digit.

        //The largest possible number that meets this criteria would be
        //Replace all the _ with 9, the smallest, replace all the _ with zero
        //The answer has to be betwen SQRT(1020304050607080900) and SQRT (1929394959697989990)
        long aux = 0;

        for (long i=(long) Math.floor(Math.sqrt(1020304050607080900L)); i< (long) Math.ceil(Math.sqrt(1929394959697989990L)); i++) {

            aux+=i;
        }
        return aux;
    }
}
