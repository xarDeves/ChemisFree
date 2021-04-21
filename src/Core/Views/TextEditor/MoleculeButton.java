package Core.Views.TextEditor;

import Core.Molecule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;


public class MoleculeButton extends JButton {

    private JWindow imageWindow = null;
    private Molecule molecule;

    private void makeImageWindow(BufferedImage molImage) {

        this.imageWindow = new JWindow();
        JPanel panel = new JPanel();
        JLabel l = new JLabel(new ImageIcon(molImage));

        panel.setBorder(BorderFactory.createLineBorder(Color.black));

        panel.add(l);
        imageWindow.add(panel);

        imageWindow.pack();
    }

    public MoleculeButton(String molecule) {

        super(molecule);

        new Thread(() -> {

            try {
                this.molecule = new Molecule(molecule);
                makeImageWindow(this.molecule.molImage);
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
