package android.csulb.edu.popularmoviesstage1;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable{
    String author;
    String content;
    String reviewId;

    public Review(String author, String content, String reviewId){
        this.author = author;
        this.content = content;
        this.reviewId = reviewId;
    }

    private Review(Parcel in){
        setAuthor(in.readString());
        setContent(in.readString());
        setReviewId(in.readString());
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public void setContent(String content){
        this.content = content;
    }

    public void setReviewId(String reviewId){
        this.reviewId = reviewId;
    }

    public String getAuthor(){
        return author;
    }

    public String getContent(){
        return content;
    }

    public String getReviewId(){
        return reviewId;
    }

    public String toString(){
        return getAuthor() + "---" + getContent() + "---" + getReviewId();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getAuthor());
        parcel.writeString(getContent());
        parcel.writeString(getReviewId());
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>(){
        @Override
        public Review createFromParcel(Parcel parcel) {
            return new Review(parcel);
        }

        @Override
        public Review[] newArray(int i) {
            return new Review[i];
        }
    };
}
