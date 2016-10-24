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
import io.github.yesalam.bhopalbrts.datamodel.CardData;
import io.github.yesalam.bhopalbrts.util.AssetDatabaseHelper;
import io.github.yesalam.bhopalbrts.util.Calculator;

import java.util.List;

/**
 * This fragment show two EditText view which is used to enter the origin and
 * destination of the journey and then search the relevant route , distance and fare .
 */
public class Bus extends Fragment implements View.OnClickListener ,AdapterView.OnItemClickListener {

    private final String LOG_TAG = Bus.class.getSimpleName() ;
    Bhopal_BRTS activity ;
    SharedPreferences setting ;

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

    AssetDatabaseHelper dbHelper ;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (Bhopal_BRTS) activity ;
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



        dbHelper = AssetDatabaseHelper.getDatabaseHelper(activity);

        String[] from = {"stop"};
        int[] to = {android.R.id.text1};

        adapter = new SimpleCursorAdapter(getActivity(),android.R.layout.simple_list_item_1,null,from,to);
        adapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor cursor) {
                if (cursor == null) {
                    return "";
                } else {
                    final int colIndex = cursor.getColumnIndexOrThrow("stop");
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
                if (start == 0) {
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
        dataset = new Calculator(from,to, dbHelper).calc() ;
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


    public static int setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return 0;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        totalHeight = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        return totalHeight ;

    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CardData currentData = (CardData) listView.getItemAtPosition(position);
        Intent intent = new Intent(getActivity(), RouteDetailActivity.class);
        intent.putExtra("ORIGIN",currentData.getFrom());
        intent.putExtra("DESTINATION",currentData.getTo());
        intent.putExtra("JUNCTION",currentData.getJunctions());
        intent.putExtra("FARE",currentData.getFare());
        intent.putExtra("BUS", currentData.getBuses());
        startActivity(intent);

    }


    public boolean isInputValid(String from , String to ){
        Context context = getActivity() ;
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

    private void moveViewToScreenFinish( View view )
    {

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics( dm );
        TranslateAnimation anim  = new TranslateAnimation( 0, -dm.widthPixels, 0,  0);
        anim.setDuration(1000);
        anim.setFillAfter( true );
        view.startAnimation(anim);
    }

    private void moveViewToScreenCenter( View view )
    {

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics( dm );
        TranslateAnimation anim = new TranslateAnimation( dm.widthPixels, 0 , 0,  0);
        anim.setDuration(2000);
        anim.setFillAfter( true );
        view.startAnimation(anim);
    }
}
