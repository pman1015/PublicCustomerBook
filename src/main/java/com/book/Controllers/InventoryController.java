package com.book.Controllers;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.*;

import com.book.DAOs.PartDAO;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import java.util.ArrayList;
import org.bson.types.ObjectId;

public class InventoryController extends DatabaseConnection {

  MongoCollection<PartDAO> collection;

  public InventoryController() {
    collection = database.getCollection("Inventory", PartDAO.class);
  }

  public void printAll() {
    System.out.println(collection.countDocuments());
    collection
      .find()
      .forEach(d -> {
        System.out.println(d.toString());
      });
  }

  public void makeNew(
    String partNumber,
    int inStock,
    double charge,
    double price
  ) {
    PartDAO part = new PartDAO();
    part.setPartNumber(partNumber);
    part.setInStock(inStock);
    part.setCharge(charge);
    part.setPrice(price);
    part.setEquipment(new ArrayList<>());
    System.out.println(part.toString());

    collection.insertOne(part);
  }

  public boolean checkPartNumber(String name) {
    return collection.find(eq("partNumber", name)).first() == null;
  }

  public InsertOneResult addPart(PartDAO dao) {
    return collection.insertOne(dao);
  }

  public ArrayList<PartDAO> getAllPartsList() {
    ArrayList<PartDAO> toReturn = new ArrayList<>();
    collection.find().into(toReturn);
    return toReturn;
  }

  public boolean findAndAddEquipment(String partNumber, ObjectId equipment) {
    PartDAO part = collection.find(eq("partNumber", partNumber)).first();
    part.addEquipment(equipment);
    return collection.findOneAndReplace(eq("_id", part.getId()), part) != null;
  }

  public String getInventory(String partNumber) {
    PartDAO dao = collection.find(eq("partNumber", partNumber)).first();
    if (dao != null) {
      return Integer.toString(dao.getInStock());
    } else return "Error";
  }

  public boolean updatePart(PartDAO update) {
    return (
      collection.findOneAndReplace(eq("_id", update.getId()), update) != null
    );
  }

  public ArrayList<PartDAO> findByString(String search) {
    ArrayList<PartDAO> toReturn = new ArrayList<>();
    collection
      .find(Filters.regex("partNumber", ".*" + search + ".*"))
      .forEach(e -> {
        toReturn.add(e);
      });
    return toReturn;
  }
  public PartDAO findByPartNumber(String partNumber){
    return collection.find(eq("partNumber",partNumber)).first();
  }
  public ArrayList<PartDAO> findAllPartsByNumber(ArrayList<String> partrNumbers){
    ArrayList<PartDAO>toReturn = new ArrayList<>();
    collection.find(in("partNumber",partrNumbers)).into(toReturn);
    return toReturn;
  }
}
