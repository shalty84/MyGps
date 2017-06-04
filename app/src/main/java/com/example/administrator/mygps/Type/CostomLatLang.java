package com.example.administrator.mygps.Type;

import java.util.ArrayList;

/**
 * Created by shalty on 14/01/2017.
 */

public class CostomLatLang {
   private Double latitude;
    private Double longitude;

    public CostomLatLang() {}
    public CostomLatLang(Double latitude,Double longitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }



}
