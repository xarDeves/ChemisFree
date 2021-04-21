package Core.Views;

import Helpers.SmileNameConverter;
import org.openscience.cdk.exception.CDKException;
import org.openscience.jchempaint.JChemPaintCustom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MoleculeSearch extends MyFrame {

    private final JButton detailBtn, editBtn;
    private final JTextField textField;

    private boolean textFieldCleared = false;

    public MoleculeSearch() {

        this.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        this.setLocationRelativeTo(null);

        textField = new JTextField();
        textField.addMouseListener(new TextFieldListener());
        textField.setPreferredSize(new Dimension(250, 40));
        textField.setFont(new Font("Consolas", Font.ITALIC, 15));
        textField.setForeground(Color.BLACK);
        textField.setBackground(Color.WHITE);
        textField.setSelectionColor(Color.GRAY);
        textField.setText("Enter Name or SMILES");

        editBtn = new JButton("Edit Structure");
        detailBtn = new JButton("Details");

        editBtn.addActionListener(new EditListener());
        detailBtn.addActionListener(new detailListener());

        this.add(textField);
        this.add(editBtn);
        this.add(detailBtn);
        this.pack();
        this.setVisible(true);
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

    private class EditListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            new Thread(() -> {

                //FIXME refactor using "Molecule" class
                try {
                    new JChemPaintCustom(SmileNameConverter.parse(textField.getText()));
                } catch (CDKException cdkException) {
                    cdkException.printStackTrace();
                }

            }).start();

        }
    }

    private class detailListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            //SmileNameConverter.parse(textField.getText());

        }
    }
}
