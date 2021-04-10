package Core.Views.TextEditor;

import Helpers.XmlTagFormatter;
import org.w3c.dom.NodeList;
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

//TODO implement article deletion
public class Article extends JPanel {

    private static final SimpleAttributeSet titleAttrs;
    private static final SimpleAttributeSet dataAttrs;

    private final StyledDocument titleStyledDoc;
    private final StyledDocument dataStyledDoc;

    //private final LinkedList<MoleculeButton> molButtons = new LinkedList<>();
    private final XmlTagFormatter tagFormatter = new XmlTagFormatter();
    private JButton articleButton;

    private final JTextPane titleTextPane;
    private final JTextPane dataTextPane;

    private final JButton destroyBtn;
    private final JPanel titlePanel;

    private final JPanel thisPanel;

    static {

        titleAttrs = new SimpleAttributeSet();
        dataAttrs = new SimpleAttributeSet();

        StyleConstants.setFontFamily(titleAttrs, "tahoma");
        StyleConstants.setFontSize(titleAttrs, 22);
        StyleConstants.setForeground(titleAttrs, Color.decode("#ff9700"));
        StyleConstants.setBold(titleAttrs, true);

        StyleConstants.setFontFamily(dataAttrs, "tahoma");
        StyleConstants.setFontSize(dataAttrs, 17);
    }

    private void stylizeTopLevelContainers(JComponent component) {

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

    public Article(String title, String data) {

        this.thisPanel = this;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.destroyBtn = new JButton(new ImageIcon("assets/shared/closeBtn.png"));

        this.titlePanel = new JPanel();
        this.titlePanel.setLayout(new BoxLayout(this.titlePanel, BoxLayout.X_AXIS));

        this.titleTextPane = new JTextPane();
        this.dataTextPane = new JTextPane();

        this.titleStyledDoc = titleTextPane.getStyledDocument();
        this.dataStyledDoc = dataTextPane.getStyledDocument();

        this.titleTextPane.setCharacterAttributes(titleAttrs, true);
        this.dataTextPane.setCharacterAttributes(dataAttrs, true);

        try {
            this.titleStyledDoc.insertString(this.titleStyledDoc.getLength(), title, titleAttrs);
            this.dataStyledDoc.insertString(this.dataStyledDoc.getLength(), data, dataAttrs);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        this.titleTextPane.addKeyListener(new titleChangeListener());
        this.destroyBtn.addActionListener(new DestroyListener());

        stylizeTopLevelContainers(this.dataTextPane);
        stylizeTopLevelContainers(this.titlePanel);
        stylizeTopLevelContainers(this);
        stylizeInternals(this.titleTextPane);
        stylizeInternals(this.destroyBtn);

        this.titlePanel.add(this.titleTextPane);
        this.titlePanel.add(this.destroyBtn);

        this.add(this.titlePanel);
        this.add(this.dataTextPane);

    }

    public void mineText() throws BadLocationException, IOException, SAXException {

        String rawData = this.getDataTextPane();

        this.tagFormatter.generateAndSplitTags(rawData);
        NodeList tags = this.tagFormatter.getMolNodes();
        LinkedList<String> rawTags = this.tagFormatter.getRawTags();

        //print molecule tags
        /*for (int i = 0; i < tags.getLength() - 1; i++) {
            System.out.println(tags.item(i).getTextContent());
        }*/

        //print all tags
        /*for (int i = 0; i < rawTags.size() - 1; i++) {
            System.out.println(rawTags.get(i));
        }*/

        this.dataTextPane.setText("");

        if (tags != null) {

            int tagCounter = 0;

            for (String rawTag : rawTags) {
                //System.out.println("STRING : " + rawTags.get(i) + " -- TAG : " + tags.item(tagCounter).getTextContent());

                if (tags.item(tagCounter) != null && tags.item(tagCounter).getTextContent().equals(rawTag)) {
                    //System.out.println("---------------------MATCHED---------------------");

                    this.dataTextPane.insertComponent(new MoleculeButton(tags.item(tagCounter).getTextContent()));
                    this.dataStyledDoc.insertString(this.dataStyledDoc.getLength(), " ", dataAttrs);

                    //FIXME for fuck's sake...
                    if (tagCounter < tags.getLength() - 1) {
                        tagCounter++;
                    }

                } else {

                    this.dataStyledDoc.insertString(this.dataStyledDoc.getLength(), rawTag + " ", dataAttrs);
                    this.dataTextPane.setCaretPosition(this.dataStyledDoc.getLength());
                }
            }
        } else {

            for (String rawTag : rawTags) {
                this.dataStyledDoc.insertString(this.dataStyledDoc.getLength(), rawTag + " ", dataAttrs);
                this.dataTextPane.setCaretPosition(this.dataStyledDoc.getLength());

            }
        }

    }

    //GOTO saveXml
    public String getTitleTextPane() {
        String text = null;

        try {
            text = this.titleStyledDoc.getText(0, this.titleStyledDoc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        return text;
    }

    public String getDataTextPane() {

        StringBuilder reconstructed = new StringBuilder();

        ElementIterator iterator = new ElementIterator(dataStyledDoc);
        Element element;

        while ((element = iterator.next()) != null) {
            //System.out.print("element : " + element);
            AttributeSet as = element.getAttributes();

            if (element.getName().equals("component")) {
                JButton btn = (JButton) StyleConstants.getComponent(as);
                reconstructed.append(btn.getText()).append(" ");

            } else if (element.getName().equals("content")) {
                try {
                    reconstructed.append(
                            dataTextPane.getText(element.getStartOffset(),
                                    element.getEndOffset() - element.getStartOffset())
                    );
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
        }

        return reconstructed.toString();

    }

    public void setDataTextPane(String data) {
        try {
            this.dataStyledDoc.insertString(this.dataStyledDoc.getLength(), data, dataAttrs);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void attachArticleButton(JButton articleButton) {

        this.articleButton = articleButton;
    }

    public JTextPane getDataPanel() {
        return this.dataTextPane;
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

            JLabel innerBtnLabel = (JLabel) articleButton.getAccessibleContext().getAccessibleChild(0);
            innerBtnLabel.setText(getTitleTextPane());

        }
    }

    private class DestroyListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            Container parent = thisPanel.getParent();
            parent.remove(thisPanel);

        }
    }

}
