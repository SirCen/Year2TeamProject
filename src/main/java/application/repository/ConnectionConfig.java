package application.repository;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class ConnectionConfig {

  @Autowired
  private DataSource dataSource;

  @Bean("connection")
  @DependsOn({"dataSource"})
  public Connection connection() throws SQLException {
    return dataSource.getConnection();
  }
}
