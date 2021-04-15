package Sniper;


import com.epam.indigo.Indigo;
import com.epam.indigo.IndigoObject;
import com.ggasoftware.imago.Imago;
import org.openscience.cdk.exception.CDKException;
import org.openscience.jchempaint.JChemPaintCustom;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

public final class SniperMolecule extends SnippingTool {

    public SniperMolecule(Dimension screenSize) throws AWTException {
        super(screenSize);
    }

    @Override
    protected void launchRecon(KeyEvent e) {

        new Thread(() -> {

            Imago imago = new Imago();
            Indigo indigo = new Indigo();

            try {
                imago.loadBufImage(this.snip);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            imago.recognize();
            String result = imago.getResult();
            IndigoObject mol = indigo.loadMolecule(result);
            String smiles = mol.smiles();//.replace(".", "");

            try {
                //TODO change "setDefaultCloseOperation" of "JChemPaint"
                System.out.println("smiles from chempaint : " + new JChemPaintCustom(smiles).getSmilesFromJChem());
            } catch (CDKException | ClassNotFoundException | CloneNotSupportedException | IOException cdkException) {
                cdkException.printStackTrace();
            }

            //makeImageFromSmiles(smiles);
            System.out.println("smiles from imago : " + smiles);

        }).start();

        sniperFrame.setVisible(false);

    }

}
