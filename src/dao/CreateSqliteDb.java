package dao;

import beans.TodosList;
import consts.Constants;
import java.sql.Connection;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import static mainapp.TodoApp.todosListsDaoImpl;
import util.Utility;

/**
 *
 * @author Nacer Salah Eddine
 */
public class CreateSqliteDb {

    public static final String CREATE_TODO_TBL
            = "CREATE TABLE `todos` (\n"
            + "	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n"
            + "	`status`	NUMERIC NOT NULL DEFAULT 3,\n"
            + "	`todo_content`	TEXT NOT NULL,\n"
            + "	`datetime_created`	TEXT NOT NULL,\n"
            + "	`creation_timestamp`	TEXT NOT NULL,\n"
            + "	`active_status`	NUMERIC NOT NULL DEFAULT 1,\n"
            + "	`todos_list_fk`	INTEGER NOT NULL,\n"
            + "	FOREIGN KEY(`todos_list_fk`) REFERENCES `todos_lists`(`list_id`)\n"
            + ");";
    public static final String CREATE_TODOSLISTS_TBL
            = "CREATE TABLE `todos_lists` (\n"
            + "	`list_id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n"
            + "	`creation_timestamp`	TEXT NOT NULL,\n"
            + "	`datetime_created`	TEXT NOT NULL,\n"
            + "	`list_name`	TEXT NOT NULL,\n"
            + "	`active_status`	INTEGER DEFAULT 1\n"
            + ");\n"
            + "";
    private static final Logger LOG = Utility.getLogger(CreateSqliteDb.class.getName());

    public static void initSQLiteDB() {
        //System.out.println("initing the sqlite database");
        try {
            DAOFactory.getInstance();
            Connection conx = DAOFactory.getConnection();
            Statement stmt = conx.createStatement();

            stmt.execute(CREATE_TODOSLISTS_TBL);
            stmt.execute(CREATE_TODO_TBL);

            todosListsDaoImpl = DAOFactory.getInstance().getTodosListDao();
            TodosList l = new TodosList(Constants.DEFAULT_LIST_NAME);
            todosListsDaoImpl.create(l);

            if (l.getListId() == -1) {
                //System.out.println("Not added to database");
            }

        } catch (Exception ex) {
            String msg = "Msg:" + ex.getMessage() + " /-/" + ex.getCause();
            LOG.log(Level.SEVERE, msg);
        }

    }
}
