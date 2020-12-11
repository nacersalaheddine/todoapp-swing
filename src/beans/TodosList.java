package beans;

import util.Utility;

/**
 *
 * @author Nacer Salah Eddine
 */
public class TodosList {

    private long listId;

    private long creationTimestamp;
    private String creationDatetime;
    private boolean activeStatus;
    private String listName;

    public TodosList() {
    }

    public TodosList(String listName) {
        this.creationTimestamp = System.currentTimeMillis();
        this.creationDatetime = Utility.getFullDateTime();
        this.activeStatus = true;
        this.listName = listName;
    }

    @Override
    public String toString() {
        return listName;
    }

    public long getListId() {
        return listId;
    }

    public void setListId(long listId) {
        this.listId = listId;
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

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

}
