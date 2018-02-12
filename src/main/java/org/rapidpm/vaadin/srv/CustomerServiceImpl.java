package org.rapidpm.vaadin.srv;

import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.vaadin.shared.Customer;
import org.rapidpm.vaadin.shared.CustomerStatus;

import java.time.LocalDate;
import java.util.*;

public class CustomerServiceImpl implements HasLogger, CustomerService {

  private static CustomerServiceImpl instance;
  private final  Map<Long, Customer> contacts = new HashMap<>();

  private CustomerServiceImpl() {
  }


  public static CustomerService getInstance() {
    if (instance == null) {
      instance = new CustomerServiceImpl();
      instance.ensureTestData();
    }
    return instance;
  }

  @Override
  public synchronized List<Customer> findAll() {
    return findAll(null);
  }

  @Override
  public synchronized List<Customer> findAll(String stringFilter) {
    List<Customer> arrayList = new ArrayList<>();
    for (Customer contact : contacts.values()) {
      try {
        boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                               || contact.toString().toLowerCase().contains(stringFilter.toLowerCase());
        if (passesFilter) {
          arrayList.add(contact);
        }
      } catch (Exception ex) {
        logger().warning(ex.getMessage());
      }
    }
    arrayList.sort((o1, o2) -> (int) (o2.getId() - o1.getId()));
    return arrayList;
  }

  @Override
  public synchronized List<Customer> findAll(String stringFilter, int start, int maxresults) {
    final List<Customer> arrayList = findAll(stringFilter);
    arrayList.sort((o1, o2) -> (int) (o2.getId() - o1.getId()));
    int end = start + maxresults;
    if (end > arrayList.size()) {
      end = arrayList.size();
    }
    return arrayList.subList(start, end);
  }

  @Override
  public synchronized long count() {
    return contacts.size();
  }

  @Override
  public synchronized void delete(Customer value) {
    contacts.remove(value.getId());
  }

  @Override
  public synchronized void save(Customer entry) {
    Objects.requireNonNull(entry, "Customer is null. Are you sure you have connected your form to the application as described in tutorial chapter 7?");

    //TOD show Notification

    if (entry.getId() == -1L) {
      entry.setId((long) (contacts.size() + 1));
    }
    contacts.put(entry.getId(), entry);
  }

  public void ensureTestData() {
    if (findAll().isEmpty()) {
      final String[] names = new String[]{"Gabrielle Patel", "Brian Robinson", "Eduardo Haugen",
                                          "Koen Johansen", "Alejandro Macdonald", "Angel Karlsson", "Yahir Gustavsson", "Haiden Svensson",
                                          "Emily Stewart", "Corinne Davis", "Ryann Davis", "Yurem Jackson", "Kelly Gustavsson",
                                          "Eileen Walker", "Katelyn Martin", "Israel Carlsson", "Quinn Hansson", "Makena Smith",
                                          "Danielle Watson", "Leland Harris", "Gunner Karlsen", "Jamar Olsson", "Lara Martin",
                                          "Ann Andersson", "Remington Andersson", "Rene Carlsson", "Elvis Olsen", "Solomon Olsen",
                                          "Jaydan Jackson", "Bernard Nilsen"};
      Random r = new Random(0);
      for (String name : names) {
        String[] split = name.split(" ");
        Customer c     = new Customer();
        c.setFirstName(split[0]);
        c.setLastName(split[1]);
        c.setEmail(split[0].toLowerCase() + "@" + split[1].toLowerCase() + ".com");
        c.setStatus(CustomerStatus.values()[r.nextInt(CustomerStatus.values().length)]);
        int daysOld = 0 - r.nextInt(365 * 15 + 365 * 60);
        c.setBirthDay(LocalDate.now().plusDays(daysOld));
        save(c);
      }
    }
  }


  public void resetData() {
    instance.contacts.clear();
    ensureTestData();
  }

}