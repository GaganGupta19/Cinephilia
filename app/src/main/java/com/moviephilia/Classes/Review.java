package com.moviephilia.Classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gagan on 6/9/2016.
 */

public class Review implements Parcelable {

    private String mId;
    private String mAuthor;
    private String mContent;
    private String mUrl;

    private Review(){

    }
    public Review(String mId,String mAuthor,String mContent,String mUrl){
        this.mId = mId;
        this.mAuthor = mAuthor;
        this.mContent = mContent;
        this.mUrl = mUrl;
    }
    public String getContent() {
        return mContent;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getUrl() {
        return mUrl;
    }

    public static final Parcelable.Creator<Review> CREATOR = new Creator<Review>() {
        public Review createFromParcel(Parcel source) {
            Review review = new Review();
            review.mId = source.readString();
            review.mAuthor = source.readString();
            review.mContent = source.readString();
            review.mUrl = source.readString();
            return review;
        }

        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mId);
        parcel.writeString(mAuthor);
        parcel.writeString(mContent);
        parcel.writeString(mUrl);
    }
}

