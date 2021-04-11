import Core.MasterController;
import Core.Model;
import Core.Views.MainGui;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        Model model = new Model();
        MasterController masterController = new MasterController(model);
        SwingUtilities.invokeLater(() -> new MainGui(masterController));

    }
}
