/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opengamecreator.compiler;

import javax.swing.JOptionPane;
import opengamecreator.OpenGameCreator;

/**
 *
 * @author Campbell Suter
 */
public class SaveDialog {

    private SaveDialog() {
    }

    public static SaveDialog getInstance() {
        return SaveDialogHolder.INSTANCE;
    }

    private static class SaveDialogHolder {

        private static final SaveDialog INSTANCE = new SaveDialog();
    }

    public int showSaveDialog(OpenGameCreator ogc) {
//        int result = JOptionPane.
//                showConfirmDialog(null, "Do you want to save the\ncurrently open project?",
//                "Save?", JOptionPane.YES_NO_CANCEL_OPTION);
        int result = JOptionPane.
                showConfirmDialog(null, "There may be unsaved changes.",
                "Save?", JOptionPane.OK_CANCEL_OPTION);
        return result;
    }
}
