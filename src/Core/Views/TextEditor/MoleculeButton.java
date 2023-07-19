package Core.Views.TextEditor;

import Core.MasterThreadPool;
import Core.Molecule;
import Core.Views.Details.DetailsPanel;
import org.openscience.cdk.depict.DepictionGenerator;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MoleculeButton extends JButton {

    private JWindow imageWindow = null;
    private BufferedImage molImage = null;
    private Molecule moleculeObject;

    public MoleculeButton(String molecule) {
        super(molecule);
        initializeStyles();
        createMoleculeImageAsync(molecule);
        attachListeners();
    }

    private void initializeStyles() {
        this.setFont(new Font("Tahoma", Font.PLAIN, 17));
        this.setForeground(Color.green);
        this.setBackground(Color.DARK_GRAY);
        this.setContentAreaFilled(false);
        this.setBorder(null);
        /*this.setBorderPainted(false);
        this.setFocusPainted(false);
        this.setOpaque(true);*/
    }

    private void createMoleculeImageAsync(String smiles) {
        MasterThreadPool.getPool().submit(() -> {
            try {
                this.moleculeObject = new Molecule(smiles);
                makeImageFromSmiles(this.moleculeObject.smiles);
                makeImageWindow();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void makeImageFromSmiles(String smiles) throws CDKException {
        final SmilesParser smilesParser = new SmilesParser(SilentChemObjectBuilder.getInstance());
        IAtomContainer mol = smilesParser.parseSmiles(smiles);
        DepictionGenerator depictionGenerator = new DepictionGenerator().withBackgroundColor(new Color(0f, 0f, 0f, 0f)).withAtomColors();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            depictionGenerator.depict(mol).writeTo("png", out);
            byte[] data = out.toByteArray();
            ByteArrayInputStream input = new ByteArrayInputStream(data);
            this.molImage = ImageIO.read(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeImageWindow() {
        this.imageWindow = new JWindow();
        JPanel panel = new JPanel();
        JLabel label = new JLabel(new ImageIcon(this.molImage));
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        panel.add(label);
        imageWindow.add(panel);
        imageWindow.pack();
    }

    private void attachListeners() {
        this.getModel().addChangeListener(e -> {
            ButtonModel model = (ButtonModel) e.getSource();
            if (model.isRollover() && !this.imageWindow.isVisible()) {
                this.imageWindow.setLocation(MouseInfo.getPointerInfo().getLocation());
                this.imageWindow.setVisible(true);
            } else {
                this.imageWindow.setVisible(false);
            }
        });

        this.addActionListener(e -> {
            new DetailsPanel(moleculeObject);
            //((JButton) e.getSource()).setText("I'm a super button!! Or label...");
        });
    }
}