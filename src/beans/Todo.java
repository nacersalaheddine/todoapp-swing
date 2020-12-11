package beans;

import java.awt.Color;
import javax.swing.JLabel;
import mainapp.TodoStatusModel;
import util.Utility;

/**
 *
 * @author Nacer Salah Eddine
 */
public class Todo extends JLabel {

    private Color doneTextColor;
    private Color pendingTextColor;
    private Color doingTextColor;
    private final Color PEND_TODO_FG_COLOR = new Color(48, 48, 48);
    private final Color DOING_TODO_FG_COLOR = new Color(250, 250, 250);
    private final Color DONE_TODO_FG_COLOR = new Color(144, 144, 144);

    private String todoText;
    private int todoStatus;

    private long creationTimestamp;
    private String creationDatetime;
    private boolean activeStatus;
    //private String todosListName;
    private long todoId;
    private long todosListId;

    public long getTodoId() {
        return todoId;
    }

    public void setTodoId(long todoId) {
        this.todoId = todoId;
    }

    public Todo() {

    }

    public Todo(String todoText, long todoId, long todosListId, int todoStatus) {
        this.todoStatus = todoStatus;
        this.todoText = todoText;
        //this.todosListName = todosListName;
        this.doneTextColor = DONE_TODO_FG_COLOR;
        this.pendingTextColor = PEND_TODO_FG_COLOR;
        this.doingTextColor = DOING_TODO_FG_COLOR;
        this.creationTimestamp = System.currentTimeMillis();
        this.creationDatetime = Utility.getFullDateTime();
        this.activeStatus = true;
        this.todoId = todoId;
        this.todosListId = todosListId;
        this.setTodoText(todoText);
        //System.out.println("Created on LIST ID>>" + todosListId);
    }

    public void setTodoList(TodosList lst) {

    }

    public final String getDisplayableTodo() {

        switch (this.todoStatus) {

            case TodoStatusModel.DONE_STATE:
                setForeground(this.doneTextColor);
                return "<html><p><s><b> " + this.todoText + " </b></s></p></html>";
            case TodoStatusModel.DOING_STATE:
                setForeground(this.doingTextColor);
                return "<html><p><i><b> " + this.todoText + " </b></i></p></html>";
            case TodoStatusModel.PENDING_STATE:
                setForeground(this.pendingTextColor);
                return "<html><p><b> " + this.todoText + " </b></p></html>";
            default:
                return "<html><p><b> " + this.todoText + " </b></p></html>";
        }
    }

    public long getTodosListId() {
        return todosListId;
    }

    public void setTodosListId(long todosListId) {
        this.todosListId = todosListId;
    }

    public Color getDoneTextColor() {
        return doneTextColor;
    }

    public void setDoneTextColor(Color doneTextColor) {
        this.doneTextColor = doneTextColor;
    }

    public Color getPendingTextColor() {
        return pendingTextColor;
    }

    public void setPendingTextColor(Color pendingTextColor) {
        this.pendingTextColor = pendingTextColor;
    }

    public Color getDoingTextColor() {
        return doingTextColor;
    }

    public void setDoingTextColor(Color doingTextColor) {
        this.doingTextColor = doingTextColor;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public String getCreationDatetime() {
        return creationDatetime;
    }

    public void setCreationDatetime(String creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    public boolean isActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(boolean activeStatus) {
        this.activeStatus = activeStatus;
    }

    public int getTodoStatus() {
        return todoStatus;
    }

    public void setTodoStatus(int todoStatus) {
        this.todoStatus = todoStatus;

    }

    public String getTodoText() {
        return todoText;
    }

    @Override
    public String toString() {
        return this.getTodoText();
    }

    public void setTodoText(String todoText) {
        this.todoText = todoText;
        setText(this.getDisplayableTodo());
    }

}
