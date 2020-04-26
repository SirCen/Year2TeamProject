package application.controllers;

import application.models.OrderModel;
import application.models.TableModel;
import application.services.AuthenticationServices;
import application.services.MenuServices;
import application.services.StartServices;
import application.services.WaiterServices;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WaiterController {

  @Autowired
  WaiterServices waiterServices;
  @Autowired
  AuthenticationServices authenticationServices;
  @Autowired
  MenuServices menuServices;
  @Autowired
  StartServices startServices;

  /**
   * RequestMapping for any GET requests coming from the waiterorderpage. Current orders are
   * loaded from the database to be displayed and sent to frontend.
   *
   * @param model     The database model
   * @param request   HTTP request to request information from the page
   * @param response  HTTP response to receive information from the page
   * @return page (html file) to be loaded.
   */
  @RequestMapping(value = "/waiter", method = RequestMethod.GET)
  public String getCurrentOrders(Model model, HttpServletRequest request, HttpServletResponse response) {
    try {
      if (!authenticationServices.authenticateCookies(request, response)) {
        return "redirect:/";
      }
      ArrayList<OrderModel> output = waiterServices.getOrders();
      ArrayList<TableModel> tablesoutput = startServices.getTables();
      model.addAttribute("currentorders", output);
      model.addAttribute("tables", tablesoutput);
      return "waiter";
    } catch (Exception e) {
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
      return "error";
    }
  }

  /**
   * RequestMapping for any POST requests coming from the modify order button on each ticket.
   *
   * @param allParams           Takes the mapping of values from the form/button
   * @param redirectAttributes  Method for passing attributes to a redirect target
   * @param request             HTTP request to request information from the page
   * @param response            HTTP response to receive information from the page
   * @throws SQLException when there is an error in a SQL statement
   * @throws UnsupportedEncodingException when an unsupported encoding method is encountered
   * @throws NoSuchAlgorithmException when no algorithm exists
   * @return page (html file) to be loaded. Needs to redirect to prevent form
   *     post resubmission when reloading the page.
   */
  @RequestMapping(value = "/waiter/modifyorder", method = RequestMethod.POST)
  public String modifyButton(@RequestParam Map<String, String> allParams,
                             RedirectAttributes redirectAttributes,
                             HttpServletRequest request,
                             HttpServletResponse response)
          throws SQLException, UnsupportedEncodingException, NoSuchAlgorithmException {
    if (!authenticationServices.authenticateCookies(request, response)) {
      return "redirect:/";
    }
    String orderId = allParams.get("orderid");
    redirectAttributes.addAttribute("orderId", orderId);
    return "redirect:/modifyorder";
  }

  /**
   * RequestMapping for the modifyorder page. This is run when the page is loaded.
   * @param model   Contains attributes such as order details or an error message.
   * @param orderId Id of the order which is to be modified.
   * @return String (name) of the page/html file to be loaded.
   */
  @RequestMapping(value = "/modifyorder", method = RequestMethod.GET)
  public String getOrder(Model model, @RequestParam(required = false) String orderId) {
    try {
      ArrayList<OrderModel> output = waiterServices.getOrder(orderId);
      model.addAttribute("order", output);
      return "modifyorder";
    } catch (Exception e) {
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
      return "error";
    }
  }

  /**
   * RequestMapping for any POST requests coming from the accept order button on each ticket.
   *
   * @param allParams     Takes the mapping of values from the form/button.
   * @param request       HTTP request to request information from the page
   * @param response      HTTP response to receive information from the page
   * @param stringStaffId Used to store the staff ID in cookies
   * @return page (html file) to be loaded. Needs to redirect to prevent form
   *        post resubmission when reloading the page.
   */
  @RequestMapping(value = "/waiter/accept", method = RequestMethod.POST)
  public String acceptButton(@RequestParam Map<String, String> allParams,
                             HttpServletRequest request,
                             HttpServletResponse response,
                             @CookieValue(value="staffid", defaultValue = "-1")
                                       String stringStaffId) {
    try {
      if (!authenticationServices.authenticateCookies(request, response)) {
        return "redirect:/";
      }
      String orderId = allParams.get("orderid");
      waiterServices.acceptButton(orderId, request, stringStaffId);
      return "redirect:/waiter";
    } catch (Exception e) {
      return "error";
    }
  }

  /**
   * RequestMapping for any POST requests coming from the cancel order button on each ticket.
   *
   * @param allParams Takes the mapping of values from the form/button.
   * @param request   HTTP request to request information from the page
   * @param response  HTTP response to receive information from the page
   * @throws SQLException when there is an error in a SQL statement
   * @throws UnsupportedEncodingException when an unsupported encoding method is encountered
   * @throws NoSuchAlgorithmException when no algorithm exists
   * @return page (html file) to be loaded. Needs to redirect to prevent form
   *        post resubmission when reloading the page.
   */
  @RequestMapping(value = "/waiter/cancel", method = RequestMethod.POST)
  public String cancelButton(@RequestParam Map<String, String> allParams,
                             HttpServletRequest request,
                             HttpServletResponse response)
          throws SQLException, UnsupportedEncodingException, NoSuchAlgorithmException {
    if (!authenticationServices.authenticateCookies(request, response)) {
      return "redirect:/";
    }
    String orderId = allParams.get("orderid");
    waiterServices.cancelButton(orderId);
    return "redirect:/waiter";
  }

  /**
   * RequestMapping for any POST requests coming from the delivered order button on each ticket.
   *
   * @param allParams Takes the mapping of values from the form/button.
   * @param request   HTTP request to request information from the page
   * @param response  HTTP response to receive information from the page
   * @throws SQLException when there is an error in a SQL statement
   * @throws UnsupportedEncodingException when an unsupported encoding method is encountered
   * @throws NoSuchAlgorithmException when no algorithm exists
   * @return page (html file) to be loaded. Needs to redirect to prevent form
   *        post resubmission when reloading the page.
   */
  //NOTE: IN FUTURE THIS MAY NEED TO BE A GENERAL CHANGE STATUS BUTTON
  @RequestMapping(value = "/waiter/delivered", method = RequestMethod.POST)
  public String deliveredButton(@RequestParam Map<String, String> allParams,
                                HttpServletRequest request,
                                HttpServletResponse response)
          throws SQLException, UnsupportedEncodingException, NoSuchAlgorithmException {
    if (!authenticationServices.authenticateCookies(request, response)) {
      return "redirect:/";
    }
    String orderId = allParams.get("orderid");
    waiterServices.deliveredButton(orderId);
    return "redirect:/waiter";
  }

  // NOTE: This method seems redundant - probably need to remove.
  /**
   * RequestMapping for any POST requests coming from the modify order button on each ticket.
   * @param allParams Takes the mapping of values from the form/button.
   * @param request   HTTP request to request information from the page
   * @param response  HTTP response to receive information from the page
   * @throws SQLException when there is an error in a SQL statement
   * @throws UnsupportedEncodingException when an unsupported encoding method is encountered
   * @throws NoSuchAlgorithmException when no algorithm exists
   * @return page (html file) to be loaded. Needs to redirect to prevent form
   *        post resubmission when reloading the page.
   */
  @RequestMapping(value = "/waiter/modify", method = RequestMethod.POST)
  public String modifyButtonTwo(@RequestParam Map<String, String> allParams,
                             HttpServletRequest request,
                             HttpServletResponse response)
          throws SQLException, UnsupportedEncodingException, NoSuchAlgorithmException {
    if (!authenticationServices.authenticateCookies(request, response)) {
      return "redirect:/";
    }
    String orderId = allParams.get("orderid");
    waiterServices.modifyButton(orderId);
    return "redirect:/waiter";
  }

  /**
   * This is called when the waiter presses the dismiss button on a help request notification.
   *
   * @param allParams Values from the form to get tablenum from.
   * @param request   HTTP request to request information from the page
   * @param response  HTTP response to receive information from the page
   * @throws SQLException when there is an error in a SQL statement
   * @throws UnsupportedEncodingException when an unsupported encoding method is encountered
   * @throws NoSuchAlgorithmException when no algorithm exists
   * @return page to be loaded/redirected, in this case the waiter page.
   */
  @RequestMapping(value = "/waiter/resolvedHelpRequest", method = RequestMethod.POST)
  public String resolvedHelpRequest(@RequestParam Map<String, String> allParams,
                                    HttpServletRequest request,
                                    HttpServletResponse response)
          throws SQLException, UnsupportedEncodingException, NoSuchAlgorithmException {
    if (!authenticationServices.authenticateCookies(request, response)) {
      return "redirect:/";
    }
    int tableNum;
    try {
      tableNum = Integer.parseInt(allParams.get("tablenum"));
    } catch (Exception e) {
      e.printStackTrace();
      return "error";
    }
    menuServices.customerNeedsAssistance(tableNum, false);
    return "redirect:/waiter";
  }

  /**
   * This is called when the waiter presses the add button on an order item.
   * @param allParams Contains values from the form submitted, such as quantity.
   * @param request   HTTP request to request information from the page
   * @param response  HTTP response to receive information from the page
   * @throws SQLException when there is an error in a SQL statement
   * @throws UnsupportedEncodingException when an unsupported encoding method is encountered
   * @throws NoSuchAlgorithmException when no algorithm exists
   * @return String (page name) of the page/html file to be loaded.
   */
  @RequestMapping(value = "/modifyorder/add", method = RequestMethod.POST)
  public String add(@RequestParam Map<String, String> allParams,
                    HttpServletRequest request,
                    HttpServletResponse response)
          throws SQLException, UnsupportedEncodingException, NoSuchAlgorithmException {
    if (!authenticationServices.authenticateCookies(request, response)) {
      return "redirect:/";
    }
    String orderItemsId = allParams.get("orderItemsID");
    int quantity = Integer.parseInt(allParams.get("number"));
    waiterServices.addOrderItem(orderItemsId, quantity);
    return "redirect:/waiter";
  }

  /**
   * This is called when the waiter presses the delete button on an order item.
   * @param allParams Contains values from the form submitted, such as quantity.
   * @param request   HTTP request to request information from the page
   * @param response  HTTP response to receive information from the page
   * @throws SQLException when there is an error in a SQL statement
   * @throws UnsupportedEncodingException when an unsupported encoding method is encountered
   * @throws NoSuchAlgorithmException when no algorithm exists
   * @return String (page name) of the page/html file to be loaded.
   */
  @RequestMapping(value = "/modifyorder/deleteButton", method = RequestMethod.POST)
  public String deleteButton(@RequestParam Map<String, String> allParams,
                             HttpServletRequest request,
                             HttpServletResponse response)
          throws SQLException, UnsupportedEncodingException, NoSuchAlgorithmException {
    if (!authenticationServices.authenticateCookies(request, response)) {
      return "redirect:/";
    }
    String orderItemsId = allParams.get("orderItemsID");
    waiterServices.deleteOrderItem(orderItemsId);
    return "redirect:/waiter";
  }
}