package com.book.DAOs;

import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class EquipmentDAO {

  ObjectId id;

 
  String ModelNumber;

 
  String Notes;

  
  ArrayList<String> Parts;

  public EquipmentDAO() {
    Parts = new ArrayList<>();
  }
  public void addPart(String PartNumber){
    if(Parts == null){
      Parts = new ArrayList<>();
    }
    Parts.add(PartNumber);
  }
}
