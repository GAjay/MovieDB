package com.themoviedb.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.themoviedb.BaseApplication;
import com.themoviedb.R;
import com.themoviedb.adapters.CastAdapter;
import com.themoviedb.adapters.RecommendationAdapter;
import com.themoviedb.adapters.VideoListingAdapter;
import com.themoviedb.apis.entity.responses.Credit;
import com.themoviedb.apis.entity.responses.Crew;
import com.themoviedb.apis.entity.responses.Recommendation;
import com.themoviedb.glide.GlideApp;
import com.themoviedb.models.GenreModel;
import com.themoviedb.models.MovieDetailModel;
import com.themoviedb.models.MovieModel;
import com.themoviedb.models.ProductionCountryModel;
import com.themoviedb.models.SpokenLanguageModel;
import com.themoviedb.presenters.MovieDetailPresenter;
import com.themoviedb.view.Utils;
import com.themoviedb.view.listener.AppBarStateChangeListener;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import javax.inject.Inject;

import static com.themoviedb.apis.retrofit.AppUrls.LARGER_IMAGES_BASE_URL;


/*
 * Created by Ajay Kumar Maheshwari .
 * A activity class for movie details to show select movie detail.
 */

public class MovieDetailActivity extends BaseActivity implements MovieDetailPresenter.MovieDetailView {

    public static final String EXTRA_MOVIE_ID = "extra_movie_id";
    public static final String EXTRA_MOVIE_NAME = "extra_movie_name";

    @Inject
    MovieDetailPresenter presenter;

    private ImageView ivThumbnail, iVProduction;
    private View loadingProgressView;
    private View detailsView;
    private TextView tvGenre;
    private TextView tvLanguages, tvTitle;
    private TextView tvCountries;
    private TextView tvOverView;
    private TextView tvReleaseDate, tvWriter, tvDir, tvVotes, tvRevenu;
    private TextView tvRatings;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportPostponeEnterTransition();
        BaseApplication.getInstance().getApplicationComponent().inject(this);
        setContentView(R.layout.activity_movie_detail);
        initViews();
        setNavigationAndToolBar();
        int movieId = getIntent().getIntExtra(EXTRA_MOVIE_ID, 0);
        presenter.startNow(this, movieId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideLoadingProgress();
    }

    /**
     * A method for initialization of view
     */
    private void initViews() {
        ivThumbnail = findViewById(R.id.ivThumbnailDetail);
        floatingActionButton = findViewById(R.id.fab);
        iVProduction = findViewById(R.id.ivProduction);
        loadingProgressView = findViewById(R.id.loadingProgressView);
        detailsView = findViewById(R.id.detailsView);
        tvGenre = findViewById(R.id.tvGenre);
        tvLanguages = findViewById(R.id.tvLanguages);
        tvCountries = findViewById(R.id.tvCountries);
        tvOverView = findViewById(R.id.tvOverView);
        tvReleaseDate = findViewById(R.id.tvReleaseDate);
        tvRatings = findViewById(R.id.tvRatings);
        tvTitle = findViewById(R.id.tvTitle);
        tvDir = findViewById(R.id.tvDir);
        tvWriter = findViewById(R.id.tvWriter);
        tvVotes = findViewById(R.id.tvVotes);

        tvRevenu = findViewById(R.id.tvRevenu);
    }

