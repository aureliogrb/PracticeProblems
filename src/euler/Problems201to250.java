package euler;

public class Problems201to250 {
    public long problem206 () {
//        Find the unique positive integer whose square has the form 1_2_3_4_5_6_7_8_9_0,
//                where each “_” is a single digit.

        //The largest number of that form would be
        //19_29_39_49_59_69_79_89_99_0 = 1,929,394,959,697,989,990
        // which is smaller than long_max 9,223,372,036,854,775,808
        //The smallest would be
        //10_20_30_40_50_60_70_80_90_0
        //Since it ends in zero the root will be a multiple of 10 so we can
        //look only into those numbers

        long floor = (long) Math.ceil(Math.sqrt(10_20_30_40_50_60_70_80_90_0L));
        //The value has to be multiple of 10 so lets round up to the nearest ten
        int lastID = Math.floorMod(floor, 10);
        if (lastID != 0) floor += (10-lastID);

        long top = (long) Math.floor(Math.sqrt(19_29_39_49_59_69_79_89_99_0L));
        for (long i = floor; i <= top; i+=10)
        {
            //All values will be 21 digits long with the last one being zero

            String num = Long.toString(i*i);
            //num = "1020304050607080900";
            boolean found = true;
            for (int d =0; d < 9; d ++) {
                if (Integer.toString(d+1).charAt(0) != num.charAt(d*2)){
                    //System.out.println("expected:" + (d+1) + "found:" + num.charAt(d*2));
                    found =false;
                    break;
                }
            }
            if (found) {
                return i;
            }
        }
        return 0;
    }




}
