package org.rapidpm.vaadin.ui.app;

import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.vaadin.ui.components.CustomerBoardComponent;
import org.vaadin.leif.headertags.Link;
import org.vaadin.leif.headertags.Meta;
import org.vaadin.leif.headertags.MetaTags;

import javax.inject.Inject;

@JavaScript("vaadin://app.js")
@Link(rel = "manifest", href = "./manifest.json")
@Link(rel = "sw.js", href = "./sw.js")
@MetaTags({
              @Meta(name = "viewport", content = "width=device-width, initial-scale=1"),
              @Meta(name = "theme-color", content = "#404549"),
              @Meta(name = "description", content = "some content"),
          })
@Title("Vaadin PWA Jumpstart")
public class MyUI extends UI implements HasLogger {

  @Inject private CustomerBoardComponent customerBoardComponent;

  @Override
  protected void init(VaadinRequest vaadinRequest) {

    setContent(customerBoardComponent);
  }


}
