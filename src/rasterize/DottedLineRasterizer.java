package rasterize;
import java.awt.*;

public class DottedLineRasterizer extends TrivialLineRasterizer {

    public DottedLineRasterizer(Raster raster) {
        super(raster);
    }

    public void rasterize(int x1, int y1, int x2, int y2, Color color, boolean isDotted) {
        drawLine(x1, y1, x2, y2, color, isDotted);
    }
}
