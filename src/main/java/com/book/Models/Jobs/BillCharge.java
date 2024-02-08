package com.book.Models.Jobs;

import com.book.DAOs.LaborChargeDAO;
import com.book.DAOs.PartChargeDAO;
import com.book.Loaders.Jobs.JobDetailLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillCharge {

  @FXML
  HBox self;

  @FXML
  Text FirstValue;

  @FXML
  Text SecondValue;

  JobDetailLoader parent;
  LaborChargeDAO lc;
  PartChargeDAO pc;

  @FXML
  public void UpdateCharge() {
    parent.setIsUpdate(true);
    if (lc == null) {
      parent.setUpdate(pc);
      parent.NewPartsCharge();
    } else {
      parent.setUpdate(lc);
      parent.NewLaborCharge();
    }
  }
}
