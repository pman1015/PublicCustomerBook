package com.book.Models.Equipment;

import com.book.DAOs.CustomerEquipmentDAO;
import com.book.Loaders.Equipment.EquipmentDetailLoader;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipmentDetailCard {

  @FXML
  Text ModelName;

  @FXML
  Text LastWorkedOn;

  EquipmentDetailLoader parent;
  CustomerEquipmentDAO self;

  @FXML
  public void initialize() {}

  public void setSelf(CustomerEquipmentDAO dao) {
    this.self = dao;
    //Set the text
    ModelName.setText(dao.getModelNumber());
    LastWorkedOn.setText(dao.getLastWorkedOn());
  }

  public void getDetails() {
    parent.getEquipmentInfoController().setRefrence(self);
    parent.setRightSideContentString("Info");
    parent.updatePageContent();
  }
}
