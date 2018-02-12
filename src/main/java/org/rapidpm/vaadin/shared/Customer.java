package org.rapidpm.vaadin.shared;

import org.rapidpm.dependencies.core.logger.HasLogger;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 */
@SuppressWarnings("serial")
public class Customer implements Serializable, HasLogger {

  private Long   id        = -1L;
  private String firstName = "";
  private String lastName  = "";
  private LocalDate      birthDay;
  private CustomerStatus status;
  private String email = "";

  public boolean isPersisted() {
    return id != -1L;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Customer)) return false;
    Customer customer = (Customer) o;
    return Objects.equals(id, customer.id) &&
           Objects.equals(firstName, customer.firstName) &&
           Objects.equals(lastName, customer.lastName) &&
           Objects.equals(birthDay, customer.birthDay) &&
           status == customer.status &&
           Objects.equals(email, customer.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, firstName, lastName, birthDay, status, email);
  }

  @Override
  public String toString() {
    return firstName + " " + lastName;
  }


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    Objects.requireNonNull(id);
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public LocalDate getBirthDay() {
    return birthDay;
  }

  public void setBirthDay(LocalDate birthDate) {
    this.birthDay = birthDate;
  }

  public CustomerStatus getStatus() {
    return status;
  }

  public void setStatus(CustomerStatus status) {
    this.status = status;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}