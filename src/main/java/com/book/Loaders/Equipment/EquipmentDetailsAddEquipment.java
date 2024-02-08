package com.book.Loaders.Equipment;

import com.book.Controllers.EquipmentController;
import com.book.Controllers.MachineController;
import com.book.DAOs.CustomerEquipmentDAO;
import com.mongodb.client.result.InsertOneResult;
import java.util.ArrayList;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
public class EquipmentDetailsAddEquipment {

  @FXML
  private ComboBox<String> ModelNumbers;

  @FXML
  void Close(ActionEvent event) {
    ModelNumbers.setValue(null);
    parent.setRightSideContentString("None");
    parent.updatePageContent();
  }

  @FXML
  void AddEquipment(ActionEvent event) {
    if (ModelNumbers.getValue() == null) {
      return;
    }
   
    ObjectId equipmentID = ec.getID(ModelNumbers.getValue());
    if (equipmentID == null) {
      return;
    }
    CustomerEquipmentDAO newMachine = new CustomerEquipmentDAO(
      ModelNumbers.getValue(),
      equipmentID,
      customerName
    );
    CustomerEquipmentDAO result = mc.addMachine(newMachine);
    if (result != null) {
      parent.updateCustomer(result.getId());
    }
    parent.setRightSideContentString("none");
    parent.updatePageContent();
  }

  ObservableList<String> AvailableNumbers;
  EquipmentDetailLoader parent;
  //Database Controllers
  MachineController mc;
  //customer Name
  String customerName;
  //EquipmentDatabase to get EquipmentID
  EquipmentController ec;

  @FXML
  public void initialize() {
    mc = new MachineController();
    ec = new EquipmentController();
  }

  public void updateModels(ArrayList<String> models) {
    AvailableNumbers = FXCollections.observableArrayList();
    AvailableNumbers.addAll(models);
    ModelNumbers.setItems(AvailableNumbers);
  }
}
