package Core.Views.Details;

import Core.Molecule;
import org.openscience.cdk.depict.DepictionGenerator;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class DetailsPanel {

    private final JTextArea textArea;
    private final Molecule moleculeObject;
    private BufferedImage molImage;

    private void setDetailsPane() {
        Document document = textArea.getDocument();
        try {
            document.insertString(document.getLength(), "Aromatic Atom Count: " + this.moleculeObject.aromaticAtomCount + "\n", null);
            document.insertString(document.getLength(), "LogP: " + this.moleculeObject.logP + "\n", null);
            document.insertString(document.getLength(), "Molecular Weight: " + this.moleculeObject.weight + "\n", null);
            document.insertString(document.getLength(), "Atom Count: " + this.moleculeObject.atomCount + "\n", null);
            document.insertString(document.getLength(), "Hydrogen Bond Acceptors: " + this.moleculeObject.hBondAcceptorCount + "\n", null);
            document.insertString(document.getLength(), "Hydrogen Bond Donors: " + this.moleculeObject.hBondDonorCount + "\n", null);
            document.insertString(document.getLength(), "IUPAC Name: " + this.moleculeObject.name + "\n", null);
            document.insertString(document.getLength(), "Number of Rotatable Bonds: " + this.moleculeObject.rotatableBondsCount + "\n", null);
            document.insertString(document.getLength(), "SMILES: " + this.moleculeObject.smiles + "\n", null);
            document.insertString(document.getLength(), "TPSA: " + this.moleculeObject.tpsa + "\n", null);
            document.insertString(document.getLength(), "LogS: " + this.moleculeObject.logS + "\n", null);
            document.insertString(document.getLength(), "Rule of Five Violations: " + this.moleculeObject.ruleOf5.size() + "\n", null);
            for (String rule : this.moleculeObject.ruleOf5) {
                document.insertString(document.getLength(), rule + "\n", null);

            }

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }


    public static void stylizeButtons(JButton button) {
        Dimension maxSize = new Dimension(190, 0);
        //button.setBorder(null);
        button.setMaximumSize(maxSize);
        button.setOpaque(false);
        button.setFocusable(false);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        button.setBorderPainted(false);


    }

    public static void fieldStyle(JTextField textField) {
        textField.setBackground(Color.decode("#1A1A1A"));
        textField.setForeground(Color.WHITE);
        textField.setFont(new Font("TimesRoman", Font.BOLD, 17));

        textField.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        textField.setBorder(BorderFactory.createBevelBorder(0, Color.BLACK, Color.decode("#242424")));


    }

    public static void labelStyle(JLabel label) {
        label.setForeground(Color.decode("#006aff"));
        label.setFont(new Font("Times Roman", Font.BOLD, 15));
//        label.setBorder(BorderFactory.createEmptyBorder(0,8,0,10));
        label.setBorder(BorderFactory.createEmptyBorder(0, 65, 10, 5));
    }


    public BufferedImage makeComposite(String root, String smiles) {

        BufferedImage combinedImage = null;

        try {
            final SmilesParser smilesParser = new SmilesParser(SilentChemObjectBuilder.getInstance());
            IAtomContainer mol = smilesParser.parseSmiles(smiles);

            BufferedImage imgBG = ImageIO.read(new File(root));

            int bgWidth = imgBG.getWidth();
            int bgHeight = imgBG.getHeight();

            DepictionGenerator depictionGenerator = new DepictionGenerator()
                    .withBackgroundColor(new Color(0f, 0f, 0f, 0f))
                    .withAtomColors()
                    .withSize(bgWidth, bgHeight);

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            depictionGenerator.depict(mol).writeTo("png", out);
            byte[] data = out.toByteArray();
            ByteArrayInputStream input = new ByteArrayInputStream(data);
            this.molImage = ImageIO.read(input);

            combinedImage = new BufferedImage(
                    bgWidth,
                    bgHeight,
                    BufferedImage.TYPE_INT_ARGB);

            Graphics2D g = combinedImage.createGraphics();

            int x = bgWidth / 2 - molImage.getWidth() / 2;
            int y = bgHeight / 2 - molImage.getHeight() / 2;

            g.drawImage(imgBG, 0, 0, null);
            g.drawImage(molImage, x, y, null);
            g.dispose();

        } catch (IOException | CDKException e) {
            e.printStackTrace();
        }

        return combinedImage;
    }

    public DetailsPanel(Molecule moleculeObject) {

        this.moleculeObject = moleculeObject;

        ImageIcon image;
        ImageIcon image2;
        image = new ImageIcon("assets/DetailsPanel/progressbar.png");

        BufferedImage compositeImage = makeComposite("assets/DetailsPanel/glass.png", this.moleculeObject.smiles);
        image2 = new ImageIcon(compositeImage);

        JFrame frame = new JFrame();
        JPanel mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(1200, 1000));
        mainPanel.setBackground(Color.decode("#1A1A1A"));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 20, 20));
