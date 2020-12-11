package todoListsSettings;

import beans.TodosList;
import consts.Constants;
import consts.Resources;
import consts.TodoUpdateTypes;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import factory.ButtonCreator;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import mainapp.TodoApp;
import static mainapp.TodoApp.currentSelectedTodosList;
import static mainapp.TodoApp.listPane;
import static mainapp.TodoApp.mainFrame;
import static mainapp.TodoApp.mainTodosContainer;
import static mainapp.TodoApp.todosListsDaoImpl;
import static mainapp.TodoApp.todosListsNames;
import util.Utility;

/**
 *
 * @author Nacer Salah Eddine
 */
public class TodosListsSettings extends JDialog {

    /**
     * Launch the application.
     */
    public Font mainFont;
    public static final int WINDOW_WIDTH = 500;
    public static final int WINDOW_HEIGHT = 270;

    public static final String SHAPE_LOGO_RES_LOC = "/res/settings.png";
    public static final String MAIN_LOGO_RES_LOC = "/about/res/Asset 3@2x.png";

    public static final String FONT_RES_LOC = "resources/fonts/Cairo-Regular.ttf";

    public static final String APP_TITLE = "List Settings";

    public static final String APP_INFO_BORDER_LBL = "List Settings";

    public static final String WINDOW_TITLE = "Settings";
    public static final Color MAIN_BG_COLOR = new Color(71, 53, 106);
    public static final Color MAIN_FG_COLOR = new Color(71, 53, 106);

    private static final Logger LOG = Utility.getLogger(TodosListsSettings.class.getName());
    public JComboBox<TodosList> todoListsCombo;

    public static void main(String[] args) {
        /* It posts an event (Runnable)at the end of Swings event list and is
		processed after all other GUI events are processed.*/
        new TodosListsSettings(null, new JComboBox<TodosList>());

    }

    public TodosListsSettings(JFrame jframe, JComboBox<TodosList> tListsCombo) {
        this.todoListsCombo = tListsCombo;
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {

                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                    mainFont = Utility.loadFont(FONT_RES_LOC);
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                        | UnsupportedLookAndFeelException ex) {
                    String msg = "Msg:" + ex.getMessage() + " /-/" + ex.getCause();
                    LOG.log(Level.SEVERE, msg);
                }
                try {

                    initUI();

                    //Creating the MenuBar and adding components
                    GridLayout gl = new GridLayout(0, 1);
                    JPanel mainPanel = new JPanel(gl);

                    JPanel leftPanel = new JPanel(); // the panel is not visible in output
                    //JPanel rightPanel = new JPanel();
                    // leftPanel.setBackground(MAIN_BG_COLOR);
                    //  leftPanel.setOpaque(true);

                    initLeftPanel(leftPanel);
                    //initRightPanel(rightPanel);

                    mainPanel.add(leftPanel);
                    // mainPanel.add(rightPanel);
                    // mainPanel.setBackground(MAIN_BG_COLOR);
                    // leftPanel.setOpaque(true);
                    getContentPane().add(mainPanel);
                    //setIconImage(Toolkit.getDefaultToolkit().getImage(FB_IMG_RES_LOC));
                    addWindowListener(new WindowAdapter() {

                        @Override
                        public void windowClosing(WindowEvent windowEvent) {
                            /*
                            if (JOptionPane.showConfirmDialog(jframe,
                                    "Are you sure you want to close this window?", "Close Window?",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                                System.exit(0);
                            }
                             */
                            //mainFrame.setVisible(true);
                            System.out.println("closing detected");
                        }

                        @Override
                        public void windowClosed(WindowEvent e) {
                            System.out.println("windowClosed detected");
                        }

                    });
                    setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                    setVisible(true);
                } catch (Exception e) {
                    String msg = "Msg:" + e.getMessage() + " /-/" + e.getCause();
                    LOG.log(Level.SEVERE, msg);
                }
            }

