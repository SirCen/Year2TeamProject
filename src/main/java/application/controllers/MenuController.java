package application.controllers;

import application.EmptyOrderException;
import application.TooManyItemsException;
import application.models.MenuItemModel;
import application.services.MenuServices;
import application.services.StartServices;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class MenuController {

  @Autowired
  MenuServices menuServices;
  @Autowired
  StartServices startServices;

  /**
   * Called when the user visits /.
   * Displays index.html
   *
   * @param model       Model used to store menu items
   * @param tableCookie Cookie used to store the selected table
   * @return page (html file) to be loaded.
   */
  @RequestMapping(value = "menu", method = RequestMethod.GET)
  public String menu(Model model, @CookieValue(value = "table", defaultValue = "-1") String tableCookie) {
    if (tableCookie.equals("-1")) {  // if table has default value
      return "redirect:/";
    }
    int tableNum = Integer.parseInt(tableCookie);
    model.addAttribute("table_number", tableNum);
    boolean tableNeedsAssistance = startServices.table_needs_assistance(tableNum);
    model.addAttribute("table_needs_assistance", tableNeedsAssistance);
    ArrayList<MenuItemModel> output = menuServices.getMenu();
    model.addAttribute("menu_items", output);
    return "menu";
  }

  /**
   * Called when the user adds or removes a particular item from their order.
   * Creates a post request.
   * This calls the backend code to add or remove an item from the method.
   *
   * @param menuCounter The menuCounter map for the method
   * @return page (html file) to be loaded.
   */
  @RequestMapping(value = "menu", method = RequestMethod.POST)
  public String counterRequest(@RequestParam Map<String, String> menuCounter) {
    return "menu";
  }

  /**
   * This method is called when the submit order button is pressed, it assigns an orderId
   * and calls the controller to handle what it has received from the frontend.
   * @param order         The order.
   * @param stringStaffId String containing staffId
   * @param tableCookie   String containing user's table number
   * @return The redirection to the appropriate page.
   */
  @RequestMapping(value = "/menu/submitOrder", method = RequestMethod.POST, consumes =
      "application/json")
  @ResponseBody
  public String submitOrder(@RequestBody String order,
                            @CookieValue(value = "staffid", defaultValue = "0") String stringStaffId,
                            @CookieValue(value = "table", defaultValue = "-1") String tableCookie) {
    if (tableCookie.equals("-1")) {  // if table has default value
      return "redirect:/";
    }
    int tableNum;
    try {
      tableNum = Integer.parseInt((String) tableCookie);
    } catch (Exception e) {
      e.printStackTrace();
      return "error";
    }
    int staffId = 0;
    try {
      staffId = Integer.parseInt(stringStaffId);
    } catch (Exception e) {
      staffId = 0;
      System.out.println("Couldn't parse the id.");
    }
    menuServices.orderToDatabase(tableNum, staffId);
    menuServices.jsonToDatabase(tableNum, order);
    return "redirect:/postorder";
  }

  /**
   * This method is called when the help button is pressed on the menu.
   * @param allParams Values from the form to get tablenum from.
   * @return redirection to the appropriate page.
   */
  @RequestMapping(value = "/menu/needsAssistance", method = RequestMethod.POST)
  public String needsAssistance(@RequestParam Map<String,String> allParams) {
    int tableNum;
    try {
      tableNum = Integer.parseInt(allParams.get("tablenum"));
    } catch (Exception e) {
      e.printStackTrace();
      return "error";
    }
    menuServices.customerNeedsAssistance(tableNum, true);
    return "redirect:/menu"; // Possibly should be 'help is on the way' screen
  }
}
