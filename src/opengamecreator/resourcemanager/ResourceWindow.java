/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opengamecreator.resourcemanager;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import opengamecreator.OpenGameCreator;

/**
 *
 * @author Campbell Suter
 */
public class ResourceWindow {

    private JTree resources;
    private JInternalFrame frame;
    private DefaultMutableTreeNode rootNode;
    private OpenGameCreator parent;
    ////////
    private DefaultMutableTreeNode itemSprites;
    private DefaultMutableTreeNode itemObjects;
    private DefaultMutableTreeNode itemLevels;
    ////////
    private JPopupMenu popupResource = new JPopupMenu("Resource");
    private JPopupMenu popupResourceType = new JPopupMenu("Resource Type");
    ///////
    public DefaultMutableTreeNode selectedNode;
    public TreePath selectedPath;

    public ResourceWindow(OpenGameCreator parent) {
        this.parent = parent;
        // everyone uses the delete button:
        Action delete = new AbstractAction("Delete") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedNode.getUserObject() instanceof ItemResourceNode) {
                    String responce = JOptionPane.showInputDialog(
                            "Are you sure?\n"
                            + "WARNING: This will evaporate the file,"
                            + "\n"
                            + "not move it to the recycle bin."
                            + "\n"
                            + "If so, type \"YeS\" (same"
                            + "\n"
                            + "caps, just to make sure).");
                    boolean sure = responce == null ? false : responce.equals("YeS");

                    if (!sure) {
                        return;
                    }
                    ((ItemResourceNode) selectedNode.getUserObject()).delete();
                    refresh();
                }
            }
        };

        Action rename = new AbstractAction("Rename") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedNode.getUserObject() instanceof ItemResourceNode) {
                    String newName = JOptionPane.showInputDialog("What do you want to call this new file?");
                    if (newName != null && !newName.isEmpty()) {
                        File old = ((ItemResourceNode) selectedNode.getUserObject()).getLocation();
                        File newFile = new File(old.getParentFile(), newName);
                        ((ItemResourceNode) selectedNode.getUserObject()).renameTo(newFile); // let it deal with it.
                        refresh();
                    }
                }
            }
        };
        // For any category of resource:
        Action vpmtemp = new AbstractAction("Add asset") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedNode.getUserObject() instanceof ItemType) {
                    try {
                        ((ItemType) selectedNode.getUserObject()).newItem();
                        refresh();
                    } catch (RuntimeException rex) {
                        JOptionPane.showMessageDialog(null, "Error:\n" + rex);
                    }
                }
            }
        };

        popupResourceType.add(vpmtemp);

        // and for single resources:
        popupResource.add(delete);

        popupResource.add(rename);
        ////////////////////////
        rootNode = new DefaultMutableTreeNode("Game");
        resources = new JTree(rootNode, true);

        refresh();
        MouseListener ml = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selRow = resources.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = resources.getPathForLocation(e.getX(), e.getY());
                if (selRow != -1) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        if (e.getClickCount() == 1) {
                        } else if (e.getClickCount() == 2) {
                            // double click
                            if (node.getUserObject() instanceof ItemResourceNode) {
                                ItemResourceNode resource = (ItemResourceNode) node.getUserObject();
                                resource.open();
                            }
                        }
                    } else if (e.getButton() == MouseEvent.BUTTON3) {
                        if (e.getClickCount() == 1) {
                            // right click
                            int x = e.getX();
                            int y = e.getY();
                            showPopup(x, y, selPath, node);
                        } else if (e.getClickCount() == 2) {
                        }
                    }
                }
            }
        };

        resources.addMouseListener(ml);
        frame = new JInternalFrame("Resource Manager");

        frame.setResizable(
                true);
        frame.setClosable(
                true);
        frame.setMaximizable(
                true);
        frame.setIconifiable(
                true);
        frame.setSize(
                200, 200);
        frame.setLocation(
                60, 60);
        frame.setContentPane(resources);

        frame.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
    }

    public void refresh() {
        rootNode.removeAllChildren();

        itemSprites = new DefaultMutableTreeNode(new ItemTypeSprite());
        allAllResources(itemSprites, "sprites", ItemResourceSprite.class, parent);

        itemObjects = new DefaultMutableTreeNode(new ItemTypeObject(parent));
        allAllResources(itemObjects, "objects", ItemResourceObject.class, parent);

        itemLevels = new DefaultMutableTreeNode("Levels");
        allAllResources(itemLevels, "levels", null, parent);

        rootNode.add(itemSprites);

        rootNode.add(itemObjects);

        rootNode.add(itemLevels);
        DefaultTreeModel treeModel = (DefaultTreeModel) resources.getModel();

        treeModel.reload();
    }

    public JInternalFrame getFrame() {
        return frame;
    }

    private void allAllResources(DefaultMutableTreeNode mtn, String type,
            Class<? extends ItemResource> resourceType, OpenGameCreator ogc) {
        File f = new File("tmp/io/resource/" + type);
        if (!f.exists()) {
            boolean success = f.mkdirs();
            if (!success) {
                JOptionPane.showMessageDialog(null, "");

                if (f.exists()) {
                    throw new RuntimeException("Folder already exists");
                }

                if (!f.getParentFile().canWrite()) {
                    throw new RuntimeException("No write access to create the folder");
                }
            }
        }
        try {
            for (File fii : f.listFiles()) {
                addFileToMTN(mtn, fii, resourceType, ogc);
            }
        } catch (NoSuchMethodException | InstantiationException |
                IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(ResourceWindow.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void addFileToMTN(DefaultMutableTreeNode mtn, File fi,
            Class<? extends ItemResource> resourceType, OpenGameCreator ogc)
            ////
            throws NoSuchMethodException, InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        ///
        if (fi.isDirectory()) {
            DefaultMutableTreeNode newMtn = new DefaultMutableTreeNode(fi.getName());
            for (File fii : fi.listFiles()) {
                addFileToMTN(newMtn, fii, resourceType, ogc);
            }
            mtn.add(newMtn);


        } else {
            ItemResource irs = resourceType.getConstructor(File.class, OpenGameCreator.class).
                    newInstance(fi, ogc);
            DefaultMutableTreeNode newMtn = new DefaultMutableTreeNode(irs, false);

            mtn.add(newMtn);
        }
    }

    public void showPopup(int x, int y, TreePath selPath, DefaultMutableTreeNode node) {
        selectedPath = selPath;
        selectedNode = node;

        if (node.getUserObject() instanceof ItemResourceNode) {
            popupResource.show(resources, x, y);
        } else if (node.getUserObject() instanceof ItemType) {
            popupResourceType.show(resources, x, y);
        }
        resources.setSelectionPath(selPath);
    }
}
