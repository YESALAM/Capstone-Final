package io.github.yesalam.bhopalbrts.adapter;

import android.content.ContentProvider;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import io.github.yesalam.bhopalbrts.R;
import io.github.yesalam.bhopalbrts.fragments.AboutUs;
import io.github.yesalam.bhopalbrts.fragments.ContactUs;
import io.github.yesalam.bhopalbrts.fragments.ThankU;

/**
 * Created by yesalam on 22-08-2015.
 */
public class AboutDevelopersPagerAdapter extends FragmentPagerAdapter {
    private Context context ;

    public AboutDevelopersPagerAdapter(FragmentManager fm,Context context){
        super(fm);
        this.context = context ;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return context.getResources().getString(R.string.aboutus_label) ;
            case 1:
                return context.getResources().getString(R.string.contactus_label) ;
            case 2:
                return context.getResources().getString(R.string.thanku_label) ;
            default:
                return context.getResources().getString(R.string.default_label) ;
        }
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment ;
        switch (position){
            case 0:
                fragment = new AboutUs();
                return fragment;
            case 1:
                fragment = new ContactUs();
                return fragment;
            case 2:
                fragment = new ThankU();
                return fragment;
            default:
                return null ;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }
}
