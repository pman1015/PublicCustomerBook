package com.book.Loaders.Bills;

import com.book.Controllers.JobController;
import com.book.DAOs.JobDAO;
import com.book.Main;
import java.io.IOException;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CompletedJobList {

  @FXML
  private TextField SearchField;

  @FXML
  private VBox CompletedJobContainer;

  @FXML
  private AnchorPane JobList;

  @FXML
  private ToggleButton SearchToggle;

  @FXML
  void toggleSearchType(ActionEvent event) {
    if (searchType.equals("Customer Name")) {
      searchType = "Job Name";
    } else {
      searchType = "Customer Name";
    }
    SearchToggle.setText(searchType);
  }

  @FXML
  void Search(ActionEvent event) {}

  String searchType = "Customer Name";
  ArrayList<JobDAO> completedJobs;
  //Job database Controller
  JobController jc;

  //Controller for the Bills Loader
  BillsLoader billController;

  @FXML
  void initialize() {
    //Ensure all Components were injected
    assert SearchField !=
    null : "fx:id=\"SearchField\" was not injected: check your FXML file 'CompletedJobList.fxml'.";
    assert CompletedJobContainer !=
    null : "fx:id=\"CompletedJobContainer\" was not injected: check your FXML file 'CompletedJobList.fxml'.";
    assert JobList !=
    null : "fx:id=\"JobList\" was not injected: check your FXML file 'CompletedJobList.fxml'.";
    assert SearchToggle !=
    null : "fx:id=\"SearchToggle\" was not injected: check your FXML file 'CompletedJobList.fxml'.";
    completedJobs = new ArrayList<>();
    //Update the label on the toggle
    SearchToggle.setText(searchType);
    //Initalise the Job controller
    jc = new JobController();
    updateJobs();

    JobList
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
              System.out.println("UpdateList");
              updateJobs();
            }
          }
        }
      );
  }

  /*
 * public void setBillController(BillsLoader billController) {
    this.billController = billController;
    updateJobs();
  }

 */

  private void updateJobs() {
    completedJobs = jc.getCompleted();
    CompletedJobContainer.getChildren().clear();
    for (JobDAO dao : completedJobs) {
      FXMLLoader cardLoader = new FXMLLoader(
        Main.class.getResource("Scenes/Bills/CompletedJobCard.fxml")
      );
      try {
        Node Content = cardLoader.load();
        CompletedJobCard controller = cardLoader.getController();
        controller.setParent(JobList);
        controller.setSelfRefrence(dao);
        controller.setBillController(billController);
        CompletedJobContainer.getChildren().add(Content);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
