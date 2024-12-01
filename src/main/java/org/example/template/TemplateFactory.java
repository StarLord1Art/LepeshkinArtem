package org.example.template;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import org.example.Main;
import spark.template.freemarker.FreeMarkerEngine;

public class TemplateFactory {

  public static FreeMarkerEngine freeMarkerEngine() {
    Configuration freeMarkerConfiguration = new Configuration(Configuration.VERSION_2_3_0);
    FreeMarkerEngine freeMarkerEngine = new FreeMarkerEngine(freeMarkerConfiguration);
    freeMarkerConfiguration.setTemplateLoader(new ClassTemplateLoader(Main.class, "/"));
    return freeMarkerEngine;
  }
}
