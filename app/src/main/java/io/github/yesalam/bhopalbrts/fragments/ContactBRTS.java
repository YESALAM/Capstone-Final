package io.github.yesalam.bhopalbrts.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.github.yesalam.bhopalbrts.R;

import java.util.regex.Pattern;

/**
 * Created by yesalam on 22-08-2015.
 */
public class ContactBRTS extends Fragment {

    TextView local_police;
    TextView cm_helpline ;
    TextView woman_helpline ;
    TextView emenrgency_response ;
    TextView railway_enquiry ;
    TextView office_brts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_brts,container,false);
        return  view ;
    }

    @Override
    public void onStart() {
        super.onStart();
        initialize();
    }

    private void initialize(){
        View root = getView() ;
        office_brts = (TextView) root.findViewById(R.id.brts_office);
        local_police = (TextView) root.findViewById(R.id.local_police);
        cm_helpline = (TextView) root.findViewById(R.id.cm_helpline);
        woman_helpline =(TextView) root.findViewById(R.id.woman_helpline) ;
        emenrgency_response = (TextView) root.findViewById(R.id.emergency_response_services);
        railway_enquiry = (TextView) root.findViewById(R.id.emergency_response_services);

        office_brts.setText(Html.fromHtml(getString(R.string.brts_office)));

        Linkify.addLinks(local_police, Pattern.compile("100"), "tel:");

        Linkify.addLinks(woman_helpline, Pattern.compile("\\d{4}"),"tel:" );
        Linkify.addLinks(cm_helpline, Pattern.compile("\\d{3}"),"tel:" );
        Linkify.addLinks(emenrgency_response, Pattern.compile("\\d{3}"),"tel:" );
        Linkify.addLinks(railway_enquiry, Pattern.compile("\\d{3}"),"tel:" );



    }
}
