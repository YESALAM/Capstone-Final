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
public class ThankU extends Fragment {

    TextView thank_u ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thank_u,container,false);
        return  view ;
    }

    @Override
    public void onStart() {
        super.onStart();
        initialize();
    }

    private void initialize(){
        View root = getView() ;
        thank_u = (TextView) root.findViewById(R.id.thankdetail);

        thank_u.setText(Html.fromHtml(getString(R.string.thank)));




    }

}
