import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import controller.Controller;
import org.flywaydb.core.Flyway;

import java.util.List;

public class Application {

  private final List<Controller> controllers;

  public Application(List<Controller> controllers) {
    this.controllers = controllers;
  }

  public void start() {
    for (Controller controller : controllers) {
      controller.initializeEndpoints();
    }
  }

  public void createDbTables() {
    Config config = ConfigFactory.load();

    Flyway flyway =
            Flyway.configure()
                    .locations("classpath:db/migrations")
                    .dataSource(config.getString("app.database.url"),
                            config.getString("app.database.user"),
                            config.getString("app.database.password"))
                    .load();
    flyway.migrate();
  }
}