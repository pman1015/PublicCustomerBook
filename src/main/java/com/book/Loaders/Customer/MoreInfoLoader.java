package com.book.Loaders.Customer;

import com.book.Controllers.CustomerController;
import com.book.Controllers.JobController;
import com.book.Controllers.MachineController;
import com.book.DAOs.CustomerDAO;
import com.book.DAOs.CustomerEquipmentDAO;
import com.book.DAOs.JobDAO;
import com.book.Loaders.Equipment.EquipmentDetailLoader;
import com.book.Loaders.Jobs.JobDetailLoader;
import com.book.Loaders.Jobs.JobHistoryLoader;
import com.book.Main;
import com.book.Models.Equipment.MoreInfoEquipmentCard;
import com.book.Models.Status;
import com.book.SceneController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.bson.types.ObjectId;

public class MoreInfoLoader {

  private CustomersLoader parent;
  private BorderPane bp;
  private Node self;
  FadeTransition fadeIn = new FadeTransition();
  FadeTransition fadeOut = new FadeTransition();

  @FXML
  Text CustomerName;

  @FXML
  TextField CustomerNameEdit;

  @FXML
  Text AliasName;

  @FXML
  TextField AliasNameEdit;

  @FXML
  Text Address;

  @FXML
  TextField AddressEdit;

  @FXML
  Text CustomerRating;

  @FXML
  ChoiceBox<String> CustomerRatingEdit;

  @FXML
  Text PhoneNumber;

  @FXML
  TextField PhoneNumberEdit;

  @FXML
  Button CustomerEdit;

  @FXML
  HBox CustomerEditOptions;

  @FXML
  Text JobNameTile;

  @FXML
  Circle JobTileStatus;

  @FXML
  TextArea JobTileDetails;

  @FXML
  Text CurrentExpenses;

  @FXML
  TextArea NotesTextArea;

  @FXML
  Button NotesEditButton;

  @FXML
  HBox NotesEditConfirm;

  @FXML
  VBox EquipmentCards;

  @FXML
  VBox JobInfo;

  @FXML
  Text NoJob;

  @FXML
  Pane Self;

  boolean CustomerEditState = false;
  String Notes;
  ArrayList<Node> Edits;
  ArrayList<Node> Values;

  CustomerController cc;
  CustomerDAO dao;

  JobDAO currentJob;

  public MoreInfoLoader() {
    fadeIn.setDuration(Duration.millis(1000));
    fadeIn.setFromValue(0);
    fadeIn.setToValue(1);

    fadeOut.setDuration(Duration.millis(1000));
    fadeOut.setFromValue(1);
    fadeOut.setToValue(0);
  }

