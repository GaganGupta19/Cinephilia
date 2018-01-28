package com.moviephilia;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.moviephilia.Adapter.DetailsAdapter;
import com.moviephilia.Classes.MovieDetail;
import com.moviephilia.Classes.Review;
import com.moviephilia.Classes.Trailer;
import com.moviephilia.Extras.TmdbUrls;
import com.moviephilia.MovieDB.ContentProviderHelperMethods;
import com.moviephilia.MovieDB.DatabaseHelper;
import com.moviephilia.MovieDB.MoviesContentProvider;
import com.moviephilia.Network.NetworkController;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private int lightColor, darkColor;
    private Bundle args;
    private String id, backdrop, title, trailerURL;
    private final String TAG="DetailActivityFragment";
    private NotifyDataChange mCallback;
    private RecyclerView mRecyclerView;
    private DetailsAdapter mAdapter;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private LinearLayoutManager mLayoutManager;
    private ImageView mImageView;
    private String actname="Fragment";
    private FloatingActionButton fab;
    private Trailer mTrailer;
    private MovieDetail mMovieDetail;
    private Review mReview;
    private ArrayList<Review> mReviewList = new ArrayList<>();
    private ArrayList<MovieDetail> mDetailsList = new ArrayList<>();
    private ArrayList<Trailer> mTrailersList = new ArrayList<>();

    public DetailActivityFragment() {
    }
    public interface NotifyDataChange{
        void notifyChange();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity activity = context instanceof MainActivity ? (MainActivity) context : null;
        if (activity != null) {
            mCallback = (NotifyDataChange) activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        args = getArguments();
        setValues(args);

        fetchMovieDetails(id);
        fetchTrailerData(id);
        fetchReviewData(id);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar_layout_movie_details);
        mToolbar = (Toolbar) rootView.findViewById(R.id.toolbar_movie_details);
        mLayoutManager = new LinearLayoutManager(getActivity());
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        mImageView = (ImageView) rootView.findViewById(R.id.backdrop);
        setUI();

        return rootView;
    }

    private void setValues(Bundle args){
        if(args!=null){
            id = args.getString("id", id);
            backdrop = args.getString("backdrop_path" ,backdrop);
            title = args.getString("title" ,title);
            darkColor = args.getInt("darkColor", darkColor);
            lightColor = args.getInt("lightColor", lightColor);
        }
        else {
            id = getActivity().getIntent().getStringExtra("id");
            lightColor = getActivity().getIntent().getIntExtra("lightColor", 0);
            backdrop = getActivity().getIntent().getStringExtra("backdrop_path");
            darkColor = getActivity().getIntent().getIntExtra("darkColor", 0);
            title = getActivity().getIntent().getStringExtra("title");
        }
    }

    private void setUI(){

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mCollapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(getActivity(), android.R.color.transparent));
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(getActivity(), android.R.color.white));
        mCollapsingToolbarLayout.setTitle(title);
        mCollapsingToolbarLayout.setBackgroundColor(lightColor);
        mCollapsingToolbarLayout.setContentScrimColor(lightColor);

        Glide.with(this)
                .load("http://image.tmdb.org/t/p/w780" + backdrop)
                .centerCrop()
                .crossFade()
                .into(mImageView);

        mToolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_back));
        mToolbar.inflateMenu(R.menu.menu_detail);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_share) {
                    startActivity(Intent.createChooser(shareIntent(TmdbUrls.YOUTUBE_LINK + trailerURL), getResources().getString(R.string.share)));
                    return true;
                }
                return true;
            }
        });
        fab.setBackgroundTintList(ColorStateList.valueOf(lightColor));
        Window window = getActivity().getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(darkColor);
    }

    private void setOffLineUI(MovieDetail mMovieDetail){

        mDetailsList.add(mMovieDetail);
        fab.setBackgroundTintList(ColorStateList.valueOf(lightColor));
        mCollapsingToolbarLayout.setTitle(mMovieDetail.getTitle());
        lightColor = ContextCompat.getColor(getActivity(),android.R.color.black);
        mAdapter = new DetailsAdapter(lightColor, mDetailsList, mReviewList, mTrailersList, getActivity());
        mRecyclerView.setAdapter(mAdapter);

    }
    private void fetchMoviefromDB(final MovieDetail mMovieDetail){

        boolean isMovieInDB = ContentProviderHelperMethods
                .isMovieInDatabase(getActivity(),
                        String.valueOf(mMovieDetail.getId()));
        if (isMovieInDB) {
            fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_like));
        } else {
            fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_like_outline));
        }
        fab.show();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isMovieInDB = ContentProviderHelperMethods
                        .isMovieInDatabase(getActivity(),
                                String.valueOf(mMovieDetail.getId()));
                if (isMovieInDB) {
                    Uri contentUri = MoviesContentProvider.CONTENT_URI;
                    getActivity().getContentResolver().delete(contentUri, "id=?", new String[]{String.valueOf(mMovieDetail.getId())});
                    Snackbar.make(view, getResources().getString(R.string.removed_from_favourites), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    if(mCallback != null)mCallback.notifyChange();
                    fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_like_outline));

                } else {
                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.KEY_ID, mMovieDetail.getId());
                    values.put(DatabaseHelper.KEY_TITLE, mMovieDetail.getTitle());
                    values.put(DatabaseHelper.KEY_RATING, mMovieDetail.getVote_average());
                    values.put(DatabaseHelper.KEY_GENRE, mMovieDetail.getGenre());
                    values.put(DatabaseHelper.KEY_DATE, mMovieDetail.getReleasedate());
                    values.put(DatabaseHelper.KEY_STATUS, mMovieDetail.getStatus());
                    values.put(DatabaseHelper.KEY_OVERVIEW, mMovieDetail.getOverview());
                    values.put(DatabaseHelper.KEY_BACKDROP, mMovieDetail.getBackdrop_path());
                    values.put(DatabaseHelper.KEY_VOTE_COUNT, mMovieDetail.getmVotecount());
                    values.put(DatabaseHelper.KEY_TAG_LINE, mMovieDetail.getmTagline());
                    values.put(DatabaseHelper.KEY_RUN_TIME, mMovieDetail.getmRuntime());
                    values.put(DatabaseHelper.KEY_LANGUAGE, mMovieDetail.getmLanguage());
                    values.put(DatabaseHelper.KEY_POPULARITY, mMovieDetail.getmPopularity());
                    values.put(DatabaseHelper.KEY_POSTER, mMovieDetail.getPoster_path());

                    getActivity().getContentResolver().insert(MoviesContentProvider.CONTENT_URI, values);

                    Snackbar.make(view, getResources().getString(R.string.added_to_favourites), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_like));
                }
            }
        });
    }

    private void fetchMovieDetails(final String id){
        mMovieDetail = null;
        mDetailsList.clear();
        String url = TmdbUrls.MOVIE_URL + id + "?" + "api_key=" + BuildConfig.THE_MOVIE_DATABASE_API_KEY;
        String reviewURL = TmdbUrls.MOVIE_URL + id + "api_key=" + TmdbUrls.REVIEW + "?" + BuildConfig.THE_MOVIE_DATABASE_API_KEY;
        Log.e("TAG", url);
        Log.e("TAG", reviewURL);
        JsonObjectRequest getDetails = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String imageUrl = "http://image.tmdb.org/t/p/w780/" + response.getString("backdrop_path");
                    String iconImageURL="http://image.tmdb.org/t/p/w342/" + response.getString("poster_path");

                    String genres = "";
                    JSONArray genreArray = response.getJSONArray("genres");

                    for (int i = 0; i < genreArray.length(); i++) {
                        String genre = genreArray.getJSONObject(i).getString("name");
                        if (i != genreArray.length() - 1)
                            genres += genre + ", ";
                        else
                            genres += genre + ".";
                    }
                    mMovieDetail = new MovieDetail(id,
                            response.getString("title"),
                            response.getString("vote_average"),
                            genres,
                            response.getString("release_date"),
                            response.getString("status"),
                            response.getString("overview"),
                            iconImageURL,
                            imageUrl,
                            response.getString("tagline"),
                            response.getString("original_language"),
                            response.getString("runtime"),
                            response.getString("popularity"),
                            response.getString("vote_count")
                    );
                    mAdapter = new DetailsAdapter(lightColor, mDetailsList, mReviewList, mTrailersList, getActivity());
                    mDetailsList.add(mMovieDetail);
                    mRecyclerView.setAdapter(mAdapter);
                    fetchMoviefromDB(mMovieDetail);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showSnackBar(getString(R.string.error_msg));
                if((mMovieDetail = ContentProviderHelperMethods.getMovieFromDatabase(getActivity(), id))!=null) {
                    setOffLineUI(mMovieDetail);
                    fetchMoviefromDB(mMovieDetail);
                }
            }
        });
        NetworkController.getInstance().addToRequestQueue(getDetails);
    }

    private void fetchTrailerData(final String id) {
        final String logtag="trailers";
        mTrailer = null;
        mTrailersList.clear();
        String requestUrl = TmdbUrls.MOVIE_URL + id + "/videos?" + "api_key=" + BuildConfig.THE_MOVIE_DATABASE_API_KEY;

        JsonObjectRequest mTrailerRequest = new JsonObjectRequest(requestUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray mResponseArray = response.getJSONArray("results");

                    for (int i = 0; i < mResponseArray.length(); i++) {
                        Log.e(logtag,mResponseArray.toString());
                        JSONObject mResponseObject = mResponseArray.getJSONObject(i);
                        trailerURL = mResponseObject.getString("key");
                        mTrailer = new Trailer(
                                mResponseObject.getString("key"),
                                mResponseObject.getString("name"),
                                mResponseObject.getString("site"),
                                mResponseObject.getString("size")
                        );
                        Log.e(logtag,mTrailer.getKey());
                        mTrailersList.add(mTrailer);
                    }
                    mAdapter = new DetailsAdapter(lightColor, mDetailsList, mReviewList, mTrailersList, getActivity());
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showSnackBar(getString(R.string.error_msg));
            }
        });

        NetworkController.getInstance().addToRequestQueue(mTrailerRequest);
    }

    private void fetchReviewData(final String id){
        mReview = null;
        mReviewList.clear();
        String reviewURL = TmdbUrls.MOVIE_URL + id + TmdbUrls.REVIEW + "?" + "api_key=" + BuildConfig.THE_MOVIE_DATABASE_API_KEY;
        Log.e("TAG", reviewURL);
        JsonObjectRequest getDetails = new JsonObjectRequest(reviewURL , null , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int size = response.getInt("total_results");
                        if (size != 0) {
                            JSONArray mResultArray = response.getJSONArray("results");
                            for (int i = 0; i < mResultArray.length(); i++) {
                                JSONObject mResultObject = mResultArray.getJSONObject(i);
                                mReview = new Review(
                                        mResultObject.getString("id"),
                                        mResultObject.getString("author"),
                                        mResultObject.getString("content"),
                                        mResultObject.getString("url"));

                                mReviewList.add(mReview);
                            }
                        }
                        mAdapter = new DetailsAdapter(lightColor, mDetailsList, mReviewList, mTrailersList, getActivity());
                        mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showSnackBar(getString(R.string.error_msg));
            }
        });
        NetworkController.getInstance().addToRequestQueue(getDetails);
    }

    void showSnackBar(String msg) {
        Snackbar.make(mRecyclerView, msg, Snackbar.LENGTH_LONG)
                .show();
    }
    public Intent shareIntent(String data) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, (getResources().getString(R.string.share_statement) + " " + data  + "."));
        return sharingIntent;
    }

}