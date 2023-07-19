package Core.Views.TextEditor;

import Helpers.XmlIoManager;
import Sniper.SniperText;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class TextEditorController {

    private static final ImageIcon articleImageBtn = new ImageIcon("assets/TextEditor/ArticleButton.png");

    private final LinkedList<Article> articles = new LinkedList<>();
    private final LinkedList<JButton> articleButtons = new LinkedList<>();
    private final TextEditor textEditor;
    private final TextPanel textPanel;

    public TextEditorController(TextPanel textPanel, TextEditor textEditor) {
        this.textPanel = textPanel;
        this.textEditor = textEditor;
    }

    public void ocr() {
        try {
            this.textEditor.setState(Frame.ICONIFIED);
            new SniperText(
                    Toolkit.getDefaultToolkit().getScreenSize(),
                    textPanel.getLastFocusedArticle(this.articles)
            );
        } catch (AWTException awtException) {
            awtException.printStackTrace();
        }
    }

    public void openXml() {
        FileDialog dialog = new FileDialog(this.textEditor, "Open File");
        dialog.setFile("*.xml");
        dialog.setVisible(true);
        String path = dialog.getDirectory() + dialog.getFile();

        if (new File(path).exists()) {
            HashMap<String, String> articlesTemp = new HashMap<>();
            XmlIoManager.loadAndDisplay(path, articlesTemp);

            for (Map.Entry<String, String> entry : articlesTemp.entrySet()) {
                createArticle(entry.getKey(), entry.getValue());
            }
        }
    }

    public void saveXml() {
        FileDialog dialog = new FileDialog(textEditor, "Save File");
        dialog.setFile("*.xml");
        dialog.setVisible(true);
        String path = dialog.getDirectory() + dialog.getFile();

        if (new File(path).exists()) {
            int response = JOptionPane.showConfirmDialog(null,
                    "Do you want to replace the existing file?",
                    "Confirm", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (response != JOptionPane.YES_OPTION) {
                return;
            }

            XmlIoManager.saveXml(path, this.articles);
        }
    }

    public void mine() {
        for (Article article : this.articles) {
            try {
                article.mineText();
            } catch (BadLocationException | IOException | SAXException badLocationException) {
                badLocationException.printStackTrace();
            }
        }
    }

    public void createArticle(String title, String details) {
        Article article = new Article(title, details, this);
        JButton articleButton = createArticleButton();
        this.textEditor.insertArticleButton(articleButton);
        article.setArticleButton(articleButton);
        this.textPanel.add(article);

        this.articles.add(article);
        this.articleButtons.add(articleButton);
    }

    public void articleButtonHandler(JButton source) {
        if (source.equals(this.articleButtons.getLast())) {
            this.createArticle("Untitled", "");
        }
        //else
        //TODO jump to article(?)
    }

    public void destroyArticleHandler(JButton articleButton, JPanel articlePanel) {
        if (articleButtons.size() > 1) {
            this.articleButtons.remove(articleButton);
            textEditor.getArticleButtonPanel().remove(articleButton);
            textPanel.remove(articlePanel);

            textEditor.revalidate();
            textEditor.repaint();

            textPanel.revalidate();
            textPanel.repaint();
        }
    }

    private JButton createArticleButton() {
        JButton button = new JButton(articleImageBtn);
        JLabel buttonLabel = new JLabel();
        button.add(buttonLabel);
        return button;
    }
}