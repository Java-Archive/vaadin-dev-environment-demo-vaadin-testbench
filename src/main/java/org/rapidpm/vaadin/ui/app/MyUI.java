package org.rapidpm.vaadin.ui.app;

import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.Title;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.rapidpm.ddi.DI;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.frp.functions.CheckedExecutor;
import org.rapidpm.vaadin.addons.framework.Registration;
import org.rapidpm.vaadin.shared.Customer;
import org.rapidpm.vaadin.srv.CustomerService;
import org.rapidpm.vaadin.srv.CustomerServiceImpl;
import org.rapidpm.vaadin.srv.PropertyService;
import org.rapidpm.vaadin.ui.components.CustomerForm;
import org.vaadin.leif.headertags.Meta;
import org.vaadin.leif.headertags.MetaTags;
import org.vaadin.leif.headertags.Link;

import static org.rapidpm.vaadin.shared.Constants.REGISTRY_BASE_KEY;

@JavaScript("vaadin://app.js")
@Link(rel = "manifest", href = "./manifest.json")
@Link(rel = "sw.js", href = "./sw.js")
@MetaTags({
              @Meta(name = "viewport", content = "width=device-width, initial-scale=1") ,
              @Meta(name = "theme-color", content = "#404549") ,
              @Meta(name = "description", content = "some content") ,
          })
@Title("Vaadin PWA Jumpstart")
public class MyUI extends UI implements HasLogger {

  public static final String COMPONENT_BASE_KEY = REGISTRY_BASE_KEY + "." + MyUI.class.getSimpleName();

  public static final String FILTER_TF_ID                     = COMPONENT_BASE_KEY + "." + "filterTF";
  public static final String FILTER_TF_PLACEHOLDER            = FILTER_TF_ID + "." + "placeholder";
  public static final String CLEAR_FILTER_BTN_ID              = COMPONENT_BASE_KEY + "." + "clearFilterBTN";
  public static final String CLEAR_FILTER_BTN_DESCRIPTION     = CLEAR_FILTER_BTN_ID + "." + "description";
  public static final String NEW_CUSTOMER_BTN_ID              = COMPONENT_BASE_KEY + "." + "newCustomerBTN";
  public static final String NEW_CUSTOMER_BTN_CAPTION         = NEW_CUSTOMER_BTN_ID + "." + "caption";
  public static final String DATA_GRID_ID                     = COMPONENT_BASE_KEY + "." + "dataGrid";
  public static final String DATA_GRID_COL                    = DATA_GRID_ID + "." + "col";
  public static final String DATA_GRID_COL_CAPTION_FIRST_NAME = DATA_GRID_COL + "." + "firstName";
  public static final String DATA_GRID_COL_CAPTION_LAST_NAME  = DATA_GRID_COL + "." + "lastName";
  public static final String DATA_GRID_COL_CAPTION_EMAIL      = DATA_GRID_COL + "." + "email";

  private final CustomerService service = CustomerServiceImpl.getInstance();

  private final Grid<Customer> grid               = new Grid<>();
  private final TextField      filterText         = new TextField();
  private final CustomerForm   customerForm       = new CustomerForm();
  private final Button         clearFilterTextBtn = new Button(FontAwesome.TIMES);
  private final Button         addCustomerBtn     = new Button();

  //  @Inject private PropertyService propertyService;
  private final PropertyService propertyService = DI.activateDI(PropertyService.class);
  private Registration deleteRegistration;
  private Registration saveRegistration;

  private String resolve(String key) {
    return propertyService.resolve(key);
  }

  @Override
  protected void init(VaadinRequest vaadinRequest) {

    filterText.setId(FILTER_TF_ID);
    filterText.setPlaceholder(resolve(FILTER_TF_PLACEHOLDER));
    filterText.addValueChangeListener(e -> updateList());
    filterText.setValueChangeMode(ValueChangeMode.LAZY);

    clearFilterTextBtn.setId(CLEAR_FILTER_BTN_ID);
    clearFilterTextBtn.setDescription(resolve(CLEAR_FILTER_BTN_DESCRIPTION));
    clearFilterTextBtn.addClickListener(e -> filterText.clear());

    CssLayout filtering = new CssLayout(filterText, clearFilterTextBtn);
    filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

    addCustomerBtn.setId(NEW_CUSTOMER_BTN_ID);
    addCustomerBtn.setCaption(resolve(NEW_CUSTOMER_BTN_CAPTION));
    addCustomerBtn.addClickListener(e -> {
      grid.asSingleSelect().clear();
      customerForm.setCustomer(new Customer());
    });

    grid.setId(DATA_GRID_ID);
    grid.addColumn(Customer::getFirstName)
        .setCaption(resolve(DATA_GRID_COL_CAPTION_FIRST_NAME))
        .setId(DATA_GRID_COL_CAPTION_FIRST_NAME);
    grid.addColumn(Customer::getLastName)
        .setCaption(resolve(DATA_GRID_COL_CAPTION_LAST_NAME))
        .setId(DATA_GRID_COL_CAPTION_LAST_NAME);
    grid.addColumn(Customer::getEmail)
        .setCaption(resolve(DATA_GRID_COL_CAPTION_EMAIL))
        .setId(DATA_GRID_COL_CAPTION_EMAIL);

    grid.setColumnOrder(
        DATA_GRID_COL_CAPTION_FIRST_NAME,
        DATA_GRID_COL_CAPTION_LAST_NAME,
        DATA_GRID_COL_CAPTION_EMAIL
    );

    grid.asSingleSelect().addValueChangeListener(event -> {
      if (event.getValue() == null) {
        customerForm.setVisible(false);
      } else {
        customerForm.setCustomer(event.getValue());
      }
    });
    grid.setSizeFull();

    HorizontalLayout main = new HorizontalLayout(grid, customerForm);
    main.setSizeFull();
    main.setExpandRatio(grid, 1);

    updateList();
    customerForm.setVisible(false);

    setContent(
        new VerticalLayout(
            new HorizontalLayout(filtering, addCustomerBtn),
            main
        ));

    deleteRegistration = customerForm.registerDeleteListener(customer -> {
      service.delete(customer);
      updateList();
    });
    saveRegistration = customerForm.registerSaveListener(customer -> {
      service.save(customer);
      updateList();
    });
  }

  @Override
  public void detach() {
    super.detach();
    ((CheckedExecutor) () -> deleteRegistration.remove()).execute();
    ((CheckedExecutor) () -> saveRegistration.remove()).execute();
  }

  private void updateList() {
    grid.setItems(service.findAll(filterText.getValue()));
  }

}
