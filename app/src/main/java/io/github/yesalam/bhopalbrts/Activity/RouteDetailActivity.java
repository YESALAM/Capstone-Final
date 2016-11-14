package io.github.yesalam.bhopalbrts.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import io.github.yesalam.bhopalbrts.Interface.ReadynessListener;
import io.github.yesalam.bhopalbrts.Interface.ShowInfoListener;
import io.github.yesalam.bhopalbrts.R;
import io.github.yesalam.bhopalbrts.adapter.RouteActivityPagerAdapter;
import io.github.yesalam.bhopalbrts.data.BusDataContract;
import io.github.yesalam.bhopalbrts.datamodel.Stop;
import io.github.yesalam.bhopalbrts.fragments.RouteDetail;
import io.github.yesalam.bhopalbrts.fragments.RouteMap;
import io.github.yesalam.bhopalbrts.data.AssetDatabaseHelper;
import io.github.yesalam.bhopalbrts.util.Calculator;
import io.github.yesalam.bhopalbrts.util.Util;

import java.util.ArrayList;

/**
 * Created by yesalam on 22-08-2015.
 */
public class RouteDetailActivity extends AppCompatActivity implements ShowInfoListener,LoaderManager.LoaderCallbacks<Cursor> ,ReadynessListener {
    private final String LOG_TAG = RouteDetailActivity.class.getSimpleName();

    ArrayList<Stop> stoplist ;
    ViewPager viewPager;
    RouteActivityPagerAdapter pagerAdapter ;
    boolean isDataset ;
    int startid ;
    int stopid ;
    String junction ;
    String from ;
    String to ;
    String bus ;
    int flag = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);
        initialize();
        pagerAdapter = new RouteActivityPagerAdapter(getSupportFragmentManager(),stoplist,this) ;
        viewPager = (ViewPager) findViewById(R.id.routepager) ;
        viewPager.setAdapter(pagerAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // Fragments handle the method on its own .

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_list :
                viewPager.setCurrentItem(0,true);
                return true;

            case R.id.action_map:
                viewPager.setCurrentItem(1,true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }


    @Override
    protected void onPause() {
        super.onPause();

    }



    /**
     * Initialize the database calculate the route and save all the stops detail in @stoplist.
     *
     */
    private void initialize(){

        Intent intent = getIntent() ;
         from = intent.getStringExtra(Util.ORIGIN) ;
         to =  intent.getStringExtra(Util.DESTINATION);
        junction = intent.getStringExtra(Util.JUNCTION) ;
         bus = intent.getStringExtra(Util.BUS);
        setTitle(bus);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        Bundle args = new Bundle() ;
        if(junction==null){
            startid = Calculator.getId(this,from, bus);
            stopid = Calculator.getId(this,to, bus);
            args.putString(Util.BUS,bus);
        }else{
            String[] buses = bus.split("\\+");
            startid = Calculator.getId(this,from, buses[0]);
            stopid = Calculator.getId(this,junction,buses[0]) ;
            args.putString(Util.BUS,buses[0]);
            bus = buses[1] ;
        }
        getSupportLoaderManager().initLoader(0, args, this);




    }


    @Override
    public void showInfo(int position) {
        RouteMap mapFragment = (RouteMap)
                getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.routepager + ":1");
        if (mapFragment != null) {
            if(mapFragment.stoplist == null ) mapFragment.setData(stoplist);
            mapFragment.showInfoWindow(position);
            viewPager.setCurrentItem(1,true);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection ;
        if(startid>stopid) selection = " _id >= "+ stopid + " and _id<= " + startid ;
        else selection = " _id >= "+ startid + " and _id<= " + stopid ;
        Uri uri = BusDataContract.ROUTE.buildAllbwIdUri(args.getString(Util.BUS),startid,stopid) ;
        String[] projection = {BusDataContract.ROUTE.COLUMN_STOPNAME,BusDataContract.ROUTE.COLUMN_LATITUDE,BusDataContract.ROUTE.COLUMN_LONGITUDE, BusDataContract.ROUTE.COLUMN_DIST} ;

        CursorLoader cl = new CursorLoader(this,uri,projection,selection,null,null);


        return cl ;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
          processCursor(data);

    }

    private void setData(){
        RouteDetail routeDetail = (RouteDetail) getSupportFragmentManager().findFragmentByTag("android:switcher:"+R.id.routepager+":0") ;
        RouteMap mapFragment = (RouteMap)
                getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.routepager + ":1");

        if(mapFragment!=null && routeDetail!=null && stoplist!= null){
            mapFragment.setData(stoplist);
            routeDetail.setData(stoplist);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void processCursor(Cursor cursor){
        if(junction == null ) {
            stoplist = getRouteDetail(cursor) ;

            setData();
        } else {
            if(flag==0){
                stoplist = getRouteDetail(cursor) ;
                startid = Calculator.getId(this,junction,bus) ;
                stopid = Calculator.getId(this,to,bus) ;
                Bundle args = new Bundle() ;
                args.putString(Util.BUS,bus);
                getSupportLoaderManager().restartLoader(0, args, this);
                flag = 1 ;
            }else{
                ArrayList<Stop> temp = new ArrayList<>() ;
                temp = getRouteDetail(cursor) ;
                stoplist.get(stoplist.size()-1).setIsJunction(true);
                int count = temp.size() ;
                float cdist =  stoplist.get(stoplist.size()-1).getDist() ;
                for(int j=1;j<count;j++){
                    float dist = temp.get(j).getDist();
                    temp.get(j).setDist(dist+cdist);
                    stoplist.add(temp.get(j));
                }

                setData();
            }

        }
    }

    public ArrayList<Stop> getRouteDetail(Cursor cursor){
        ArrayList<Stop> result = new ArrayList<>() ;
        int temp = -1 ;
        float pre = 0 ;



        if(startid>stopid) {
            //id has been swaped ;
            cursor.moveToLast();
            do {
                //get vicinity

                Stop stop = new Stop();
                stop.setStop(cursor.getString(0));
                stop.setLattitude(Double.parseDouble(cursor.getString(2)));
                stop.setLongitude(Double.parseDouble(cursor.getString(1)));
                float dist = cursor.getFloat(3);


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
            //
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

    @Override
    public void imReady() {
        setData();
    }
}
