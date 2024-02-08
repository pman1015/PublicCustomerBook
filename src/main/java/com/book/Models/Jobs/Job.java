package com.book.Models.Jobs;

import com.book.DAOs.JobDAO;
import com.book.Loaders.Jobs.JobDetailLoader;
import com.book.Main;
import com.book.Models.Status;
import java.io.IOException;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Job {

  @FXML
  AnchorPane StatusHover;

  @FXML
  Label StatusTip;

  @FXML
  Text JobName;

  @FXML
  Text CustomerName;

  @FXML
  Circle Status;

  @FXML
  TextArea JobNameArea;

  @FXML
  Text StartEndDate;

  BorderPane bp;
  Node parent;

  String type;
  Status s;
  JobDAO selfRefrence;

  @FXML
  public void initialize() {
    //Set the type of Job card based on if there is a StartEndDate
    if (StartEndDate == null) {
      type = "HomePage";
    } else {
      type = "JobPage";
    }
    StatusTip.setVisible(false);
    StatusHover
      .hoverProperty()
      .addListener(
        new ChangeListener<Boolean>() {
          @Override
          public void changed(
            ObservableValue<? extends Boolean> observable,
            Boolean oldValue,
            Boolean newValue
          ) {
            if (newValue) {
              StatusTip.setVisible(true);
            } else {
              StatusTip.setVisible(false);
            }
          }
        }
      );
  }

  public void setUp(String jobName, String customerName, String status) {
    s = new Status(status, Status);
    JobName.setText(jobName);
    CustomerName.setText(customerName);
    Status = s.getIndicator();
    StatusTip.setText(status);
  }

  public void setUp(
    String jobName,
    String customerName,
    String status,
    String startEndDate
  ) {
    if (type.equals("HomePage")) {
      return;
    }
    s = new Status(status, Status);
    CustomerName.setText(customerName);
    Status = s.getIndicator();
    JobNameArea.setText(jobName);
    StartEndDate.setText(startEndDate);
    StatusTip.setText(status);
  }

  public void GoToJob() {
    FXMLLoader loader = new FXMLLoader(
      Main.class.getResource("Scenes/Jobs/JobDetail.fxml")
    );
    parent.setVisible(false);
    try {
      Node Content = loader.load();
      JobDetailLoader jdl = loader.getController();
      jdl.setParent(parent);
      jdl.setBp(bp);
      jdl.setSelfRefrence(selfRefrence);
      bp.setCenter(Content);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
