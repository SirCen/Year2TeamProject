package application.services;

import application.models.MenuItemModel;
import application.repository.DatabaseTools;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MenuServices {

  @Autowired
  private Connection connection;

  private DatabaseTools databaseTools = new DatabaseTools();

  /**
   * This method gets all the information from the menu and outputs it for use on the webpage.
   * @return the full menu
   */
  public ArrayList<MenuItemModel> getMenu() {  // gets ALL items
    ArrayList<MenuItemModel> output = new ArrayList<>();
    try {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM menu ORDER BY menu_id");
      MenuItemModel data;
      while (rs.next()) {
        data = new MenuItemModel(rs.getInt("menu_id"), rs.getString("name"), rs.getFloat("price"),
                rs.getString("description"), rs.getString("allergies"), rs.getInt("calories"),
                rs.getInt("available"), rs.getString("image"), rs.getString("category"));
        output.add(data);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return output;
  }

  /**
   * Returns a list of all the available items.
   * @throws SQLException if there is an error in a SQL statement
   * @return ArrayList of MenuItemModel.
   */
  public ArrayList<MenuItemModel> availableMenu() throws SQLException {
    // gets only available items
    ArrayList<MenuItemModel> output = new ArrayList<>();
    Statement stmt = connection.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM menu WHERE available = '1' ORDER BY menu_id");
    MenuItemModel data;
    while (rs.next()) {
      data = new MenuItemModel(rs.getInt("menu_id"), rs.getString("name"),
          rs.getFloat("price"), rs.getString("description"),
          rs.getString("allergies"), rs.getInt("calories"),
          rs.getInt("available"), rs.getString("image"),
          rs.getString("category"));
      output.add(data);
    }
    return output;
  }

  /**
   * This method sends the order to the database by calling the appropriate service.
   *
   * @param tableNum The table number
   * @param waiterId The ID of the waiter
   */
  public void orderToDatabase(int tableNum, int waiterId) {
    String executeStatement = "INSERT INTO orders (table_num, status) VALUES (" + tableNum
        + ", 'ordered')";

    if (waiterId != 0) {
      executeStatement = "INSERT INTO orders (table_num, staff_id, status) VALUES (" + tableNum
          + ", " + waiterId + ", 'accepted')";
      // bypass waiter page acceptance as the waiter made the order
    }

    try {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate(executeStatement);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method is used to set the needs_assistance column for a table.
   * @param tableNum The table number
   * @param needsHelp Boolean for whether or not the table needs help
   */
  public void customerNeedsAssistance(int tableNum, boolean needsHelp) {
    try {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("UPDATE tables SET needs_assistance=" + needsHelp
          + " WHERE table_id = " + tableNum + ";");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method parses the json of the order and it's modifications received from the frontend into
   * a usable format from the backend using lambda expressions.
   *
   * @param tableNum    The table number
   * @param stringOrder The order itself
   */
  public void jsonToDatabase(int tableNum, String stringOrder) {
    int orderId = 0;  //temp
    try {  // getting id
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT order_id FROM orders WHERE "
          + "table_num =" + tableNum + " ORDER BY order_id DESC LIMIT 1");
      while (rs.next()) {
        orderId = rs.getInt("order_id");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    JSONObject order = new JSONObject(stringOrder);
    int finalOrderId = orderId;
    order.keys().forEachRemaining(id -> {
      JSONObject item = order.getJSONObject(id);
      item.keys().forEachRemaining(mod -> {
        if (!mod.equals("name")) {
          String itemEntry = String.format("%d, %d, ", finalOrderId, Integer.parseInt(id));
          String adjustments = mod.substring(1, mod.length() - 1).trim().isEmpty()
              ? "None" : mod.substring(1, mod.length() - 1);
          itemEntry += adjustments;
          String ordered = "ordered";
          for (int i = 0; i < (int) item.get(mod); i++) {
            try {
              PreparedStatement stmt = connection.prepareStatement("INSERT INTO "
                  + "order_items (order_id, menu_id, adjustments, status) VALUES (?,?,?,?)");
              String[] items = itemEntry.split(",");
              stmt.setInt(1, Integer.parseInt(items[0]));
              stmt.setInt(2, Integer.parseInt(items[1].replaceAll("\\s+","")));
              stmt.setString(3, items[2]);
              stmt.setString(4,ordered);
              stmt.executeUpdate();
            } catch (SQLException e) {
              e.printStackTrace();
            }
          }
        }
      });
    });
  }
}
