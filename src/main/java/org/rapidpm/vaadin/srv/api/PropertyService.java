package org.rapidpm.vaadin.srv.api;

/**
 *
 */
public interface PropertyService {

  String resolve(String key);

  boolean hasKey(String key);
}
