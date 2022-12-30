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

    private Polygon polygon = new Polygon();

    // Boolean variables
    private Boolean isMouseDragged = false;
    private Boolean isRegularPoly = false;

    // Colors
    private Color colorYellow = new Color(0xFFFF00);
    private Color colorWhite = new Color(0xFFFFFFF);
    private int colorDefault = 0xaaaaaa;

    // Canvas sizes
    public int width = 800;
    public int height = 600;

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
            // Keycodes list https://stackoverflow.com/a/31637206

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 67) {
                    clear();
                }
                // Handle pro vykreslení rovnoramenného trojůhelníku
                if (e.getKeyCode() == 84) {
                    // Zapinani a vypinani rovnoramenneho trojuhelnika
                    isRegularPoly = !isRegularPoly;
                    // Vymaz jiz aplikovane body
                    polygon.deletePoints();
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
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                Point lastPoint = polygon.getLastAdded();
                Point firstPoint = polygon.getFirstPoint();

                if (!isRegularPoly) {
                    if (polygon.getSize() > 0) {
                        drawLine(lastPoint.getX(), lastPoint.getY(), e.getX(), e.getY(), colorWhite, false);
                    }

                    if (polygon.getSize() >= 2) {
                        drawLine(firstPoint.getX(), firstPoint.getY(), e.getX(), e.getY(), new Color(0xFFFFFF), false);
                    }
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
                raster.clear();

                if (isRegularPoly) {
                    drawRegularPolygon(lastPoint.getX(), lastPoint.getY(), e.getX(), e.getY());
                } else {
                    // Pokud mam jen jeden bod, vedu usecku pouze k jednomu bodu, pokud dva vedu k oboum
                    if (polygon.getSize() <= 1) {
                        drawLine(lastPoint.getX(), lastPoint.getY(), e.getX(), e.getY(), colorYellow, true);
                    } else {
                        drawLine(lastPoint.getX(), lastPoint.getY(), e.getX(), e.getY(), colorYellow, true);
                        drawLine(firstPoint.getX(), firstPoint.getY(), e.getX(), e.getY(), colorYellow, true);
                    }
                    redrawPolyline();
                }
                panel.repaint();
            }
        };
        // Naveseni listeneru na panel
        this.panel.addMouseListener(mouseActions);
        this.panel.addMouseMotionListener(mouseMotionActions);
    }

    /**
     * Pomocí Graphics vykreslí raster do panelu
     * Specifické pro BufferedImage
     */
    private void present(Graphics graphics) {
        raster.repaint(graphics);
    }

   /**
    * Vykreslí úsečku dle parametrů
    */
    private void drawLine(int startX, int startY, int endX, int endY, Color color, boolean isDottedLine) {
        // Kontrola zda se body nenachazi mimo vyhrazeny prostor
        boolean isNotOutOfBounds = (endY > 0 && startY > 0 && endY < raster.getHeight() - 1 && startY < raster.getHeight() - 1) && (endX > 0 && startX > 0 && endX < raster.getWidth() - 1 && startX < raster.getWidth() - 1);

        if (isNotOutOfBounds) {
            if (isDottedLine) {
                dottedLineRasterizer.rasterize(startX, startY, endX, endY, color, true);
            } else {
                filledLineRasterizer.rasterize(startX, startY, endX, endY, color);
            }
            repaint(0xaaaaaa);
        }
    }

    /**
     * Překreslí polygon
     */
    public void redrawPolyline() {
        int polygonSize = polygon.getSize();
        if (polygon.getSize() > 1) {
            for (int i = 0; i < polygonSize; ++i) {
               drawLine((polygon.getPointByIndex((i + 1) % polygonSize)).getX(), (polygon.getPointByIndex((i + 1) % polygonSize)).getY(), (polygon.getPointByIndex(i)).getX(), (polygon.getPointByIndex(i)).getY(), colorWhite, false);
            }
        }
    }

    /**
     * Vykreslí rovnoramenný trojúhelník
     */
    public void drawRegularPolygon(int x1, int y1, int x2, int y2) {
        double actualRadius = 0.0;
        double defaultRadius = 360.0;
        double size = 3;

        double x0 = x2 - x1;
        double y0 = y2 - y1;

        double y;
        for (double step = defaultRadius / size; actualRadius < defaultRadius; y0 = y) {
            actualRadius += step;
            double x = x0 * Math.cos(Math.toRadians(step)) + y0 * Math.sin(Math.toRadians(step));
            y = y0 * Math.cos(Math.toRadians(step)) - x0 * Math.sin(Math.toRadians(step));
            drawLine((int) x0 + x1, (int) y0 + y1, (int) x + x1, (int) y + y1, colorWhite, false);
            x0 = x;
        }
    }

    /**
     * Vyčistí raster včetně bodů
     */
    private void clear() {

        raster.setClearColor(colorDefault);
        raster.clear();
        polygon.clear();
    }

    /**
     * Překresli plátno
     */
    private void repaint(int color) {
        raster.setClearColor(color);
        panel.repaint();
    }

    /**
     * Startovací třída
     * Vyčistí a překeslí plátno
     */
    private void start() {
        clear();
        panel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MyCanvas(800, 600).start());
    }
}
