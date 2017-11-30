package com.alex.heverrest.Controller;

import com.alex.heverrest.Model.Restaurant;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by alexi on 11/30/2017.
 */

public class RestaurantController {
    private static final RestaurantController _ins = new RestaurantController();

    ArrayList<Restaurant> _rests = new ArrayList<>();
    HashMap<Restaurant.RestSubType, ArrayList<Restaurant>> _hashRestList = new HashMap<>();
    boolean _isPopulated = false;

    public static RestaurantController getInstance() {
        return _ins;
    }

    private RestaurantController() {

    }

    public boolean getIsPopulated() {
        return _isPopulated;
    }

    public void populateRestaurants(ArrayList rests, HashMap hashRestList) {
        this._rests = rests;
        this._hashRestList = hashRestList;
        _isPopulated = true;
    }

    public ArrayList getRestList() {
        return _rests;
    }

    public HashMap<Restaurant.RestSubType, ArrayList<Restaurant>> getHashRestList() {
        return _hashRestList;
    }
}
