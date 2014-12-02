/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opengamecreator.resourcebundle;

import edu.mit.blocks.workspace.ContextMenu;
import java.util.ResourceBundle;

/**
 *
 * @author Campbell Suter
 */
public class ResourceSource implements ContextMenu.BundleSource {

    @Override
    public ResourceBundle getBundle() {
        return ResourceBundle.getBundle("opengamecreator.resourcebundle.ResourceLoader");
    }
}
