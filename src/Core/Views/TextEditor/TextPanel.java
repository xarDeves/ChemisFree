package Core.Views.TextEditor;


import javax.swing.*;
import java.util.LinkedList;

//TODO this could be of no use, use "JPanel" in "TextEditor", migrate "getLastFocusedArticle" in "TextEditorController"
public class TextPanel extends JPanel {

    public TextPanel() {

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public Article getLastFocusedArticle(LinkedList<Article> articles) {

        for (Article article : articles) {

            if (article.getDetailsPane().hasFocus()) return article;
        }
        return articles.get(articles.size() - 1);
    }

}