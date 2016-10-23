package io.github.yesalam.bhopalbrts.Activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import io.github.yesalam.bhopalbrts.adapter.AboutDevelopersPagerAdapter;
import io.github.yesalam.bhopalbrts.R ;

/**
 * Created by yesalam on 22-08-2015.
 */
public class AboutDevloper extends AppCompatActivity {
    private final String LOG_TAG = AboutDevloper.class.getSimpleName();
    ViewPager viewPager;
    AboutDevelopersPagerAdapter pagerAdapter ;
    AdView mAdView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_devloper);

        pagerAdapter = new AboutDevelopersPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.about_developer_pager);
        mAdView = (AdView) findViewById(R.id.adViewdevelopers);
        viewPager.setAdapter(pagerAdapter);

        int id = getIntent().getIntExtra("_id",0);
        switch (id){
            case 4:
                viewPager.setCurrentItem(0);
                break;
            case 5:
                viewPager.setCurrentItem(1);
                break;
            case 6:
                viewPager.setCurrentItem(2);
                break;
        }

        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_about_devloper, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
