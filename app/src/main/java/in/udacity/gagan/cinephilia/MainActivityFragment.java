package in.udacity.gagan.cinephilia;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.udacity.gagan.cinephilia.Adapter.mListAdapter;
import in.udacity.gagan.cinephilia.Classes.Movie;
import in.udacity.gagan.cinephilia.Extras.TmdbUrls;
import in.udacity.gagan.cinephilia.MovieDB.ContentProviderHelperMethods;
import in.udacity.gagan.cinephilia.Network.NetworkController;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    int firstVisibleItem, visibleItemCount, totalItemCount;
    private boolean isFavoritesSelected;
    int sort = 0;
    private String TAG="Fragment";
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 4;
    private int pageCount = 1;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Movie> mMoviesList=new ArrayList<>();
    private Movie mObj;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            mMoviesList = savedInstanceState.getParcelableArrayList("mMovieList");
            isFavoritesSelected = savedInstanceState.getBoolean("isFavoritesSelected");
            pageCount = savedInstanceState.getInt("pageCount");
            previousTotal = savedInstanceState.getInt("previousTotal");
            firstVisibleItem = savedInstanceState.getInt("firstVisibleItem");
            visibleItemCount = savedInstanceState.getInt("visibleItemCount");
            totalItemCount = savedInstanceState.getInt("totalItemCount");
            loading = savedInstanceState.getBoolean("loading");
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("mMovieList",mMoviesList);
        outState.putInt("pageCount", pageCount);
        outState.putBoolean("isFavoritesSelected", isFavoritesSelected);
        outState.putInt("previousTotal", previousTotal);
        outState.putInt("firstVisibleItem", firstVisibleItem);
        outState.putInt("visibleItemCount", visibleItemCount);
        outState.putInt("totalItemCount", totalItemCount);
        outState.putBoolean("loading", loading);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_item, menu);
    }

   @Override
    public void onStart(){
        super.onStart();
    }

    //removing settings item from toolbar
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_settings).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id=item.getItemId();
        if(id == R.id.most_popular){
            isFavoritesSelected = false;
            sort = 0;
            refreshList(0);
            return true;
        }else if(id == R.id.highest_rated){
            isFavoritesSelected = false;
            sort = 1;
            refreshList(1);
            return true;
        }else if(id == R.id.favorites) {
            isFavoritesSelected = true;
            sort = 2;
            refreshList(2);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onResume(){
        if(isFavoritesSelected) {
            getMovieList();
        }
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        final LinearLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        showContentOnStart();
        mAdapter = new mListAdapter(Glide.with(this), mMoviesList, getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = mRecyclerView.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                            pageCount++;
                        }
                    }
                    if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                        if (sort == 0) {
                            String url = TmdbUrls.MOVIE_URL + TmdbUrls.SORT_POPULARITY + "api_key=" + BuildConfig.THE_MOVIE_DATABASE_API_KEY + TmdbUrls.MODE + "&page=" + String.valueOf(pageCount);
                            showSnackBar(getString(R.string.loading_page) + String.valueOf(pageCount));
                            getDataFromApi(url);
                        } else if(sort == 1){
                            String url = TmdbUrls.MOVIE_URL + TmdbUrls.SORT_R_RATED + "api_key=" + BuildConfig.THE_MOVIE_DATABASE_API_KEY + TmdbUrls.MODE + "&page=" + String.valueOf(pageCount);
                            showSnackBar(getString(R.string.loading_page) + String.valueOf(pageCount));
                            getDataFromApi(url);
                        } else if(sort == 2)
                            getMovieList();
                        loading = true;
                    }
                }
        });
        return rootView;
    }
    private void showContentOnStart(){
        if (pageCount == 1) {
            String url = TmdbUrls.MOVIE_URL + TmdbUrls.SORT_POPULARITY + "api_key=" +BuildConfig.THE_MOVIE_DATABASE_API_KEY + TmdbUrls.MODE ;
            getDataFromApi(url);
        } else {
            mRecyclerView.scrollToPosition(firstVisibleItem);
        }
    }

    public void setDataChange(){
        if(isFavoritesSelected)
            getMovieList();
    }
    private void getMovieList() {
        mMoviesList.clear();
        ArrayList<Movie> list = new ArrayList<>(ContentProviderHelperMethods
                .getMovieListFromDatabase(getActivity()));
        for (Movie movie : list) {
            mMoviesList.add(movie);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void refreshList(int position) {

        loading = true;
        visibleThreshold = 4;
        pageCount = 1;
        previousTotal = 0;
        mObj=null;
        mMoviesList.clear();

        if (position == 0)
            getDataFromApi(TmdbUrls.MOVIE_URL + TmdbUrls.SORT_POPULARITY + "api_key=" +BuildConfig.THE_MOVIE_DATABASE_API_KEY + TmdbUrls.MODE );
        else if(position == 1)
            getDataFromApi(TmdbUrls.MOVIE_URL + TmdbUrls.SORT_R_RATED + "api_key=" + BuildConfig.THE_MOVIE_DATABASE_API_KEY + TmdbUrls.MODE);
        else if(position == 2)
            getMovieList();
    }

    private void getDataFromApi(String url) {

        JsonObjectRequest getListData = new JsonObjectRequest(url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray mResultArray = response.getJSONArray("results");
                    for (int i = 0; i < mResultArray.length(); i++) {
                        Log.v(TAG,mResultArray.toString());
                        JSONObject mResultObject = mResultArray.getJSONObject(i);
                        mObj=new Movie(mResultObject.getString("id"),
                                mResultObject.getString("title"),
                                mResultObject.getString("poster_path"),
                                mResultObject.getString("overview"),
                                mResultObject.getString("vote_average"),
                                mResultObject.getString("release_date"),
                                mResultObject.getString("backdrop_path")
                                );
                        mMoviesList.add(mObj);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showSnackBar(getString(R.string.error_msg));
            }
        });

      NetworkController.getInstance().addToRequestQueue(getListData);
    }

    void showSnackBar(String msg) {
        Snackbar.make(mRecyclerView, msg, Snackbar.LENGTH_LONG)
                .show();
    }

}

