import rasterize.LineRasterizer;
import rasterize.RasterBufferedImage;
import rasterize.TrivialLineRasterizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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

        // Key listener pro zachytravani eventu klavesnice
        panel.requestFocus();
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 67) {
                    raster.setClearColor(0xFFFFFF);
                    raster.clear();
                    System.out.println("Platno bylo vymazano");
                }
            }
        });

        // Mouse listener na panel pro zachytavani eventu mysi
        panel.addMouseMotionListener(new MouseAdapter() {
            // Startovaci body s vychozi hodnotou 0
            int startedPositionX = 0;
            int startedPositionY = 0;

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                // Log pozice pro kontrolu
                System.out.println(e.getX() + " / " + e.getY());
                // Aktualni pozice mysi
                startedPositionX = e.getX();
                startedPositionY = e.getY();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                // Vycisti raster pred kreslenim
                raster.clear();
                // Vykresluj usecku
                lineRasterizer.rasterize(startedPositionX, startedPositionY, e.getX(), e.getY(), new Color(0xFFFFFF));
                // Prekresli platno
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
