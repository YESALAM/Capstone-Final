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

    public static String ORIGIN = "ORIGIN" ;
    public static String DESTINATION = "DESTINATION" ;
    public static String JUNCTION = "JUNCTION" ;
    public static String BUS = "BUS" ;
    public static String FARE = "FARE" ;
    public static String ID = "_id" ;
    public static String PLAIN_TEXT = "text/plain" ;
    public static String TR4AC = "TR4AC" ;



}
