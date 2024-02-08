package com.book.Controllers;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.*;

import com.book.DAOs.CustomerDAO;
import com.mongodb.client.MongoCollection;
import java.util.ArrayList;

public class CustomerController extends DatabaseConnection {

  MongoCollection<CustomerDAO> collection;

  public CustomerController() {
    collection = database.getCollection("Customers", CustomerDAO.class);
  }

  public void printAll() {
    //System.out.println(collection.countDocuments());
    collection
      .find()
      .forEach(d -> {
        System.out.println(d.toString());
      });
  }

  public void makeNew(String Name, String Address, String Alias, int Rating) {
    CustomerDAO customer = new CustomerDAO();
    customer.setName(Name);
    customer.setAddress(Address);
    customer.setJobIDs(new ArrayList<>());
    customer.setMachineIDs(new ArrayList<>());
    customer.setAlias(Alias);
    customer.setRating(Rating);
    customer.setNotes("");

    collection.insertOne(customer);
  }

  public void addDAO(CustomerDAO newDAO) {
    collection.insertOne(newDAO);
  }

  public ArrayList<CustomerDAO> getCustomers(int size, int skip) {
    ArrayList<CustomerDAO> customers = new ArrayList<>();
    collection
      .find()
      .sort(descending("name"))
      .limit(size)
      .skip(skip)
      .forEach(c -> {
        customers.add(c);
      });

    return customers;
  }

  public CustomerDAO getDAO(String customerName) {
    return collection.find(eq("name", customerName)).first();
  }

  public boolean updateCustomer(CustomerDAO updated) {
    CustomerDAO isUpdated = collection.findOneAndReplace(
      eq("_id", updated.getId()),
      updated
    );
    if (isUpdated == null) {
      return false;
    }
    return true;
  }
  public ArrayList<String> allCustomerNames(){
    ArrayList<String> names = new ArrayList<>();
    collection.find().forEach(e->{
      names.add(e.getName());
    });
    return names;
  }
}