//        mainPanel.add(Box.createRigidArea(new Dimension(1, 1)));

        frame.add(mainPanel);
        String bg = "#1A1A1A";


//------------MOLECULE & DRUG LIKENESS PANEL----------//

        JPanel panelTop = new JPanel();
        panelTop.setLayout(new FlowLayout(FlowLayout.CENTER, 140, 10));
        panelTop.setPreferredSize(new Dimension(1600, 300));
        panelTop.setBackground(Color.decode(bg));
        mainPanel.add(panelTop);

        JLabel glassPanel = new JLabel(image2, JLabel.LEFT);
        glassPanel.setPreferredSize(new Dimension(500, 280));
        panelTop.add(glassPanel);

        JLabel drugLikeness = new JLabel("Drug Likeness", image, JLabel.RIGHT);
        drugLikeness.setFont(new Font("SansSerif", Font.BOLD, 25));
        drugLikeness.setIconTextGap(30);
        drugLikeness.setForeground(Color.decode("#006aff"));
        drugLikeness.setHorizontalTextPosition(JLabel.CENTER);
        drugLikeness.setVerticalTextPosition(JLabel.TOP);
        panelTop.add(drugLikeness);


//-----------TEXTFIELD-PANEL--------------------------//

        JPanel panelMiddle = new JPanel();
        panelMiddle.setPreferredSize(new Dimension(1600, 130));
        panelMiddle.setBackground(Color.decode(bg));
//        panelMiddle.setBackground(Color.red);
        mainPanel.add(panelMiddle);


        JPanel panel1 = new JPanel();
        panel1.setBackground(Color.decode("#1A1A1A"));
//        panel1.setBackground(Color.BLUE);
        panel1.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        panel1.setPreferredSize(new Dimension(1600, 130));
        panelMiddle.add(panel1);
