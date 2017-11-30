package com.alex.heverrest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alex.heverrest.Controller.RestaurantController;
import com.alex.heverrest.Model.Restaurant;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.vision.text.Line;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class FrontActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    public static final String TAG = "hever-rest";

    private static final String JSON_NAME_TAG = "name";
    private static final String JSON_SUB_TYPE_TAG = "sub_type";
    private static final String JSON_ADDRESS_TAG = "address";
    private static final String JSON_PIC_TAG = "pic";
    private static final String JSON_LAT_TAG = "lat";
    private static final String JSON_LONG_TAG = "long";

    public final static int MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 1;
    public final static int MY_PERMISSION_REQUEST_FINE_LOCATION = 2;

    Location mLastLocation;
    LocationRequest mLoationRequest = null;

    ArrayList<Restaurant.RestSubType> selectedCategories = new ArrayList();

    private GoogleApiClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);

        // setting onclick for all categories.
        findViewById(R.id.tvCatAsian).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSelectedCategory(v);
            }
        });
        findViewById(R.id.tvCatBar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSelectedCategory(v);
            }
        });
        findViewById(R.id.tvCatCoffee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSelectedCategory(v);
            }
        });
        findViewById(R.id.tvCatDairy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSelectedCategory(v);
            }
        });
        findViewById(R.id.tvCatFish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSelectedCategory(v);
            }
        });
        findViewById(R.id.tvCatItalian).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSelectedCategory(v);
            }
        });
        findViewById(R.id.tvCatFrench).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSelectedCategory(v);
            }
        });
        findViewById(R.id.tvCatMeat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSelectedCategory(v);
            }
        });
        findViewById(R.id.tvCatMediterranean).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSelectedCategory(v);
            }
        });
        findViewById(R.id.tvCatMexican).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSelectedCategory(v);
            }
        });
        findViewById(R.id.tvCatMiddleEastern).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSelectedCategory(v);
            }
        });
        findViewById(R.id.tvCatSandwiches).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSelectedCategory(v);
            }
        });
        findViewById(R.id.tvCatSushi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSelectedCategory(v);
            }
        });
        findViewById(R.id.tvCatVegan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSelectedCategory(v);
            }
        });
        findViewById(R.id.tvCatSweets).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSelectedCategory(v);
            }
        });

        findViewById(R.id.btnSearchCategory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedCategories.size() == 0) {
                    Toast.makeText(FrontActivity.this, getString(R.string.no_categories), Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                Intent i = new Intent(FrontActivity.this, RestListActivity.class);
                i.putExtra(RestListActivity.extraFilter, selectedCategories);
                i.putExtra(RestListActivity.extraLocation, mLastLocation);
                startActivity(i);
            }
        });

        findViewById(R.id.btnSelectAll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Restaurant.RestSubType> list = Arrays.asList(Restaurant.RestSubType.values());
                selectedCategories.clear();
                selectedCategories.addAll(list);

                LinearLayout layout = (LinearLayout)findViewById(R.id.llSubTypes);

                for(int i=0; i<layout.getChildCount(); i++) {
                    LinearLayout subLayout = (LinearLayout) layout.getChildAt(i);
                    for(int j=0; j<3; j++) {
                        TextView tv = (TextView) subLayout.getChildAt(j);
                        tv.setTextAppearance(R.style.CategorySelected);
                        tv.setBackgroundResource(R.drawable.item_category_selected);
                    }
                }
            }
        });

        findViewById(R.id.btnDeSelectAll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCategories.clear();

                LinearLayout layout = (LinearLayout)findViewById(R.id.llSubTypes);

                for(int i=0; i<layout.getChildCount(); i++) {
                    LinearLayout subLayout = (LinearLayout) layout.getChildAt(i);
                    for(int j=0; j<3; j++) {
                        TextView tv = (TextView) subLayout.getChildAt(j);
                        tv.setTextAppearance(R.style.Category);
                        tv.setBackgroundResource(R.drawable.item_category);
                    }
                }
            }
        });

        populateAllRestaurant();

        mClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
//                .addOnConnectionlFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(AppIndex.API).build();
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        if(!mClient.isConnected())
            mClient.connect();

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mClient);

        if (mLastLocation != null) {
            String lat = String.valueOf(mLastLocation.getLatitude());
            String lng = String.valueOf(mLastLocation.getLongitude());
//            Toast.makeText(this, String.valueOf(lat) + " " + String.valueOf(lng)
//                    + " " + String.valueOf(mLastLocation.getAccuracy()), Toast.LENGTH_LONG).show();
        }

        if(mLoationRequest == null) {
            mLoationRequest = new LocationRequest();
            mLoationRequest.setInterval(10000);
            mLoationRequest.setFastestInterval(5000);
            mLoationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mClient, mLoationRequest, this);
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.

        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.removeLocationUpdates(mClient, this);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient.connect();
        AppIndex.AppIndexApi.start(mClient, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(mClient, getIndexApiAction());
        mClient.disconnect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.

        if (mClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_COARSE_LOCATION);

            return;
        }

        startLocationUpdates();
    }

    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        String lat = String.valueOf(mLastLocation.getLatitude());
        String lng = String.valueOf(mLastLocation.getLongitude());
