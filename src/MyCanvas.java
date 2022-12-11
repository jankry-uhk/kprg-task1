import rasterize.*;

import model.Point;
import model.Polygon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Vlastní examples.Canvas, který využívá RasterBufferedImage a LineRasterizer.
 */
public class MyCanvas {
    private JPanel panel;
    private RasterBufferedImage raster;
    private FilledLineRasterizer filledLineRasterizer;
    private DottedLineRasterizer dottedLineRasterizer;

    // Vytvoreni pointu jakozto zakladni pozice
    private Point startedPoints;
    private Polygon polygon = new Polygon();

    private Boolean isMouseDragged = false;

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
        // Filled line rasterizer s upravenym trivialnim algoritmem
        filledLineRasterizer = new FilledLineRasterizer(raster);
        dottedLineRasterizer = new DottedLineRasterizer(raster);

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
                    repaint(0xaaaaaa);
                    System.out.println("Platno bylo vymazano");
                }
            }
        });

        MouseAdapter mouseActions = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                // Add new point on click
                if (polygon.getSize() < 1) {
                    polygon.addPoint(new Point(e.getX(), e.getY()));
                    System.out.println("Added point" + e.getX() + ":" + e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                if (polygon.getSize() > 1) {
                    Point lastPoint = polygon.getLastAdded();

                    System.out.println(lastPoint.getX() + " | " + lastPoint.getY() + " | " + e.getX() + " | " + e.getY());
                    drawLine(lastPoint.getX(), lastPoint.getY(), e.getX(), e.getY(), new Color(0xFFFFFFF), false);
                }


                polygon.addPoint(new Point(e.getX(), e.getY()));
                System.out.println("Added point" + e.getX() + ":" + e.getY());

                panel.repaint();
            }
        };

        MouseMotionAdapter mouseMotionActions = new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                Point lastPoint = polygon.getLastAdded();
                // Vycisti raster pred kreslenim
                clear(0xaaaaaa);

                if (polygon.getSize() <= 1) {
                    // Nakresli usecku
                    drawLine(lastPoint.getX(), lastPoint.getY(), e.getX(), e.getY(), new Color(0xFF0000), true);
                } else {
                    Point lastPrev = polygon.getPointByIndex(polygon.getSize() - 2);
                    drawLine(lastPoint.getX(), lastPoint.getY(), e.getX(), e.getY(), new Color(0xFF0000), true);
                    drawLine(lastPrev.getX(), lastPrev.getY(), e.getX(), e.getY(), new Color(0xFF0000), true);
                }
            }
        };
        // Naveseni listeneru na panel
        this.panel.addMouseListener(mouseActions);
        this.panel.addMouseMotionListener(mouseMotionActions);
    }

    // Pomocí Graphics vykreslí raster do panelu. Specifické pro BufferedImage
    private void present(Graphics graphics) {
        raster.repaint(graphics);
    }

    // Vykreslení úsečky jako samostatná metoda
    private void drawLine(int startX, int startY, int endX, int endY, Color color, boolean isDottedLine) {
        // Vykresluj usecku dle parametru
        System.out.println(">> Vykresluji usecku");
        //System.out.println(startX + "/ " + startY + "/ " + endX + "/ " + endY + "/ " + color);

        if (isDottedLine) {
            dottedLineRasterizer.rasterize(startX, startY, endX, endY, color, true);
        } else {
            System.out.println("is filled");
            filledLineRasterizer.rasterize(startX, startY, endX, endY, color);
        }
        // Prekresli platno
        repaint(0xaaaaaa);
    }

    // Vyčistí raster danou barvou
    private void clear(int color) {
        raster.setClearColor(0xaaaaaa);
        raster.clear();
    }
    // Prekresli platno
    private void repaint(int color) {
        raster.setClearColor(color);
        panel.repaint();
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
