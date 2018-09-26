package com.themoviedb.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.themoviedb.R;
import com.themoviedb.apis.entity.responses.ResultsItem;
import com.themoviedb.apis.retrofit.AppUrls;
import com.themoviedb.glide.GlideApp;
import com.themoviedb.glide.GlideRequest;
import com.themoviedb.glide.GlideRequests;

import java.util.ArrayList;
import java.util.List;

public class VideoListingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ResultsItem> itemsData = new ArrayList<>();
    private GlideRequest<Drawable> requestBuilder;

    private Activity activity;

    public VideoListingAdapter(List<ResultsItem> itemsData, Activity activity) {
        this.itemsData = itemsData;
        this.activity = activity;
        GlideRequests requestManager = GlideApp.with(activity);
        requestBuilder = requestManager.asDrawable();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video_layout, null);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final VideoListingAdapter.ViewHolder viewHolder = (VideoListingAdapter.ViewHolder) holder;

        if (itemsData.get(position).getSite().equalsIgnoreCase("YouTube")) {

            viewHolder.itemView.setVisibility(View.VISIBLE);
            requestBuilder.clone()
                    .load(AppUrls.IMG_YouTUBE + itemsData.get(position).getKey() + AppUrls.IMG_YOUTUBE_TYPE)
                    .placeholder(R.drawable.thumb_place_holder)
                    .error(R.drawable.thumb_place_holder)
                    .transform(new RoundedCorners(5))
                    .into(viewHolder.iThumb);

            GlideApp.with(activity).load(AppUrls.IMG_YouTUBE + itemsData.get(position).getKey() + AppUrls.IMG_YOUTUBE_TYPE)
                    .preload();
            //playing video
            viewHolder.iVideosPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(AppUrls.YOUTUBELINK + itemsData.get(position).getKey())));
                    Log.i("Video", "Video Playing....");
                }
            });
            viewHolder.tVideo.setText(itemsData.get(position).getName());
        } else {
            viewHolder.itemView.setVisibility(View.GONE);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iThumb;

        public ImageView iVideosPlay;

        public TextView tVideo;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            iThumb = (ImageView) itemLayoutView.findViewById(R.id.ivThumbVideo);
            iVideosPlay = (ImageView) itemLayoutView.findViewById(R.id.ivVideoPlay);
            tVideo = (TextView) itemLayoutView.findViewById(R.id.tvVideoname);
        }
    }


    @Override
    public int getItemCount() {
        return (itemsData.size() > 5) ? 5 : itemsData.size();
    }
}