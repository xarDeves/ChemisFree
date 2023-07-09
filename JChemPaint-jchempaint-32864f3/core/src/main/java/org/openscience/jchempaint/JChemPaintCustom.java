package org.openscience.jchempaint;

import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IChemModel;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.jchempaint.application.JChemPaint;

import java.io.IOException;


public class JChemPaintCustom {

    private final JChemPaintPanel JChemEditorInstance;

    public JChemPaintPanel getChemPaintPanel() {
        return JChemEditorInstance;
    }

    public void show(String title) {
        JChemPaint.showExistingInstance(title, this.JChemEditorInstance);
    }

    /*public static void main(String[] args) throws CDKException, ClassNotFoundException, CloneNotSupportedException, IOException {

        SmilesParser smilesParser = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IAtomContainer molecule = smilesParser.parseSmiles("CCCCCCCCC");

        IChemModel chemModel = DefaultChemObjectBuilder.getInstance().newInstance(IChemModel.class);
        chemModel.setMoleculeSet(chemModel.getBuilder().newInstance(IAtomContainerSet.class));
        chemModel.getMoleculeSet().addAtomContainer(chemModel.getBuilder().newInstance(IAtomContainer.class));
        JChemPaintPanel JChemEditor = JChemPaint.showInstance(chemModel, "", true);

        JChemPaint.generateModel(JChemEditor, molecule, true, false);

        while (true) {
            try {
                Thread.sleep(2000);
                System.out.println(JChemEditor.getSmiles());
            } catch (CDKException |
                    ClassNotFoundException |
                    InterruptedException |
                    IOException |
                    CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

    }*/
    public String getSmilesFromJChem() throws Exception {
        return JChemEditorInstance.getSmiles();
    }

    public JChemPaintCustom(String smiles) throws CDKException {
        SmilesParser smilesParser = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IAtomContainer molecule = smilesParser.parseSmiles(smiles);

        IChemModel chemModel = DefaultChemObjectBuilder.getInstance().newInstance(IChemModel.class);
        chemModel.setMoleculeSet(chemModel.getBuilder().newInstance(IAtomContainerSet.class));
        chemModel.getMoleculeSet().addAtomContainer(chemModel.getBuilder().newInstance(IAtomContainer.class));

        JChemEditorInstance = JChemPaint.getInstance(chemModel, "", false);

        JChemPaint.generateModel(JChemEditorInstance, molecule, true, false);
    }

    public JChemPaintCustom() throws Exception {

        IChemModel chemModel = DefaultChemObjectBuilder.getInstance().newInstance(IChemModel.class);
        chemModel.setMoleculeSet(chemModel.getBuilder().newInstance(IAtomContainerSet.class));
        chemModel.getMoleculeSet().addAtomContainer(chemModel.getBuilder().newInstance(IAtomContainer.class));

        JChemEditorInstance = JChemPaint.showInstance(chemModel, "", false);

        //this.show("");
    }
}