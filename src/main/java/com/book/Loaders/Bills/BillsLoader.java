package com.book.Loaders.Bills;

import com.book.Controllers.JobController;
import com.book.DAOs.JobDAO;
import com.book.DAOs.PartDAO;
import com.book.Main;
import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class BillsLoader {

  @FXML
  private AnchorPane RightSideContent;

  @FXML
  private AnchorPane LeftSideContent;

  @FXML
  private Pane Self;

  //Strings to set what should be shown on each side
  String leftContent = "completedJobList";
  String rightContent = "";

  //Nodes for the content options for each side
  Node completedJobList;
  Node billInfo;
  //Controllers for each node
  CompletedJobList completedJobListController;
  BillInfo billInfoController;

  @FXML
  public void initialize() {
    //Check to ensure all FXML components were loaded
    assert RightSideContent !=
    null : "fx:id=\"RightSideContent\" was not injected: check your FXML file 'BillPage.fxml'.";
    assert LeftSideContent !=
    null : "fx:id=\"LeftSideContent\" was not injected: check your FXML file 'BillPage.fxml'.";
    assert Self !=
    null : "fx:id=\"Self\" was not injected: check your FXML file 'BillPage.fxml'.";

    //Set the left and right side Nodes
    FXMLLoader completedJobLoader = new FXMLLoader(
      Main.class.getResource("Scenes/Bills/CompletedJobList.fxml")
    );
    FXMLLoader billInfoLoader = new FXMLLoader(
      Main.class.getResource("Scenes/Bills/BillInfo.fxml")
    );
    try {
      completedJobList = completedJobLoader.load();
      completedJobListController = completedJobLoader.getController();
      completedJobListController.setBillController(this);

      billInfo = billInfoLoader.load();
      billInfo.setVisible(false);
      billInfoController = billInfoLoader.getController();
      billInfoController.setBillController(this);

    } catch (IOException e) {
      e.printStackTrace();
    }
    update();

    Self
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
              update();
            }
          }
        }
      );
  }

  public void update() {
    if (LeftSideContent.getChildren().size() > 0) {
      LeftSideContent.getChildren().get(0).setVisible(false);
    }
    //Set the left Content
    LeftSideContent.getChildren().clear();
    switch (leftContent) {
      case "completedJobList":
        LeftSideContent.getChildren().add(completedJobList);
        LeftSideContent.getChildren().get(0).setVisible(true);
        break;
      default:
        break;
    }
    if (RightSideContent.getChildren().size() > 0) {
      RightSideContent.getChildren().get(0).setVisible(false);
    }
    //Set the right side Content
    RightSideContent.getChildren().clear();
    switch (rightContent) {
      case "billInfo":
        RightSideContent.getChildren().add(billInfo);
        RightSideContent.getChildren().get(0).setVisible(true);
        break;
      default:
        break;
    }
  }

  public void updateRightContent(JobDAO dao) {
    billInfoController.setSelfRefrence(dao);
    rightContent = "billInfo";
    update();
  }
}
