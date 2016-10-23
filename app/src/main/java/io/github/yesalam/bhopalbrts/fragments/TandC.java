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
public class TandC extends Fragment {

    TextView tandc_detail ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_t_and_c,container,false);
        return  view ;
    }

    @Override
    public void onStart() {
        super.onStart();
        initialize();
    }

    private void initialize(){
        View root = getView() ;
        tandc_detail = (TextView) root.findViewById(R.id.tandcdetail);

        tandc_detail.setText(Html.fromHtml(getString(R.string.tandc)));




    }


}
