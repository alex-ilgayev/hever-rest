package com.alex.heverrest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alex.heverrest.Model.Restaurant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

import static com.alex.heverrest.Model.Restaurant.hashRestList;
import static com.alex.heverrest.Model.Restaurant.restList;

public class FrontActivity extends AppCompatActivity {

    private static final String JSON_NAME_TAG = "name";
    private static final String JSON_SUB_TYPE_TAG = "sub_type";
    private static final String JSON_ADDRESS_TAG = "address";


    ArrayList selectedCategories = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);

        findViewById(R.id.btnChargeBlue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FrontActivity.this, ChargeActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.btnLookAround).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FrontActivity.this, RestListActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.btnSearchCategory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FrontActivity.this, RestListActivity.class);
                i.putExtra(RestListActivity.extraFilter, selectedCategories);
                startActivity(i);
            }
        });

        populateAllRestaurant();
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

                name = jObj.getString(JSON_NAME_TAG);
                subTypes = Restaurant.RestSubType.findAllSubTypes(jObj.getString(JSON_SUB_TYPE_TAG));
                address = jObj.getString(JSON_ADDRESS_TAG);
                Restaurant rest = new Restaurant(i+1, name, null, subTypes, address, 32.0, 35.0, R.drawable.r1);
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

    }
}
