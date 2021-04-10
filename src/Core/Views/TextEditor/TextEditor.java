package Core.Views.TextEditor;

import Core.Controller;
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

    private final Controller controller;

    private static final Color bgColor = Color.decode("#1A1A1A");
    private static final Border articleBtnBorder = BorderFactory.createEmptyBorder(0, 23, 0, 22);
    private static final Dimension articleBtnDim = new Dimension(200, 50);
    private static final ImageIcon articleImageBtn = new ImageIcon("assets/TextEditor/ArticleButton.png");

    private final LinkedList<Article> articles = new LinkedList<>();

    private final JPanel panelArticleButtons;
    private final JFrame frame;
    private final TextPanel textPanel;

    public class TextPanel extends JPanel {

        private final JTextPane textPane;
        private final Document doc;

        public TextPanel() {

            setLayout(new BorderLayout(0, 0));

            JScrollPane scrollPane = new JScrollPane();

            //TODO TextPane could get deprecated, directly use "this" JPanel
            textPane = new JTextPane();
            textPane.setBackground(Color.DARK_GRAY);
            textPane.setEditable(false);
            doc = textPane.getStyledDocument();
            scrollPane.setViewportView(textPane);

            this.setLayout(new BorderLayout(0, 0));

            this.add(scrollPane, BorderLayout.CENTER);

        }

        public Article getLastFocusedArticle() {

            for (Article article : articles) {

                if (article.getDataPanel().hasFocus()) {
                    return article;
                }
            }

            return articles.get(articles.size() - 1);
        }

        public void addArticle(Article article) {

            textPane.insertComponent(article);
            //doc.insertString(doc.getLength(), "\n", null);
            //doc.insertString(doc.getLength(), "\n", null);
            textPane.setCaretPosition(doc.getLength());

            articles.add(article);
            article.attachArticleButton(createArticleButton());
        }

    }

    public TextEditor(Controller controller) {

        this.controller = controller;

        this.frame = this;
        this.setLocation(500, 200);
        this.setLayout(new BorderLayout());

        panelArticleButtons = new JPanel();
        final JPanel panelToolButtons = new JPanel();
        final JPanel panelDown = new JPanel();
        final JPanel panelupper = new JPanel();
        textPanel = new TextPanel();

        panelToolButtons.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        panelArticleButtons.setBorder(BorderFactory.createEmptyBorder(20, 9, 0, 32));

        panelupper.setBackground(bgColor);
        panelToolButtons.setBackground(bgColor);
        panelDown.setBackground(bgColor);
        panelArticleButtons.setBackground(bgColor);
        //TODO get rid of this eventually (?)
        textPanel.addArticle(new Article("Untitled", ""));

        panelToolButtons.setLayout(new BoxLayout(panelToolButtons, BoxLayout.Y_AXIS));
        panelupper.setLayout(new FlowLayout(FlowLayout.LEADING, 228, 20));
        panelArticleButtons.setLayout(new BoxLayout(panelArticleButtons, BoxLayout.Y_AXIS));

        panelupper.setPreferredSize(new Dimension(0, 20));
        panelArticleButtons.setPreferredSize(new Dimension(230, 300));
        panelToolButtons.setPreferredSize(new Dimension(100, 400));
        panelDown.setPreferredSize(new Dimension(0, 20));
        textPanel.setPreferredSize(new Dimension(500, 300));

        JButton textMineBtn = new JButton(new ImageIcon("assets/TextEditor/TextMine.png"));
        //TODO need new asset for this
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

        this.add(panelupper, BorderLayout.NORTH);
        this.add(panelArticleButtons, BorderLayout.WEST);
        this.add(panelToolButtons, BorderLayout.EAST);
        this.add(panelDown, BorderLayout.SOUTH);
        this.add(textPanel, BorderLayout.CENTER);

        this.pack();
        this.setVisible(true);

    }

    private JButton createArticleButton() {

        JButton articleBtn = new JButton(String.valueOf(articles.size()), articleImageBtn);
        articleBtn.addActionListener(new articleNoBtnListener());
        JLabel articleBtnLabel = new JLabel(String.valueOf(articles.size()));
        //articleBtnLabel.setSize(200, 100);
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
            try {
                frame.setState(Frame.ICONIFIED);
                new SniperText(controller.getScreenSize(), textPanel.getLastFocusedArticle());
            } catch (AWTException awtException) {
                awtException.printStackTrace();
            }

        }
    }

    private class openFileListenerXml implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            FileDialog dialog = new FileDialog(frame, "Open File");
            dialog.setVisible(true);
            String path = dialog.getDirectory() + dialog.getFile();

            if (new File(path).exists())
                XmlIoManager.loadAndDisplay(path, textPanel);

            //for normal text:
            /*StringBuilder textData = new StringBuilder(" ");

            FileDialog dialog = new FileDialog(frame, "Open File");
            dialog.setVisible(true);
            String path = dialog.getDirectory() + dialog.getFile();

            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                String line;
                while ((line = br.readLine()) != null) {
                    textData.append(line);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error!");
            }*/

            //textArea.setText(textData.toString());

        }
    }

    private class saveFileListerXml implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            FileDialog dialog = new FileDialog(frame, "Save File");
            dialog.setVisible(true);
            String path = dialog.getDirectory() + dialog.getFile();

            if (new File(path).exists()) {
                int response = JOptionPane.showConfirmDialog(null, //
                        "Do you want to replace the existing file?", //
                        "Confirm", JOptionPane.YES_NO_OPTION, //
                        JOptionPane.QUESTION_MESSAGE);
                if (response != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            XmlIoManager.saveXml(path, articles);

            //for normal text
            /*FileDialog dialog = new FileDialog(frame, "Save File");
            dialog.setVisible(true);
            String path = dialog.getDirectory() + dialog.getFile();

            try (PrintWriter out = new PrintWriter(path)) {
                out.println(textArea.getText());
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }*/
        }
    }

    private class textMineListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            for (Article article : articles) {

                try {
                    article.mineText();
                } catch (BadLocationException | IOException | SAXException badLocationException) {
                    badLocationException.printStackTrace();
                }

            }


        }
    }

    private class articleNoBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            //gets the last component (article button) in the panel
            Component component = panelArticleButtons.getComponent(panelArticleButtons.getComponents().length - 1);

            if (component.equals(e.getSource())) {
                textPanel.addArticle(new Article("Untitled", ""));
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