//        Toast.makeText(this, String.valueOf(lat) + " " + StResting.valueOf(lng)
//                + " " + String.valueOf(mLastLocation.getAccuracy()), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.d(TAG, "Connection suspended");
        mClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.d(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_FINE_LOCATION:
            case MY_PERMISSIONS_REQUEST_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //TODO:
                } else {
                    Toast.makeText(this, getString(R.string.no_location_error), Toast.LENGTH_LONG)
                            .show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    protected void onClickSelectedCategory(View v) {
        TextView tv = (TextView)v;
        Restaurant.RestSubType cat = Restaurant.RestSubType.Asian;
        switch(v.getId()) {
            case R.id.tvCatAsian:
                cat = Restaurant.RestSubType.Asian;
                break;
            case R.id.tvCatBar:
                cat = Restaurant.RestSubType.Bar;
                break;
            case R.id.tvCatCoffee:
                cat = Restaurant.RestSubType.Coffee;
                break;
            case R.id.tvCatDairy:
                cat = Restaurant.RestSubType.Dairy;
                break;
            case R.id.tvCatFish:
                cat = Restaurant.RestSubType.Fish;
                break;
            case R.id.tvCatFrench:
                cat = Restaurant.RestSubType.French;
                break;
            case R.id.tvCatItalian:
                cat = Restaurant.RestSubType.Italian;
                break;
            case R.id.tvCatMeat:
                cat = Restaurant.RestSubType.Meat;
                break;
            case R.id.tvCatMediterranean:
                cat = Restaurant.RestSubType.Mediterranean;
                break;
            case R.id.tvCatMexican:
                cat = Restaurant.RestSubType.Mexican;
                break;
            case R.id.tvCatMiddleEastern:
                cat = Restaurant.RestSubType.MiddleEastern;
                break;
            case R.id.tvCatSandwiches:
                cat = Restaurant.RestSubType.Sandwiches;
                break;
            case R.id.tvCatSushi:
                cat = Restaurant.RestSubType.Sushi;
                break;
            case R.id.tvCatSweets:
                cat = Restaurant.RestSubType.Sweets;
                break;
            case R.id.tvCatVegan:
                cat = Restaurant.RestSubType.Vegan;
                break;
        }
        if(selectedCategories.contains(cat)) {
            selectedCategories.remove(cat);
            tv.setTextAppearance(R.style.Category);
            tv.setBackgroundResource(R.drawable.item_category);
         }
        else {
            selectedCategories.add(cat);
            tv.setTextAppearance(R.style.CategorySelected);
            tv.setBackgroundResource(R.drawable.item_category_selected);
        }
    }

    protected void populateAllRestaurant() {
        if(RestaurantController.getInstance().getIsPopulated())
            return;
        ArrayList<Restaurant> restList = new ArrayList<>();
        HashMap<Restaurant.RestSubType, ArrayList<Restaurant>> hashRestList = new HashMap<>();

        try {
            InputStream is = getResources().openRawResource(R.raw.json_rests);
            Writer writer = new StringWriter();
            char[] buffer = new char[1024];

            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }

            String jsonString = writer.toString();

            JSONArray jArr = new JSONArray(jsonString);

            for(int i=0; i<jArr.length(); i++) {
                JSONObject jObj = jArr.getJSONObject(i);

                String name;
                Restaurant.RestSubType[] subTypes;
                String address;
                String pic;
                String lat;
                String lng;



                name = jObj.getString(JSON_NAME_TAG);
                subTypes = Restaurant.RestSubType.findAllSubTypes(jObj.getString(JSON_SUB_TYPE_TAG));
                if(!jObj.has(JSON_ADDRESS_TAG)) {
                    int x = 0;
                    x++;
                    continue;
                }
                address = jObj.getString(JSON_ADDRESS_TAG);
                pic = jObj.getString(JSON_PIC_TAG);
                final int id = getResources().getIdentifier(pic, "drawable", getPackageName());
                lat = jObj.getString(JSON_LAT_TAG);
                lng = jObj.getString(JSON_LONG_TAG);


                Restaurant rest = new Restaurant(i+1, name, null, subTypes, address,
                        Double.parseDouble(lat), Double.parseDouble(lng), id);

                restList.add(rest);

                for(Restaurant.RestSubType type: rest.subType) {
                    if(!hashRestList.containsKey(type))
                        hashRestList.put(type, new ArrayList<Restaurant>());
                    hashRestList.get(type).add(rest);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.json_error), Toast.LENGTH_LONG).show();
        }
        RestaurantController.getInstance().populateRestaurants(restList, hashRestList);
    }
}
