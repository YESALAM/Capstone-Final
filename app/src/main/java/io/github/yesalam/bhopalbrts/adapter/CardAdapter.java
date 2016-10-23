package io.github.yesalam.bhopalbrts.adapter;

/**
 * Created by Sadar-e- on 7/12/2015.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import io.github.yesalam.bhopalbrts.R;
import io.github.yesalam.bhopalbrts.datamodel.CardData;

import java.util.ArrayList;
import java.util.List;

/**
 * This adapter is used to fill the list in bus fragment result.
 */
public class CardAdapter extends ArrayAdapter<CardData> {
    Context context ;
    ArrayList<CardData> dataset ;

    public CardAdapter(Context context, List<CardData> objects) {
        super(context, R.layout.card, objects);
        this.dataset = (ArrayList<CardData>) objects;
        this.context = context ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.card, parent, false);
        TextView tvbuses ;
        TextView tvfrom ;
        TextView tvto;
        TextView tvdist ;
        TextView tvfare ;
        TextView tvrupee ;

        tvbuses = (TextView) itemView.findViewById(R.id.busesTextView);
        tvfrom = (TextView) itemView.findViewById(R.id.fromStop);
        tvto = (TextView) itemView.findViewById(R.id.toStop);
        tvdist = (TextView) itemView.findViewById(R.id.distValue);
        tvfare = (TextView) itemView.findViewById(R.id.fareValue);
        tvrupee = (TextView) itemView.findViewById(R.id.rupeeSymbol) ;

        tvbuses.setText(dataset.get(position).getBuses());
        tvfrom.setText(dataset.get(position).getFrom());
        tvto.setText(dataset.get(position).getTo());
        tvdist.setText(dataset.get(position).getDist()+"");
        tvfare.setText(dataset.get(position).getFare()+"");
        tvrupee.setTypeface(Typeface.createFromAsset(context.getAssets(), "Fonts/rupee.ttf"));
        tvrupee.setText("`");

        return itemView;
    }
}
