package Core.Views.TextEditor;

import Core.TextEditorController;
import Core.Views.MyFrame;
import Helpers.XmlIoManager;
import Sniper.SniperText;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class TextEditor extends MyFrame {

    private final TextEditorController textController;
    private final JPanel panelArticleButtons;

    private static final Color bgColor = Color.decode("#1A1A1A");
    private static final Border articleBtnBorder = BorderFactory.createEmptyBorder(0, 23, 0, 22);
    private static final Dimension articleBtnDim = new Dimension(200, 50);
    private static final ImageIcon articleImageBtn = new ImageIcon("assets/TextEditor/ArticleButton.png");

    public TextEditor() {

        this.setLocation(500, 200);
        this.setLayout(new BorderLayout());

        panelArticleButtons = new JPanel();
        final JPanel panelToolButtons = new JPanel();
        final JPanel panelDown = new JPanel();
        final JPanel panelUpper = new JPanel();
        final TextPanel textPanel = new TextPanel();

        panelToolButtons.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        panelArticleButtons.setBorder(BorderFactory.createEmptyBorder(20, 9, 0, 32));

        panelUpper.setBackground(bgColor);
        panelToolButtons.setBackground(bgColor);
        panelDown.setBackground(bgColor);
        panelArticleButtons.setBackground(bgColor);
        //TODO get rid of this eventually (?)

        panelToolButtons.setLayout(new BoxLayout(panelToolButtons, BoxLayout.Y_AXIS));
        panelUpper.setLayout(new FlowLayout(FlowLayout.LEADING, 228, 20));
        panelArticleButtons.setLayout(new BoxLayout(panelArticleButtons, BoxLayout.Y_AXIS));

        panelUpper.setPreferredSize(new Dimension(0, 20));
        panelArticleButtons.setPreferredSize(new Dimension(230, 300));
        panelToolButtons.setPreferredSize(new Dimension(100, 400));
        panelDown.setPreferredSize(new Dimension(0, 20));
        textPanel.setPreferredSize(new Dimension(500, 300));

        JButton textMineBtn = new JButton(new ImageIcon("assets/TextEditor/TextMine.png"));
        JButton ocrBtn = new JButton(new ImageIcon("assets/TextEditor/OCR.png"));
        JButton saveBtn = new JButton(new ImageIcon("assets/TextEditor/Save.png"));
        JButton openFileBtn = new JButton(new ImageIcon("assets/TextEditor/Folder.png"));
        textMineBtn.addActionListener(new textMineListener());
        ocrBtn.addActionListener(new ocrListener());
        saveBtn.addActionListener(new saveFileListerXml());
        openFileBtn.addActionListener(new openFileListenerXml());
        stylizeToolBtn(textMineBtn);
        stylizeToolBtn(ocrBtn);
        stylizeToolBtn(saveBtn);
        stylizeToolBtn(openFileBtn);

        panelToolButtons.add(ocrBtn);
        panelToolButtons.add(textMineBtn);
        panelToolButtons.add(saveBtn);
        panelToolButtons.add(openFileBtn);

        this.add(panelUpper, BorderLayout.NORTH);
        this.add(panelArticleButtons, BorderLayout.WEST);
        this.add(panelToolButtons, BorderLayout.EAST);
        this.add(panelDown, BorderLayout.SOUTH);
        this.add(textPanel, BorderLayout.CENTER);

        this.pack();
        this.setVisible(true);

        this.textController = new TextEditorController(textPanel, this);
        this.textController.addArticle(new Article("Untitled", ""));

    }

    public JButton createArticleButton(String pos) {

        JButton articleBtn = new JButton(pos, articleImageBtn);
        articleBtn.addActionListener(new articleNoBtnListener());
        JLabel articleBtnLabel = new JLabel(pos);
        articleBtn.add(articleBtnLabel);
        stylizeArticleBtn(articleBtn);

        panelArticleButtons.add(Box.createRigidArea(new Dimension(0, 1)));
        panelArticleButtons.add(articleBtn);

        panelArticleButtons.revalidate();
        panelArticleButtons.repaint();

        return articleBtn;

    }

    private class ocrListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            textController.ocr();

        }
    }

    private class openFileListenerXml implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            textController.openXml();

        }
    }

    private class saveFileListerXml implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            textController.saveXml();

        }
    }

    //TODO refactor to controller
    private class textMineListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            textController.mine();

        }
    }

    //TODO refactor to controller
    private class articleNoBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            //gets the last component (article button) in the panel
            Component component = panelArticleButtons.getComponent(panelArticleButtons.getComponents().length - 1);

            if (component.equals(e.getSource())) {
                textController.addArticle(new Article("Untitled", ""));
            } else {
                //TODO jump to article's line
            }
        }
    }

    private void stylizeToolBtn(JButton button1) {
        Dimension maxSize = new Dimension(150, 80);
        button1.setBorder(null);
        button1.setMaximumSize(maxSize);
        button1.setOpaque(true);
        button1.setFocusable(false);
        button1.setContentAreaFilled(false);
        button1.setBorderPainted(true);
        button1.setFont(new Font("TimesRoman", Font.BOLD, 15));
        button1.setForeground(Color.MAGENTA);
    }

    private void stylizeArticleBtn(JButton button) {

        button.setBorder(articleBtnBorder);
        button.setMaximumSize(articleBtnDim);
        button.setOpaque(false);
        button.setFocusable(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);

    }
}