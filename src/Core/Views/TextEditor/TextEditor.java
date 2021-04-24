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
        final JPanel panelDown = new JPanel();
        final JPanel panelUpper = new JPanel();
        final TextPanel textPanel = new TextPanel();
        final JScrollPane textPanelScroll = new JScrollPane(textPanel);

        panelToolButtons.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        articleButtonPanel.setBorder(BorderFactory.createEmptyBorder(20, 9, 0, 32));

        panelUpper.setBackground(bgColor);
        panelToolButtons.setBackground(bgColor);
        panelDown.setBackground(bgColor);
        articleButtonPanel.setBackground(bgColor);

        panelToolButtons.setLayout(new BoxLayout(panelToolButtons, BoxLayout.Y_AXIS));
        panelUpper.setLayout(new FlowLayout(FlowLayout.LEADING, 228, 20));
        articleButtonPanel.setLayout(new BoxLayout(articleButtonPanel, BoxLayout.Y_AXIS));

        panelUpper.setPreferredSize(new Dimension(0, 20));
        articleButtonPanel.setPreferredSize(new Dimension(230, 300));
        panelToolButtons.setPreferredSize(new Dimension(100, 400));
        panelDown.setPreferredSize(new Dimension(0, 20));
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

        this.add(panelUpper, BorderLayout.NORTH);
        this.add(articleButtonPanel, BorderLayout.WEST);
        this.add(panelToolButtons, BorderLayout.EAST);
        this.add(panelDown, BorderLayout.SOUTH);
        this.add(textPanelScroll, BorderLayout.CENTER);

        this.pack();
        this.setVisible(true);

        this.textController = new TextEditorController(textPanel, this);
        this.textController.createArticle("Untitled", "");
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