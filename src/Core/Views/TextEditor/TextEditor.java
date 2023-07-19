package Core.Views.TextEditor;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TextEditor extends JFrame {

    private final TextEditorController textController;
    private JPanel articleButtonPanel;
    private JPanel panelToolButtons;
    private TextPanel textPanel;
    private JScrollPane textPanelScroll;
    private static final Color bgColor = Color.decode("#1A1A1A");
    private static final Border articleBtnBorder = BorderFactory.createEmptyBorder(0, 23, 0, 22);
    private static final Dimension articleBtnDim = new Dimension(200, 50);

    public TextEditor() {
        initializeFrame();
        initializeComponents();
        addComponentsToFrame();
        attachListeners();
        setupStyles();

        textController = new TextEditorController(textPanel, this);
        textController.createArticle("Untitled", "");

        pack();
        setVisible(true);
    }

    private void initializeFrame() {
        setLocation(500, 200);
        setLayout(new BorderLayout());

        setBackground(bgColor);
    }
    private void initializeComponents() {
        articleButtonPanel = new JPanel();
        panelToolButtons = new JPanel();
        textPanel = new TextPanel();
        textPanelScroll = new JScrollPane(textPanel);
        textPanelScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        textPanelScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    private void addComponentsToFrame() {
        articleButtonPanel.setPreferredSize(new Dimension(230, 300));
        panelToolButtons.setPreferredSize(new Dimension(100, 400));
        textPanelScroll.setPreferredSize(new Dimension(500, 300));

        add(articleButtonPanel, BorderLayout.WEST);
        add(panelToolButtons, BorderLayout.EAST);
        add(textPanelScroll, BorderLayout.CENTER);
    }

    private void attachListeners() {
        JButton ocrBtn = createStyledButton("assets/TextEditor/OCR.png");
        JButton textMineBtn = createStyledButton("assets/TextEditor/TextMine.png");
        JButton saveBtn = createStyledButton("assets/TextEditor/Save.png");
        JButton openFileBtn = createStyledButton("assets/TextEditor/Folder.png");

        ocrBtn.addActionListener(new OcrListener());
        textMineBtn.addActionListener(new TextMineListener());
        saveBtn.addActionListener(new SaveFileListenerXml());
        openFileBtn.addActionListener(new OpenFileListenerXml());

        panelToolButtons.add(ocrBtn);
        panelToolButtons.add(textMineBtn);
        panelToolButtons.add(saveBtn);
        panelToolButtons.add(openFileBtn);
    }

    private JButton createStyledButton(String imagePath) {
        JButton button = new JButton(new ImageIcon(imagePath));
        Dimension maxSize = new Dimension(150, 80);
        button.setBorder(null);
        button.setMaximumSize(maxSize);
        button.setOpaque(true);
        button.setFocusable(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(true);
        button.setFont(new Font("TimesRoman", Font.BOLD, 15));
        button.setForeground(Color.MAGENTA);
        return button;
    }

    private void setupStyles() {
        panelToolButtons.setLayout(new BoxLayout(panelToolButtons, BoxLayout.Y_AXIS));
        articleButtonPanel.setLayout(new BoxLayout(articleButtonPanel, BoxLayout.Y_AXIS));
        panelToolButtons.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        articleButtonPanel.setBorder(BorderFactory.createEmptyBorder(20, 9, 0, 32));
        panelToolButtons.setBackground(bgColor);
        articleButtonPanel.setBackground(bgColor);
        textPanel.setBackground(bgColor);
        textPanelScroll.getViewport().setBackground(bgColor);
    }

    public void insertArticleButton(JButton button) {
        button.addActionListener(new ArticleNoBtnListener());
        stylizeArticleBtn(button);

        articleButtonPanel.add(button);
        articleButtonPanel.revalidate();
        articleButtonPanel.repaint();
    }

    private void stylizeArticleBtn(JButton button) {
        button.setBorder(articleBtnBorder);
        button.setMaximumSize(articleBtnDim);
        button.setOpaque(false);
        button.setFocusable(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
    }

    private class OcrListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            textController.ocr();
        }
    }

    private class OpenFileListenerXml implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            textController.openXml();
        }
    }

    private class SaveFileListenerXml implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            textController.saveXml();
        }
    }

    private class TextMineListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            textController.mine();
        }
    }

    private class ArticleNoBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            textController.articleButtonHandler((JButton) e.getSource());
        }
    }

    public JPanel getArticleButtonPanel() {
        return articleButtonPanel;
    }
}
