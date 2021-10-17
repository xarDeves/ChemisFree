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
        this.getContentPane().setBackground(Color.decode("#1A1A1A"));

        textField = new JTextField();
        textField.addMouseListener(new TextFieldListener());
        textField.addKeyListener(new KeyPressListener());
        textField.setPreferredSize(new Dimension(250, 40));
        textField.setFont(new Font("Consolas", Font.ITALIC, 15));
        textField.setForeground(Color.LIGHT_GRAY);
        textField.setBackground(Color.decode("#1A1A1A"));
        textField.setSelectionColor(Color.GRAY);
        textField.setBorder(BorderFactory.createBevelBorder(0, Color.BLACK, Color.decode("#242424")));
        textField.setText("Enter Name or SMILES");

        JButton editBtn = new JButton("Edit Structure");
        editBtn.setForeground(Color.decode("#006aff"));
        editBtn.setBackground(Color.decode("#1A1A1A"));
        editBtn.setBorder(BorderFactory.createEmptyBorder());
        JButton detailBtn = new JButton("Details");
        detailBtn.setForeground(Color.decode("#006aff"));
        detailBtn.setBackground(Color.decode("#1A1A1A"));
        detailBtn.setBorder(BorderFactory.createEmptyBorder());

        editBtn.addActionListener(new EditListener());
        detailBtn.addActionListener(new detailListener());

        this.add(textField);
        this.add(editBtn);
        this.add(detailBtn);
        this.pack();
        this.setVisible(true);
    }

    //TODO throw popup on molecule creation failure (test without net)
    //prevents creating new molecule if previous is the same
    private void makeMolecule() {

        String textFieldData = textField.getText().toUpperCase(Locale.ROOT);
        molecule = new Molecule(textFieldData);

        /*if (this.molecule != null) {
            if (!(this.molecule.name.equals(textFieldData) || this.molecule.smiles.equals(textFieldData))) {
                molecule = new Molecule(textFieldData);
                System.out.println("made new");
            }
        } else {
            molecule = new Molecule(textFieldData);
        }*/
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

            new Thread(() -> {
                makeMolecule();
                new DetailsPanel(molecule);
            }).start();
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
