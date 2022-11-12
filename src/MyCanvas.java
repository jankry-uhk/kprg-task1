import rasterize.LineRasterizer;
import rasterize.RasterBufferedImage;
import rasterize.TrivialLineRasterizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Vlastní examples.Canvas, který využívá RasterBufferedImage a LineRasterizer.
 */
public class MyCanvas {
    private JPanel panel;
    private RasterBufferedImage raster;
    private LineRasterizer lineRasterizer;

    public MyCanvas(int width, int height) {
        // Instance okna
        JFrame frame = new JFrame();
        // Layout okna
        frame.setLayout(new BorderLayout());
        // Titulek okna
        frame.setTitle("UHK FIM PGRF : " + this.getClass().getName());
        // Není možné měnit velikost okna
        frame.setResizable(false);
        // Pokud by zde nebylo, po zavření okna by se neukončila aplikace, důležité!
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Instance rasteru. Můžeme mít různé rastery.
        raster = new RasterBufferedImage(width, height);
        // Instance line rasterizeru. Můžeme mít různé rasterizery. Např. pro každý algoritmus tzn. TrivialLineRasterizer, MidpointLineRasterizer, ...
        lineRasterizer = new TrivialLineRasterizer(raster);

        // Vytvoříme panel, který umí vykreslit BufferedImage
        panel = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Vykreslíme BufferedImage
                present(g);
            }
        };
        // Velikost panelu
        panel.setPreferredSize(new Dimension(width, height));

        // Panel přidáme do okna, okno zvětšíme na jeho obsah, okno nastavíme jako viditelné
        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        // Přidáme listener, který odchytává pohyb myší
        panel.addMouseMotionListener(new MouseAdapter() {
            // mouseDragged se zavolá, když "kliknu, držím a hýbu myší"
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                // Vyčistím raster
                raster.clear();
                // Vykreslím úsečku ze středu obrazovky k pozici myši
                // tzn. A = střed obrazovky, B = pozice myši
                lineRasterizer.rasterize(width / 2, height / 2, e.getX(), e.getY(),
                        new Color(0xFFFFFF));

                // Překreslím plátno, jinak by uživatel neviděl výsledek!
                panel.repaint();
            }
        });

    }

    // Pomocí Graphics vykreslí raster do panelu. Specifické pro BufferedImage
    private void present(Graphics graphics) {
        raster.repaint(graphics);
    }

    // Vyčistí raster danou barvou
    private void clear(int color) {
        raster.setClearColor(color);
        raster.clear();
    }

    // Při spuštění provedeme vyčištění rasteru a raster zobrazíme
    private void start() {
        clear(0xaaaaaa);
        panel.repaint();
    }

    // Vstupní bod do aplikace
    public static void main(String[] args) {
        // Vytvoří se instance třídy MyCanvas a zavolá se metoda start()
        SwingUtilities.invokeLater(() -> new MyCanvas(800, 600).start());
    }
}
