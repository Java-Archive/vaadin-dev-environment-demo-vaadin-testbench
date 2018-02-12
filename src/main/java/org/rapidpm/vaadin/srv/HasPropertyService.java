package org.rapidpm.vaadin.srv;


/**
 *
 */
public interface HasPropertyService {

  public default PropertyService properties() {
    return new PropertyServiceInMemory();
  }
}
