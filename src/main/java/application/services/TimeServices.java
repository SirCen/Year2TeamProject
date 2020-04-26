package application.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.stereotype.Service;

@Service
public class TimeServices {

  /**
   * This method converts the time into a simple 24 hour clock format.
   * @param currentTime The current time
   * @return the converted time.
   */
  public String convertTime(String currentTime) {
    String timeoutput = "";
    try {
      DateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date date = inputFormatter.parse(currentTime);
      DateFormat outputFormatter = new SimpleDateFormat("HH:mm:ss");

      timeoutput = outputFormatter.format(date); // Output : 00:00:00
    } catch (Exception e) {
      timeoutput = "ERROR";
    }
    return timeoutput;
  }
}
