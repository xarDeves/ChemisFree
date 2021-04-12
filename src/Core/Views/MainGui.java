package Core.Views;

import Core.MasterController;
import Core.TextEditorController;
import Core.Views.TextEditor.TextEditor;
import Sniper.SniperMolecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.jchempaint.JChemPaintCustom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGui extends MyFrame {

    private final Dimension buttonMaxSize = new Dimension(60, 60);
    private final MasterController masterController;

    private final JPanel panel;

    private final JButton molBtn;
    private final JButton textBtn;
    private final JButton drawBtn;
    private final JButton sourceBtn;
    //private JButton folderBtn;
    private final JButton phoneBtn;
    //private JButton saveBtn;
    private final JButton homeBtn;

    /*private void makeButtons() {

    }

    private void attachListeners() {

    }

    private void initStylize() {

    }*/

    public MainGui(MasterController masterController) {

        this.masterController = masterController;

        Image image = new ImageIcon("assets/MainGui/MainBar.png").getImage();

        panel = new MotionPanel(this, image);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 16, 0, 0));
        this.setContentPane(panel);

        molBtn = new JButton(new ImageIcon("assets/MainGui/MolButton.png"));
        textBtn = new JButton(new ImageIcon("assets/MainGui/TextSearchButton.png"));
        drawBtn = new JButton(new ImageIcon("assets/MainGui/DrawButton.png"));
        sourceBtn = new JButton(new ImageIcon("assets/MainGui/SourceButton.png"));
        phoneBtn = new JButton(new ImageIcon("assets/Shared/PhoneButton.png"));
        homeBtn = new JButton(new ImageIcon("assets/MainGui/HomeButton.png"));

        molBtn.addActionListener(new MolListener());
        textBtn.addActionListener(new TextListener());
        drawBtn.addActionListener(new DrawListener());
        sourceBtn.addActionListener(new SourceListener());

        stylizeAndGLue(molBtn, panel);
        stylizeAndGLue(textBtn, panel);
        stylizeAndGLue(drawBtn, panel);
        stylizeAndGLue(sourceBtn, panel);
        stylizeAndGLue(phoneBtn, panel);
        stylizeAndGLue(homeBtn, panel);

        this.setPreferredSize(new Dimension(100, 480));
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    private void stylizeAndGLue(JButton button, JPanel panel) {

        button.setMaximumSize(buttonMaxSize);
        button.setBorder(null);
        button.setOpaque(false);
        button.setFocusable(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);

        panel.add(button);

    }

    private class MolListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            try {
                new SniperMolecule(Toolkit.getDefaultToolkit().getScreenSize());
            } catch (AWTException awtException) {
                awtException.printStackTrace();
            }

        }
    }

    private class TextListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            new TextEditor();

        }
    }

    private class SourceListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            new MoleculeSearch();

        }
    }

    private class DrawListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            try {
                new JChemPaintCustom();
            } catch (CDKException cdkException) {
                cdkException.printStackTrace();
            }

        }
    }

}
