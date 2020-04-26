package services;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import application.repository.ConnectionConfig;
import application.repository.DataSourceConfig;
import application.repository.DatabaseTools;
import application.services.AuthenticationServices;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootApplication(scanBasePackages = "application")
@SpringBootTest(classes = {ConnectionConfig.class, DataSourceConfig.class, DatabaseTools.class,
    AuthenticationServices.class})
@ActiveProfiles("test")
public class AuthenticationServicesTest {

  @Autowired
  private Connection connection;
  @Autowired
  private AuthenticationServices authenticationServices;

  /**
   * Setup method using a mock order for testing purposes.
   */
  @Before
  public void setup() {
    try {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("DROP TABLE IF EXISTS menu");
      stmt.executeUpdate("DROP TABLE IF EXISTS waiter");
      stmt.executeUpdate("DROP TABLE IF EXISTS orders");
      stmt.executeUpdate("DROP TABLE IF EXISTS order_items");
      stmt.executeUpdate("DROP TABLE IF EXISTS tables");
      stmt.executeUpdate("DROP TABLE IF EXISTS sessions");
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS menu (menu_id INTEGER PRIMARY KEY, "
          + "name TEXT NOT NULL, "
          + "price FLOAT NOT NULL, "
          + "description TEXT, "
          + "allergies TEXT, "
          + "calories FLOAT, "
          + "available INTEGER"
          + ")");
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS staff (staff_id INTEGER PRIMARY KEY, "
              + "name TEXT NOT NULL, "
              + "password TEXT NOT NULL, "
              + "role TEXT NOT NULL"
              + ")");
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS orders (order_id INTEGER PRIMARY KEY, "
          + "table_num INTEGER NOT NULL, "
          + "time_ordered DATETIME DEFAULT CURRENT_TIMESTAMP, "
          + "staff_id INTEGER, "
          // id cannot have foreign key constraint as this can be null before accepted by waiter.
          + "status TEXT NULL NULL"
          + ")");
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS order_items ("
          + "order_items_id INTEGER PRIMARY KEY, "
          + "order_id INTEGER REFERENCES orders (order_id), "
          + "menu_id INTEGER REFERENCES menu (menu_id),"
          + "adjustments TEXT NOT NULL, "
          + "status TEXT NOT NULL"
          + ")");
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tables ("
          + "table_id INTEGER PRIMARY KEY, "
          + "size INTEGER"
          + ")");
    } catch (Exception e) {
      e.printStackTrace();
      fail("Cannot create db tables");
    }
  }

//  @Test
//  public void testOne() throws SQLException {
//    Statement stmt = connection.createStatement();
//    stmt.executeUpdate("INSERT INTO waiter (waiter_id, name, pin) VALUES (1, 'test', 1234)");
//    assertTrue(authenticationServices.checkCred("test", "1234"));
//    assertFalse(authenticationServices.checkCred("test", "4321"));
//    assertFalse(authenticationServices.checkCred("tset", "1234"));
//  }

//  @Test
//  public void testTwo() throws SQLException {
//    assertTrue(authenticationServices.checkIfExpired("2020-03-10 23:54:33"));
//  }
}