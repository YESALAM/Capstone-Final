package io.github.yesalam.bhopalbrts.util;

/**
 * Created by yesalam on 20-08-2015.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import io.github.yesalam.bhopalbrts.datamodel.Stop;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The helper class of SQLlite database which copy the database from application .
 * Provides various method to dealt with database .
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private final String LOG_TAG = DataBaseHelper.class.getSimpleName() ;
    private static DataBaseHelper dataBaseHelper ;

    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/io.github.yesalam.bhopalbrts/databases/";

    private static String DB_NAME = "brtsM.db";

    private static SQLiteDatabase myDataBase;

    private final Context myContext;



    public static synchronized DataBaseHelper getInstance(Context context){
        if(dataBaseHelper == null ){
            dataBaseHelper = new DataBaseHelper(context);
        }
        return  dataBaseHelper ;
    }

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }




    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    /**
     * Return a cursor object which containg all the stops name startint with the given contstrain.
     * @param constrain
     *
     */
    public Cursor getStops(String constrain){
        String sql = "select _id,stop from allstops where stop like '"+constrain+"%' or stop like '%"+constrain+"%'" ;
        Cursor result = myDataBase.rawQuery(sql,null);

        return  result ;
    }

    public Cursor getAllStops(CharSequence constrain){
        String sql = "select _id,stop,vicinity from allstops where stop like '"+constrain+"%'" ;

        return  myDataBase.rawQuery(sql, null);
    }

    public Cursor query(CharSequence constrain){
        String[] projectionin = {"_id","stop","vicinity"};
        String selection = "stop like "+"'"+constrain+"%'"+" or stop like '%"+constrain+"%'"+"or vicinity like "+"'%"+constrain+"%'";
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables("allstops");
        return  builder.query(myDataBase, projectionin, selection, null, null, null, null);
    }

    public Cursor query(CharSequence constrain,boolean b){
        String sql = "select _id,stop , vicinity from allstops where stop like '"+constrain+"%' union all select _id,stop ,vicinity from allstops where vicinity like '%"+constrain+"%'";
        return myDataBase.rawQuery(sql, null);
    }

    public String getBuses(String stop){
        String sql = "select buses from allstops where stop =='"+stop+"'" ;
        Cursor localcursor = myDataBase.rawQuery(sql,null);
        localcursor.moveToFirst();
        return localcursor.getString(0);
    }

    //TODO improve the integer later to gain float
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
        Cursor cursor = myDataBase.rawQuery(sql,null);
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

    //TODO need to delete this method when new data set id feeded /////
    private int getId(String busstop,String bustable){
        String[] selectionarg = {busstop};
        Cursor cursor = myDataBase.rawQuery("select _id from " + bustable + " where stop_name = ?", selectionarg);
        cursor.moveToFirst();
        int result = cursor.getInt(0);
        cursor.close();
        return  result ;
    }

    public List getJunctionList(String bustable){
        List result = new ArrayList<String>();
        Cursor cursor = myDataBase.rawQuery("select stop_name from " + bustable + " where isJunction = 1", null);

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
        Cursor cursor = myDataBase.rawQuery("select stop_name,latitude,longitude,dist from "+bus+" where _id >="+startid+" and _id<= "+ stopid ,null) ;
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

    public String[] getRouteInfo(String route){
        Cursor cursor = myDataBase.rawQuery("select stop_name from "+route,null) ;
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


    public Cursor getMain(){
        Cursor cursor = myDataBase.rawQuery("select stop,latitude,longitude from allstops",null);

        return cursor;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }


    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();
        super.close();

    }

}
