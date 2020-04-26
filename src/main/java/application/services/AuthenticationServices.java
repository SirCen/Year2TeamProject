package application.services;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServices {

  @Autowired
  private Connection connection;

  /**
   * Checks if username/password correct.
   * @param staffId  Staff id of the user who needs a session created
   * @param password Password of the user who needs a session created
   * @param response HTTP response to send cookies to
   * @return whether or not the username/password exist
   * @throws SQLException if there is an error when querying the database
   * @throws UnsupportedEncodingException if an unsupported encoding method is encountered
   * @throws NoSuchAlgorithmException if no algorithm exists
   */
  public boolean login(String staffId, String password, HttpServletResponse response) throws SQLException, UnsupportedEncodingException, NoSuchAlgorithmException {
    String staffRole = "";
    String staffName = "";
    Boolean staffEnabled = null;

    String hashedPassword = generateHash(password, "staffId:" + staffId);

    PreparedStatement updatePassword =
        connection.prepareStatement("SELECT * FROM staff WHERE staff_id=? AND password=?");
    updatePassword.setInt(1, Integer.parseInt(staffId));
    updatePassword.setString(2, hashedPassword);
    ResultSet rs = updatePassword.executeQuery();
    if (!rs.isBeforeFirst()) {  // if no user account matches
      return false;
    }
    while (rs.next()) {
      staffName = rs.getString("name");
      staffRole = rs.getString("role");
      staffEnabled = rs.getBoolean("enabled");
    }
    if (!staffEnabled) {  // if account is disabled
      return false;
    }

    // baking cookies
    Cookie staffIdCookie = new Cookie("staffid", staffId);
    staffIdCookie.setMaxAge(12 * 60 * 60);  // expires in 12 hours
    staffIdCookie.setSecure(false);  // disabled as our app does not use SSL. Not great
    staffIdCookie.setHttpOnly(true);   // JS cannot read cookie, for security reasons
    Cookie staffRoleCookie = new Cookie("staffrole", staffRole);
    staffRoleCookie.setMaxAge(12 * 60 * 60);
    staffRoleCookie.setSecure(false);
    staffRoleCookie.setHttpOnly(true);
    Cookie staffNameCookie = new Cookie("staffname", staffName);
    staffNameCookie.setMaxAge(12 * 60 * 60);
    staffNameCookie.setSecure(false);
    staffNameCookie.setHttpOnly(true);

    // cookie containing hash of 3 values to help protect against tampering
    String salt = "staffid" + "staffrole" + "staffname";
    String hash = staffId + staffRole + staffName;
    String hashed = URLEncoder.encode( this.generateHash(hash, salt), "UTF-8" );
    Cookie hashCookie = new Cookie("hash", hashed);
    hashCookie.setMaxAge(12 * 60 * 60);
    hashCookie.setSecure(false);
    hashCookie.setHttpOnly(true);

    // sending cookies
    response.addCookie(staffIdCookie);
    response.addCookie(staffRoleCookie);
    response.addCookie(staffNameCookie);
    response.addCookie(hashCookie);
    return true;
  }

  /**
   * Removes session from db and invalidates httpsession.
   * @param response HTTP response to send cookies to.
   */
  public void logout(HttpServletResponse response) {
    // removing cookies by sending them again with slightly different values
    Cookie staffIdCookie = new Cookie("staffid", null);
    staffIdCookie.setMaxAge(0);
    staffIdCookie.setSecure(false);
    staffIdCookie.setHttpOnly(true);
    Cookie staffRoleCookie = new Cookie("staffrole", null);
    staffRoleCookie.setMaxAge(0);
    staffRoleCookie.setSecure(false);
    staffRoleCookie.setHttpOnly(true);
    Cookie staffNameCookie = new Cookie("staffname", null);
    staffNameCookie.setMaxAge(0);
    staffNameCookie.setSecure(false);
    staffNameCookie.setHttpOnly(true);
    Cookie hashCookie = new Cookie("hash", null);
    hashCookie.setMaxAge(0);
    hashCookie.setSecure(false);
    hashCookie.setHttpOnly(true);

    // sending cookies
    response.addCookie(staffIdCookie);
    response.addCookie(staffRoleCookie);
    response.addCookie(staffNameCookie);
    response.addCookie(hashCookie);
  }

  /**
   * Method for handling cookies related to staff authentication.
   * @param request  HTTP request for requesting information from the page
   * @param response HTTP response for receiving information from the page
   * @return whether or not the login attempt was successful
   * @throws SQLException if there is an error in an SQL statement
   * @throws UnsupportedEncodingException if an unsupported encoding method is encountered
   * @throws NoSuchAlgorithmException if no algorithm exists
   */
  public boolean authenticateCookies(HttpServletRequest request, HttpServletResponse response) throws SQLException, UnsupportedEncodingException, NoSuchAlgorithmException {
    Cookie[] cookies = request.getCookies();
    String staffId = "";
    String staffRole = "";
    String staffName = "";
    String cookieHash = "";
    for (Cookie cookie : cookies) {
      if (cookie.getName().equals("staffid")) {
        staffId = cookie.getValue();
      } else if (cookie.getName().equals("staffrole")) {
        staffRole = cookie.getValue();
      } else if (cookie.getName().equals("staffname")) {
        staffName = cookie.getValue();
      } else if (cookie.getName().equals("hash")) {
        cookieHash = cookie.getValue();
      }
    }
    if (staffId.equals("") || staffRole.equals("") || cookieHash.equals("") || staffName.equals(
        "")) {
      System.out.println("Cookies empty");
      this.logout(response);
      return false;
    }
    Statement check = connection.createStatement();
    ResultSet rs = check.executeQuery("SELECT * FROM staff WHERE staff_id=" + staffId);
    String staffIdDb = "";
    String staffRoleDb = "";
    String staffNameDb = "";
    Boolean staffEnabled = null;
    while (rs.next()) {
      staffIdDb = Integer.toString(rs.getInt("staff_id"));
      staffRoleDb = rs.getString("role");
      staffNameDb = rs.getString("name");
      staffEnabled = rs.getBoolean("enabled");
    }
    if (staffIdDb.equals("") || staffRoleDb.equals("") || staffNameDb.equals("")) {
      System.out.println("db user account empty");
      this.logout(response);
      return false;
    }
    if (!staffEnabled) {  // if account is disabled
      return false;
    }
    String salt = "staffid" + "staffrole" + "staffname";
    String hash = staffId + staffRole + staffName;
    String hashed = URLEncoder.encode(this.generateHash(hash, salt), "UTF-8" );
    if (!cookieHash.equals(hashed)) {
      System.out.println("failed hash");
      this.logout(response);
      return false;
    }
    return true;
  }

  /**
   * Method for generating password hashes.
   * @param password The desired password
   * @param salt     Accompanying data used to produce a unique hash of the password
   * @return the newly generated hash
   * @throws UnsupportedEncodingException when an unsupported encoding method is encountered
   * @throws NoSuchAlgorithmException when no algorithm exists
   */
  public String generateHash(String password, String salt) throws UnsupportedEncodingException, NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA-512");
    md.update(salt.getBytes());
    byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
    String hashString = new String(hashedPassword, StandardCharsets.UTF_8);
    // encoding hash due to cookies no liking the unencoded value;
    return URLEncoder.encode(hashString, "UTF-8");
  }

  /**
   * Getter for retrieving the staff role using cookies.
   * @param request HTTP request for requesting information from the page
   * @return the role of the currently logged in staff member
   */
  public String getRole (HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    for (Cookie cookie : cookies) {
      if (cookie.getName().equals("staffrole")) {
        return cookie.getValue();
      }
    }
    return "";
  }

  /**
   * Checks whether a role exists.
   * @param request HTTP request for requesting information from the page
   * @param check   Role to be checked
   * @return boolean that shows whether the passed in role exists or not
   */
  public Boolean isRole (HttpServletRequest request, String check) {
    Cookie[] cookies = request.getCookies();
    for (Cookie cookie : cookies) {
      if (cookie.getName().equals("staffrole") && cookie.getValue().equals(check)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns a string of "redirect:[url of previous page]" if available.
   * Otherwise, "redirect:/" if unable to get previous page.
   * Usage:
   * return authenticationServices.getPreviousPageByRequest(request).orElse("/");
   * @param request HTTP request for requesting information from the page
   * @return Redirect string
   */
  public Optional<String> getPreviousPageByRequest(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader("Referer"))
        .map(requestUrl -> "redirect:" + requestUrl);
  }
}
