/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opengamecreator.resourcemanager;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 *
 * @author Campbell Suter
 */
public class ItemTypeSprite extends ItemType {

    public ItemTypeSprite() {
        super(ItemResourceSprite.class, "Sprites");
    }

    @Override
    public void newItem() {
        File f = findFirstAvalibleFile("tmp/io/resource/sprites/sprite%%%id%%%.png");
        try {
            ImageIO.write(new BufferedImage(32, 32, BufferedImage.TYPE_4BYTE_ABGR), "png", f);
        } catch (IOException ex) {
            Logger.getLogger(ItemTypeSprite.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error:\n" + ex,
                    "Error creating image!", JOptionPane.ERROR_MESSAGE);
        }
    }
}
