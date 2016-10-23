package io.github.yesalam.bhopalbrts.adapter;

/**
 * Created by Sadar-e- on 7/8/2015.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import io.github.yesalam.bhopalbrts.R;
import io.github.yesalam.bhopalbrts.fragments.Bus;
import io.github.yesalam.bhopalbrts.fragments.Fare;
import io.github.yesalam.bhopalbrts.fragments.More;
import io.github.yesalam.bhopalbrts.fragments.Route;

/**
 * The Adapter for Main layout which return the corresponding views.
 */
public class TabPagerAdapter extends FragmentPagerAdapter {

    Context context ;

    public TabPagerAdapter(android.support.v4.app.FragmentManager fm, Context context){
        super(fm);
        this.context = context ;
    }



    @Override
    public Fragment getItem(int position) {
        Fragment fragment ;
        switch (position){
            case 0:
                fragment = new Bus();
                break;
            case 1:
                fragment = new Fare();
                break;
            case 2:
                fragment = new Route();
                break;
            case 3:
                fragment = new More();
                break;
            default:
                fragment = new Bus();

        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return context.getString(R.string.title_bus);

            case 1:
                return context.getString(R.string.title_fare);
            case 2:
                return context.getString(R.string.title_route);
            case 3:
                return context.getString(R.string.title_more);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
