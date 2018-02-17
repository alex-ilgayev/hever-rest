package com.alex.heverrest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class FrontActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    public static final String TAG = "hever-rest";
    public static final String PREFS = "com.alex.heverrest";
    public static final String PREF_LAST_UPDATED_TIMESTAMP = "timestamp";
    public static final String PREF_DATA = "data";

    private static final String JSON_NAME_TAG = "name";
    private static final String JSON_SUB_TYPE_TAG = "sub_type";
    private static final String JSON_ADDRESS_TAG = "address";
    private static final String JSON_KOSHER_TAG = "kosher";
    private static final String JSON_PIC_TAG = "pic";
    private static final String JSON_LAT_TAG = "lat";
    private static final String JSON_LONG_TAG = "long";

    public final static int MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 1;
    public final static int MY_PERMISSION_REQUEST_FINE_LOCATION = 2;

    TextView tvCatAsian;
    TextView tvCatBar;
    TextView tvCatCoffee;
    TextView tvCatDairy;
    TextView tvCatFish;
    TextView tvCatItalian;
    TextView tvCatFrench;
    TextView tvCatMeat;
    TextView tvCatMediterranean;
    TextView tvCatMexican;
    TextView tvCatMiddleEastern;
    TextView tvCatSandwiches;
    TextView tvCatSushi;
    TextView tvCatVegan;
    TextView tvCatSweets;
    ArrayList<TextView> mCategoryList = new ArrayList<>();
    ProgressBar pbDatabaseUpdate;
    Button btnSearchCategory;

    SharedPreferences mPrefs;

    Location mLastLocation;
    LocationRequest mLoationRequest = null;

    ArrayList<Restaurant.RestSubType> mSelectedCategories = new ArrayList();
    boolean mIsKosherSelected = false;

    private GoogleApiClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);

        tvCatAsian = (TextView) findViewById(R.id.tvCatAsian);
        tvCatBar = (TextView) findViewById(R.id.tvCatBar);
        tvCatCoffee = (TextView) findViewById(R.id.tvCatCoffee);
        tvCatDairy = (TextView) findViewById(R.id.tvCatDairy);
        tvCatFish = (TextView) findViewById(R.id.tvCatFish);
        tvCatItalian = (TextView) findViewById(R.id.tvCatItalian);
        tvCatFrench = (TextView) findViewById(R.id.tvCatFrench);
        tvCatMeat = (TextView) findViewById(R.id.tvCatMeat);
        tvCatMediterranean = (TextView) findViewById(R.id.tvCatMediterranean);
        tvCatMexican = (TextView) findViewById(R.id.tvCatMexican);
        tvCatMiddleEastern = (TextView) findViewById(R.id.tvCatMiddleEastern);
        tvCatSandwiches = (TextView) findViewById(R.id.tvCatSandwiches);
        tvCatSushi = (TextView) findViewById(R.id.tvCatSushi);
        tvCatVegan = (TextView) findViewById(R.id.tvCatVegan);
        tvCatSweets = (TextView) findViewById(R.id.tvCatSweets);
        pbDatabaseUpdate = (ProgressBar) findViewById(R.id.pbDatabaseUpdate);
        btnSearchCategory = (Button) findViewById(R.id.btnSearchCategory);

        // creating text view list so we can select them all, or deselect.
        mCategoryList.add(tvCatAsian);
        mCategoryList.add(tvCatBar);
        mCategoryList.add(tvCatCoffee);
        mCategoryList.add(tvCatDairy);
        mCategoryList.add(tvCatFish);
        mCategoryList.add(tvCatItalian);
        mCategoryList.add(tvCatFrench);
        mCategoryList.add(tvCatMeat);
        mCategoryList.add(tvCatMediterranean);
        mCategoryList.add(tvCatMexican);
        mCategoryList.add(tvCatMiddleEastern);
        mCategoryList.add(tvCatSandwiches);
        mCategoryList.add(tvCatSushi);
        mCategoryList.add(tvCatVegan);
        mCategoryList.add(tvCatSweets);

        // setting onclick for all categories.
        tvCatAsian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSelectedCategory(v);
            }
        });
        tvCatBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSelectedCategory(v);
            }
        });
        tvCatCoffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSelectedCategory(v);
            }
        });
        tvCatDairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSelectedCategory(v);
            }
        });
        tvCatFish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSelectedCategory(v);
            }
        });
        tvCatItalian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSelectedCategory(v);
            }
        });
        tvCatFrench.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSelectedCategory(v);
            }
        });
        tvCatMeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSelectedCategory(v);
            }
        });
        tvCatMediterranean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSelectedCategory(v);
            }
        });
        tvCatMexican.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSelectedCategory(v);
            }
        });
        tvCatMiddleEastern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSelectedCategory(v);
            }
        });
        tvCatSandwiches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSelectedCategory(v);
            }
        });
        tvCatSushi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSelectedCategory(v);
            }
        });
        tvCatVegan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSelectedCategory(v);
            }
        });
        tvCatSweets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSelectedCategory(v);
            }
        });

        // kosher button is a bit different because it is not a category.
        findViewById(R.id.tvKosher).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView tv = (TextView) v;
                if(mIsKosherSelected) {
                    mIsKosherSelected = false;
                    tv.setTextAppearance(R.style.Category);
                    tv.setBackgroundResource(R.drawable.item_category);
                }
                else {
                    mIsKosherSelected = true;
                    tv.setTextAppearance(R.style.CategorySelected);
                    tv.setBackgroundResource(R.drawable.item_category_selected);
                }
            }
        });

        btnSearchCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectedCategories.size() == 0) {
                    Toast.makeText(FrontActivity.this, getString(R.string.no_categories), Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                Intent i = new Intent(FrontActivity.this, RestListActivity.class);
                i.putExtra(RestListActivity.extraFilter, mSelectedCategories);
                i.putExtra(RestListActivity.extraIsKosher, mIsKosherSelected);
                i.putExtra(RestListActivity.extraLocation, mLastLocation);
                startActivity(i);
            }
        });

        findViewById(R.id.btnSelectAll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Restaurant.RestSubType> list = Arrays.asList(Restaurant.RestSubType.values());
                mSelectedCategories.clear();
                mSelectedCategories.addAll(list);

                for(TextView tv: mCategoryList) {
                    tv.setTextAppearance(R.style.CategorySelected);
                    tv.setBackgroundResource(R.drawable.item_category_selected);
                }
            }
        });

        findViewById(R.id.btnDeSelectAll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedCategories.clear();

                for(TextView tv: mCategoryList) {
                    tv.setTextAppearance(R.style.Category);
                    tv.setBackgroundResource(R.drawable.item_category);
                }
            }
        });

        mPrefs = getSharedPreferences(PREFS, MODE_PRIVATE);

        /**
         * FLOW:
         *
         * is already populated ?
         *      yes
         *          finishing
         *      have cache?
         *          no
         *              retreiving remote database
         *              populating
         *              storing cache
         *          yes
         *              populating using cache
         *              retreiving date
         *              if newer?
         *                  yes
         *                      retreiving remote database
         *                      populating
         *                      storing cache
         *                  no
         *                      finishing
         */

        if(RestaurantController.getInstance().getIsPopulated()) {
            // happens when entering multiple timese without quitting application.
            Log.i(TAG, "already populated");
            pbDatabaseUpdate.setVisibility(View.INVISIBLE);
            btnSearchCategory.setVisibility(View.VISIBLE);
        } else {
            // first checking if update is needed according to timestamp key.
            // if no timestamp entry in shared preferences, means no update has been done.
            String lastUpdate = mPrefs.getString(PREF_LAST_UPDATED_TIMESTAMP, null);
            String data = mPrefs.getString(PREF_DATA, null);
            if(lastUpdate != null && data != null) {
                // populating local data
                Log.i(TAG, "populating local data");

                Type type = new TypeToken<List<HashMap<String, String>>>(){}.getType();
                ArrayList rests = new Gson().fromJson(data, type);
                populateAllRestaurants(rests);
            } else {
                Log.i(TAG, "first usage");
                Toast.makeText(this, getString(R.string.database_first_update),
                        Toast.LENGTH_SHORT).show();
            }

            new UpdateRestDatabaseTask(this).execute();
        }

        mClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
