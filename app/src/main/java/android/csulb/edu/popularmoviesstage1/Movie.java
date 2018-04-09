package android.csulb.edu.popularmoviesstage1;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable{
    private String id;
    private String title;
    private String image;
    private String plot;
    private String rating;
    private String release;

    public Movie() {
    }

    public Movie(String id, String title, String image, String plot, String rating, String release) {
        this.title = title;
        this.image = image;
        this.plot = plot;
        this.rating = rating;
        this.release = release;
    }

    private Movie(Parcel in){
        setId(in.readString());
        setTitle(in.readString());
        setImage(in.readString());
        setPlot(in.readString());
        setRating(in.readString());
        setRelease(in.readString());
    }

    public void setId(String id){
        this.id = id;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public void setRating(String rating){
        this.rating = rating;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getId(){
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getPlot() {
        return plot;
    }

    public String getRating() {
        return rating;
    }

    public String getRelease() {
        return release;
    }

    public String toString(){
        return getId() + "---" + getTitle() + "---" + getImage() + "---" + getPlot() + "---" + getRating() + "---" + getRelease();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getId());
        parcel.writeString(getTitle());
        parcel.writeString(getImage());
        parcel.writeString(getPlot());
        parcel.writeString(getRating());
        parcel.writeString(getRelease());
    }

    public final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>(){

        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }
    };
}

