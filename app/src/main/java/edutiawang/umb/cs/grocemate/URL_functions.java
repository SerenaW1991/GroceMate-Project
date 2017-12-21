package edutiawang.umb.cs.grocemate;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by tengwang on 12/16/17.
 */

public class URL_functions extends AsyncTask<String, Bitmap, List<Bitmap>> {
    List<ImageView> image_views = new LinkedList<ImageView>();
    private TaskCompleted mCallback;

    URL_functions(List<ImageView> image_views, TaskCompleted mCallback){
        this.image_views = image_views;
        this.mCallback = mCallback;
    }


    @Override
    protected List<Bitmap> doInBackground(String... url_string) {
        JSONObject result = null;
        List<URL> image_urls = new LinkedList<URL>();
        List<Bitmap> bitmaps = new LinkedList<Bitmap>();
        try {

            HttpURLConnection urlConnection = null;
            URL url = new URL(url_string[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setDoOutput(true);
            urlConnection.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();

            String jsonString = sb.toString();
//            14 means get rid of flickr api at the beginning, start from {
            result = new JSONObject(jsonString.substring(14, jsonString.length() - 1));

//  jsonFlickrApi({"photos":{"page":1,"pages":4580,"perpage":6,"total":"27476","photo":
//  [{"id":"38344258784","owner":"30478819@N08","secret":"c1941b6469","server":"4535","farm":5,"title":"Cold Medecine on a White Background","ispublic":1,"isfriend":0,"isfamily":0},
//  the url of the image is at  http://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg
                JSONObject photos = result.getJSONObject("photos");
                JSONArray imageJSONArray = null;

                imageJSONArray = photos.getJSONArray("photo");

                for (int i = 0; i < imageJSONArray.length(); i++) {
                    JSONObject item = imageJSONArray.getJSONObject(i);
                    String id = item.getString("id");
                    String secret = item.getString("secret");
                    String server = item.getString("server");
                    String farm = item.getString("farm");

                    URL new_url = new URL("https://farm" + farm + ".staticflickr.com/" + server + "/" + id + "_" + secret + ".jpg");
                    image_urls.add(new_url);
                }

            Bitmap bitmap = null;
            InputStream input = null;

            for (int i=0; i<image_urls.size(); i++){
                input = image_urls.get(i).openStream();
                bitmap = BitmapFactory.decodeStream(input);
                bitmaps.add(bitmap);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bitmaps;
    }


    @Override
    protected void onPostExecute(List<Bitmap> bitmaps){
        List<float[]> ratio_list = null;
        if (bitmaps.size() == image_views.size()){

            for (int i=0; i<image_views.size(); i++){
                image_views.get(i).setImageBitmap(bitmaps.get(i));
            }

            Image_Handlers handle_image = new Image_Handlers();
            ratio_list = new LinkedList<float[]>();

            for (Bitmap each_bitmap : bitmaps){
                float[] ratio = handle_image.rgbValuesFromBitmap(each_bitmap);
                ratio_list.add(ratio);
            }

        }

        mCallback.onTaskComplete(ratio_list);
    }

}
