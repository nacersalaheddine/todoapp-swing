package factory;

import dialogs.EditTodoDialog;
import beans.Todo;
import beans.TodosList;
import consts.Constants;
import consts.Resources;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import mainapp.TodoApp;
import mainapp.TodoStatusModel;
import consts.TodoUpdateTypes;
import dialogs.TodoInfo;
import java.awt.Component;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import static mainapp.TodoApp.mainTodosContainer;
import util.Utility;
import static mainapp.TodoApp.currentSelectedTodosList;
import static mainapp.TodoApp.mainFrame;
import static mainapp.TodoApp.todoDaoImpl;
import org.json.JSONObject;
import static splash_screen.SplashScreen.mainFont;
import static mainapp.TodoApp.NEW_LINE;
import static mainapp.TodoApp.exportTypes;

/**
 *
 * @author Nacer Salah Eddine
 */
public final class TodoPanelComponent extends JPanel {

    private Color prevStateColor1;
    public Todo todoContent;
    public JLabel statusImgLbl;
    public JMenuItem doingRMenuItem, doneRMenuItem, pendingRMenuItem;
    public MouseEvent ev = null;
    public ImageIcon pendingImageIcon = Utility.scaleImage(new ImageIcon(TodoApp.class.getResource(Resources.PENDING_STATUS_ICON_RES)), 20, 20);
    public ImageIcon doneImageIcon = Utility.scaleImage(new ImageIcon(TodoApp.class.getResource(Resources.DONE_STATUS_ICON_RES)), 20, 20);
    public ImageIcon doingImageIcon = Utility.scaleImage(new ImageIcon(TodoApp.class.getResource(Resources.DOING_STATUS_ICON_RES)), 20, 20);
    private static final Logger LOG = Utility.getLogger(TodoPanelComponent.class.getName());

    private final Color PEND_TODO_BG_COLOR = new Color(241, 243, 245);//new Color(240, 240, 240);
    private final Color DOING_TODO_BG_COLOR = new Color(148, 172, 223);
    private final Color DONE_TODO_BG_COLOR = new Color(215, 215, 215);
    private JFileChooser jFileChooser;

