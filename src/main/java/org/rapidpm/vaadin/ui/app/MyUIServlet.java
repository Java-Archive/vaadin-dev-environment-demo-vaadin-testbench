package org.rapidpm.vaadin.ui.app;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;
import org.rapidpm.vaadin.addon.di.ddi.DDIVaadinServlet;

import javax.servlet.annotation.WebServlet;
import java.util.List;

import static java.util.Collections.singletonList;

/**
 *
 */
@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
public class MyUIServlet extends DDIVaadinServlet {
  @Override
  public List<String> topLevelPackagesToActivate() {
    return singletonList("org.rapidpm");
  }
}
