package dialogs;

import factory.ButtonCreator;
import beans.Todo;
import consts.Resources;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import mainapp.TodoApp;
import consts.TodoUpdateTypes;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import static mainapp.TodoApp.todoDaoImpl;
import util.Utility;

/**
 *
 * @author Nacer Salah Eddine
 */
public class EditTodoDialog extends JDialog {

    public static final int WIND_WIDTH = 400;
    public static final int WIND_HEIGHT = 300;
    private static final Logger LOG = Utility.getLogger(EditTodoDialog.class.getName());

    public EditTodoDialog(Todo todoContent) {
        super(TodoApp.mainFrame);
        super.setTitle("Edit todo");

        super.setSize(WIND_WIDTH, WIND_HEIGHT);
        super.setLocationRelativeTo(null);
        super.setLayout(new BorderLayout());
        JPanel basic = new JPanel();
        basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
        super.add(basic);
        JPanel topPanel = new JPanel(new BorderLayout(0, 0));
        topPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 0));

        JLabel name = new JLabel("Edit todo");
        name.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 10));
        topPanel.add(name);

        JSeparator separator = new JSeparator();
        separator.setForeground(Color.gray);

        topPanel.add(separator, BorderLayout.SOUTH);

        basic.add(topPanel);

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        JTextPane pane = new JTextPane();

        //pane.setContentType("text/html");
        // String text = "<paragraph><b>Content</b></paragraph>";
        pane.setText(todoContent.getTodoText());
        pane.setEditable(true);
        textPanel.add(new JScrollPane(pane));

        basic.add(textPanel);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 25));
        ButtonCreator confirmEditBtn = new ButtonCreator("Confirm Edit", "Confirm Edit", null, Cursor.HAND_CURSOR);
        ButtonCreator cancelBtn = new ButtonCreator("Cancel", "Cancel action", null, Cursor.HAND_CURSOR);

        confirmEditBtn.setIcon(Utility.scaleImage(new ImageIcon(getClass().getResource(Resources.OK_WIND_BTN_ICON_RES)), 16, 16));
        cancelBtn.setIcon(Utility.scaleImage(new ImageIcon(getClass().getResource(Resources.CANCEL_WIND_BTN_ICON_RES)), 16, 16));

        confirmEditBtn.setMnemonic(KeyEvent.VK_N);

        cancelBtn.setMnemonic(KeyEvent.VK_C);

        bottom.add(confirmEditBtn);
        bottom.add(cancelBtn);
        basic.add(bottom);

        bottom.setMaximumSize(new Dimension(Short.MAX_VALUE, 0));

        /*
        JTextArea textArea = new JTextArea();
        textArea.setText(todoContent.getTodoText());
        textArea.setFont(new Font("cairo", Font.BOLD, 15));
         */
        add(name, BorderLayout.NORTH);

        confirmEditBtn.addActionListener((ActionEvent event) -> {
            String oldText = todoContent.getText().trim();
            String newText = pane.getText().trim();
            if (newText != null && !newText.isEmpty()) {
                if (!oldText.equals(newText)) {
                    todoContent.setTodoText(newText);
                    todoDaoImpl.update(todoContent.getTodoId(), new Object[]{TodoUpdateTypes.CONTENT_UPDATE, todoContent});
                }
            }
            dispose();
        });
        cancelBtn.addActionListener((e) -> {
            dispose();
        });
        // add(textArea, BorderLayout.CENTER);
        //add(close, BorderLayout.SOUTH);
        super.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        super.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    }

    public static void main(String[] args) {
        try {
            EditTodoDialog ed = new EditTodoDialog(new Todo("test", 1, 1, 5));
            ed.setVisible(true);
        } catch (Exception e) {
           // System.out.println("errrr");
                String msg = "Msg:" + e.getMessage() +" /-/"+ e.getCause();
            LOG.log(Level.SEVERE, msg);
        }

    }
}
