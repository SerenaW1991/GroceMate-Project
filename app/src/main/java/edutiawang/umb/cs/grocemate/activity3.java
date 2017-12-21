package edutiawang.umb.cs.grocemate;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class activity3 extends AppCompatActivity implements SurfaceHolder.Callback,Camera.PreviewCallback {

    SurfaceView cameraView,transparentView;
    SurfaceHolder holder,holderTransparent;

    ImageUtilize imageUtilize = null;
    Camera camera;

    String image_ratios = "";
    final int example_num = 6;

    private float RectLeft, RectTop,RectRight,RectBottom ;
    int  deviceHeight,deviceWidth;
    int WIDTH, HEIGHT;
    private int[] pixels;

    private double[][] sample_ratio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity3);
        cameraView = (SurfaceView)findViewById(R.id.CameraView);
        holder = cameraView.getHolder();

        holder.addCallback((SurfaceHolder.Callback) this);

        // Create second surface with another holder (holderTransparent)

        transparentView = (SurfaceView)findViewById(R.id.TransparentView);
        holderTransparent = transparentView.getHolder();
        holderTransparent.addCallback((SurfaceHolder.Callback) this);
        holderTransparent.setFormat(PixelFormat.TRANSLUCENT);
        transparentView.setZOrderMediaOverlay(true);

        //getting the device heigth and width

        deviceWidth=getScreenWidth();
        deviceHeight=getScreenHeight();

        pixels = new int[deviceHeight * deviceWidth];
        image_ratios = getIntent().getStringExtra("color_ratios");

        System.out.println(image_ratios);

        sample_ratio = getSampleRatios();

        RectLeft = 0;
        RectTop = 0;
        RectRight = 0;
        RectBottom =0;

    }

    double[][] getSampleRatios(){
        sample_ratio = new double[example_num][3];
        String[] each_image = image_ratios.split("\n");

        for (int i=0; i<each_image.length; i++){
            String[] each_num_in_image = each_image[i].split(",");
            for (int j=0; j<each_num_in_image.length; j++){
                sample_ratio[i][j] = Double.parseDouble(each_num_in_image[j]);
            }
        }

        System.out.println(sample_ratio[0][0]);
        return sample_ratio;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }


    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }




    private void Draw()
    {
        Canvas canvas = holderTransparent.lockCanvas(null);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);

        paint.setStrokeWidth(3);

        Rect rec=new Rect((int) RectLeft,(int)RectTop,(int)RectRight,(int)RectBottom);
        canvas.drawRect(rec,paint);
//        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        holderTransparent.unlockCanvasAndPost(canvas);
    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            synchronized (holder)
            {Draw();}   //call a draw method
            camera = Camera.open(); //open a camera
        }

        catch (Exception e) {
            return;

        }

        Camera.Parameters param;
        param = camera.getParameters();

        param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);

        Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();

        if(display.getRotation() == Surface.ROTATION_0)
        {
            camera.setDisplayOrientation(90);
        }

        camera.setParameters(param);

        try {
            camera.setPreviewDisplay(holder);
            camera.setPreviewCallback(this);
            camera.startPreview();
        }

        catch (Exception e) {
            return;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        refreshCamera();
    }

    public void refreshCamera() {

        if (holder.getSurface() == null) {
            return;
        }

        try {
            camera.stopPreview();
        }

        catch (Exception e) {}

        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        }

        catch (Exception e) {}

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.release(); //for release a camera
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
//        WIDTH = camera.getParameters().getPreviewSize().width;
//        HEIGHT = camera.getParameters().getPreviewSize().height;

        WIDTH = deviceWidth; HEIGHT =deviceHeight;
        imageUtilize = new ImageUtilize(WIDTH, HEIGHT);
        imageUtilize.decodeYUV420SP(pixels, data, 0, 0);

        int[] location = imageUtilize.search_grid_by_grid(pixels, data, sample_ratio);


        try {
            synchronized (holder) {

                if (location != null){
                    RectLeft = location[0];
                    RectRight = WIDTH - (location[0] + WIDTH / 100);
                    RectTop = location[1];
                    RectBottom = RectTop + HEIGHT/100;

                    Draw();
                }
            }
        } catch (Exception e) {
        }
    }

}
