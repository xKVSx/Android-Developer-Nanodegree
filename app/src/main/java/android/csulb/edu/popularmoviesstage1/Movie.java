package android.csulb.edu.popularmoviesstage1;

public class Movie {
    String title, image, plot, rating, release;

    public Movie() {
    }

    public Movie(String title, String image, String plot, String rating, String release) {
        this.title = title;
        this.image = image;
        this.plot = plot;
        this.rating = rating;
        this.release = release;
    }

    void setTitle(String title){
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
}
