import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import controller.ArticleController;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.LoggerFactory;
import repositories.ArticleRepository;
import repositories.CommentRepository;
import service.ArticleService;
import spark.Service;

import java.util.List;
import org.slf4j.Logger;

public class Main {

  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {

    LOG.info("App started");
    Service service = Service.ignite();
    ObjectMapper objectMapper = new ObjectMapper();
    Application application = new Application(
      List.of(
        new ArticleController(
          service,
            new ArticleService(
              new ArticleRepository(JdbiConnection()), JdbiConnection()),
            objectMapper
          )
        )
    );
    application.createDbTables();
    application.start();
  }

  public static Jdbi JdbiConnection() {
    Config config = ConfigFactory.load();

    return Jdbi.create(config.getString("app.database.url"),
            config.getString("app.database.user"),
            config.getString("app.database.password"));
  }
}