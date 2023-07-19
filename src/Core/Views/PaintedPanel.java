package Core.Views;

import javax.swing.*;
import java.awt.*;

public class PaintedPanel extends JPanel {

    Image image;

    public PaintedPanel(Image image) {
        super(true);

        this.image = image;
        this.setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;

        //Set  anti-alias!
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Set anti-alias for text
        g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2D.drawImage(image, 0, 0, null);
    }

}