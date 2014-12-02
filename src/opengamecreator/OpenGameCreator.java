/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opengamecreator;

import edu.mit.blocks.controller.WorkspaceController;
import edu.mit.blocks.workspace.ContextMenu;
import edu.mit.blocks.workspace.Page;
import edu.mit.blocks.workspace.SearchBar;
import edu.mit.blocks.workspace.SearchableContainer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import opengamecreator.compiler.BlockCompiler;
import opengamecreator.compiler.SaveDialog;
import opengamecreator.resourcebundle.ResourceSource;
import opengamecreator.resourcemanager.ResourceWindow;
import org.w3c.dom.Node;

/**
 *
 * @author Campbell Suter
 */
public class OpenGameCreator {

    private WorkspaceController wc;
    private JFrame window;
    private MenuHolder menus;
    private BlockCompiler compiler;
    private SearchBar sb;
    private JDesktopPane desktopPane;
    private JInternalFrame logicEditorWindow;
    private File openFile = new File("default-untitled.ogcgamebundle");
    private ResourceWindow resourceWindow;
    private JFileChooser fileChooser = new JFileChooser(".");

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException |
                InstantiationException | IllegalAccessException e) {
            Logger.getLogger(OpenGameCreator.class.getName()).log(Level.WARNING, null, e);
        }

        OpenGameCreator ogc = new OpenGameCreator();
    }

    public OpenGameCreator() throws IOException {
        compiler = BlockCompiler.getDefaultBlockCompiler(); //new BlockCompiler();
        wc = new WorkspaceController();
        initLang();
        initGUI();
    }

    private void initGUI() {

        ContextMenu.bundleSource = new ResourceSource();

        loadNewProject();

        menus = new MenuHolder();

        menus.resourceManagerIcon = new JMenuItem("Resource Manager");
        menus.resourceManagerIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resourceWindow.getFrame().show();
            }
        });

        menus.logicEditorItem = new JMenuItem("Logic Editor");
        menus.logicEditorItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logicEditorWindow.show();
            }
        });

        menus.windowItem = new JMenu("Window");
        menus.windowItem.add(menus.resourceManagerIcon);
        menus.windowItem.add(menus.logicEditorItem);

        menus.new_ = new JMenuItem("New");
        menus.new_.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        menus.new_.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });

        menus.open = new JMenuItem("Open");
        menus.open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        menus.open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SaveDialog.getInstance().showSaveDialog(OpenGameCreator.this)) {
//                    int result = fileChooser.showDialog(null, "Open");
                    loadProject(openFile);
                }
            }
        });

        menus.save = new JMenuItem("Save");
        menus.save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        menus.save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveProject(openFile);
            }
        });

        menus.saveAs = new JMenuItem("Save As");
        menus.saveAs.setAccelerator(KeyStroke.
                getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
        menus.saveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });

        menus.run = new JMenuItem("Run");
        menus.run.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0));
        menus.run.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                compiler.compileAndRun(wc);
            }
        });

        menus.build = new JMenuItem("Build / Compile");
        menus.build.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                compiler.compile(wc, false);
            }
        });

        menus.fileItem = new JMenu("File");
        menus.fileItem.add(menus.new_);
        menus.fileItem.add(menus.open);
        menus.fileItem.add(menus.save);
        menus.fileItem.add(menus.saveAs);
        menus.fileItem.add(menus.run);
        menus.fileItem.add(menus.build);

        menus.menubar = new JMenuBar();
        menus.menubar.add(menus.fileItem);
        menus.menubar.add(menus.windowItem);


        window = new JFrame("Open Game Creator");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        desktopPane = new JDesktopPane();

        sb = new SearchBar("Search blocks",
                "Search for blocks in the drawers and workspace", wc.getWorkspace());
        sb.getComponent().setPreferredSize(new Dimension(130, 23));
        resetSearch();

        window.setContentPane(desktopPane);
        window.setBounds(10, 10, 500, 500);
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setJMenuBar(menus.menubar);
        window.setVisible(true);

        logicEditorWindow = new JInternalFrame("Game Logic");
        logicEditorWindow.setResizable(true);
        logicEditorWindow.setClosable(true);
        logicEditorWindow.setMaximizable(true);
        logicEditorWindow.setIconifiable(true);
        logicEditorWindow.setSize(200, 200);
        logicEditorWindow.setVisible(true);
        logicEditorWindow.setLocation(60, 60);
        logicEditorWindow.add(wc.getWorkspacePanel());
        logicEditorWindow.add(wc.getSearchBar(), BorderLayout.NORTH);
        logicEditorWindow.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        desktopPane.add(logicEditorWindow);
        resourceWindow = new ResourceWindow(this);
        desktopPane.add(resourceWindow.getFrame());
    }

    private void resetSearch() {
        sb.reset();
        for (final SearchableContainer con : wc.getAllSearchableContainers()) {
            sb.addSearchableContainer(con);
        }
    }

    private void initLang() throws IOException {
        wc.resetLanguage();
        wc.setLangDefDtd(this.getClass().getResourceAsStream("/opengamecreator/lang/lang_def.dtd"));
        wc.setLangDefStream(this.getClass().getResourceAsStream("/opengamecreator/lang/lang_def.xml"));
        wc.resetWorkspace();
//        loadNewProject();
    }

    public void loadNewProject() {
        wc.loadFreshWorkspace();
        clearTmpFiles();

        Page global = new Page(wc.getWorkspace(), "Global");
        wc.getWorkspace().addPage(global);

        // remove the 'turtles' page
        wc.getWorkspace().removePageAt(0);
    }

    private void clearTmpFiles() {
        File tmp = new File("tmp/");
        Utils.delete(tmp);
    }

    public void prepProjectChange() {
        resetSearch();
        resourceWindow.refresh();
    }

    public void loadProject(File project) {
        clearTmpFiles();
        try {
            File tmp = new File("tmp/io/");
//            File res = new File("tmp/resource");
            File nres = new File(tmp, "resource");
//            res.delete();
            nres.delete();
            tmp.delete();
            tmp.mkdirs();

            ZipFile zf = new ZipFile(project);
            zf.extractAll(tmp.getAbsolutePath());

            File xml = new File(tmp, "opengamecreator/src");
            wc.resetWorkspace();
            wc.loadProjectFromPath(xml.getAbsolutePath());

//            if (nres.exists()) {
//                System.out.println("ok: " + res.exists());
//                nres.renameTo(res);
//                System.out.println("ok: " + nres + " moved to " + res);
//            } else {
//                res.mkdirs();
//            }
        } catch (ZipException | IOException ex) {
            Logger.getLogger(OpenGameCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
        prepProjectChange();
    }

    public void saveProject(File project) {
        try {
            File saveTo = new File("tmp/io/output.tmp");
            File tmp = new File("tmp/io/opengamecreator");
            File res = new File("tmp/io/resource");
            res.mkdirs();
            Utils.delete(tmp);
            tmp.mkdirs();
            saveTo.delete();

            ZipFile zf = new ZipFile(saveTo);

            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

            File xml = new File(tmp, "src");
            saveFile(getBlocksSaveString(), xml);
            zf.addFolder(tmp, parameters);
            zf.addFolder(res, parameters);

            project.delete();
            saveTo.renameTo(project);
        } catch (ZipException | IOException ex) {
            Logger.getLogger(OpenGameCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void saveFile(String content, File fi) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fi))) {
            bw.write(content);
        }
    }

    public WorkspaceController getWc() {
        return wc;
    }

    public void showSaveDialog() {
    }

    public String getBlocksSaveString() {
        try {
            Node node = wc.getSaveNode(false); // gives us some horrorable error!

            StringWriter writer = new StringWriter();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(node), new StreamResult(writer));
            return writer.toString();
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }
}
