package io.github.yesalam.bhopalbrts.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import io.github.yesalam.bhopalbrts.datamodel.Stop;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yesalam on 22-08-2015.
 */
public class AssetDatabaseHelper extends SQLiteAssetHelper {
    private static final String DB_NAME = "brtsM.db" ;
    private static final int DB_VERSION = 2 ;
    public static AssetDatabaseHelper insatance = null ;

    private AssetDatabaseHelper(Context context) {
        super(context,DB_NAME, null,DB_VERSION );
    }

    public static AssetDatabaseHelper getDatabaseHelper(Context context){
        if(insatance == null ){
            insatance = new AssetDatabaseHelper(context);
        }
        return insatance ;
    }

    public String getBuses(String stop){
        String sql = "select buses from allstops where stop =='"+stop+"'" ;
        Cursor localcursor = getReadableDatabase().rawQuery(sql,null);
        localcursor.moveToFirst();
        return localcursor.getString(0);
    }

    public Cursor getMain(){
        Cursor cursor = getReadableDatabase().rawQuery("select stop,latitude,longitude from allstops", null);

        return cursor;
    }

    public Cursor getStops(String constrain){
        String sql = "select _id,stop from allstops where stop like '"+constrain+"%' or stop like '%"+constrain+"%'" ;
        Cursor result = getReadableDatabase().rawQuery(sql, null);

        return  result ;
    }

    public Cursor getAllStops(CharSequence constrain){
        String sql = "select _id,stop,vicinity from allstops where stop like '"+constrain+"%'" ;

        return  getReadableDatabase().rawQuery(sql, null);
    }

    public Cursor query(CharSequence constrain){
        String[] projectionin = {"_id","stop","vicinity"};
        String selection = "stop like "+"'"+constrain+"%'"+" or stop like '%"+constrain+"%'"+"or vicinity like "+"'%"+constrain+"%'";
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables("allstops");
        return  builder.query(getReadableDatabase(), projectionin, selection, null, null, null, null);
    }

    public Cursor query(CharSequence constrain,boolean b){
        String sql = "select _id,stop , vicinity from allstops where stop like '"+constrain+"%' union all select _id,stop ,vicinity from allstops where vicinity like '%"+constrain+"%'";
        return getReadableDatabase().rawQuery(sql, null);
    }




    public String[] getRouteInfo(String route){
        Cursor cursor = getReadableDatabase().rawQuery("select stop_name from "+route,null) ;
        int count = 0 ;
        String origin ;
        cursor.moveToFirst();
        origin = cursor.getString(0);
        count++ ;
        String destination = null;
        while(cursor.moveToNext()){
            count++ ;
            destination = cursor.getString(0);
        }
        String[] result = new String[3];
        result[0] = route ;
        result[1] = origin+" To "+destination ;
        result[2] = count+" Stops" ;
        cursor.close();
        return result;
    }

    public float getDistance(String from,String to,String bus){
        float dist = 0;
        int fromid = getId(from, bus);
        int toid = getId(to,bus);

        if(fromid>toid){
            int temp = fromid;
            fromid= toid;
            toid = temp;
        }
        //Log.e("Database",from+"  "+to+"  "+bus);
        String sql = "select dist from "+bus+" where _id = "+fromid+" or _id = "+toid ;
        // Log.e("Database",sql);
        Cursor cursor = getReadableDatabase().rawQuery(sql, null);
        cursor.moveToFirst();
        float first_distant = cursor.getFloat(0);
        cursor.moveToNext();
        float second_distance = cursor.getFloat(0);
        if(first_distant<second_distance){
            dist = second_distance - first_distant ;
        } else {
            dist = first_distant - second_distance ;
        }
        cursor.close();
        return dist;
    }

    private int getId(String busstop,String bustable){
        String[] selectionarg = {busstop};
        Cursor cursor = getReadableDatabase().rawQuery("select _id from " + bustable + " where stop_name = ?", selectionarg);
        cursor.moveToFirst();
        int result = cursor.getInt(0);
        cursor.close();
        return  result ;
    }

    public List getJunctionList(String bustable){
        List result = new ArrayList<String>();
        Cursor cursor = getReadableDatabase().rawQuery("select stop_name from " + bustable + " where isJunction = 1", null);

        while(cursor.moveToNext()){
            result.add(cursor.getString(0));
        }
        cursor.close();
        return result;

    }


    public ArrayList<Stop> getRoute(String bus,String from ,String to){
        ArrayList<Stop> result = new ArrayList<>() ;
        int startid = getId(from, bus);
        int stopid = getId(to,bus);
        int temp = -1 ;
        float pre = 0 ;
        if(startid>stopid) {
            temp = startid ;
            startid = stopid ;
            stopid = temp ;
        }
        Cursor cursor = getReadableDatabase().rawQuery("select stop_name,latitude,longitude,dist from " + bus + " where _id >=" + startid + " and _id<= " + stopid, null) ;
        if(temp != -1) {
            //id has been swaped ;
            cursor.moveToLast();
            do {
                //get vicinity

                Stop stop = new Stop();
                stop.setStop(cursor.getString(0));
                stop.setLattitude(Double.parseDouble(cursor.getString(2)));
                stop.setLongitude(Double.parseDouble(cursor.getString(1)));
                float dist = cursor.getFloat(3);

                //float floatdist = Util.twoDigitPrecision(dist);
                //conversion complete
                //TODO chane equation after database updation .

                if(pre == 0)  {
                    stop.setDist(0);
                    pre = dist ;
                } else {
                    stop.setDist(pre-dist);
                }
                result.add(stop);




            } while(cursor.moveToPrevious()) ;
        } else {

            temp = 0 ;
            while (cursor.moveToNext()) {
                Stop stop = new Stop();
                stop.setStop(cursor.getString(0));
                stop.setLattitude(Double.parseDouble((cursor.getString(2))));
                stop.setLongitude(Double.parseDouble(cursor.getString(1)));
                float dist = cursor.getFloat(3);

                if(temp == 0){
                    temp = 1 ;
                    stop.setDist(0);
                    pre = dist ;
                } else {
                    stop.setDist(dist-pre);
                }//TODO chane equation after database updation .

                result.add(stop);

            }
        }
        cursor.close();
        return  result ;
    }

}
