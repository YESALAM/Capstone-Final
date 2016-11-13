package io.github.yesalam.bhopalbrts.Activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import io.github.yesalam.bhopalbrts.R;
import io.github.yesalam.bhopalbrts.adapter.TabPagerAdapter;

public class Bhopal_BRTS extends AppCompatActivity {

    private final String LOG_TAG = Bhopal_BRTS.class.getSimpleName();
    private final String PREFERENCE_NAME = "apppreference" ;
    ViewPager viewPager ;
    TabPagerAdapter tabPagerAdapter;
    TabLayout tabLayout;
    AdView mAdView ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bhopal_brts);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        mAdView = (AdView) findViewById(R.id.adViewMain) ;

        // Create the adapter that will return a fragment for each of the four
        // primary sections of the activity.
        tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(),this);

        viewPager.setAdapter(tabPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);


        AdRequest adRequest = new AdRequest.Builder().build() ;
        mAdView.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bhopal__brt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_info) {
            Intent intent = new Intent(this,AboutDevloper.class);
            startActivity(intent);
            return true;
        } else if(id== R.id.action_share){
            Intent localIntent1 = new Intent();
            localIntent1.setAction("android.intent.action.SEND");
            localIntent1.putExtra("android.intent.extra.TEXT", "Bhopal BRTS is a must have app for MYBUS commuter and Bhopal tourist .\nJust click on the link given below:\nhttps://play.google.com/store/apps/details?id=com.seatech.bhopalbrts\n Don't forget to share with your mates..");
            localIntent1.setType("text/plain");
            startActivity(Intent.createChooser(localIntent1, "Share via:"));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }



    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void finish() {
        super.finish();
    }
}
