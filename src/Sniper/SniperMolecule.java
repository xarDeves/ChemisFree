package Sniper;

import Core.Views.ChemPaintTabView;
import com.epam.indigo.Indigo;
import com.ggasoftware.imago.Imago;
import gov.nih.ncats.molvec.Molvec;
import org.openscience.jchempaint.JChemPaintCustom;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public final class SniperMolecule extends SnippingTool {

    private final HashMap<String, JChemPaintCustom> instances = new HashMap<>(2);

    private final Indigo indigo = new Indigo();
    private final Imago imago = new Imago();

    public SniperMolecule(Dimension screenSize) throws AWTException {
        super(screenSize);
    }

    private void recImago() {

        try {
            imago.loadBufImage(this.snip);
            imago.recognize();

            String result = imago.getResult();
            String smiles = indigo.loadMolecule(result).smiles();
            System.out.println("Smiles from imago : " + smiles);

            if (!smiles.isBlank()) {
                this.instances.put("Imago", new JChemPaintCustom(smiles));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void recMolvec() {

        try {
            String result = Molvec.ocr(this.snip);
            String smiles = indigo.loadMolecule(result).smiles();
            System.out.println("Smiles from molvec : " + smiles);

            if (!smiles.isBlank()) {
                this.instances.put("Molvec", new JChemPaintCustom(smiles));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void launchRecon(KeyEvent e) {

        new Thread(() -> {

            //TODO these JChem instances should not have a smile insertion bar
            recImago();
            recMolvec();
            new ChemPaintTabView(this.instances);

        }).start();

        sniperFrame.setVisible(false);
    }

}
