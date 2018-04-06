package android.csulb.edu.popularmoviesstage1.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    public static URL buildImageUrl(String imageUrl){
        return null;
    }

    public static URL buildDetailUrl(String detailUrl){

        return null;
    }

    public static URL buildPopularUrl(String popularString){
        //testing right now
        URL popularUrl = null;
        try{
           popularUrl = new URL(popularString);
        }catch (MalformedURLException e) {
            e.printStackTrace();
            }

            return popularUrl;
    }

    /**
     * This method returns the entire result from the HTTP response.
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
