package io.github.yesalam.bhopalbrts;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import io.github.yesalam.bhopalbrts.datamodel.Stop;
import io.github.yesalam.bhopalbrts.util.AssetDatabaseHelper;


import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by yesalam on 22-08-2015.
 */
public class SelectStopActivity extends AppCompatActivity implements TextWatcher,AdapterView.OnItemClickListener , View.OnClickListener, android.location.LocationListener,com.google.android.gms.location.LocationListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {

    private final String LOG_TAG = SelectStopActivity.class.getSimpleName() ;
    EditText editText;
    ListView listView;
    SimpleCursorAdapter adapter;
    Cursor cursor;
    AssetDatabaseHelper dbHelper;
    ImageView gpsbutton ;
    LocationManager manager ;
    ArrayList<Stop> dataset;
    ProgressDialog dialog ;
    boolean gps_clicked = false ;

    GoogleApiClient mGoogleApiClient ;
    LocationRequest mLocationRequest ;

    @Override
    protected void onPause() {
        Log.e(LOG_TAG, "Activity Paused");
        super.onPause();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_stop);
        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        editText = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(R.id.listViewall);
        gpsbutton = (ImageView) findViewById(R.id.gpsbutton);

        gpsbutton.setOnClickListener(this);
        editText.addTextChangedListener(this);
        listView.setOnItemClickListener(this);

        String[] from = {"stop","vicinity"};
        int[] to ={android.R.id.text1,android.R.id.text2};
        dbHelper = AssetDatabaseHelper.getDatabaseHelper(this);
        cursor = dbHelper.getAllStops("");

        adapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_2,cursor,from,to);

        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_select_stop, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        cursor = dbHelper.query(s, true);
        cursor = dbHelper.query(s);
        adapter.swapCursor(cursor);

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor item = (Cursor) adapter.getItem(position);
        String stop = item.getString(1);
        Log.e(LOG_TAG,stop);
        finishThis(stop);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mGoogleApiClient.isConnected()){
             mGoogleApiClient.disconnect();
        }
        if(gps_clicked){
            manager.removeUpdates(this);
        }


    }

    private  void finishThis(String stop){
        Intent localIntent = new Intent();
        localIntent.putExtra("stop", stop);
        this.setResult(-1, localIntent);
        this.finish();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.gpsbutton :
                gps_clicked = true ;

                manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    createGpsDisabledAlert();
                    return;
                }
                dialog = new ProgressDialog(this);
                dialog.setMessage("Getting your Location....");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                if(mGoogleApiClient.isConnected()){
                    if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
                    }
                }
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
                break;
            default:
        }
    }


    public void createGpsDisabledAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Location is disabled! Would you like to enable it?").setCancelable(false).setPositiveButton("Enable Location", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                showGpsOptions();
            }
        });


        builder.setNegativeButton("Do nothing", new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showGpsOptions() {
        Intent gpsOptionsIntent = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
        startActivity(gpsOptionsIntent);
    }

    public void getNearestStopList(double currentLatitude,double currentLongitude)  {
        dataset = new ArrayList<>();
        Cursor cursor1 = dbHelper.getMain();
        while (cursor1.moveToNext()){
            Log.i(LOG_TAG,"Into while");
            float[] result = new float[1];
            double  lst_lat = cursor1.getDouble(2);
            double  lst_lng = cursor1.getDouble(1);

            Location.distanceBetween(currentLatitude, currentLongitude, lst_lat, lst_lng, result);

            double dist = result[0]/1000;
            String distpercise = String.format("%.2f",dist);
            float distance = Float.parseFloat(distpercise);
            Log.i(LOG_TAG,"Dist is " + distance);
            if(distance<3.0){
                Log.e(LOG_TAG,"Minimum dist"+distance);
                String stopname = cursor1.getString(0);
                Stop temp = new Stop(stopname,distance);
                dataset.add(temp);
            }

        }
    }



    @Override
    public void onLocationChanged(Location location) {
        if(location != null) {
            double currentLatitude = location.getLatitude();
            double currentLongitude = location.getLongitude();
            dialog.dismiss();
            Log.e(LOG_TAG,currentLatitude+"   "+currentLongitude);
            getNearestStopList(currentLatitude, currentLongitude);
            Collections.sort(dataset);
            if(dataset.size()>0){
                String stopname = dataset.get(0).getStop();
                finishThis(stopname);
            } else {
                Log.i(LOG_TAG,"Location is "+currentLatitude+" "+currentLongitude);
                Toast.makeText(this,"NO Stop Found",Toast.LENGTH_SHORT).show();
            }
            manager.removeUpdates(this);
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10); // Update location every second


    }

    @Override
    public void onConnectionSuspended(int i) {
        if(i== GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST){
            Toast.makeText(this,"Network Lost",Toast.LENGTH_SHORT).show();
        }
        if(i== GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED){
            Toast.makeText(this,"Service disconnected",Toast.LENGTH_SHORT).show();
        }
        Log.i(LOG_TAG, "GoogleApiClient connection has been suspend");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(LOG_TAG, "GoogleApiClient connection has failed");
    }
}

