package in.udacity.gagan.cinephilia.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import in.udacity.gagan.cinephilia.Classes.MovieDetail;
import in.udacity.gagan.cinephilia.Classes.Review;
import in.udacity.gagan.cinephilia.Classes.Trailer;
import in.udacity.gagan.cinephilia.Extras.TmdbUrls;
import in.udacity.gagan.cinephilia.R;

/**
 * Created by Gagan on 4/23/2016.
 */
public class DetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Trailer> mTrailerList = new ArrayList<>();
    private ArrayList<MovieDetail> mMovieList = new ArrayList<>();
    private ArrayList<Review> mReviewList = new ArrayList<>();
    private Activity mAct;
    private @ColorInt
    ViewHolderReview viewHolderReview;
    ViewHolderDetails info;
    ViewHolderTrailer viewHolderTrailer;

    int color;
    private LayoutInflater mInflater;

    public DetailsAdapter(int color, ArrayList<MovieDetail> mMovieList, ArrayList<Review> mReviewList, ArrayList<Trailer> mTrailerList, Activity activity) {

        this.mTrailerList = mTrailerList;
        this.mMovieList = mMovieList;
        this.mReviewList = mReviewList;
        this.mAct = activity;
        this.color = color;
        mInflater = (LayoutInflater) this.mAct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        RecyclerView.ViewHolder vh;
        switch (viewType) {
            case 0:
                View viewInfo = mInflater.inflate(R.layout.final_movie_details, parent, false);
                vh = new ViewHolderDetails(viewInfo);
                return vh;
            case 1:
                View viewTrailer = mInflater.inflate(R.layout.trailers, parent, false);
                vh = new ViewHolderTrailer(viewTrailer);
                return vh;

            case 2:
                View viewReviews = mInflater.inflate(R.layout.review, parent, false);
                vh = new ViewHolderReview(viewReviews);
                return vh;
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return 0;
        else if(position > 0 && position <= mTrailerList.size())
            return 1;
        else if(position > mTrailerList.size() && position <= mReviewList.size() + mTrailerList.size())
            return 2;

        return 99;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MovieDetail movieDetail = mMovieList.get(0);
        switch (getItemViewType(position)) {
            case 0:
                info = ((ViewHolderDetails) holder);
                if(movieDetail.getPoster_path()!=null) {

                    Glide.with(mAct)
                            .load(movieDetail.getPoster_path())
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .crossFade()
                            .into(info.getImageView());
                }
                else{
                    Glide.clear(info.getImageView());
                }

                info.getTitleView().setText(movieDetail.getTitle());
                info.getTitleView().setTextColor(color);
                if (!movieDetail.getmTagline().equals(""))
                    info.getTaglineView().setText("\"" + movieDetail.getmTagline() + "\"");
                else
                    info.getTaglineView().setVisibility(View.GONE);

                info.getDateStatusView().setText(movieDetail.getReleasedate()
                        + " (" + movieDetail.getStatus() + ")");
                info.getDurationView().setText(mAct.getString(R.string.duration)
                        + movieDetail.getmRuntime() + mAct.getString(R.string.min));
                info.getRatingView().setText(movieDetail.getVote_average());
                try {
                    info.getGenreView().setText(movieDetail.getGenre());
                } catch (StringIndexOutOfBoundsException e) {
                    e.printStackTrace();
                    info.getGenreView().setText(movieDetail.getGenre());
                }
                info.getPopularityView().setText(movieDetail.getmPopularity().substring(0, 4));
                info.getLanguageView().setText(movieDetail.getmLanguage());
                info.getOverviewView().setText(movieDetail.getOverview());
                info.getVoteCountView().setText(movieDetail.getmVotecount() + " votes");

                info.getRatingsBackground().getDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                info.getGenreBackground().getDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                info.getPopBackground().getDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                info.getLangBackground().getDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                break;

            case 1:
                final Trailer mtrailer = mTrailerList.get(position - 1);
                viewHolderTrailer = ((ViewHolderTrailer) holder);
                if(mtrailer.getTrailerUrl()!=null) {
                    Glide.with(mAct)
                            .load(TmdbUrls.TRAILER_THUMBNAIL + mtrailer.getKey() + TmdbUrls.YOUTUBE_QUALITY)
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .crossFade()
                            .into(viewHolderTrailer.getImageView());
                }
                else{
                    Glide.clear(viewHolderTrailer.getImageView());
                }
                viewHolderTrailer.getTitleView().setText(mtrailer.getName());
                viewHolderTrailer.getTitleView().setTextColor(color);
                viewHolderTrailer.getSiteView().setText(mAct.getString(R.string.site) + mtrailer.getmSite());
                viewHolderTrailer.getQualityView().setText(mAct.getString(R.string.quality) + mtrailer.getSize() + "p");

                viewHolderTrailer.getImageView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAct.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mtrailer.getTrailerUrl())));
                    }
                });
                break;
            case 2:
                Review review = mReviewList.get(position - (mTrailerList.size() + 1));
                if(review!=null) {
                    viewHolderReview = ((ViewHolderReview) holder);
                    viewHolderReview.getmAuthor().setText(review.getAuthor());
                    viewHolderReview.getmAuthor().setTextColor(color);
                    viewHolderReview.getmContent().setText(review.getContent());
                }
                break;
        }

    }

    @Override
    public int getItemCount() {
        return (mMovieList.size()+mReviewList.size()+mTrailerList.size());
    }

    public static class ViewHolderDetails extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView titleView, taglineView, dateStatusView, durationView,
                ratingView, genreView, popularityView, languageView, overviewView, voteCountView;
        private ImageView ratingsBackground, genreBackground, popBackground, langBackground;

        public ViewHolderDetails(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.image);
            titleView = (TextView) v.findViewById(R.id.title);
            taglineView = (TextView) v.findViewById(R.id.tagline);
            dateStatusView = (TextView) v.findViewById(R.id.date_status);
            durationView = (TextView) v.findViewById(R.id.duration);
            ratingView = (TextView) v.findViewById(R.id.rating);
            genreView = (TextView) v.findViewById(R.id.genre);
            popularityView = (TextView) v.findViewById(R.id.popularity);
            languageView = (TextView) v.findViewById(R.id.language);
            overviewView = (TextView) v.findViewById(R.id.overview);
            ratingsBackground = (ImageView) v.findViewById(R.id.ratings_background);
            voteCountView = (TextView) v.findViewById(R.id.vote_count);
            genreBackground = (ImageView) v.findViewById(R.id.genre_background);
            popBackground = (ImageView) v.findViewById(R.id.pop_background);
            langBackground = (ImageView) v.findViewById(R.id.lang_background);
        }

        public ImageView getImageView() {
            return imageView;
        }

        public TextView getTitleView() {
            return titleView;
        }

        public TextView getTaglineView() {
            return taglineView;
        }

        public TextView getDateStatusView() {
            return dateStatusView;
        }

        public TextView getDurationView() {
            return durationView;
        }

        public TextView getRatingView() {
            return ratingView;
        }

        public TextView getGenreView() {
            return genreView;
        }

        public TextView getPopularityView() {
            return popularityView;
        }

        public TextView getLanguageView() {
            return languageView;
        }

        public TextView getOverviewView() {
            return overviewView;
        }

        public ImageView getRatingsBackground() {
            return ratingsBackground;
        }

        public TextView getVoteCountView() {
            return voteCountView;
        }

        public ImageView getGenreBackground() {
            return genreBackground;
        }

        public ImageView getPopBackground() {
            return popBackground;
        }

        public ImageView getLangBackground() {
            return langBackground;
        }

    }

    public static class ViewHolderTrailer extends RecyclerView.ViewHolder {

    private ImageView imageView;
    private TextView titleView, siteView, qualityView;

    public ViewHolderTrailer(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.trailer_image);
        titleView = (TextView) itemView.findViewById(R.id.title_text);
        siteView = (TextView) itemView.findViewById(R.id.site_text);
        qualityView = (TextView) itemView.findViewById(R.id.quality_text);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public TextView getTitleView() {
        return titleView;
    }

    public TextView getSiteView() {
        return siteView;
    }

    public TextView getQualityView() {
        return qualityView;
    }
}

    public static class ViewHolderReview extends RecyclerView.ViewHolder {

        private TextView mAuthor;
        private TextView mContent;

        public ViewHolderReview(View v) {
            super(v);
            mAuthor= (TextView) v.findViewById(R.id.author_name);
            mContent = (TextView) v.findViewById(R.id.content);
        }
        public TextView getmAuthor() {
            return mAuthor;
        }

        public void setmAuthor(TextView mAuthor) {
            this.mAuthor = mAuthor;
        }

        public TextView getmContent() {
            return mContent;
        }

        public void setmContent(TextView mContent) {
            this.mContent = mContent;
        }

    }
}