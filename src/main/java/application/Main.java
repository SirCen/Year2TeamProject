package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

@Controller
@SpringBootApplication
public class Main {

  /**
   * The main method.
   * Runs the spring application from main.
   * @param args arguments to be passed in
   */
  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }

}
