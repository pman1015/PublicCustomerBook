package com.book.Models.Equipment;

import com.book.Controllers.EquipmentController;
import com.book.Controllers.InventoryController;
import com.book.Controllers.MachineController;
import com.book.DAOs.CustomerEquipmentDAO;
import com.book.DAOs.EquipmentDAO;
import com.book.DAOs.PartDAO;
import com.book.Loaders.Equipment.EquipmentDetailLoader;
import com.book.Main;
import java.io.IOException;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipmentDetailInfo {

  @FXML
  Pane Self;

  @FXML
  TextArea ModelNotes;

  @FXML
  TextArea MachineNotes;

  @FXML
  VBox CompatiblePartsContainer;

  @FXML
  VBox PartReplacmentContainer;

  @FXML
  Text LastWorkedOn;

  @FXML
  Text MachineModel;

  //refrence to parent controller
  EquipmentDetailLoader parent;
  //Self data
  CustomerEquipmentDAO refrence;
  //Database controller for euipment
  EquipmentController ec;
  //Refrence for the equipment dao
  EquipmentDAO equipmentRefrence;

  //Inventory database refrence
  InventoryController ic;

  //Holder for all of the partDAOs
  ArrayList<PartDAO> parts;

  //Machine Controller
  MachineController mc;

  @FXML
  public void initialize() {
    //Initalize the database connections
    ec = new EquipmentController();
    ic = new InventoryController();
    mc = new MachineController();
    //
    parts = new ArrayList<>();

    //Listner for page flip to apply changes
    Self
      .visibleProperty()
      .addListener(
        new ChangeListener<Boolean>() {
          @Override
          public void changed(
            ObservableValue<? extends Boolean> observable,
            Boolean oldValue,
            Boolean newValue
          ) {
            if (!newValue) {
              mc.updateMachine(refrence);
              ec.updateEquipment(equipmentRefrence);
            }
          }
        }
      );

    //Set the two notes fields
    MachineNotes
      .focusedProperty()
      .addListener(
        new ChangeListener<Boolean>() {
          @Override
          public void changed(
            ObservableValue<? extends Boolean> observable,
            Boolean oldValue,
            Boolean newValue
          ) {
            if (!newValue) {
              if (!MachineNotes.getText().equals(refrence.getNotes())) {
                refrence.setNotes(MachineNotes.getText());
              }
            }
          }
        }
      );
    ModelNotes
      .focusedProperty()
      .addListener(
        new ChangeListener<Boolean>() {
          @Override
          public void changed(
            ObservableValue<? extends Boolean> observable,
            Boolean oldValue,
            Boolean newValue
          ) {
            if (!newValue) {
              if (!ModelNotes.getText().equals(equipmentRefrence.getNotes())) {
                equipmentRefrence.setNotes(ModelNotes.getText());
              }
            }
          }
        }
      );
  }

  @FXML
  public void Close() {
    parent.setRightSideContentString("None");
    parent.updatePageContent();
  }

  public void setRefrence(CustomerEquipmentDAO dao) {
    refrence = dao;
    //Set the text fields
    MachineNotes.setText(dao.getNotes());
    MachineModel.setText(dao.getModelNumber());
    LastWorkedOn.setText(dao.getLastWorkedOn());

    //Get all of the compatible parts for the machine
    equipmentRefrence = ec.findByID(dao.getEquipmentID());
    //Gate for null equipment refrence
    if (equipmentRefrence == null) {
      return;
    }
    //set the equipment notes
    ModelNotes.setText(equipmentRefrence.getNotes());

    //Set Compatible parts
    CompatiblePartsContainer.getChildren().clear();
    if (equipmentRefrence.getParts() != null) {
      parts = ic.findAllPartsByNumber(equipmentRefrence.getParts());
      for (PartDAO part : parts) {
        FXMLLoader partCardLoader = new FXMLLoader(
          Main.class.getResource("Scenes/Equipment/EquipmentDetilPartCard.fxml")
        );
        try {
          Node Card = partCardLoader.load();
          EquipmentDetailPartCard controller = partCardLoader.getController();
          controller.setPartDAO(part);
          CompatiblePartsContainer.getChildren().add(Card);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
