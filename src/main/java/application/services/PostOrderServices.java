package application.services;

import application.models.OrderItemModel;
import application.models.PostOrderModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostOrderServices {

  @Autowired
  private Connection connection;

  /**
   * This method gets the orders from the orders table to display on the post order menu.
   * @param tableNum The table number
   * @return the orders to be retrieved
   * @throws SQLException if there is an error in a SQL statement
   */
  public ArrayList<OrderItemModel> getOrders(int tableNum) throws SQLException {
    ArrayList<OrderItemModel> output = new ArrayList<>();
    Statement orderStatement = connection.createStatement();
    ResultSet orderSet = orderStatement.executeQuery("SELECT * FROM order_items, menu, orders "
            + "WHERE order_items.menu_id=menu.menu_id "
            + "AND orders.order_id=order_items.order_id "
            + "AND orders.table_num=" + tableNum);
    OrderItemModel order;
    while (orderSet.next()) {
      order = new OrderItemModel(orderSet.getInt("order_items_id"), 
              orderSet.getInt("order_id"), 
              orderSet.getInt("menu_id"), 
              orderSet.getString("adjustments"), 
              orderSet.getString("status"),
              orderSet.getString("name"));
      output.add(order);
    }
    return output;
  }

  /**
   * This method gets the order info from the orders table to display on the post order menu.
   * @param tableNum The table number
   * @return the orders to be retrieved
   * @throws SQLException if there is an error in a SQL statement
   */
  public ArrayList<PostOrderModel> getOrderInfo(int tableNum) throws SQLException {
    ArrayList<PostOrderModel> info = new ArrayList<>();
    Statement orderStatement = connection.createStatement();
    ResultSet orderSet = orderStatement.executeQuery("SELECT DISTINCT orders.order_id, "
        + "orders.status, orders.table_num, orders.paid FROM order_items, menu, orders "
        + "WHERE order_items.menu_id=menu.menu_id "
        + "AND orders.order_id=order_items.order_id "
        + "AND orders.table_num=" + tableNum);
    PostOrderModel order = null;
    while (orderSet.next()) {
      order = new PostOrderModel(orderSet.getInt("order_id"), 
              orderSet.getString("status"),
              orderSet.getInt("table_num"),
              orderSet.getBoolean("paid"));
      info.add(order);
    }
    return info;
  }

  /**
   * Sets an order paid status to true.
   * @param order_id The ID of the order to be set to paid
   */
  public void setPaid(int order_id) {
    try {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("UPDATE orders SET paid=true WHERE order_id = " + order_id + ";");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
