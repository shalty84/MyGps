package com.example.administrator.mygps.Type;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Administrator on 27/11/2016.
 */

public class Track {

    private ArrayList<LatLng> track;
    private ArrayList<CostomLatLang> costomtrack;
    private String name;
    private String trackId;
    private String imageUrl;
    private String title;
    private String thumbnail;

    public Track(){}

    public Track(ArrayList<LatLng> track, String name) {
        this.track = track;
        this.name = name;
        this.trackId = trackId;
    }
    public Track( String name,ArrayList<CostomLatLang> track) {
        this.costomtrack = track;
        this.name = name;
        this.trackId = trackId;
    }

    public ArrayList<LatLng> getTrack() {
        return track;
    }

    public String getName() {
        return name;
    }

    public void setTrack(ArrayList<LatLng> track) {
        this.track = track;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addCoordinate(LatLng Coordinate)
    {
        track.add(Coordinate);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public ArrayList<CostomLatLang> getCostomtrack() {
        return costomtrack;
    }

    public void setCostomtrack(ArrayList<CostomLatLang> costomtrack) {
        this.costomtrack = costomtrack;
    }



    public Track convertTrack(Track track)
    {
        ArrayList<CostomLatLang> costomLatLangs = new ArrayList<>();

        for (LatLng item:track.getTrack())
        {
            costomLatLangs.add(new CostomLatLang(item.latitude,item.longitude));
        }
        Track costomTrack = new Track(track.name,costomLatLangs);


        return costomTrack;
    }
    public Track unConvertTrack(Track costomtrack)
    {
        ArrayList<LatLng> LatLangs = new ArrayList<>();

        for (CostomLatLang item:costomtrack.costomtrack)
        {
            LatLangs.add(new LatLng(item.getLatitude(),item.getLongitude()));
        }
        Track track = new Track(LatLangs,costomtrack.name);


        return track;
    }

}
