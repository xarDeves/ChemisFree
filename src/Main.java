import Core.Controller;
import Core.Model;
import Core.Views.MainGui;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        Model model = new Model();
        Controller controller = new Controller(model);
        SwingUtilities.invokeLater(() -> new MainGui(controller));

    }
}
