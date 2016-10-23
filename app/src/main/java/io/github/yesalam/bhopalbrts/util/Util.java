package io.github.yesalam.bhopalbrts.util;

/**
 * Created by yesalam on 20-08-2015.
 */
public class Util {
    public static float twoDigitPrecision(int number){
        float distanceb = ((float) number / 1000);
        String distpercise = String.format("%.2f", distanceb);
        float floatdist = Float.parseFloat(distpercise);
        return floatdist;
    }

    public static float twoDigitPrecision(float number){
        String distpercise = String.format("%.2f", number);
        float floatdist = Float.parseFloat(distpercise);
        return floatdist;
    }


}
