package com.book.Loaders.Jobs;

import com.book.Controllers.JobController;
import com.book.DAOs.JobDAO;
import com.book.Main;
import com.book.Models.Jobs.Job;
import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobLoader {

  @FXML
  VBox JobsContainer;

  private ArrayList<Node> jobs;
  private ArrayList<JobDAO> jobDAOs;
  public int loadCount;
  JobController jc;
  BorderPane bp;
  Node self;

  public JobLoader() {}

  @FXML
  public void initialize() {
    updateJobs();
  }

  public void updateJobs() {
    this.jobs = new ArrayList<>();
    jc = new JobController();
    loadCount = 7;
    jobDAOs = jc.getList(loadCount, 0);
    for (JobDAO job : jobDAOs) {
      try {
        FXMLLoader loader = new FXMLLoader(
          Main.class.getResource("Scenes/Jobs/JobCard.fxml")
        );
        Node content = loader.load();
        Job controller = loader.getController();
        controller.setBp(bp);
        controller.setParent(self);
        controller.setSelfRefrence(job);
        job.toJob(controller);
        jobs.add(content);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  @FXML
  public Node SetJobs(Node page) {
    updateJobs();
    loadCount = loadCount + 1;
    JobsContainer.getChildren().clear();
    for (Node j : jobs) {
      JobsContainer.getChildren().add(j);
    }
    System.out.println(loadCount);
    return page;
  }
}
