package Core;

import Core.Views.MoleculeSearch;
import Core.Views.TextEditor.TextEditor;
import Sniper.SniperMolecule;
import org.openscience.jchempaint.JChemPaintCustom;

import java.awt.*;
import java.util.concurrent.ExecutorService;

public final class MasterController {

    private final ExecutorService threadPool;

    public MasterController() {
        threadPool = MasterThreadPool.getPool();
    }

    public void createSniperMolecule() {
        threadPool.submit(() -> {
            try {
                new SniperMolecule(Toolkit.getDefaultToolkit().getScreenSize());
            } catch (AWTException awtException) {
                awtException.printStackTrace();
            }
        });
    }

    public void createTextEditorThread() {
        threadPool.submit(TextEditor::new);
    }

    public void createMoleculeSearchThread() {
        threadPool.submit(MoleculeSearch::new);
    }

    public void createJChemPaintCustom() {
        threadPool.submit(() -> {
            try {
                new JChemPaintCustom();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public void shutdownThreadPool() {
        threadPool.shutdown();
    }
}