            public JPanel createListItem(String lblText, JButton btn) {
                JPanel listPane = new JPanel();
                //listPane.setBackground(new Color(0, 0, 0, 0));

                listPane.setLayout(new BoxLayout(listPane, BoxLayout.LINE_AXIS));

                listPane.add(Box.createVerticalGlue());
                JLabel lbl = new JLabel(lblText);
                listPane.add(lbl);

                listPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

                JPanel buttonPane = new JPanel();
                buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
                // buttonPane.setBackground(new Color(0, 0, 0, 0));

                buttonPane.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, new Color(205, 202, 232)));

                lbl.setForeground(MAIN_FG_COLOR);
                buttonPane.add(Box.createRigidArea(new Dimension(5, 0)));
                buttonPane.add(btn);
                buttonPane.add(Box.createVerticalGlue());
                listPane.add(buttonPane);
                //buttonPane.add(Box.createHorizontalGlue());
                listPane.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent mouseEvent) {
                        if (mouseEvent.getClickCount() == 2) {

                            System.out.println("opening image");
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        super.mouseEntered(e); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        super.mouseExited(e); //To change body of generated methods, choose Tools | Templates.
                    }

                });

                return listPane;
            }

            private void initUI() {
                setTitle(WINDOW_TITLE);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
                setLocationRelativeTo(null);
                setResizable(false);
                if (mainFont != null) {
                    Utility.setFont(getContentPane(), mainFont);

                }
            }
            JLabel appNameLbl;

            private void initLeftPanel(JPanel leftPanel) {
                // leftPanel.setBackground(new Color(86, 56, 171));

                Border b = BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(200, 200, 200));
                leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
                leftPanel.setBorder(BorderFactory.createTitledBorder(b, APP_INFO_BORDER_LBL, TitledBorder.LEFT, TitledBorder.TOP, mainFont, MAIN_FG_COLOR));
                JPanel topPen = new JPanel();

                topPen.setLayout(new BoxLayout(topPen, BoxLayout.Y_AXIS));

                JPanel headerPnl = new JPanel();
                headerPnl.setLayout(new BoxLayout(headerPnl, BoxLayout.LINE_AXIS));

                //headerPnl.setBackground(new Color(0, 0, 0, 0));
                JLabel logoLbl = new JLabel(Utility.scaleImage(new ImageIcon(TodoApp.class.getResource(SHAPE_LOGO_RES_LOC)), 80, 80));

                TodosList currentList = (TodosList) todoListsCombo.getSelectedItem();
                String n = (currentList == null ? "" : "\"" + currentList + "\" ");
                appNameLbl = new JLabel(n + APP_TITLE);
                appNameLbl.setForeground(MAIN_FG_COLOR);
                // appNameLbl.setBackground(MAIN_BG_COLOR);
                // appNameLbl.setOpaque(true);

                //JLabel appVerLbl = new JLabel(APP_VERSTION);
                //appVerLbl.setForeground(Color.white);
                headerPnl.add(Box.createRigidArea(new Dimension(10, 0)));
                headerPnl.add(logoLbl);
                headerPnl.add(Box.createRigidArea(new Dimension(20, 0)));
                headerPnl.add(appNameLbl);
                headerPnl.add(Box.createRigidArea(new Dimension(30, 0)));

                //headerPnl.add(appVerLbl);
                headerPnl.add(Box.createVerticalGlue());
                headerPnl.add(Box.createHorizontalGlue());

                topPen.add(headerPnl);
                //topPen.setBackground(MAIN_BG_COLOR);
                //topPen.setOpaque(false);
                ButtonCreator deleteListItemBtn = new ButtonCreator(Constants.DELETE_LIST_BUTTON_MSG,
                        Constants.DELETE_LIST_BUTTON_TOOLTIP_MSG, null, Cursor.HAND_CURSOR
                );
                deleteListItemBtn.setIcon(Utility.scaleImage(new ImageIcon(getClass().getResource(Resources.DELETE_LIST_BUTTON_ICON_RES)), Constants.SMALL_RES_ICONS_WIDTH, Constants.SMALL_RES_ICONS_HEIGHT));

                ButtonCreator renameListBtn = new ButtonCreator(Constants.RENAME_LIST_BUTTON_MSG,
                        Constants.RENAME_LIST_BUTTON_TOOLTIP_MSG, null, Cursor.HAND_CURSOR
                );

                renameListBtn.setIcon(Utility.scaleImage(new ImageIcon(getClass().getResource(Resources.RENAME_LIST_BUTTON_ICON_RES)), Constants.SMALL_RES_ICONS_WIDTH, Constants.SMALL_RES_ICONS_HEIGHT));

                renameListBtn.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.WHITE));
                renameListBtn.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));

                deleteListItemBtn.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.WHITE));
                deleteListItemBtn.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));

                renameListBtn.addActionListener((e) -> {
                    if (TodoApp.todosListsNames.size() > 0) {
                        String input = JOptionPane.showInputDialog(TodoApp.mainFrame, "Rename list: \"" + currentSelectedTodosList + "\"", "Input new list name:",
                                JOptionPane.WARNING_MESSAGE);
                        System.out.println(input);

                        if (input != null && !input.trim().isEmpty()) {
                            TodosList todoList = new TodosList(input);
                            todoList.setListId(TodoApp.currentSelectedTodosList.getListId());
                            todoList.setCreationDatetime(TodoApp.currentSelectedTodosList.getCreationDatetime());
                            todoList.setCreationTimestamp(TodoApp.currentSelectedTodosList.getCreationTimestamp());

                            Object obj = TodoApp.mainTodosContainer.get(TodoApp.currentSelectedTodosList.getListId());
                            TodoApp.mainTodosContainer.put(todoList.getListId(), (ArrayList) obj);
                            TodoApp.currentSelectedTodosList = todoList;

                            TodoApp.todosListsDaoImpl.update(todoList.getListId(), new Object[]{TodoUpdateTypes.LIST_CHANGE_NAME_UPDATE, todoList});
                            if (todoList.getListId() != -1) {
                                System.err.println("renamed succussfully");
                                updateTitleMsg(todoList.getListName());
                                System.err.println("new name is:" + todoList.getListName());
                            }
                            int si = todoListsCombo.getSelectedIndex();
                            TodoApp.todosListsNames.set(si, todoList);
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
                                updateTitleMsg((TodosList) todoListsCombo.getSelectedItem() + "");
                            } else {
                                //  todoListsCombo.actionPerformed(new ActionEvent("", 0, s));
                                todoListsCombo.removeAllItems();
                                updateTitleMsg("");
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

                topPen.add(createListItem("Rename:" + "                   ", renameListBtn));
                topPen.add(Box.createVerticalStrut(5));
                topPen.add(createListItem("Delete:" + "                      ", deleteListItemBtn));
                topPen.add(Box.createVerticalStrut(5));
                
               
                if (mainFont != null) {

                    appNameLbl.setFont(mainFont.deriveFont(Font.BOLD, 15f));
                }
                leftPanel.add(topPen);
            }

            private void updateTitleMsg(String name) {

                String n = ("".equals(name) ? " " : "\"" + name + "\" ");
                appNameLbl.setText(n + APP_TITLE);
                SwingUtilities.updateComponentTreeUI(appNameLbl);

                //appNameLbl.setForeground(Color.white);
                //appNameLbl.setBackground(new Color(0,0,0,0));
            }

        });

    }

}
