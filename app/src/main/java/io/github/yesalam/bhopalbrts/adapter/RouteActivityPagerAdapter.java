package io.github.yesalam.bhopalbrts.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import io.github.yesalam.bhopalbrts.R;
import io.github.yesalam.bhopalbrts.datamodel.Stop;
import io.github.yesalam.bhopalbrts.fragments.RouteDetail;
import io.github.yesalam.bhopalbrts.fragments.RouteMap;

import java.util.ArrayList;

/**
 * Created by yesalam on 22-08-2015.
 */
public class RouteActivityPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Stop> stopList;
    Context context ;


    public RouteActivityPagerAdapter(FragmentManager fm, ArrayList<Stop> stopList,Context context) {
        super(fm);
        this.stopList = stopList ;
        this.context = context ;
    }



    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return context.getString(R.string.routedetail_label) ;
        } else {
            return context.getString(R.string.routemap_label) ;
        }

    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            RouteDetail fragment = new RouteDetail() ;
            return fragment ;
        } else {
            RouteMap fragment = new RouteMap();
            return fragment ;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }
}
