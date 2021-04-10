package Sniper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

//TODO when sniper is initiated, minimize all windows
//FIXME implement JFrame construction in "this" instead of "Controller"
public abstract class SnippingTool extends JPanel {

    protected final short SCALE = 6;

    protected boolean snipTaken = false;

    protected int x, y, x2, y2 = 0;
    protected int px, py, pw, ph;

    protected BufferedImage image;
    protected BufferedImage scaled;

    protected final JFrame sniperFrame;
    protected BufferedImage snip;

    public SnippingTool(Dimension screenSize) throws AWTException {

        this.sniperFrame = new JFrame("Select Recognition Bounds");
        this.sniperFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.sniperFrame.setSize(screenSize.width, screenSize.height);
        this.sniperFrame.setUndecorated(true);
        this.sniperFrame.setContentPane(this);
        this.setFocusable(true);

        this.image = new Robot().createScreenCapture(new Rectangle(screenSize));

        this.setBorder(BorderFactory.createRaisedBevelBorder());

        this.addKeyListener(new MyKeyListener());
        MyMouseListener listener = new MyMouseListener();
        addMouseListener(listener);
        addMouseMotionListener(listener);

        this.sniperFrame.setVisible(true);

    }

    protected abstract void launchRecon(KeyEvent e);

    class MyKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                sniperFrame.setVisible(false);
            }

            if (snipTaken) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    launchRecon(e);
                }
            }
        }

    }

    class MyMouseListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            setStartPoint(e.getX(), e.getY());
        }

        public void mouseDragged(MouseEvent e) {
            setEndPoint(e.getX(), e.getY());
            repaint();
        }

        public void mouseReleased(MouseEvent e) {
            setEndPoint(e.getX(), e.getY());
            repaint();

            takeSnip();

        }
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
        g.setColor(Color.RED);
        drawPerfectRect(g, x, y, x2, y2);

    }

    private void setStartPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private void setEndPoint(int x, int y) {
        x2 = (x);
        y2 = (y);
    }

    private void drawPerfectRect(Graphics g, int x, int y, int x2, int y2) {
        px = Math.min(x, x2);
        py = Math.min(y, y2);
        pw = Math.abs(x - x2);
        ph = Math.abs(y - y2);
        g.drawRect(px, py, pw, ph);
    }

    //grayscale manual converter:
    /*private void makeGray(BufferedImage img) {

        for (int x = 0; x < img.getWidth(); ++x)
            for (int y = 0; y < img.getHeight(); ++y) {

                int rgb = img.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb & 0xFF);

                // Normalize and gamma correct:
                float rr = (float) Math.pow(r / 255.0, 2.2);
                float gg = (float) Math.pow(g / 255.0, 2.2);
                float bb = (float) Math.pow(b / 255.0, 2.2);

                // Calculate luminance:
                float lum = (float) (0.2126 * rr + 0.7152 * gg + 0.0722 * bb);

                // Gamma compand and rescale to byte range:
                int grayLevel = (int) (255.0 * Math.pow(lum, 1.0 / 2.2));
                int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
                img.setRGB(x, y, gray);
            }
    }*/

    private void takeSnip() {

        this.snip = this.image.getSubimage(px, py, pw, ph);
        this.snipTaken = true;
    }

}
