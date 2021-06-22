package Core.Views.TextEditor;


import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

//TODO this could be of no use, use "JPanel" in "TextEditor", migrate "getLastFocusedArticle" in "TextEditorController"
public class TextPanel extends JPanel implements Scrollable {

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return new Dimension(600, 200);
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 128;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 128;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return true;
    }


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