package edutiawang.umb.cs.grocemate;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by tengwang on 12/16/17.
 */

public class Flickr_functions {
    private static final String FLICKR_PHOTOS_SEARCH_STRING = "flickr.photos.search";
    private static final String FLICKR_BASE_URL = "https://api.flickr.com/services/rest/?method=";
    private static final String APIKEY_SEARCH_STRING = "&api_key=cf638d1a814322c39e845db2e4c72138";
//    private static final String API_SECRET = "361f36f371191dad";
    private static final int NUMBER_OF_PHOTOS = 6;

    private static final String TAGS_STRING = "&tags=";
    private static final String FORMAT_STRING = "&format=json";

    public static String createURL(String parameter) {
        String url = FLICKR_BASE_URL + FLICKR_PHOTOS_SEARCH_STRING +
                APIKEY_SEARCH_STRING + TAGS_STRING + parameter +
                FORMAT_STRING + "&per_page="+NUMBER_OF_PHOTOS+"&media=photos";
        return url;
    }

    public static Bitmap getImage(String image_url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(image_url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (Exception e) {
            Log.d("Flickr_functions ",e.getMessage());
        }
        return bm;
    }


}
