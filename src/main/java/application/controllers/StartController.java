package application.controllers;

import application.models.TableModel;
import application.services.StartServices;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class StartController {

  @Autowired
  StartServices startServices;

  /**
   * This method gets the start page.
   * @param model The frontend model.
   * @return the start menu.
   */
  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String start(Model model) {
    ArrayList<TableModel> output = startServices.getTables();
    model.addAttribute("tables", output);
    return "start";
  }

  /**
   * This method handles post requests on the start page.
   * @param allParams The parameters on the frontend.
   * @param request   HTTP request to grab cookies from.
   * @param response  HTTP response to send cookies to.
   * @return the redirection to the menu.
   */
  @RequestMapping(value = "/select", method = RequestMethod.POST)
  public String select(@RequestParam Map<String, String> allParams, HttpServletRequest request, HttpServletResponse response) {
    Cookie tableCookie = new Cookie("table",allParams.get("table"));
    tableCookie.setHttpOnly(false);
    tableCookie.setMaxAge(-1);
    response.addCookie(tableCookie);

    return "redirect:/menu";
  }
}
