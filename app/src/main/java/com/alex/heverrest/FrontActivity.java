package com.alex.heverrest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alex.heverrest.Model.Restaurant;

import java.util.ArrayList;

public class FrontActivity extends AppCompatActivity {



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
}
