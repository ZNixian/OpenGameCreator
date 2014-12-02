/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opengamecreator.resourcemanager;

import java.io.File;

/**
 *
 * @author Campbell Suter
 */
public abstract class ItemResourceNode extends ItemResource {

    private File location;

    public ItemResourceNode(File location) {
        super(location.getName());
        this.location = location;
    }

    public File getLocation() {
        return location;
    }

    public abstract void open();

    public void renameTo(File newName) {
        location.renameTo(newName);
        setName(newName.getName());
    }

    void delete() {
        location.delete();
    }
}
