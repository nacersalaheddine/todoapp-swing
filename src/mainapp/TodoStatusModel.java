package mainapp;

import consts.Constants;

/**
 *
 * @author Nacer Salah Eddine
 */
public class TodoStatusModel {

    public static final int DONE_STATE = 1;
    public static final int DOING_STATE = 2;
    public static final int PENDING_STATE = 3;
    public static final int ALL_STATE = 4;
    public static final int PENDING_DOING_STATE = 5;

    private int status;

    @Override
    public String toString() {
        return statusStr;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }
    private String statusStr;

    TodoStatusModel(int status) {
        this.status = status;
        switch (this.status) {

            case DONE_STATE:
                statusStr = Constants.DONE_TODOS_STATUS_COMBO_TEXT;
                break;
            case DOING_STATE:
                statusStr = Constants.DOING_TODOS_STATUS_COMBO_TEXT;
                break;
            case PENDING_STATE:
                statusStr = Constants.PENDING_TODOS_STATUS_COMBO_TEXT;
                break;
            case PENDING_DOING_STATE:
                statusStr = Constants.PENDING_DOING_TODOS_STATUS_COMBO_TEXT;
                break;

            default:
                statusStr = Constants.ALL_TODOS_STATUS_COMBO_TEXT;
        }
    }

    public static String getStatusByCode(int statusCode) {

        switch (statusCode) {

            case DONE_STATE:
                return Constants.DONE_STATE_MSG;

            case DOING_STATE:
                return Constants.DOING_STATE_MSG;

            case PENDING_STATE:
                return Constants.PENDING_STATE_MSG;
            default:
                return "?";
        }

    }

}
