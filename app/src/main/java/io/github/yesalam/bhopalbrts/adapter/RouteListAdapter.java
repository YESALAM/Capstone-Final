package io.github.yesalam.bhopalbrts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import io.github.yesalam.bhopalbrts.R;

import java.util.ArrayList;

/**
 * Created by yesalam on 03-08-2015.
 */
public class RouteListAdapter extends ArrayAdapter<String[]> {

    ArrayList<String[]> data ;
    Context context ;


    public RouteListAdapter(Context context, ArrayList<String[]> data){
        super(context, R.layout.list_item_route_fragment,data);
        this.context = context ;
        this.data = data ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_item_route_fragment,parent,false);
        TextView route_id_holder = (TextView) view.findViewById(R.id.route_id);
        TextView route_leafs_holder = (TextView) view.findViewById(R.id.route_leafs);
        TextView route_stop_no = (TextView) view.findViewById(R.id.route_stop_no) ;

        route_id_holder.setText(data.get(position)[0]);
        route_leafs_holder.setText(data.get(position)[1]);
        route_stop_no.setText(data.get(position)[2]);

        return view ;
    }


}
