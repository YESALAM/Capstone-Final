package io.github.yesalam.bhopalbrts.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import io.github.yesalam.bhopalbrts.Interface.ShowInfoListener;
import io.github.yesalam.bhopalbrts.R;
import io.github.yesalam.bhopalbrts.adapter.RouteDetailAdapter;
import io.github.yesalam.bhopalbrts.datamodel.Stop;
import io.github.yesalam.bhopalbrts.util.AssetDatabaseHelper;

import java.util.ArrayList;

/**
 * Created by yesalam on 22-08-2015.
 */
public class RouteDetail extends Fragment implements AdapterView.OnItemClickListener {
    private final String LOG_TAG = RouteDetail.class.getSimpleName() ;
    ListView listView ;
    AssetDatabaseHelper dbHelper;
    ArrayList<Stop> stopList ;
    ShowInfoListener showInfoListener;
    View footerview;

    public RouteDetail(){}

    public void setStopList(ArrayList<Stop> stopList) {
        this.stopList = stopList;
    }

    @Override
    public void onStart() {
        super.onStart();
        initialize();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        footerview = inflater.inflate(R.layout.list_item_route_detail,null);
        return inflater.inflate(R.layout.fragment_route_detail, container, false);
    }

    private void initialize(){

        listView = (ListView) getView().findViewById(R.id.listView) ;
        ImageView icon = (ImageView) footerview.findViewById(R.id.stopicon);
        icon.setVisibility(View.INVISIBLE);
        RelativeLayout rl = (RelativeLayout) footerview.findViewById(R.id.route_detail_item_layout);
        rl.setVisibility(View.INVISIBLE);
        RouteDetailAdapter adapter = new RouteDetailAdapter(getActivity(),stopList) ;
        listView.addFooterView(footerview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_card_map, menu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e(LOG_TAG, "Item clicked");
        showInfoListener.showInfo(position);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            showInfoListener = (ShowInfoListener) activity;
        } catch (ClassCastException e) {
            Log.e(LOG_TAG,"Activity should implement interface");
            throw new ClassCastException(activity.toString()
                    + " must implement ShowInfoListener");
        }
    }
}
