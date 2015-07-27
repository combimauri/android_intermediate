package com.example.mauricioarce.bankmap;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Mauricio Arce on 26/07/2015.
 */
public class Agency {

    private String name;
    private String bankName;
    private LatLng location;

    public Agency(String name, String bankName, LatLng location) {
        this.name = name;
        this.bankName = bankName;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getBankName() {
        return bankName;
    }

    public LatLng getLocation() {
        return location;
    }

}
