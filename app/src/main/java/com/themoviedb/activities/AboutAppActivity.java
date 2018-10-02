package com.themoviedb.activities;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import com.themoviedb.R;

import java.util.Objects;

public class AboutAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        setNavigationAndToolBar();
        showAboutApp();
    }

    @SuppressWarnings("deprecation")
    private void showAboutApp() {

        TextView tvAboutApp = findViewById(R.id.tvAboutApp);
        String source = getString(R.string.aboutAPPString);
        tvAboutApp.setText(Html.fromHtml(source));
    }

    private void setNavigationAndToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(R.string.app_name);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.back_arrow);
            Objects.requireNonNull(drawable).setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
            supportActionBar.setHomeAsUpIndicator(drawable);
        }
        toolbar.setTitleTextAppearance(this, R.style.RalewayTextAppearance);


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
