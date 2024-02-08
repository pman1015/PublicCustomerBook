package com.book.DAOs;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;

@Getter
@Setter
@ToString
public class CustomerDAO {

  ObjectId id;
  String Name;
  String Alias;
  int Rating;
  String Address;
  String Notes;
  String phoneNumber;
  ArrayList<ObjectId> JobIDs;
  ArrayList<ObjectId> MachineIDs;

  
}
