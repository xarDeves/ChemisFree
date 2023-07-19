package Core.Views;

import Core.MasterController;

import javax.swing.*;
import java.awt.*;

public class MainGui extends MyFrame {
    private final Dimension buttonMaxSize = new Dimension(62, 67);
    private final MasterController masterController;

    private final JPanel panel;

    private final JButton molBtn;
    private final JButton textBtn;
    private final JButton drawBtn;
    private final JButton sourceBtn;
    private final JButton phoneBtn;
    private final JButton homeBtn;

    public MainGui(MasterController masterController) {
        this.masterController = masterController;
        Image image = new ImageIcon("assets/MainGui/MainBar.png").getImage();

        panel = new MotionPanel(this, image);
        panel.setPreferredSize(new Dimension(500, 800));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 16, 0, 0));
        this.setContentPane(panel);

        molBtn = new JButton(new ImageIcon("assets/MainGui/MolButton.png"));
        textBtn = new JButton(new ImageIcon("assets/MainGui/TextSearchButton.png"));
        drawBtn = new JButton(new ImageIcon("assets/MainGui/DrawButton.png"));
        sourceBtn = new JButton(new ImageIcon("assets/MainGui/SourceButton.png"));
        phoneBtn = new JButton(new ImageIcon("assets/Shared/PhoneButton.png"));
        homeBtn = new JButton(new ImageIcon("assets/MainGui/HomeButton.png"));

        initializeButtons();
        setupMainPanel();
        setupFrameProperties();
    }

    private void initializeButtons() {
        molBtn.addActionListener(e -> masterController.createSniperMolecule());
        textBtn.addActionListener(e -> masterController.createTextEditorThread());
        drawBtn.addActionListener(e -> masterController.createJChemPaintCustom());
        sourceBtn.addActionListener(e -> masterController.createMoleculeSearchThread());
    }

    private void setupMainPanel() {
        stylizeAndGlue(molBtn, panel);
        stylizeAndGlue(textBtn, panel);
        stylizeAndGlue(drawBtn, panel);
        stylizeAndGlue(sourceBtn, panel);
        stylizeAndGlue(phoneBtn, panel);
        stylizeAndGlue(homeBtn, panel);
    }

    private void setupFrameProperties() {
        this.setPreferredSize(new Dimension(500, 800));
        //this.setBackground(Color.black);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void stylizeAndGlue(JButton button, JPanel panel) {
        button.setMaximumSize(buttonMaxSize);
        button.setBorder(null);
        button.setOpaque(false);
        button.setFocusable(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);

        panel.add(button);
    }

}
