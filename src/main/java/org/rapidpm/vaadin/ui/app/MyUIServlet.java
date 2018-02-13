package org.rapidpm.vaadin.ui.app;

import com.vaadin.annotations.VaadinServletConfiguration;
import org.rapidpm.frp.model.serial.Pair;
import org.rapidpm.vaadin.addon.di.ddi.DDIVaadinServlet;

import javax.servlet.annotation.WebServlet;
import java.util.stream.Stream;

import static java.util.stream.Stream.of;
import static org.rapidpm.frp.model.serial.Pair.next;

/**
 *
 */
@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
public class MyUIServlet extends DDIVaadinServlet {

  @Override
  public Stream<String> topLevelPackagesToActivate() {
    return of("org.rapidpm");
  }

  @Override
  public Stream<Pair<String, String>> attributesToAddToHTML() {
    return of(next("lang", "en_US"));
  }
}