    /**
     * A mehtod for set Toolbar and Navigation Bar.
     */
    private void setNavigationAndToolBar() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setTitleEnabled(true);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            String movieName = getIntent().getStringExtra(EXTRA_MOVIE_NAME);
            collapsingToolbar.setTitle(movieName);
            collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.transparent)); // transperent color = #00000000
            collapsingToolbar.setCollapsedTitleTextColor(Color.rgb(255, 255, 255)); //Color of your title
            Typeface typeface = ResourcesCompat.getFont(this, R.font.montserrat);
            collapsingToolbar.setCollapsedTitleTypeface(typeface);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.back_arrow);
            drawable.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
            supportActionBar.setHomeAsUpIndicator(drawable);
        }
        toolbar.setTitleTextAppearance(this, R.style.RalewayTextAppearance);


    }

    /**
     * A method to set data in layout after get response from presenter.
     *
     * @param model
     * @param credit
     * @param recommendation
     */
    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void showMovieDetail(final MovieDetailModel model, Credit credit, Recommendation recommendation) {
        if (isFinishing() || model == null) {
            exit();
            Toast.makeText(this, R.string.no_movie_detail, Toast.LENGTH_LONG).show();
            return;
        }

        ((RelativeLayout) findViewById(R.id.rlError)).setVisibility(View.GONE);
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        hideLoadingProgress();
        appBarLayout.setVisibility(View.VISIBLE);
        ((NestedScrollView) findViewById(R.id.scrollView)).setVisibility(View.VISIBLE);
        detailsView.setVisibility(View.VISIBLE);
        final MovieModel movie = model.getMovie();
        if (movie != null) {
            String thumbnail = movie.getBackdropPath();
            GlideApp.with(this)
                    // "https://img.youtube.com/vi/dNW0B0HsvVs/hqdefault.jpg"
                    .load((null != thumbnail) ? LARGER_IMAGES_BASE_URL + thumbnail.replace("/", "") : R.drawable.thumb_place_holder)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.thumb_place_holder)
                            .error(R.drawable.thumb_place_holder)
                            .override(ivThumbnail.getWidth(), 650)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(false)
                    )
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            supportStartPostponedEnterTransition();
                            return false;
                        }

                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            supportStartPostponedEnterTransition();
                            return false;
                        }
                    })
                    .into(ivThumbnail);}
            if (null != model.getProductionCompanies() && model.getProductionCompanies().size() > 0) {
                if (null != model.getProductionCompanies().get(0).getLogoPath()) {
                    GlideApp.with(this)
                            .asBitmap()
                            .load(LARGER_IMAGES_BASE_URL + model.getProductionCompanies().get(0).getLogoPath().replace("/", ""))
                            .apply(new RequestOptions()
                                    .placeholder(R.drawable.thumb_place_holder)
                                    .error(R.drawable.thumb_place_holder)
                                    .override((int) getResources().getDimension(R.dimen.movie_image_width), (int) getResources().getDimension(R.dimen.d_larger))
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(false))
                            .into(iVProduction);
                } else {
                    iVProduction.setVisibility(View.GONE);
                }
            }
        else {
            iVProduction.setVisibility(View.GONE);
        }
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state, int verticalOffset) {
                switch (state.name()) {
                    case "COLLAPSED":
                        if (null != model.getProductionCompanies() && model.getProductionCompanies().size() > 0) {
                            if (null != model.getProductionCompanies().get(0).getLogoPath()) {
                                Utils.exitReveal(iVProduction);
                            }
                        }
                        break;
                    case "IDLE":
                        if (null != model.getProductionCompanies() && model.getProductionCompanies().size() > 0) {
                            if (null != model.getProductionCompanies().get(0).getLogoPath()) {
                                iVProduction.setAlpha(0.3f);
                            }
                        }
                        break;
                    case "EXPANDED":
                        if (null != model.getProductionCompanies() && model.getProductionCompanies().size() > 0) {
                            if (null != model.getProductionCompanies().get(0).getLogoPath()) {
                                Utils.enterReveal(iVProduction);
                            }
                        }
                        break;

                }
            }
        });
        String date = (!TextUtils.isEmpty(movie.getReleaseDate())) ? " (" + Utils.getYear(movie.getReleaseDate()) + ")" : "";
        tvTitle.setText(getIntent().getStringExtra(EXTRA_MOVIE_NAME) + date);
        if (model.isAdult()) {
            tvTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_plus_18_movie, 0);
            tvTitle.setCompoundDrawablePadding(5);
        } else {
            tvTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            tvTitle.setCompoundDrawablePadding(0);

        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = (!TextUtils.isEmpty(model.getHomepage())) ? model.getHomepage().toString() : "";
                String message = getString(R.string.sharemovie) + val;
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, message);

                startActivity(Intent.createChooser(share, getString(R.string.share)));
            }
        });


        String separator = " | ";
        if (null != model.getGenres()) {
            if (model.getGenres().size() > 0) {
                List<GenreModel> genres = model.getGenres();
                if (genres != null) {
                    StringBuilder builder = new StringBuilder();
                    int size = genres.size();
                    for (int i = 0; i < size; i++) {
                        GenreModel genreModel = genres.get(i);
                        String name = genreModel.getName();
                        if (name != null && name.trim().length() > 0) {
                            builder.append(name);
                            if (i < size - 1) {
                                builder.append(separator);
                            }
                        }
                    }
                    if (builder.length() > 0) {
                        SpannableString spannableString = new SpannableString(getString(R.string.genres) + " " + builder.toString().trim());
                        tvGenre.setText(Utils.boldString(spannableString, getString(R.string.genres)));

                    }
                }
            } else {
                tvGenre.setVisibility(View.GONE);
            }
        }

        //movie actors and cast team
        RecyclerView recyclerView = findViewById(R.id.rlCast);
        if (null != credit.getCast()) {
            if (credit.getCast().size() > 0) {
                recyclerView.setAdapter(new CastAdapter(credit.getCast(), MovieDetailActivity.this));
            } else {
                ((TextView) findViewById(R.id.tvCastsLabel)).setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
            }
        }
        //recommendation
        RecyclerView recycler = findViewById(R.id.rlRecoomedndation);
        if (null != recommendation.getResults()) {
            if (recommendation.getResults().size() > 0) {
                recycler.setAdapter(new RecommendationAdapter(recommendation.getResults(), MovieDetailActivity.this));
            } else {
                ((TextView) findViewById(R.id.tvRecommendationlabel)).setVisibility(View.GONE);
                recycler.setVisibility(View.GONE);
            }
        }
        //Movie videos
        RecyclerView recyclerViewVideos = findViewById(R.id.rlVideos);
        if (null != model.getVideos()) {
            recyclerViewVideos.setAdapter(new VideoListingAdapter(model.getVideos().getResults(), MovieDetailActivity.this));
        } else {
            ((TextView) findViewById(R.id.tvVideosLabel)).setVisibility(View.GONE);
            recyclerViewVideos.setVisibility(View.GONE);
        }
        if (null != model.getSpokenLanguages()) {
            if (model.getSpokenLanguages().size() > 0) {
                List<SpokenLanguageModel> languages = model.getSpokenLanguages();
                if (languages != null) {
                    StringBuilder builder = new StringBuilder();
                    int size = languages.size();
                    for (int i = 0; i < size; i++) {
                        SpokenLanguageModel languageModel = languages.get(i);
                        String name = languageModel.getName();
                        if (name != null && name.trim().length() > 0) {
                            builder.append(name);
                            if (i < size - 1) {
                                builder.append(separator);
                            }
                        }
                    }
                    if (builder.length() > 0) {
                        SpannableString spannableString = new SpannableString(getString(R.string.lang) + " " + builder.toString().trim());

                        tvLanguages.setText(Utils.boldString(spannableString, getString(R.string.lang)));
                    }
                }
            } else {
                tvLanguages.setVisibility(View.GONE);
            }
        }


        List<Crew> crew = credit.getCrew();
        if (crew != null && crew.size() > 0) {
            StringBuilder builder = new StringBuilder();
            int size = crew.size();
            boolean isFirst = true;
            for (int i = 0; i < size; i++) {
                Crew crewModel = crew.get(i);
                String department = crewModel.getDepartment();
                if (department.contains("Writing")) {
                    if (department != null && department.trim().length() > 0) {
                        if (!isFirst) {
                            builder.append(separator);
                        } else {
                            isFirst = false;
                        }
                        builder.append(crewModel.getName());

                    }
                } else if (department.contains("Directing") && crewModel.getJob().equalsIgnoreCase("Director")) {
                    SpannableString spannableString = new SpannableString(getString(R.string.director) + " " + crewModel.getName().trim());
                    tvDir.setText(Utils.boldString(spannableString, getString(R.string.director)));

                }
            }
            if (builder.length() > 0) {
                SpannableString spannableString = new SpannableString(getString(R.string.writer) + " " + builder.toString().trim());
                tvWriter.setText(Utils.boldString(spannableString, getString(R.string.writer)));
            }

            if (isFirst) {
                tvWriter.setVisibility(View.GONE);
            }
        } else {
            tvWriter.setVisibility(View.GONE);
            tvDir.setVisibility(View.GONE);
        }


        if (null != model.getProductionCountries()) {
            if (model.getProductionCountries().size() > 0) {
                List<ProductionCountryModel> countries = model.getProductionCountries();
                StringBuilder builder;
                if (countries != null) {
                    builder = new StringBuilder();
                    int size = countries.size();
                    for (int i = 0; i < size; i++) {
                        ProductionCountryModel countryModel = countries.get(i);
                        String name = countryModel.getName();
                        if (name != null && name.trim().length() > 0) {
                            builder.append(name);
                            if (i < size - 1) {
                                builder.append(separator);
                            }
                        }
                    }
                    if (builder.length() > 0) {
                        SpannableString spannableString = new SpannableString(getString(R.string.country) + " " + builder.toString().trim());
                        tvCountries.setText(Utils.boldString(spannableString, getString(R.string.country)));

                    }
                }
            } else {
                tvCountries.setVisibility(View.GONE);
            }
        }


        if (movie != null) {
            if (null != model.getTagline())
                ((TextView) findViewById(R.id.tvTagline)).setText(model.getTagline());
            else
                ((TextView) findViewById(R.id.tvTagline)).setVisibility(View.GONE);
            String overview = movie.getOverview();
            if (overview != null && overview.trim().length() > 0) {
                tvOverView.setText(overview);
            } else {
                tvOverView.setText(getString(R.string.nooverview));
            }
            String releaseDate = movie.getReleaseDate();
            if (!TextUtils.isEmpty(movie.getReleaseDate())) {
                tvReleaseDate.setText(calTimeDuration(model.getRuntime()) + " " + Utils.getDate(releaseDate));
            } else {
                tvReleaseDate.setVisibility(View.GONE);
            }
            SpannableString spannableString = new SpannableString(String.valueOf(movie.getVoteAverage()) + getString(R.string.ten));
            tvRatings.setText(Utils.increaseFontSizeForPath(spannableString, String.valueOf(movie.getVoteAverage()), 2));
            SpannableString spannableStringVotes = new SpannableString(getString(R.string.votes) + "  " + String.valueOf(movie.getVoteCount()));
            tvVotes.setText(Utils.boldString(spannableStringVotes, getString(R.string.votes)));
            SpannableString spannableStringrevenue = new SpannableString(
                    getString(R.string.revenue) + "  " + getString(R.string.dollars) + Utils.coolFormat(Double.valueOf(model.getRevenue()), 0));
            tvRevenu.setText(Utils.boldString(spannableStringrevenue, getString(R.string.revenue)));

            //calculating profit.
            int profit = model.getRevenue() - model.getBudget();
            GraphView graph = (GraphView) findViewById(R.id.graph);
            if (!TextUtils.isEmpty(Utils.getYear(releaseDate))) {
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                        new DataPoint((Integer.valueOf(Utils.getYear(releaseDate)) - 2), 0),
                        new DataPoint((Integer.valueOf(Utils.getYear(releaseDate)) - 1), 0),
                        new DataPoint(Integer.valueOf(Utils.getYear(releaseDate)), Integer.valueOf(
                                Utils.removeLastChar(Utils.coolFormat(Double.valueOf(profit), 0))))
                });
                //setting graph
                series.setTitle(getString(R.string.grossvalue));
                graph.addSeries(series);

            } else {
                graph.setVisibility(View.GONE);
            }
        }
    }
        /**
         * A method for calculate movie time duration from runtime and return string value.
         *
         * @param runTime
         * @return
         */
        private String calTimeDuration ( int runTime){
            String duration = "";
            int hours = runTime / 60;
            int mins = runTime % 60;
            duration = String.valueOf(hours) + "h " + String.valueOf(mins) + "mins";
            return duration;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        private void setTransparentStatusBar ( int color){
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                getWindow().setStatusBarColor(color);
            }
        }

        /**
         * A method for show loading progressbar.
         */
        @Override
        public void showLoadingProgress () {
            if (isFinishing()) {
                return;
            }
            loadingProgressView.setVisibility(View.VISIBLE);
        }

        @Override
        public void hideLoadingProgress () {
            if (isFinishing()) {
                return;
            }
            loadingProgressView.setVisibility(View.GONE);
        }

        @Override
        public void onBackPressed () {
            super.onBackPressed();
            presenter.cleanUp();
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            switch (item.getItemId()) {
                case android.R.id.home:
                    exit();
                    break;
                default:
                    break;
            }
            return super.onOptionsItemSelected(item);
        }

        /**
         * A method for clean app data of activity and presenter.
         */
        private void exit () {
            ActivityCompat.finishAfterTransition(this);
            presenter.cleanUp();
        }

        public void cleanPresenter () {
            presenter.cleanUp();
        }

        @Override
        public void onError (Throwable throwable){
            throwable.printStackTrace();
            hideLoadingProgress();
            ((Button) findViewById(R.id.btnGoBack)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exit();
                }
            });
            ((AppBarLayout) findViewById(R.id.appbar)).setVisibility(View.GONE);
            ((NestedScrollView) findViewById(R.id.scrollView)).setVisibility(View.GONE);
            ((RelativeLayout) findViewById(R.id.rlError)).setVisibility(View.VISIBLE);
            if (throwable instanceof UnknownHostException || throwable instanceof SocketTimeoutException) {
                Snackbar.make(detailsView, getString(R.string.check_network_connection) + " : " + throwable.getMessage(), Snackbar.LENGTH_LONG).show();
                return;
            }
            Snackbar.make(detailsView, getString(R.string.something_went_wrong) + " : " + throwable.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }
