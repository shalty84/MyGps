package com.example.administrator.mygps.Adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.mygps.Fragments.Roads;
import com.example.administrator.mygps.R;
import com.example.administrator.mygps.Type.Track;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by shalty on 11/01/2017.
 */
public class RoadsListAdpter extends RecyclerView.Adapter<RoadsListAdpter.CustomViewHolder> {
        private List<Track> feedItemList;
        private Context mContext;

        public RoadsListAdpter(Context context, List<Track> feedItemList) {
            this.feedItemList = feedItemList;
            this.mContext = context;
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, null);
            CustomViewHolder viewHolder = new CustomViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
            Track feedItem = feedItemList.get(i);

            //Render image using Picasso library
            if (!TextUtils.isEmpty(feedItem.getThumbnail())) {
                Picasso.with(mContext).load(feedItem.getThumbnail())
                        .error(R.drawable.ic_menu_gallery)
                        .placeholder(R.drawable.ic_menu_gallery)
                        .into(customViewHolder.imageView);
            }

            //Setting text view title
            customViewHolder.textView.setText(i+"");
        }

        @Override
        public int getItemCount() {
            return (null != feedItemList ? feedItemList.size() : 0);
        }

        class CustomViewHolder extends RecyclerView.ViewHolder {
            protected ImageView imageView;
            protected TextView textView;

            public CustomViewHolder(View view) {
                super(view);
                this.imageView = (ImageView) view.findViewById(R.id.thumbnail);
                this.textView = (TextView) view.findViewById(R.id.title);
            }
        }
    }
