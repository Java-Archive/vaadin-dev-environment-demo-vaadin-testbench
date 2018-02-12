package org.rapidpm.vaadin.srv.impl;

import org.rapidpm.vaadin.srv.api.PropertyService;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class PropertyServiceInMemory implements PropertyService {
  private final Map<String, String> storage = new HashMap<>();

  @Override
  public String resolve(final String key) {
    return storage.get(key);
  }

  @Override
  public boolean hasKey(final String key) {
    return storage.containsKey(key);
  }

  @PostConstruct
  public void init() {


    storage.put("admin", "admin");

    storage.put("generic.ok", "Ok");
    storage.put("generic.cancel", "Cancel");

    storage.put("login.name", "Login"); // i18n
    storage.put("login.info", "Please enter your username and password"); // i18n
    storage.put("login.username", "username"); // i18n
    storage.put("login.password", "password"); // i18n
    storage.put("login.failed", "Login failed..."); // i18n
    storage.put("login.failed.description", "Login failed, please use right User / Password combination"); // i18n

    storage.put("login.language.de", "German");
    storage.put("login.language.en", "English");

//Jumpstart App
    storage.put("customerboardcomponent-textfield-filtertf.placeholder", "filter by name...");
    storage.put("customerboardcomponent-button-clearfilterbtn.description", "Clear the current filter");
    storage.put("customerboardcomponent-button-newcustomerbtn.caption", "Add new customer");
    storage.put("customerboardcomponent-grid-datagrid.col.firstName", "First Name");
    storage.put("customerboardcomponent-grid-datagrid.col.lastName", "Last Name");
    storage.put("customerboardcomponent-grid-datagrid.col.email", "Email");


    storage.put("customerform-textfield-tf_first_name.caption", "First name");
    storage.put("customerform-textfield-tf_last_name.caption", "Last name");
    storage.put("customerform-textfield-tf_email.caption", "Email");
    storage.put("customerform-combobox-cb_status.caption", "Status");
    storage.put("customerform-datefield-df_birthday.caption", "Birthday");
    storage.put("customerform-button-btn_save.caption", "Save");
    storage.put("customerform-button-btn_delete.caption", "Delete");

  }
}
