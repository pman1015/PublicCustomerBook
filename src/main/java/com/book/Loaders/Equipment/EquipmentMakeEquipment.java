package com.book.Loaders.Equipment;

import com.book.Controllers.EquipmentController;
import com.book.DAOs.EquipmentDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import lombok.Setter;

@Setter
public class EquipmentMakeEquipment {

  @FXML
  Text ModelNameError;

  @FXML
  private TextArea ModelNotes;

  @FXML
  private TextField ModelName;

  EquipmentLoader parent;
  EquipmentController ec;

  @FXML
  void initialize() {
    ec = new EquipmentController();
  }

  @FXML
  void Close(ActionEvent event) {
    clear();
    parent.Close();
  }

  @FXML
  void AddModel(ActionEvent event) {
    //Check the name
    if (ec.validName(ModelName.getText())) {
      EquipmentDAO dao = new EquipmentDAO();
      dao.setModelNumber(ModelName.getText());
      dao.setNotes(ModelNotes.getText());
      if (ec.makeEquipment(dao)) {
        clear();
        parent.updateEquipment();
        parent.Close();
      }
    } else {
      ModelNameError.setText("Model Name Already Exists");
      ModelNameError.setVisible(true);
    }
  }

  public void clear() {
    ModelName.setText("");
    ModelNotes.setText("");
    ModelNameError.setVisible(false);
  }
}
