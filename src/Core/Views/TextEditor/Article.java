package Core.Views.TextEditor;

import Helpers.XmlTagFormatter;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.LinkedList;


public class Article extends JPanel {

    private final TextEditorController textEditorController;

    private static final SimpleAttributeSet titleAttrs;
    private static final SimpleAttributeSet detailsAttrs;

    private final StyledDocument titleStyledDoc;
    private final StyledDocument detailsStyledDoc;

    private final XmlTagFormatter tagFormatter = new XmlTagFormatter();
    private JButton articleButton;

    private final JTextPane titleTextPane;
    private final JTextPane detailsTextPane;

    private final JButton destroyBtn;
    private final JPanel titlePanel;

    private final JPanel thisPanel;

    static {

        titleAttrs = new SimpleAttributeSet();
        detailsAttrs = new SimpleAttributeSet();

        StyleConstants.setFontFamily(titleAttrs, "tahoma");
        StyleConstants.setFontSize(titleAttrs, 22);
        StyleConstants.setForeground(titleAttrs, Color.decode("#ff9700"));
        StyleConstants.setBold(titleAttrs, true);

        StyleConstants.setFontFamily(detailsAttrs, "tahoma");
        StyleConstants.setFontSize(detailsAttrs, 17);
    }

    public Article(String title, String data, TextEditorController textEditorController) {

        this.thisPanel = this;
        this.textEditorController = textEditorController;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.destroyBtn = new JButton(new ImageIcon("assets/shared/closeBtn.png"));

        this.titlePanel = new JPanel();
        this.titlePanel.setLayout(new BorderLayout());

        this.titleTextPane = new JTextPane();
        this.detailsTextPane = new JTextPane();

        this.titleStyledDoc = titleTextPane.getStyledDocument();
        this.detailsStyledDoc = detailsTextPane.getStyledDocument();

        this.titleTextPane.setCharacterAttributes(titleAttrs, true);
        this.detailsTextPane.setCharacterAttributes(detailsAttrs, true);

        try {
            this.titleStyledDoc.insertString(this.titleStyledDoc.getLength(), title, titleAttrs);
            this.detailsStyledDoc.insertString(this.detailsStyledDoc.getLength(), data, detailsAttrs);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        this.titleTextPane.addKeyListener(new titleChangeListener());
        this.destroyBtn.addActionListener(new DestroyListener());

        stylizeTopLevel(this.detailsTextPane);
        stylizeTopLevel(this.titlePanel);
        stylizeTopLevel(this);
        stylizeInternals(this.titleTextPane);
        stylizeInternals(this.destroyBtn);

        this.titlePanel.add(this.titleTextPane);
        this.titlePanel.add(this.destroyBtn, BorderLayout.EAST);

        this.add(this.titlePanel);
        this.add(this.detailsTextPane);

    }

    private void stylizeTopLevel(JComponent component) {

        component.setOpaque(true);
        component.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        component.setBackground(Color.DARK_GRAY);
        component.setForeground(Color.WHITE);
    }

    private void stylizeInternals(JComponent component) {

        component.setOpaque(true);
        component.setBorder(BorderFactory.createEmptyBorder());
        component.setBackground(Color.DARK_GRAY);
    }

    public void mineText() throws BadLocationException, IOException, SAXException {

        String rawData = this.getArticleText();

        this.tagFormatter.filterTags(rawData);
        LinkedList<String> molTags = this.tagFormatter.getMolTags();
        LinkedList<String> rawTags = this.tagFormatter.getRawTags();

        this.detailsTextPane.setText("");

        if (molTags.size() != 0)
            insertInteractiveText(molTags, rawTags);
        else
            insertNormalText(rawTags);

    }

    private void insertInteractiveText(LinkedList<String> molTags, LinkedList<String> rawTags) throws BadLocationException {
        int tagCounter = 0;

        for (String rawTag : rawTags) {

            if (molTags.get(tagCounter).equals(rawTag)) {
                this.detailsTextPane.insertComponent(new MoleculeButton(molTags.get(tagCounter)));
                if (tagCounter < molTags.size() - 1) {
                    tagCounter++;
                }
            } else {
                this.detailsStyledDoc.insertString(this.detailsStyledDoc.getLength(), rawTag + " ", detailsAttrs);
                this.detailsTextPane.setCaretPosition(this.detailsStyledDoc.getLength());
            }
        }
    }

    private void insertNormalText(LinkedList<String> rawTags) throws BadLocationException {
        for (String rawTag : rawTags) {
            this.detailsStyledDoc.insertString(this.detailsStyledDoc.getLength(), rawTag + " ", detailsAttrs);
            this.detailsTextPane.setCaretPosition(this.detailsStyledDoc.getLength());

        }
    }

    public String getTitleText() {
        String text = null;

        try {
            text = this.titleStyledDoc.getText(0, this.titleStyledDoc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        return text;
    }

    public String getArticleText() {

        return reconstructText();
    }

    public JTextPane getDetailsPane() {
        return this.detailsTextPane;
    }

    public void setDetailsTextPane(String data) {
        try {
            this.detailsStyledDoc.insertString(this.detailsStyledDoc.getLength(), data, detailsAttrs);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void setArticleButton(JButton articleButton) {

        this.articleButton = articleButton;
        editButtonLabel();
    }

    //fetches text from buttons (if any)
    private String reconstructText() {

        StringBuilder reconstructed = new StringBuilder();

        ElementIterator iterator = new ElementIterator(detailsStyledDoc);
        Element element;

        while ((element = iterator.next()) != null) {
            //System.out.print("element : " + element);
            AttributeSet as = element.getAttributes();

            if (element.getName().equals("content")) {

                try {
                    reconstructed.append(
                            detailsTextPane.getText(element.getStartOffset(),
                                    element.getEndOffset() - element.getStartOffset())
                    );
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            } else if (element.getName().equals("component")) {

                JButton btn = (JButton) StyleConstants.getComponent(as);
                reconstructed.append(btn.getText()).append(" ");
            }
        }

        return reconstructed.toString();
    }

    private void editButtonLabel() {
        JLabel innerBtnLabel = (JLabel) articleButton.getAccessibleContext().getAccessibleChild(0);
        innerBtnLabel.setText(getTitleText());
    }

    private class DestroyListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            textEditorController.destroyArticleHandler(articleButton, thisPanel);

        }
    }

    private class titleChangeListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {

            editButtonLabel();
        }
    }
}