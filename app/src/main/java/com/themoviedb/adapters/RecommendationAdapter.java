package com.themoviedb.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.themoviedb.R;
import com.themoviedb.activities.MovieDetailActivity;
import com.themoviedb.apis.entity.responses.RecommendateResult;
import com.themoviedb.glide.GlideApp;
import com.themoviedb.glide.GlideRequest;
import com.themoviedb.glide.GlideRequests;

import java.util.ArrayList;
import java.util.List;

import static com.themoviedb.apis.retrofit.AppConstants.IMAGES_BASE_URL;

public class RecommendationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<RecommendateResult> itemsData = new ArrayList<>();
    private GlideRequest<Drawable> requestBuilder;

    private Activity activity;

    public RecommendationAdapter(List<RecommendateResult> itemsData, Activity activity) {
        this.itemsData = itemsData;
        this.activity = activity;
        GlideRequests requestManager = GlideApp.with(activity);
        requestBuilder = requestManager.asDrawable();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_movie_recommd, null);

        RecommendationAdapter.ViewHolder viewHolder = new RecommendationAdapter.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final RecommendationAdapter.ViewHolder viewHolder = (RecommendationAdapter.ViewHolder) holder;
        if (null != itemsData.get(position).getBackdropPath()) {
            requestBuilder.clone()
                    .load(IMAGES_BASE_URL + itemsData.get(position).getBackdropPath().replace("/", ""))
                    .placeholder(R.drawable.thumb_place_holder)
                    .error(R.drawable.ic_user)
                    .transform(new RoundedCorners(10))
                    .into(viewHolder.ivThumbnail);

            GlideApp.with(activity).load(IMAGES_BASE_URL + itemsData.get(position).getBackdropPath().replace("/", ""))
                    .preload();
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, MovieDetailActivity.class);
                    intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ID, itemsData.get(position).getId());
                    intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_NAME, itemsData.get(position).getTitle());

                    activity.startActivity(intent);
                    ((MovieDetailActivity)activity).showLoadingProgress();
                    ((MovieDetailActivity)activity).cleanPresenter();
                }
            });
        }
        else{
            viewHolder.ivThumbnail.setImageDrawable(activity.getResources().getDrawable(R.drawable.thumb_place_holder));
        }
        viewHolder.tvName.setText(itemsData.get(position).getTitle());
        viewHolder.tvDescription.setText(itemsData.get(position).getOverview());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView tvName;
        public TextView tvDescription;
        public ImageView ivThumbnail;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            this.ivThumbnail = itemLayoutView.findViewById(R.id.ivThumbnail);
            this.tvName = itemLayoutView.findViewById(R.id.tvName);
            this.tvDescription = itemLayoutView.findViewById(R.id.tvDescription);
        }
    }


    @Override
    public int getItemCount() {
        return (itemsData.size() > 10) ? 10 : itemsData.size();
    }
}