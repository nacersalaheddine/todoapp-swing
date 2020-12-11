package dao;

/**
 *
 * @author Nacer Salah Eddine
 */
import beans.TodosList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mainapp.TodoApp;
import consts.TodoUpdateTypes;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Utility;

public class TodosListDaoImpl implements DaoInterface<TodosList> {

    private Connection conx;
    private Statement statement = null;
    private ResultSet resultSet = null;
    public static final String TABLE_NAME = "todos_lists";
    public static String INSERT_QUERY = "INSERT INTO " + TABLE_NAME
            + " (list_name,datetime_created,creation_timestamp) VALUES (?,?,?);";
    private PreparedStatement preparedStatement = null;
    public static String DATABASE_NAME = "todos_db";//move to constants
    public static String SELECT_QUERY = "SELECT * FROM " + TABLE_NAME + ";";
    public static String SELECT_BY_ACTIVE_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE active_status = 1;";

    public static String SOFT_DELETE_RECORD_QUERY = "UPDATE " + TABLE_NAME + " SET active_status = 0 WHERE list_id =?;";
    private static final Logger LOG = Utility.getLogger(TodosListDaoImpl.class.getName());

    public TodosListDaoImpl(DAOFactory daoFactory) {

        try {
            this.conx = DAOFactory.getConnection();
        } catch (SQLException e) {
            System.err.println("Unable to get connection ... " + e);
            String msg = "Msg:" + e.getMessage() + " /-/" + e.getCause();
            LOG.log(Level.SEVERE, msg);
        }
    }

    @Override
    public Optional<TodosList> get(long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TodosList> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    @Override
    public void showAllActive() {
        //SELECT_BY_ACTIVE_QUERY7

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

    private void writeResultSet(ResultSet resultSet) throws SQLException {
        // ResultSet is initially before the first data set
        while (resultSet.next()) {
            // It is possible to get the columns via name
            // also possible to get the columns via the column number
            // which starts at 1
            // e.g. resultSet.getSTring(2);

            int listId = resultSet.getInt("list_id");

            String creationTimeStamp = resultSet.getString("creation_timestamp");
            String creationDateTime = resultSet.getString("datetime_created");
            String listName = resultSet.getString("list_name");

            int activeStatus = resultSet.getInt("active_status");
            /*
            System.out.println("--------------------------------------");

            System.out.println("listId>>" + listId);
            System.out.println("creationTimeStamp>>" + creationTimeStamp);
            System.out.println("creationDateTime>>" + creationDateTime);
            System.out.println("listName>>" + listName);
            System.out.println("activeStatus>>" + activeStatus);
            System.out.println("--------------------------------------");
            */
            TodoApp.currentSelectedTodosList = new TodosList(listName);
            TodoApp.currentSelectedTodosList.setListId(listId);
            TodoApp.currentSelectedTodosList.setCreationDatetime(creationDateTime);
            TodoApp.currentSelectedTodosList.setCreationTimestamp(Long.parseLong(creationTimeStamp));

            TodoApp.todosListsNames.add(TodoApp.currentSelectedTodosList);
            TodoApp.mainTodosContainer.put(TodoApp.currentSelectedTodosList.getListId(), new ArrayList<>());
        }
    }

    @Override
    public void create(TodosList t) {

        try {
            //add entry to database
            preparedStatement = this.conx.prepareStatement(INSERT_QUERY);
            //preparedStatement.setInt(0, t.getTodoStatus());//Commented because its defaulted to 3(pending)
            //preparedStatement.setInt(0, t.isActiveStatus() ? 1 : 0);//Commented because its defaulted to 1(active)

            preparedStatement.setString(1, t.getListName());
            preparedStatement.setString(2, t.getCreationDatetime());
            preparedStatement.setString(3, t.getCreationTimestamp() + "");

            int res = preparedStatement.executeUpdate();
            if (res == 1) {
                long lastInsertedId = getLastInsertedId();
                //System.out.println(">>>" + lastInsertedId);
                t.setListId(lastInsertedId);
            } else {
                t.setListId(-1);
            }

            //ExecuteNonQueryd
        } catch (Exception e) {
            System.err.println("Error executing " + INSERT_QUERY + " Query.");
            System.err.println("Exception " + e);
            String msg = "Msg:" + e.getMessage() + " /-/" + e.getCause();
            LOG.log(Level.SEVERE, msg);
        }

    }

    public static String UPDATE_LIST_NAME_QUERY = "UPDATE " + TABLE_NAME + " SET list_name = ? WHERE list_id =?;";

    @Override
    public void update(long id, Object[] params) {

        try {
            TodoUpdateTypes type = (TodoUpdateTypes) params[0];
            TodosList todosList = (TodosList) params[1];
            if (null == type) {
                System.err.println("Null type error updating");
            } else {
                switch (type) {
                    case LIST_CHANGE_NAME_UPDATE:
                        System.err.println("old list name:" + todosList.getListName());
                        System.err.println("new list name:" + todosList.getListName());
                        System.err.println("new list name:" + todosList.getListId());

                        String newListName = todosList.getListName();
                        preparedStatement = this.conx.prepareStatement(UPDATE_LIST_NAME_QUERY);
                        preparedStatement.setString(1, newListName);
                        preparedStatement.setLong(2, id);
                        break;
                    default:
                        System.err.println("Unknown error");
                        break;
                }
            }
            int res = preparedStatement.executeUpdate();
            //System.out.println("Update execution result.::" + res);
        } catch (SQLException e) {
            System.err.println("Error executing Query.");
            System.err.println("Exception " + e);
            String msg = "Msg:" + e.getMessage() + " /-/" + e.getCause();
            LOG.log(Level.SEVERE, msg);
        }

    }

    @Override
    public void delete(long t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void softDelete(long id) {

        try {

            // Remove again the insert comment
            preparedStatement = this.conx.prepareStatement(SOFT_DELETE_RECORD_QUERY);
            preparedStatement.setString(1, id + "");
            preparedStatement.executeUpdate();
            int res = preparedStatement.executeUpdate();
            if (res == 0) {
                System.err.println("Nothing updated: " + SOFT_DELETE_RECORD_QUERY + " Query.");
            } else if (res == 1) {
                //System.out.println("Deleted softly successfully");

            }

        } catch (SQLException e) {
            System.err.println("Error executing " + SOFT_DELETE_RECORD_QUERY + " Query.");
            System.err.println("Exception " + e);
            String msg = "Msg:" + e.getMessage() + " /-/" + e.getCause();
            LOG.log(Level.SEVERE, msg);
        }
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

    private long getLastInsertedId() throws Exception {
        return this.conx.prepareStatement("SELECT last_insert_rowid() AS LAST FROM " + TABLE_NAME).executeQuery().getLong("LAST");
    }

    @Override
    public void softDeleteAll(long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void softDeleteByStatus(long id, int state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
