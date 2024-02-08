package com.book.Loaders.Equipment;

import com.book.Controllers.InventoryController;
import com.book.DAOs.EquipmentDAO;
import com.book.Main;
import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class EquipmentPageDetail {

  @FXML
  private Text ModelNumber;

  @FXML
  private VBox PartsContainer;

  @FXML
  private TextArea EquipmentNotes;

  @FXML
  private AnchorPane EquipmentDetails;

  EquipmentDAO self;
  EquipmentLoader parent;
  ArrayList<EquipmentPageDetailPart> partsController;

  @FXML
  void initialize() {
    EquipmentNotes
      .focusedProperty()
      .addListener((ov, oldValue, newValue) -> {
        if (!newValue) {
          NonFocus();
        }
      });
  }

  public void NonFocus() {
    self.setNotes(EquipmentNotes.getText());
    parent.updateEquipmentDAO(self);
  }

  @FXML
  void AddNewPart(ActionEvent event) {
    parent.setRightContent("MakePart");
    parent.makePartController.setRefrence(self);
    parent.updateContent();
  }

  @FXML
  void AddPartFromInventory(ActionEvent event) {
    parent.setRightContent("AddPart");
    parent.addPartController.setRefrence(self);
    parent.updateContent();
  }

  public void setValues(EquipmentDAO dao) {
    self = dao;
    ModelNumber.setText(dao.getModelNumber());
    EquipmentNotes.setText(dao.getNotes());
    updateParts();
  }

  @FXML
  void Close() {
    parent.Close();
  }

  public void updateParts() {
    InventoryController ic = new InventoryController();
    PartsContainer.getChildren().clear();
    partsController = new ArrayList<>();
    for (String Part : self.getParts()) {
      FXMLLoader loader = new FXMLLoader(
        Main.class.getResource("Scenes/Equipment/EquipmentPageDetailPart.fxml")
      );
      try {
        Node content = loader.load();
        EquipmentPageDetailPart controller = loader.getController();
        controller.getPartNumber().setText(Part);
        controller.getQuanty().setText(ic.getInventory(Part));
        controller.setParent(this);
        partsController.add(controller);
        PartsContainer.getChildren().add(content);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }
}
