package com.book.Loaders.Inventory;

import com.book.DAOs.PromotionalPriceDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.Setter;

@Setter
public class InventoryPartDetailPromoCard {

  @FXML
  private Label Price;

  @FXML
  private Label Charge;

  @FXML
  private Label Count;

  InventoryPartDetail parent;
  PromotionalPriceDAO self;

  @FXML
  void UpdatePromo() {
    System.out.println(this.getClass().toString());
    parent.updatePromo(this, self);
  }

  public void setSelf(PromotionalPriceDAO dao) {
    this.self = dao;
    Price.setText(String.valueOf(self.getPurchasePrice()));
    Charge.setText(String.valueOf(self.getCharge()));
    Count.setText(String.valueOf(self.getQuanity()));
  }
}
