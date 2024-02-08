package com.book.Loaders.Bills;

import com.book.DAOs.LaborChargeDAO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class BillLaborCharge {

  @FXML
  private URL location;

  @FXML
  private Label Hours;

  @FXML
  private Label Rate;

  @FXML
  private Label Charge;

  @FXML
  void initialize() {
    assert Hours !=
    null : "fx:id=\"Hours\" was not injected: check your FXML file 'BillLaborCharge.fxml'.";
    assert Rate !=
    null : "fx:id=\"Rate\" was not injected: check your FXML file 'BillLaborCharge.fxml'.";
    assert Charge !=
    null : "fx:id=\"Charge\" was not injected: check your FXML file 'BillLaborCharge.fxml'.";
  }

  public void setInfo(LaborChargeDAO dao) {
    Hours.setText(dao.getHours() + "Hrs");
    Rate.setText(dao.getRate() + "/Hr");
    Charge.setText("$" + String.valueOf(dao.getCost()));
  }
}
