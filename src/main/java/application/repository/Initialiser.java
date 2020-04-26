package application.repository;

import application.services.AuthenticationServices;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Initialiser {

  @Autowired
  private Connection connection;

  @Value("${spring.profiles.active}")
  private String activeProfile;

  /**
   * Called when the program starts.
   * Creates a necessary tables in the database.
   * Changes what queries are used based on what spring profile is in use.
   */
  @PostConstruct
  public void init() {
    try {
      Statement stmt = connection.createStatement();
      if (activeProfile.equals("prod")) {  // Postgresql queries
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS menu (menu_id SERIAL PRIMARY KEY, "
            + "name TEXT NOT NULL, "
            + "price REAL NOT NULL, "
            + "description TEXT, "
            + "allergies TEXT, "
            + "calories REAL, "
            + "available INTEGER, "
            + "image TEXT, "
            + "category TEXT"
            + ")");
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS staff (staff_id SERIAL PRIMARY KEY, "
            + "name TEXT NOT NULL, "
            + "password TEXT NOT NULL, "
            + "role TEXT NOT NULL, "
            + "enabled BOOLEAN NOT NULL"
            + ")");
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS orders (order_id SERIAL PRIMARY KEY, "
            + "table_num INTEGER NOT NULL, "
            + "time_ordered TIMESTAMP DEFAULT now(), "
            + "staff_id INTEGER, "
            // id cannot have foreign key constraint as this can be null before accepted by waiter.
            + "status TEXT NOT NULL, "
            + "paid BOOLEAN"
            + ")");
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS order_items ("
            + "order_items_id SERIAL PRIMARY KEY, "
            + "order_id INTEGER REFERENCES orders (order_id), "
            + "menu_id INTEGER REFERENCES menu (menu_id),"
            + "adjustments TEXT NOT NULL, "
            + "status TEXT NOT NULL"
            + ")");
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tables ("
            + "table_id SERIAL PRIMARY KEY, "
            + "size INTEGER, "
            + "needs_assistance BOOLEAN"
            + ")");

      } else if (activeProfile.equals("dev")) { // SQLite queries
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
            + "role TEXT NOT NULL, "
            + "enabled BOOLEAN NOT NULL"
            + ")");
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS orders (order_id INTEGER PRIMARY KEY, "
            + "table_num INTEGER NOT NULL, "
            + "time_ordered DATETIME DEFAULT CURRENT_TIMESTAMP, "
            + "staff_id INTEGER, "
            // id cannot have foreign key constraint as this can be null before accepted by waiter.
            + "status TEXT NULL NULL, "
            + "paid BOOLEAN"
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
      }
      if (!activeProfile.equals("test")) {
        CsvDb reader = new CsvDb(connection);
        reader.insertMenuIfEmpty();
        reader.insertTablesIfEmpty();
      }

      Statement checkStaff = connection.createStatement();
      ResultSet rs = checkStaff.executeQuery("SELECT * FROM staff");
      if (!rs.isBeforeFirst() ) {  // if no user account exists
        // generating hashed password
        String salt = "staffId:1";
        String plainPassword = "admin";
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt.getBytes());
        byte[] hashedPassword = md.digest(plainPassword.getBytes(StandardCharsets.UTF_8));
        String hashString = new String(hashedPassword, StandardCharsets.UTF_8);
        // encoding hash due to cookies no liking the unencoded value;
        String encodedHashString = URLEncoder.encode(hashString, "UTF-8");

        // using hashed password to create default account
        Statement createAdmin = connection.createStatement();
        createAdmin.executeUpdate("INSERT INTO staff (name, password, role, enabled) VALUES "
            + "('admin', '" + encodedHashString + "', 'manager', true)");
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}