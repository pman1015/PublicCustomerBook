package com.book.Controllers;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.*;
import static com.mongodb.client.model.Updates.*;

import com.book.DAOs.CustomerEquipmentDAO;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import org.bson.types.ObjectId;

public class MachineController extends DatabaseConnection {

  MongoCollection<CustomerEquipmentDAO> collection;

  public MachineController() {
    collection = database.getCollection("Machines", CustomerEquipmentDAO.class);
  }

  public void printAll() {
    collection
      .find()
      .forEach(d -> {
        System.out.println(d.toString());
      });
  }

  public ArrayList<CustomerEquipmentDAO> findAll(ArrayList<ObjectId> ids) {
    ArrayList<CustomerEquipmentDAO> toReturn = new ArrayList<>();
    collection.find(in("_id", ids)).into(toReturn);
    return toReturn;
  }

  public CustomerEquipmentDAO addMachine(CustomerEquipmentDAO dao) {
    InsertOneResult result = collection.insertOne(dao);
    return collection.find(eq("_id", result.getInsertedId())).first();
  }

  public CustomerEquipmentDAO getByID(ObjectId id) {
    return collection.find(eq("_id", id)).first();
  }

  public ObjectId findCustomerMachineID(
    ObjectId equipmentID,
    String CustomerName
  ) {
    return collection
      .find(and(eq("customer", CustomerName), eq("equipmentID", equipmentID)))
      .first()
      .getId();
  }

  public boolean updateMachine(CustomerEquipmentDAO dao) {
    return collection.findOneAndReplace(eq("_id", dao.getId()), dao) != null;
  }

  public void workOnMachine(ObjectId machineID) {
    DateTimeFormatter df = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    collection.findOneAndUpdate(
      eq("_id", machineID),
      set("lastWorkedOn", df.format(LocalDate.now()))
    );
  }
}
