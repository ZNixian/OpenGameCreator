/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opengamecreator.resourcebundle;

import java.util.ListResourceBundle;

/**
 *
 * @author Campbell Suter
 */
public class ResourceLoader_en_NZ extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return contents;
    }
    private static final Object[][] contents = {
        {"ardublock.ui.add_comment", "Add Comment"},
        {"ardublock.ui.clone", "Duplicate"}
    };
}
