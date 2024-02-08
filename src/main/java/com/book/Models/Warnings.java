package com.book.Models;

import com.book.Loaders.Inventory.InventoryPartDetail;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Warnings {

  @FXML
  private HBox OneChoice;

  @FXML
  private Label Message;

  @FXML
  private HBox TwoChoices;

  @FXML
  private Button Confirm;

  @FXML
  private Button SecondChoice;

  @FXML
  private Button FirstChoice;

  boolean returnValue;

  @FXML
  void Confirm(ActionEvent event) {
    returnValue = true;
    Stage s = (Stage) ((Button) event.getSource()).getScene().getWindow();
    s.close();
  }

  @FXML
  void Cancel(ActionEvent event) {
    Stage s = (Stage) ((Button) event.getSource()).getScene().getWindow();
    s.close();
  }

  @FXML
  void initalize() {
    returnValue = false;
  }

  public boolean getReturnValue() {
    return returnValue;
  }

  public void singleButton() {
    TwoChoices.setVisible(false);
    OneChoice.setVisible(true);
    OneChoice.toFront();
  }

  public void twoButton() {
    TwoChoices.setVisible(true);
    OneChoice.setVisible(false);
    TwoChoices.toFront();
  }
}
