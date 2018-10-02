package com.themoviedb.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.themoviedb.R;
import com.themoviedb.apis.retrofit.AppConstants;
import com.themoviedb.glide.GlideApp;

public class AboutDeveloper extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_profile);
        setNavigationAndToolBar();
        showAboutdDeveloper();
    }

    private void showAboutdDeveloper() {
        ImageView cover = findViewById(R.id.ivThumbDeveloper);
        ImageView profile = findViewById(R.id.ivProfile);

        GlideApp.with(this)
                // "https://img.youtube.com/vi/dNW0B0HsvVs/hqdefault.jpg"
                .load(AppConstants.COVER_URL)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.thumb_place_holder)
                        .error(R.drawable.thumb_place_holder)
                        .override(cover.getWidth(), 650)
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
                .into(cover);

        GlideApp.with(this)
                .asBitmap()
                .load(AppConstants.PROFILE_URL)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_user)
                        .error(R.drawable.ic_user)
                        .transform(new RoundedCorners(200))
                        .override((int) getResources().getDimension(R.dimen.movie_card_width), (int) getResources().getDimension(R.dimen.movie_card_width))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(false))
                .into(profile);

        ((TextView) findViewById(R.id.tvDeveloperName)).setText(getString(R.string.developername));
        ((TextView) findViewById(R.id.tvExperience)).setText(getString(R.string.nExperience));
        ((TextView) findViewById(R.id.tvProjects)).setText(getString(R.string.nProjects));
        ((TextView) findViewById(R.id.tvPost)).setText(R.string.post);
        ((TextView) findViewById(R.id.tvAboutDeveloper)).setText(getString(R.string.aboutmes));
        findViewById(R.id.btnLinkedIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = AppConstants.LINKEDINURL;
                try {
                    Uri webpage = Uri.parse(url);
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
                    startActivity(myIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(AboutDeveloper.this, R.string.errorNoAPP,  Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

    }

    private void setNavigationAndToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbarDeveloper);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setTitleEnabled(true);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            //supportActionBar.setTitle(R.string.app_name);
            collapsingToolbar.setTitle(getString(R.string.developername));
            collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.transparent)); // transperent color = #00000000
            collapsingToolbar.setCollapsedTitleTextColor(Color.rgb(255, 255, 255)); //Color of your title
            Typeface typeface = ResourcesCompat.getFont(this, R.font.montserrat);
            collapsingToolbar.setCollapsedTitleTypeface(typeface);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.back_arrow);
            drawable.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
            supportActionBar.setHomeAsUpIndicator(drawable);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ActivityCompat.finishAfterTransition(this);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
