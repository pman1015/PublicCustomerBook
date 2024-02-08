package com.book.Loaders.Equipment;

import com.book.DAOs.EquipmentDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import lombok.Setter;

@Setter
public class EquipmentCard {

  @FXML
  private Text ModelNumber;

  public void EquipmentDetail() {
    parent.setDetails(dao);
  }

  EquipmentLoader parent;
  EquipmentDAO dao;

  public void setDao(EquipmentDAO dao) {
    this.dao = dao;
    ModelNumber.setText(dao.getModelNumber());
  }
}
