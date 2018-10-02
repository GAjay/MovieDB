package com.themoviedb.adapters;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.themoviedb.R;
import com.themoviedb.apis.entity.responses.Cast;
import com.themoviedb.glide.GlideApp;
import com.themoviedb.glide.GlideRequest;
import com.themoviedb.glide.GlideRequests;

import java.util.ArrayList;
import java.util.List;

import static com.themoviedb.apis.retrofit.AppConstants.IMAGES_BASE_URL;

public class CastAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Cast> itemsData = new ArrayList<>();
    private GlideRequest<Drawable> requestBuilder;

    private Activity activity;

    public CastAdapter(List<Cast> itemsData, Activity activity) {
        this.itemsData = itemsData;
        this.activity = activity;
        GlideRequests requestManager = GlideApp.with(activity);
        requestBuilder = requestManager.asDrawable();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, null);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final CastAdapter.ViewHolder viewHolder = (CastAdapter.ViewHolder) holder;

        if (null != itemsData.get(position).getProfilePath()) {
            requestBuilder.clone()
                    .load(IMAGES_BASE_URL + itemsData.get(position).getProfilePath().replace("/", ""))
                    .placeholder(R.drawable.ic_user)
                    .error(R.drawable.ic_user)
                    .transform(new RoundedCorners(100))
                    .into(viewHolder.imgViewIcon);

            GlideApp.with(activity).load(IMAGES_BASE_URL + itemsData.get(position).getProfilePath().replace("/", ""))
                    .preload();
        }
        else{
            viewHolder.imgViewIcon.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_user));
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgViewIcon;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            imgViewIcon = itemLayoutView.findViewById(R.id.ivCasts);
        }
    }


    @Override
    public int getItemCount() {
        return (itemsData.size() > 10) ? 10 : itemsData.size();
    }
}