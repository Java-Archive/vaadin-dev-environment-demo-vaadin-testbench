package org.rapidpm.vaadin.srv.api;


import org.rapidpm.vaadin.srv.impl.PropertyServiceInMemory;

/**
 *
 */
public interface HasPropertyService {

  default PropertyService properties() {
    return new PropertyServiceInMemory();
  }
}