  @FXML
  public void initialize() {
    cc = new CustomerController();
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
              Update();
            }
          }
        }
      );

    setNotes();

    ArrayList<String> options = new ArrayList<>();
    int value = 1;
    while (value <= 5) {
      options.add(String.valueOf(value));
      value++;
    }
    CustomerRatingEdit.setItems(FXCollections.observableList(options));
    Edits =
      new ArrayList<>(
        Arrays.asList(
          CustomerNameEdit,
          AliasNameEdit,
          AddressEdit,
          CustomerRatingEdit,
          PhoneNumberEdit
        )
      );
    Values =
      new ArrayList<>(
        Arrays.asList(
          CustomerName,
          AliasName,
          Address,
          PhoneNumber,
          CustomerRating
        )
      );
    for (Node n : Edits) n.setOpacity(0);
    CustomerEditOptions.setOpacity(0);
  }

  public void setEquipment() {
    //Initialise the arrayList that stores equipment cards
    EquipmentCards.getChildren().clear();
    if (dao == null) return;

    if (dao.getMachineIDs() == null || dao.getMachineIDs().size() < 1) return;

    MachineController mc = new MachineController();
    for (CustomerEquipmentDAO machine : mc.findAll(dao.getMachineIDs())) {
      FXMLLoader eq = new FXMLLoader(
        Main.class.getResource("Scenes/Customers/EquipmentCard.fxml")
      );
      try {
        Node card = eq.load();
        MoreInfoEquipmentCard controller = eq.getController();
        controller.setParent(Self);
        controller.setSelfRefrence(machine);
        EquipmentCards.getChildren().add(card);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void setNotes() {
    NotesEditConfirm.visibleProperty().set(false);
    NotesTextArea.setText(Notes);
  }

  public void toggleNotesEdit() {
    //Toggle the edit visibility and the editability of the text area
    if (NotesEditButton.isVisible()) {
      NotesEditButton.visibleProperty().set(false);
      NotesEditConfirm.visibleProperty().set(true);
      NotesTextArea.setEditable(true);
    } else {
      NotesEditButton.visibleProperty().set(true);
      NotesEditConfirm.visibleProperty().set(false);
      NotesTextArea.setEditable(false);
      NotesTextArea.setText(dao.getNotes());
    }
  }

  public void ApplyNotesEdit() {
    Notes = NotesTextArea.getText();
    dao.setNotes(NotesTextArea.getText());
    if (cc.updateCustomer(dao)) {
      toggleNotesEdit();
    }
  }

  public void setJobTile() {
    //If no recent job return
    if (currentJob == null) {
      System.out.println("No Job");
      return;
    }
    //Place Holder for a database call to get the Job title

    setJobTitle(currentJob.getJobName());

    //Set the Job status indicator based on status
    setJobStatus(currentJob.getStatus());

    //Set the current expenses
    CurrentExpenses.setText(
      String.valueOf(currentJob.getBill().getBillTotal())
    );

    //Set the description
    JobTileDetails.setText(currentJob.getDetails());
  }

  public void setJobStatus(String status) {
    new Status(status, JobTileStatus).getIndicator();
  }

  public void setJobTitle(String title) {
    int fontsize = 32;
    if (title.length() > 24) {
      fontsize = 20;
      if (title.length() > 32) {
        fontsize = 16;
        if (title.length() > 48) {
          title = title.substring(0, 40) + "...";
        }
      }
    }
    //JobNameTile.setStyle("-fx-font-size: "+fontsize+ "px");
    JobNameTile.setFont(
      Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, fontsize)
    );
    JobNameTile.setText(title);
  }

  //Reset the edits to reflect the current state of the
  public void UpdateEdits() {
    CustomerNameEdit.setText(CustomerName.getText());
    CustomerRatingEdit.setValue(CustomerRating.getText());
    AliasNameEdit.setText(AliasName.getText());
    PhoneNumberEdit.setText(PhoneNumber.getText());
    AddressEdit.setText(Address.getText());
  }

  public void toggleEdit() {
    CustomerEditState = !CustomerEditState;
    UpdateEdits();

    //Fade in or out Edits and values
    ArrayList<Node> toFadeIn = Values;
    ArrayList<Node> toFadeOut = Edits;
    if (CustomerEditState) {
      toFadeIn = Edits;
      toFadeOut = Values;
    }
    int pos = 0;
    while (pos < Edits.size()) {
      FadeTransition FadeIn = new FadeTransition(
        Duration.millis(1000),
        toFadeIn.get(pos)
      );
      FadeIn.setToValue(1);
      FadeIn.setFromValue(0);
      FadeIn.play();
      toFadeIn.get(pos).toFront();
      FadeTransition FadeOut = new FadeTransition(
        Duration.millis(1000),
        toFadeOut.get(pos)
      );
      FadeOut.setToValue(0);
      FadeOut.setFromValue(1);
      FadeOut.play();
      pos++;
    }

    //Disable the hidden text fields
    CustomerNameEdit.disableProperty().set(!CustomerEditState);
    AliasNameEdit.disableProperty().set(!CustomerEditState);
    AddressEdit.disableProperty().set(!CustomerEditState);
    CustomerRatingEdit.disableProperty().set(!CustomerEditState);
    PhoneNumberEdit.disableProperty().set(!CustomerEditState);

    //Toggle the visibiliy of the edit button and the accept and clear buttons
    Node makeVisible = CustomerEdit;
    Node hide = CustomerEditOptions;
    if (CustomerEditState) {
      makeVisible = CustomerEditOptions;
      hide = CustomerEdit;
    }
    FadeTransition fo = new FadeTransition(Duration.millis(1000), hide);
    fo.setToValue(0);
    fo.setFromValue(1);
    FadeTransition fi = new FadeTransition(Duration.millis(1000), makeVisible);
    fi.setToValue(1);
    fi.setFromValue(0);
    fo.play();
    fi.play();

    return;
  }

  public void setParent(CustomersLoader parent) {
    this.parent = parent;
  }

  public void setBP(BorderPane bp) {
    this.bp = bp;
  }

  public void setName(String Name) {
    CustomerName.setText(Name);
    Update();
  }

  public void Update() {
    //lookup customer in the database
    dao = cc.getDAO(CustomerName.getText());
    if (dao.equals(null)) {
      return;
    }
    //Set the customer info
    AliasName.setText(dao.getAlias());
    CustomerRating.setText(String.valueOf(dao.getRating()));
    Address.setText(dao.getAddress());
    if (dao.getPhoneNumber() != null) {
      PhoneNumber.setText(dao.getPhoneNumber());
    }

    //Set Notes Value
    NotesTextArea.setText(dao.getNotes());

    //Set Most recent Jobs
    if (dao.getJobIDs() == null || dao.getJobIDs().size() < 1) {
      NoJob.setVisible(true);
      JobInfo.setVisible(false);
    } else {
      NoJob.setVisible(false);
      JobInfo.setVisible(true);
      //Get the most recent Job from the list
      JobController jc = new JobController();
      currentJob = jc.getMostRecent(dao.getJobIDs());
      setJobTile();
    }
    setEquipment();
  }

  public void setSelf(Node n) {
    self = n;
  }

  public Node getSelf() {
    return self;
  }

  public void Back() {
    parent.reset();
  }

  public void setCenter(Node n) {
    parent.setCenter(n);
  }

  public void JobHistory() {
    FXMLLoader JH = new FXMLLoader(
      Main.class.getResource("Scenes/Jobs/JobHistory.fxml")
    );
    try {
      Node content = JH.load();
      ((JobHistoryLoader) JH.getController()).setParent(this);
      ((JobHistoryLoader) JH.getController()).setBP(bp);
      ((JobHistoryLoader) JH.getController()).setHistory(
          CustomerName.getText()
        );
      parent.setCenter(content);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void EquipmentDetails() {
    FXMLLoader ED = new FXMLLoader(
      Main.class.getResource("Scenes/Equipment/EquipmentDetails.fxml")
    );
    try {
      Node content = ED.load();
      EquipmentDetailLoader controller = ED.getController();

      controller.setParent(self);
      controller.setRefrence(dao);

      bp.setCenter(content);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void ApplyCustomerChanges() {
    CustomerName.setText(CustomerNameEdit.getText());
    dao.setName(CustomerNameEdit.getText());

    AliasName.setText(AliasNameEdit.getText());
    dao.setAlias(AliasNameEdit.getText());

    Address.setText(AddressEdit.getText());
    dao.setAddress(AddressEdit.getText());

    CustomerRating.setText(CustomerRatingEdit.getValue());
    dao.setRating(Integer.parseInt(CustomerRatingEdit.getValue()));

    PhoneNumber.setText(PhoneNumberEdit.getText());
    dao.setPhoneNumber(PhoneNumberEdit.getText());

    if (cc.updateCustomer(dao)) {
      toggleEdit();
    }
  }

  @FXML
  void GoToJob() {
    //Guard statment for if there is no current Job
    if (currentJob == null) return;

    FXMLLoader JDLoader = new FXMLLoader(
      Main.class.getResource("Scenes/Jobs/JobDetail.fxml")
    );
    try {
      Node Content = JDLoader.load();
      JobDetailLoader controller = JDLoader.getController();
      controller.setParent(Self);
      controller.setSelfRefrence(currentJob);
      controller.setBp((BorderPane) ((Self).getScene().getRoot()));
      Self.setVisible(false);
      ((BorderPane) (Self.getScene().getRoot())).setCenter(Content);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