//                .addOnConnectionlFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(AppIndex.API).build();
    }

    private void populateAllRestaurants(ArrayList<HashMap<String, String>> data) {
        ArrayList<Restaurant> restList = new ArrayList<>();
        HashMap<Restaurant.RestSubType, ArrayList<Restaurant>> hashRestList = new HashMap<>();

        for(int i=0; i<data.size(); i++) {
            HashMap<String, String> obj = data.get(i);

            // rest data
            String name;
            Restaurant.RestSubType[] subTypes;
            boolean isKosher;
            String kosherType;
            String address;
            String pic;
            String lat;
            String lng;

            name = obj.get(JSON_NAME_TAG);
            subTypes = Restaurant.RestSubType.findAllSubTypes(obj.get(JSON_SUB_TYPE_TAG));

            if(obj.containsKey(JSON_KOSHER_TAG)) {
                isKosher = true;
                kosherType = obj.get(JSON_KOSHER_TAG);
                if(kosherType.equals(getString(R.string.kosher_without_permission)))
                    isKosher = false;
            }
            else {
                isKosher = false;
                kosherType = "";
            }

            if(!obj.containsKey(JSON_ADDRESS_TAG)) {
                int x = 0;
                x++;
                continue;
            }
            address = obj.get(JSON_ADDRESS_TAG);
            pic = obj.get(JSON_PIC_TAG);
            lat = obj.get(JSON_LAT_TAG);
            lng = obj.get(JSON_LONG_TAG);


            Restaurant rest = new Restaurant(i+1, name, null,
                    isKosher, kosherType,
                    subTypes, address,
                    Double.parseDouble(lat), Double.parseDouble(lng), pic);

            restList.add(rest);

            for(Restaurant.RestSubType type: rest.subType) {
                if(!hashRestList.containsKey(type))
                    hashRestList.put(type, new ArrayList<Restaurant>());
                hashRestList.get(type).add(rest);
            }
        }
        RestaurantController.getInstance().populateRestaurants(restList, hashRestList);

        Log.i(TAG, "finished populating restaurants");

        pbDatabaseUpdate.setVisibility(View.INVISIBLE);
        btnSearchCategory.setVisibility(View.VISIBLE);
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
        if(mSelectedCategories.contains(cat)) {
            mSelectedCategories.remove(cat);
            tv.setTextAppearance(R.style.Category);
            tv.setBackgroundResource(R.drawable.item_category);
         }
        else {
            mSelectedCategories.add(cat);
            tv.setTextAppearance(R.style.CategorySelected);
            tv.setBackgroundResource(R.drawable.item_category_selected);
        }
    }

