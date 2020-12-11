package mainapp;

import factory.TodoPanelComponent;
import factory.ButtonCreator;
import about.About;
import beans.Todo;
import beans.TodosList;
import consts.Constants;
import consts.Resources;
import dao.DaoInterface;
import help.Help;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import static mainapp.TodoStatusModel.DOING_STATE;
import static mainapp.TodoStatusModel.DONE_STATE;
import static mainapp.TodoStatusModel.PENDING_DOING_STATE;
import static mainapp.TodoStatusModel.PENDING_STATE;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import static splash_screen.SplashScreen.mainFont;
import todoListsSettings.TodosListsSettings;
import util.Utility;
import watch.DigitalWatch;

/**
 *
 * @author Nacer Salah Eddine
 */
public class TodoApp {

    public static JFrame mainFrame;
    public static JPanel listPane = new JPanel();

    public JComboBox<TodosList> todoListsCombo;
    public TodoStatusModel[] todosStatusList = {new TodoStatusModel(4), new TodoStatusModel(DONE_STATE), new TodoStatusModel(DOING_STATE),
        new TodoStatusModel(PENDING_STATE), new TodoStatusModel(PENDING_DOING_STATE)};

    public JMenuBar mb;
    public JPanel topPanel = new JPanel();
    public JPanel bottomPanel;
    public static JScrollPane labelsScroll;
    public JLabel enterTodoLabel;
    public JTextField todoTextField;
    public ButtonCreator addTodoBtn, resetBtn;
    public JComboBox todosStatus;
    public JLabel label;
    public JTextField todoListTf;
    /*
    public ButtonCreator deleteListItemBtn;
    public ButtonCreator addNewTodoListBtn;
    public ButtonCreator renameListBtn;
     */
    public ButtonCreator showHidePanelItemBtn;
    public ButtonCreator listSettingsItemBtn;

    public Image appIcon;
    public TrayIcon trayIcon;
    public SystemTray tray;
    public static DaoInterface<TodosList> todosListsDaoImpl;
    public static DaoInterface<Todo> todoDaoImpl;

    public static Vector<TodosList> todosListsNames = new Vector<>();
    public static Map<Long, ArrayList<TodoPanelComponent>> mainTodosContainer = new HashMap<>();
    public static TodosList currentSelectedTodosList = null;
    public static TodoStatusModel currentSelectedTodoStatus = new TodoStatusModel(TodoStatusModel.ALL_STATE);
    public static final Color MAIN_PNL_BG = Color.WHITE;//new Color(241,243,245);

    private static JComboBox<String> sortTodosByCombo;
    private final static String[] sortOptions = {"Sort By", "Recent", "Old", "Status", "ID"};
    private static final Logger LOG = Utility.getLogger(TodoApp.class.getName());
    public static final String NEW_LINE = System.getProperty("line.separator");
    public static final String[] exportTypes = {"JSON", "TXT"};
    public static Pattern pattern;
    public static Matcher matcher;
    public static FileNameExtensionFilter textFilesFilterResctrict = new FileNameExtensionFilter("Only .txt files", "txt");
    public static FileNameExtensionFilter jsonFilesFilterResctrict = new FileNameExtensionFilter("Only .json files", "json");
    private static Task readFileTodosTask;

