package com.book.Models.Equipment;

import com.book.Controllers.MachineController;
import com.book.DAOs.CustomerEquipmentDAO;
import com.book.Loaders.Customer.MoreInfoLoader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoreInfoEquipmentCard {

  @FXML
  Text ModelName;

  @FXML
  TextArea NotesField;

  Node Parent;
  CustomerEquipmentDAO selfRefrence;
  MachineController mc;

  @FXML
  public void initialize() {
    mc = new MachineController();
    NotesField
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
              if (!NotesField.getText().equals(selfRefrence.getNotes())) {
                selfRefrence.setNotes(NotesField.getText());
                mc.updateMachine(selfRefrence);
              }
            }
          }
        }
      );
  }

  public void setSelfRefrence(CustomerEquipmentDAO dao) {
    this.selfRefrence = dao;
    NotesField.setText(dao.getNotes());
    ModelName.setText(dao.getModelNumber());
  }
}
