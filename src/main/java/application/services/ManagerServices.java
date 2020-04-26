package application.services;

import application.BadConfirmPasswordException;
import application.models.StaffModel;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Services for the manager controller.
 */
@Service
public class ManagerServices {
  @Autowired
  private Connection connection;

  @Autowired
  AuthenticationServices authenticationServices;

  /**
   * Creates a new user
   *
   * @param name      The name of the user
   * @param password  The password of the user
   * @param password2 The second password to confirm they are the same
   * @param role      The role of the user
   * @throws SQLException if there is an error in a SQL statement
   * @throws BadConfirmPasswordException the passwords were not the same exception
   * @throws UnsupportedEncodingException if an unsupported encoding method is encountered
   * @throws NoSuchAlgorithmException if no algorithm exists
   * @return the ID of the user
   */
  public int addUser(String name, String password, String password2, String role)
      throws SQLException, BadConfirmPasswordException,
      UnsupportedEncodingException, NoSuchAlgorithmException {
    if (!password.equals(password2)) {
      throw new BadConfirmPasswordException();
    }
    PreparedStatement addUser = connection.prepareStatement("INSERT INTO staff (name, password,"
        + " role, enabled) VALUES (?, ?, ?, true)");
    addUser.setString(1, name);
    addUser.setString(2, password);
    addUser.setString(3, role);
    addUser.executeUpdate();
    Statement getUser = connection.createStatement();
    ResultSet rs = getUser.executeQuery("SELECT staff_id FROM staff ORDER BY staff_id DESC LIMIT"
        + " 1");
    int id = 0;
    while (rs.next()) {
      id = rs.getInt("staff_id");
    }

    String hashedPassword = authenticationServices.generateHash(password, "staffId:" + id);

    PreparedStatement updatePassword =
        connection.prepareStatement("UPDATE staff SET password=? WHERE staff_id=?");
    updatePassword.setString(1, hashedPassword);
    updatePassword.setInt(2, id);
    updatePassword.executeUpdate();

    return id;
  }

  /**
   * Gets a list of users.
   *
   * @throws SQLException if there is an error in a SQL statement
   * @return list of users
   */
  public LinkedList<StaffModel> getUsers() throws SQLException {
    Statement getUsers = connection.createStatement();
    ResultSet rs = getUsers.executeQuery("SELECT * FROM staff ORDER BY staff_id");
    LinkedList<StaffModel> output = new LinkedList<StaffModel>();
    StaffModel user;
    while (rs.next()) {
      user = new StaffModel(rs.getInt("staff_id"),
          rs.getString("name"),
          rs.getString("password"),
          rs.getString("role"),
          rs.getBoolean("enabled"));
      output.add(user);
    }
    return output;
  }

  /**
   * Changes the name of a user.
   *
   * @param id   The id of the user
   * @param name The new name
   * @throws SQLException if there is an error in a SQL statement
   */
  public void changeName(String id, String name) throws SQLException {
    PreparedStatement changeName =
        connection.prepareStatement("UPDATE staff SET name=? WHERE staff_id=?");
    changeName.setString(1, name);
    changeName.setInt(2, Integer.parseInt(id));
    changeName.executeUpdate();
  }

  /**
   * Changes the password of the user.
   *
   * @param id        The id of the user
   * @param password  The new password of the user
   * @param password2 Another password for confirmation
   * @throws SQLException if there is an error in a SQL statement
   * @throws BadConfirmPasswordException the passwords were not the same exception
   * @throws UnsupportedEncodingException if an unsupported encoding method is encountered
   * @throws NoSuchAlgorithmException if no algorithm exists
   */
  public void changePassword(String id, String password, String password2)
      throws SQLException, BadConfirmPasswordException, UnsupportedEncodingException, NoSuchAlgorithmException {
    if (!password.equals(password2)) {
      throw new BadConfirmPasswordException();
    }
    String hashedPassword = authenticationServices.generateHash(password, "staffId:" + id);
    PreparedStatement changePassword =
        connection.prepareStatement("UPDATE staff SET password=? WHERE staff_id=?");
    changePassword.setString(1, hashedPassword);
    changePassword.setInt(2, Integer.parseInt(id));
    changePassword.executeUpdate();
  }

  /**
   * Change the enabled status of a user.
   *
   * @param id      The id of the user
   * @param enabled Whether the user should be enabled or disabled
   * @throws SQLException if there is an error in a SQL statement
   */
  public void changeEnabled(String id, String enabled) throws SQLException {
    PreparedStatement changeEnabled =
        connection.prepareStatement("UPDATE staff SET enabled=? WHERE staff_id=?");
    changeEnabled.setBoolean(1, Boolean.valueOf(enabled.toLowerCase()));
    changeEnabled.setInt(2, Integer.parseInt(id));
    changeEnabled.executeUpdate();
  }
}
