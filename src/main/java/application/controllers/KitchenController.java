package application.controllers;

import application.models.KitchenItemModel;
import application.services.AuthenticationServices;
import application.services.KitchenServices;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KitchenController {
  @Autowired
  KitchenServices kitchenServices;

  @Autowired
  AuthenticationServices authenticationServices;

  /**
   * This RequestMapping method gets the orders from the database for the kitchen staff.
   * @param model     The database model
   * @param request   HTTP request for requesting information from the page
   * @param response  HTTP response for receiving information from the page
   * @return A string of the kitchen orders
   */
  @RequestMapping("/kitchen")
  public String getKitchenOrders(Map<String, Object> model,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
    try {
      if (!authenticationServices.authenticateCookies(request, response)) {
        return "redirect:/";
      }
      ArrayList<KitchenItemModel> output = kitchenServices.getCurrentOrders();
      model.put("items", output);
      return "kitchen";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      e.printStackTrace();
      return "error";
    }
  }

  /**
   * RequestMapping for any post requests coming from the kitchenorders.
   * @param allParams Identifier for whether the order is ready or not
   * @param request   HTTP request for requesting information from the page
   * @param response  HTTP request for receiving information from the page
   * @throws SQLException if there is an exception in an SQL statement
   * @throws UnsupportedEncodingException if an unsupported encoding format is encountered
   * @throws NoSuchAlgorithmException if no algorithm exists
   * @return page (html file) to be loaded. Needs to redirect to prevent form
   *      post resubmission when reloading the page.
   */
  @RequestMapping(value = "/kitchen/ready", method = RequestMethod.POST)
  public String orderReady(@RequestParam Map<String,String> allParams,
                           HttpServletRequest request,
                           HttpServletResponse response)
          throws SQLException, UnsupportedEncodingException, NoSuchAlgorithmException {
    if (!authenticationServices.authenticateCookies(request, response)) {
      return "redirect:/";
    }
    kitchenServices.orderReady(allParams);
    return "redirect:/kitchen";
  }
}
