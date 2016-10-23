package io.github.yesalam.bhopalbrts.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import io.github.yesalam.bhopalbrts.fragments.AboutUs;
import io.github.yesalam.bhopalbrts.fragments.ContactUs;
import io.github.yesalam.bhopalbrts.fragments.ThankU;

/**
 * Created by yesalam on 22-08-2015.
 */
public class AboutDevelopersPagerAdapter extends FragmentPagerAdapter {
    public AboutDevelopersPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "About us" ;
            case 1:
                return "Contact us" ;
            case 2:
                return "Thank you" ;
            default:
                return "Love you" ;
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
