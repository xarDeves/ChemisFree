package Core.Views;

import Core.Molecule;
import Core.Views.Details.DetailsPanel;
import org.openscience.jchempaint.JChemPaintCustom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Locale;

public class MoleculeSearch extends JFrame {

    private final JTextField textField;

    private boolean textFieldCleared = false;

    private Molecule molecule = null;

    public MoleculeSearch() {

        this.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        this.setLocationRelativeTo(null);

        textField = new JTextField();
        textField.addMouseListener(new TextFieldListener());
        textField.addKeyListener(new KeyPressListener());
        textField.setPreferredSize(new Dimension(250, 40));
        textField.setFont(new Font("Consolas", Font.ITALIC, 15));
        textField.setForeground(Color.BLACK);
        textField.setBackground(Color.WHITE);
        textField.setSelectionColor(Color.GRAY);
        textField.setText("Enter Name or SMILES");

        JButton editBtn = new JButton("Edit Structure");
        JButton detailBtn = new JButton("Details");

        editBtn.addActionListener(new EditListener());
        detailBtn.addActionListener(new detailListener());

        this.add(textField);
        this.add(editBtn);
        this.add(detailBtn);
        this.pack();
        this.setVisible(true);
    }

    //prevents creating new molecule if previous is the same
    private void makeMolecule() {

        String textFieldData = textField.getText().toUpperCase(Locale.ROOT);

        if (this.molecule != null) {
            if (!(this.molecule.name.equals(textFieldData) || this.molecule.smiles.equals(textFieldData))) {
                molecule = new Molecule(textFieldData);
                System.out.println("made new");
            }
        } else {
            molecule = new Molecule(textFieldData);
        }
    }

    private class EditListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            new Thread(() -> {

                try {
                    makeMolecule();
                    new JChemPaintCustom(molecule.smiles).show(molecule.name);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }).start();

        }
    }

    private class detailListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            makeMolecule();
            new DetailsPanel(molecule);

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

    private class TextFieldListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (!textFieldCleared) {
                textField.setText("");
                textFieldCleared = true;
            }
        }
    }
}
