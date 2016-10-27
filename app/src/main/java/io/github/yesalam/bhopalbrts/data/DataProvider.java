package io.github.yesalam.bhopalbrts.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by yesalam on 26/10/16.
 */

public class DataProvider extends ContentProvider {

    private static final UriMatcher uriMatcher = buildUriMatcher() ;
    private AssetDatabaseHelper databaseHelper ;

    static final int BUSES_WITH_STOP = 101 ;
    static final int ALL_FROM_ALLSTOP = 100 ;
    static final int STOPS_WITH_QUERY = 102 ;
    //static final int STOPS_AND_VICINITY_WITH_QUERY = 103 ;

    static final int JUNCTION_FROM_ROUTE = 200 ;
    static final int ID_WITH_STOP_NAME = 201 ;
    static final int ALL_BW_IDS = 202 ;
    //static final int ALL_WITH_IDS = 203 ;



    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH) ;
        final String authority = BusDataContract.CONTENT_AUTHORITY ;

        matcher.addURI(authority,BusDataContract.PATH_ALL_STOP,ALL_FROM_ALLSTOP);
        matcher.addURI(authority,BusDataContract.PATH_ALL_STOP+"/buses/",BUSES_WITH_STOP);
        matcher.addURI(authority,BusDataContract.PATH_ALL_STOP+"/*",STOPS_WITH_QUERY);
        //matcher.addURI(authority,BusDataContract.PATH_ALL_STOP+"/vicinity/*",STOPS_AND_VICINITY_WITH_QUERY);

        matcher.addURI(authority,BusDataContract.PATH_ROUTE+"/*",JUNCTION_FROM_ROUTE);
        matcher.addURI(authority,BusDataContract.PATH_ROUTE+"/*/*",ID_WITH_STOP_NAME);
        matcher.addURI(authority,BusDataContract.PATH_ROUTE+"/*/#/#", ALL_BW_IDS);
        //matcher.addURI(authority,BusDataContract.PATH_ROUTE+"/*/all/#/#",ALL_WITH_IDS);

        return matcher ;

    }



    @Override
    public boolean onCreate() {
        databaseHelper = new AssetDatabaseHelper(getContext()) ;
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor ;
        switch (uriMatcher.match(uri)) {
            case ALL_FROM_ALLSTOP :
                retCursor = databaseHelper.getReadableDatabase().query(
                        BusDataContract.STOPS.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                );
                break;
            case BUSES_WITH_STOP :
                retCursor = databaseHelper.getReadableDatabase().query(
                        BusDataContract.STOPS.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );
                break;
            case STOPS_WITH_QUERY :
                retCursor = databaseHelper.getReadableDatabase().query(
                        BusDataContract.STOPS.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                ) ;
                break;
            /*case STOPS_AND_VICINITY_WITH_QUERY :
                break;*/
            case JUNCTION_FROM_ROUTE :
                String route_name = uri.getLastPathSegment() ;
                retCursor = databaseHelper.getReadableDatabase().query(
                        route_name,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );
                break;
            case ID_WITH_STOP_NAME :

                retCursor = databaseHelper.getReadableDatabase().query(
                        uri.getLastPathSegment(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );
                break;
            case ALL_BW_IDS:
                retCursor = databaseHelper.getReadableDatabase().query(
                        uri.getLastPathSegment(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );
                break;
            /*case ALL_WITH_IDS :
                break;*/
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri) ;
        }

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = uriMatcher.match(uri);

        switch (match){
            case ALL_FROM_ALLSTOP :
                return BusDataContract.STOPS.CONTENT_TYPE ;
            case BUSES_WITH_STOP :
                return BusDataContract.STOPS.CONTENT_ITEM_TYPE ;
            case STOPS_WITH_QUERY :
                return BusDataContract.STOPS.CONTENT_TYPE ;
           /* case STOPS_AND_VICINITY_WITH_QUERY :
                return BusDataContract.STOPS.CONTENT_TYPE ;*/

            case JUNCTION_FROM_ROUTE :
                return BusDataContract.ROUTE.CONTENT_TYPE ;
            case ID_WITH_STOP_NAME :
                return BusDataContract.ROUTE.CONTENT_ITEM_TYPE ;
            case ALL_BW_IDS:
                return BusDataContract.ROUTE.CONTENT_TYPE ;
            /*case ALL_WITH_IDS :
                return BusDataContract.ROUTE.CONTENT_TYPE ;*/
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri) ;
        }


    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
