package io.github.yesalam.bhopalbrts.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by yesalam on 25/10/16.
 */

public class BusDataContract {

    public static final String CONTENT_AUTHORITY = "io.github.yesalam.bhopalbrts" ;

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY) ;

    public static final String PATH_ALL_STOP = "stop" ;

    public static final String PATH_ROUTE = "route" ;

    public static final class STOPS implements BaseColumns {
        public static final Uri BASE_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ALL_STOP).build() ;

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+ "/" +CONTENT_AUTHORITY+"/" +PATH_ALL_STOP ;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE+ "/" +CONTENT_AUTHORITY+"/" +PATH_ALL_STOP ;

       public static final String TABLE_NAME = "allstops" ;

        public static final String COLUMN_STOP = "stop" ;

        public static final String COLUMN_BUSES = "buses" ;

        public static final String COLUMN_LONGITUDE = "longitude" ;

        public static final String COLUMN_LATITUDE = "latitude" ;

        public static final String COLUMN_ISJUNCTION = "isjunction" ;

        public static final String COLUMN_VICINITY = "vicinity" ;

        public static final Uri BUSES_WITH_STOP = BASE_URI.buildUpon().appendPath("buses").build() ;



        public static Uri buildStopqueryUri(String query){
            return  BASE_URI.buildUpon().appendPath(query).build() ;
        }


    }

    public static final class ROUTE implements BaseColumns{

        public static final Uri BASE_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ROUTE).build() ;

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+ "/" +CONTENT_AUTHORITY+"/" +PATH_ROUTE ;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE+ "/" +CONTENT_AUTHORITY+"/" +PATH_ROUTE ;


        public static final String[] TABLE_NAME = {"SR1","SR2","SR3","SR4","SR5","SR6","SR7","SR8","TR1","TR2","TR3","TR4","TR4A","TR4AC"} ;

        public static final String COLUMN_STOPNAME = "stop_name" ;

        public static final String COLUMN_LATITUDE = "latitude" ;

        public static final String COLUMN_LONGITUDE = "longitude" ;

        public static final String COLUMN_DIST =  "dist" ;

        public static final String COLUMN_ISJUNCTION = "isjunction" ;

        public static Uri buildJuctionRouteUri(String route){
            return BASE_URI.buildUpon().appendPath(route).build() ;
        }

        public static Uri buildIdStopnameUri(String stopName,String route){
            return BASE_URI.buildUpon().appendPath(stopName).appendPath(route).build() ;
        }

        public static Uri buildAllbwIdUri(String route,int startId,int stopId){
            return BASE_URI.buildUpon().appendPath(""+stopId).appendPath(""+startId).appendPath(route).build() ;
        }

    }


}
