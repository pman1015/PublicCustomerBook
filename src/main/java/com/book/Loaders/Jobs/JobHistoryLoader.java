package com.book.Loaders.Jobs;

import com.book.Controllers.JobController;
import com.book.DAOs.JobDAO;
import com.book.Loaders.Customer.MoreInfoLoader;
import com.book.Main;
import com.book.Models.Jobs.JobHistoryCard;
import java.io.IOException;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public class JobHistoryLoader {

  @FXML
  VBox JobCardContainer;

  @FXML
  Pane Self;

  MoreInfoLoader parent;
  BorderPane bp;

  String customer;
  ArrayList<JobHistoryCard> jobs;
  ArrayList<JobDAO> jobDAOs;

  JobDAO testEntry = new JobDAO();
  JobController jc = new JobController();

  @FXML
  public void initialize() {
    jobDAOs = new ArrayList<>();
    jobs = new ArrayList<>();
    //Add a listner to reload when page visiblity changes
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
            System.out.println("Change in visibility");
            setHistory(customer);
          }
        }
      );
  }

  public void setHistory(String customer) {
    //Add a database call to get a list of Jobs
    JobCardContainer.getChildren().clear();
    jobDAOs = jc.getJobHistory(customer, 50, 0);
    this.customer = customer;
    for (JobDAO jd : jobDAOs) {
      FXMLLoader JHL = new FXMLLoader(
        Main.class.getResource("Scenes/Jobs/JobHistoryCard.fxml")
      );
      try {
        //Load the node and add it to the VBox
        Node newJob = JHL.load();
        JobCardContainer.getChildren().addAll(newJob);

        //Update the JobHistoryCard Controller
        ((JobHistoryCard) JHL.getController()).setUP(
            jd.getJobName(),
            jd.getDetails(),
            jd.getStartDate() + "-" + jd.getEndDate(),
            jd.getStatus(),
            this,
            jd.clone()
          );
        jobs.add((JobHistoryCard) JHL.getController());
      } catch (IOException | CloneNotSupportedException e) {
        e.printStackTrace();
      }
    }
  }

  public void setParent(MoreInfoLoader parent) {
    this.parent = parent;
  }

  public void setBP(BorderPane bp) {
    this.bp = bp;
  }

  public void Back() {
    bp.setCenter(parent.getSelf());
  }

  @FXML
  public void CreateNewJob(ActionEvent event) {
    //Load the Create New Job screen
    FXMLLoader loader = new FXMLLoader(
      Main.class.getResource("Scenes/Jobs/JobCreate.fxml")
    );
    try {
      Node content = loader.load();
      JobCreate controller = loader.getController();

      controller.setParent(
        (
          (BorderPane) (((Button) event.getSource()).getScene().getRoot())
        ).getCenter()
      );
      if (controller.setCustomerNameString(customer)) {
        Self.setVisible(false);
        //Set the create as the displayed node
        (
          (BorderPane) (((Button) event.getSource()).getScene().getRoot())
        ).setCenter(content);
      }
    } catch (IOException e) {
      System.out.println("Error Loading Create Job!");
      e.printStackTrace();
    }
  }
}
