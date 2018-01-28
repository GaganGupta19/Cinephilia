package com.moviephilia.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.ImageViewTarget;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import com.moviephilia.Classes.Movie;
import com.moviephilia.Palette.PaletteBitmap;
import com.moviephilia.Palette.PaletteBitmapTranscoder;
import com.moviephilia.R;


/**
 * Created by Gagan on 4/16/2016.
 */
public class mListAdapter extends RecyclerView.Adapter<mListAdapter.ViewHolder> {

    private String TAG="mListAdapter";
    private  BitmapRequestBuilder<String, PaletteBitmap> glideRequest;
    private @ColorInt
    int defaultColor;
    public OnAdapterItemClickListener getItemClickListener;//callback

    private  ArrayList<Movie> mMovieList=new ArrayList<>();

    private Activity mAct;
    private LayoutInflater mInflater;

    public mListAdapter(RequestManager glide, ArrayList<Movie> mMoviesList, Activity activity){
        this.mMovieList=mMoviesList;
        getItemClickListener = (OnAdapterItemClickListener) activity;
        this.mAct = activity;
        this.defaultColor = ContextCompat.getColor(activity, R.color.default_background);
        this.glideRequest = glide
                .fromString()
                .asBitmap()
                .transcode(new PaletteBitmapTranscoder(activity), PaletteBitmap.class)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        mInflater = (LayoutInflater) this.mAct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.movie_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        String imageUrl = "http://image.tmdb.org/t/p/w185/" + mMovieList.get(position).getPoster();//+mImageList.get(position);
        String backDropPath= "http://image.tmdb.org/t/p/w342/" + mMovieList.get(position).getBackdrop(); //mImageListBackDrop.get(position);
        if(mMovieList.get(position).getPosterUrl(this.mAct) !=null || mMovieList.get(position).getBackdropUrl(this.mAct) !=null) {
            glideRequest
                    .load(backDropPath)
                    .diskCacheStrategy( DiskCacheStrategy.ALL) //Storing only compressed images for saving disk space (Disc Cache)
                    .into(new ImageViewTarget<PaletteBitmap>(holder.getImageView()) {
                        @Override protected void setResource(PaletteBitmap resource) {
                            super.view.setImageBitmap(resource.bitmap);
                            holder.setDarkColor(resource.palette.getDarkVibrantColor(defaultColor));
                            holder.setLightColor(resource.palette.getDarkMutedColor(defaultColor));
                            holder.getCardView().setCardBackgroundColor(holder.getDarkColor());
                            holder.getTitleView().setTextColor(ContextCompat.getColor(mAct, android.R.color.white));
                        }
                    });
            Glide.with(mAct)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .centerCrop()
                    .crossFade()
                    .into(holder.getmRoundedImageView());

        }
        else {
            //if json data contains nothing
            Glide.clear(holder.getImageView());
            Glide.clear(holder.getmRoundedImageView());
            holder.getImageView().setVisibility(View.GONE);
            holder.getmRoundedImageView().setVisibility(View.GONE);
        }
        holder.getTitleView().setText(mMovieList.get(position).getTitle());
        holder.getTitleView().setTextSize(15);

        if(mMovieList.get(position).getUserRating()!=null && mMovieList.get(position).getUserRating() !="0"){
            holder.getRatingValue().setText(mMovieList.get(position).getUserRating());
            holder.getmIcon().setImageResource(R.mipmap.ic_stars_white_18dp);
        }else{
            holder.getRatingValue().setText("Rating not available.");
            holder.getmIcon().setVisibility(View.GONE);
        }


        holder.getCardView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getItemClickListener != null) {
                    getItemClickListener.getItemDetails(mMovieList.get(position).getId(),
                            mMovieList.get(position).getBackdrop(),
                            mMovieList.get(position).getTitle(),
                            holder.getDarkColor(),
                            holder.getLightColor());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }
    public interface OnAdapterItemClickListener{
        void getItemDetails(String id,String backdrop, String title, int darkcolor, int lightcolor );
    }

    //View Holder Class
    public static class ViewHolder extends RecyclerView.ViewHolder {

        int darkColor;
        int lightColor;
        private TextView mViewTitle;
        private CircleImageView mRoundedImageView;
        private ImageView mImageView;
        private CardView mCardView;


        private ImageView mIcon;
        private TextView ratingValue;


        public ViewHolder(View v) {
            super(v);

            mCardView = (CardView) v.findViewById(R.id.card_view);
            mViewTitle = (TextView) v.findViewById(R.id.title);
            mImageView = (ImageView) v.findViewById(R.id.image);
            mRoundedImageView = (CircleImageView) v.findViewById(R.id.roundedImage);
            darkColor = Color.parseColor("#60000000");
            lightColor = Color.parseColor("#60000000");
            mIcon=(ImageView)v.findViewById(R.id.ratingicon);
            ratingValue=(TextView) v.findViewById(R.id.starvalue);
        }

        public ImageView getmIcon() {
            return mIcon;
        }

        public TextView getRatingValue() {
            return ratingValue;
        }

        public CardView getCardView() {
            return mCardView;
        }

        public TextView getTitleView() {
            return mViewTitle;
        }

        public ImageView getImageView() {
            return mImageView;
        }

        public CircleImageView getmRoundedImageView(){return mRoundedImageView; }

        public int getDarkColor() {
            return darkColor;
        }

        public void setDarkColor(int color) {
            darkColor = color;
        }

        public int getLightColor() {
            return lightColor;
        }

        public void setLightColor(int color) {
            lightColor = color;
        }
    }
}
