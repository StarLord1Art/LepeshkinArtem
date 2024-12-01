package org.example;

import java.util.List;
import org.example.controller.Controller;

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
}
