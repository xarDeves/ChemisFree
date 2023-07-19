package Sniper;

import Core.MasterThreadPool;
import Core.Views.TextEditor.Article;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public final class SniperText extends SnippingTool {

    private final Article article;

    public SniperText(Dimension screenSize, Article article) throws AWTException {
        super(screenSize);
        this.article = article;
    }

    @Override
    protected void launchRecon(KeyEvent e) {

        MasterThreadPool.getPool().submit(() -> {

            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath("src/Dependencies/tess4j/tessdata");

            scaled = new BufferedImage(SCALE * this.snip.getWidth(null),
                    SCALE * this.snip.getHeight(null),
                    BufferedImage.TYPE_BYTE_GRAY);

            Graphics2D grph = (Graphics2D) scaled.getGraphics();
            grph.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            grph.scale(SCALE, SCALE);
            grph.drawImage(this.snip, 0, 0, null);
            grph.dispose();

            try {
                String result = tesseract.doOCR(scaled);
                if (result.isBlank()) {
                    JOptionPane.showMessageDialog(null, "ERROR! no characters found");
                } else {
                    article.setDetailsTextPane(result);
                }
            } catch (TesseractException tesseractException) {
                JOptionPane.showMessageDialog(null, "ERROR!");
                tesseractException.printStackTrace();
            }
        });

        sniperFrame.setVisible(false);

    }

}
