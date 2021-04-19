package Core.Views.TextEditor;

import Helpers.SmileNameConverter;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.depict.DepictionGenerator;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class MoleculeButton extends JButton {

    private BufferedImage molImage;
    private JWindow imageWindow = null;

    //TODO add molecular weight
    private void makeImageFromSmiles(String smiles, String molecule) throws CDKException {

        SmilesParser smipar = new SmilesParser(SilentChemObjectBuilder.getInstance());
        IAtomContainer mol = smipar.parseSmiles(smiles);
        mol.setProperty(CDKConstants.TITLE, molecule);

        DepictionGenerator dptgen = new DepictionGenerator();
        // size in px (raster) or mm (vector)
        // annotations are red by default
        dptgen.withSize(300, 350)
                .withMolTitle()
                .withTitleColor(Color.DARK_GRAY);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            dptgen.depict(mol).writeTo("png", out);
            byte[] data = out.toByteArray();
            ByteArrayInputStream input = new ByteArrayInputStream(data);
            molImage = ImageIO.read(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void makeImageWindow() {

        this.imageWindow = new JWindow();
        JPanel panel = new JPanel();
        JLabel l = new JLabel(new ImageIcon(this.molImage));

        panel.setBorder(BorderFactory.createLineBorder(Color.black));

        panel.add(l);
        imageWindow.add(panel);

        imageWindow.pack();

    }

    public MoleculeButton(String molecule) {

        super(molecule);

        new Thread(() -> {

            try {
                makeImageFromSmiles(SmileNameConverter.parse(molecule), molecule);
                makeImageWindow();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();

        //hovered
        this.getModel().addChangeListener(e -> {

            ButtonModel model = (ButtonModel) e.getSource();

            //FIXME this is buggy
            if (model.isRollover() && !this.imageWindow.isVisible()) {
                this.imageWindow.setLocation(MouseInfo.getPointerInfo().getLocation());
                this.imageWindow.setVisible(true);
            } else {
                this.imageWindow.setVisible(false);
            }

        });

        //pressed
        this.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                System.out.println("PRESSED");
                //((JButton) e.getSource()).setText("I'm a super button!! Or label...");
            }
        });

        this.setFont(new Font("tahoma", Font.PLAIN, 17));
        this.setForeground(Color.green);
        this.setBackground(Color.DARK_GRAY);
        this.setContentAreaFilled(false);
        this.setBorder(null);
        /*this.setBorderPainted(false);
        this.setFocusPainted(false);
        this.setOpaque(true);*/
    }

}
