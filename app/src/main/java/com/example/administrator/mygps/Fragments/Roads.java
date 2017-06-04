package com.example.administrator.mygps.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.administrator.mygps.Adpter.RoadsListAdpter;
import com.example.administrator.mygps.Interfaces.FirebaseHlper;
import com.example.administrator.mygps.R;
import com.example.administrator.mygps.Type.Track;
import com.example.administrator.mygps.Type.User;
import com.example.administrator.mygps.Utility.UtilFirebase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Roads extends Fragment implements FirebaseHlper{

    RecyclerView mRecyclerView;
    ProgressBar progressBar;
    private ArrayList<Track> trackArrayList = new ArrayList<>();
    private RoadsListAdpter adapter;


    public Roads() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UtilFirebase.getUserTrack(User.getUserInstance().getUid(),this);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_roads, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);



        return view;
    }

    @Override
    public void getTrakList(ArrayList<Track> tracks) {
        if (!trackArrayList.isEmpty())
        trackArrayList.clear();
        trackArrayList = tracks;
        adapter = new RoadsListAdpter(getContext(), trackArrayList);
        mRecyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
    }
}
