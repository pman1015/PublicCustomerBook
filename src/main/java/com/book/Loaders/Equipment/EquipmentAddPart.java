package com.book.Loaders.Equipment;

import com.book.Controllers.InventoryController;
import com.book.DAOs.EquipmentDAO;
import com.book.DAOs.PartChargeDAO;
import com.book.DAOs.PartDAO;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EquipmentAddPart {

  EquipmentLoader parent;
  EquipmentDAO refrence;
  InventoryController ic;
  ArrayList<PartDAO> parts;

  @FXML
  void initialize() {
    ic = new InventoryController();
  }

  @FXML
  private ComboBox<String> PartNumber;

  @FXML
  private Text PartNumberError;

  @FXML
  void Close(ActionEvent event) {
    parent.setRightContent("Details");
    parent.updateEquipment();
    parent.detailsController.updateParts();
    parent.updateContent();
  }

  @FXML
  void AddPart(ActionEvent event) {
    ic.findAndAddEquipment(PartNumber.getValue(), refrence.getId());
    parent.ec.addPart(refrence, PartNumber.getValue());
    Close(null);
  }

  public void setRefrence(EquipmentDAO refrence) {
    this.refrence = refrence;
    setComboBox();
  }

  public void setComboBox() {
    if (refrence == null) {
      return;
    }
    PartNumber.getItems().clear();
    parts = ic.getAllPartsList();
    ObservableList<String> partStrings = FXCollections.observableArrayList();
    for (PartDAO dao : parts) {
      if (!refrence.getParts().contains(dao.getPartNumber())) {
        partStrings.add(dao.getPartNumber());
      }
    }
    PartNumber.setItems(partStrings);
  }
}
