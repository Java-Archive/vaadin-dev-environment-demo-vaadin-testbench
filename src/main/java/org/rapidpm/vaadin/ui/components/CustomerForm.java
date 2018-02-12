package org.rapidpm.vaadin.ui.components;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.vaadin.addons.framework.Registration;
import org.rapidpm.vaadin.shared.Customer;
import org.rapidpm.vaadin.shared.CustomerStatus;
import org.rapidpm.vaadin.srv.api.PropertyService;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Set;

import static java.util.concurrent.ConcurrentHashMap.newKeySet;
import static org.rapidpm.vaadin.addons.framework.ComponentIDGenerator.*;

public class CustomerForm extends Composite implements HasLogger {

  public static final String TF_FIRST_NAME_ID      = textfieldID().apply(CustomerForm.class, "tf_first_name");
  public static final String TF_FIRST_NAME_CAPTION = caption().apply(TF_FIRST_NAME_ID);

  public static final String TF_LAST_NAME_ID      = textfieldID().apply(CustomerForm.class, "tf_last_name");
  public static final String TF_LAST_NAME_CAPTION = caption().apply(TF_LAST_NAME_ID);

  public static final String TF_EMAIL_ID      = textfieldID().apply(CustomerForm.class, "tf_email");
  public static final String TF_EMAIL_CAPTION = caption().apply(TF_EMAIL_ID);

  public static final String CB_STATUS_ID      = comboBoxID().apply(CustomerForm.class, "cb_status");
  public static final String CB_STATUS_CAPTION = caption().apply(CB_STATUS_ID);

  public static final String DF_BIRTHDAY_ID      = dateFieldID().apply(CustomerForm.class, "df_birthday");
  public static final String DF_BIRTHDAY_CAPTION = caption().apply(DF_BIRTHDAY_ID);

  public static final String BTN_SAVE_ID      = buttonID().apply(CustomerForm.class, "btn_save");
  public static final String BTN_SAVE_CAPTION = caption().apply(BTN_SAVE_ID);

  public static final String BTN_DELETE_ID      = buttonID().apply(CustomerForm.class, "btn_delete");
  public static final String BTN_DELETE_CAPTION = caption().apply(BTN_DELETE_ID);


  private final TextField                firstName = new TextField();
  private final TextField                lastName  = new TextField();
  private final TextField                email     = new TextField();
  private final ComboBox<CustomerStatus> status    = new ComboBox<>();
  private final DateField                birthday  = new DateField();
  private final Button                   save      = new Button();
  private final Button                   delete    = new Button();


  private final Binder<Customer> beanBinder      = new Binder<>(Customer.class);
  private final Set<UpdateEvent> saveListeners   = newKeySet();
  private final Set<UpdateEvent> deleteListeners = newKeySet();
  private       Customer         customer;

  @Inject private PropertyService propertyService;

  public String resolve(String key) {
    return propertyService.resolve(key);
  }

  private final Layout layout = new FormLayout(firstName,
                                               lastName,
                                               email,
                                               status,
                                               birthday,
                                               new HorizontalLayout(save, delete)
  );

  public CustomerForm() {
    setCompositionRoot(layout);
    setSizeUndefined();
  }

  @PostConstruct
  private void postConstruct() {
    firstName.setId(TF_FIRST_NAME_ID);
    firstName.setCaption(resolve(TF_FIRST_NAME_CAPTION));

    lastName.setId(TF_LAST_NAME_ID);
    lastName.setCaption(resolve(TF_LAST_NAME_CAPTION));

    email.setId(TF_EMAIL_ID);
    email.setCaption(resolve(TF_EMAIL_CAPTION));

    status.setId(CB_STATUS_ID);
    status.setCaption(resolve(CB_STATUS_CAPTION));
    status.setItems(CustomerStatus.values());

    birthday.setId(DF_BIRTHDAY_ID);
    birthday.setCaption(resolve(DF_BIRTHDAY_CAPTION));

    save.setId(BTN_SAVE_ID);
    save.setCaption(resolve(BTN_SAVE_CAPTION));
    save.setStyleName(ValoTheme.BUTTON_PRIMARY);
    save.setClickShortcut(KeyCode.ENTER);
    save.addClickListener(e -> this.save());

    delete.setId(BTN_DELETE_ID);
    delete.setCaption(resolve(BTN_DELETE_CAPTION));
    delete.addClickListener(e -> this.delete());

    beanBinder.bindInstanceFields(this);

  }


  public void setCustomer(Customer customer) {
    this.customer = customer;
    beanBinder.setBean(customer);

    // Show delete button for only customers already in the database
    delete.setVisible(customer.isPersisted());
    setVisible(true);
    firstName.selectAll();
  }

  private void delete() {
    setVisible(false);
    deleteListeners.forEach(listener -> listener.update(customer));
  }

  private void save() {
    setVisible(false);
    saveListeners.forEach(listener -> listener.update(customer));
  }

  public Registration registerSaveListener(UpdateEvent updateEvent) {
    saveListeners.add(updateEvent);
    return () -> saveListeners.remove(updateEvent);
  }

  public Registration registerDeleteListener(UpdateEvent updateEvent) {
    deleteListeners.add(updateEvent);
    return () -> deleteListeners.remove(updateEvent);
  }


  public interface UpdateEvent {
    void update(Customer customer);
  }
}
