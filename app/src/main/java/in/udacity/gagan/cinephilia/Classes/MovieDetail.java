package in.udacity.gagan.cinephilia.Classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gagan on 6/9/2016.
 */
public class MovieDetail implements Parcelable {

    String id;
    String title;
    String vote_average;
    String genre;
    String releasedate;
    String status;
    String overview;
    String poster_path;
    String backdrop_path;
    private String mTagline;
    private String mLanguage;
    private String mRuntime;
    private String mPopularity;
    private String mVotecount;

    private MovieDetail(){}

    public MovieDetail(String id,String title,String vote_average,String genre,String releasedate,
                       String status,String overview,String poster_path,String backdrop_path,
                       String tagline, String language, String runtime, String popularity, String vote_count){
        this.id = id;
        this.title = title;
        this.vote_average = vote_average;
        this.genre = genre;
        this.releasedate = releasedate;
        this.status = status;
        this.overview = overview;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        mTagline = tagline;
        mLanguage = language;
        mRuntime = runtime;
        mPopularity = popularity;
        mVotecount = vote_count;
    }
    public String getId() {
        return id;
    }

    public String getmVotecount() {
        return mVotecount;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getTitle() {
        return title;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getGenre() {
        return genre;
    }

    public String getReleasedate() {
        return releasedate;
    }

    public String getStatus() {
        return status;
    }

    public String getOverview() {
        return overview;
    }

    public String getmTagline() {
        return mTagline;
    }

    public String getmLanguage() {
        return mLanguage;
    }

    public String getmRuntime() {
        return mRuntime;
    }

    public String getmPopularity() {
        return mPopularity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(vote_average);
        dest.writeString(genre);
        dest.writeString(releasedate);
        dest.writeString(status);
        dest.writeString(overview);
        dest.writeString(backdrop_path);
        dest.writeString(poster_path);
        dest.writeString(mTagline);
        dest.writeString(mLanguage);
        dest.writeString(mRuntime);
        dest.writeString(mPopularity);
        dest.writeString(mVotecount);

    }

    public static final Parcelable.Creator<MovieDetail> CREATOR = new Creator<MovieDetail>() {
        @Override
        public MovieDetail createFromParcel(Parcel source) {
            MovieDetail mMovieDetail=new MovieDetail();
            mMovieDetail.title = source.readString();
            mMovieDetail.vote_average = source.readString();
            mMovieDetail.genre = source.readString();
            mMovieDetail.releasedate = source.readString();
            mMovieDetail.status = source.readString();
            mMovieDetail.overview = source.readString();
            mMovieDetail.backdrop_path = source.readString();
            mMovieDetail.poster_path = source.readString();
            mMovieDetail.mTagline = source.readString();
            mMovieDetail.mLanguage = source.readString();
            mMovieDetail.mRuntime = source.readString();
            mMovieDetail.mPopularity = source.readString();
            mMovieDetail.mVotecount = source.readString();
            return mMovieDetail;
        }

        @Override
        public MovieDetail[] newArray(int size) {
            return new MovieDetail[size];
        }
    };
}
