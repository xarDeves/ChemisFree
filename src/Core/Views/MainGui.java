package Core.Views;

import Core.Controller;
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
    private Controller controller;

    private final JPanel panel;

    private JButton molBtn;
    private JButton textBtn;
    private JButton drawBtn;
    private JButton sourceBtn;
    private JButton folderBtn;
    private JButton phoneBtn;
    private JButton saveBtn;
    private JButton homeBtn;

    private void makeButtons() {
        molBtn = new JButton(new ImageIcon("assets/MainGui/MolButton.png"));
        textBtn = new JButton(new ImageIcon("assets/MainGui/TextSearchButton.png"));
        drawBtn = new JButton(new ImageIcon("assets/MainGui/DrawButton.png"));
        //TODO poooosssibly deprecate
        sourceBtn = new JButton(new ImageIcon("assets/MainGui/SourceButton.png"));
        folderBtn = new JButton(new ImageIcon("assets/MainGui/FolderButton.png"));
        phoneBtn = new JButton(new ImageIcon("assets/Shared/PhoneButton.png"));
        saveBtn = new JButton(new ImageIcon("assets/Shared/SaveButton.png"));
        homeBtn = new JButton(new ImageIcon("assets/MainGui/HomeButton.png"));
    }

    private void attachListeners() {
        molBtn.addActionListener(new MolListener());
        textBtn.addActionListener(new TextListener());
        drawBtn.addActionListener(new DrawListener());
        sourceBtn.addActionListener(new SourceListener());
    }

    private void initStylize() {
        stylizeAndGLue(molBtn, panel);
        stylizeAndGLue(textBtn, panel);
        stylizeAndGLue(drawBtn, panel);
        stylizeAndGLue(sourceBtn, panel);
        stylizeAndGLue(folderBtn, panel);
        stylizeAndGLue(phoneBtn, panel);
        stylizeAndGLue(saveBtn, panel);
        stylizeAndGLue(homeBtn, panel);
    }

    public MainGui(Controller controller) {

        this.controller = controller;

        Image image = new ImageIcon("assets/MainGui/MainBar.png").getImage();

        panel = new MotionPanel(this, image);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 16, 0, 0));
        this.setContentPane(panel);

        makeButtons();
        attachListeners();
        initStylize();

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
                new SniperMolecule(controller.getScreenSize());
            } catch (AWTException awtException) {
                awtException.printStackTrace();
            }

        }
    }

    private class TextListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            new TextEditor(controller);

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

    /*public void addMolListener(ActionListener molListener) {
        molBtn.addActionListener(molListener);

    }

    public void addTextListener(ActionListener textListener) {
        textBtn.addActionListener(textListener);

    }

    public void addDrawListener(ActionListener drawcListener) {
        drawBtn.addActionListener(drawcListener);

    }

    public void addSourceListener(ActionListener sourceListener) {
        sourceBtn.addActionListener(sourceListener);

    }

    public void addFolderListener(ActionListener folderListener) {
        folderBtn.addActionListener(folderListener);

    }

    public void addPhoneListener(ActionListener phoneListener) {
        phoneBtn.addActionListener(phoneListener);

    }

    public void addSaveListener(ActionListener saveListener) {
        saveBtn.addActionListener(saveListener);

    }

    public void addHomeListener(ActionListener homeListener) {
        homeBtn.addActionListener(homeListener);

    }*/
}
