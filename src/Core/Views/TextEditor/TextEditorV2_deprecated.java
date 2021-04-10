package Core.Views.TextEditor;

import Core.Views.MyFrame;
import Helpers.XmlIoManager;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;

//TODO create new article on button press with marked text
public class TextEditorV2_deprecated extends MyFrame {

    private int btnCount = 1;
    private final LinkedList<Article> articles = new LinkedList<>();

    private final Dimension maxSize = new Dimension(150, 80);
    private final TextPanel textPanel;
    private final JFrame frame;

    public class TextPanel extends JPanel {

        JTextPane textPane;
        Document doc;

        public TextPanel() {

            setLayout(new BorderLayout(0, 0));

            JScrollPane scrollPane = new JScrollPane();

            //TODO inflate directly in "this" panel, no need for JTextPane
            textPane = new JTextPane();
            textPane.setBackground(Color.DARK_GRAY);
            textPane.setEditable(false);
            doc = textPane.getStyledDocument();
            scrollPane.setViewportView(textPane);

            this.setLayout(new BorderLayout(0, 0));

            this.add(scrollPane, BorderLayout.CENTER);

        }

        public void addArticle(String title, String data) throws BadLocationException {

            Article article = new Article(title, data);

            textPane.insertComponent(article);
            doc.insertString(doc.getLength(), "\n", null);
            doc.insertString(doc.getLength(), "\n", null);
            textPane.setCaretPosition(doc.getLength());

            articles.add(article);
        }

    }

    public TextEditorV2_deprecated(String data) {

        this.frame = this;

        this.setLayout(new BorderLayout());

        textPanel = new TextPanel();
        try {
            textPanel.addArticle("Article 1", data);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        JPanel panelUp = new JPanel();
        JPanel panelBar = new JPanel();
        JPanel panelButton = new JPanel();
        JPanel panelDown = new JPanel();

        Color bgColor = Color.decode("#1A1A1A");
        panelUp.setBackground(bgColor);
        panelBar.setBackground(bgColor);
        panelButton.setBackground(bgColor);
        panelDown.setBackground(bgColor);

        panelUp.setPreferredSize(new Dimension(1000, 50));
        panelBar.setPreferredSize(new Dimension(100, 500));
        panelButton.setPreferredSize(new Dimension(100, 500));
        panelDown.setPreferredSize(new Dimension(1000, 50));

        panelButton.setLayout(new BoxLayout(panelButton, BoxLayout.Y_AXIS));
        panelButton.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 0));

        JButton textMining = new JButton("Text Mining");
        stylizeButtons(textMining);
        //textMining.addActionListener(new TextMinerListener());

        JButton saveButton = new JButton("Save");
        stylizeButtons(saveButton);
        saveButton.addActionListener(new saveFileListerXml());

        JButton folderButton = new JButton("Open Folder");
        stylizeButtons(folderButton);
        folderButton.addActionListener(new openFileListenerXml());

        panelButton.add(textMining);
        panelButton.add(saveButton);
        panelButton.add(folderButton);

        this.add(panelUp, BorderLayout.NORTH);
        this.add(panelBar, BorderLayout.WEST);
        this.add(panelButton, BorderLayout.EAST);
        this.add(panelDown, BorderLayout.SOUTH);
        this.add(textPanel, BorderLayout.CENTER);

        this.pack();
        this.setVisible(true);

    }

    //TODO open xml for xml parser
    private class openFileListenerXml implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            FileDialog dialog = new FileDialog(frame, "Open File");
            dialog.setVisible(true);
            String path = dialog.getDirectory() + dialog.getFile();

            /*if (new File(path).exists())
                XmlIoHelper.loadAndDisplay(path, textPanel);*/

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

    //TODO check if file exists -> overwrite
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

    private class articleNoBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            JButton btn = (JButton) e.getSource();
            JLabel innerBtnLabel = (JLabel) btn.getAccessibleContext().getAccessibleChild(0);
            String buttonNo = innerBtnLabel.getText();

            if (Integer.parseInt(buttonNo) == btnCount) {

                ++btnCount;

                /*newArticlePanel.add(Box.createRigidArea(new Dimension(0, 2)));
                JButton newButton = new JButton(articleImageBtn);
                newButton.add(new JLabel(Integer.toString(btnCount)));
                stylizeButtons(newButton, dim2, border);
                newButton.addActionListener(new TextEditor.articleNoBtnListener());
                newArticlePanel.add(newButton);
                newArticlePanel.revalidate();
                newArticlePanel.repaint();

                textArea.insert("\nArticle " + buttonNo + ":\n", textArea.getCaretPosition());*/

            } else {
                //TODO jump to article's line
            }
        }
    }

    private void stylizeButtons(JButton button) {

        button.setBorder(null);
        button.setMaximumSize(maxSize);
        button.setOpaque(true);
        button.setFocusable(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(true);
        button.setFont(new Font("TimesRoman", Font.BOLD, 15));
        button.setForeground(Color.MAGENTA);
    }

}