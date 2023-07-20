package Core.Views;

import Core.MasterThreadPool;
import Core.Molecule;
import Core.Views.Details.DetailsPanel;
import org.openscience.jchempaint.JChemPaintCustom;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class MoleculeSearch extends JFrame {

    private JTextField textField;
    private boolean textFieldCleared = false;
    private Molecule molecule = null;

    public MoleculeSearch() {
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(Color.decode("#1A1A1A"));

        textField = new JTextField();
        setupTextField();

        JButton editBtn = createButton("Edit Structure", Color.decode("#006aff"));
        JButton detailBtn = createButton("Details", Color.decode("#006aff"));

        editBtn.addActionListener(new EditListener());
        detailBtn.addActionListener(new DetailListener());

        this.add(textField);
        this.add(editBtn);
        this.add(detailBtn);
        this.pack();
        this.setVisible(true);
    }

    private void setupTextField() {
        textField.setPreferredSize(new Dimension(250, 40));
        textField.setFont(new Font("Consolas", Font.ITALIC, 15));
        textField.setForeground(Color.LIGHT_GRAY);
        textField.setBackground(Color.decode("#1A1A1A"));
        textField.setSelectionColor(Color.GRAY);
        textField.setBorder(BorderFactory.createBevelBorder(0, Color.BLACK, Color.decode("#242424")));
        textField.setText("Enter Name or SMILES");
        textField.addMouseListener(new TextFieldMouseListener());
        textField.addKeyListener(new KeyPressListener());
    }

    private JButton createButton(String label, Color foregroundColor) {
        JButton button = new JButton(label);
        button.setForeground(foregroundColor);
        button.setBackground(Color.decode("#1A1A1A"));
        button.setBorder(BorderFactory.createEmptyBorder());
        return button;
    }

    private void makeMolecule() {
        String textFieldData = textField.getText().toUpperCase(Locale.ROOT);
        molecule = new Molecule(textFieldData);
    }

    private class EditListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            MasterThreadPool.getPool().submit(() -> {
                try {
                    makeMolecule();
                    new JChemPaintCustom(molecule.smiles).show(molecule.name);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }
    }

    private class DetailListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            MasterThreadPool.getPool().submit(() -> {
                makeMolecule();
                new DetailsPanel(molecule);
            });
        }
    }

    private class KeyPressListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (!textFieldCleared) {
                textField.setText("");
                textFieldCleared = true;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }

    private class TextFieldMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (!textFieldCleared) {
                textField.setText("");
                textFieldCleared = true;
            }
        }
    }

}
