package Core.Views.TextEditor;


import Core.Views.MotionPanel;
import Core.Views.MyFrame;
import Core.Views.PaintedPanel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class TextEditorV1_deprecated extends MyFrame {

    int btnCount = 1;
    String textData;

    private final Dimension dim1 = new Dimension(400, 400);
    private final Dimension dim2 = new Dimension(52, 52);
    private final Border border = BorderFactory.createEmptyBorder(0, 23, 0, 22);

    private final ImageIcon articleImageBtn = new ImageIcon("assets/TextEditor/ArticleButton.png");
    private final JPanel newArticlePanel;
    private final JTextArea textArea;
    private final JFrame frame;

    public TextEditorV1_deprecated(String data) {

        this.frame = this;
        this.textData = data;

        String bgColor = "#1A1A1A";
        Image image = new ImageIcon("assets/TextEditor/BackgroundTxtEd.png").getImage();

        MotionPanel mainPanel = new MotionPanel(this, image);
        this.setContentPane(mainPanel);

        JPanel leftPanel;

        leftPanel = (JPanel) getContentPane();
        leftPanel.setLayout(new BorderLayout(0, 0));

        newArticlePanel = new PaintedPanel(new ImageIcon("assets/TextEditor/ArticleBar.png").getImage());
        newArticlePanel.setLayout(new BoxLayout(newArticlePanel, BoxLayout.Y_AXIS));
        newArticlePanel.setBorder(BorderFactory.createEmptyBorder(20, 9, 0, 32));

        JToolBar toolbar = new JToolBar();
        toolbar.setBackground(Color.decode(bgColor));
        toolbar.setBorderPainted(false);

        JButton openFileBtn = new JButton(new ImageIcon("assets/TextEditor/FolderButtonTxtEd.png"));
        JButton saveBtn = new JButton(new ImageIcon("assets/Shared/SaveButton.png"));
        //TODO refactor to side panel "pop-up", continuously update (timer)
        JButton chemRecBtn = new JButton(new ImageIcon("assets/TextEditor/ChemRecButton.png"));
        openFileBtn.addActionListener(new openFileListener());
        saveBtn.addActionListener(new saveButtonLister());
        //chemRecBtn.addActionListener(new chemRecLister());
        stylizeButtons(openFileBtn, dim1, null);
        stylizeButtons(saveBtn, dim1, null);
        stylizeButtons(chemRecBtn, dim1, null);

        toolbar.add(openFileBtn);
        toolbar.add(saveBtn);
        toolbar.add(chemRecBtn);

        leftPanel.add(toolbar, BorderLayout.NORTH);

        JLabel firstBtnLabel = new JLabel("1");
        firstBtnLabel.setSize(50, 60);

        JButton labelButton = new JButton(articleImageBtn);
        labelButton.addActionListener(new articleNoBtnListener());
        labelButton.add(firstBtnLabel);
        stylizeButtons(labelButton, dim2, border);

        leftPanel.add(newArticlePanel, BorderLayout.WEST);

        newArticlePanel.add(Box.createRigidArea(new Dimension(0, 1)));
        newArticlePanel.add(labelButton);
        newArticlePanel.repaint();

        leftPanel.setBackground(Color.decode("#0000ff"));

        textArea = new JTextArea();
        textArea.setBackground(Color.decode("#EEEAEA"));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.getCaret().setVisible(true);
        textArea.setCaretColor(Color.BLUE);

        JScrollPane pane = new JScrollPane(textArea);
        pane.setPreferredSize(new Dimension(50, 480));
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        leftPanel.add(pane, BorderLayout.CENTER);

        Font f = new Font("TimesRoman", Font.PLAIN, 17);
        textArea.setFont(f);
        textArea.setText(textData);
    }

    private class openFileListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            StringBuilder textData = new StringBuilder(" ");

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
            }

            textArea.setText(textData.toString());

        }
    }

    //TODO check if file exists -> overwrite
    private class saveButtonLister implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            FileDialog dialog = new FileDialog(frame, "Save File");
            dialog.setVisible(true);
            String path = dialog.getDirectory() + dialog.getFile();

            try (PrintWriter out = new PrintWriter(path)) {
                out.println(textArea.getText());
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
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

                newArticlePanel.add(Box.createRigidArea(new Dimension(0, 2)));
                JButton newButton = new JButton(articleImageBtn);
                newButton.add(new JLabel(Integer.toString(btnCount)));
                stylizeButtons(newButton, dim2, border);
                newButton.addActionListener(new articleNoBtnListener());
                newArticlePanel.add(newButton);
                newArticlePanel.revalidate();
                newArticlePanel.repaint();

                textArea.insert("\nArticle " + buttonNo + ":\n", textArea.getCaretPosition());

            } else {
                //TODO jump to article's line
            }
        }
    }

    private void stylizeButtons(JButton button, Dimension dimension, Border border) {

        button.setBorder(border);
        button.setMaximumSize(dimension);
        button.setOpaque(false);
        button.setFocusable(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);

    }

}