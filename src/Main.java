import Core.MasterController;
import Core.Model;
import Core.Views.MainGui;

import javax.swing.*;

//check out pyMol
//TODO fix JChemPaint erase bug
//TODO fix TextEditor scroll/text wrap bug
//TODO fix DetailsPanel image (if too big -> out of bounds)
//TODO insert last dependencies (retrosynthesis AI, NMRdb, similarity finder) and the phytochemical db, connect all with the rest of the app
//TODO TextEditor: change all of the quantities at once (if possible)
//TODO TextEditor: molecule images should have a "details" and "edit structure" button (or link)
//TODO TextEditor: highlight time and give choice to start a timer
//TODO DetailsPanel: differentiate font size/color between headlines and details
//TODO DetailsPanel: update buttons (erase "toxicity" and add "Synthesis", "msds" and "Similar molecules"
//TODO DetailsPanel: fix molecular formula
//TODO DetailsPanel: pull logP from pubchem (or other website) and fix the algorithm to calculate logS
//TODO DetailsPanel: make the pubchem and swissADME buttons functional (when pressed they open the molecule's page, not the homepage)
//TODO DetailsPanel: create more Druglikeness pics and fix the algorithm so that the correct pic is displayed each time
//TODO create those little dialog boxes that explain stuff when you hover the cursor on a button, then fill them with useful details
//TODO figure out what little things to include in the "more" button on the main bar, things like timer, calculator, measurement unit calculator etc
//TODO connect all of the app's different components (drawing panel to detailspanel etc)
//TODO fix main bar's moving problem and add a "minimize" and "close" button to every panel
//TODO if possible find a way to calculate other useful descriptors like solubility, pKa, isoelectric point etc
//TODO stop having alzheimer's and remember what else there is to do

public class Main {

    public static void main(String[] args) {

        Model model = new Model();
        MasterController masterController = new MasterController(model);
        SwingUtilities.invokeLater(() -> new MainGui(masterController));

    }
}