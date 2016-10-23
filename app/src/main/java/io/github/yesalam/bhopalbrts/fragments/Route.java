package io.github.yesalam.bhopalbrts.fragments;

/**
 * Created by yesalam on 20-08-2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import io.github.yesalam.bhopalbrts.Bhopal_BRTS;
import io.github.yesalam.bhopalbrts.R;
import io.github.yesalam.bhopalbrts.RouteDetailActivity;
import io.github.yesalam.bhopalbrts.adapter.RouteListAdapter;

import java.util.ArrayList;

/**
 * This fragment shows the all available route of BRTS and summary of them .
 */
public class Route extends Fragment implements AdapterView.OnItemClickListener {

    private final String LOG_TAG = Route.class.getSimpleName() ;

    Bhopal_BRTS activity ;
    ListView listView ;

    ArrayList<String[]> data ;
    SharedPreferences settings ;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (Bhopal_BRTS) activity ;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_route,container,false);
        listView = (ListView) view.findViewById(R.id.routeListView);

        data = new ArrayList<>();


        Resources res = getResources() ;
        String[] routes = res.getStringArray(R.array.routes_array);
        String[] nodes = res.getStringArray(R.array.route_nodes);
        String[] stop_count = res.getStringArray(R.array.stop_count);
        for(int i=0;i<routes.length;i++){
            String[] cdata = {routes[i],nodes[i],stop_count[i]};
            data.add(cdata);
        }

        RouteListAdapter adapter = new RouteListAdapter(getActivity(),data);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        return view ;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String[] data_array = data.get(position) ;
        String route  = data_array[0] ;
        String[] leafs = data_array[1].split(" To ");
        String origin = leafs[0] ;
        String destination = leafs[1] ;
        String junction = null ;
        Intent intent = new Intent(getActivity(), RouteDetailActivity.class);
        intent.putExtra("BUS",route);
        intent.putExtra("ORIGIN",origin);
        intent.putExtra("DESTINATION",destination);
        intent.putExtra("JUNCTION",junction);
        startActivity(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(LOG_TAG, "OnStop called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(LOG_TAG, "OnPause called");
    }

    @Override
    public void onStart() {
        //settings = activity.setting ;
        //settings.edit().putInt("tab_id",3).commit();
        super.onStart();
        Log.e(LOG_TAG, "OnStart called");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(LOG_TAG, "OnCreate called");
    }

    @Override
    public void onResume() {
        Log.e(LOG_TAG, "OnResume called");
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(LOG_TAG, "OnDestroy called");

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.e(LOG_TAG, "OnLowMemory called");

    }


}

