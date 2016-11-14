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
import io.github.yesalam.bhopalbrts.util.Util;

import static android.content.Intent.ACTION_SEND;
import static android.content.Intent.EXTRA_TEXT;

public class Bhopal_BRTS extends AppCompatActivity {

    private final String LOG_TAG = Bhopal_BRTS.class.getSimpleName();

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
            localIntent1.setAction(ACTION_SEND);
            localIntent1.putExtra(EXTRA_TEXT,getResources().getString(R.string.send_text));
            localIntent1.setType(Util.PLAIN_TEXT);
            startActivity(Intent.createChooser(localIntent1,getString(R.string.share_via)));
        }

        return super.onOptionsItemSelected(item);
    }


}
