/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opengamecreator.resourcemanager;

import edu.mit.blocks.workspace.Page;
import edu.mit.blocks.workspace.Workspace;
import edu.mit.blocks.workspace.WorkspaceWidget;
import java.io.File;
import opengamecreator.OpenGameCreator;

/**
 *
 * @author Campbell Suter
 */
public class ItemResourceObject extends ItemResourceNode {

    private final OpenGameCreator ogc;

    public ItemResourceObject(File location, OpenGameCreator ogc) {
        super(location);
        this.ogc = ogc;
    }

    @Override
    public void open() {
        System.err.println("okkkk!!!!!!!!!");
        Workspace ws = ogc.getWc().getWorkspace();

        for (Page pg : ws.getBlockCanvas().getPages()) {
            pg.setInFullview(false);
        }

        Page pageNamed = ws.getPageNamed(getName());
        pageNamed.setInFullview(true);
    }

    @Override
    public void renameTo(File newName) {
        String newNameString = newName.getName();
        String oldName = getName();
        Workspace wc = ogc.getWc().getWorkspace();

        if (wc.getPageNamed(newNameString) != null) {
            throw new IllegalArgumentException("This object already exists!");
        }

        wc.renamePage(oldName, newNameString);
        super.renameTo(newName); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    void delete() {
        Workspace workspace = ogc.getWc().getWorkspace();
        workspace.removePage(workspace.getPageNamed(getName()));

        super.delete(); //To change body of generated methods, choose Tools | Templates.
    }
}
