package com.alex.heverrest;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alex.heverrest.Model.Restaurant;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.vision.text.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RestListActivity extends AppCompatActivity  {

    public final static String extraFilter = "filter";
    public final static String extraLocation = "location";

    ArrayList<Restaurant.RestSubType> mCatFilter = null;
    Location mLocation;
    ListView mLvRests;
    ArrayList<Restaurant> mRests = new ArrayList<>();
    RestAdapter mAdapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent().getExtras().get(extraFilter) != null) {
            mCatFilter = (ArrayList<Restaurant.RestSubType>) getIntent().getExtras().get(extraFilter);
        }

        if(getIntent().getExtras().get(extraLocation) != null) {
            mLocation = (Location) getIntent().getExtras().get(extraLocation);
        }

        checkLocationValidity();


        mLvRests = (ListView) findViewById(R.id.lvRests);

        mRests = populateRestaurants();

        mAdapter = new RestAdapter(this, mRests);

        mLvRests.setAdapter(mAdapter);

        mLvRests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Restaurant r = mRests.get(position);
                Uri uri = Uri.parse("geo:0,0?q=" + Uri.encode(r.address));
                //Uri uri = Uri.parse("geo:0,0?q=" + r.lat + "," + r.lng);
                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                i.setPackage("com.google.android.apps.maps");
                startActivity(i);
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

        mClient = new GoogleApiClient.Builder(this)
                .addApi(AppIndex.API).build();
    }

    private void checkLocationValidity() {
        if(mLocation == null) {
            Toast.makeText(this, getString(R.string.location_error), Toast.LENGTH_LONG).show();
            return;
        }

        if(!mLocation.hasAccuracy() || mLocation.getAccuracy() > 150.0) {
            Toast.makeText(this, getString(R.string.location_accuracy_error), Toast.LENGTH_LONG).show();
            return;
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

    public ArrayList<Restaurant> populateRestaurants() {
        Set<Restaurant> restsSet = new HashSet<>();

        if(mCatFilter != null) {
            for(Restaurant.RestSubType type: mCatFilter){
                for(Restaurant r: Restaurant.hashRestList.get(type))
                    restsSet.add(r);
            }
        }

        ArrayList arr = new ArrayList<>(restsSet);
        Collections.sort(arr, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                if(mLocation == null)
                    return 0;

                Restaurant r1 = (Restaurant)o1;
                Restaurant r2 = (Restaurant)o2;

                float d1 = r1.distanceTo(mLocation);
                float d2 = r2.distanceTo(mLocation);

                return d1 < d2 ? -1 : 1;
            }
        });
        return arr;
    }

    class RestAdapter extends ArrayAdapter<Restaurant> {

        public RestAdapter(Context context, List<Restaurant> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Restaurant rest = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_rest, parent, false);
            }
            // Lookup view for data population
            TextView tvName = (TextView) convertView.findViewById(R.id.tvRestName);
            TextView tvAddress = (TextView) convertView.findViewById(R.id.tvRestAddress);
            TextView tvRestSubType = (TextView) convertView.findViewById(R.id.tvRestSubType);
            ImageView ivPic = (ImageView) convertView.findViewById(R.id.ivPic);
            TextView tvDistance = (TextView) convertView.findViewById(R.id.tvDistance);

            // Populate the data into the template view using the data object
            tvName.setText(rest.name);
            tvAddress.setText(rest.address);
            ivPic.setImageResource(rest.picRes);
            if(mLocation != null)
                tvDistance.setText(String.format("%.1f",rest.distanceTo(mLocation)) + " ק\"מ");

            int i = 0;
            String s = "";
            for(Restaurant.RestSubType type: rest.subType) {
                if(i != 0)
                    s += " | ";
                s += type;
                i++;
            }
            tvRestSubType.setText(s);

            // Return the completed view to render on screen
            return convertView;

        }

    }
}
