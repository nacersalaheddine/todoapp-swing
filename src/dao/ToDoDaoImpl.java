package dao;

import beans.Todo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import mainapp.TodoApp;
import static splash_screen.SplashScreen.mainFont;
import static mainapp.TodoApp.mainTodosContainer;
import static mainapp.TodoApp.updateTodosPanelByStatus;
import factory.TodoPanelComponent;
import consts.TodoUpdateTypes;

import java.util.logging.Level;
import java.util.logging.Logger;
import util.Utility;

/**
 *
 * @author Nacer Salah Eddine
 */
public class ToDoDaoImpl implements DaoInterface<Todo> {

    private Connection conx;
    private Statement statement = null;
    private ResultSet resultSet = null;
    public static final String TABLE_NAME = "todos";
    public static String INSERT_QUERY = "INSERT INTO " + TABLE_NAME
            + " (todo_content,datetime_created,creation_timestamp,todos_list_fk) VALUES (?,?,?,?);";
    private PreparedStatement preparedStatement = null;
    public static String DATABASE_NAME = "todos_db";//move to constants
    public static String SELECT_QUERY = "SELECT * FROM " + TABLE_NAME + ";";
    public static String SOFT_DELETE_RECORD_QUERY = "UPDATE " + TABLE_NAME + " SET active_status = 0 WHERE id =?;";
    public static String SELECT_BY_ACTIVE_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE active_status = 1;";
    private static final Logger LOG = Utility.getLogger(ToDoDaoImpl.class.getName());
    public static String UPDATE_CONTENT_QUERY = "UPDATE " + TABLE_NAME + " SET todo_content = ? WHERE id =?;";
    public static String UPDATE_STATUS_QUERY = "UPDATE " + TABLE_NAME + " SET status = ? WHERE id =?;";
    public static String UPDATE_LIST_ID_QUERY = "UPDATE " + TABLE_NAME + " SET todos_list_fk = ? WHERE id =?;";

    public ToDoDaoImpl(DAOFactory daoFactory) {

        try {
            this.conx = DAOFactory.getConnection();
        } catch (SQLException e) {
            System.err.println("Unable to get connection ... " + e);
            String msg = "Msg:" + e.getMessage() + " /-/" + e.getCause();
            LOG.log(Level.SEVERE, msg);

        }
    }

