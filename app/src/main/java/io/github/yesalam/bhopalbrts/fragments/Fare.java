package io.github.yesalam.bhopalbrts.fragments;

/**
 * Created by yesalam on 20-08-2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import io.github.yesalam.bhopalbrts.Bhopal_BRTS;
import io.github.yesalam.bhopalbrts.R;
import io.github.yesalam.bhopalbrts.SelectStopActivity;
import io.github.yesalam.bhopalbrts.util.AssetDatabaseHelper;
import io.github.yesalam.bhopalbrts.util.Calculator;
import io.github.yesalam.bhopalbrts.util.DataBaseHelper;

/**
 * This fragment is for minimum fare calculation between two stops . Stops
 * may be direct or via junctions.
 */
public class Fare extends Fragment implements View.OnClickListener {

    Bhopal_BRTS activity ;

    private final String LOG_TAG = Fare.class.getSimpleName() ;
    SharedPreferences setting ;

    AutoCompleteTextView actvfrom;
    AutoCompleteTextView actvto ;
    Button btfrom;
    Button btto;
    Button search;
    ImageView ivfrom;
    ImageView ivto;
    TextView fareamount;
    TextView farerupee;
    TextView viaholder;
    //DataBaseHelper dbManager;
    SimpleCursorAdapter adapter;

    AssetDatabaseHelper dbHelper ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fare,container,false);

        return view ;
    }

    public void onStart(){
       //setting = activity.setting ;
        //setting.edit().putInt("tab_id",3).commit();
        Log.e("fare", "onStart called");
        super.onStart();
        initialize();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (Bhopal_BRTS) activity ;
    }

    private void initialize(){
        actvfrom = (AutoCompleteTextView) getView().findViewById(R.id.fareautoCompleteTextViewFindRouteFrom);
        actvto = (AutoCompleteTextView) getView().findViewById(R.id.fareautoCompleteTextViewFindRouteTo);
        btfrom = (Button) getView().findViewById(R.id.farebuttonFindRouteFrom);
        btto = (Button) getView().findViewById(R.id.farebuttonFindRouteTo);
        search = (Button) getView().findViewById(R.id.farebuttonSearchRoute);
        ivfrom = (ImageView) getView().findViewById(R.id.fareimageViewCancleFrom);
        ivto = (ImageView) getView().findViewById(R.id.fareimageViewCancleTo);
        fareamount = (TextView) getView().findViewById(R.id.textViewFareAmount);
        farerupee = (TextView) getView().findViewById(R.id.textViewFareRupee);
        viaholder = (TextView) getView().findViewById(R.id.viaholder) ;
        viaholder.setVisibility(View.GONE);
        farerupee.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "Fonts/rupee.ttf"));
        farerupee.setText("`");


        dbHelper = AssetDatabaseHelper.getDatabaseHelper(activity);

        String[] from = {"stop"};
        int[] to = {android.R.id.text1};

        adapter = new SimpleCursorAdapter(getActivity(),android.R.layout.simple_list_item_1,null,from,to);
        adapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor cursor) {
                if(cursor == null){
                    return "" ;
                } else {
                    final int colIndex = cursor.getColumnIndexOrThrow("stop");
                    return cursor.getString(colIndex);
                }

            }
        });
        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {

                Cursor cursor=null;
                if(constraint!=null){
                    int count = constraint.length();
                    if(count>=3){
                        String constrains = constraint.toString();
                        cursor = dbHelper.getStops(constrains);
                    }
                }
                return cursor;

            }
        });

        actvfrom.setAdapter(adapter);
        actvto.setAdapter(adapter);


        btfrom.setOnClickListener(this);
        btto.setOnClickListener(this);
        search.setOnClickListener(this);
        ivfrom.setOnClickListener(this);
        ivto.setOnClickListener(this);

        actvfrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(start==0 ) {
                    ivfrom.setVisibility(View.GONE);
                } else {
                    ivfrom.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        actvto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(start==0 ) {
                    ivto.setVisibility(View.GONE);
                } else {
                    ivto.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        // ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        switch (v.getId()){
            case R.id.farebuttonFindRouteFrom :

                startActivityForResult(new Intent(getActivity(), SelectStopActivity.class), 0);

                break;
            case R.id.farebuttonFindRouteTo :

                startActivityForResult(new Intent(getActivity(), SelectStopActivity.class),1);
                break;
            case R.id.farebuttonSearchRoute :
                ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                String from = actvfrom.getText().toString() ;
                String to = actvto.getText().toString() ;
                //Chech the validity of input strings .
                if(isInputValid(from,to))
                {
                    Calculator calci = new Calculator(from,to, dbHelper);
                    calci.calc();
                    String fare =  calci.getFare()+"" ;
                    String via = calci.getRoute();
                    viaholder.setVisibility(View.VISIBLE);
                    //TODO Set the route also so that user can know fare for which route is shown bellow.
                    fareamount.setText(fare);
                    viaholder.setText(via);
                }

                break;
            case R.id.fareimageViewCancleFrom :
                //Clear the autoCompleteTextViewFindRouteFrom
                actvfrom.setText("");
                break;
            case R.id.fareimageViewCancleTo :
                //Clear the autoCompleteTextViewFindRouteTo
                actvto.setText("");
                break;
            default:
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == -1){

            if(requestCode == 0){
                this.actvfrom.requestFocus();
                this.actvfrom.setAdapter(null);
                this.actvfrom.setText(data.getStringExtra("stop"));
                this.actvfrom.setAdapter(this.adapter);
            } else if(requestCode == 1){
                this.actvto.requestFocus();
                this.actvto.setAdapter(null);
                this.actvto.setText(data.getStringExtra("stop"));
                this.actvto.setAdapter(this.adapter);
            }
        }

    }


    private  boolean isInputValid(String from , String to ){
        Context context = getActivity();
        if(from.isEmpty()){
            Toast.makeText(context, "Please enter Origin", Toast.LENGTH_SHORT).show();
            return false ;
        }else if(ivfrom.getVisibility() == View.VISIBLE){
            Toast.makeText(context,"Origin not found",Toast.LENGTH_SHORT).show();
            return false;
        }else if(to.isEmpty()){
            Toast.makeText(context,"Please enter Destination",Toast.LENGTH_SHORT).show();
            return false ;
        }else if(ivto.getVisibility() == View.VISIBLE){
            Toast.makeText(context,"Destination not found",Toast.LENGTH_SHORT).show();
            return false ;
        }else if(from.equalsIgnoreCase(to)){
            Toast.makeText(context,"You are at destination already ",Toast.LENGTH_SHORT).show();
            return false ;
        }else {
            return true ;
        }
    }


}
