package experiments;

import consts.Resources;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONWriter;
import util.Utility;

/**
 *
 * @author Nacer Salah Eddine
 */
public class JSONTEST {

    // public static final String JSON_FILE_LOCATION = "resources/data/todo_app_data.json";
    public static final String JSON_FILE_LOCATION = "resources/data/new.json";
    public static File jsonFile;
    public static BufferedWriter writer = null;
    public static JSONObject mainObj;
    public static StringBuilder writerJsnStrBuilder;
    public static JSONWriter jsonWriter;

    public static void main(String[] args) throws Exception {
        //build the template json
        Path jsonFilePath = Paths.get(JSON_FILE_LOCATION);
        if (Files.exists(jsonFilePath)) {
            //insertTodo("aaaaaaaaaa", "salah todos");
            /*
            readJsonDataFile();
            removeTodo(0, "new list");
            readJsonDataFile();
             */
            //createNewList("salah todos");
            //deactivateList("new list") ;
            //deactivateList("salah todos");
            updateTodoStatus(0, "DONE", "new list");
        } else if (Files.notExists(jsonFilePath)) {

            initJson();
            insertTodo("this is a new todo", "new list");
        } else {
            System.out.println("unknown error , the existence of the file cannot be verified. (maybe there is no access rights to this path)");
        }

        //reads();
    }

    private static void insertTodo(String todoContent, String listName) throws Exception {
        if (mainObj == null) {
            byte[] filesBytesData = Files.readAllBytes(Paths.get(JSON_FILE_LOCATION));
            String fileStrData = new String(filesBytesData);
            mainObj = new JSONObject(fileStrData);
        }

        // long idCounter = 1;//first get the last id from the last existing todo else start from one
        //mainObj.put("new key", "new value");
        //remember to update todos_number value
        JSONObject todoJsonObject = new JSONObject();

        todoJsonObject.put("datetime_created", Utility.getFullDateTime());
        todoJsonObject.put("creation_timestamp", System.currentTimeMillis());
        todoJsonObject.put("status", "PENDING");
        todoJsonObject.put("todo_content", todoContent);

        for (Object todoList : mainObj.getJSONObject("data").getJSONArray("todos_lists")) {

            if (((JSONObject) todoList).get("todos_active_status").equals("ACTIVE")) {
                if (((JSONObject) todoList).get("list_name").equals(listName)) {

                    long lastTodoId = ((JSONObject) todoList).getLong("last_todo_id");
                    todoJsonObject.put("id", ++lastTodoId);
                    long todosCount = ((JSONArray) ((JSONObject) todoList).get("todos")).length();

                    System.out.println("number of todos" + todosCount);

                    ((JSONObject) todoList).put("todos_number", todosCount + 1);

                    ((JSONArray) ((JSONObject) todoList).get("todos")).put((int) todosCount, todoJsonObject);
                    ((JSONObject) todoList).put("last_todo_id", lastTodoId);
                    break;
                }
            }
        }
        /*
        ((JSONObject) mainObj.getJSONObject("data").getJSONArray("todos_lists").get(0)).put("creation_timestamp", System.currentTimeMillis());
        ((JSONObject) mainObj.getJSONObject("data").getJSONArray("todos_lists").get(0)).put("datetime_created", Utility.getFullDateTime());
         */
        writeJSON();
    }

    private static void removeTodo(int index, String listName) throws Exception {
        if (mainObj == null) {
            byte[] filesBytesData = Files.readAllBytes(Paths.get(JSON_FILE_LOCATION));
            String fileStrData = new String(filesBytesData);
            mainObj = new JSONObject(fileStrData);
        }

        for (Object todoList : mainObj.getJSONObject("data").getJSONArray("todos_lists")) {

            if (((JSONObject) todoList).get("todos_active_status").equals("ACTIVE")) {
                if (((JSONObject) todoList).get("list_name").equals(listName)) {

                    long todosCount = ((JSONArray) ((JSONObject) todoList).get("todos")).length();
                    if (todosCount == 0) {
                        return;
                    }
                    ((JSONArray) ((JSONObject) todoList).get("todos")).remove(index);
                    ((JSONObject) todoList).put("todos_number", todosCount - 1);
                    System.out.println("number of todos" + todosCount);
                    break;
                }
            }
        }
        /*
        ((JSONObject) mainObj.getJSONObject("data").getJSONArray("todos_lists").get(0)).put("creation_timestamp", System.currentTimeMillis());
        ((JSONObject) mainObj.getJSONObject("data").getJSONArray("todos_lists").get(0)).put("datetime_created", Utility.getFullDateTime());
         */
        writeJSON();
    }

