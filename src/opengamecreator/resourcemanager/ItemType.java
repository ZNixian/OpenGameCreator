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
public abstract class ItemType extends ItemResource {

    private Class<? extends ItemResourceNode> type;

    public ItemType(Class<? extends ItemResourceNode> type, String name) {
        super(name);
        this.type = type;
    }

    public abstract void newItem();

    public static File findFirstAvalibleFile(String path) {
        int i = 1;
        while (true) {
            File fi = new File(path.replace("%%%id%%%", i + ""));
            if(!fi.exists()) {
                return fi;
            }
            i++;
        }
    }
}
