package com.themoviedb.adapters;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.themoviedb.R;
import com.themoviedb.apis.request.DiscoveryRequest;
import com.themoviedb.glide.GlideApp;
import com.themoviedb.glide.GlideRequest;
import com.themoviedb.glide.GlideRequests;
import com.themoviedb.models.MovieModel;
import com.themoviedb.view.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static com.themoviedb.apis.retrofit.AppUrls.LARGER_IMAGES_BASE_URL;

/**
 * Created by Ajay Kumar Maheshwari .
 */

public class MovieListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ListPreloader.PreloadSizeProvider<MovieModel>,
        ListPreloader.PreloadModelProvider<MovieModel> {

    private List<MovieModel> movies = new ArrayList<>();

    private final LayoutInflater inflater;

    private Activity activity;

    private OnMovieSelectionListener movieSelectionListener;

    private GlideRequest<Drawable> requestBuilder;

    private int[] actualDimensions;
    private boolean isgrid;
    private String query;

    public MovieListAdapter(Activity activity) {

        this.activity = activity;
        inflater = LayoutInflater.from(activity);

        GlideRequests requestManager = GlideApp.with(activity);
        requestBuilder = requestManager.asDrawable();

        setHasStableIds(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view;
        if (isgrid == false) {
            view = inflater.inflate(R.layout.row_movie, parent, false);
        } else {
            view = inflater.inflate(R.layout.row_movie_grid, parent, false);
        }
        if (actualDimensions == null) {
            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    if (actualDimensions == null) {
                        actualDimensions = new int[]{view.getWidth(), view.getHeight()};
                    }
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    return true;
                }
            });
        }

        return (isgrid)?new MovieViewHolderGrid(view):new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {


        final MovieModel model = movies.get(position);
        if (!isgrid) {
            final MovieViewHolder holder = (MovieViewHolder) viewHolder;

            holder.tvName.setText(model.getTitle());
            if (!TextUtils.isEmpty(model.getOverview())) {
                holder.tvDescription.setText(model.getOverview());
            } else {
                holder.tvDescription.setText(activity.getString(R.string.nooverview));
            }

            setImageInView(model, holder.ivThumbnail);

            String releasedOn = model.getReleaseDate();
            if (releasedOn != null && releasedOn.contains("-")) {
                holder.tvYear.setText(Utils.getYear(releasedOn));
            } else {
                holder.tvYear.setText(R.string.unknown);
            }
            if (0 == model.getVoteAverage()) {
                holder.tvVotes.setText("0");
            } else {
                holder.tvVotes.setText(String.valueOf(model.getVoteAverage()));
            }

            if(TextUtils.isEmpty(query)){
                holder.tvYear.setTextColor(activity.getResources().getColor(R.color.black));
            }
            else{

                holder.tvYear.setTextColor((Utils.getYear(releasedOn).equals(String.valueOf(Calendar.getInstance().get(Calendar.YEAR))))?activity.getResources().getColor(R.color.red):activity.getResources().getColor(R.color.black));
            }
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (movieSelectionListener != null) {
                        movieSelectionListener.onMovieSelected(model, holder.ivThumbnail, position);
                    }
                }
            });
        } else {
            final MovieViewHolderGrid holderGrid = (MovieViewHolderGrid) viewHolder;
            setImageInView(model, holderGrid.ivThumbGrid);
            holderGrid.tvNameGrid.setText(model.getTitle());
            if (0 == model.getVoteAverage()) {
                holderGrid.tvVotesGrid.setText("0");
            } else {
                holderGrid.tvVotesGrid.setText(String.valueOf(model.getVoteAverage()));
            }

            String releasedOn = model.getReleaseDate();
            if (releasedOn != null && releasedOn.contains("-")) {
                holderGrid.tvYearGrid.setText(Utils.getYear(releasedOn));
            } else {
                holderGrid.tvYearGrid.setText(R.string.unknown);
            }

            if(TextUtils.isEmpty(query)){
                holderGrid.tvYearGrid.setTextColor(activity.getResources().getColor(R.color.black));
            }
            else{

                holderGrid.tvYearGrid.setTextColor((Utils.getYear(releasedOn).equals(String.valueOf(Calendar.getInstance().get(Calendar.YEAR))))?activity.getResources().getColor(R.color.red):activity.getResources().getColor(R.color.black));
            }

        }
    }

    private void setImageInView(MovieModel model, ImageView imageView) {
        requestBuilder.clone()
                .load((null != model.getPosterPath()) ? LARGER_IMAGES_BASE_URL + model.getPosterPath().replace("/", "") : R.drawable.thumb_place_holder)
                .placeholder(R.drawable.thumb_place_holder)
                .error(R.drawable.thumb_place_holder)
                .transform(new RoundedCorners(10))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false)
                .into(imageView);

        GlideApp.with(activity).load((null != model.getPosterPath()) ? LARGER_IMAGES_BASE_URL + model.getPosterPath().replace("/", "") : R.drawable.thumb_place_holder)
                .preload();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public boolean isIsgrid() {
        return isgrid;
    }

    public void setIsgrid(boolean isgrid) {
        this.isgrid = isgrid;
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public OnMovieSelectionListener getMovieSelectionListener() {
        return movieSelectionListener;
    }

    public void setMovieSelectionListener(OnMovieSelectionListener movieSelectionListener) {
        this.movieSelectionListener = movieSelectionListener;
    }

    public List<MovieModel> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieModel> movies, String query) {
        if (movies == null) {
            return;
        }
        this.movies = movies;
        this.query= query;
        notifyDataSetChanged();
    }

    public void notifyMoviesListChanged() {
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public List<MovieModel> getPreloadItems(int position) {
        return Collections.singletonList(movies.get(position));
    }

    @Nullable
    @Override
    public RequestBuilder getPreloadRequestBuilder(MovieModel item) {

        return requestBuilder
                .clone()
                .load(item.getPosterPath());
    }

    @Nullable
    @Override
    public int[] getPreloadSize(MovieModel item, int adapterPosition, int perItemPosition) {
        return actualDimensions;
    }

    private static class MovieViewHolder extends RecyclerView.ViewHolder {

        public View view;
        public TextView tvName;
        public TextView tvYear;
        public TextView tvDescription;
        public TextView tvVotes;
        public ImageView ivThumbnail;

        public MovieViewHolder(View view) {
            super(view);
            this.view = view;
            this.ivThumbnail = view.findViewById(R.id.ivThumbnail);
            this.tvName = view.findViewById(R.id.tvName);
            this.tvYear = view.findViewById(R.id.tvYear);
            this.tvDescription = view.findViewById(R.id.tvDescription);
            this.tvVotes = view.findViewById(R.id.tvVotes);
        }
    }

    private static class MovieViewHolderGrid extends RecyclerView.ViewHolder {

        public View view;
        public TextView tvNameGrid;
        public ImageView ivThumbGrid;
        public TextView tvYearGrid;
        public  TextView tvVotesGrid;

        public MovieViewHolderGrid(View view) {
            super(view);
            this.view = view;
            this.ivThumbGrid = view.findViewById(R.id.ivPostergrid);
            this.tvNameGrid = view.findViewById(R.id.tvNameGrid);
            this.tvYearGrid= view.findViewById(R.id.tvGridyear);

            this.tvVotesGrid= view.findViewById(R.id.tvGridVotes);
        }
    }

    public interface OnMovieSelectionListener {

        void onMovieSelected(MovieModel model, View view, int position);
    }
}
