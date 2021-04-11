package Core;


import Core.Views.TextEditor.Article;
import Core.Views.TextEditor.TextEditor;
import Core.Views.TextEditor.TextPanel;
import Helpers.XmlIoManager;
import Sniper.SniperText;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class TextEditorController {

    private final LinkedList<Article> articles = new LinkedList<>();
    private final TextEditor textEditor;
    private final TextPanel textPanel;

    public TextEditorController(TextPanel textPanel, TextEditor textEditor) {

        this.textPanel = textPanel;
        this.textEditor = textEditor;

    }

    public void ocr() {
        try {
            this.textEditor.setState(Frame.ICONIFIED);
            new SniperText(Toolkit.getDefaultToolkit().getScreenSize(), textPanel.getLastFocusedArticle(this.articles));
        } catch (AWTException awtException) {
            awtException.printStackTrace();
        }
    }

    public void openXml() {

        FileDialog dialog = new FileDialog(this.textEditor, "Open File");
        dialog.setVisible(true);
        String path = dialog.getDirectory() + dialog.getFile();

        if (new File(path).exists()) {

            LinkedList<Article> articlesTemp = new LinkedList<>();
            XmlIoManager.loadAndDisplay(path, articlesTemp);

            for (Article article : articlesTemp) {
                addArticle(article);
            }
        }


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

    public void saveXml() {

        FileDialog dialog = new FileDialog(textEditor, "Save File");
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
        XmlIoManager.saveXml(path, this.articles);

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

    public void mine() {

        for (Article article : this.articles) {

            try {
                article.mineText();
            } catch (BadLocationException | IOException | SAXException badLocationException) {
                badLocationException.printStackTrace();
            }

        }

    }

    public void addArticle(Article article) {

        article.attachArticleButton(this.textEditor.createArticleButton(String.valueOf(this.articles.size())));
        this.textPanel.inflateArticle(article);
        this.articles.add(article);

    }

}
