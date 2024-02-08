package com.book.Loaders.Jobs;

import com.book.Controllers.CustomerController;
import com.book.Controllers.EquipmentController;
import com.book.Controllers.JobController;
import com.book.Controllers.MachineController;
import com.book.Controllers.ReportsController;
import com.book.DAOs.CustomerDAO;
import com.book.DAOs.CustomerEquipmentDAO;
import com.book.DAOs.JobDAO;
import com.book.Utilities.WarningSender;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
public class JobCreate {

  @FXML
  private ComboBox<String> CustomerNameOptions;

  @FXML
  private TextArea JobDescription;

  @FXML
  private ComboBox<String> MachineOptions;

  @FXML
  private HBox Title;

  @FXML
  private HBox CustomerNameContainer;

  @FXML
  private TextField JobNameEdit;

  @FXML
  private Pane JobCreate;

  @FXML
  private Label JobNameError;

  @FXML
  private Label MachineError;

  @FXML
  private Label CustomerNameError;

  ArrayList<Node> fields;
  ArrayList<Label> errorLabels;

  Node parent;
  String customerNameString;
  CustomerController cc;
  MachineController mc;
  EquipmentController ec;
  JobController jc;

  @FXML
  public void initialize() {
    fields =
      new ArrayList<>(
        Arrays.asList(JobNameEdit, MachineOptions, CustomerNameOptions)
      );
    errorLabels =
      new ArrayList<>(
        Arrays.asList(JobNameError, MachineError, CustomerNameError)
      );
    cc = new CustomerController();
    mc = new MachineController();
    ec = new EquipmentController();
    jc = new JobController();

    CustomerNameOptions
      .valueProperty()
      .addListener((observedValue, oldValue, newValue) -> {
        updateChoices(newValue);
      });
    errorLabels.forEach(e -> {
      e.setVisible(false);
    });
  }

  @FXML
  void CreateJob(ActionEvent event) {
    //Check the validity of the fields
    if (inValid()) return;

    //get the customer dao
    CustomerDAO customerDAO = cc.getDAO(CustomerNameOptions.getValue());

    ObjectId equipmentID = ec.getID(MachineOptions.getValue());
    ObjectId machineID = mc.findCustomerMachineID(
      equipmentID,
      CustomerNameOptions.getValue()
    );

    ObjectId jobID = jc.createAndAdd(
      JobNameEdit.getText(),
      machineID,
      equipmentID,
      CustomerNameOptions.getValue(),
      JobDescription.getText()
    );
    if (customerDAO.getJobIDs() == null) customerDAO.setJobIDs(
      new ArrayList<ObjectId>()
    );
    customerDAO.getJobIDs().add(jobID);
    cc.updateCustomer(customerDAO);
    //Update the new Jobs report 
    new ReportsController().newJob(jobID);

    //Set the last worked on date for the machine to now
    mc.workOnMachine(machineID);
    Cancel(event);
  }

  @FXML
  void Cancel(ActionEvent event) {
    parent.setVisible(true);
    (
      (BorderPane) (((Button) event.getSource()).getScene().getRoot())
    ).setCenter(parent);
  }

  public boolean inValid() {
    boolean fail = false;
    //Check the validity of each field
    int pos = 0;
    while (pos < fields.size()) {
      boolean result = valueCheck(fields.get(pos), errorLabels.get(pos));
      fail = fail || result;
      pos++;
    }
    return fail;
  }

  public boolean valueCheck(Node n, Label error) {
    boolean fail = false;
    //if the node is disabled then selection is locked and cannot be invalid
    if (n.isDisable()) {
      return false;
    }
    //if the node is a text field check if its empty
    if (n.getClass().equals(TextField.class)) {
      if (
        ((TextField) n).getText() == null ||
        ((TextField) n).getText().length() < 1
      ) {
        fail = true;
        error.setVisible(true);
        error.setText("Field Cannot be blank");
      } else {
        error.setVisible(false);
      }
    } else {
      //If the value is a comboBox make sure the selected value is inside the items
      if (n.getClass().equals(ComboBox.class)) {
        if (!((ComboBox) n).getItems().contains(((ComboBox) n).getValue())) {
          fail = true;
          error.setVisible(true);
          error.setText("Value must be selected from the choices provided");
        } else {
          error.setVisible(false);
        }
      }
    }

    return fail;
  }

  public boolean setCustomerNameString(String customerName) {
    this.customerNameString = customerName;
    updateFields();
    //Should add a warning if you are about to try to make a job for someone with no machines
    if (MachineOptions.getItems().size() < 1) {
      new WarningSender()
        .sendWarning(
          "The Customer you are accessing has no machines added and cannot have any jobs",
          "acknowledgement"
        );
      return false;
    }
    return true;
  }

  public void updateFields() {
    //If no customer name is provided set the machine options to all machines
    if (customerNameString == null) {
      setAllMachines();
      cc = new CustomerController();
      CustomerNameOptions.setItems(
        FXCollections.observableArrayList(cc.allCustomerNames())
      );
    } else {
      CustomerNameOptions.setValue(customerNameString);
      CustomerNameOptions.setDisable(true);
    }
  }

  public void updateChoices(String customerName) {
    MachineOptions.getItems().clear();
    //Find the customer
    CustomerDAO customer = cc.getDAO(customerNameString);
    //Get their machines
    ArrayList<ObjectId> allMachines = customer.getMachineIDs();
    //Get the model numbers
    ArrayList<CustomerEquipmentDAO> machineDAOs = mc.findAll(allMachines);
    //Add all of the names
    ArrayList<String> modelNumbers = new ArrayList<>();
    machineDAOs.forEach(e -> {
      modelNumbers.add(e.getModelNumber());
    });
    MachineOptions.setItems(FXCollections.observableArrayList(modelNumbers));
  }

  public void setAllMachines() {
    MachineOptions.getItems().clear();
    ec = new EquipmentController();
    MachineOptions.setItems(
      FXCollections.observableArrayList(ec.getAllModels())
    );
  }
}
