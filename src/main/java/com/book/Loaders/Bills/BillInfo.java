package com.book.Loaders.Bills;

import com.book.DAOs.JobDAO;
import com.book.DAOs.LaborChargeDAO;
import com.book.DAOs.PartChargeDAO;
import com.book.Utilities.Invoice.InvoiceGenerator;
import com.book.Main;
import java.io.IOException;
import java.sql.Date;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillInfo {

  @FXML
  private Text DeliveryCharge;

  @FXML
  private VBox LaborCharges;

  @FXML
  private Text DateCompleted;

  @FXML
  private Text JobName;

  @FXML
  private AnchorPane BillInfo;

  @FXML
  private Text BillStatus;

  @FXML
  private Text BillTotal;

  @FXML
  private VBox PartCharges;

  @FXML
  void DownloadInvoice() {
    if(selfRefrence != null){
        new InvoiceGenerator(selfRefrence);
    }
  }

  JobDAO selfRefrence;
  BillsLoader billController;

  @FXML
  void initialize() {
    assert DeliveryCharge !=
    null : "fx:id=\"DeliveryCharge\" was not injected: check your FXML file 'BillInfo.fxml'.";
    assert LaborCharges !=
    null : "fx:id=\"LaborCharges\" was not injected: check your FXML file 'BillInfo.fxml'.";
    assert DateCompleted !=
    null : "fx:id=\"DateCompleted\" was not injected: check your FXML file 'BillInfo.fxml'.";
    assert JobName !=
    null : "fx:id=\"JobName\" was not injected: check your FXML file 'BillInfo.fxml'.";
    assert BillInfo !=
    null : "fx:id=\"BillInfo\" was not injected: check your FXML file 'BillInfo.fxml'.";
    assert BillStatus !=
    null : "fx:id=\"BillStatus\" was not injected: check your FXML file 'BillInfo.fxml'.";
    assert BillTotal !=
    null : "fx:id=\"BillTotal\" was not injected: check your FXML file 'BillInfo.fxml'.";
    assert PartCharges !=
    null : "fx:id=\"PartCharges\" was not injected: check your FXML file 'BillInfo.fxml'.";

    BillInfo
      .visibleProperty()
      .addListener(
        new ChangeListener<Boolean>() {
          @Override
          public void changed(
            ObservableValue<? extends Boolean> observable,
            Boolean oldValue,
            Boolean newValue
          ) {
            if (newValue) {
              System.out.println("Update BillInfo");
              updateContent();
            }
          }
        }
      );
  }

  public void updateContent() {
    if (selfRefrence != null) {
      selfRefrence.getBill().updateCost();
      BillStatus.setText(
        selfRefrence.getStatus().equals("CompletedPaid") ? "Paid" : "UnPaid"
      );
      DateCompleted.setText(selfRefrence.getEndDate());
      JobName.setText(selfRefrence.getJobName());
      PartCharges.getChildren().clear();
      if (selfRefrence.getBill().getPartCharges() != null) {
        for (PartChargeDAO dao : selfRefrence.getBill().getPartCharges()) {
          FXMLLoader partChargeLoader = new FXMLLoader(
            Main.class.getResource("Scenes/Bills/BillPartCharge.fxml")
          );
          try {
            PartCharges.getChildren().add(partChargeLoader.load());
            ((BillPartCharge) partChargeLoader.getController()).setInfo(dao);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
      LaborCharges.getChildren().clear();
      if (selfRefrence.getBill().getLaborCharges() != null) {
        for (LaborChargeDAO dao : selfRefrence.getBill().getLaborCharges()) {
          FXMLLoader laborChargeLoader = new FXMLLoader(
            Main.class.getResource("Scenes/Bills/BillLaborCharge.fxml")
          );
          try {
            LaborCharges.getChildren().add(laborChargeLoader.load());
            ((BillLaborCharge) laborChargeLoader.getController()).setInfo(dao);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
      DeliveryCharge.setText("$" + selfRefrence.getBill().getDeliveryCost());
      BillTotal.setText("$" + selfRefrence.getBill().getBillTotal());
    }
  }
}
