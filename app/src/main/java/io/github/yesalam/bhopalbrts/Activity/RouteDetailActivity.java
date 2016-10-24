package io.github.yesalam.bhopalbrts.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import io.github.yesalam.bhopalbrts.Interface.ShowInfoListener;
import io.github.yesalam.bhopalbrts.R;
import io.github.yesalam.bhopalbrts.adapter.RouteActivityPagerAdapter;
import io.github.yesalam.bhopalbrts.datamodel.Stop;
import io.github.yesalam.bhopalbrts.fragments.RouteMap;
import io.github.yesalam.bhopalbrts.util.AssetDatabaseHelper;

import java.util.ArrayList;

/**
 * Created by yesalam on 22-08-2015.
 */
public class RouteDetailActivity extends AppCompatActivity implements ShowInfoListener {
    private final String LOG_TAG = RouteDetailActivity.class.getSimpleName();
    AssetDatabaseHelper dbHelper;
    static ArrayList<Stop> stoplist ;
    ViewPager viewPager;
    RouteActivityPagerAdapter pagerAdapter ;

    int flag = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);

        initialize();

        pagerAdapter = new RouteActivityPagerAdapter(getSupportFragmentManager(),stoplist) ;
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
                Log.i(LOG_TAG, "setting clicked");
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
        dbHelper =  AssetDatabaseHelper.getDatabaseHelper(this);

        Intent intent = getIntent() ;
        String from = intent.getStringExtra("ORIGIN") ;
        String to =  intent.getStringExtra("DESTINATION");
        String junction = intent.getStringExtra("JUNCTION") ;
        String bus = intent.getStringExtra("BUS");
        setTitle(bus);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if( junction == null ) {
            Log.i(LOG_TAG, "Junction is null");
            stoplist = dbHelper.getRoute(bus,from,to) ;

        } else {
            Log.i(LOG_TAG,junction) ;

            String[] buses = bus.split("\\+");
            ArrayList<Stop>[] lists = new ArrayList[buses.length] ;
            for(int i=0 ;i<buses.length;i++){
                lists[i] = dbHelper.getRoute(buses[i],from,junction);
                Log.i(LOG_TAG,i+" times runned");
                from = junction ;
                junction = to ;
            }

            //changes the Juction .
            lists[0].get(lists[0].size()-1).setIsJunction(true);

            int count = lists[1].size() ;
            float cdist =  lists[0].get(lists[0].size()-1).getDist() ;
            for(int j=1;j<count;j++){
                float dist = lists[1].get(j).getDist();

                lists[1].get(j).setDist(dist+cdist);
                lists[0].add(lists[1].get(j));
            }

            stoplist = lists[0] ;

        }

    }


    @Override
    public void showInfo(int position) {
        //TODO CardDeatail fragments have called this method .



        RouteMap mapFragment = (RouteMap)
                getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.routepager + ":1");

        if (mapFragment != null) {

            mapFragment.showInfoWindow(position);
            viewPager.setCurrentItem(1,true);
        } else {

            Log.e(LOG_TAG, "Fragment no available");

        }
    }
}
