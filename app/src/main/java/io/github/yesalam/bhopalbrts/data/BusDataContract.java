package io.github.yesalam.bhopalbrts.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by yesalam on 25/10/16.
 */

public class BusDataContract {

    public static final String CONTENT_AUTHORITY = "io.github.yesalam.bhopalbrts.app" ;

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY) ;

    public static final String PATH_ALL = "stop" ;

    public static final String PATH_ROUTE = "route" ;

    public static final class STOPS implements BaseColumns {

       public static final String TABLE_NAME = "allstops" ;

        public static final String COLUMN_STOP = "stop" ;

        public static final String COLUMN_BUSES = "buses" ;

        public static final String COLUMN_LONGITUDE = "longitude" ;

        public static final String COLUMN_LATITUDE = "latitude" ;

        public static final String COLUMN_ISJUNCTION = "isjunction" ;

        public static final String COLUMN_VICINITY = "vicinity" ;

    }

    public static final class ROUTE implements BaseColumns{
        public static final String[] TABLE_NAME = {"SR1","SR2","SR3","SR4","SR5","SR6","SR7","SR8","TR1","TR2","TR3","TR4","TR4A","TR4AC"} ;

        public static final String COLUMN_STOPNAME = "stop_name" ;

        public static final String COLUMN_LATITUDE = "latitude" ;

        public static final String COLUMN_LONGITUDE = "longitude" ;

        public static final String COLUMN_DIST =  "dist" ;

        public static final String COLUMN_ISJUNCTION = "isjunction" ;

    }


}
