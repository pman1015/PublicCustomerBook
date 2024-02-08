package com.book.Loaders.Equipment;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EquipmentPageDetailPart {

  @FXML
  private Text Quanty;

  @FXML
  private Text PartNumber;

  EquipmentPageDetail parent;

  @FXML
  public void Details() {
    System.out.println("Click");
    parent.getParent().setInventoryDetail(PartNumber.getText());
  }
}
