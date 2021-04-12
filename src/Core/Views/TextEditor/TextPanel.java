package Core.Views.TextEditor;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.util.LinkedList;

public class TextPanel extends JPanel {

    private final JTextPane textPane;
    private final Document doc;

    public TextPanel() {

        setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();

        //FIXME get rid of "textPane", directly use "this" JPanel
        textPane = new JTextPane();
        textPane.setBackground(Color.DARK_GRAY);
        textPane.setEditable(false);
        doc = textPane.getStyledDocument();
        scrollPane.setViewportView(textPane);

        this.setLayout(new BorderLayout(0, 0));

        this.add(scrollPane, BorderLayout.CENTER);

    }

    public Article getLastFocusedArticle(LinkedList<Article> articles) {

        for (Article article : articles) {

            if (article.getDataPanel().hasFocus()) {
                return article;
            }
        }

        return articles.get(articles.size() - 1);
    }

    public void inflateArticle(Article article) {

        try {
            textPane.insertComponent(article);

            doc.insertString(doc.getLength(), "\n", null);
            doc.insertString(doc.getLength(), "\n", null);
            textPane.setCaretPosition(doc.getLength());

            //article.attachArticleButton(createArticleButton());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

}