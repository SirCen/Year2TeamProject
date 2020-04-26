package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import application.models.MenuItemModel;
import org.junit.Before;
import org.junit.Test;


/**
 This is the test class for the MenuItemModel.
 */
public class MenuItemModelTest {
  int menuId;
  String name;
  float price;
  String description;
  String allergies;
  int calories;
  int available;
  String image;
  String category;
  private MenuItemModel menuItemModel;

  /**
   * Setup method using a mock set of entries for testing purposes.
   */
  @Before
  public void setup() {
    menuId = 2;
    name = "chicken";
    price = 5.40f;
    description = "our finest meat";
    allergies = "soy";
    calories = 500;
    available = 1;
    image = "test";
    category = "test";
    menuItemModel = new MenuItemModel(menuId, name, price, description, allergies,
        calories, available, image, category);
  }

  /**
   * Testing the constructor.
   */
  @Test
  public void testMenuItemConstructor() {
    new MenuItemModel(menuId, name, price, description, allergies,
            calories, available, image, category);
  }

  /**
   * Testing the getter methods for each entry.
   */
  @Test
  public void testGetMenuId() {
    assertEquals("Correct Menu ID not obtained", menuId, menuItemModel.getMenu_id());
  }

  @Test
  public void testGetName() {
    assertEquals("Correct name not obtained", name, menuItemModel.getName());
  }

  @Test
  public void testGetPrice() {
    assertEquals("Correct price not obtained", price, menuItemModel.getPrice(), 0.0);
  }

  @Test
  public void testGetDescription() {
    assertEquals("Correct description not obtained", description,
        menuItemModel.getDescription());
  }

  @Test
  public void testGetAllergies() {
    assertEquals("Correct allergies not obtained", allergies,
        menuItemModel.getAllergies());
  }

  @Test
  public void testGetCalories() {
    assertEquals("Correct calories not obtained", calories, menuItemModel.getCalories());
  }

  @Test
  public void testGetAvailability() {
    assertEquals("Correct availability not obtained", available,
        menuItemModel.getAvailable());
  }

  /**
   * Testing the equals method with an identical object and a differing object.
   */
  @Test
  public void testEquals() {
    MenuItemModel menuItemModel2 = new MenuItemModel(menuId, "beef", price,
        "our worst meat", allergies, 600, available, image, category);
    MenuItemModel menuItemModel3 = new MenuItemModel(menuId, name, price,
        description, allergies, calories, available, image, category);
    assertNotEquals("Models are equal", menuItemModel, menuItemModel2);
    assertEquals("Models are unequal", menuItemModel, menuItemModel3);
  }
}

