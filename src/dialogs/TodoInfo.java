package dialogs;

import beans.TodosList;
import beans.Todo;

import factory.ButtonCreator;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import mainapp.TodoStatusModel;
import util.Utility;

/**
 *
 * @author Nacer Salah Eddine
 */
public class TodoInfo {

    public static final int WINDOW_WIDTH = 500;
    public static final int WINDOW_HEIGHT = 520;
    public static final String APP_TITLE = "TODO-APP";

    private String todoContent;
    private long todoId;
    private String todoStatus;
    private String todoCreationDatetime;
    private long todoTimestamp;
    private String listName;
    private long listID;
    private String listCreationDatetime;
    private long listTimestamp;

    private String INFO_TEXT2 = "";

    private static final Logger LOG = Utility.getLogger(TodoInfo.class.getName());

    private JTextPane pane;

    public TodoInfo(Frame f, Todo todo, TodosList todosList) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                        | UnsupportedLookAndFeelException ex) {

                    String msg = "Msg:" + ex.getMessage() + " /-/" + ex.getCause();
                    LOG.log(Level.SEVERE, msg);
                }

                initData(todo, todosList);

                JDialog d = new JDialog(f, "To-Do information");
                JPanel basic = new JPanel();
                basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
                d.add(basic);

                JPanel topPanel = new JPanel(new BorderLayout(0, 0));
                //        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
                topPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 0));

                //stopPanel.setMaximumSize(new Dimension(WINDOW_WIDTH, 0));
                JLabel hint = new JLabel("<html><h3 style=\"color:blue\">Info of \" " + todo.getTodoText().substring(0, todo.getTodoText().length() > 10 ? 10 : todo.getTodoText().length()) + "... \" To-Do.</h3> </html>");
                hint.setBorder(BorderFactory.createEmptyBorder(5, 25, 5, 0));
                topPanel.add(hint);

                JSeparator separator = new JSeparator();
                separator.setForeground(Color.gray);

                topPanel.add(separator, BorderLayout.SOUTH);

                basic.add(topPanel);

                JPanel textPanel = new JPanel();
                textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
                textPanel.setBackground(Color.WHITE);
                //textPanel.setMaximumSize(new Dimension(WINDOW_WIDTH, 0));
                textPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
                pane = new JTextPane();

                pane.setContentType("text/html");

                pane.setEditable(false);
                textPanel.add(new JScrollPane(pane));
                textPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
                pane.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));

                basic.add(textPanel);

                //String text = "<html><h2><b style=\"font-size:2.5em;\">1</b>&#47;2</h2></html>";
                JPanel jp = createListItem(new JLabel(""), d);

                basic.add(jp);
                // basic.add(Box.createVerticalBox());
                d.setSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
                d.setResizable(true);
                d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                d.setLocationRelativeTo(null);
                d.setVisible(true);
            }

        });
    }

    private void initData(Todo todo, TodosList todosList) {

        this.todoContent = todo.getTodoText();
        this.todoId = todo.getTodoId();
        this.todoStatus = TodoStatusModel.getStatusByCode(todo.getTodoStatus());
        this.todoCreationDatetime = todo.getCreationDatetime();
        this.todoTimestamp = todo.getCreationTimestamp();

        this.listName = todosList.getListName();
        this.listID = todosList.getListId();
        this.listCreationDatetime = todosList.getCreationDatetime();
        this.listTimestamp = todosList.getCreationTimestamp();

        this.INFO_TEXT2
                = "<hr>"
                + "<p><b>To-Do Content:</b>" + this.todoContent + "</p>"
                + "<p><b>To-Do Status:</b>" + this.todoStatus + "</p>"
                + "<p><b>To-Do ID:</b>" + this.todoId + "</p>"
                + "<p><b>To-Do Creation Date-Time:</b>" + this.todoCreationDatetime + "</p>"
                + "<p><b>To-Do Creation Timestamp:</b>" + this.todoTimestamp + "</p>"
                + "<hr>"
                + "<p><b>List Name:</b>" + this.listName + "</p>"
                + "<p><b>List ID:</b>" + this.listID + "</p>"
                + "<p><b>LIST Creation Date-Time:</b>" + this.listCreationDatetime + "</p>"
                + "<p><b>LIST Creation Timestamp:</b>" + this.listTimestamp + "</p>";

    }

    public JPanel createListItem(JLabel infoLbl, JDialog d) {
        JPanel listPane = new JPanel();

        ButtonCreator closeBtn = new ButtonCreator("Close", "Close", null, Cursor.HAND_CURSOR);
        ButtonCreator copyBtn = new ButtonCreator("Copy", "Copy to clipboard", null, Cursor.HAND_CURSOR);

        closeBtn.addActionListener((e) -> {
            d.dispose();
        });
        copyBtn.addActionListener((e) -> {
            //System.out.println("copying to clipboard");
            Utility.copyToClipboardText(this.pane.getText());
        });
        this.pane.setText(INFO_TEXT2);
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.X_AXIS));

        listPane.setBorder(new EmptyBorder(10, 25, 10, 25));

        infoLbl.setFont(new Font("impact", Font.PLAIN, 15));

        infoLbl.setForeground(Color.DARK_GRAY);

        JLabel sep = new JLabel(" \\\\ INFO \\\\ ");
        sep.setFont(new Font("impact", Font.PLAIN, 15));
        listPane.add(sep);
        listPane.add(infoLbl);

        listPane.add(Box.createHorizontalGlue());
        listPane.add(copyBtn);
        listPane.add(closeBtn);
        listPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {

                    //System.out.println("opening image");
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

}
