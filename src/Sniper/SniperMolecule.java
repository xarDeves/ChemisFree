package Sniper;

import com.epam.indigo.Indigo;
import com.epam.indigo.IndigoObject;
import com.ggasoftware.imago.Imago;
import gov.nih.ncats.molvec.Molvec;
import org.openscience.cdk.exception.CDKException;
import org.openscience.jchempaint.JChemPaintCustom;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

public final class SniperMolecule extends SnippingTool {

    private static final Indigo indigo = new Indigo();
    private static final Imago imago = new Imago();

    public SniperMolecule(Dimension screenSize) throws AWTException {
        super(screenSize);
    }

    private String getImagoSmiles() {

        try {
            imago.loadBufImage(this.snip);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        imago.recognize();
        String result = imago.getResult();
        //TODO deprecate "smiles" string, return value directly
        String smiles = indigo.loadMolecule(result).smiles();
        System.out.println("Smiles from imago : " + smiles);

        return smiles;
    }

    private String getMolvecSmiles() {

        String result = null;
        try {
            result = Molvec.ocr(this.snip);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //TODO deprecate "smiles" string, return value directly
        String smiles = indigo.loadMolecule(result).smiles();
        System.out.println("Smiles from molvec : " + smiles);

        return smiles;
    }

    @Override
    protected void launchRecon(KeyEvent e) {

        new Thread(() -> {

            //TODO check if smiles are blank and create ui elements accordingly
            try {
                new JChemPaintCustom(getImagoSmiles());
                new JChemPaintCustom(getMolvecSmiles());
            } catch (CDKException cdkException) {
                cdkException.printStackTrace();
            }

        }).start();

        sniperFrame.setVisible(false);
    }

}