//        panel1.setBorder(BorderFactory.createEmptyBorder(0,15,0,5));
        JLabel iupac = new JLabel("IUPAC: ");
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.gridheight = 1;
        c.ipadx = 10;
        c.gridwidth = 1;


        iupac.setForeground(Color.decode("#006aff"));
        iupac.setFont(new Font("Times Roman", Font.BOLD, 15));
        panel1.add(iupac, c);

        JTextField tf1 = new JTextField(this.moleculeObject.name, 40);
        //tf1.setPreferredSize(new Dimension(50,5));
        fieldStyle(tf1);
        c.gridy = 0;
        c.gridx = 1;
        c.gridheight = 1;
        c.gridwidth = 1;

        c.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(tf1, c);

        JLabel formula = new JLabel("Formula: ");
        labelStyle(formula);
        c.gridy = 0;
        c.gridx = 2;
        c.gridheight = 1;
        c.gridwidth = 1;

        panel1.add(formula, c);


        JTextField tf3 = new JTextField("", 10);
        fieldStyle(tf3);
        c.gridy = 0;
        c.gridx = 3;
        c.gridheight = 1;
        c.gridwidth = 1;

        c.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(tf3, c);

        JLabel labelLogP = new JLabel("LogP: ");
        labelStyle(labelLogP);
        c.gridy = 0;
        c.gridx = 4;
        c.gridheight = 1;
        c.gridwidth = 1;

        panel1.add(labelLogP, c);

        JTextField tf4 = new JTextField(this.moleculeObject.logP, 6);
        fieldStyle(tf4);
        c.gridy = 0;
        c.gridx = 5;
        c.gridheight = 1;
        c.gridwidth = 1;

        c.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(tf4, c);

        JLabel labelSmiles = new JLabel("SMILES: ");
        labelSmiles.setForeground(Color.decode("#006aff"));
        labelSmiles.setFont(new Font("Times Roman", Font.BOLD, 15));
        c.gridy = 1;
        c.gridx = 0;
        c.gridheight = 1;
        c.gridwidth = 1;

        c.anchor = GridBagConstraints.LAST_LINE_START;
        panel1.add(labelSmiles, c);

        JTextField tf2 = new JTextField(this.moleculeObject.smiles);
        c.gridy = 1;
        c.gridx = 1;
        c.gridheight = 1;
        c.gridwidth = 1;
        ;
        c.fill = GridBagConstraints.HORIZONTAL;
        fieldStyle(tf2);
        panel1.add(tf2, c);

        JLabel mw = new JLabel("M.W: ");
        c.gridy = 1;
        c.gridx = 2;
        c.gridheight = 1;
        c.gridwidth = 1;

        labelStyle(mw);
        panel1.add(mw, c);


        JTextField tf5 = new JTextField(this.moleculeObject.weight);
        tf5.setText(moleculeObject.weight);
        c.gridy = 1;
        c.gridx = 3;
        c.gridheight = 1;
        c.gridwidth = 1;

        c.fill = GridBagConstraints.HORIZONTAL;
        fieldStyle(tf5);
        panel1.add(tf5, c);

        JLabel labelLogS = new JLabel("LogS: ");
        c.gridy = 1;
        c.gridx = 4;
        c.gridheight = 1;
        c.gridwidth = 1;

        labelStyle(labelLogS);
        panel1.add(labelLogS, c);

        JTextField tf6 = new JTextField(this.moleculeObject.logS.toString());
        c.gridy = 1;
        c.gridx = 5;
        c.gridheight = 1;
        c.gridwidth = 2;

        c.fill = GridBagConstraints.HORIZONTAL;
        fieldStyle(tf6);
        panel1.add(tf6, c);


        //---------BUTTON-PANEL & TEXTAREA-----------------------//

        JPanel panelBottom = new JPanel();
//        panelBottom.setLayout(new FlowLayout(FlowLayout.CENTER,70,10));
        panelBottom.setLayout(new BorderLayout(20, 10));
        panelBottom.setPreferredSize(new Dimension(1600, 500));
        panelBottom.setBackground(Color.decode(bg));
        mainPanel.add(panelBottom);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(190, 500));
        buttonPanel.setBackground(Color.decode(bg));
//        buttonPanel.setBackground(Color.BLUE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 0, 10));
        panelBottom.add(buttonPanel, BorderLayout.WEST);


        textArea = new JTextArea();
        textArea.setPreferredSize(new Dimension(710, 500));
        textArea.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        panelBottom.add(textArea, BorderLayout.CENTER);
        textArea.setBackground(Color.decode(bg));
        textArea.setBorder(BorderFactory.createBevelBorder(0, Color.BLACK, Color.decode("#242424")));
        this.setDetailsPane();

//----------BUTTONS----------------------------------//
        JButton button = new JButton(new ImageIcon("assets/DetailsPanel/pubchem.png"));
        stylizeButtons(button);
        buttonPanel.add(button);
        //button.addActionListener(new AnPan2.buttonListener());

        JButton button2 = new JButton(new ImageIcon("assets/DetailsPanel/swissadme.png"));
        stylizeButtons(button2);
        buttonPanel.add(button2);
        //button.addActionListener(new AnPan2.buttonListener());

        JButton button3 = new JButton(new ImageIcon("assets/DetailsPanel/spectra.png"));
        stylizeButtons(button3);
        buttonPanel.add(button3);
        //button.addActionListener(new AnPan2.buttonListener());

        JButton button4 = new JButton(new ImageIcon("assets/DetailsPanel/toxicity.png"));
        stylizeButtons(button4);
        buttonPanel.add(button4);
        //button.addActionListener(new AnPan2.buttonListener());


//------------TEXTFIELDS---------------------------//

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
