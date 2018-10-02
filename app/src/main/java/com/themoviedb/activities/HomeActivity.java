package com.themoviedb.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.themoviedb.BaseApplication;
import com.themoviedb.R;
import com.themoviedb.adapters.MovieListAdapter;
import com.themoviedb.apis.request.DiscoveryRequest;
import com.themoviedb.apis.retrofit.AppConstants;
import com.themoviedb.models.MovieModel;
import com.themoviedb.presenters.HomePresenter;
import com.themoviedb.view.Utils;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import javax.inject.Inject;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomePresenter.HomeView {

    @Inject
    HomePresenter presenter;

    private DrawerLayout drawer;
    private RecyclerView rvMovies;
    private View loadingProgressView;
    private View filterView, errorView;
    private View emptyView;
    private NumberPicker numberPicker1;
    private Button btnResetFilters;
    private Button btnFilterDone;

    private MovieListAdapter movieListAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        MenuItem mSearch = menu.findItem(R.id.action_filter_search);
        final SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                mSearchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // mAdapter.getFilter().filter(newText);
                //callSearch(newText);
                return true;
            }

            public void callSearch(String query) {
                //Do searching
                Utils.hideKeyboard(HomeActivity.this);
                int startYear = numberPicker1.getValue();
                Spinner mySpinner = findViewById(R.id.spinner);
                String text = mySpinner.getSelectedItem().toString();
                presenter.filterMovieList(startYear, 0, text, query);
                Log.i("query", "" + query);

            }
        });

        //getting searchbar event
        mSearch.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {

                return true;
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                resetFilters();
                return true;
            }
        });
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApplication.getInstance().getApplicationComponent().inject(this);
        setContentView(R.layout.activity_home);
        initViews();
        setNavigationAndToolBar();
        presenter.startNow(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!presenter.isLoading()) {
            hideLoadingProgress();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.clear();
    }

    /**
     * A method for initalise view and set data in it.
     */
    private void initViews() {
        String[] country = {AppConstants.SortPopDesc,
                AppConstants.SortPopASC,
                AppConstants.SortRateDesc,
                AppConstants.SortRateAsc};
        Spinner spin = findViewById(R.id.spinner);
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
        //set data in movielist adapter.
        movieListAdapter = new MovieListAdapter(this);
        drawer = findViewById(R.id.drawer_layout);
        rvMovies = findViewById(R.id.rvMovies);

        boolean isMobile = getResources().getBoolean(R.bool.isMobile);
        if (isMobile) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                final LinearLayoutManager layoutManager = new GridLayoutManager(this, 3);
                movieListAdapter.setIsgrid(true);
                rvMovies.setLayoutManager(layoutManager);
            } else {
                DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
                itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
                movieListAdapter.setIsgrid(false);
                final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                rvMovies.setLayoutManager(layoutManager);
                rvMovies.addItemDecoration(itemDecorator);
            }
            movieListAdapter.setMovieSelectionListener(new MovieListAdapter.OnMovieSelectionListener() {
                @Override
                public void onMovieSelected(MovieModel model, View view, int position) {
                    movieSelected(model, view, position);
                }
            });
        } else {
            final LinearLayoutManager layoutManager = new GridLayoutManager(this, 3);
            movieListAdapter.setIsgrid(true);
            rvMovies.setLayoutManager(layoutManager);
            // do something else
        }
        //check screen orientation.

        rvMovies.setAdapter(movieListAdapter);

        RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (presenter.isLoading()) {
                    return;
                }
                int visibleItemCount = 0;
                int totalItemCount = 0;
                int pastVisibleItems = 0;
                if (rvMovies.getLayoutManager() instanceof LinearLayoutManager) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) rvMovies.getLayoutManager();
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                } else {
                    GridLayoutManager layoutManager = (GridLayoutManager) rvMovies.getLayoutManager();
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                }
                if (pastVisibleItems + visibleItemCount >= totalItemCount - 10) {
                    fetchNext();
                }
            }
        };
        rvMovies.addOnScrollListener(scrollListener);

        loadingProgressView = findViewById(R.id.loadingProgressView);

        filterView = findViewById(R.id.filterView);
        emptyView = findViewById(R.id.emptyView);

        numberPicker1 = findViewById(R.id.numberPicker1);

        numberPicker1.setMinValue(DiscoveryRequest.MIN_YEAR);
        numberPicker1.setMaxValue(DiscoveryRequest.MAX_YEAR);

        btnFilterDone = findViewById(R.id.btnDone);
        btnFilterDone.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                filter();
            }
        });

        btnResetFilters = findViewById(R.id.btnReset);
        btnResetFilters.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                resetFilters();
            }
        });
    }

    /**
     * A method to resent all data.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void resetFilters() {
        hideFilterMenu();
        Utils.hideKeyboard(HomeActivity.this);
        presenter.fetchFirst();
    }

    /**
     * To get next movies.
     */
    private void fetchNext() {
        presenter.fetchNext();
    }


    /**
     * A method to call service as per filter values.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void filter() {
        hideFilterMenu();
        Utils.hideKeyboard(HomeActivity.this);
        int startYear = numberPicker1.getValue();
        Spinner mySpinner = findViewById(R.id.spinner);
        String text = mySpinner.getSelectedItem().toString();

        presenter.filterMovieList(startYear, 0, text, null);
    }


    private void movieSelected(MovieModel model, View view, int position) {
        if (isFinishing()) {
            return;
        }
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ID, model.getId());
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_NAME, model.getTitle());

        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this,
                        view,
                        ViewCompat.getTransitionName(view));
        startActivity(intent, options.toBundle());
        showLoadingProgress();
        //startActivity(intent);
    }

    /**
     * A method to set navigation toolbar and drawer.
     */
    private void setNavigationAndToolBar() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //define action drawer.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setTitleTextAppearance(this, R.style.RalewayTextAppearance);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (filterView.getVisibility() == View.VISIBLE) {
            hideFilterMenu();
        } else {
            super.onBackPressed();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_filter) {
            if (filterView.getVisibility() == View.VISIBLE) {
                hideFilterMenu();
            } else {
                showFilterMenu();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        Intent intent;
        switch (item.getItemId()) {

            case R.id.about_app:
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, AboutAppActivity.class));
                break;
            case R.id.project_github:
                drawer.closeDrawer(GravityCompat.START);
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(AppConstants.KEYURL, AppConstants.GITHUBURL);
                startActivity(intent);
                break;
            case R.id.iblog:
                drawer.closeDrawer(GravityCompat.START);
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(AppConstants.KEYURL, AppConstants.BLOGURL);
                startActivity(intent);
                break;
            case R.id.about_developer:

                drawer.closeDrawer(GravityCompat.START);
                intent = new Intent(this, AboutDeveloper.class);
                startActivity(intent);
                break;

            default:
                drawer.closeDrawer(GravityCompat.START);
                break;
        }

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showFilterMenu() {
        Utils.enterReveal(filterView);
        //filterView.setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void hideFilterMenu() {
        Utils.exitReveal(filterView);
    }


    @Override
    public void showMovies(List<MovieModel> movies, int minYear, int maxYear, String query) {
        if (isFinishing()) {
            return;
        }
        if (movies == null || movies.size() == 0) {
            showEmptyView();
            return;
        }

        hideEmptyView();
        movieListAdapter.setMovies(movies, query);

        numberPicker1.setValue(minYear);
    }

    private void hideEmptyView() {
        if (emptyView != null) {
            findViewById(R.id.il_nodata).setVisibility(View.GONE);
        }
    }

    private void showEmptyView() {
        if (emptyView != null) {
            findViewById(R.id.il_nodata).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.tvOops)).setText(R.string.nodatafound);
            ((TextView) findViewById(R.id.tvMessage)).setText(R.string.datanotpresent);
        }
    }

    @Override
    public void notifyMoviesListChanged() {
        if (isFinishing()) {
            return;
        }
        movieListAdapter.notifyMoviesListChanged();
    }

    @Override
    public void showLoadingProgress() {
        if (isFinishing()) {
            return;
        }
        loadingProgressView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingProgress() {
        if (isFinishing()) {
            return;
        }
        loadingProgressView.setVisibility(View.GONE);
    }

    /**
     * Get error when service not working or something went wrong in it.
     *
     * @param throwable
     */
    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
        hideLoadingProgress();
        if (throwable instanceof UnknownHostException || throwable instanceof SocketTimeoutException) {
            errorView = findViewById(R.id.il_nodata);
            errorView.setVisibility(View.VISIBLE);
            errorView.findViewById(R.id.btn_tryagain).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    errorView.setVisibility(View.GONE);
                    presenter.startNow(HomeActivity.this);
                }
            });
            Snackbar.make(drawer, getString(R.string.check_network_connection) + " : " + throwable.getMessage(), Snackbar.LENGTH_LONG).show();
            return;
        }
        Snackbar.make(drawer, getString(R.string.something_went_wrong) + " : " + throwable.getMessage(), Snackbar.LENGTH_LONG).show();
    }

}
