package com.book.Loaders.Equipment;

import com.book.Controllers.InventoryController;
import com.book.DAOs.EquipmentDAO;
import com.book.DAOs.PartDAO;
import com.mongodb.client.result.InsertOneResult;

import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import lombok.Setter;

@Setter
public class EquipmentPageMakePart {

  @FXML
  private Text ChargePriceError;

  @FXML
  private TextField PartNumber;

  @FXML
  private TextField PurchaseURL;

  @FXML
  private TextField ChargePrice;

  @FXML
  private TextField PurchasePrice;

  @FXML
  private Text PartNumberError;

  @FXML
  private Text PurchasePriceError;

  EquipmentLoader parent;
  InventoryController ic;
  EquipmentDAO refrence;

  @FXML
  void initialize() {
    ic = new InventoryController();
  }

  @FXML
  void Close(ActionEvent event) {
    clear();
    parent.setRightContent("Details");
    parent.updateContent();
  }

  @FXML
  void AddNewPart(ActionEvent event) {
    boolean fail = false;
    //Check if the part number exists
    if (PartNumber.getText().length() < 1) {
      PartNumberError.setText("Part Number Cannot be blank");
      PartNumberError.setVisible(true);
      fail = true;
    }
    if (!fail && !ic.checkPartNumber(PartNumber.getText())) {
      PartNumberError.setText("Part Number Already Exists");
      PartNumberError.setVisible(true);
      fail = true;
    }
    //Check purchase price
    if (PurchasePrice.getText().length() < 1) {
      PurchasePriceError.setText("You need to enter a price");
      PurchasePriceError.setVisible(true);
      fail = true;
    } else {
      if (!Pattern.matches("(\\-?\\d*\\.?\\d+)", PurchasePrice.getText())) {
        PurchasePriceError.setText("Set the price without a dollar sign");
        PurchasePriceError.setVisible(true);
        fail = true;
      }
    }
    //Check the Charge price
    if (ChargePrice.getText().length() < 1) {
      ChargePriceError.setText("You need to enter a price");
      ChargePriceError.setVisible(true);
      fail = true;
    } else {
      if (!Pattern.matches("(\\-?\\d*\\.?\\d+)", ChargePrice.getText())) {
        ChargePriceError.setText("Set the price without a dollar sign");
        ChargePriceError.setVisible(true);
        fail = true;
      }
    }
    if (fail) {
      return;
    }
    PartDAO dao = new PartDAO(
      PartNumber.getText(),
      PurchasePrice.getText(),
      ChargePrice.getText()
    );
    dao.setUrl(PurchaseURL.getText());
    dao.addEquipment(refrence.getId());
    InsertOneResult result = ic.addPart(dao);
    if(result != null){
        clear();
        parent.addMadePart(dao.getPartNumber(),refrence);
    }
     
  }

  public void clear() {
    PartNumber.setText(null);
    PartNumberError.setVisible(false);

    PurchasePrice.setText(null);
    PurchasePriceError.setVisible(false);

    ChargePrice.setText(null);
    ChargePriceError.setVisible(false);

    PurchaseURL.setText(null);
  }
}
