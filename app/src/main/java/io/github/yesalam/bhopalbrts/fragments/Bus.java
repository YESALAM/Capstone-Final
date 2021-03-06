package io.github.yesalam.bhopalbrts.fragments;

/**
 * Created by Sadar-e- on 7/8/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import io.github.yesalam.bhopalbrts.Activity.Bhopal_BRTS;
import io.github.yesalam.bhopalbrts.R;
import io.github.yesalam.bhopalbrts.Activity.RouteDetailActivity;
import io.github.yesalam.bhopalbrts.Activity.SelectStopActivity;
import io.github.yesalam.bhopalbrts.adapter.CardAdapter;
import io.github.yesalam.bhopalbrts.data.BusDataContract;
import io.github.yesalam.bhopalbrts.datamodel.CardData;
import io.github.yesalam.bhopalbrts.data.AssetDatabaseHelper;
import io.github.yesalam.bhopalbrts.util.Calculator;
import io.github.yesalam.bhopalbrts.util.Util;

import java.util.List;

/**
 * This fragment show two EditText view which is used to enter the origin and
 * destination of the journey and then search the relevant route , distance and fare .
 */
public class Bus extends Fragment implements View.OnClickListener ,AdapterView.OnItemClickListener,TextWatcher {

    private final String LOG_TAG = Bus.class.getSimpleName() ;


    AutoCompleteTextView actvfrom;
    AutoCompleteTextView actvto ;
    Button btfrom;
    Button btto;
    Button search;

    ImageView ivfrom;
    ImageView ivto;
    ImageView bus_motion ;
    ListView listView ;
    CardAdapter cadapter;
    RelativeLayout input_area ;



