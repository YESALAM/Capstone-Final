package io.github.yesalam.bhopalbrts.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.github.yesalam.bhopalbrts.R;

/**
 * Created by yesalam on 22-08-2015.
 */
public class AboutBRTS extends Fragment {


    TextView brts_detail ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_brts,container,false);

        return view ;
    }

    @Override
    public void onStart() {
        super.onStart();
        Initialize();
    }

    private void Initialize(){
       View root = getView() ;
        brts_detail = (TextView) root.findViewById(R.id.bhopal_brts_detail);

        brts_detail.setText(Html.fromHtml(getString(R.string.bhopal_brts_detail)));

    }

}