    public TodoPanelComponent(Todo todo) {
        todoContent = todo;

        BoxLayout box = new BoxLayout(this, BoxLayout.LINE_AXIS);
        ButtonCreator delBtn = new ButtonCreator("", "Delete Todo", null, Cursor.HAND_CURSOR);
        delBtn.setIcon(Utility.scaleImage(new ImageIcon(TodoApp.class.getResource(Resources.DELETE_TODO_ITEM_BUTTON_ICON_RES)), 16, 16));

        delBtn.makeTransparent();

        // build poup menu
        final JPopupMenu popup = new JPopupMenu();
        // New project menu item
        JMenuItem moveTodoMenuItem = new JMenuItem("Move this todo to specified list...",
                Utility.scaleImage(new ImageIcon(TodoApp.class.getResource(Resources.MOVE_TODO_ITEM_ICON_RES)), 16, 16));
        moveTodoMenuItem.setMnemonic(KeyEvent.VK_P);
        moveTodoMenuItem.getAccessibleContext().setAccessibleDescription(
                "New Project");
        moveTodoMenuItem.addActionListener((ActionEvent e) -> {
            //JOptionPane.showMessageDialog(TodoApp.mainFrame, "New Project clicked!");

            JDialog.setDefaultLookAndFeelDecorated(true);
            // String initialSelection = "Dogs";
            Object selection = JOptionPane.showInputDialog(TodoApp.mainFrame, "Choose the move list?",
                    "Move Todo", JOptionPane.QUESTION_MESSAGE, null, TodoApp.todosListsNames.toArray(), currentSelectedTodosList);
            //System.out.println(selection);
            if (selection != null && !selection.toString().trim().isEmpty() && currentSelectedTodosList != selection) {
                TodosList moveList = (TodosList) selection;

                mainTodosContainer.get(moveList.getListId()).add(TodoPanelComponent.this);

                //update DB
                todoContent.setTodosListId(moveList.getListId());
                todoDaoImpl.update(todoContent.getTodoId(), new Object[]{TodoUpdateTypes.LIST_CHANGE_UPDATE, todoContent});

                TodoApp.listPane.remove(TodoPanelComponent.this);
                mainTodosContainer.get(currentSelectedTodosList.getListId()).remove(TodoPanelComponent.this);
                TodoApp.updateTodosPanelByStatus(false, true);
            }
        });
        JMenuItem editTodoMenuItem = new JMenuItem("Edit this To-Do...",
                Utility.scaleImage(new ImageIcon(getClass().getResource(Resources.EDIT_MENU_ITEM_ICON_RES)), 16, 16));
        JMenuItem showTodoInfoMenuItem = new JMenuItem("Show To-Do Info ...",
                Utility.scaleImage(new ImageIcon(getClass().getResource(Resources.SHOW_TODO_INFO_ITEM_ICON_RES)), 16, 16));
        JMenuItem exportTodoMenuItem = new JMenuItem("Export this To-Do as ...",
                Utility.scaleImage(new ImageIcon(getClass().getResource(Resources.EXPORT_TODO_ITEM_ICON_RES)), 16, 16));
        /*
        JMenuItem exportAllTodosInListMenuItem = new JMenuItem("Export all todos in this list as ...",
                Utility.scaleImage(new ImageIcon(getClass().getResource(Resources.EXPORT_ALL_TODOS_ITEM_ICON_RES)), 16, 16));
         */
        JMenuItem copyTodoMenuItem = new JMenuItem("Copy to clipboard...",
                Utility.scaleImage(new ImageIcon(getClass().getResource(Resources.COPY_TODO_ITEM_ICON_RES)), 16, 16));

        JMenu changeTodoStateMenu = initStatusMenu();
        changeTodoStateMenu.setIcon(Utility.scaleImage(new ImageIcon(getClass().getResource(Resources.COPY_PATH_BUTTON_ICON_RES)), 16, 16));
        /**/

        exportTodoMenuItem.addActionListener((ActionEvent e) -> {
            /*
            Todo j;
            if (getComponent(4) instanceof Todo) {
                j = (Todo) getComponent(4);
                System.out.println(">>" + j.getTodoText());
               
            }*/
            JDialog.setDefaultLookAndFeelDecorated(true);
            Object selection = JOptionPane.showInputDialog(TodoApp.mainFrame, "Choose the Export Type?",
                    "Export this Todo", JOptionPane.QUESTION_MESSAGE, null, TodoApp.exportTypes, exportTypes[1]);

            System.out.println(selection);
            if (selection != null && !selection.toString().trim().isEmpty() && currentSelectedTodosList != selection) {
                String selectedExportType = (String) selection;
                StringBuilder stringBldrData;
                String outputData;
                //show the file chooser
                //prepare the file to export , for now as json
                System.out.println("-----" + selectedExportType);
                System.out.println(this.todoContent.getTodoText());
                System.out.println(this.todoContent.getTodosListId());
                System.out.println(this.todoContent.getTodoStatus());

                switch (selectedExportType) {

                    case "JSON":
                        JSONObject mainObj = new JSONObject();
                        mainObj.put("list_name", currentSelectedTodosList.getListName());
                        mainObj.put("list_id", currentSelectedTodosList.getListId());
                        mainObj.put("list_datetime_created", currentSelectedTodosList.getCreationDatetime());
                        mainObj.put("list_creation_timestamp", currentSelectedTodosList.getCreationTimestamp());

                        JSONObject todoObj = new JSONObject();
                        todoObj.put("id", this.todoContent.getTodoId());
                        todoObj.put("datetime_created", this.todoContent.getCreationDatetime());
                        todoObj.put("creation_timestamp", this.todoContent.getCreationTimestamp());
                        todoObj.put("status", TodoStatusModel.getStatusByCode(this.todoContent.getTodoStatus()));
                        todoObj.put("todo_content", this.todoContent.getTodoText());

                        mainObj.put("todo", todoObj);

                        outputData = mainObj.toString();

                        break;

                    case "TXT"://TXT            
                        stringBldrData = new StringBuilder();

                        stringBldrData.append("LIST NAME:").append(currentSelectedTodosList.getListName()).append(NEW_LINE);
                        stringBldrData.append("LIST ID:").append(currentSelectedTodosList.getListId()).append(NEW_LINE);
                        stringBldrData.append("LIST CREATION DATE-TIME:").append(currentSelectedTodosList.getCreationDatetime()).append(NEW_LINE);
                        stringBldrData.append("LIST CREATION TIMESTAMP:").append(currentSelectedTodosList.getCreationTimestamp()).append(NEW_LINE);
                        stringBldrData.append("----------------------------------------------------------------------------").append(NEW_LINE);
                        stringBldrData.append("TODO ID:").append(this.todoContent.getTodoId()).append(NEW_LINE);
                        stringBldrData.append("TODO CONTENT:").append(this.todoContent.getTodoText()).append(NEW_LINE);
                        String statusStr = TodoStatusModel.getStatusByCode(this.todoContent.getTodoStatus());
                        stringBldrData.append("TODO STATUS:").append(statusStr).append(NEW_LINE);
                        stringBldrData.append("TODO CREATION DATE-TIME:").append(this.todoContent.getCreationDatetime()).append(NEW_LINE);
                        stringBldrData.append("TODO CREATION TIMESTAMP:").append(this.todoContent.getCreationTimestamp()).append(NEW_LINE);
                        outputData = stringBldrData.toString();
                        break;

                    default:
                        return;
                }

                System.out.println("-----");
                // create an object of JFileChooser class 
                jFileChooser = new JFileChooser(FileSystemView.getFileSystemView().
                        getFileSystemView().getHomeDirectory());

                // invoke the showsSaveDialog function to show the save dialog 
                int r = jFileChooser.showSaveDialog(mainFrame);

                // if the user selects a file 
                if (r == JFileChooser.APPROVE_OPTION) {
                    // set the label to the path of the selected file 
                    System.out.println(jFileChooser.getSelectedFile().getAbsolutePath());
                    //+"."+selectedExportType

                    Utility.writeFile(jFileChooser.getSelectedFile(), outputData, selectedExportType.toLowerCase());
                } // if the user cancelled the operation 
                else {
                    System.out.println("the user cancelled the operation");
                }
            }
        });
        /*
        exportAllTodosInListMenuItem.addActionListener((ActionEvent e) -> {
            
        });
         */
 /**/
        showTodoInfoMenuItem.addActionListener((ActionEvent e) -> {
            Todo todoComp;
            if (getComponent(4) instanceof Todo) {
                todoComp = (Todo) getComponent(4);
                System.out.println(">>" + todoComp.getTodoText());

                new TodoInfo(mainFrame, todoComp, currentSelectedTodosList);
            }
        });
        copyTodoMenuItem.addActionListener((ActionEvent e) -> {
            Todo j;

            if (getComponent(4) instanceof Todo) {
                j = (Todo) getComponent(4);
                System.out.println(">>" + j);
                Utility.copyToClipboardText(j.getTodoText());
            }
        });

        popup.add(showTodoInfoMenuItem);
        popup.add(new JSeparator());
        popup.add(moveTodoMenuItem);
        popup.add(editTodoMenuItem);
        popup.add(new JSeparator());

        popup.add(copyTodoMenuItem);
        popup.add(changeTodoStateMenu);
        popup.add(new JSeparator());

        popup.add(exportTodoMenuItem);
        //popup.add(exportAllTodosInListMenuItem);
        editTodoMenuItem.addActionListener((e) -> {

            EditTodoDialog ed = new EditTodoDialog(todoContent);
            Utility.setFont(ed, mainFont);
            ed.setVisible(true);

        });
        JMenuItem deleteTodoMenuItem = new JMenuItem("Delete this todo...",
                Utility.scaleImage(new ImageIcon(TodoApp.class.getResource(Resources.DELETE_TODO_ITEM_BUTTON_ICON_RES)), 16, 16));
        deleteTodoMenuItem.setMnemonic(KeyEvent.VK_F);

        deleteTodoMenuItem.addActionListener((ActionEvent e) -> {
            delBtn.doClick();
        });
        popup.add(new JSeparator());
        popup.add(deleteTodoMenuItem);
        TodoApp.addEditMenueItems(popup);

        setLayout(box);
        add(Box.createRigidArea(new Dimension(5, 0)));

        this.statusImgLbl = new JLabel();
        updateTodoStatus(todoContent, TodoStatusModel.PENDING_STATE);
        add(this.statusImgLbl);

        JSeparator jsep = new JSeparator(SwingConstants.VERTICAL);
        jsep.setMaximumSize(this.getMinimumSize());
        jsep.setBackground(Color.WHITE);
        jsep.setForeground(Color.WHITE);
        jsep.setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
        add(jsep);

        setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(Box.createRigidArea(new Dimension(5, 0)));

        add(todoContent);
        add(Box.createHorizontalGlue());
        add(delBtn);
        //  p.add(copyPathBtn);
        //p.add(editTodo);
        setBackground(PEND_TODO_BG_COLOR);
        //checkBox.setBackground(new Color(239, 246, 250));
        Border paddingBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        Border border = BorderFactory.createLineBorder(new Color(0, 0, 0, 0));

        this.statusImgLbl.setBorder(BorderFactory.createCompoundBorder(border, paddingBorder));

        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.WHITE), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        Utility.setFont(popup, mainFont);
        popup.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                // System.out.println("popupMenuWillBecomeVisible."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                //  System.out.println("popupMenuWillBecomeInvisible."); //To change body of generated methods, choose Tools | Templates.
                // prevStateColor1;
                // prevStateColor2;
                setBackground(prevStateColor1);
                //checkBox.setBackground(prevStateColor2);
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                System.out.println("popupMenuCanceled."); //To change body of generated methods, choose Tools | Templates.
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                //JTable table = (JTable) mouseEvent.getSource();
                //Point point = mouseEvent.getPoint();
                //int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2) {
                    // your valueChanged overridden method 
                    System.out.println("opening todo for edit");
                }
            }

            private void showPopup(MouseEvent e) {

                prevStateColor1 = getBackground();

                setBackground(new Color(0, 246, 0));

                popup.show(e.getComponent(),
                        e.getX(), e.getY());

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                Todo selectedTodo;
                int todoComponentLoc = 4;
                if (getComponent(todoComponentLoc) instanceof Todo) {
                    selectedTodo = (Todo) getComponent(todoComponentLoc);
                    if (e instanceof MouseEventTrigger) {
                        //When we are here, this means the we have clicked on the popup list 
                        updateTodoStatus(selectedTodo, ((MouseEventTrigger) e).getCustomStatus());
                        //update database
                        TodoApp.todoDaoImpl.update(selectedTodo.getTodoId(), new Object[]{TodoUpdateTypes.STATUS_UPDATE, selectedTodo});
                        TodoApp.updateTodosPanelByStatus(false, true);

                        selectedTodo.setText(selectedTodo.getDisplayableTodo());
                        return;
                    }
                    
                    switch (e.getButton()) {

                        case MouseEvent.BUTTON1://Left mouse
                           // System.out.println("left mouse pressed>>>");
                            switch (selectedTodo.getTodoStatus()) {
                                case TodoStatusModel.DONE_STATE:
                                    updateTodoStatus(selectedTodo, TodoStatusModel.PENDING_STATE);
                                    //update database
                                    TodoApp.todoDaoImpl.update(selectedTodo.getTodoId(), new Object[]{TodoUpdateTypes.STATUS_UPDATE, selectedTodo});
                                    break;
                                case TodoStatusModel.PENDING_STATE:
                                    updateTodoStatus(selectedTodo, TodoStatusModel.DONE_STATE);
                                    //update database
                                    TodoApp.todoDaoImpl.update(selectedTodo.getTodoId(), new Object[]{TodoUpdateTypes.STATUS_UPDATE, selectedTodo});
                                    break;

                                default:
                                    break;
                            }

                            TodoApp.updateTodosPanelByStatus(false, true);
                            break;
                        //key.getKeyChar() == KeyEvent.VK_ENTER
                        //key.getKeyChar() == KeyEvent.VK_ENTER

                        case MouseEvent.BUTTON3://Right mouse
                            showPopup(e);
                            break;
                        case MouseEvent.BUTTON2://Middle mouse
                            //System.out.println("BUTTON2");
                            System.out.println(selectedTodo.getText());

                            if (selectedTodo.getTodoStatus() == TodoStatusModel.DOING_STATE) {

                                updateTodoStatus(selectedTodo, TodoStatusModel.PENDING_STATE);
                                //update database
                                TodoApp.todoDaoImpl.update(selectedTodo.getTodoId(), new Object[]{TodoUpdateTypes.STATUS_UPDATE, selectedTodo});

                            } else if (selectedTodo.getTodoStatus() == TodoStatusModel.PENDING_STATE) {

                                updateTodoStatus(selectedTodo, TodoStatusModel.DOING_STATE);

                                //update database
                                TodoApp.todoDaoImpl.update(selectedTodo.getTodoId(), new Object[]{TodoUpdateTypes.STATUS_UPDATE, selectedTodo});

                            }
                            TodoApp.updateTodosPanelByStatus(false, true);

                            break;
                        default:
                            break;
                    }

                    selectedTodo.setText(selectedTodo.getDisplayableTodo());

                }

            }

        });

        delBtn.addActionListener((e) -> {
            TodoApp.listPane.remove(this);
            mainTodosContainer.get(currentSelectedTodosList.getListId()).remove(this);
            todoDaoImpl.softDelete(todoContent.getTodoId());
            //TodoApp.todoDaoImpl.update(selectedTodo.getTodoId(), new Object[]{TodoUpdateTypes.STATUS_UPDATE, selectedTodo});

            TodoApp.updateTodosPanelByStatus(false, true);

        });

    }

    private JMenu initStatusMenu() {

        JMenu changeTodoStateMenu = new JMenu("Change todo state...");
        doingRMenuItem = new JMenuItem(Constants.DOING_STATE_MSG, Utility.scaleImage(new ImageIcon(getClass().getResource(Resources.DOING_STATUS_ICON_RES)), 16, 16));

        changeTodoStateMenu.add(doingRMenuItem);

        doingRMenuItem.addActionListener((e) -> {
            /*
            ev = new MouseEvent(this, // which
                    MouseEvent.MOUSE_CLICKED, // what
                    System.currentTimeMillis(), // when
                    MouseEvent.BUTTON2_MASK,//MouseEvent.BUTTON2_MASK, //  modifiers
                    10, 10, // where: at (x, y}
                    1, // only n click 
                    false); // not a popup trigger
             */
            ev = new MouseEventTrigger(this, // which
                    MouseEvent.MOUSE_CLICKED, // what
                    System.currentTimeMillis(), // when
                    MouseEvent.BUTTON1_MASK, //  modifiers
                    10, 10, // where: at (x, y}
                    1, // only n click 
                    false, TodoStatusModel.DOING_STATE);
            if (ev != null) {
                dispatchEvent(ev);
            }
        });

        doneRMenuItem = new JMenuItem(Constants.DONE_STATE_MSG, Utility.scaleImage(new ImageIcon(getClass().getResource(Resources.DONE_STATUS_ICON_RES)), 16, 16));
        changeTodoStateMenu.add(doneRMenuItem);

        doneRMenuItem.addActionListener((e) -> {
            /*
            ev = new MouseEvent(this, // which
                    MouseEvent.MOUSE_CLICKED, // what
                    System.currentTimeMillis(), // when
                    MouseEvent.BUTTON1_MASK, //  modifiers
                    10, 10, // where: at (x, y}
                    1, // only n click 
                    false); // not a popup trigger
             */
            ev = new MouseEventTrigger(this, // which
                    MouseEvent.MOUSE_CLICKED, // what
                    System.currentTimeMillis(), // when
                    MouseEvent.BUTTON1_MASK, //  modifiers
                    10, 10, // where: at (x, y}
                    1, // only n click 
                    false, TodoStatusModel.DONE_STATE);
            if (ev != null) {
                dispatchEvent(ev);
            }
        });

        pendingRMenuItem = new JMenuItem(Constants.PENDING_STATE_MSG, Utility.scaleImage(new ImageIcon(getClass().getResource(Resources.PENDING_STATUS_ICON_RES)), 16, 16));
        changeTodoStateMenu.add(pendingRMenuItem);
        // updateSelectionStatus(pendingRMenuItem,false);
        pendingRMenuItem.setSelected(true);
        //            pendingRMenuItem.setText("<html><b><u>" + Constants.PENDING_STATE_MSG + "</u></b></html>");
        updateSelectionStatus(pendingRMenuItem);
        pendingRMenuItem.addActionListener((e) -> {
            /*
            ev = new MouseEvent(this, // which
                    MouseEvent.MOUSE_CLICKED, // what
                    System.currentTimeMillis(), // when
                    MouseEvent.BUTTON1_MASK, //  modifiers
                    10, 10, // where: at (x, y}
                    1, // only n click 
                    false); // not a popup trigger
             */
            ev = new MouseEventTrigger(this, // which
                    MouseEvent.MOUSE_CLICKED, // what
                    System.currentTimeMillis(), // when
                    MouseEvent.BUTTON1_MASK, //  modifiers
                    10, 10, // where: at (x, y}
                    1, // only n click 
                    false, TodoStatusModel.PENDING_STATE); // not a popup trigger
            if (ev != null) {
                dispatchEvent(ev);
            }
        });

        return changeTodoStateMenu;
    }

    private class MouseEventTrigger extends MouseEvent {

        private final int customStatus;

        public int getCustomStatus() {
            return customStatus;
        }

        public MouseEventTrigger(Component source, int id, long when, int modifiers, int x, int y, int clickCount, boolean popupTrigger, int customStatus) {
            super(source, id, when, modifiers, x, y, clickCount, popupTrigger);
            this.customStatus = customStatus;
        }

    }

    private void updateSelectionStatus(JMenuItem mi) {

        if (doingRMenuItem.equals(mi)) {
            doneRMenuItem.setText(Constants.DONE_STATE_MSG);
            pendingRMenuItem.setText(Constants.PENDING_STATE_MSG);
            doingRMenuItem.setText("<html><b><u>" + Constants.DOING_STATE_MSG + "</u></b></html>");

        } else if (doneRMenuItem.equals(mi)) {
            doingRMenuItem.setText(Constants.DOING_STATE_MSG);
            pendingRMenuItem.setText(Constants.PENDING_STATE_MSG);
            doneRMenuItem.setText("<html><b><u>" + Constants.DONE_STATE_MSG + "</u></b></html>");

        } else if (pendingRMenuItem.equals(mi)) {
            doneRMenuItem.setText(Constants.DONE_STATE_MSG);
            doingRMenuItem.setText(Constants.DOING_STATE_MSG);
            pendingRMenuItem.setText("<html><b><u>" + Constants.PENDING_STATE_MSG + "</u></b></html>");

        }

    }

    public void updateTodoStatus(Todo todo, int newStatus) {
        todo.setTodoStatus(newStatus);
        switch (newStatus) {

            case TodoStatusModel.DONE_STATE:
                setBackground(DONE_TODO_BG_COLOR);

                this.statusImgLbl.setIcon(doneImageIcon);
                this.statusImgLbl.setToolTipText(Constants.DONE_STATE_MSG);

                setToolTipText(Constants.DONE_STATE_MSG);
                updateSelectionStatus(doneRMenuItem);

                break;
            case TodoStatusModel.DOING_STATE:

                this.statusImgLbl.setIcon(doingImageIcon);
                this.statusImgLbl.setToolTipText(Constants.DOING_STATE_MSG);

                setBackground(DOING_TODO_BG_COLOR);//new Color(150, 200, 200));
                setToolTipText(Constants.DOING_STATE_MSG);
                updateSelectionStatus(doingRMenuItem);

                break;
            case TodoStatusModel.PENDING_STATE:
                updateSelectionStatus(pendingRMenuItem);
                setBackground(PEND_TODO_BG_COLOR);//new Color(239, 246, 250));

                this.statusImgLbl.setIcon(pendingImageIcon);
                // pendingRMenuItem.setSelected(true);
                // updateSelectionStatus(pendingRMenuItem,false);
                this.statusImgLbl.setToolTipText(Constants.PENDING_STATE_MSG);

                setToolTipText(Constants.PENDING_STATE_MSG);

                break;
            default:

        }

    }

}
