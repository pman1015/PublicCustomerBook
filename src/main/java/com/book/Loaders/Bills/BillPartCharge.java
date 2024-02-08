package com.book.Loaders.Bills;

import com.book.DAOs.PartChargeDAO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class BillPartCharge {

  @FXML
  private Label PartNumber;

  @FXML
  private Label Quanity;

  @FXML
  private Label Charge;

  @FXML
  private Label Cost;

  @FXML
  public void initialize() {
    assert PartNumber !=
    null : "fx:id=\"PartNumber\" was not injected: check your FXML file 'BillPartCharge.fxml'.";
    assert Quanity !=
    null : "fx:id=\"Quanity\" was not injected: check your FXML file 'BillPartCharge.fxml'.";
    assert Charge !=
    null : "fx:id=\"Charge\" was not injected: check your FXML file 'BillPartCharge.fxml'.";
    assert Cost !=
    null : "fx:id=\"Cost\" was not injected: check your FXML file 'BillPartCharge.fxml'.";
  }

  public void setInfo(PartChargeDAO dao) {
    PartNumber.setText(dao.getPartNumber());
    Charge.setText("$" + dao.getCharge());
    Cost.setText("$" + dao.getCost());
    Quanity.setText(String.valueOf(dao.getQuanity()));
  }
}
