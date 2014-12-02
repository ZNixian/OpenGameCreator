/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opengamecreator.blocks.impl;

import edu.mit.blocks.codeblocks.Block;
import edu.mit.blocks.workspace.Workspace;
import opengamecreator.blocks.EvalUnit;

/**
 *
 * @author Campbell Suter
 */
public class rgbColour {

    public static String eval(Block b, Workspace w) {
        Block bb = w.getEnv().getBlock(b.getSocketAt(0).getBlockID());
        String strs = EvalUnit.eval(bb, w);
        return "(OGCGame.colourFromString(" + strs + "))";
    }
}
