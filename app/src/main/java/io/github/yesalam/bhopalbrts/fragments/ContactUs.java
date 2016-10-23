package io.github.yesalam.bhopalbrts.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.github.yesalam.bhopalbrts.R;

/**
 * Created by yesalam on 22-08-2015.
 */
public class ContactUs extends Fragment {
    TextView contact_detail;
    TextView contact_alam ;

    @Override
    public void onStart() {
        super.onStart();
        iniialize();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact_us,container,false);
        return view;
    }

    private void iniialize(){
        View root = getView();
        contact_detail = (TextView) root.findViewById(R.id.contact_us_detail);
        contact_alam = (TextView) root.findViewById(R.id.contact_alam_detail);
        contact_detail.setText(Html.fromHtml(getString(R.string.contact_us_detail)));
        contact_alam.setText(Html.fromHtml(getString(R.string.contact_alam_detail)));
        contact_detail.setMovementMethod(LinkMovementMethod.getInstance());
        contact_alam.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
