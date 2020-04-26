package application.services;

import application.models.OrderItemModel;
import application.models.OrderModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;

@Service
public class WaiterServices {

  @Autowired
  private Connection connection;

  @Autowired
  TimeServices timeServices;

  @Autowired
  AuthenticationServices authenticationServices;

  /**
   * acceptButton method which runs when the accept button is pressed in an order ticket.
   * @param orderID       The ID of the order, passed from the Controller
   * @param request       HTTP request for requesting information from the page
   * @param stringStaffId ID of the staff member as a string
   */
  public void acceptButton(String orderID, HttpServletRequest request, String stringStaffId) {
    try {
      int staffId = Integer.parseInt(stringStaffId);
      Statement accept = connection.createStatement();
      if (staffId > 0) {
        String waiterIdForQuery = Integer.toString(staffId);
        accept.executeUpdate("UPDATE orders SET staff_id=" + waiterIdForQuery
            + ", status='accepted'" + "WHERE order_ID = " + orderID + ";");
      } else {
        System.out.println("The waiter wasn't logged in/didn't have a session.");
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * cancelButton method which runs when the cancel button is pressed in an order ticket.
   * @param orderID The ID of the order, passed from the Controller
   */
  public void cancelButton(String orderID) {
    try {
      Statement remove = connection.createStatement();
      remove.executeUpdate("DELETE FROM order_items WHERE order_id=" + orderID + ";");
      remove.executeUpdate("DELETE FROM orders WHERE order_ID=" + orderID + ";");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // NOTE: This method seems redundant - probably need to remove.
  /**
   * modifyButton method which runs when the modify button is pressed in an order ticket.
   * @param orderID The ID of the order, passed from the Controller
   * @throws SQLException if there is an error in a SQL statement
   * @return redirect to the page for modifying orders
   */
  public String modifyButton(String orderID) throws SQLException {
    getOrder(orderID);
    //Take to modify order page and then from that page take them back to waiter order page.
    return "redirect:/modifyOrder";
  }

  /**
   * deliveredButton which runs when the delivered button is pressed in an order ticket.
   * @param orderID The ID of the order, passed from the Controller
   */
  public void deliveredButton(String orderID) {
    try {
      Statement accept = connection.createStatement();
      accept.executeUpdate("UPDATE orders SET status = 'delivered' WHERE order_id = "
          + orderID);
      accept.executeUpdate("UPDATE order_items SET status = 'delivered' "
          + "WHERE order_id = " + orderID);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * This is the getOrder method. This method will be used to the items of a given order and add
   * them to an output.
   * @param orderId The ID of the order
   * @return an array list of ordermodels
   * @throws SQLException if there is an error in a SQL statement
   */
  public ArrayList<OrderModel> getOrder(String orderId) throws SQLException {
    ArrayList<OrderModel> output = new ArrayList<>();
    ArrayList<OrderItemModel> items = new ArrayList<>();
    Statement orderStmt = connection.createStatement();
    ResultSet order = orderStmt.executeQuery("SELECT * FROM orders WHERE order_id = "
        + orderId + ";");
    Statement orderName = connection.createStatement();
    while (order.next()) {
      OrderModel orderModel;
      ResultSet names = orderName.executeQuery("SELECT * FROM order_items, menu "
          + "WHERE order_items.menu_id = menu.menu_id "
          + "AND order_id = " + order.getInt("order_id"));
      while (names.next()) {
        String adjustments = "";
        OrderItemModel itemModel = new OrderItemModel(names.getInt("order_items_id"),
            order.getInt("order_id"),
            names.getInt("menu_id"),
            adjustments,
            order.getString("status"),
            names.getString("name"));
        items.add(itemModel);
      }
      orderModel = new OrderModel(order.getInt("order_id"),
          order.getInt("table_num"),
          order.getString("time_ordered"),
          order.getInt("staff_id"),
          order.getString("status"),
          order.getBoolean("paid"),
          items);
      output.add(orderModel);
    }
    return output;
  }

  /**
   * This method gets the orders from the order_items table, which is filled from the menu
   * page, so they can be seen on the waiter order page.
   * @return the orders to send to the front end
   * @throws SQLException if there is an error in a SQL statement
   */
  public ArrayList<OrderModel> getOrders() throws SQLException {
    // List that will be passed to front end
    ArrayList<OrderModel> output = new ArrayList<>();

    // Statements need to be different to avoid overwriting
    // queries being processed at the same time
    Statement orderStmt = connection.createStatement();
    Statement orderItemsStmt = connection.createStatement();
    // Query to select overall order
    ResultSet allOrders = orderStmt.executeQuery("SELECT * FROM orders ORDER BY order_id");
    // Query to select all items from an order
    ResultSet orderItems;

    while (allOrders.next()) {
      orderItems = orderItemsStmt.executeQuery("SELECT * FROM order_items, menu "
        + "WHERE order_items.menu_id = menu.menu_id "
        + "AND order_id = " + allOrders.getString("order_id"));

      ArrayList<OrderItemModel> items = new ArrayList<>();
      while (orderItems.next()) {
        OrderItemModel itemModel = new OrderItemModel(orderItems.getInt("order_items_id"),
                orderItems.getInt("order_id"),
                orderItems.getInt("menu_id"),
                orderItems.getString("adjustments"),
                orderItems.getString("status"),
                orderItems.getString("name"));
        items.add(itemModel);
      }

      // waiter_id could be null so this is checked for
      String staff_id = "";
      if (allOrders.getObject("staff_id") == null) {
        staff_id = "None";
      } else {
        staff_id = allOrders.getObject("staff_id").toString();
      }
      OrderModel orderModel;
      orderModel = new OrderModel(allOrders.getInt("order_id"),
              allOrders.getInt("table_num"),
              timeServices.convertTime(allOrders.getObject("time_ordered").toString()),
              allOrders.getInt("staff_id"),
              allOrders.getString("status"),
              allOrders.getBoolean("paid"),
              items);

      if (!allOrders.getObject("status").toString().equals("delivered")) {
        output.add(orderModel);
      }
    }
    return output;
  }

  /**
   * Logic for the delete button to delete an item from an order.
   * @param orderItemsId The order items id
   * @throws SQLException if there is an error in a SQL statement
   */
  public void deleteOrderItem(String orderItemsId) throws SQLException {
    //the statement/query is used to get the orderId of the orderItem before the deletion is
    //carried out. Needed in case the order needs to be deleted in the orders table.
    Statement getOrderId = connection.createStatement();
    ResultSet orderId = getOrderId.executeQuery("SELECT order_id FROM order_items WHERE "
        + "order_items_id=" + orderItemsId + ";");
    //Statement/query needed for the deletion of an order item.
    Statement delete = connection.createStatement();
    delete.executeUpdate("DELETE FROM order_items WHERE order_items_id='" + orderItemsId + "';");
    //statement/query needed to check to see if there aren't an items in the table left with the
    //order id from the first query.
    //statement/query which gets items from order_items_id for a particular order_id
    Statement items = connection.createStatement();
    ResultSet allItems = items.executeQuery("SELECT * FROM order_items WHERE order_id="
        + orderId.getString(1));
    int size = 0;
    while (allItems.next()) {
      size++;
    }
    if (size == 0) {
      Statement deleteOrder = connection.createStatement();
      deleteOrder.executeUpdate("DELETE FROM orders WHERE order_id=" + orderId.getString(1));
    }
  }

  /**
   * Logic for the addition button to add an item to an order.
   * @param orderItemsId The order items id
   * @param quantity     The quantity
   * @throws SQLException if there is an error in a SQL statement
   */
  public void addOrderItem(String orderItemsId, int quantity) throws SQLException {
    Statement getOrderId = connection.createStatement();
    ResultSet orderId = getOrderId.executeQuery("SELECT order_id FROM order_items WHERE "
        + "order_items_id=" + orderItemsId + ";");
    Statement getMenuId = connection.createStatement();
    ResultSet menuId = getMenuId.executeQuery("SELECT menu_id FROM order_items WHERE "
        + "order_items_id=" + orderItemsId + ";");
    while (menuId.next()) {
      for (int i = 0; i <= quantity - 1; i++) {
        Statement add = connection.createStatement();
        add.executeUpdate("INSERT INTO order_items (order_id, menu_id, adjustments, status) VALUES ("
            + orderId.getString(1) + ", " + menuId.getString(1) + ", 'none', 'ordered');");
      }
    }
  }
}