    @Override
    public Optional<Todo> get(long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Todo> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void showAllActive() {

        try {
            // Statements allow to issue SQL queries to the database
            this.statement = this.conx.createStatement();
            // Result set get the result of the SQL query
            this.resultSet = this.statement.executeQuery(SELECT_BY_ACTIVE_QUERY);
            writeResultSet(this.resultSet);

        } catch (SQLException e) {
            System.err.println("Error executing " + SELECT_BY_ACTIVE_QUERY + " Query.");
            System.err.println("Exception " + e);
            String msg = "Msg:" + e.getMessage() + " /-/" + e.getCause();
            LOG.log(Level.SEVERE, msg);

        }
    }

    @Override
    public void showAll() {

        try {
            // Statements allow to issue SQL queries to the database
            this.statement = this.conx.createStatement();
            // Result set get the result of the SQL query
            this.resultSet = this.statement.executeQuery(SELECT_QUERY);
            writeResultSet(this.resultSet);

        } catch (SQLException e) {
            System.err.println("Error executing " + SELECT_QUERY + " Query.");
            System.err.println("Exception " + e);
            String msg = "Msg:" + e.getMessage() + " /-/" + e.getCause();
            LOG.log(Level.SEVERE, msg);

        }

    }

    private void writeResultSet(ResultSet resultSet) throws SQLException {
        // ResultSet is initially before the first data set
        while (resultSet.next()) {
            // It is possible to get the columns via name
            // also possible to get the columns via the column number
            // which starts at 1
            // e.g. resultSet.getSTring(2);

            int todoId = resultSet.getInt("id");
            int todoStatus = resultSet.getInt("status");

            String creationTimeStamp = resultSet.getString("creation_timestamp");
            String creationDateTime = resultSet.getString("datetime_created");
            String todoContent = resultSet.getString("todo_content");

            int activeStatus = resultSet.getInt("active_status");
            long todosListFk = resultSet.getLong("todos_list_fk");
            /*
            System.out.println("--------------------------------------");
            System.out.println("todoId>>" + todoId);
            System.out.println("todoStatus>>" + todoStatus);
            System.out.println("creationTimeStamp>>" + creationTimeStamp);
            System.out.println("creationDateTime>>" + creationDateTime);
            System.out.println("todoContent>>" + todoContent);
            System.out.println("activeStatus>>" + activeStatus);
            System.out.println("todosListFk>>" + todosListFk);
            System.out.println("--------------------------------------");
             */
            if (TodoApp.currentSelectedTodosList == null) {
                return;
            }
            Todo todo = new Todo(todoContent, todoId, todosListFk, todoStatus);
            todo.setCreationDatetime(creationDateTime);
            todo.setCreationTimestamp(Long.parseLong(creationTimeStamp));
            TodoPanelComponent todoPanelItem = new TodoPanelComponent(todo);
            todoPanelItem.updateTodoStatus(todo, todoStatus);

            if (mainFont != null) {

                Utility.setFont(todoPanelItem, mainFont);
            }
            //System.out.println(">>" + todo.getTodosListId());
            if (mainTodosContainer.get(todo.getTodosListId()) != null) {
                mainTodosContainer.get(todo.getTodosListId()).add(todoPanelItem);
            }

        }
        updateTodosPanelByStatus(false, true);
    }

    @Override
    public void create(Todo t) {

        try {
            //add entry to database
            preparedStatement = this.conx.prepareStatement(INSERT_QUERY);
            //preparedStatement.setInt(0, t.getTodoStatus());//Commented because its defaulted to 3(pending)
            //preparedStatement.setInt(0, t.isActiveStatus() ? 1 : 0);//Commented because its defaulted to 1(active)

            preparedStatement.setString(1, t.getTodoText());
            preparedStatement.setString(2, t.getCreationDatetime());
            preparedStatement.setString(3, t.getCreationTimestamp() + "");
            preparedStatement.setLong(4, t.getTodosListId());

            int res = preparedStatement.executeUpdate();
            if (res == 1) {
                long lastInsertedId = getLastInsertedId();
                //System.out.println(">>>" + lastInsertedId);
                t.setTodoId(lastInsertedId);
            } else {
                t.setTodoId(-1);
            }
        } catch (Exception e) {
            System.err.println("Error executing " + INSERT_QUERY + " Query.");
            System.err.println("Exception " + e);
            String msg = "Msg:" + e.getMessage() + " /-/" + e.getCause();
            LOG.log(Level.SEVERE, msg);

        }

    }

    private long getLastInsertedId() throws Exception {
        return this.conx.prepareStatement("SELECT last_insert_rowid() AS LAST FROM " + TABLE_NAME).executeQuery().getLong("LAST");
    }

    @Override
    public void update(long id, Object[] params) {

        try {
            TodoUpdateTypes type = (TodoUpdateTypes) params[0];
            Todo todo = (Todo) params[1];
            if (null == type) {
                System.err.println("Null type error updating");
            } else {
                switch (type) {
                    case CONTENT_UPDATE:
                        String newContent = todo.getTodoText();
                        preparedStatement = this.conx.prepareStatement(UPDATE_CONTENT_QUERY);
                        preparedStatement.setString(1, newContent);
                        preparedStatement.setLong(2, id);
                        break;
                    case STATUS_UPDATE:
                        int newStatus = todo.getTodoStatus();
                        preparedStatement = this.conx.prepareStatement(UPDATE_STATUS_QUERY);
                        preparedStatement.setInt(1, newStatus);
                        preparedStatement.setLong(2, id);

                        break;
                    case LIST_CHANGE_UPDATE:
                        long newListId = todo.getTodosListId();
                        preparedStatement = this.conx.prepareStatement(UPDATE_LIST_ID_QUERY);
                        preparedStatement.setLong(1, newListId);
                        preparedStatement.setLong(2, id);
                        break;
                    default:
                        System.err.println("Unknown error");
                        break;
                }
            }
            int res = preparedStatement.executeUpdate();
            System.out.println("Update execution result.::" + res);
        } catch (SQLException e) {
            System.err.println("Error executing Query.");
            System.err.println("Exception " + e);
            String msg = "Msg:" + e.getMessage() + " /-/" + e.getCause();
            LOG.log(Level.SEVERE, msg);

        }

        /*
        
    Todo todo = new Todo();
        entity.setName(Objects.requireNonNull(params[0], "Name cannot be null"));
        entity.setEmail(Objects.requireNonNull(params[1], "Email cannot be null"));
         */
    }

    @Override
    public void delete(long t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void close() {
        try {
            if (this.resultSet != null) {
                this.resultSet.close();
            }

            if (this.statement != null) {
                this.statement.close();
            }

            if (this.conx != null) {
                DAOFactory.getConnection().close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing " + e);
            String msg = "Msg:" + e.getMessage() + " /-/" + e.getCause();
            LOG.log(Level.SEVERE, msg);

        }
    }

    public static String SOFT_DELETE_ALL_RECORDS_QUERY = "UPDATE "
            + TABLE_NAME + " SET active_status = 0 WHERE todos_list_fk =?;";
    public static String SOFT_DELETE_RECORDS_BY_STATUS_QUERY = "UPDATE "
            + TABLE_NAME + " SET active_status = 0 WHERE todos_list_fk =? and status=?;";

    @Override
    public void softDeleteAll(long id) {
        try {

            // Remove again the insert comment
            preparedStatement = this.conx.prepareStatement(SOFT_DELETE_ALL_RECORDS_QUERY);
            preparedStatement.setString(1, id + "");
            int res = preparedStatement.executeUpdate();
            if (res == 0) {
                System.err.println("Nothing updated: " + SOFT_DELETE_ALL_RECORDS_QUERY + " Query.");
            } else if (res == 1) {
                System.out.println("Deleted softly successfully");

            }

        } catch (SQLException e) {
            System.err.println("Error executing " + SOFT_DELETE_ALL_RECORDS_QUERY + " Query.");
            System.err.println("Exception " + e);
            String msg = "Msg:" + e.getMessage() + " /-/" + e.getCause();
            LOG.log(Level.SEVERE, msg);

        }
    }

    @Override
    public void softDelete(long id) {
        try {

            // Remove again the insert comment
            preparedStatement = this.conx.prepareStatement(SOFT_DELETE_RECORD_QUERY);
            preparedStatement.setString(1, id + "");
            int res = preparedStatement.executeUpdate();
            if (res == 0) {
                System.err.println("Nothing updated: " + SOFT_DELETE_RECORD_QUERY + " Query.");
            } else if (res == 1) {
                System.out.println("Deleted softly successfully");

            }

        } catch (SQLException e) {
            System.err.println("Error executing " + SOFT_DELETE_RECORD_QUERY + " Query.");
            System.err.println("Exception " + e);
            String msg = "Msg:" + e.getMessage() + " /-/" + e.getCause();
            LOG.log(Level.SEVERE, msg);

        }
    }

    @Override
    public void softDeleteByStatus(long id, int state) {

        try {

            // Remove again the insert comment
            preparedStatement = this.conx.prepareStatement(SOFT_DELETE_RECORDS_BY_STATUS_QUERY);
            preparedStatement.setString(1, id + "");
            preparedStatement.setString(2, state + "");
            int res = preparedStatement.executeUpdate();
            if (res == 0) {
                System.err.println("Nothing updated: " + SOFT_DELETE_RECORDS_BY_STATUS_QUERY + " Query.");
            } else if (res == 1) {
                System.out.println("Deleted softly successfully");

            }

        } catch (SQLException e) {
            System.err.println("Error executing " + SOFT_DELETE_RECORDS_BY_STATUS_QUERY + " Query.");
            System.err.println("Exception " + e);
            String msg = "Msg:" + e.getMessage() + " /-/" + e.getCause();
            LOG.log(Level.SEVERE, msg);

        }

    }

}
