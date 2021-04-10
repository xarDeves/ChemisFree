package Core;

import java.awt.*;

public final class Controller {

    private final Model model;
    private final Dimension screenSize;

    public Controller(Model model) {

        this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.model = model;

    }

    public Dimension getScreenSize() {
        return this.screenSize;
    }

}
