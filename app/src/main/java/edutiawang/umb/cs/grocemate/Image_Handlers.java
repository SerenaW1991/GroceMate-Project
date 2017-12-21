package edutiawang.umb.cs.grocemate;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

/**
 * Created by tengwang on 12/17/17.
 */

public class Image_Handlers {

    public float[] rgbValuesFromBitmap(Bitmap bitmap) {
        ColorMatrix colorMatrix = new ColorMatrix();
        ColorFilter colorFilter = new ColorMatrixColorFilter(
                colorMatrix);
        Bitmap argbBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(argbBitmap);

        Paint paint = new Paint();

        paint.setColorFilter(colorFilter);
        canvas.drawBitmap(bitmap, 0, 0, paint);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int componentsPerPixel = 3;
        float[] ratio = new float[componentsPerPixel];
        int totalPixels = width * height;
        int totalBytes = totalPixels * componentsPerPixel;

        byte[] rgbValues = new byte[totalBytes];

        int[] argbPixels = new int[totalPixels];
        argbBitmap.getPixels(argbPixels, 0, width, 0, 0, width, height);

        float r_sum = 0, g_sum = 0, b_sum = 0;

        for (int i = 0; i < totalPixels; i++) {
            int argbPixel = argbPixels[i];
            int red = Color.red(argbPixel);
            int green = Color.green(argbPixel);
            int blue = Color.blue(argbPixel);
            rgbValues[i * componentsPerPixel + 0] = (byte) red;
            r_sum += red;
            rgbValues[i * componentsPerPixel + 1] = (byte) green;
            g_sum += green;
            rgbValues[i * componentsPerPixel + 2] = (byte) blue;
            b_sum += blue;
        }

        float total_sum= r_sum + g_sum + b_sum;
        ratio[0] = r_sum/total_sum; ratio[1] = g_sum/total_sum; ratio[2] = b_sum/total_sum;
        System.out.println(ratio[0] + " "+ ratio[1] +" " + ratio[2]);
        return ratio;
    }
}
