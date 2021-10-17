import Core.MasterController;
import Core.Model;
import Core.Views.MainGui;

import javax.swing.*;

//TODO fix JChemPaint erase bug
//TODO fix TextEditor scroll/text wrap bug
//TODO fix DetailsPanel image (if too big -> out of bounds)
public class Main {

    public static void main(String[] args) {

        Model model = new Model();
        MasterController masterController = new MasterController(model);
        SwingUtilities.invokeLater(() -> new MainGui(masterController));

    }
}