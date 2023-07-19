package Core.Views;

import org.openscience.jchempaint.JChemPaintCustom;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class ChemPaintTabView extends JFrame {

    JTabbedPane tabbedPane = new JTabbedPane();

    public ChemPaintTabView(HashMap<String, JChemPaintCustom> instances) {
        for (Map.Entry<String, JChemPaintCustom> entry : instances.entrySet()) {
            tabbedPane.addTab(entry.getKey(), entry.getValue().getChemPaintPanel());
            //tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        }

        this.add(tabbedPane);

        this.pack();
        this.setVisible(true);
    }
}
