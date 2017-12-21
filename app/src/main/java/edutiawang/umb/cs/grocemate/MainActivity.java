package edutiawang.umb.cs.grocemate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskCompleted {
    EditText input;
    List<ImageView> images = null;
    List<float[]> ratios = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        input = (EditText) findViewById(R.id.input_item);

        images = new LinkedList<ImageView>();

        images.add((ImageView) findViewById(R.id.image1));
        images.add((ImageView) findViewById(R.id.image2));
        images.add((ImageView) findViewById(R.id.image3));
        images.add((ImageView) findViewById(R.id.image4));
        images.add((ImageView) findViewById(R.id.image5));
        images.add((ImageView) findViewById(R.id.image6));

        setSupportActionBar(toolbar);
    }

    public void sendMessage(View view)
    {

        Flickr_functions flickr_func = new Flickr_functions();
        String search = flickr_func.createURL(input.getText().toString());

        URL_functions get_respond = new URL_functions(images, this);
        get_respond.execute(search);
    }

    public void go_to_video(View view){
        Intent intent = new Intent(MainActivity.this, activity3.class);
        String toPass = "";

        if (ratios.size() >0){
            for (int i=0; i<ratios.size(); i++){
                toPass += ratios.get(i)[0] + "," + ratios.get(i)[1] +"," + ratios.get(i)[2] + "\n";
            }
        }else{
            toPass = "";
        }
        System.out.println(toPass);

        intent.putExtra("color_ratios", toPass);
        startActivity(intent);
    }


    @Override
    public void onTaskComplete(List toCarry) {
        this.ratios = toCarry;
    }
}
