package application.repository;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CsvDb {
  private CSVReaderHeaderAware reader;
  private ArrayList<HashMap<String, String>> values;
  private Connection connection;

  public CsvDb(Connection incomingConnection) {
    connection = incomingConnection;
  }

  private void readCsv(String file) throws IOException, CsvValidationException {
    reader = new CSVReaderHeaderAware(new FileReader(file));
    values = new ArrayList<>();
    Map<String, String> value;
    while ((value = reader.readMap()) != null) {
      values.add((HashMap<String, String>) value);
    }
  }

  /**
   * Method to insert data from the csv into the menu table.
   * @throws SQLException if there is an error with the SQL query
   * @throws IOException if there is an error with the input
   * @throws CsvValidationException if there is an error with the CSV
   */
  public void insertMenu()
      throws SQLException, IOException, CsvValidationException {
    this.readCsv("menu.csv");
    Statement insert = connection.createStatement();
    for (HashMap<String, String> entry : values) {
      insert.executeUpdate("INSERT INTO menu ("
          + "menu_id,name,price,description,allergies,calories,available,image,category"
          + ") "
          + "VALUES ("
          + entry.get("menu_id") + ", "
          + "'" + entry.get("name") + "', "
          + entry.get("price") + ", "
          + "'" + entry.get("description") + "', "
          + "'" + entry.get("allergies") + "', "
          + entry.get("calories") + ", "
          + entry.get("available") + ", "
          + "'" + entry.get("image") + "', "
          + "'" + entry.get("category") + "'"
          + ")");
    }
  }

  /**
   * Method to make sure data is only inserted if it does not yet exist.
   * @throws SQLException if there is an error with the SQL query
   * @throws IOException if there is an error with the input
   * @throws CsvValidationException if there is an error with the CSV
   */
  public void insertMenuIfEmpty()
      throws SQLException, IOException, CsvValidationException {
    Statement check = connection.createStatement();
    ResultSet rs = check.executeQuery("SELECT * FROM menu");
    if (!rs.next()) {
      this.insertMenu();
    }
  }

  /**
   * Method to insert data from the csv into the tables table.
   * @throws SQLException if there is an error with the SQL query
   * @throws IOException if there is an error with the input
   * @throws CsvValidationException if there is an error with the CSV
   */
  public void insertTables()
          throws SQLException, IOException, CsvValidationException {
    this.readCsv("tables.csv");
    Statement insert = connection.createStatement();
    for (HashMap<String, String> entry : values) {
      insert.executeUpdate("INSERT INTO tables ("
              + "table_id,size,needs_assistance"
              + ") "
              + "VALUES ("
              + entry.get("table_id") + ", "
              + entry.get("size") + ", "
              + entry.get("needs_assistance")
              + ")");
    }
  }

  /**
   * Method to make sure data is only inserted if it does not yet exist.
   * @throws SQLException if there is an error with the SQL query
   * @throws IOException if there is an error with the input
   * @throws CsvValidationException if there is an error with the CSV
   */
  public void insertTablesIfEmpty()
          throws SQLException, IOException, CsvValidationException {
    Statement check = connection.createStatement();
    ResultSet rs = check.executeQuery("SELECT * FROM tables");
    if (!rs.next()) {
      this.insertTables();
    }
  }
}