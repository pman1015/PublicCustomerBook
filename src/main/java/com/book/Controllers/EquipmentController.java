package com.book.Controllers;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.*;

import com.book.DAOs.EquipmentDAO;
import com.book.DAOs.PartDAO;
import com.mongodb.client.MongoCollection;
import java.util.ArrayList;
import org.bson.types.ObjectId;

public class EquipmentController extends DatabaseConnection {

  MongoCollection<EquipmentDAO> collection;

  public EquipmentController() {
    collection = database.getCollection("Equipment", EquipmentDAO.class);
  }

  public EquipmentDAO getFirst() {
    return collection.find().first();
  }

  public void printAll() {
    collection
      .find()
      .forEach(d -> {
        System.out.println(d.toString());
      });
  }

  public EquipmentDAO findByID(ObjectId id) {
    EquipmentDAO result = collection.find(eq("_id", id)).first();
    return result;
  }

  public ArrayList<EquipmentDAO> getEquipment(int size, int skip) {
    ArrayList<EquipmentDAO> result = new ArrayList<>();
    collection
      .find()
      .limit(size)
      .skip(skip)
      .forEach(e -> {
        result.add(e);
      });
    return result;
  }

  public ArrayList<String> getAllModels() {
    ArrayList<String> toReturn = new ArrayList<>();
    collection
      .find()
      .forEach(e -> {
        toReturn.add(e.getModelNumber());
      });

    return toReturn;
  }

  public boolean makeEquipment(EquipmentDAO dao) {
    return collection.insertOne(dao) != null;
  }

  public boolean validName(String name) {
    if (collection.find(eq("modelNumber", name)).first() != null) {
      return false;
    }
    return true;
  }

  public boolean updateEquipment(EquipmentDAO update) {
    return (
      collection.findOneAndReplace(eq("_id", update.getId()), update) != null
    );
  }

  public boolean addPart(EquipmentDAO dao, String PartNumber) {
    dao = collection.find(eq("_id", dao.getId())).first();
    dao.addPart(PartNumber);
    return updateEquipment(dao);
  }

  public ObjectId getID(String modelNumber) {
    EquipmentDAO temp = collection.find(eq("modelNumber", modelNumber)).first();
    return temp.getId();
  }
}
