package application.controllers;

import application.models.OrderItemModel;
import application.models.PostOrderModel;
import application.services.PostOrderServices;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PostOrderController {

  @Autowired
  PostOrderServices postOrderService;

  /**
   * This method gets the postorder page.
   * @param model The frontend model.
   * @param tableCookie String containing user's table number
   * @return the redirection to the postorder page.
   */
  @RequestMapping(value = "/postorder", method = RequestMethod.GET)
  public String getOrder(Model model, @CookieValue(value = "table", defaultValue = "-1") String tableCookie) {
    if (tableCookie.equals("-1")) {  // if table has default value
      return "redirect:/";
    }
    int tableNum;
    try {
      tableNum = Integer.parseInt( tableCookie);
      ArrayList<OrderItemModel> output = postOrderService.getOrders(tableNum);
      ArrayList<PostOrderModel> info = postOrderService.getOrderInfo(tableNum);
      model.addAttribute("orders", output);
      model.addAttribute("info", info);
      return "postorder";
    } catch (Exception e) {
      model.addAttribute("message", e.getMessage());
      e.printStackTrace();
      return "redirect:/";
    }
  }

  /**
   * Tells the database that a table has paid for their order
   * @param allParams Used to retrieve the order ID for the relevant order
   * @return the redirection back to the postorder page
   */
  @RequestMapping(value="/postorder/payment", method=RequestMethod.POST)
  public String setPaid(@RequestParam Map<String,String> allParams) {
    int orderId;
    try {
      for (Object key: allParams.keySet().toArray()) {
        orderId = Integer.parseInt((String) key);
        postOrderService.setPaid(orderId);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return "error";
    }
    return "redirect:/postorder";
  }
}

