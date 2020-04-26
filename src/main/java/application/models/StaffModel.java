package application.models;

/**
 * The Staff model.
 */
public class StaffModel {
  private int staffId;
  private String name;
  private String password;
  private String role;
  private boolean enabled;

  /**
   * Instantiates a new Staff model.
   *
   * @param staffId  The staff id
   * @param name     The name of the staff
   * @param password The password of the user
   * @param role     The role of the user
   * @param enabled  Whether the user is enabled or not
   */
  public StaffModel(int staffId, String name, String password, String role, boolean enabled) {
    this.staffId = staffId;
    this.name = name;
    this.password = password;
    this.role = role;
    this.enabled = enabled;
  }

  /**
   * Gets staff id.
   *
   * @return the staff id
   */
  public int getStaffId() {
    return staffId;
  }

  /**
   * Gets name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Gets password.
   *
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * Gets role.
   *
   * @return the role
   */
  public String getRole() {
    return role;
  }

  /**
   * Gets whether the user is enabled or not.
   *
   * @return the boolean
   */
  public boolean getEnabled() {
    return enabled;
  }
}
