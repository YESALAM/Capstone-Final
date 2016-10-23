package io.github.yesalam.bhopalbrts.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
public class AboutUs extends Fragment {

    TextView about_app ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us,container,false);
        return view ;
    }

    @Override
    public void onStart() {
        super.onStart();
        initialize();
    }

    private void initialize(){
        View root = getView() ;
        about_app = (TextView) root.findViewById(R.id.about_app_detail);
        about_app.setText(Html.fromHtml(getString(R.string.about_app_detail)));

    }
}