    SimpleCursorAdapter adapter;
    List<CardData> dataset ;



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    private void initialize(){

        input_area = (RelativeLayout) getView().findViewById(R.id.input_area);
        actvfrom = (AutoCompleteTextView) getView().findViewById(R.id.autoCompleteTextViewFindRouteFrom);
        actvto = (AutoCompleteTextView) getView().findViewById(R.id.autoCompleteTextViewFindRouteTo);
        btfrom = (Button) getView().findViewById(R.id.buttonFindRouteFrom);
        btto = (Button) getView().findViewById(R.id.buttonFindRouteTo);
        search = (Button) getView().findViewById(R.id.buttonSearchRoute);

        ivfrom = (ImageView) getView().findViewById(R.id.imageViewCancleFrom);
        ivto = (ImageView) getView().findViewById(R.id.imageViewCancleTo);
        listView = (ListView) getView().findViewById(R.id.myrecycleview);
        listView.setVisibility(View.GONE);
        listView.setOnItemClickListener(this);
        bus_motion = (ImageView) getView().findViewById(R.id.bus_motion_view);


        String[] from = {BusDataContract.STOPS.COLUMN_STOP};
        int[] to = {android.R.id.text1};

        adapter = new SimpleCursorAdapter(getActivity(),android.R.layout.simple_list_item_1,null,from,to);
        adapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor cursor) {
                if (cursor == null) {
                    return "";
                } else {
                    final int colIndex = cursor.getColumnIndexOrThrow(BusDataContract.STOPS.COLUMN_STOP);
                    String result = cursor.getString(colIndex);
                    return result;
                }

            }
        });
        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {

                Cursor cursor = null;
                if (constraint != null) {
                    int count = constraint.length();
                    if (count >= 3) {
                        String query = constraint.toString();

                        String[] projection = {BusDataContract.STOPS._ID,BusDataContract.STOPS.COLUMN_STOP} ;
                        String selection = "stop like '"+query+"%' or stop like '%"+query+"%'" ;
                        cursor = getContext().getContentResolver().query(BusDataContract.STOPS.buildStopqueryUri(query),projection,selection,null,null);
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

        actvfrom.addTextChangedListener(this);

        actvto.addTextChangedListener(this);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bus,container,false);
        return view ;
    }

    public void onStart(){

        super.onStart();
        initialize();
        moveViewToScreenCenter(bus_motion);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.buttonFindRouteFrom :

                startActivityForResult(new Intent(getActivity(), SelectStopActivity.class), 0);

                break;
            case R.id.buttonFindRouteTo :

                startActivityForResult(new Intent(getActivity(), SelectStopActivity.class),1);
                break;
            case R.id.buttonSearchRoute :
                //TODO
                ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                String from = actvfrom.getText().toString() ;
                String to = actvto.getText().toString() ;
                //Check the validity of the strings.
                if(isInputValid(from, to)){
                    listView.setVisibility(View.GONE);
                    bus_motion.invalidate();

                    searchHandler(from, to);
                }

                break;
            case R.id.imageViewCancleFrom :
                //Clear the autoCompleteTextViewFindRouteFrom
                actvfrom.setText("");
                break;
            case R.id.imageViewCancleTo :
                //Clear the autoCompleteTextViewFindRouteTo
                actvto.setText("");
                break;

            default:
        }
    }

    /**
     * Handle the search
     *
     */
    private void searchHandler(String from , String to) {
        dataset = new Calculator(from,to, getContext()).calc() ;
        cadapter = new CardAdapter(getActivity(),dataset);


        moveViewToScreenFinish(bus_motion);

        bus_motion.postDelayed(new Runnable() {
            @Override
            public void run() {
                listView.setVisibility(View.VISIBLE);
                listView.setAdapter(cadapter);
            }
        },1000);

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == -1){

            if(requestCode == 0){
                this.actvfrom.requestFocus();
                this.actvfrom.setAdapter(null);
                this.actvfrom.setText(data.getStringExtra(BusDataContract.STOPS.COLUMN_STOP));
                this.actvfrom.setAdapter(this.adapter);
            } else if(requestCode == 1){
                this.actvto.requestFocus();
                this.actvto.setAdapter(null);
                this.actvto.setText(data.getStringExtra(BusDataContract.STOPS.COLUMN_STOP));
                this.actvto.setAdapter(this.adapter);
            }
        }

    }






    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CardData currentData = (CardData) listView.getItemAtPosition(position);
        Intent intent = new Intent(getActivity(), RouteDetailActivity.class);
        intent.putExtra(Util.ORIGIN,currentData.getFrom());
        intent.putExtra(Util.DESTINATION,currentData.getTo());
        intent.putExtra(Util.JUNCTION,currentData.getJunctions());
        intent.putExtra(Util.FARE,currentData.getFare());
        intent.putExtra(Util.BUS, currentData.getBuses());
        startActivity(intent);

    }


    public boolean isInputValid(String from , String to ){
        if(from.isEmpty()){
           toastIt(R.string.error_input_origin);
            return false ;
        }else if(ivfrom.getVisibility() == View.VISIBLE){
            toastIt(R.string.error_origin_unknown);
            return false;
        }else if(to.isEmpty()){
           toastIt(R.string.error_input_destination);
            return false ;
        }else if(ivto.getVisibility() == View.VISIBLE){
            toastIt(R.string.error_destination_unknown);
            return false ;
        }else if(from.equalsIgnoreCase(to)){
            toastIt(R.string.error_input_same);
            return false ;
        }else {
            return true ;
        }
    }

    private void toastIt(int message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {


        View view ;
        if(actvfrom.isFocused()) view = ivfrom ;
        else view = ivto ;
        if(start==0 ) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    private void moveViewToScreenFinish( View view )
    {

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics( dm );
        TranslateAnimation anim  = new TranslateAnimation( 0, -dm.widthPixels, 0,  0);
        anim.setDuration(1000);
        anim.setFillAfter( true );
        view.startAnimation(anim);
    }

    private void moveViewToScreenCenter( View view )
    {

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics( dm );
        TranslateAnimation anim = new TranslateAnimation( dm.widthPixels, 0 , 0,  0);
        anim.setDuration(2000);
        anim.setFillAfter( true );
        view.startAnimation(anim);
    }



}
