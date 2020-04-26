package application.controllers;

import application.models.MenuItemModel;
import application.services.AuthenticationServices;
import application.services.MenuServices;
import application.services.ModifyAvailabilityServices;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ModifyAvailabilityController {

  @Autowired
  ModifyAvailabilityServices modifyAvailabilityServices;

  @Autowired
  MenuServices menuServices;

  @Autowired
  AuthenticationServices authenticationServices;

  /**
   * This method gets the modifyavailability page.
   * @param model     The model for the frontend.
   * @param request   HTTP request for requesting information from the page
   * @param response  HTTP response for receiving information from the page
   * @return the redirection or an error, depending on outcome.
   */
  @RequestMapping(value = "/modifyavailability", method = RequestMethod.GET)
  public String getMenuItems(Model model,
                             HttpServletRequest request,
                             HttpServletResponse response) {
    try {
      if (!authenticationServices.authenticateCookies(request, response)) {
        return "redirect:/";
      }
      ArrayList<MenuItemModel> output = menuServices.getMenu();
      model.addAttribute("menuItems", output);
      return "modifyavailability";
    } catch (Exception e) {
      model.addAttribute("message", e.getMessage());
      return "error";
    }
  }

  /**
   * This method handles the post requests from the modifyavailability page.
   * @param allParams The parameters from the frontend.
   * @param request   HTTP request for requesting information from the page
   * @param response  HTTP response for receiving information from the page
   * @return the redirection or an error, depending on outcome.
   */
  @RequestMapping(value = "/modifyavailability/modify", method = RequestMethod.POST)
  public String acceptButton(@RequestParam Map<String, String> allParams,
                             HttpServletRequest request,
                             HttpServletResponse response) {
    try {
      if (!authenticationServices.authenticateCookies(request, response)) {
        return "redirect:/";
      }
      String orderId = allParams.get("menu_id");
      modifyAvailabilityServices.changeAvailabilty(orderId);
      return "redirect:/modifyavailability";
    } catch (Exception e) {
      e.printStackTrace();
      return "error";
    }
  }
}