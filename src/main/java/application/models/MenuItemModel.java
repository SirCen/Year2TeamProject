package application.models;

public class MenuItemModel {
  private int menuId;
  private String name;
  private float price;
  private String description;
  private String allergies;
  private int calories;
  private int available;
  private String image;
  private String category;

  /**
   * The model for a menu item.
   * @param menuId      The menu id
   * @param name        The name of the item
   * @param price       The price of the item
   * @param description The description of the item
   * @param allergies   The allergies that item may trigger
   * @param calories    The calories in that item
   * @param available   The availability of that item
   * @param image       The image of the item
   * @param category    The category that item belongs to
   */
  public MenuItemModel(int menuId, String name, float price, String description, String allergies,
                       int calories, int available, String image, String category) {
    this.menuId = menuId;
    this.name = name;
    this.price = price;
    this.description = description;
    this.allergies = allergies;
    this.calories = calories;
    this.available = available;
    this.image = image;
    this.category = category;
  }

  public int getMenu_id() {
    return menuId;
  }

  public String getName() {
    return name;
  }

  public float getPrice() {
    return price;
  }

  public String getDescription() {
    return description;
  }

  public String getAllergies() {
    return allergies;
  }

  public int getCalories() {
    return calories;
  }

  public int getAvailable() {
    return available;
  }

  public String getImage() {
    return "/food-images/" + image;
  }

  public String getCategory() {
    return category;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MenuItemModel that = (MenuItemModel) o;
    return menuId == that.menuId
        && Float.compare(that.price, price) == 0
        && calories == that.calories
        && available == that.available
        && name.equals(that.name)
        && description.equals(that.description)
        && allergies.equals(that.allergies);
  }
}
