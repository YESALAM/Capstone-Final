package io.github.yesalam.bhopalbrts.fragments;

/**
 * Created by yesalam on 20-08-2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import io.github.yesalam.bhopalbrts.Activity.Bhopal_BRTS;
import io.github.yesalam.bhopalbrts.Interface.ReadynessListener;
import io.github.yesalam.bhopalbrts.R;
import io.github.yesalam.bhopalbrts.Activity.RouteDetailActivity;
import io.github.yesalam.bhopalbrts.adapter.RouteListAdapter;
import io.github.yesalam.bhopalbrts.data.BusDataContract;
import io.github.yesalam.bhopalbrts.util.Util;

import java.util.ArrayList;

/**
 * This fragment shows the all available route of BRTS and summary of them .
 */
public class Route extends Fragment implements AdapterView.OnItemClickListener {

    private final String LOG_TAG = Route.class.getSimpleName() ;

    Bhopal_BRTS activity ;
    ListView listView ;
    ArrayList<String[]> data ;





    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_route,container,false);
        listView = (ListView) view.findViewById(R.id.routeListView);
        listView.setOnItemClickListener(this);

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
        intent.putExtra(Util.BUS,route);
        intent.putExtra(Util.ORIGIN,origin);
        intent.putExtra(Util.DESTINATION,destination);
        intent.putExtra(Util.JUNCTION,junction);
        startActivity(intent);
    }



}

