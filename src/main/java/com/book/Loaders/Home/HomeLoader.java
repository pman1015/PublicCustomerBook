package com.book.Loaders.Home;

import com.book.Controllers.JobController;
import com.book.Controllers.ReportsController;
import com.book.DAOs.JobDAO;
import com.book.DAOs.ReportDAO;
import com.book.Main;
import com.book.Models.Expenses;
import com.book.Models.Jobs.Job;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HomeLoader {

  //Job Stats
  private int pendingJobsCount;
  private int newJobsCount;
  private int completedJobsCount;

  //income stats
  private double weeklyIncome;
  private double monthlyIncome;
  private double yearlyIncome;

  //Storage for mainpage fields
  private ArrayList<Node> recentJobs;
  private Expenses expenses;

  //max number of jobs to display under recent
  int maxJobs = 10;

  @FXML
  VBox JobCardContainer;

  @FXML
  VBox ExpenseContainer;

  @FXML
  Text WeeklyExpenses;

  @FXML
  Text YTDExpenses;

  @FXML
  Text TotalJobsPendingLabel;

  @FXML
  Text NewJobsWeekly;

  @FXML
  Text JobsCompleted;

  @FXML
  Text WeeklyIncome;

  @FXML
  Text MontlyIncome;

  @FXML
  Text YearlyIncome;

  BorderPane bp;

  @FXML
  Node self;

  ReportsController rc;

  @FXML
  public void initialize() {
    rc = new ReportsController();
    self
      .visibleProperty()
      .addListener(
        new ChangeListener<Boolean>() {
          @Override
          public void changed(
            ObservableValue<? extends Boolean> observable,
            Boolean oldValue,
            Boolean newValue
          ) {
            if (newValue) updatePage(ExpenseContainer);
          }
        }
      );
  }

  public HomeLoader() {
    expenses = new Expenses();
    updateJobs();
  }

  public void updateJobs() {
    recentJobs = new ArrayList<Node>();
    //Get top 10 Jobs by End date
    JobController jc = new JobController();
    ArrayList<JobDAO> recentDAOs = jc.getRecent();

    for (JobDAO job : recentDAOs) {
      try {
        FXMLLoader loader = new FXMLLoader(
          Main.class.getResource("Scenes/Home/HomePageJobCard.fxml")
        );
        Node content = loader.load();
        Job controller = loader.getController();
        controller.setBp(bp);
        controller.setParent(self);
        controller.setSelfRefrence(job);
        job.toJob(controller);
        recentJobs.add(content);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  public Node updatePage(Node origin) {
    ReportDAO report = rc.getReport();
    //Add database calls and updates
    pendingJobsCount = report.getOpenJobs();
    newJobsCount = report.getNewJobs();
    completedJobsCount = report.getCompletedJobs();

    monthlyIncome = report.getMonthlyIncome();
    weeklyIncome = report.getWeeklyIncome();
    yearlyIncome = report.getYearlyIncome();
    //Jobs set
    TotalJobsPendingLabel.setText(String.valueOf(pendingJobsCount));
    NewJobsWeekly.setText(String.valueOf(newJobsCount));
    JobsCompleted.setText(String.valueOf(completedJobsCount));

    //Set Income

    WeeklyIncome.setText("$" + weeklyIncome);
    MontlyIncome.setText("$" + monthlyIncome);
    YearlyIncome.setText("$" + yearlyIncome);

    JobCardContainer.getChildren().clear();
    updateJobs();
    int pos = 0;
    while (pos < maxJobs && pos < recentJobs.size()) {
      JobCardContainer.getChildren().add(pos, recentJobs.get(pos));
      pos++;
    }
    pos = 0;
    //Set Expenses
    WeeklyExpenses.setText("$" + String.valueOf(expenses.getWeekly()));
    YTDExpenses.setText("$" + String.valueOf(expenses.getYTD()));

    ExpenseContainer.getChildren().clear();
    try {
      expenses.getCards(ExpenseContainer);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return (origin);
  }
}
