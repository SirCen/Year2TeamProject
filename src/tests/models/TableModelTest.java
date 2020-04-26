package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import application.models.TableModel;
import org.junit.Before;
import org.junit.Test;


/**
 This is the test class for the MenuItemModel.
 */
public class TableModelTest {
  private int id;
  private int size;
  private TableModel tableModelNoAssist;
  private TableModel tableModelAssist;

  /**
   * Setup method using a mock set of entries for testing purposes.
   */
  @Before
  public void setup() {
    id = 2;
    size = 4;
    tableModelNoAssist = new TableModel(id, size);
    tableModelAssist = new TableModel(id, size, true);
  }

  /**
   * Testing the constructor.
   */
  @Test
  public void testTableConstructor() {
    tableModelNoAssist = new TableModel(id, size);
    tableModelAssist = new TableModel(id, size, true);
  }

  /**
   * Testing the getter methods for each entry.
   */
  @Test
  public void testGetId() {
    assertEquals("Correct Id not obtained", id, tableModelNoAssist.getId());
  }

  @Test
  public void testGetSize() {
    assertEquals("Correct size not obtained", size, tableModelNoAssist.getSize());
  }

  @Test
  public void testGetAssist() {
    assertEquals("Correct assistance status not obtained",
        false, tableModelNoAssist.isNeedsassistance());
    assertEquals("Correct assistance status not obtained",
        true, tableModelAssist.isNeedsassistance());
  }

  /**
   * Testing the equals method with an identical object and a differing object.
   */
  @Test
  public void testEquals() {
    TableModel tableModel1 = new TableModel(id, 2, true);
    TableModel tableModel2 = new TableModel(id, 2);
    TableModel tableModel3 = new TableModel(id, size);
    assertNotEquals("Models are equal", tableModelNoAssist, tableModel1);
    assertNotEquals("Models are equal", tableModelNoAssist, tableModel2);
    assertEquals("Models are unequal", tableModelNoAssist, tableModel3);
  }
}