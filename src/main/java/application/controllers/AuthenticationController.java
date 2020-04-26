package application.controllers;

import application.services.AuthenticationServices;
import java.sql.SQLException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthenticationController {

  @Autowired
  AuthenticationServices authenticationServices;

  /**
   * This is a method which takes the user input from the loginPage.
   * @param allParams parameters from the frontend.
   * @param request   HTTP request to get cookies to.
   * @param response  HTTP response to send cookies to.
   * @return redirect to the appropriate page URL
   */
  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public String loginForm(@RequestParam Map<String, String> allParams, HttpServletRequest request,
      HttpServletResponse response) {
    try {
      String password = allParams.get("password");
      String userID = allParams.get("userName");
      if (authenticationServices.login(userID, password, response)) {
        return "redirect:/waiter";
      }
      return "redirect:/";
      // There should be more checks here and display logged in page if
      // the login was successful
    } catch (Exception e) {
      e.printStackTrace();
      return "redirect:/";
    }
  }

  /**
   * This method handles the logging out of a staff member and the redirection it causes.
   * @param response HTTP response for receiving data
   * @return redirect to the appropriate page URL
   */
  @RequestMapping(value = "/logout", method = RequestMethod.GET)
  public String select(HttpServletResponse response) {
    try {
      authenticationServices.logout(response);
      return "redirect:/";
    } catch (Exception e) {
      e.printStackTrace();
      return "redirect:/";
    }
  }
}
