package application.services;

import application.models.TableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StartServices {
  @Autowired
  private Connection connection;

  /**
   * This method is used to set if a table needs assistance or not.
   * @param tableId The table id
   * @return the boolean to determine whether the table needs assistance
   */
  public boolean table_needs_assistance(int tableId) {
    try {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT needs_assistance FROM tables WHERE table_id = "
          + tableId + ";");
      while (rs.next()) {
        return rs.getBoolean("needs_assistance");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * This method gets all the information from the menu and outputs it for use on the webpage.
   *
   * @return the full menu
   */
  public ArrayList<TableModel> getTables() {
    ArrayList<TableModel> output = new ArrayList<>();
    try {
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM tables ORDER BY table_id");
      TableModel data;
      while (rs.next()) {
        data = new TableModel(rs.getInt("table_id"),
            rs.getInt("size"),
            rs.getBoolean("needs_assistance"));
        output.add(data);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return output;
  }
}
