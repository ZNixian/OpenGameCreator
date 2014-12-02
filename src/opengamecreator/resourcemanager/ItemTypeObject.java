/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opengamecreator.resourcemanager;

import edu.mit.blocks.workspace.Page;
import edu.mit.blocks.workspace.Workspace;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import opengamecreator.OpenGameCreator;

/**
 *
 * @author Campbell Suter
 */
public class ItemTypeObject extends ItemType {

    private final OpenGameCreator gameCreator;

    public ItemTypeObject(OpenGameCreator gameCreator) {
        super(ItemResourceObject.class, "Objects");
        this.gameCreator = gameCreator;
    }

    @Override
    public void newItem() {
        try {
            File f = findFirstAvalibleFile("tmp/io/resource/objects/object%%%id%%%");
            f.createNewFile();
            Workspace wc = gameCreator.getWc().getWorkspace();

            if (wc.getPageNamed(f.getName()) != null) { // if this page already exists
                throw new IllegalArgumentException("This object already exists!");
            }

            Page page = new Page(wc, f.getName());
            wc.addPage(page);
        } catch (IOException ex) {
            Logger.getLogger(ItemTypeObject.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
