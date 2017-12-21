package edutiawang.umb.cs.grocemate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by tengwang on 12/19/17.
 */

public class camera_overlay extends SurfaceView implements SurfaceHolder.Callback {

    SurfaceHolder surfaceHolder;

    SurfaceView cameraView = null;

    private float RectLeft, RectTop,RectRight,RectBottom ;
    int  deviceHeight,deviceWidth;

    Camera camera = null;

    @SuppressLint("WrongViewCast")
    public camera_overlay(Context context, Camera camera) {
        super(context);
        this.camera = camera;
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        cameraView = findViewById(R.id.camera_preview);

        deviceWidth=getScreenWidth();
        deviceHeight=getScreenHeight();
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


    private void Draw()

    {
        Canvas canvas = surfaceHolder.lockCanvas(null);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(5);

        RectLeft = 1;
        RectTop = 200 ;
        RectRight = RectLeft+ deviceWidth-100;
        RectBottom =RectTop+ 200;

        Rect rec=new Rect((int) RectLeft,(int)RectTop,(int)RectRight,(int)RectBottom);

        canvas.drawRect(rec,paint);
        surfaceHolder.unlockCanvasAndPost(canvas);

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        try {
            synchronized (surfaceHolder)
            {Draw();}   //call a draw method
        }

        catch (Exception e) {
        }

//        Camera.Parameters param;
//        param = camera.getParameters();
//
//        param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);

//        Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
//        if(display.getRotation() == Surface.ROTATION_0)
//        {
//            camera.setDisplayOrientation(90);
//        }
//        camera.setParameters(param);

//        try {
//
//            camera.setPreviewDisplay(surfaceHolder);
////            camera.startPreview();
//        }
//
//        catch (Exception e) {
//            return;
//
//        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }

}
