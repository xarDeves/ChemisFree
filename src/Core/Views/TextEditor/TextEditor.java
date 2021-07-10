package Core.Views.TextEditor;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class TextEditor extends JFrame {

    private final TextEditorController textController;
    private final JPanel articleButtonPanel;

    private static final Color bgColor = Color.decode("#1A1A1A");
    private static final Border articleBtnBorder = BorderFactory.createEmptyBorder(0, 23, 0, 22);
    private static final Dimension articleBtnDim = new Dimension(200, 50);

    public TextEditor() {

        this.setLocation(500, 200);
        this.setLayout(new BorderLayout());

        articleButtonPanel = new JPanel();
        final JPanel panelToolButtons = new JPanel();
        final TextPanel textPanel = new TextPanel();
        final JScrollPane textPanelScroll = new JScrollPane(textPanel);
        textPanelScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        textPanelScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        panelToolButtons.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        articleButtonPanel.setBorder(BorderFactory.createEmptyBorder(20, 9, 0, 32));

        panelToolButtons.setBackground(bgColor);
        articleButtonPanel.setBackground(bgColor);

        panelToolButtons.setLayout(new BoxLayout(panelToolButtons, BoxLayout.Y_AXIS));
        articleButtonPanel.setLayout(new BoxLayout(articleButtonPanel, BoxLayout.Y_AXIS));

        articleButtonPanel.setPreferredSize(new Dimension(230, 300));
        panelToolButtons.setPreferredSize(new Dimension(100, 400));
        textPanelScroll.setPreferredSize(new Dimension(500, 300));

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

        this.add(articleButtonPanel, BorderLayout.WEST);
        this.add(panelToolButtons, BorderLayout.EAST);
        this.add(textPanelScroll, BorderLayout.CENTER);

        this.textController = new TextEditorController(textPanel, this);
        this.textController.createArticle("Untitled", "");

        this.pack();
        this.setVisible(true);
    }

    public void insertArticleButton(JButton button) {

        button.addActionListener(new articleNoBtnListener());
        stylizeArticleBtn(button);

        articleButtonPanel.add(button);
        articleButtonPanel.revalidate();
        articleButtonPanel.repaint();
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

    private class textMineListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            textController.mine();
        }
    }

    private class articleNoBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            textController.articleButtonHandler((JButton) e.getSource());
        }
    }

    public JPanel getArticleButtonPanel() {
        return articleButtonPanel;
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