    private static void writeJSON() {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new File(JSON_FILE_LOCATION));
            pw.print(mainObj.toString());
        } catch (JSONException ex) {
            Logger.getLogger(JSONTEST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(JSONTEST.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pw != null) {
                try {
                    pw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private static void readJsonDataFile() {
        try {
            if (mainObj == null) {
                byte[] filesBytesData = Files.readAllBytes(Paths.get(JSON_FILE_LOCATION));
                String fileStrData = new String(filesBytesData);
                mainObj = new JSONObject(fileStrData);
            }

            JSONObject dataObj = mainObj.getJSONObject("data");

            int listsNumber = dataObj.getInt("lists_number");
            JSONArray todoListsArry = dataObj.getJSONArray("todos_lists");

            System.out.println(listsNumber);
            for (Object todoListObj : todoListsArry) {
                String listName = ((JSONObject) todoListObj).getString("list_name");
                long listId = ((JSONObject) todoListObj).getLong("list_id");
                long listCreationTimestamp = ((JSONObject) todoListObj).getLong("creation_timestamp");
                String listCreationDatetime = ((JSONObject) todoListObj).getString("datetime_created");
                System.out.println(listName);
                System.out.println(listId);
                System.out.println(listCreationTimestamp);
                System.out.println(listCreationDatetime);

                JSONArray todosArry = ((JSONObject) todoListObj).getJSONArray("todos");
                //loop the list of todos here
                for (Object todoObj : todosArry) {
                    String todoStatus = ((JSONObject) todoObj).getString("status");
                    System.out.println(todoStatus);
                }

            }

            //  System.out.println(todoListsArry);
        } catch (IOException ex) {
            System.out.format("I/O Exception:", ex);

        } catch (Exception ex) {
            System.out.format("Exception:", ex);

        }
    }

    private static void reads() {
        Path filePath = Paths.get(JSON_FILE_LOCATION);

        Charset charset = StandardCharsets.UTF_8;

        try {
            //converting to stream
            Files.lines(filePath, charset).forEach(System.out::println);
            //Files.lines(filePath, charset).forEach((k)->System.out.println(">>"+k));
        } catch (IOException ex) {
            System.out.format("I/O Exception:", ex);
        }
    }

    private static void writeJsonData() {

        //  writerJsnStrBuilder.append(mainObj.toString());
        try {

            // writer.write(writerJsnStrBuilder.toString());
            writer.write("fucking test");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                writer.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private static void initJson() {

        // writerJsnStrBuilder = new StringBuilder();
        mainObj = new JSONObject(Resources.JSON_TEMP);

        //jsonWriter = new JSONWriter(new PrintWriter (new File(JSON_FILE_LOCATION)));
        ((JSONObject) mainObj.getJSONObject("data").getJSONArray("todos_lists").get(0)).put("creation_timestamp", System.currentTimeMillis());
        ((JSONObject) mainObj.getJSONObject("data").getJSONArray("todos_lists").get(0)).put("datetime_created", Utility.getFullDateTime());
        /*
            System.out.println(
                    ((JSONObject) obj.getJSONObject("data").getJSONArray("todos_lists").get(0)).getLong("creation_timestamp")
            );
            System.out.println(
                    ((JSONObject) obj.getJSONObject("data").getJSONArray("todos_lists").get(0)).getString("datetime_created")
            );
         */

        mainObj.put("creation_timestamp", System.currentTimeMillis());
        mainObj.put("creation_datetime", Utility.getFullDateTime());

        writeJSON();
    }

    private static void createNewList(String newListName) throws Exception {

        if (mainObj == null) {
            byte[] filesBytesData = Files.readAllBytes(Paths.get(JSON_FILE_LOCATION));
            String fileStrData = new String(filesBytesData);
            mainObj = new JSONObject(fileStrData);
        }
        JSONArray listsArr = mainObj.getJSONObject("data").getJSONArray("todos_lists");
        int listsSize = listsArr.length();

        long lastListId = mainObj.getJSONObject("data").getLong("last_list_id");
        JSONObject todoJsonListObject = new JSONObject();

        todoJsonListObject.put("datetime_created", Utility.getFullDateTime());
        todoJsonListObject.put("creation_timestamp", System.currentTimeMillis());
        todoJsonListObject.put("list_name", newListName);
        todoJsonListObject.put("last_todo_id", 0);
        todoJsonListObject.put("todos_number", 0);
        todoJsonListObject.put("list_id", lastListId + 1);
        todoJsonListObject.put("todos_active_status", "ACTIVE");
        todoJsonListObject.put("todos", new JSONArray());
        mainObj.getJSONObject("data").put("last_list_id", lastListId + 1);

        mainObj.getJSONObject("data").put("lists_number", listsSize + 1);
        listsArr.put(listsSize, todoJsonListObject);

        writeJSON();

    }

    private static void deactivateList(String listName) throws Exception {
        if (mainObj == null) {
            byte[] filesBytesData = Files.readAllBytes(Paths.get(JSON_FILE_LOCATION));
            String fileStrData = new String(filesBytesData);
            mainObj = new JSONObject(fileStrData);
        }

        for (Object todoList : mainObj.getJSONObject("data").getJSONArray("todos_lists")) {
            System.out.println(">>" + ((JSONObject) todoList).get("list_name"));
            if (((JSONObject) todoList).get("list_name").equals(listName)) {
                if (((JSONObject) todoList).get("todos_active_status").equals("ACTIVE")) {

                    ((JSONObject) todoList).put("todos_active_status", "INACTIVE");
                    break;
                } else {
                    System.out.println("specified list " + listName + " is inactive");
                    return;
                }
            }
        }

        writeJSON();
    }

    private static void updateTodoStatus(int index, String newStatus, String listName) throws Exception {

        if (mainObj == null) {
            byte[] filesBytesData = Files.readAllBytes(Paths.get(JSON_FILE_LOCATION));
            String fileStrData = new String(filesBytesData);
            mainObj = new JSONObject(fileStrData);
        }

        for (Object todoList : mainObj.getJSONObject("data").getJSONArray("todos_lists")) {

            if (((JSONObject) todoList).get("todos_active_status").equals("ACTIVE")) {
                if (((JSONObject) todoList).get("list_name").equals(listName)) {

                    JSONArray todos = ((JSONArray) ((JSONObject) todoList).get("todos"));
                    ((JSONObject) todos.get(index)).put("status", newStatus);

                    break;
                }
            }
        }

        writeJSON();
    }

}
