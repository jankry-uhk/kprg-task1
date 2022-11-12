package rasterize;

public class TrivialLineRasterizer extends LineRasterizer {

    public TrivialLineRasterizer(Raster raster) {
        super(raster);
    }

    @Override
    protected void drawLine(int x1, int y1, int x2, int y2) {
        float k = (y2 - y1) / (float) (x2 - x1);
        float q = y1 - k * x1;

        if (x1 > x2) {
            int a = x1;
            x1 = x2;
            x2 = a;
        }

        for (int x = x1; x <= x2; x++) {
            float y = k * x + q;
            raster.setPixel(x, Math.round(y), 0xffffff);
        }
    }
}
