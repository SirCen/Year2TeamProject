package application.models;

import java.util.Objects;

public class TableModel {

  private int id;
  private int size;
  private boolean needsAssistance;

  /**
   * Constructor for TableModel (Holds a single table as an object).
   *
   * @param id   ID of the table
   * @param size Number of seats for the table
   */
  public TableModel(int id, int size) {
    this.id = id;
    this.size = size;
    this.needsAssistance = false;
  }

  /**
   * Constructor for TableModel (Holds a single table as an object).
   *
   * @param id              ID of the table
   * @param size            Number of seats for the table
   * @param needsAssistance Indicator for whether the table needs assistance
   */
  public TableModel(int id, int size, boolean needsAssistance) {
    this.id = id;
    this.size = size;
    this.needsAssistance = needsAssistance;
  }

  public int getId() {
    return id;
  }
  public int getSize() {
    return size;
  }
  public boolean isNeedsassistance() {
    return needsAssistance;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TableModel that = (TableModel) o;
    return id == that.id
        && size == that.size
        && needsAssistance == that.needsAssistance;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, size, needsAssistance);
  }
}
