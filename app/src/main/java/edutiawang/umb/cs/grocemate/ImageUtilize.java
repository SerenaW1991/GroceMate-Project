package edutiawang.umb.cs.grocemate;

/**
 * Created by tengwang on 12/20/17.
 */

public class ImageUtilize {

    private int WIDTH, HEIGHT;
    private int grid_WIDTH, grid_HEIGHT;
    private int grid_num;
    private double THRESH = 0.1;

    ImageUtilize(int WIDTH, int HEIGHT){
        this.WIDTH = WIDTH; this.HEIGHT = HEIGHT;
        grid_num = 10;
        grid_WIDTH = WIDTH/grid_num; grid_HEIGHT = HEIGHT/grid_num;
    }



    double[] decodeYUV420SP(int[] rgb, byte[] yuv420sp, int leftx, int lefty) {

        int frameSize = grid_HEIGHT * grid_WIDTH;
        double[] ratios = new double[3];

        for (int j = lefty, yp = 0; j < lefty+grid_HEIGHT; j++) {
            int uvp = frameSize + (j >> 1) * grid_WIDTH, u = 0, v = 0;

            for (int i = leftx; i < leftx + grid_WIDTH; i++, yp++) {
                int y = (0xff & ((int) yuv420sp[yp])) - 16;
                if (y < 0)
                    y = 0;
                if ((i & 1) == 0) {
                    v = (0xff & yuv420sp[uvp++]) - 128;
                    u = (0xff & yuv420sp[uvp++]) - 128;
                }

                int y1192 = 1192 * y;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);

                if (r < 0)                  r = 0;               else if (r > 262143)
                    r = 262143;
                if (g < 0)                  g = 0;               else if (g > 262143)
                    g = 262143;
                if (b < 0)                  b = 0;               else if (b > 262143)
                    b = 262143;

                ratios[0] +=r; ratios[1] +=g; ratios[2] += b;
                rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
            }

        }

        double all_sum = ratios[0] + ratios[1] + ratios[2];
        ratios[0] = ratios[0]/all_sum; ratios[1] = ratios[1]/all_sum; ratios[2] = ratios[2]/all_sum;
        return ratios;
    }

    int[] search_grid_by_grid(int[] rgb, byte[] yuv420sp, double[][] sample_ratios){
        int[] result = new int[2];

        for (int index = 0; index < grid_num; index++){
            int currentx = index*grid_WIDTH; int currenty = index*grid_HEIGHT;

            double[] grid_ratio = decodeYUV420SP(rgb, yuv420sp, currentx, currenty);

            System.out.println(grid_ratio[0] + " " +grid_ratio[1] +" " + grid_ratio[2]);

            for (int i=0; i<sample_ratios.length; i++){
                boolean match = true;
                for (int j=0; j<sample_ratios[0].length; j++){
                    if (grid_ratio[j] <= (sample_ratios[i][j] + THRESH) &&
                            grid_ratio[j] >= (sample_ratios[i][j] - THRESH)){
                            match = match && true;
                    }else {
                        match = match && false;
                    }
                }
                if (match){
                    result[0] = currentx; result[1] = currenty;
                    return result;
                }
            }
        }

        return null;
    }
}
