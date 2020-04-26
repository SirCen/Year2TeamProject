package application.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModifyAvailabilityServices {

  @Autowired
  private Connection connection;

  /**
   * This method handles the changing of the availability of items on the menu.
   * @param menuId The menu id
   * @throws SQLException if there is an error in a SQL statement
   */
  public void changeAvailabilty(String menuId) throws SQLException {
    Statement select = connection.createStatement();
    Statement update = connection.createStatement();
    ResultSet rs = select.executeQuery("SELECT available FROM menu WHERE menu_id = "
        + menuId + " ORDER BY menu_id");
    while (rs.next()) {
      int availability = rs.getInt("available");
      if (availability == 1) {  // item available
        update.executeUpdate("UPDATE menu SET available = 0 WHERE menu_id = " + menuId);
      } else if (availability == 0) {  // item unavailable
        update.executeUpdate("UPDATE menu SET available = 1 WHERE menu_id = " + menuId);
      }
    }
  }
}
