package experiments;

import dao.DaoInterface;
import dao.DAOFactory;
import java.util.Optional;
import beans.Entity;
import beans.Todo;
import beans.TodosList;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlDBExp {

    private static DaoInterface<Entity> entityDaoImpl;
    private static DaoInterface<Todo> todoDaoImpl;
    private static DaoInterface<TodosList> todosListsDaoImpl;
    static final String SQLITE_DB_FILE_LOCATION = "resources/sqlite/todos_db.db";

    public static void main(String[] args) throws Exception {
        Path dbFilePath = Paths.get(SQLITE_DB_FILE_LOCATION);
        if (Files.exists(dbFilePath)) {
            //testTodoListDao();
            testTodoDao();

            //testEntityDao();
        } else if (Files.notExists(dbFilePath)) {

            initSQLiteDB();
        } else {
            System.out.println("unknown error , the existence of the file cannot be verified. (maybe there is no access rights to this path)");
        }

    }

    private static Entity getUser(long id) {
        Optional<Entity> entity = entityDaoImpl.get(id);

        return entity.orElseGet(
                () -> new Entity("non-existing user", "no-email"));
    }

    private static void testTodoDao() throws Exception {
        todoDaoImpl = DAOFactory.getInstance().getTodoDao();
        // entityDaoImpl.showAll();
        todoDaoImpl.create(new Todo("new todo content",100,1,1));

        //todoDaoImpl.showAll();
        //todoDaoImpl.softDelete(200);
        //System.out.println("0000000000000000000000000000000000000000000000");
        todoDaoImpl.showAllActive();
        Todo todo = new Todo("this si asdkfqjmsdfkq ml",100,1,1);
        todoDaoImpl.create(todo);
        todoDaoImpl.showAll();
        /*
        todo.setTodoStatus(TodoStatusModel.DONE_STATE);
         */
        todo.setTodosListId(9);
        //todoDaoImpl.update(7, new Object[]{TodoUpdateTypes.CONTENT_UPDATE,todo});
        //todoDaoImpl.update(7, new Object[]{TodoUpdateTypes.STATUS_UPDATE,todo});
        //todoDaoImpl.update(7, new Object[]{TodoUpdateTypes.LIST_CHANGE_UPDATE,todo});

        //System.out.println("0000000000000000000000000000000000000000000000");
        // todoDaoImpl.showAllActive();
        todoDaoImpl.close();
    }

    private static void testTodoListDao() throws Exception {
        todosListsDaoImpl = DAOFactory.getInstance().getTodosListDao();
        // entityDaoImpl.showAll();
        TodosList l = new TodosList("Study list");
        todosListsDaoImpl.create(l);
        //  TodosList todosList = new TodosList("fucked up list name hahahaha", 9);
        //  todosListsDaoImpl.showAll();
        if (l.getListId() == -1) {
            System.out.println("Not added to database");
        }
        System.out.println("0000000000000000000000000000000000000000000000>" + l.getListId());
        // todosListsDaoImpl.update(9, new Object[]{TodoUpdateTypes.LIST_CHANGE_NAME_UPDATE, todosList});

        // todosListsDaoImpl.showAllActive();
        // todosListsDaoImpl.softDelete(8);
        todosListsDaoImpl.close();

    }

    private static void testEntityDao() throws Exception {
        entityDaoImpl = DAOFactory.getInstance().getEntityDao();
        // entityDaoImpl.showAll();
        entityDaoImpl.create(new Entity("John", "john@domain.com"));
        entityDaoImpl.showAll();
        Entity entity = getUser(1);
        System.out.println(entity.getName());
        System.out.println(entity.getEmail());
        //entityDaoImpl.delete(1);
        //entityDaoImpl.update(1, new String[]{"blaky", "black@domain.com"});

        entityDaoImpl.close();
    }
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

    private static void initSQLiteDB() {
        System.out.println("initing the sqlite database");
        try {
            Connection conx = DAOFactory.getConnection();
            Statement stmt = conx.createStatement();

            stmt.execute(CREATE_TODOSLISTS_TBL);
            stmt.execute(CREATE_TODO_TBL);

            if (conx != null) {
                conx.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
