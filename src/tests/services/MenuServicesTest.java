package services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import application.models.MenuItemModel;
import application.repository.ConnectionConfig;
import application.repository.DataSourceConfig;
import application.repository.DatabaseTools;
import application.services.MenuServices;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
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
    MenuServices.class})
@ActiveProfiles("test")
public class MenuServicesTest {

  @Autowired
  Connection connection;

  @Autowired
  private MenuServices menuServices;

  @Autowired
  private DatabaseTools databaseTools;

  private int orderID;
  private int tableNum;


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
          + "available INTEGER, "
          + "image TEXT, "
          + "category TEXT"
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
          + "size INTEGER, "
          + "needs_assistance BOOLEAN"
          + ")");
    } catch (Exception e) {
      fail("Cannot create db tables");
    }
    orderID = 43;
    tableNum = 11;
  }

  @Test
  public void getMenuTest() {
    databaseTools.executeUpdate("INSERT INTO menu VALUES (1, 'TEST ITEM 1', 3, "
        + "'TEST DESCRIPTION 1', 'TEST ALLERGIES 1', 3, 1, 'test', 'test')");
    databaseTools.executeUpdate("INSERT INTO menu VALUES (2, 'TEST ITEM 2', 3, "
        + "'TEST DESCRIPTION 2', 'TEST ALLERGIES 2', 3, 1, 'test', 'test')");
    ArrayList<MenuItemModel> testList = new ArrayList<>();
    testList.add(new MenuItemModel(1, "TEST ITEM 1", 3,
        "TEST DESCRIPTION 1", "TEST ALLERGIES 1", 3,
        1, "/food-images/test", "test"));
    testList.add(new MenuItemModel(2, "TEST ITEM 2", 3,
        "TEST DESCRIPTION 2", "TEST ALLERGIES 2", 3, 1,
        "/food-images/test", "test"));
    assertEquals(testList, menuServices.getMenu());
  }

}

