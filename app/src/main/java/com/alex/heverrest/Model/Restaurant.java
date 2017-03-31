package com.alex.heverrest.Model;

import android.location.Location;

import com.alex.heverrest.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Alex on 1/20/2017.
 */

public class Restaurant {

    public enum RestType {
        Caffee,
        Restaurant,
        Bar,
        Icecream,
        Gourmet,
        Pastry,
        Bakery,
    }

    public enum RestSubType {
        Italian("איטלקי"),
        Asian("אסייתי"),
        Mexican("מקסיקני"),
        Sushi("סושי"),
        French("צרפתי"),
        Vegan("צמחוני-טבעוני"),
        Sweets("מתוקים-קינוחים"),
        Meat("בשרים"),
        Coffee("בית קפה"),
        Sandwiches("כריכים-סלטים"),
        Dairy("חלבי"),
        Fish("דגים"),
        Bar("בר-פאב"),
        Mediterranean("ים תיכוני"),
        MiddleEastern("מזרחי"),;

        private String type;

        RestSubType(String type) {
            this.type = type;
        }

        /**
         * receives string in format בשרי | אסייתי and returns subtypes array.
         */
        public static RestSubType[] findAllSubTypes(String str) {
            String[] splitted = str.split(" \\| ");
            ArrayList<RestSubType> typeArr = new ArrayList<>();
            for(String s: splitted) {
                typeArr.add(RestSubType.findRestSubType(s));
            }
            return (RestSubType[])typeArr.toArray(new RestSubType[0]);
        }

        /**
         * receives string in format אסייתי with single subtype and return and type.
         */
        public static RestSubType findRestSubType(String type) {
            switch(type) {
                case "איטלקי":
                    return Italian;
                case "אסייתי":
                    return Asian;
                case "מקסיקני":
                    return Mexican;
                case "סושי":
                    return Sushi;
                case "צרפתי":
                    return French;
                case "צמחוני-טבעוני":
                    return Vegan;
                case "מתוקים-קינוחים":
                    return Sweets;
                case "בשרים":
                    return Meat;
                case "בית קפה":
                    return Coffee;
                case "כריכים-סלטים":
                    return Sandwiches;
                case "חלבי":
                    return Dairy;
                case "דגים":
                    return Fish;
                case "בר-פאב":
                    return Bar;
                case "ים תיכוני":
                    return Mediterranean;
                default:
                    return MiddleEastern;
            }
        }

        @Override
        public String toString() {
            return this.type;
        }
    }

    public int id;
    public String name;
    public RestType[] type;
    public RestSubType[] subType;
    public String address;
    public double lat;
    public double lng;
    public int openEmza;
    public int closeEmza;
    public int openShishi;
    public int closeShishi;
    public int openShabat;
    public int closeShabar;
    public String phoneNumber;
    public int picRes;

    public Restaurant(int id, String name, RestType[] restTypes, RestSubType[] restSubTypes,
                      String address, double lat, double lng, int picRes) {
        this.id = id;
        this.name = name;
        this.type = restTypes;
        this.subType = restSubTypes;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.picRes = picRes;
    }

    public float distanceTo(Location l) {
        Location rest = new Location("rest");
        rest.setLatitude(this.lat);
        rest.setLongitude(this.lng);
        return rest.distanceTo(l)/1000.0f;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Restaurant that = (Restaurant) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    public static Restaurant r1 = new Restaurant(1,"BEIR GARTEN",
            new RestType[]{RestType.Bar},
            new RestSubType[]{RestSubType.Bar},
            "תל אביב - יפו, התערוכה 3 ביתן 16",
            32.096483,
            34.773274,
            R.drawable.r1);

    public static Restaurant r2 = new Restaurant(2,"Black",
            new RestType[]{RestType.Restaurant},
            new RestSubType[]{RestSubType.Meat},
            "תל אביב - יפו, הברזל 23",
            32.108941,
            34.839759,
            R.drawable.r2);

    public static Restaurant r3 = new Restaurant(3,"Bread Story",
            new RestType[]{RestType.Caffee},
            new RestSubType[]{RestSubType.Coffee},
            "תל אביב - יפו, דיזינגוף 88",
            32.076959,
            34.774479,
            R.drawable.r3);

    public static Restaurant r4 = new Restaurant(4,"BROWN",
            new RestType[]{RestType.Caffee, RestType.Restaurant},
            new RestSubType[]{RestSubType.Italian, RestSubType.Dairy, RestSubType.Sweets},
            "תל אביב - יפו, ניסים אלוני 10",
            32.088866,
            34.797339,
            R.drawable.r4);

    public static Restaurant r5 = new Restaurant(5,"Chop Chop",
            new RestType[]{RestType.Restaurant},
            new RestSubType[]{RestSubType.Asian},
            "תל אביב - יפו, אבן גבירול 20",
            32.073648,
            34.781894,
            R.drawable.r5);

    public static ArrayList<Restaurant> restList = new ArrayList();
    public static HashMap<RestSubType, ArrayList<Restaurant>> hashRestList = new HashMap<>();
}

