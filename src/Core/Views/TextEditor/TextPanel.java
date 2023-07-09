package Core.Views.TextEditor;


import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

//TODO this could be of no use, use "JPanel" in "TextEditor", migrate "getLastFocusedArticle" in "TextEditorController"
public class TextPanel extends JPanel implements Scrollable {

    public TextPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 16; // Set the desired scrolling unit increment
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return visibleRect.height - 10; // Set the desired scrolling block increment
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true; // Adjust this based on your requirements
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false; // Adjust this based on your requirements
    }

    public Article getLastFocusedArticle(LinkedList<Article> articles) {

        for (Article article : articles) {

            if (article.getDetailsPane().hasFocus()) return article;
        }
        return articles.get(articles.size() - 1);
    }

}