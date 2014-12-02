/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opengamecreator;

import edu.mit.blocks.codeblocks.Block;
import edu.mit.blocks.workspace.Workspace;

/**
 *
 * @author Campbell Suter
 */
public class BlockInfo {

    private BlockInfo() {
    }

    public static Block getBlockById(Workspace ws, long bid) {
        for (Block block : ws.getBlocks()) {
            if (bid == block.getBlockID()) {
                return block;
            }
        }
        return null;
    }
}
