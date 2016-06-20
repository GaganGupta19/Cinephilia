package in.udacity.gagan.cinephilia.Classes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gagan on 6/9/2016.
 */
public class Trailer implements Parcelable {

    private String mId;
    private String mKey;
    private String mName;



    private String mSite;

    private String mSize;

    // Only for createFromParcel
    private Trailer() {
    }
    public Trailer(String mKey,
                   String mName,
                   String mSite,
                   String mSize){

        this.mKey = mKey;
        this.mName = mName;
        this.mSite = mSite;
        this.mSize = mSize;
    }
    public String getmSite() {
        return mSite;
    }
    public String getSize() {
        return mSize;
    }
    public String getName() {
        return mName;
    }

    public String getKey() {
        return mKey;
    }

    public String getTrailerUrl() {
        return "http://www.youtube.com/watch?v=" + mKey;
    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Creator<Trailer>() {
        public Trailer createFromParcel(Parcel source) {
            Trailer trailer = new Trailer();
            trailer.mId = source.readString();
            trailer.mKey = source.readString();
            trailer.mName = source.readString();
            trailer.mSite = source.readString();
            trailer.mSize = source.readString();
            return trailer;
        }

        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mId);
        parcel.writeString(mKey);
        parcel.writeString(mName);
        parcel.writeString(mSite);
        parcel.writeString(mSize);
    }
}

