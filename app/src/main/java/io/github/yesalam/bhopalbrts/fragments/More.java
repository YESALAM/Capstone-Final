package io.github.yesalam.bhopalbrts.fragments;

/**
 * Created by yesalam on 20-08-2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.github.yesalam.bhopalbrts.Activity.AboutBRTS;
import io.github.yesalam.bhopalbrts.Activity.AboutDevloper;
import io.github.yesalam.bhopalbrts.Activity.Bhopal_BRTS;
import io.github.yesalam.bhopalbrts.R;

/**
 * This fragment show the other detail about the app and the developer .
 */
public class More extends Fragment implements View.OnClickListener {

    private final String LOG_TAG = More.class.getSimpleName();
    private final String ID = "_id" ;
    SharedPreferences setting ;
    Bhopal_BRTS activity ;
    TextView bhopal_brts;
    TextView contact_brts;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (Bhopal_BRTS) activity ;
    }

    TextView t_and_c;
    TextView about_us;
    TextView contact_us;

    TextView thank_u;

    @Override
    public void onStart() {
        //setting = activity.setting ;
        //setting.edit().putInt("tab_id",4).commit();
        super.onStart();
        initialize();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more,container,false);

        return view ;
    }

    /**
     * Initialize the views of this fragment to be useable in java code
     */
    private void initialize(){
        bhopal_brts = (TextView) getView().findViewById(R.id.bhopal_brts);
        contact_brts = (TextView) getView().findViewById(R.id.contact_brts);
        t_and_c = (TextView) getView().findViewById(R.id.tandc);
        about_us = (TextView) getView().findViewById(R.id.about_us);
        contact_us = (TextView) getView().findViewById(R.id.contact_us) ;

        thank_u =(TextView) getView().findViewById(R.id.thanku) ;

        bhopal_brts.setOnClickListener(this);
        contact_brts.setOnClickListener(this);
        t_and_c.setOnClickListener(this);
        about_us.setOnClickListener(this);
        contact_us.setOnClickListener(this);

        thank_u.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent ;
        int id = v.getId();
        switch (id){
            case R.id.bhopal_brts:
                //TODO
                intent = new Intent(getActivity(), AboutBRTS.class);
                intent.putExtra(ID, 1);
                startActivity(intent);

                break;
            case R.id.contact_brts:
                //TODO
                intent = new Intent(getActivity(), AboutBRTS.class);
                intent.putExtra(ID, 2);
                startActivity(intent);

                break;
            case R.id.tandc:
                //TODO
                intent = new Intent(getActivity(), AboutBRTS.class);
                intent.putExtra(ID, 3);
                startActivity(intent);

                break;
            case R.id.about_us:
                //TODO
                intent = new Intent(getActivity(), AboutDevloper.class);
                intent.putExtra(ID, 4);
                startActivity(intent);

                break;
            case R.id.contact_us:
                //TODO
                intent = new Intent(getActivity(), AboutDevloper.class);
                intent.putExtra(ID, 5);
                startActivity(intent);

                break;

            case R.id.thanku:
                //TODO
                intent = new Intent(getActivity(), AboutDevloper.class);
                intent.putExtra(ID, 6);
                startActivity(intent);

                break;
            default:

        }
    }
}
