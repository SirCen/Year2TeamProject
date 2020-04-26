package application.services;

import application.models.KitchenItemModel;
import application.models.OrderItemModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class KitchenServices {
  @Autowired
  private Connection connection;

  @Autowired
  TimeServices timeServices;

  /**
   * RequestMapping for any post requests coming from the kitchenorders.
   * This sets the status of an item to 'ready' and changes the status of a whole order
   *     to ready if all items inside it are ready.
   * @param allParams identifier for whether the order is ready or not
   * @return page (html file) to be loaded. Needs to redirect to prevent form
   *      post resubmission when reloading the page.
   */
  public String orderReady(@RequestParam Map<String, String> allParams) {
    String orderitemsid = "";

    if (allParams.get("ready") != null) {
      orderitemsid = allParams.get("ready");
      try {
        Statement ready = connection.createStatement();
        ready.executeUpdate("UPDATE order_items SET status='ready' "
            + "WHERE order_items_id = " + orderitemsid + ";");

        // Get the OrderID from the orderitemid
        Statement orderidstatement = connection.createStatement();
        ResultSet rs = orderidstatement.executeQuery("SELECT order_id FROM order_items WHERE "
            + "order_items_id = " + orderitemsid + ";");
        int orderid = -1; // Defaults to -1
        while (rs.next()) {
          orderid = rs.getInt("order_id");
        }

        // Check if all items in an order are ready
        String currentStatus = "ready";
        Statement orderready = connection.createStatement();
        ResultSet rs2 = orderready.executeQuery("SELECT status FROM order_items WHERE "
            + "order_id = " + orderid + ";");
        while (rs2.next()) {
          String status = rs2.getString("status");
          if (!(status.equals("ready")) && currentStatus.equals("ready")) {
            currentStatus = "accepted";
            break;
          }
        }
        // If the whole order is ready, update the status
        if (currentStatus.equals("ready")) {
          Statement setNewStatus = connection.createStatement();
          setNewStatus.executeUpdate("UPDATE orders SET status='ready' WHERE "
              + "order_id = " + orderid + ";");
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return "redirect:/kitchenorders";
  }

  /**
   * This methods gets the current orders from the order items table for display on the
   * kitchen page.
   *
   * @return The current orders, items and modifications, and their status.
   * @throws SQLException if there is an SQL error
   */
  public ArrayList<KitchenItemModel> getCurrentOrders() throws SQLException {
    Statement stmt = connection.createStatement();
    ResultSet orderItems = stmt.executeQuery("SELECT * FROM order_items, menu, orders "
        + "WHERE order_items.menu_id=menu.menu_id "
        + "AND order_items.order_id=orders.order_id "
        + "AND order_items.status='ordered'"
        + "AND orders.status='accepted'"
        + "ORDER BY order_items.order_items_id");

    ArrayList<KitchenItemModel> output = new ArrayList<>();
    KitchenItemModel kitchenItemModel;
    OrderItemModel itemModel;
    while (orderItems.next()) {
      // adjustments could be null so this is checked for
      String adjustments = "";
      if (orderItems.getObject("adjustments") == null) {
        adjustments = "None";
      } else {
        adjustments = orderItems.getString("adjustments");
      }

      // making order_item model
      itemModel = new OrderItemModel(orderItems.getInt("order_items_id"),
          orderItems.getInt("order_id"),
          orderItems.getInt("menu_id"),
          adjustments,
          orderItems.getString("status"),
          orderItems.getString("name"));

      // making kitchen item model
      kitchenItemModel = new KitchenItemModel(orderItems.getInt("table_num"),
          orderItems.getInt("order_id"),
          timeServices.convertTime(orderItems.getObject("time_ordered").toString()),
          itemModel);

      output.add(kitchenItemModel);
    }
    return output;
  }
}
