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

    private Color colorYellow = new Color(0xFFFF00);
    private Color colorWhite = new Color(0xFFFFFFF);
    private int colorDefault = 0xaaaaaa;

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
                clear();
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
                Point lastPoint = polygon.getLastAdded();
                Point firstPoint = polygon.getFirstPoint();

                if (polygon.getSize() > 0) {
                    drawLine(lastPoint.getX(), lastPoint.getY(), e.getX(), e.getY(), colorWhite, false);
                }

                if (polygon.getSize() >= 2) {
                    drawLine(firstPoint.getX(), firstPoint.getY(), e.getX(), e.getY(), new Color(0xFFFFFF), false);
                }

                polygon.addPoint(new Point(e.getX(), e.getY()));
                repaint(0xaaaaaa);
            }
        };

        MouseMotionAdapter mouseMotionActions = new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);

                Point lastPoint = polygon.getLastAdded();
                Point firstPoint = polygon.getFirstPoint();
                // Vycisti raster pred kreslenim
                raster.clear();

                // Pokud mam jen jeden bod, vedu usecku pouze k jednomu bodu, pokud dva vedu k oboum
                if (polygon.getSize() <= 1) {
                    drawLine(lastPoint.getX(), lastPoint.getY(), e.getX(), e.getY(), colorYellow, true);
                } else {
                    drawLine(lastPoint.getX(), lastPoint.getY(), e.getX(), e.getY(), colorYellow, true);
                    drawLine(firstPoint.getX(), firstPoint.getY(), e.getX(), e.getY(), colorYellow, true);
                }
                // Prekresli polygon
                redrawPolyline();
                // Prekresli panel
                panel.repaint();

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
        if (isDottedLine) {
            dottedLineRasterizer.rasterize(startX, startY, endX, endY, color, true);
        } else {
            System.out.println("is filled");
            filledLineRasterizer.rasterize(startX, startY, endX, endY, color);
        }
        // Prekresli platno
        repaint(0xaaaaaa);
    }

    public void redrawPolyline() {
        if (polygon.getSize() > 1) {
            for(int i = 0; i < polygon.getSize(); ++i) {
                int polygonSize = polygon.getSize();
               drawLine((polygon.getPointByIndex((i + 1) % polygonSize)).getX(), (polygon.getPointByIndex((i + 1) % polygonSize)).getY(), (polygon.getPointByIndex(i)).getX(), (polygon.getPointByIndex(i)).getY(), colorWhite, false);
            }
        }
    }

    // Vyčisti raster a odeber všechny body
    private void clear() {
        raster.setClearColor(colorDefault);
        raster.clear();
        polygon.clear();
    }
    // Prekresli platno
    private void repaint(int color) {
        raster.setClearColor(color);
        panel.repaint();
    }
    // Při spuštění provedeme vyčištění rasteru a raster zobrazíme
    private void start() {
        clear();
        panel.repaint();
    }
    // Vstupní bod do aplikace
    public static void main(String[] args) {
        // Vytvoří se instance třídy MyCanvas a zavolá se metoda start()
        SwingUtilities.invokeLater(() -> new MyCanvas(800, 600).start());
    }
}