//    protected void populateAllRestaurant() {
//        // checking if update needed, and updates.
//        new UpdateRestDatabaseTask().execute();
//
//        if(RestaurantController.getInstance().getIsPopulated())
//            return;
//        ArrayList<Restaurant> restList = new ArrayList<>();
//        HashMap<Restaurant.RestSubType, ArrayList<Restaurant>> hashRestList = new HashMap<>();
//
//        try {
//            InputStream is = getResources().openRawResource(R.raw.json_rests);
//            Writer writer = new StringWriter();
//            char[] buffer = new char[1024];
//
//            try {
//                Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                int n;
//                while ((n = reader.read(buffer)) != -1) {
//                    writer.write(buffer, 0, n);
//                }
//            } finally {
//                is.close();
//            }
//
//            String jsonString = writer.toString();
//
//            JSONArray jArr = new JSONArray(jsonString);
//
//            for(int i=0; i<jArr.length(); i++) {
//                JSONObject jObj = jArr.getJSONObject(i);
//
//                String name;
//                Restaurant.RestSubType[] subTypes;
//                boolean isKosher;
//                String kosherType;
//                String address;
//                String pic;
//                String lat;
//                String lng;
//
//
//
//                name = jObj.getString(JSON_NAME_TAG);
//
//                subTypes = Restaurant.RestSubType.findAllSubTypes(jObj.getString(JSON_SUB_TYPE_TAG));
//
//                if(jObj.has(JSON_KOSHER_TAG)) {
//                    isKosher = true;
//                    kosherType = jObj.getString(JSON_KOSHER_TAG);
//                    if(kosherType.equals(getString(R.string.kosher_without_permission)))
//                        isKosher = false;
//                }
//                else {
//                    isKosher = false;
//                    kosherType = "";
//                }
//
//                if(!jObj.has(JSON_ADDRESS_TAG)) {
//                    int x = 0;
//                    x++;
//                    continue;
//                }
//                address = jObj.getString(JSON_ADDRESS_TAG);
//                pic = jObj.getString(JSON_PIC_TAG);
//                final int id = getResources().getIdentifier(pic, "drawable", getPackageName());
//                lat = jObj.getString(JSON_LAT_TAG);
//                lng = jObj.getString(JSON_LONG_TAG);
//
//
//                Restaurant rest = new Restaurant(i+1, name, null,
//                        isKosher, kosherType,
//                        subTypes, address,
//                        Double.parseDouble(lat), Double.parseDouble(lng), id);
//
//                restList.add(rest);
//
//                for(Restaurant.RestSubType type: rest.subType) {
//                    if(!hashRestList.containsKey(type))
//                        hashRestList.put(type, new ArrayList<Restaurant>());
//                    hashRestList.get(type).add(rest);
//                }
//            }
//        } catch(Exception e) {
//            e.printStackTrace();
//            Toast.makeText(this, getString(R.string.json_error), Toast.LENGTH_LONG).show();
//        }
//        RestaurantController.getInstance().populateRestaurants(restList, hashRestList);
//    }

    private class UpdateRestDatabaseTask extends AsyncTask<Void, Void, Void> {
        private final Context _ctx;

        UpdateRestDatabaseTask(Context ctx) {
            this._ctx = ctx;
        }

        /**
         * checking if there is newer timestamp in remote server.
         * if remote date is newer then local date, or local date doesn't exists,
         * retrieving remote database, and storing in cache.
         */
        protected Void doInBackground(Void... urls) {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();

            // first checking if update is needed accoring to timestamp key.
            // if no timestamp entry in shared preferences, means no update has been done.
            final String lastUpdate = mPrefs.getString(PREF_LAST_UPDATED_TIMESTAMP, null);
            if(lastUpdate == null) {
                Log.i(TAG, "first time updating database");
                getRemoteDatabase();
                return null;
            }

            DatabaseReference ref = database.getReference("timestamp");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Date dateRemote, dateLocal;
                    String dateStr = (String) dataSnapshot.getValue();
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    try {
                        dateRemote = format.parse(dateStr);
                        dateLocal = format.parse(lastUpdate);
                        if(dateRemote.compareTo(dateLocal) > 0) {
                            // new update is existed
                            Log.i(TAG, "newer database existing in remote server, updating.");
                            getRemoteDatabase();
                        } else {
                            // populating the cache data stored in application.
                            Log.i(TAG, "no new data in remote server");
                            String data = mPrefs.getString(PREF_DATA, null);
                            if(data == null) {
                                Log.e(TAG, "no data error");
                                return;
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Log.w(TAG, e.getMessage());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, databaseError.toException());
                }
            });

            return null;
        }

        private void getRemoteDatabase() {
//            mPrefs.edit().putString(PREF_LAST_UPDATED_TIMESTAMP, "15/02/2018").commit();
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("rests");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList data = (ArrayList) dataSnapshot.getValue();
                    Log.i(TAG, "got the remote database. now populating");
                    populateAllRestaurants(data);
                    storeCache(data);

                    // posting success message
                    Handler handler = new Handler(FrontActivity.this.getMainLooper());

                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(FrontActivity.this, getString(R.string.database_update_success),
                                    Toast.LENGTH_SHORT).show();
                        }
                    };
                    handler.post(runnable);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, databaseError.toException());
                }
            });
        }

        /**
         * after a new database has been retrieved, it is saved in cache for later use
         * saving the array as json
         * saving current date.
         * @param data - the data to be saved
         */
        private void storeCache(ArrayList data) {
            String strData = new Gson().toJson(data);

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String strDate = format.format(Calendar.getInstance().getTime());

            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString(PREF_LAST_UPDATED_TIMESTAMP, strDate);
            editor.putString(PREF_DATA, strData);
            editor.commit();
            Log.i(TAG, "new data is stored");
        }
    }
}