    public TodoApp() {
        /* It posts an event (Runnable)at the end of Swings event list and is
		processed after all other GUI events are processed.*/
        EventQueue.invokeLater(new Runnable() {
            JPanel jp2;

            @Override
            public void run() {
                //try - catch block
                try {
                    // mainFont = Utility.loadFont(Constants.MAIN_FONT_RES);
                    initMainFrame();
                    initMenuBar();
                    initTopPanel();
                    initBottomPanel();

                    if (mainFont != null) {
                        UIManager.put("OptionPane.messageFont", mainFont);
                        UIManager.put("OptionPane.buttonFont", mainFont);

                    }

                    initSystemTray();

                    //Adding Components to the frame.
                    mainFrame.getContentPane().add(BorderLayout.SOUTH, bottomPanel);
                    mainFrame.getContentPane().add(BorderLayout.NORTH, topPanel);

                    labelsScroll = new JScrollPane(listPane);

                    listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));

                    labelsScroll.setBackground(MAIN_PNL_BG);
                    listPane.setBackground(MAIN_PNL_BG);
                    labelsScroll.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.LIGHT_GRAY));
                    //labelsScroll.setHorizontalScrollBarPolicy(labelsScroll.HORIZONTAL_SCROLLBAR_NEVER);

                    mainFrame.getContentPane().add(labelsScroll, BorderLayout.CENTER);
                    mainFrame.setVisible(true);
                    mainFrame.setIconImage(appIcon);

                    mainFrame.addWindowStateListener((WindowEvent e) -> {
                        /*
                        if (e.getNewState() == Frame.ICONIFIED) {
                            try {
                                tray.add(trayIcon);
                                mainFrame.setVisible(false);
                                System.out.println("added to SystemTray");
                            } catch (AWTException ex) {
                                System.out.println("unable to add to tray");

                                  String msg = "Msg:" + ex.getMessage() +" /-/"+ ex.getCause();
                                 LOG.log(Level.SEVERE, msg);
                            }
                        }*/

                        if (e.getNewState() == Frame.MAXIMIZED_BOTH) {
                            tray.remove(trayIcon);
                            mainFrame.setVisible(true);
                            //System.out.println("Tray icon removed");
                        }
                        if (e.getNewState() == Frame.NORMAL) {
                            tray.remove(trayIcon);
                            mainFrame.setVisible(true);
                            // System.out.println("Tray icon removed");
                        }
                    });

                    mainFrame.addWindowListener(new WindowAdapter() {

                        @Override
                        public void windowClosing(WindowEvent windowEvent) {
                            mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

                            try {
                                tray.add(trayIcon);
                                mainFrame.setVisible(false);
                                mainFrame.setExtendedState(JFrame.ICONIFIED);
                                //System.out.println("added to SystemTray");
                            } catch (AWTException ex) {
                                //System.out.println("unable to add to tray");

                                String msg = "Msg:" + ex.getMessage() + " /-/" + ex.getCause();
                                LOG.log(Level.SEVERE, msg);
                            }

                        }

                        @Override
                        public void windowClosed(WindowEvent e) {
                            //System.out.println("windowClosed detected");
                        }

                    });

                } catch (Exception e) {

                    String msg = "Msg:" + e.getMessage() + " /-/" + e.getCause();
                    LOG.log(Level.SEVERE, msg);
                }
            }

            private void initMainFrame() {

                //Set the app theme as the system.
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                        | UnsupportedLookAndFeelException ex) {

                    String msg = "Msg:" + ex.getMessage() + " /-/" + ex.getCause();
                    LOG.log(Level.SEVERE, msg);
                }
                appIcon = Toolkit.getDefaultToolkit().getImage(getClass().getResource(Resources.APP_ICON));
                mainFrame = new JFrame();
                //Set frame title
                mainFrame.setTitle(Constants.APP_MAIN_TITLE);
                //set default close operation
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
                mainFrame.setMinimumSize(new Dimension(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT));
                mainFrame.setLocationRelativeTo(null);
            }

            private void initMenuBar() {
                mb = new JMenuBar();
                JMenu fileJmenu = new JMenu("File");
                JMenu editJmenu = new JMenu("Edit");
                JMenu m3 = new JMenu("Help");
                addEditMenueItems(editJmenu);

                mb.add(fileJmenu);
                mb.add(editJmenu);
                mb.add(Box.createHorizontalGlue());
                mb.add(m3);

                JMenuItem m11 = new JMenuItem("Open new sqlite database");
                JMenuItem m22 = new JMenuItem("Save current database as");
                JMenuItem exportMenuItem = new JMenuItem("Export Current Todos list as json");

                JMenuItem exit = new JMenuItem("Exit");

                JMenuItem aboutMenuItem = new JMenuItem("About");
                JMenuItem helpMenuItem = new JMenuItem("Help Content");
                aboutMenuItem.setIcon(Utility.scaleImage(new ImageIcon(getClass().getResource(Resources.ABOUT_BUTTON_ICON_RES)), Constants.SMALL_RES_ICONS_WIDTH, Constants.SMALL_RES_ICONS_HEIGHT));
                helpMenuItem.setIcon(Utility.scaleImage(new ImageIcon(getClass().getResource(Resources.HELP_BUTTON_ICON_RES)), Constants.SMALL_RES_ICONS_WIDTH, Constants.SMALL_RES_ICONS_HEIGHT));

                exit.setIcon(Utility.scaleImage(new ImageIcon(getClass().getResource(Resources.EXIT_BUTTON_ICON_RES)), Constants.SMALL_RES_ICONS_WIDTH, Constants.SMALL_RES_ICONS_HEIGHT));

                m3.add(aboutMenuItem);
                m3.add(helpMenuItem);

                exit.setMnemonic(KeyEvent.VK_X);
                exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
                        InputEvent.CTRL_DOWN_MASK));
                aboutMenuItem.addActionListener((e) -> {
                    new About(mainFrame);

                });
                helpMenuItem.addActionListener((e) -> {
                    new Help(mainFrame);

                });
                exit.addActionListener((e) -> {
                    if (JOptionPane.showConfirmDialog(mainFrame,
                            "Are you sure you want to close this application?", "Close App?",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                        exitApplication();

                    }

                });

                //fileJmenu.add(m11);
                //fileJmenu.add(m22);
                //fileJmenu.add(exportMenuItem);
                //fileJmenu.add(new JSeparator());
                fileJmenu.add(exit);
            }

            private void initTopPanel() {
                topPanel.setLayout(new BorderLayout());
                topPanel.add(mb, BorderLayout.NORTH);

                JPanel panelContainer = new JPanel();
                panelContainer.setLayout(new BoxLayout(panelContainer, BoxLayout.Y_AXIS));
                JPanel listAndSettingsPanel = createTopPanelContents();
                jp2 = createTopPanelContents1();

                panelContainer.add(listAndSettingsPanel);
                panelContainer.add(new JSeparator(SwingConstants.HORIZONTAL));
                panelContainer.add(jp2);
                listAndSettingsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
                jp2.setBorder(new EmptyBorder(10, 10, 10, 10));
                topPanel.add(panelContainer, BorderLayout.SOUTH);
                // topPanel.add(jp2, BorderLayout.NORTH);

                //setting font
                if (mainFont != null) {
                    Utility.setFont(listAndSettingsPanel, mainFont);
                    Utility.setFont(jp2, mainFont.deriveFont(12f));
                }

            }

            private void initBottomPanel() {
                //Creating the panel at bottom and adding components
                bottomPanel = new JPanel();

                bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
                bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
                //bottomPanel.add(Box.createHorizontalStrut(20));

                enterTodoLabel = new JLabel(Constants.ENTER_TODO_LBL_MSG);

                //String addTodoPromptText = "Input todo here ";
                todoTextField = new JTextField( Constants.TODO_TEXTFIELD_MAX_CHARS); // accepts upto n characters

                //addPromptFocusEvent(todoTextField, addTodoPromptText);
                todoTextField.requestFocus();//try to call it in invoke later and when clicking button add too

                addTodoBtn = new ButtonCreator(Constants.ENTER_TODO_BUTTON_MSG,
                        Constants.ENTER_TODO_BUTTON_TOOLTIP_MSG, Resources.ADD_TODO_BUTTON_ICON_RES, Cursor.HAND_CURSOR
                );

                resetBtn = new ButtonCreator(Constants.RESET_BUTTON_MSG,
                        Constants.RESET_BUTTON_TOOLTIP_MSG, Resources.RESET_BUTTON_ICON_RES, Cursor.HAND_CURSOR
                );

                bottomPanel.add(enterTodoLabel); // Components Added using Flow Layout
                bottomPanel.add(Box.createRigidArea(new Dimension(5, 0)));
                bottomPanel.add(todoTextField);

                bottomPanel.add(Box.createRigidArea(new Dimension(5, 0)));
                bottomPanel.add(new JSeparator(SwingConstants.VERTICAL));
                bottomPanel.add(Box.createRigidArea(new Dimension(5, 0)));
                bottomPanel.add(addTodoBtn);

                bottomPanel.add(Box.createRigidArea(new Dimension(5, 0)));
                // bottomPanel.add(resetBtn);

                if (mainFont != null) {
                    Utility.setFont(bottomPanel, mainFont);
                }
                //Adding Event Listeners
                todoTextField.addKeyListener(new KeyAdapter() {

                    @Override
                    public void keyPressed(KeyEvent key) {

                        if (key.getKeyChar() == KeyEvent.VK_ENTER && todoTextField.getText().trim().length() > 0) {
                            addTodoBtn.doClick();

                        }

                    }

                });

                addTodoBtn.addActionListener((e) -> {
                    String todoText = todoTextField.getText().trim();
                    //System.out.println(currentSelectedTodosList);
                    if (todoText.length() > 0 && currentSelectedTodosList != null && mainTodosContainer.get(currentSelectedTodosList.getListId()) != null) {

                        Todo todo = new Todo(todoText, -1, currentSelectedTodosList.getListId(), TodoStatusModel.PENDING_STATE);
                        todoDaoImpl.create(todo);

                        TodoPanelComponent todoPanelItem = new TodoPanelComponent(todo);

                        if (mainFont != null) {

                            Utility.setFont(todoPanelItem, mainFont);
                        }
                        mainTodosContainer.get(currentSelectedTodosList.getListId()).add(todoPanelItem);

                        listPane.add(todoPanelItem);

                        todoTextField.setText(Constants.RESET_TEXT);

                        updateTodosPanelByStatus(true, true);

                    } else if (currentSelectedTodosList == null || mainTodosContainer.get(currentSelectedTodosList.getListId()) == null) {
                        JOptionPane.setRootFrame(mainFrame);
                        JOptionPane.showMessageDialog(mainFrame,
                                "Create a new list",
                                "No list error",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        todoTextField.setText(Constants.RESET_TEXT);
                    }

                });

            }

            private JPanel createTopPanelContents() {
                JPanel panel = new JPanel();

                BoxLayout box = new BoxLayout(panel, BoxLayout.LINE_AXIS);
                panel.setLayout(box);

                todoListsCombo = new JComboBox(todosListsNames);

                todoListsCombo.setCursor(new Cursor(Cursor.HAND_CURSOR));
                int todosListsCount = todosListsNames.size();
                if (todosListsCount > 0) {
                    todoListsCombo.setSelectedIndex(todosListsCount - 1);
                }
                todosStatus = new JComboBox(todosStatusList);
                todosStatus.setPrototypeDisplayValue(new TodoStatusModel(5));
                todosStatus.setCursor(new Cursor(Cursor.HAND_CURSOR));

                label = new JLabel(Constants.ENTER_NEW_TODO_LIST_LBL_MSG);

                todoListTf = new JTextField(Constants.TODO_LIST_TEXTFIELD_MAX_CHARS); // accepts upto n characters

                listSettingsItemBtn = new ButtonCreator("",
                        Constants.LISTS_SETTINGS_BUTTON_TOOLTIP_MSG, null, Cursor.HAND_CURSOR
                );
                listSettingsItemBtn.setIcon(Utility.scaleImage(new ImageIcon(getClass().getResource(Resources.SETTINGS_LIST_BUTTON_ICON_RES)), Constants.SMALL_RES_ICONS_WIDTH, Constants.SMALL_RES_ICONS_HEIGHT));

                showHidePanelItemBtn = new ButtonCreator(Constants.SHOW_HIDE_PANEL_BUTTON_MSG,
                        Constants.SHOW_HIDE_PANEL_BUTTON_TOOLTIP_MSG, null, Cursor.HAND_CURSOR
                );
                showHidePanelItemBtn.setIcon(Utility.scaleImage(new ImageIcon(getClass().getResource(Resources.SHOW_HIDE_LIST_BUTTON_ICON_RES)), Constants.SMALL_RES_ICONS_WIDTH, Constants.SMALL_RES_ICONS_HEIGHT));

                JSeparator jsep = new JSeparator(SwingConstants.VERTICAL);
                JSeparator jsep1 = new JSeparator(SwingConstants.VERTICAL);
                JSeparator jsep2 = new JSeparator(SwingConstants.VERTICAL);
                JSeparator jsep3 = new JSeparator(SwingConstants.VERTICAL);

                jsep.setMaximumSize(listSettingsItemBtn.getMinimumSize());
                jsep1.setMaximumSize(listSettingsItemBtn.getMinimumSize());
                jsep2.setMaximumSize(listSettingsItemBtn.getMinimumSize());
                jsep3.setMaximumSize(listSettingsItemBtn.getMinimumSize());

                /*
                deleteListItemBtn = new ButtonCreator(Constants.DELETE_LIST_BUTTON_MSG,
                        Constants.DELETE_LIST_BUTTON_TOOLTIP_MSG, null, Cursor.HAND_CURSOR
                );
                deleteListItemBtn.setIcon(Utility.scaleImage(new ImageIcon(getClass().getResource(Resources.DELETE_LIST_BUTTON_ICON_RES)), Constants.SMALL_RES_ICONS_WIDTH, Constants.SMALL_RES_ICONS_HEIGHT));

                addNewTodoListBtn = new ButtonCreator(Constants.ADD_LIST_BUTTON_MSG,
                        Constants.ADD_LIST_BUTTON_TOOLTIP_MSG, null, Cursor.HAND_CURSOR
                );
                addNewTodoListBtn.setIcon(Utility.scaleImage(new ImageIcon(getClass().getResource(Resources.ADD_TODOS_LIST_BUTTON_ICON_RES)), Constants.SMALL_RES_ICONS_WIDTH, Constants.SMALL_RES_ICONS_HEIGHT));

                renameListBtn = new ButtonCreator(Constants.RENAME_LIST_BUTTON_MSG,
                        Constants.RENAME_LIST_BUTTON_TOOLTIP_MSG, null, Cursor.HAND_CURSOR
                );
                
                renameListBtn.setIcon(Utility.scaleImage(new ImageIcon(getClass().getResource(Resources.RENAME_LIST_BUTTON_ICON_RES)), Constants.SMALL_RES_ICONS_WIDTH, Constants.SMALL_RES_ICONS_HEIGHT));
                
                 */
                todoListsCombo.setPrototypeDisplayValue(new TodosList("XXXXXXXXXXXXXXXXXX"));
                todoListsCombo.setSize(DOING_STATE, DOING_STATE);

                todoListsCombo.setMaximumSize(listSettingsItemBtn.getMinimumSize());
                todosStatus.setMaximumSize(listSettingsItemBtn.getMinimumSize());

                panel.add(Box.createRigidArea(new Dimension(5, 0)));
                panel.add(label);
                panel.add(Box.createRigidArea(new Dimension(5, 0)));
                panel.add(todoListTf);
                panel.add(Box.createRigidArea(new Dimension(5, 0)));
                panel.add(jsep);
                panel.add(Box.createRigidArea(new Dimension(5, 0)));

                panel.add(todoListsCombo);
                panel.add(Box.createRigidArea(new Dimension(5, 0)));

                panel.add(listSettingsItemBtn);

                panel.add(Box.createRigidArea(new Dimension(5, 0)));
                //panel.add(reset);

                panel.add(jsep1);
                panel.add(Box.createRigidArea(new Dimension(5, 0)));
                panel.add(todosStatus);
                panel.add(Box.createRigidArea(new Dimension(5, 0)));

                panel.add(jsep2);
                panel.add(Box.createRigidArea(new Dimension(5, 0)));
                panel.add(showHidePanelItemBtn);
                panel.add(Box.createRigidArea(new Dimension(5, 0)));

                panel.add(jsep3);

                panel.add(Box.createRigidArea(new Dimension(10, 0)));

                JPanel clockPanel = new JPanel();
                Dimension d = new Dimension(new Dimension(200, 50));
                clockPanel.setPreferredSize(d);
                clockPanel.setMinimumSize(d);
                clockPanel.setMaximumSize(d);

                clockPanel.setBackground(new Color(200, 200, 200));

                clockPanel.add(new DigitalWatch());
                panel.add(clockPanel);

                listSettingsItemBtn.addActionListener((e) -> {
                    new TodosListsSettings(mainFrame, todoListsCombo);
                });

                showHidePanelItemBtn.addActionListener((e) -> {
                    // SwingUtilities.updateComponentTreeUI(listPane);
                    if (labelsScroll != null) {
                        SwingUtilities.invokeLater(() -> {
                            JScrollBar bar = labelsScroll.getVerticalScrollBar();
                            bar.setValue(bar.getMinimum());
                        });
                    }
                    if (jp2.isVisible()) {
                        jp2.setVisible(false);
                    } else {
                        jp2.setVisible(true);
                    }

                    //new TodosListsSettings(mainFrame);
                });

                /*
                renameListBtn.addActionListener((e) -> {

                    if (todosListsNames.size() > 0) {
                        String input = JOptionPane.showInputDialog(mainFrame, "Rename list: \"" + currentSelectedTodosList + "\"", "Input new list name:",
                                JOptionPane.WARNING_MESSAGE);
                        System.out.println(input);

                        if (input != null && !input.trim().isEmpty()) {
                            TodosList todoList = new TodosList(input);
                            todoList.setListId(currentSelectedTodosList.getListId());
                            todoList.setCreationDatetime(currentSelectedTodosList.getCreationDatetime());
                            todoList.setCreationTimestamp(currentSelectedTodosList.getCreationTimestamp());

                            Object obj = mainTodosContainer.get(currentSelectedTodosList.getListId());
                            mainTodosContainer.put(todoList.getListId(), (ArrayList) obj);
                            currentSelectedTodosList = todoList;

                            todosListsDaoImpl.update(todoList.getListId(), new Object[]{TodoUpdateTypes.LIST_CHANGE_NAME_UPDATE, todoList});
                            if (todoList.getListId() != -1) {
                                System.err.println("renamed succussfully");
                                System.err.println("new name is:" + todoList.getListName());
                            }
                            int si = todoListsCombo.getSelectedIndex();
                            todosListsNames.set(si, todoList);
                            SwingUtilities.updateComponentTreeUI(todoListsCombo);
                            todoListsCombo.setSelectedIndex(si);

                        }
                    }

                });
                
                deleteListItemBtn.addActionListener((e) -> {

                    int numItems = todosListsNames.size();
                    if (numItems > 0) {

                        TodosList s = (TodosList) todoListsCombo.getSelectedItem();
                        if (JOptionPane.showConfirmDialog(mainFrame,
                                "Delete List \"" + s + "\"? Which has " + mainTodosContainer.get(currentSelectedTodosList.getListId()).size() + " Item(s)", "Delete List?",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION) {

                        } else {

                            todosListsDaoImpl.softDelete(s.getListId());

                            todosListsNames.remove(s);

                            mainTodosContainer.remove(s.getListId());

                            if (todosListsNames.size() > 0) {
                                todoListsCombo.setSelectedIndex(0);
                                currentSelectedTodosList = todosListsNames.get(0);
                            } else {
                                //  todoListsCombo.actionPerformed(new ActionEvent("", 0, s));

                                currentSelectedTodosList = null;
                                listPane.removeAll();
                                SwingUtilities.updateComponentTreeUI(listPane);
                            }
                            SwingUtilities.updateComponentTreeUI(todoListsCombo);
                        }
                    } else {
                        JOptionPane.showMessageDialog(mainFrame,
                                "Can't remove more items",
                                "Empty error",
                                JOptionPane.ERROR_MESSAGE);

                    }
                });
                addNewTodoListBtn.addActionListener((e) -> {
                    String value = todoListTf.getText().trim();
                    TodosList todosLst = new TodosList(value);
                    if (value.length() > 0 && !todosListsNames.contains(todosLst)) {

                        //add to database 
                        todosListsDaoImpl.create(todosLst);
                        if (todosLst.getListId() != -1) {
                            System.out.println("Inserted successfully");
                        }

                        todosListsNames.add(todosLst);
                        todoListTf.setText(Constants.RESET_TEXT);
                        mainTodosContainer.put(todosLst.getListId(), new ArrayList<>());
                        SwingUtilities.updateComponentTreeUI(todoListsCombo);
                        todoListsCombo.setSelectedIndex(todosListsNames.size() - 1);

                    } else if (todosListsNames.contains(todosLst)) {
                        JOptionPane.showMessageDialog(mainFrame,
                                "List already exists ",
                                "Can't add new list",
                                JOptionPane.WARNING_MESSAGE);

                    }

                });
                 */
                todosStatus.addActionListener((e) -> {
                    if (todosListsNames.size() > 0) {
                        //System.out.println("---------");
                        JComboBox cb = (JComboBox) e.getSource();
                        TodoStatusModel seletectedStatus = (TodoStatusModel) cb.getSelectedItem();

                        //add to database
                        currentSelectedTodoStatus = seletectedStatus;
                        if (mainTodosContainer.get(currentSelectedTodosList.getListId()) != null) {
                            updateTodosPanelByStatus(true, true);
                        }
                    } else {

                        /*
                        JOptionPane.showMessageDialog(mainFrame,
                                "Create a new list first.",
                                "No list error",
                                JOptionPane.INFORMATION_MESSAGE);
                         */
                        // todosStatus.setSelectedIndex(0);
                    }

                });
                todoListsCombo.addActionListener((e) -> {
                    JComboBox cb = (JComboBox) e.getSource();
                    TodosList listObj = (TodosList) cb.getSelectedItem();

                    if (!listObj.equals(currentSelectedTodosList)) {
                        currentSelectedTodosList = listObj;//new TodosList(listObj.getListName(),1);
                        updateTodosPanelByStatus(true, true);
                        //System.out.println(listObj);
                        //System.out.println(mainTodosContainer.get(listObj.getListId()));

                    } else {
                        //System.out.println("no updating list");
                    }

                });
                todoListTf.addKeyListener(new KeyAdapter() {

                    @Override
                    public void keyPressed(KeyEvent key) {
                        /*
                        System.out.println(key.getKeyChar() == KeyEvent.VK_ENTER);
                        System.out.println("----");

                        System.out.println(todoListTf.getText().trim().length() > 0);
                        System.out.println(todoListTf.getText().trim().length());
                        System.out.println("----");
                         */
                        if (key.getKeyChar() == KeyEvent.VK_ENTER && todoListTf.getText().trim().length() > 0) {
                            // addNewTodoListBtn.doClick();
                            String value = todoListTf.getText().trim();
                            TodosList todosLst = new TodosList(value);
                            //System.out.println("adding new list");
                            if (value.length() > 0 && !todosListsNames.contains(todosLst)) {

                                //add to database 
                                todosListsDaoImpl.create(todosLst);
                                if (todosLst.getListId() != -1) {
                                    //System.out.println("Inserted successfully");
                                }

                                todosListsNames.add(todosLst);
                                todoListTf.setText(Constants.RESET_TEXT);
                                mainTodosContainer.put(todosLst.getListId(), new ArrayList<>());
                                SwingUtilities.updateComponentTreeUI(todoListsCombo);
                                todoListsCombo.setSelectedIndex(todosListsNames.size() - 1);

                            } else if (todosListsNames.contains(todosLst)) {
                                JOptionPane.showMessageDialog(mainFrame,
                                        "List already exists ",
                                        "Can't add new list",
                                        JOptionPane.WARNING_MESSAGE);

                            }
                        }

                    }

                });
                return panel;
            }

            private JPanel createTopPanelContents1() {
                JPanel panel = new JPanel();

                BoxLayout box = new BoxLayout(panel, BoxLayout.LINE_AXIS);
                panel.setLayout(box);

                sortTodosByCombo = new JComboBox(sortOptions);

                sortTodosByCombo.setCursor(new Cursor(Cursor.HAND_CURSOR));

                JLabel label = new JLabel(Constants.FIND_TODO_LBL_MSG);

                JTextField searchTodosListTf = new JTextField( Constants.TODO_LIST_TEXTFIELD_MAX_CHARS); // accepts upto n characters

                ButtonCreator searchTodosItemBtn = new ButtonCreator(Constants.SEARCH_TODOS_BUTTON_MSG,
                        Constants.SEARCH_TODOS_BUTTON_TOOLTIP_MSG, null, Cursor.HAND_CURSOR
                );
                searchTodosItemBtn.setIcon(Utility.scaleImage(new ImageIcon(getClass().getResource(Resources.SEARCH_TODOS_BUTTON_ICON_RES)), Constants.SMALL_RES_ICONS_WIDTH, Constants.SMALL_RES_ICONS_HEIGHT));

                panel.add(Box.createRigidArea(new Dimension(5, 0)));
                panel.add(label);
                panel.add(Box.createRigidArea(new Dimension(5, 0)));
                panel.add(searchTodosListTf);

                //panel.add(new JSeparator(SwingConstants.VERTICAL));
                //panel.add(new DigitalWatch());
                panel.add(Box.createRigidArea(new Dimension(5, 0)));

                panel.add(Box.createRigidArea(new Dimension(5, 0)));
                panel.add(new JSeparator(SwingConstants.VERTICAL));
                panel.add(Box.createRigidArea(new Dimension(5, 0)));
                panel.add(searchTodosItemBtn);

                panel.add(Box.createRigidArea(new Dimension(5, 0)));

                panel.add(new JSeparator(SwingConstants.VERTICAL));
                panel.add(Box.createRigidArea(new Dimension(5, 0)));
                panel.add(sortTodosByCombo);
                panel.add(Box.createRigidArea(new Dimension(5, 0)));
                searchTodosItemBtn.addActionListener((e) -> {
                    String searchStr = searchTodosListTf.getText().trim();
                    if (!searchStr.isEmpty()) {

                        findTodo(searchStr);

                    } else {

                        updateTodosPanelByStatus(false, true);
                    }
                });

                sortTodosByCombo.addActionListener((e) -> {

                    JComboBox cb = (JComboBox) e.getSource();
                    String selectedItem = (String) cb.getSelectedItem();
                    ArrayList<TodoPanelComponent> todosPanelList = mainTodosContainer.get(currentSelectedTodosList.getListId());

                    if (sortOptions[0].equals(selectedItem)) {
                        //Sort By

                        //System.out.println("sorting by nothing");
                        return;
                    } else if (sortOptions[1].equals(selectedItem)) {
                        //Recent
                        //System.out.println("sorting by recent");
                        Collections.sort(todosPanelList, (t1, t2) -> {
                            /*long a = t1.todoContent.getTodoId();
                        long b = t2.todoContent.getTodoId();*/
                            long a = t1.todoContent.getCreationTimestamp();
                            long b = t2.todoContent.getCreationTimestamp();
                            /*
                            return a < b ? -1
                                    : a > b ? 1
                                            : 0;
                             */
                            return (int) (b - a);
                        });

                    } else if (sortOptions[2].equals(selectedItem)) {
                        //Old
                        //System.out.println("sorting by old");
                        Collections.sort(todosPanelList, (t1, t2) -> {
                            long a = t1.todoContent.getCreationTimestamp();
                            long b = t2.todoContent.getCreationTimestamp();

                            return (int) (a - b);
                        });
                    } else if (sortOptions[3].equals(selectedItem)) {
                        //Group
                        //System.out.println("sorting by status group");
                        Collections.sort(todosPanelList, (t1, t2) -> {
                            long a = t1.todoContent.getTodoStatus();
                            long b = t2.todoContent.getTodoStatus();

                            return (int) (a - b);
                        });
                    } else if (sortOptions[4].equals(selectedItem)) {
                        //id
                        //System.out.println("sorting by id");
                        Collections.sort(todosPanelList, (t1, t2) -> {
                            long a = t1.todoContent.getTodoId();
                            long b = t2.todoContent.getTodoId();

                            return (int) (a - b);

                        });
                    }
                    /*
                    for (TodoPanelComponent todoPanelComponent : todosPanelList) {
                        System.out.println("------------------------");
                        System.out.println("ID:" + todoPanelComponent.todoContent.getTodoId());
                        System.out.println("CONTENT:" + todoPanelComponent.todoContent.getTodoText());
                        System.out.println("Status:" + todoPanelComponent.todoContent.getTodoStatus());

                        System.out.println("------------------------");

                    }*/
                    updateTodosPanelByStatus(false, false);
                    //Scroll up
                    if (labelsScroll != null) {
                        SwingUtilities.invokeLater(() -> {
                            JScrollBar bar = labelsScroll.getVerticalScrollBar();
                            bar.setValue(bar.getMinimum());
                        });
                    }
                });

                searchTodosListTf.addKeyListener(new KeyAdapter() {

                    @Override
                    public void keyPressed(KeyEvent key) {

                        if (key.getKeyChar() == KeyEvent.VK_ENTER) {
                            searchTodosItemBtn.doClick();
                        }

                    }

                });
                return panel;
            }

            private void initSystemTray() {

                if (SystemTray.isSupported()) {
                    // System.out.println("system tray supported");
                    tray = SystemTray.getSystemTray();
                    ActionListener exitListener = (ActionEvent e) -> {

                        if (JOptionPane.showConfirmDialog(mainFrame,
                                "Are you sure you want to close this application?", "Close App?",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                            exitApplication();

                        }
                    };
                    PopupMenu popup = new PopupMenu();
                    MenuItem defaultItem = new MenuItem("Exit");
                    defaultItem.addActionListener(exitListener);
                    popup.add(defaultItem);
                    defaultItem = new MenuItem("Open");
                    defaultItem.addActionListener((ActionEvent e) -> {
                        mainFrame.setVisible(true);
                        mainFrame.setExtendedState(JFrame.NORMAL);
                        //tray.remove(trayIcon);
                    });

                    popup.add(defaultItem);
                    trayIcon = new TrayIcon(appIcon, Constants.APP_MAIN_TITLE, popup);
                    trayIcon.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (e.getClickCount() == 2) {
                                // your valueChanged overridden method 
                                mainFrame.setVisible(true);
                                mainFrame.setExtendedState(JFrame.NORMAL);
                                //tray.remove(trayIcon);
                            }
                        }

                    });
                    trayIcon.setImageAutoSize(true);
                } else {
                    //System.out.println("system tray not supported");
                }
            }

            public void exitApplication() {
                if (todoDaoImpl != null) {
                    todoDaoImpl.close();
                }

                if (todosListsDaoImpl != null) {
                    todosListsDaoImpl.close();
                }
                System.exit(0);
            }

            private void addPromptFocusEvent(JTextField todoTextField, String addTodoPromptText) {
                todoTextField.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        if (todoTextField.getText().equals(addTodoPromptText)) {
                            todoTextField.setText("");
                        }
                    }

                    @Override
                    public void focusLost(FocusEvent e) {

                        if (todoTextField.getText().isEmpty()) {
                            todoTextField.setText(addTodoPromptText);
                            
                        }
                    }
                });
            }

        }
        );

    }

    private static void deleteTodos(int statusCond, String msg) {
        int size = mainTodosContainer.get(currentSelectedTodosList.getListId()).size();
        Todo j;
        int todoComponentLoc = 4;
        Predicate<JPanel> condition;
        if (size > 0) {
            long doneSize = mainTodosContainer.get(currentSelectedTodosList.getListId()).stream().filter((pnl) -> ((Todo) pnl.getComponent(todoComponentLoc)).getTodoStatus() == statusCond).count();
            if (doneSize > 0) {
                if (JOptionPane.showConfirmDialog(mainFrame,
                        "Are you sure you want to delete all " + doneSize + " " + msg + " todos, from \"" + currentSelectedTodosList + "\" list?", "Delete todos?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

                    TodoApp.listPane.removeAll();

                    condition = (pnl) -> ((Todo) pnl.getComponent(todoComponentLoc)).getTodoStatus() == statusCond;

                    mainTodosContainer.get(currentSelectedTodosList.getListId()).removeIf(condition);

                    TodoApp.updateTodosPanelByStatus(true, true);

                }
            }
        }
    }

    public static void addEditMenueItems(Object o) {

        JMenuItem pasteTextFromClipboardAsTodoMenuItem = new JMenuItem("Paste Text from clipboard as a To-Do",
                Utility.scaleImage(new ImageIcon(TodoApp.class.getResource(Resources.PASTE_TODO_ITEM_ICON_RES)), 16, 16));

        JMenuItem copyAllTodosInListMenuItem = new JMenuItem("Copy to Clipboard all To-Dos in this list ...",
                Utility.scaleImage(new ImageIcon(TodoApp.class.getResource(Resources.COPY_TO_CLIP_ALL_TODOS_ITEM_ICON_RES)), 16, 16));
        JMenuItem exportAllTodosInListMenuItem = new JMenuItem("Export all To-Dos in this list as ...",
                Utility.scaleImage(new ImageIcon(TodoApp.class.getResource(Resources.EXPORT_ALL_TODOS_ITEM_ICON_RES)), 16, 16));
        JMenuItem importListOfTodosMenuItem = new JMenuItem("Import list of To-Dos from ...",
                Utility.scaleImage(new ImageIcon(TodoApp.class.getResource(Resources.IMPORT_ALL_TODOS_ITEM_ICON_RES)), 16, 16));

        JMenuItem deleteAllTodosMenuItem = new JMenuItem(Constants.DELETE_ALL_TODOS_MENUITEM_MSG,
                Utility.scaleImage(new ImageIcon(TodoApp.class.getResource(Resources.DELETE_ITEM_BUTTON_ICON_RES)), 16, 16));

        JMenuItem deleteAllDoneTodosMenuItem = new JMenuItem(Constants.DELETE_ALL_DONE_TODOS_MENUITEM_MSG,
                Utility.scaleImage(new ImageIcon(TodoApp.class.getResource(Resources.DELETE_ITEM_BUTTON_ICON_RES)), 16, 16));
        JMenuItem deleteAllPendingTodosMenuItem = new JMenuItem(Constants.DELETE_ALL_PENDING_TODOS_MENUITEM_MSG,
                Utility.scaleImage(new ImageIcon(TodoApp.class.getResource(Resources.DELETE_ITEM_BUTTON_ICON_RES)), 16, 16));
        JMenuItem deleteAllDoingTodosMenuItem = new JMenuItem(Constants.DELETE_ALL_DOING_TODOS_MENUITEM_MSG,
                Utility.scaleImage(new ImageIcon(TodoApp.class.getResource(Resources.DELETE_ITEM_BUTTON_ICON_RES)), 16, 16));

        /**/
        copyAllTodosInListMenuItem.addActionListener((e) -> {
            System.out.println("copyAllTodosInListMenuItem");

            if (currentSelectedTodosList != null && mainTodosContainer.get(currentSelectedTodosList.getListId()) != null
                    && mainTodosContainer.get(currentSelectedTodosList.getListId()).size() > 0) {
                StringBuilder stringBldrData = new StringBuilder();

                //check frist if list exists and there are items in it
                ArrayList<TodoPanelComponent> l = mainTodosContainer.get(currentSelectedTodosList.getListId());

                //stringBldrData.append("LIST NAME:").append(currentSelectedTodosList.getListName()).append(NEW_LINE);
                for (TodoPanelComponent td : l) {

                    stringBldrData.append(td.todoContent.getTodoText()).append(NEW_LINE);

                }

                //stringBldrData.append(NEW_LINE).append(NEW_LINE).append(NEW_LINE);
                Utility.copyToClipboardText(stringBldrData.toString());

            }

        });
        exportAllTodosInListMenuItem.addActionListener((e) -> {
            System.out.println("exportAllTodosInListMenuItem");
            System.out.println("exporting all list of todos");
            JDialog.setDefaultLookAndFeelDecorated(true);
            Object selection = JOptionPane.showInputDialog(TodoApp.mainFrame, "Choose the Export Type?",
                    "Export this Todo", JOptionPane.QUESTION_MESSAGE, null, exportTypes, exportTypes[1]);

            System.out.println(selection);
            if (selection != null && !selection.toString().trim().isEmpty() && currentSelectedTodosList != selection) {
                String selectedExportType = (String) selection;
                ArrayList<TodoPanelComponent> l;
                StringBuilder stringBldrData;
                String outputData;
                switch (selectedExportType) {

                    case "JSON":
                        JSONObject mainObj = new JSONObject();
                        l = mainTodosContainer.get(currentSelectedTodosList.getListId());

                        mainObj.put("list_name", currentSelectedTodosList.getListName());
                        mainObj.put("list_id", currentSelectedTodosList.getListId());
                        mainObj.put("list_datetime_created", currentSelectedTodosList.getCreationDatetime());
                        mainObj.put("list_creation_timestamp", currentSelectedTodosList.getCreationTimestamp());

                        JSONArray todosObj = new JSONArray();

                        for (TodoPanelComponent td : l) {
                            JSONObject tdObj = new JSONObject();
                            tdObj.put("id", td.todoContent.getTodoId());
                            tdObj.put("datetime_created", td.todoContent.getCreationDatetime());
                            tdObj.put("creation_timestamp", td.todoContent.getCreationTimestamp());
                            tdObj.put("status", TodoStatusModel.getStatusByCode(td.todoContent.getTodoStatus()));
                            tdObj.put("todo_content", td.todoContent.getTodoText());

                            todosObj.put(tdObj);

                        }
                        mainObj.put("todos", todosObj);

                        outputData = mainObj.toString();
                        break;

                    case "TXT"://TXT        
                        stringBldrData = new StringBuilder();
                        l = mainTodosContainer.get(currentSelectedTodosList.getListId());

                        stringBldrData.append("##########################").append(NEW_LINE);
                        stringBldrData.append("LIST ID:").append(currentSelectedTodosList.getListId()).append(NEW_LINE);
                        stringBldrData.append("LIST NAME:").append(currentSelectedTodosList.getListName()).append(NEW_LINE);
                        stringBldrData.append("LIST CREATION DATE-TIME:").append(currentSelectedTodosList.getCreationDatetime()).append(NEW_LINE);
                        stringBldrData.append("LIST CREATION TIMESTAMP:").append(currentSelectedTodosList.getCreationTimestamp()).append(NEW_LINE);
                        stringBldrData.append("##########################").append(NEW_LINE);
                        stringBldrData.append("##-ALL-TODOS##").append(NEW_LINE);

                        for (TodoPanelComponent td : l) {

                            stringBldrData.append("TODO ID:").append(td.todoContent.getTodoId()).append(NEW_LINE);
                            stringBldrData.append("TODO CONTENT:").append(td.todoContent.getTodoText()).append(NEW_LINE);
                            String statusStr = TodoStatusModel.getStatusByCode(td.todoContent.getTodoStatus());
                            stringBldrData.append("TODO STATUS:").append(statusStr).append(NEW_LINE);
                            stringBldrData.append("TODO CREATION DATE-TIME:").append(td.todoContent.getCreationDatetime()).append(NEW_LINE);
                            stringBldrData.append("TODO CREATION TIMESTAMP:").append(td.todoContent.getCreationTimestamp()).append(NEW_LINE);

                            stringBldrData.append("---------------------------------------------------------------").append(NEW_LINE);;
                        }

                        //stringBldrData.append(NEW_LINE).append(NEW_LINE).append(NEW_LINE);
                        outputData = stringBldrData.toString();
                        break;

                    default:
                        return;
                }

                System.out.println("-----");
                // create an object of JFileChooser class 
                JFileChooser jFileChooser = new JFileChooser(FileSystemView.getFileSystemView().
                        getFileSystemView().getHomeDirectory());

                // invoke the showsSaveDialog function to show the save dialog 
                int r = jFileChooser.showSaveDialog(null);

                // if the user selects a file 
                if (r == JFileChooser.APPROVE_OPTION) {
                    // set the label to the path of the selected file 
                    System.out.println(jFileChooser.getSelectedFile().getAbsolutePath());
                    Utility.writeFile(jFileChooser.getSelectedFile(), outputData, selectedExportType.toLowerCase());
                } // if the user cancelled the operation 
                else {
                    //System.out.println("the user cancelled the operation");
                }
            }
        });

        pasteTextFromClipboardAsTodoMenuItem.addActionListener((e) -> {

            String clipText = Utility.copyTextFromClipboard();
            addTodo(clipText);

        });

        importListOfTodosMenuItem.addActionListener((e) -> {

            JDialog.setDefaultLookAndFeelDecorated(true);
            Object selection = JOptionPane.showInputDialog(TodoApp.mainFrame, "Choose the Import Type?",
                    "Import Todo as..", JOptionPane.QUESTION_MESSAGE, null, TodoApp.exportTypes, exportTypes[1]);

            System.out.println(selection);
            if (selection != null && !selection.toString().trim().isEmpty() && currentSelectedTodosList != selection) {
                String selectedImportType = (String) selection;

                // create an object of JFileChooser class 
                JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getFileSystemView().getHomeDirectory());
                // only allow files of .txt extension 
                //j.getChoosableFileFilters() = 0;//make it empty to remove the all files *
                if (selectedImportType.equals(exportTypes[1])) {
                    j.addChoosableFileFilter(textFilesFilterResctrict);
                } else if (selectedImportType.equals(exportTypes[0])) {
                    j.addChoosableFileFilter(jsonFilesFilterResctrict);
                }
                // invoke the showsOpenDialog function to show the save dialog 
                int r = j.showOpenDialog(mainFrame);

                // if the user selects a file 
                if (r == JFileChooser.APPROVE_OPTION) {
                    // set the label to the path of the selected file 
                    System.out.println(j.getSelectedFile().getAbsolutePath());
                    try {
                        //check for certain that the extension of the file is really what was selected 
                        //j.getFileFilter().accept(new File("."+"xml"));
                        byte[] filesBytesData = Files.readAllBytes(Paths.get(j.getSelectedFile().getAbsolutePath()));
                        String fileStrData = new String(filesBytesData);
                        //if the extension is txt then do this
                        switch (selectedImportType) {

                            case "JSON":

                                readFileTodosTask = new Task(fileStrData, "JSON");
                                //task.addPropertyChangeListener(this);
                                readFileTodosTask.execute();
                                break;

                            case "TXT":

                                readFileTodosTask = new Task(fileStrData, "TXT");
                                //task.addPropertyChangeListener(this);
                                readFileTodosTask.execute();
                                break;

                            default:
                                return;
                        }

                        //if theextension is json then do this
                        //j.getSelectedFile().getParentFile().//
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(mainFrame,
                                "Error importing data... /" + ex.getMessage(),
                                "Import Error",
                                JOptionPane.ERROR_MESSAGE);
                    }

                } // if the user cancelled the operation 
                else {
                    System.out.println("the user cancelled the operation");
                }

            }
        });

        /**/
        deleteAllTodosMenuItem.addActionListener((e) -> {
            int size = mainTodosContainer.get(currentSelectedTodosList.getListId()).size();
            if (size > 0) {
                if (JOptionPane.showConfirmDialog(mainFrame,
                        "Are you sure you want to delete all " + size + " todos, from \"" + currentSelectedTodosList + "\" list?", "Delete all todos?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {

                    todoDaoImpl.softDeleteAll(currentSelectedTodosList.getListId());

                    TodoApp.listPane.removeAll();

                    mainTodosContainer.get(currentSelectedTodosList.getListId()).clear();

                    TodoApp.updateTodosPanelByStatus(true, true);

                }
            }
        });

        deleteAllDoneTodosMenuItem.addActionListener((e) -> {
            todoDaoImpl.softDeleteByStatus(currentSelectedTodosList.getListId(), TodoStatusModel.DONE_STATE);
            deleteTodos(TodoStatusModel.DONE_STATE, Constants.DONE_STATE_MSG);

        });

        deleteAllPendingTodosMenuItem.addActionListener((e) -> {
            todoDaoImpl.softDeleteByStatus(currentSelectedTodosList.getListId(), TodoStatusModel.PENDING_STATE);

            deleteTodos(TodoStatusModel.PENDING_STATE, Constants.PENDING_STATE_MSG);
        });
        deleteAllDoingTodosMenuItem.addActionListener((e) -> {
            todoDaoImpl.softDeleteByStatus(currentSelectedTodosList.getListId(), TodoStatusModel.DOING_STATE);

            deleteTodos(TodoStatusModel.DOING_STATE, Constants.DOING_STATE_MSG);
        });

        if (o instanceof JPopupMenu) {

            ((JPopupMenu) o).add(deleteAllTodosMenuItem);
            ((JPopupMenu) o).add(deleteAllDoneTodosMenuItem);
            ((JPopupMenu) o).add(deleteAllPendingTodosMenuItem);
            ((JPopupMenu) o).add(deleteAllDoingTodosMenuItem);

        } else {

            ((JMenu) o).add(pasteTextFromClipboardAsTodoMenuItem);
            ((JMenu) o).add(copyAllTodosInListMenuItem);
            ((JMenu) o).add(new JSeparator());

            ((JMenu) o).add(exportAllTodosInListMenuItem);
            ((JMenu) o).add(importListOfTodosMenuItem);

            ((JMenu) o).add(new JSeparator());

            ((JMenu) o).add(deleteAllTodosMenuItem);
            ((JMenu) o).add(deleteAllDoneTodosMenuItem);
            ((JMenu) o).add(deleteAllPendingTodosMenuItem);
            ((JMenu) o).add(deleteAllDoingTodosMenuItem);

        }

    }

    private static void findMatch(JPanel p, Todo todo) {
        matcher = pattern.matcher(todo.getTodoText());
        boolean matchFound = matcher.find();
        if (matchFound) {
            listPane.add(p);
        } else {
            System.out.println("Match not found in >>" + todo.getTodoText());
        }
    }

    public static void findTodo(String s) {
        int componentLoc = 4;
        pattern = Pattern.compile(s, Pattern.CASE_INSENSITIVE);
        //also if currentSelectedTodoStatus is clicked many times
        listPane.removeAll();
        System.out.println("searching..");
        mainTodosContainer.get(currentSelectedTodosList.getListId()).stream().filter((p) -> (p.getComponent(componentLoc) instanceof Todo)).forEachOrdered((p) -> {

            Todo todo = (Todo) p.getComponent(componentLoc);

            if (currentSelectedTodoStatus.getStatus() == TodoStatusModel.ALL_STATE) {

                findMatch(p, todo);

            } else if (currentSelectedTodoStatus.getStatus() == TodoStatusModel.PENDING_DOING_STATE) {

                if (((Todo) p.getComponent(componentLoc)).getTodoStatus() == TodoStatusModel.PENDING_STATE
                        || ((Todo) p.getComponent(componentLoc)).getTodoStatus() == TodoStatusModel.DOING_STATE) {

                    findMatch(p, todo);
                }

            } else if (((Todo) p.getComponent(componentLoc)).getTodoStatus() == currentSelectedTodoStatus.getStatus()) {
                findMatch(p, todo);

            }
        }
        );

        SwingUtilities.updateComponentTreeUI(listPane);
        //Scroll to the top
        if (labelsScroll != null) {
            SwingUtilities.invokeLater(() -> {
                JScrollBar bar = labelsScroll.getVerticalScrollBar();
                bar.setValue(bar.getMinimum());
            });
        }
    }

    private static void addTodo(String text) {
        if (!"".equals(text)) {

            if (currentSelectedTodosList != null && mainTodosContainer.get(currentSelectedTodosList.getListId()) != null) {

                Todo todo = new Todo(text, -1, currentSelectedTodosList.getListId(), TodoStatusModel.PENDING_STATE);
                todoDaoImpl.create(todo);

                TodoPanelComponent todoPanelItem = new TodoPanelComponent(todo);

                if (mainFont != null) {

                    Utility.setFont(todoPanelItem, mainFont);
                }
                mainTodosContainer.get(currentSelectedTodosList.getListId()).add(todoPanelItem);

                listPane.add(todoPanelItem);

                updateTodosPanelByStatus(true, true);

            } else if (currentSelectedTodosList == null || mainTodosContainer.get(currentSelectedTodosList.getListId()) == null) {
                JOptionPane.setRootFrame(mainFrame);
                JOptionPane.showMessageDialog(mainFrame,
                        "Create a new list",
                        "No list error",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        }
    }

    public static void updateTodosPanelByStatus(boolean doScroll, boolean resetSort) {

        int componentLoc = 4;
        //also if currentSelectedTodoStatus is clicked many times
        listPane.removeAll();
        System.out.println("updating..");
        mainTodosContainer.get(currentSelectedTodosList.getListId()).stream().filter((p) -> (p.getComponent(componentLoc) instanceof Todo)).forEachOrdered((p) -> {
            if (currentSelectedTodoStatus.getStatus() == TodoStatusModel.ALL_STATE) {
                listPane.add(p);
            } else if (currentSelectedTodoStatus.getStatus() == TodoStatusModel.PENDING_DOING_STATE) {

                if (((Todo) p.getComponent(componentLoc)).getTodoStatus() == TodoStatusModel.PENDING_STATE
                        || ((Todo) p.getComponent(componentLoc)).getTodoStatus() == TodoStatusModel.DOING_STATE) {

                    listPane.add(p);
                }

            } else if (((Todo) p.getComponent(componentLoc)).getTodoStatus() == currentSelectedTodoStatus.getStatus()) {
                listPane.add(p);

            }
        });
        if (resetSort && sortTodosByCombo != null) {
            String selectedItem = (String) sortTodosByCombo.getSelectedItem();
            System.out.println("trying to reset");
            if (!selectedItem.equals(sortOptions[0])) {
                System.out.println("resettings sort index............................................................");
                sortTodosByCombo.setSelectedIndex(0);
            }

        }
        SwingUtilities.updateComponentTreeUI(listPane);
        if (doScroll) {
            //Scroll to the bottom
            if (labelsScroll != null) {
                SwingUtilities.invokeLater(() -> {
                    JScrollBar bar = labelsScroll.getVerticalScrollBar();
                    bar.setValue(bar.getMaximum());
                });
            }
        }

    }

    private static class Task extends SwingWorker<Void, Void> {

        private final Logger LOG = Logger.getLogger(Task.class.getName());
        private String fileStrData;
        private String importType;

        Task(String data, String importType) {
            this.fileStrData = data;
            this.importType = importType;
        }

        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {

            try {

                switch (this.importType) {

                    case "JSON":
                        System.out.println("running task for json data");

                        JSONObject mainObj = new JSONObject(fileStrData);

                        System.out.println("parsing as json" + fileStrData);
                        readJsonDataFile(mainObj);
                        break;

                    case "TXT":
                        String lines[] = this.fileStrData.split("\\r?\\n");
                        for (String line : lines) {

                            addTodo(line);

                        }

                        break;

                }

            } catch (JSONException e) {

                System.err.println("unknown error , " + e);

            }

            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            //setCursor(null); //turn off the wait cursor

            //when done make progress bar hide after its shows it 
        }

        private void readJsonDataFile(JSONObject mainObj) {
            try {
                JSONObject todoObj;
                String todoStatus;
                String listName = mainObj.getString("list_name");
                long listId = mainObj.getLong("list_id");
                long creationTimeStamp = mainObj.getLong("list_creation_timestamp");
                String creationDateTime = mainObj.getString("list_datetime_created");
                System.out.println(">>>" + listName);
                if (mainObj.has("todo")) {
                    todoObj = mainObj.getJSONObject("todo");

                    todoObj.getLong("id");
                    todoObj.getString("datetime_created");
                    todoObj.getLong("creation_timestamp");
                    todoStatus = todoObj.getString("status");
                    todoObj.getString("todo_content");
                    System.out.println(">>>" + todoStatus);
                } else if (mainObj.has("todos")) {
                    JSONArray todosArry = mainObj.getJSONArray("todos");
                    for (Object todo : todosArry) {
                        ((JSONObject) todo).getLong("id");
                        ((JSONObject) todo).getString("datetime_created");
                        ((JSONObject) todo).getLong("creation_timestamp");
                        todoStatus = ((JSONObject) todo).getString("status");
                        ((JSONObject) todo).getString("todo_content");
                        System.out.println(">>>" + todoStatus);
                    }
                }

                //Here we will need to create a new list and add the read todos to it
            } catch (JSONException ex) {

                JOptionPane.showMessageDialog(mainFrame,
                        "Error importing data... /" + ex.getMessage(),
                        "Import Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

    }

}
