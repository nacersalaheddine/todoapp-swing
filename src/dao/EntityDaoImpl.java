package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.sql.Statement;
import beans.Entity;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.Utility;

public class EntityDaoImpl implements DaoInterface<Entity> {

    private List<Entity> entityList = new ArrayList<>();
    private DAOFactory daoFactory;
    private Connection conx;
    private Statement statement = null;
    private ResultSet resultSet = null;
    private PreparedStatement preparedStatement = null;
    private static final Logger LOG = Utility.getLogger(EntityDaoImpl.class.getName());

    public static String DATABASE_NAME = "entities_application";
    public static String TABLE_NAME = "entities";
    //public static String SELECT_QUERY = "SELECT * FROM "+DATABASE_NAME+"."+TABLE_NAME+";";
    public static String SELECT_QUERY = "SELECT * FROM " + TABLE_NAME + ";";

    public static String SELECT_BY_ID_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE id=";
    public static String INSERT_QUERY = "INSERT INTO " + TABLE_NAME
            + " (name, email) VALUES (?,?);";

    public static String DELETE_RECORD_QUERY = "DELETE FROM " + TABLE_NAME + " WHERE id=?;";
    public static String UPDATE_DATA_QUERY = "UPDATE " + TABLE_NAME + " SET name = ?, email = ? WHERE id =?;";

    public EntityDaoImpl(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;

        try {
            this.conx = this.daoFactory.getConnection();
        } catch (Exception e) {
            System.err.println("Unable to get connection ... " + e);
            String msg = "Msg:" + e.getMessage() + " /-/" + e.getCause();
            LOG.log(Level.SEVERE, msg);
        }
    }

    @Override
    public Optional<Entity> get(long id) {
        Entity entity = null;

        try {

            this.statement = this.conx.createStatement();
            SELECT_BY_ID_QUERY += id + ";";
            this.resultSet = this.statement.executeQuery(SELECT_BY_ID_QUERY);

            while (this.resultSet.next()) {
                String name = this.resultSet.getString("name");
                String email = this.resultSet.getString("email");
                entity = new Entity(name, email);
            }

        } catch (Exception ex) {
            System.err.println("Error executing " + SELECT_BY_ID_QUERY + " Query.");
            System.err.println("Exception " + ex);
            String msg = "Msg:" + ex.getMessage() + " /-/" + ex.getCause();
            LOG.log(Level.SEVERE, msg);
        }

        return Optional.ofNullable(entity);
    }

    @Override
    public List<Entity> getAll() {

        try {

            this.statement = this.conx.createStatement();
            // Result set get the result of the SQL query
            this.resultSet = this.statement.executeQuery(SELECT_QUERY);
            // ResultSet is initially before the first data set
            while (this.resultSet.next()) {
                String name = this.resultSet.getString("name");
                String email = this.resultSet.getString("email");
                entityList.add(new Entity(name, email));
            }

        } catch (Exception e) {
            System.err.println("Error executing " + SELECT_QUERY + " Query.");
            System.err.println("Exception " + e);
            String msg = "Msg:" + e.getMessage() + " /-/" + e.getCause();
            LOG.log(Level.SEVERE, msg);
        }

        return entityList;
    }

    @Override
    public void showAll() {

        try {
            // Statements allow to issue SQL queries to the database
            this.statement = this.conx.createStatement();
            // Result set get the result of the SQL query
            this.resultSet = this.statement.executeQuery(SELECT_QUERY);
            writeResultSet(this.resultSet);

        } catch (Exception e) {
            System.err.println("Error executing " + SELECT_QUERY + " Query.");
            System.err.println("Exception " + e);
            String msg = "Msg:" + e.getMessage() + " /-/" + e.getCause();
            LOG.log(Level.SEVERE, msg);

        }

    }

    @Override
    public void create(Entity entity) {
        try {
            //add entry to database
            preparedStatement = this.conx.prepareStatement(INSERT_QUERY);
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error executing " + INSERT_QUERY + " Query.");
            System.err.println("Exception " + e);
            String msg = "Msg:" + e.getMessage() + " /-/" + e.getCause();
            LOG.log(Level.SEVERE, msg);

        }

    }

    @Override
    public void update(long id, Object[] params) {
        /*
        Entity entity = new Entity();
        entity.setName(Objects.requireNonNull(params[0], "Name cannot be null"));
        entity.setEmail(Objects.requireNonNull(params[1], "Email cannot be null"));
        
        try {
 
            preparedStatement = this.conx.prepareStatement(UPDATE_DATA_QUERY);
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getEmail());
            preparedStatement.setString(3, id+"");
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            System.err.println("Error executing "+UPDATE_DATA_QUERY+" Query.");
            System.err.println("Exception "+e);
        }

         */

    }

    @Override
    public void delete(long id) {

        try {

            // Remove again the insert comment
            preparedStatement = this.conx.prepareStatement(DELETE_RECORD_QUERY);
            preparedStatement.setString(1, id + "");
            preparedStatement.executeUpdate();
            //System.out.println("Deleted successfully");
            System.err.println(DELETE_RECORD_QUERY + " Query.");

        } catch (Exception e) {
            System.err.println("Error executing " + DELETE_RECORD_QUERY + " Query.");
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
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");

            //System.out.println("Name: " + name);
            //System.out.println("Email: " + email);

        }
    }

    // You need to close the resultSet
    public void close() {
        try {
            if (this.resultSet != null) {
                this.resultSet.close();
            }

            if (this.statement != null) {
                this.statement.close();
            }

            if (this.conx != null) {
                this.conx.close();
            }
        } catch (Exception e) {
            System.err.println("Error closing " + e);
            String msg = "Msg:" + e.getMessage() + " /-/" + e.getCause();
            LOG.log(Level.SEVERE, msg);

        }
    }

    @Override
    public void softDelete(long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void showAllActive() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
