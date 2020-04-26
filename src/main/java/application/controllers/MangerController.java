package application.controllers;

import application.BadConfirmPasswordException;
import application.services.AuthenticationServices;
import application.services.ManagerServices;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for Manager pahes.
 */
@Controller
public class MangerController {
  /**
   * Authentication services used to check if user have the correct role.
   */
  @Autowired
  AuthenticationServices authenticationServices;
  /**
   * The services the manager controller relies on.
   */
  @Autowired
  ManagerServices managerServices;

  /**
   * Method called when someone goes on the manager page.
   * It adds all of the model attributes thymeleaf uses and returns manager.html.
   *
   * @param model     The model that holds all of the attributes that are used in thymeleaf
   * @param allParams All the parameters retrieved from forms and similar
   * @param request   The HttpServletRequest to retrieve cookies from
   * @param response  The HttpServletResponse to send cookies to
   * @return the manager html file. Due to how spring works this can be returned as a simple string
   */
  @RequestMapping(value = "/manager", method = RequestMethod.GET)
  public String display(Model model,
                        @RequestParam(required = false) Map<String, String> allParams,
                        HttpServletRequest request,
                        HttpServletResponse response) {
    try {
      if (!authenticationServices.authenticateCookies(request, response)) {
        return "redirect:/";
      }
      if (!authenticationServices.isRole(request, "manager")) {
        return authenticationServices.getPreviousPageByRequest(request).orElse("/");
      }

      model.addAttribute("staff", managerServices.getUsers());

      // attributes need for loginForm()
      model.addAttribute("result", allParams.get("result"));
      model.addAttribute("id", allParams.get("id"));
      return "manager";
    } catch (Exception e) {
      e.printStackTrace();
      return "error";
    }
  }

  /**
   * Method called when someone sends a POST request to add a new user.
   * It does the user authentication checks and creates a new user based on input.
   *
   * @param allParams All the parameters retrieved from forms
   * @param request   The HttpServletRequest to retrieve cookies from
   * @param redirectAttributes The redirect attributes
   * @param response  The HttpServletResponse to send cookies to
   * @return the redirect to the manager html file. Due to how spring works this can be returned as a simple string
   */
  @RequestMapping(value = "/manager/adduser", method = RequestMethod.POST)
  public String loginForm(@RequestParam Map<String, String> allParams,
                          HttpServletRequest request,
                          RedirectAttributes redirectAttributes,
                          HttpServletResponse response) {
    try {
      if (!authenticationServices.authenticateCookies(request, response)) {
        return "redirect:/";
      }
      if (!authenticationServices.isRole(request, "manager")) {
        return authenticationServices.getPreviousPageByRequest(request).orElse("/");
      }
      String name = allParams.get("name");
      String password = allParams.get("password");
      String password2 = allParams.get("password2");
      String role = allParams.get("role");
      int id = managerServices.addUser(name, password, password2, role);
      redirectAttributes.addAttribute("result", "successCreate");
      redirectAttributes.addAttribute("id", id);
      return "redirect:/manager";
    } catch (BadConfirmPasswordException e) {
      redirectAttributes.addAttribute("result", "BadConfirmPassword");
      return "redirect:/manager";
    } catch (Exception e) {
      e.printStackTrace();
      redirectAttributes.addAttribute("result", "error");
      redirectAttributes.addAttribute("error", e.getMessage());
      return "redirect:/manager";
    }
  }

  /**
   * Method called when someone sends a POST request to modify a user.
   * It does the user authentication checks and modifies user based on input.
   *
   * @param allParams All the parameters retrieved from forms
   * @param request   The HttpServletRequest to retrieve cookies from
   * @param redirectAttributes The redirect attributes
   * @param response  The HttpServletResponse to send cookies to
   * @return the redirect to the manager html file. Due to how spring works this can be returned as a simple string
   */
  @RequestMapping(value = "/manager/modify", method = RequestMethod.POST)
  public String modifyForm(@RequestParam Map<String, String> allParams,
                          HttpServletRequest request,
                          RedirectAttributes redirectAttributes,
                          HttpServletResponse response) {
    try {
      if (!authenticationServices.authenticateCookies(request, response)) {
        return "redirect:/";
      }
      if (!authenticationServices.isRole(request, "manager")) {
        return authenticationServices.getPreviousPageByRequest(request).orElse("/");
      }
      if (allParams.get("staffId") != null && !allParams.get("staffId").equals("")) {
        if (allParams.get("enabled") != null && !allParams.get("enabled").equals("")) {
          managerServices.changeEnabled(allParams.get("staffId"), allParams.get("enabled"));
        }
        if (allParams.get("name") != null && !allParams.get("name").equals("")) {
          managerServices.changeName(allParams.get("staffId"), allParams.get("name"));
        }
        if ((allParams.get("password") != null)
            && (allParams.get("password2") != null)
            && !allParams.get("password").equals("")
            && !allParams.get("password2").equals("")) {
          managerServices.changePassword(allParams.get("staffId"), allParams.get("password"),
              allParams.get("password2"));
        }
      }
      redirectAttributes.addAttribute("result", "successModify");
      redirectAttributes.addAttribute("id", allParams.get("staffId"));
      return "redirect:/manager";
    } catch (BadConfirmPasswordException e) {
      redirectAttributes.addAttribute("result", "BadConfirmPassword");
      return "redirect:/manager";
    } catch (Exception e) {
      e.printStackTrace();
      redirectAttributes.addAttribute("result", "error");
      redirectAttributes.addAttribute("error", e.getMessage());
      return "redirect:/manager";
    }
  }
}