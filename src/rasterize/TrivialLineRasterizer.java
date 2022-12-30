package rasterize;

import java.awt.*;

import static java.lang.Math.abs;

public class TrivialLineRasterizer extends LineRasterizer {

    public TrivialLineRasterizer(Raster raster) {
        super(raster);
    }

    @Override
    protected void drawLine(int x1, int y1, int x2, int y2, Color color, boolean isDotted) {
        float k = (y2 - y1) / (float) (x2 - x1);
        float q = y1 - k * x1;
        // Pokud ma byt cara carkovana, pridej gap
        int dotGap =  isDotted ? 30 : 1;
        if (abs(k) < 1) {
            if (x1 > x2) {
                int a = x1;
                x1 = x2;
                x2 = a;
            }

            for (int x = x1; x <= x2; x = x + dotGap) {
                float y = k * x + q;
                raster.setPixel(x, Math.round(y), color.hashCode());
            }
        } else {
            if (y1 > y2) {
                int a = y1;
                y1 = y2;
                y2 = a;
            }

            for (int y = y1; y <= y2; y = y + dotGap) {
                float x = (y - q) / k;
                raster.setPixel(Math.round(x), y, color.hashCode());
            }
        }
    }
}
