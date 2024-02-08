package com.book.Loaders.Bills;

import com.book.DAOs.JobDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompletedJobCard {

  @FXML
  private AnchorPane CompletedJobCard;

  @FXML
  private Label JobName;

  @FXML
  private Label CustomerName;

  @FXML
  void LoadJob() {
    if (billController == null) {
      System.out.println("No billController");
      return;
    }
    billController.updateRightContent(selfRefrence);
  }

  Node Parent;
  JobDAO selfRefrence;
  BillsLoader billController;

  @FXML
  void initialize() {
    assert CompletedJobCard !=
    null : "fx:id=\"CompletedJobCard\" was not injected: check your FXML file 'CompletedJobCard.fxml'.";
    assert JobName !=
    null : "fx:id=\"JobName\" was not injected: check your FXML file 'CompletedJobCard.fxml'.";
    assert CustomerName !=
    null : "fx:id=\"CustomerName\" was not injected: check your FXML file 'CompletedJobCard.fxml'.";
  }

  public void setSelfRefrence(JobDAO selfRefrence) {
    this.selfRefrence = selfRefrence;
    //Set the initial values
    JobName.setText(selfRefrence.getJobName());
    CustomerName.setText(selfRefrence.getCustomerName());
  }
}
