package com.book.DAOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CustomerEquipmentDAO {

  ObjectId id;
  String modelNumber;
  ObjectId equipmentID;
  String customer;
  String lastWorkedOn;
  String notes;

 

  public CustomerEquipmentDAO(String machineName, ObjectId equipmentID,String cutomer){
    this.modelNumber = machineName;
    this.equipmentID = equipmentID;
    this.customer = cutomer;
    lastWorkedOn = "Never";
    notes = "";
  }
}
