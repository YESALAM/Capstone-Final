package io.github.yesalam.bhopalbrts.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import io.github.yesalam.bhopalbrts.fragments.AboutBRTS;
import io.github.yesalam.bhopalbrts.fragments.ContactBRTS;
import io.github.yesalam.bhopalbrts.fragments.TandC;

/**
 * Created by yesalam on 22-08-2015.
 */
public class AboutBRTSPagerAdapter extends FragmentPagerAdapter {

    Context context ;

    public AboutBRTSPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "About ";
            case 1:
                return "Contact" ;
            case 2:
                return "Terms and Conditon";
            default:
                return "love you" ;
        }
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment ;
        switch (position){
            case 0:
                fragment = new AboutBRTS();
                return fragment ;
            case 1:
                fragment = new ContactBRTS();
                return fragment ;
            case 2:
                fragment = new TandC();
                return fragment ;
            default:
                return null ;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }
}
