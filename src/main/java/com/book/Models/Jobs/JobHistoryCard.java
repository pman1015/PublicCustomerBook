package com.book.Models.Jobs;

import com.book.DAOs.JobDAO;
import com.book.Loaders.Jobs.JobDetailLoader;
import com.book.Loaders.Jobs.JobHistoryLoader;
import com.book.Main;
import com.book.Models.Status;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class JobHistoryCard {

  @FXML
  Text JobName;

  @FXML
  TextArea JobDescription;

  @FXML
  Text StartEndDate;

  @FXML
  Circle StatusIndicator;

  public JobHistoryLoader parent;
  JobDAO refrence;
  Status s;

  @FXML
  public void initialize() {}

  public void setUP(
    String JobName,
    String JobDescription,
    String StartEndDate,
    String Status,
    JobHistoryLoader parent,
    JobDAO refrence
  ) {
    this.refrence = refrence;
    this.parent = parent;
    this.JobName.setText(JobName);
    this.JobDescription.setText(JobDescription);
    this.StartEndDate.setText(StartEndDate);
    s = new Status(Status, StatusIndicator);
    StatusIndicator = s.getIndicator();
  }

  public void JobSelected() {
    FXMLLoader loader = new FXMLLoader(
      Main.class.getResource("Scenes/Jobs/JobDetail.fxml")
    );
    try {
      Node Content = loader.load();
      JobDetailLoader jdl = loader.getController();
      jdl.setParent(parent.getSelf());
      jdl.setBp(parent.getBp());
      jdl.setSelfRefrence(refrence.clone());
      parent.getBp().setCenter(Content);
    } catch (IOException | CloneNotSupportedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
