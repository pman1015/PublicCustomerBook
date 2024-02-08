package com.book.Loaders.Inventory;

import com.book.DAOs.PartDAO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InventoryCard {

  @FXML
  private Text RetailPrice;

  @FXML
  private Text PartNumber;

  @FXML
  private Text InStock;

  @FXML
  private Text PurchasePrice;

  @FXML
  void initialize() {
    assert RetailPrice !=
    null : "fx:id=\"RetailPrice\" was not injected: check your FXML file 'InventoryCard.fxml'.";
    assert PartNumber !=
    null : "fx:id=\"PartNumber\" was not injected: check your FXML file 'InventoryCard.fxml'.";
    assert InStock !=
    null : "fx:id=\"InStock\" was not injected: check your FXML file 'InventoryCard.fxml'.";
    assert PurchasePrice !=
    null : "fx:id=\"PurchasePrice\" was not injected: check your FXML file 'InventoryCard.fxml'.";
  }

  PartDAO selfRefrence;
  InventoryLoader parent;

  public void setSelfRefrence(PartDAO dao) {
    this.selfRefrence = dao;
    setContent();
  }

  public void setContent() {
    PartNumber.setText(selfRefrence.getPartNumber());
    InStock.setText(Integer.toString(selfRefrence.getInStock()));
    PurchasePrice.setText("$" + Double.toString(selfRefrence.getPrice()));
    RetailPrice.setText("$" + Double.toString(selfRefrence.getCharge()));
  }

  @FXML
  public void LoadDetails() {
    parent.setRightSideContent("partDetail");
    parent.getPartDetailController().setSelf(selfRefrence);
    parent.updateContent();
  }
}
