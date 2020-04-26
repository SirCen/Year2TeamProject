package application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseTools {
  @Autowired
  private Connection connection;

  /**
   * Used to execute database queries and obtain an iterable result set.
   * @param query The database query to be executed
   * @return A ResultSet containing the results of the query
   */
  public ResultSet executeQuery(String query) {
    try {
      PreparedStatement statement = connection.prepareStatement(query);
      return statement.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Database update method that takes no value arguments.
   * @param query Update query to be performed
   */
  public void executeUpdate(String query) {
    executeUpdate(query, new String[0]);
  }

  /**
   * Database update method using prepared statements to alter the database.
   * @param query  Update query to be executed
   * @param values Values to be inserted into the query
   */
  public void executeUpdate(String query, String[] values) {
    try {
      PreparedStatement statement = connection.prepareStatement(query);
      for (int i = 0; i < values.length; i++) {
        statement.setString(i + 1, values[i]);
      }
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
