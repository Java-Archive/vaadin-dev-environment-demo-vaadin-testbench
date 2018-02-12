package org.rapidpm.vaadin.srv;

/**
 *
 */
public interface PropertyService {

  String resolve(String key);

  boolean hasKey(String key);
}
