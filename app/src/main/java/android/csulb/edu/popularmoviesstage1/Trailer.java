package android.csulb.edu.popularmoviesstage1;

import android.os.Parcel;
import android.os.Parcelable;

public class Trailer implements Parcelable {

    private String name; //the name of the trailer
    private String key;  //the key used in youtube URL

    public Trailer(String name, String key){
        this.name = name;
        this.key = key;
    }

    private Trailer(Parcel in){
        setName(in.readString());
        setKey(in.readString());
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setKey(String key){
        this.key = key;
    }

    public String getName(){
        return name;
    }

    public String getKey(){
        return key;
    }

    public String toString(){
        return getName() + "---" + getKey();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getName());
        parcel.writeString(getKey());
    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>(){

        @Override
        public Trailer createFromParcel(Parcel parcel) {
            return new Trailer(parcel);
        }

        @Override
        public Trailer[] newArray(int i) {
            return new Trailer[i];
        }
    };
}
